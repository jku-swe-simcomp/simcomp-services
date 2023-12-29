package at.jku.swe.simcomp.axisconverter.service;

import at.jku.swe.simcomp.commons.adaptor.dto.*;


import java.util.ArrayList;
import java.util.List;

public class DirectKinematics {

    public static PoseDTO directKinematics(List<JointPositionDTO> axes) {

        double[] axesValues = validateAxes(axes);

        applyBounds(axesValues);

        List<double[][]> matrices = getMultiplicationMatrices(axesValues);

        double[][] totalTMatrix = matrices.get(0);

        for(int i = 1; i < matrices.size(); i++) {
            totalTMatrix = matrixMultiplication(totalTMatrix, matrices.get(i));
        }

        double[] coords = matrixVectorMultiplication(totalTMatrix, new double[]{0, 0, 0, 1});
        double[] orientation = getOrientation(totalTMatrix);

        PositionDTO positionDTO = new PositionDTO(coords[0], coords[1], coords[2]);
        OrientationDTO orientationDTO = new OrientationDTO(orientation[0], orientation[1], orientation[2]);
        return new PoseDTO(positionDTO, orientationDTO);
    }

    private static List<double[][]> getMultiplicationMatrices(double[] axes) {
        List<double[][]> matrices = new ArrayList<>();

        matrices.add(new double[][]{
                {Math.cos(axes[0]), -Math.sin(axes[0]), 0, 0},
                {Math.sin(axes[0]), Math.cos(axes[0]), 0, 0},
                {0, 0, 1, 103},
                {0, 0, 0, 1}
        });

        matrices.add(new double[][]{
                {Math.cos(axes[1]), 0, Math.sin(axes[1]), 0},
                {0, 1, 0, 0},
                {-Math.sin(axes[1]), 0, Math.cos(axes[1]), 80},
                {0, 0, 0, 1}
        });

        matrices.add(new double[][]{
                {Math.cos(axes[2]), 0, Math.sin(axes[2]), 0},
                {0, 1, 0, 0},
                {-Math.sin(axes[2]), 0, Math.cos(axes[2]), 210},
                {0, 0, 0, 1}
        });

        matrices.add(new double[][]{
                {1, 0, 0, 41.5},
                {0, Math.cos(axes[3]), -Math.sin(axes[3]), 0},
                {0, Math.sin(axes[3]), Math.cos(axes[3]), 30},
                {0, 0, 0, 1}
        });

        matrices.add(new double[][]{
                {Math.cos(axes[4]), 0, Math.sin(axes[4]), 180},
                {0, 1, 0, 0},
                {-Math.sin(axes[4]), 0, Math.cos(axes[4]), 0},
                {0, 0, 0, 1}
        });

        matrices.add(new double[][]{
                {1, 0, 0, 23.7},
                {0, Math.cos(axes[5]), -Math.sin(axes[5]), 0},
                {0, Math.sin(axes[5]), Math.cos(axes[5]), -5.5},
                {0, 0, 0, 1}
        });

        return matrices;
    }

    private static double[] validateAxes(List<JointPositionDTO> axes) {
        long axesCount = axes.stream()
                .map(JointPositionDTO::getJoint)
                .distinct()
                .count();
        if(axesCount != 6) {
            throw new IllegalArgumentException("Not all Axis values are set. Please set the axes 1-6.");
        }

        double[] axesValues = new double[6];
        axes.forEach(a -> axesValues[a.getJoint().getIndex()-1] = a.getRadians());
        return axesValues;
    }

    private static double[][] matrixMultiplication(double[][] matrixA, double[][] matrixB) {
        int rowsA = matrixA.length;
        int colsA = matrixA[0].length;
        int colsB = matrixB[0].length;

        double[][] result = new double[rowsA][colsB];

        for (int i = 0; i < rowsA; i++) {
            for (int j = 0; j < colsB; j++) {
                for (int k = 0; k < colsA; k++) {
                    result[i][j] += matrixA[i][k] * matrixB[k][j];
                }
            }
        }

        return result;
    }

    private static double[] matrixVectorMultiplication(double[][] matrix, double[] vector) {
        int rows = matrix.length;
        int cols = matrix[0].length;

        if (cols != vector.length) {
            throw new IllegalArgumentException("Matrix and vector dimensions do not match for multiplication.");
        }

        double[] result = new double[rows];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                result[i] += matrix[i][j] * vector[j];
            }
        }

        return result;
    }

    private static double[] getOrientation(double[][] matrix) {
        double alpha = 0;
        double beta = Math.atan2(Math.sqrt(matrix[0][2] * matrix[0][2] + matrix[1][2] * matrix[1][2]), matrix[2][2]);
        double gamma;
        double betaRounde = Math.round(beta*1000)/1000.0;

        if (betaRounde == Math.round(1000*Math.PI / 2)/1000.0) {
            gamma = Math.atan2(matrix[1][0], matrix[0][0]);
        } else if (betaRounde == Math.round(1000*-Math.PI / 2)/1000.0) {
            gamma = -Math.atan2(matrix[1][0], matrix[0][0]);
        } else if (betaRounde == 0) {
            gamma = 0;
            alpha = Math.cos(beta) * Math.atan2(matrix[1][0], matrix[0][0]);
        } else {
            alpha = Math.atan2(matrix[1][2] / Math.sin(beta), matrix[1][0] / Math.sin(beta));
            gamma = Math.atan2(-matrix[2][1] / Math.sin(beta), matrix[2][0] / Math.sin(beta));
        }

        return new double[]{alpha, beta, gamma};
    }

    private static void applyBounds(double[] axes) {
        double[][] rangeRotation = {
                {-160, 160},
                {-45.8, 45.8},
                {-86, 74.4},
                {-114.5, 114.5},
                {-86, 86},
                {-143, 143}
        };

        for (int i = 0; i < axes.length; i++) {
            axes[i] = boundAngle(axes[i], rangeRotation[i][0], rangeRotation[i][1]);
        }
    }

    private static double boundAngle(double x, double lowerBound, double upperBound) {
        x =  Math.round(x * 10000) / 10000.0;
        lowerBound = Math.round(lowerBound * 10000) / 10000.0;
        upperBound = Math.round(upperBound * 10000) / 10000.0;

        x = Math.max(x, lowerBound);
        return Math.min(x, upperBound);
    }
}
