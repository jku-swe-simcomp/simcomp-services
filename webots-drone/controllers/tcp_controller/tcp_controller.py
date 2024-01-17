import threading

from controller import Robot
import socket
import json


max_speed = 6.28
HOST = '127.0.0.1'
PORT = 10020
tcp = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
orig = (HOST, PORT)
tcp.bind(orig)
tcp.listen()
tcp.settimeout(100)


def clamp(value, value_min, value_max):
    return min(max(value, value_min), value_max)


class Mavic (Robot):
    # Constants, empirically found.
    K_VERTICAL_THRUST = 68.5  # with this thrust, the drone lifts.
    # Vertical offset where the robot actually targets to stabilize itself.
    K_VERTICAL_OFFSET = 0.6
    K_VERTICAL_P = 3.0        # P constant of the vertical PID.
    K_ROLL_P = 50.0           # P constant of the roll PID.
    K_PITCH_P = 30.0          # P constant of the pitch PID.

    MAX_YAW_DISTURBANCE = 0.4
    MAX_PITCH_DISTURBANCE = -1
    # Precision between the target position and the robot position in meters
    target_precision = 0.5

    def __init__(self):
        Robot.__init__(self)

        self.time_step = int(self.getBasicTimeStep())

        # Get and enable devices.
        self.camera = self.getDevice("camera")
        self.camera.enable(self.time_step)
        self.imu = self.getDevice("inertial unit")
        self.imu.enable(self.time_step)
        self.gps = self.getDevice("gps")
        self.gps.enable(self.time_step)
        self.gyro = self.getDevice("gyro")
        self.gyro.enable(self.time_step)

        self.front_left_motor = self.getDevice("front left propeller")
        self.front_right_motor = self.getDevice("front right propeller")
        self.rear_left_motor = self.getDevice("rear left propeller")
        self.rear_right_motor = self.getDevice("rear right propeller")
        self.camera_pitch_motor = self.getDevice("camera pitch")
        self.camera_pitch_motor.setPosition(0.7)
        motors = [self.front_left_motor, self.front_right_motor,
                  self.rear_left_motor, self.rear_right_motor]
        for motor in motors:
            motor.setPosition(float('inf'))
            motor.setVelocity(1)

        self.current_pose = 6 * [0]  # X, Y, Z, yaw, pitch, roll
        self.target_position = [0, 0, 0]
        self.target_index = 0
        self.target_altitude = 0

    def set_position(self, pos):
        self.current_pose = pos

    def set_altitude(self, new_altitude):
        print(f'Setting altitude to new high: {new_altitude}')
        self.target_altitude = new_altitude

    def adjust_altitude(self, new_altitude):
        print(f'Setting altitude to new high: {self.target_altitude + new_altitude}')
        self.target_altitude = self.target_altitude + new_altitude


def wait_for_connection():
    connection, adaptor = None, None
    while connection is None:
        try:
            connection, adaptor = tcp.accept()
            print('Connected by', adaptor)
            return connection, adaptor
        except (socket.timeout, socket.error):
            robot.step(robot.time_step)


def tcp_socket():
    while robot.step(robot.time_step) != -1:
        print('Listen for Query')
        con, client = wait_for_connection()
        msg = con.recv(1024)
        if not msg:
            con_tmp, cliente_tmp = wait_for_connection()
            if con_tmp is not None:
                con = con_tmp
                client = cliente_tmp
            continue
        print('Processing message')
        command = msg.decode('UTF-8')

        response = '\n'
        # Split the received message by comma
        try:
            print('Received command: ' + command)
            json_data = json.loads(command)
            operation = json_data.get("operation")
            if operation == 'set_altitude':
                value = json_data.get("value")
                robot.set_altitude(value)
                response = '{"result": "success"}\n'
            elif operation == 'adjust_altitude':
                value = json_data.get("value")
                robot.adjust_altitude(value)
                response = '{"result": "success"}\n'
            else :
                response = '{"result": "unsupported operation"}'

        except json.JSONDecodeError as e:
            print('json could not be pared')
            print(e.msg)
            response = '{"result":"invalid json"}\n'

        print('Returning response: ' + response)
        con.sendall(response.encode('utf-8'))
        print('Ending client connection ', client)
        con.close()


def fly_drone():
    while robot.step(robot.time_step) != -1:
        roll, pitch, yaw = robot.imu.getRollPitchYaw()
        x_pos, y_pos, altitude = robot.gps.getValues()
        roll_acceleration, pitch_acceleration, _ = robot.gyro.getValues()
        robot.set_position([x_pos, y_pos, altitude, roll, pitch, yaw])

        roll_input = robot.K_ROLL_P * clamp(roll, -1, 1) + roll_acceleration
        pitch_input = robot.K_PITCH_P * clamp(pitch, -1, 1) + pitch_acceleration
        clamped_difference_altitude = clamp(robot.target_altitude - altitude + robot.K_VERTICAL_OFFSET, -1.0, 1.0)
        vertical_input = robot.K_VERTICAL_P * pow(clamped_difference_altitude, 3.0)

        front_left_motor_input = robot.K_VERTICAL_THRUST + vertical_input + pitch_input - roll_input
        front_right_motor_input = robot.K_VERTICAL_THRUST + vertical_input + pitch_input + roll_input
        rear_left_motor_input = robot.K_VERTICAL_THRUST + vertical_input - pitch_input - roll_input
        rear_right_motor_input = robot.K_VERTICAL_THRUST + vertical_input - pitch_input + roll_input

        robot.front_left_motor.setVelocity(front_left_motor_input)
        robot.front_right_motor.setVelocity(-front_right_motor_input)
        robot.rear_left_motor.setVelocity(-rear_left_motor_input)
        robot.rear_right_motor.setVelocity(rear_right_motor_input)


robot = Mavic()
robot.target_altitude = 0.50

print(f'Running at host {HOST} and port {PORT}.')

flying_thread = threading.Thread(target=fly_drone)
flying_thread.start()

tcp_thread = threading.Thread(target=tcp_socket)
tcp_thread.start()
