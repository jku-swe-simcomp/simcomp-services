import json
import socket
from controller import Robot

"""
Controller for the Webots Robot
To move the Robot connect to 127.0.0.1:10020
Inputs 
"""

robot = Robot()

# get the time step of the current world
timestep = 250
max_speed = 6.28
HOST = '127.0.0.1'
PORT = 10010
tcp = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
orig = (HOST, PORT)
tcp.bind(orig)
tcp.listen()
tcp.settimeout(100)

# Init the motors - the Ned robot is a 6-axis robot arm
# You can find the name of the rotationalMotors is the device parameters of each HingeJoints
m1 = robot.getDevice('joint_1')
m2 = robot.getDevice('joint_2')
m3 = robot.getDevice('joint_3')
m4 = robot.getDevice('joint_4')
m5 = robot.getDevice('joint_5')
m6 = robot.getDevice('joint_6')
m7 = robot.getDevice('gripper::left')


def initial_position():
    # Set the motor velocity
    # First we make sure that every joints are at their initial positions
    m1.setPosition(0)
    m2.setPosition(0)
    m3.setPosition(0)
    m4.setPosition(0)
    m5.setPosition(0)
    m6.setPosition(0)
    m7.setPosition(0)
    json_result = {
        "result": "success",
        "axis1": 0,
        "axis2": 0,
        "axis3": 0,
        "axis4": 0,
        "axis5": 0,
        "axis6": 0
    }
    print('Setting all positions to 0')
    return '{"result": "success"}\n'


initial_position()

# Set the motors speed. Here we set it to 1 radian/second
m1.setVelocity(5)
m2.setVelocity(5)
m3.setVelocity(5)
m4.setVelocity(5)
m5.setVelocity(5)
m6.setVelocity(5)
m7.setVelocity(5)

print('New Robot was set up, waiting for client to connect')


def wait_for_connection():
    connection, adaptor = None, None
    while connection is None:
        try:
            connection, adaptor = tcp.accept()
            print('Connected by', adaptor)
            return connection, adaptor
        except (socket.timeout, socket.error):
            robot.step(timestep)


def get_value(motor):
    motor.getPositionSensor().enable(100)
    return motor.getPositionSensor().getValue()


def set_axis(set_axis_input):
    print('Executing set_axis')
    try:
        axis = set_axis_input.get("axis")
        position = float(set_axis_input.get("value"))
        print(f'Setting axis {axis} to value {position}')
        if axis == 1:
            print('test')
            m1.setPosition(position)
            print('test')
        elif axis == 2:
            m2.setPosition(position)
        elif axis == 3:
            m3.setPosition(position)
        elif axis == 4:
            m4.setPosition(position)
        elif axis == 5:
            m5.setPosition(position)
        elif axis == 6:
            m6.setPosition(position)
    except ValueError:
        print("Invalid position value received.")
        return '{"result":"The axis could not be set"}\n'
    return '{"result": "success"}\n'


def adjust_axis(adjust_axis_input):
    print('Executing adjust_axis')
    try:
        axis = adjust_axis_input.get("axis")
        position = float(adjust_axis_input.get("value"))
        if axis == 1:
            current_value = get_value(m1)
            print(f'Setting axis 1 to value {current_value + position}')
            m1.setPosition(current_value + position)
        elif axis == 2:
            current_value = get_value(m2)
            print(f'Setting axis 2 to value {current_value + position}')
            m2.setPosition(current_value + position)
        elif axis == 3:
            current_value = get_value(m3)
            print(f'Setting axis 3 to value {current_value + position}')
            m3.setPosition(current_value + position)
        elif axis == 4:
            current_value = get_value(m4)
            print(f'Setting axis 4 to value {current_value + position}')
            m4.setPosition(current_value + position)
        elif axis == 5:
            current_value = get_value(m5)
            print(f'Setting axis 5 to value {current_value + position}')
            m5.setPosition(current_value + position)
        elif axis == 6:
            current_value = get_value(m6)
            print(f'Setting axis 6 to value {current_value + position}')
            m6.setPosition(current_value + position)
    except ValueError:
        print("Invalid position value received.")
        return '{"result":"The axis could not be adjusted"}\n'
    return '{"result": "success"}\n'


def get_position_json():
    val1 = get_value(m1)
    val2 = get_value(m2)
    val3 = get_value(m3)
    val4 = get_value(m4)
    val5 = get_value(m5)
    val6 = get_value(m6)
    positions = [val1, val2, val3, val4, val5, val6]
    json_result = {
        "result": "success",
        "positions": positions
    }
    result = json.dumps(json_result)+'\n'
    print(f'Returning: {result}')
    return result


while robot.step(timestep) != -1:
    # wait for a client
    con, client = wait_for_connection()
    msg = con.recv(1024)
    if not msg:
        conTmp, clienteTmp = wait_for_connection()
        if conTmp is not None:
            con = conTmp
            client = clienteTmp
        continue
    print('Processing message')
    command = msg.decode('UTF-8')

    response = '\n'
    # Split the received message by comma
    try:
        print('Received command: ' + command)
        json_data = json.loads(command)
        operation = json_data.get("operation")
        if operation == 'adjust_axis':
            response = adjust_axis(json_data)
        elif operation == 'set_axis':
            response = set_axis(json_data)
        elif operation == 'initial_position':
            response = initial_position()
        elif operation == 'get_position':
            response = get_position_json()

    except json.JSONDecodeError as e:
        print('json could not be pared')
        print(e.msg)
        response = '{"result":"The JSON file could not be parsed"}\n'

    print('Returning response: ' + response)
    con.sendall(response.encode('utf-8'))

print('Ending client connection ', client)
con.close()
