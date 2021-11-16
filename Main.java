package com.aco;

public class Main {
    public static void main(String[] args) {
        CitiesData citiesData = new CitiesData(300);
        System.out.println(("Number of cities = " + citiesData.cities.length));

        int totalNumberBees = 100;
        int numberInactive = 20;
        int numberActive = 50;
        int numberScout = 30;
        int maxNumberVisits = 100;
        int maxNumberCycles = 3460;

        Hive hive = new Hive(totalNumberBees,
                numberInactive, numberActive, numberScout, maxNumberVisits,
                maxNumberCycles, citiesData);

        boolean doProgressBar = true;
        hive.Solve(doProgressBar);
    }
}
