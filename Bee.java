package com.aco;

import java.lang.reflect.Array;
import java.util.Arrays;

public class Bee {
    public int status;
    public int[] memoryMatrix;
    public double measureOfQuality;
    public int numberOfVisits;

    public Bee(int status, int[] memoryMatrix,
               double measureOfQuality, int numberOfVisits) {
        this.status = status;
        this.memoryMatrix = new int[memoryMatrix.length];
        System.arraycopy (memoryMatrix, 0, this.memoryMatrix, 0, memoryMatrix.length);
        this.measureOfQuality = measureOfQuality;
        this.numberOfVisits = numberOfVisits;
    }

    public String toString() {
        String s = "";
        s += "Status = " + this.status + "\n";
        s += " Memory = " + "\n";
        for (int i = 0; i < this.memoryMatrix.length-1; ++i)
            s += this.memoryMatrix[i] + "->";
        s += this.memoryMatrix[this.memoryMatrix.length-1] + "\n";
        s += " Quality = " + this.measureOfQuality;
        s += " Number visits = " + this.numberOfVisits;
        return s;
    }
}
