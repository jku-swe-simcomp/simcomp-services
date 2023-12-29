package at.jku.swe.simcomp.axisconverter.service;

import at.jku.swe.simcomp.commons.adaptor.dto.JointPositionDTO;
import at.jku.swe.simcomp.commons.adaptor.dto.PoseDTO;
import at.jku.swe.simcomp.commons.adaptor.dto.RoboJoint;

import java.util.ArrayList;
import java.util.List;

public class InverseKinematics {

    final static double AXIS_1_MIN = -2.8;
    final static double AXIS_1_MAX = 2.8;
    final static double AXIS_2_MIN = -0.8;
    final static double AXIS_2_MAX = 0.8;
    final static double AXIS_3_MIN = -1.5;
    final static double AXIS_3_MAX = 1.3;
    final static double AXIS_4_MIN = -2;
    final static double AXIS_4_MAX = 2;
    final static double AXIS_5_MIN = -1.5;
    final static double AXIS_5_MAX = 1.5;
    public static List<JointPositionDTO> inverseKinematics(PoseDTO position, int granularity){

        if(granularity > 35 || granularity < 2) {
            throw new IllegalArgumentException("The granularity must be within the bounds 2 and 35");
        }

        if(position == null) {
            throw new IllegalArgumentException("The position may not be null");
        }

        double minimumError = Double.MAX_VALUE;
        double[] bestSolution = new double[]{0,0,0,0,0,0};
        for(int axis1 = 0; axis1 < granularity; axis1 ++){
            for(int axis2 = 0; axis2 < granularity; axis2 ++){
                for(int axis3 = 0; axis3 < granularity; axis3 ++){
                    for(int axis4 = 0; axis4 < granularity; axis4 ++){
                        for(int axis5 = 0; axis5 < granularity; axis5 ++){
                            double[] axisPos = getAxisPos(axis1,
                                    axis2, axis3,
                                    axis4, axis5,
                                    granularity);
                            double error = calculateError(axisPos, position);
                            if(error < minimumError) {
                                minimumError = error;
                                bestSolution = axisPos;
                            }
                        }
                    }
                }
            }
        }
        return jointPositionList(bestSolution);
    }

    private static double calculateError(double[] axisPosition, PoseDTO position){
        PoseDTO axisPose = DirectKinematics.directKinematics(jointPositionList(axisPosition));
        // TODO maybe also consider direction
        double difX = axisPose.getPosition().getX() - position.getPosition().getX();
        double difY = axisPose.getPosition().getY() - position.getPosition().getY();
        double difZ = axisPose.getPosition().getZ() - position.getPosition().getZ();
        return Math.pow(difX, 2)+Math.pow(difY, 2)+Math.pow(difZ, 2);
    }

    private static List<JointPositionDTO> jointPositionList(double[] bestSolution) {
        List<JointPositionDTO> joints = new ArrayList<>(6);
        joints.add(new JointPositionDTO(RoboJoint.AXIS_1, bestSolution[0]));
        joints.add(new JointPositionDTO(RoboJoint.AXIS_2, bestSolution[1]));
        joints.add(new JointPositionDTO(RoboJoint.AXIS_3, bestSolution[2]));
        joints.add(new JointPositionDTO(RoboJoint.AXIS_4, bestSolution[3]));
        joints.add(new JointPositionDTO(RoboJoint.AXIS_5, bestSolution[4]));
        joints.add(new JointPositionDTO(RoboJoint.AXIS_6, bestSolution[5]));
        return joints;
    }

    private static double[] getAxisPos(int axis1, int axis2, int axis3, int axis4, int axis5, int granularity) {
        double axisPos1 = AXIS_1_MIN + axis1*(AXIS_1_MAX-AXIS_1_MIN)/(granularity-1);
        double axisPos2 = AXIS_2_MIN + axis2*(AXIS_2_MAX-AXIS_2_MIN)/(granularity-1);
        double axisPos3 = AXIS_3_MIN + axis3*(AXIS_3_MAX-AXIS_3_MIN)/(granularity-1);
        double axisPos4 = AXIS_4_MIN + axis4*(AXIS_4_MAX-AXIS_4_MIN)/(granularity-1);
        double axisPos5 = AXIS_5_MIN + axis5*(AXIS_5_MAX-AXIS_5_MIN)/(granularity-1);
        return new double[]{axisPos1, axisPos2, axisPos3, axisPos4, axisPos5, 0};
    }
}
