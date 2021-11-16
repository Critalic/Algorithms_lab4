package com.aco;

import java.util.Random;

class CitiesData {
    public final int[][] cities;

    public CitiesData(int matrixSize) {
        this.cities = generate(matrixSize);
    }

    public int Distance(int firstCity, int secondCity) {
        return cities[firstCity][secondCity];
    }

//    public double ShortestPathLength() {
//        return 1.0 * (this.cities.Length - 1);
//    }

//    public override string ToString() {
        //        for(int i=0; i<size; i++) { //TODO for testing
//            for(int b=0; b<size; b++) {
//                System.out.print(matrix[i][b] + " ");
//            }
//            System.out.println();
//        }
//    }

    private static int[][] generate(int size) {
        Random random = new Random();
        int[][] matrix = new int[size][size];
        for(int i=0; i<size; i++) {
            for(int b=0; b<size; b++) {
                if(b==i) matrix[i][b] =0;
                else {
                    matrix[i][b] =random.nextInt(150);
                }
            }
        }

        for(int i=0; i<size; i++) { //TODO for testing
            for(int b=0; b<size; b++) {
                System.out.print(matrix[i][b] + " ");
            }
            System.out.println();
        }

        return matrix;
    }
}
