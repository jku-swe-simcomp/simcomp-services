package at.jku.swe.simcomp.axisconverter.service;

import at.jku.swe.simcomp.commons.adaptor.dto.JointAngleAdjustmentDTO;
import at.jku.swe.simcomp.commons.adaptor.dto.JointPositionDTO;
import at.jku.swe.simcomp.commons.adaptor.dto.PoseDTO;

import java.util.Arrays;

import java.util.LinkedList;
import java.util.List;

public class InverseKinematics {

    public static List<JointPositionDTO> inverseKinematics(PoseDTO position) {
        return new LinkedList<>();

        /*double threshold = Math.PI / 36; // rad
        double[] trials = new double[(int) (2 * Math.PI / threshold)];

        for (int i = 0; i < trials.length; i++) {
            trials[i] = -Math.PI + i * threshold;
        }

        double[] errorVec = new double[0];
        double[][] bestSolution = new double[6][0];
        double[] weightSolution = new double[0];

        int count = 0;

        for (double theta6 : trials) {
            try {
                double[][] P05_T06 = getJointPositionFive(O, theta6);
                double[][] joint123 = getPlanarGeometry(P05_T06[0][0], P05_T06[1][0], P05_T06[2][0] - 103 - 80);
                double[][] T03 = computeT03(joint123);
                double[][] T06 = computeT06(T03, P05_T06[1][0]);

                double[][] joints6Dof = computeJoints456(T06, joint123);
                joints6Dof = getRealMovements(joints6Dof, false);

                // Calculate the error of the computed solutions
            } catch (Exception e) {
                System.out.println("Error was found. Possible a lost imaginary solution. :(");
                // Handle the exception as needed
                return;
            }

            for (double[] joint : joints6Dof) {
                double error = Math.abs(MathUtils.angleDifference(theta6, joint[5]));

                double[][] dirKin = directKinematics(joint);
                double dirKinDiff = Vector3D.distance(new Vector3D(O[0], O[1], O[2]),
                        new Vector3D(dirKin[0][0], dirKin[1][0], dirKin[2][0]));

                if (Math.round(error - threshold, 3) <= 0 && dirKinDiff < 1) {
                    errorVec = Arrays.copyOf(errorVec, errorVec.length + 1);
                    errorVec[errorVec.length - 1] = error;

                    bestSolution = Arrays.copyOf(bestSolution, bestSolution.length + 1);
                    bestSolution[bestSolution.length - 1] = Arrays.copyOf(joint, joint.length);

                    weightSolution = Arrays.copyOf(weightSolution, weightSolution.length + 1);
                    weightSolution[weightSolution.length - 1] = dirKinDiff;
                }

                // Bonus point research
                errorVec = Arrays.copyOf(errorVec, errorVec.length + 1);
                errorVec[errorVec.length - 1] = error;
                // Plotting points or other actions can be added here
                count++;
            }
        }

        // Sorting by closest xyz
        int[] order = MathUtils.argsort(weightSolution);
        double[][] bestSolutionSorted = new double[bestSolution.length][bestSolution[0].length];

        for (int i = 0; i < order.length; i++) {
            bestSolutionSorted[i] = Arrays.copyOf(bestSolution[order[i]], bestSolution[0].length);
        }

        boolean hasSolutions = bestSolutionSorted.length > 0;

        if (!hasSolutions) {
            System.out.println("There are no available solutions!");
            return;
        }

        System.out.println("Found at least " + bestSolutionSorted.length + " solutions.");
        System.out.println("Best inverse kinematic solution with " + weightSolution[order[0]] + " norm error.");

        System.out.println("Best solution :");
        System.out.println(Arrays.toString(bestSolutionSorted[0]));

        // Bonus point
        int n = myKmeans(bestSolutionSorted);

        // Plot error evolution
        // Add plotting code or other visualization here*/
    }
}
