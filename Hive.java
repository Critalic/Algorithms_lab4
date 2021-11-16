package com.aco;


import java.util.Random;

public class Hive {
        static Random random = null;

        public CitiesData citiesData;

        public int totalNumberBees;
        public int numberInactive;
        public int numberActive;
        public int numberScout;

        public int maxNumberCycles;
        public int maxNumberVisits;

        public double probPersuasion = 0.90;
        public double probMistake = 0.01;

        public Bee[] bees;
        public int[] bestMemoryMatrix;
        public double bestMeasureOfQuality;
        public int[] indexesOfInactiveBees;


//        public override string ToString() { . . . }

        public Hive(int totalNumberBees, int numberInactive,
                    int numberActive, int numberScout, int maxNumberVisits,
                    int maxNumberCycles, CitiesData citiesData) {
            random = new Random(0);

            this.totalNumberBees = totalNumberBees;
            this.numberInactive = numberInactive;
            this.numberActive = numberActive;
            this.numberScout = numberScout;
            this.maxNumberVisits = maxNumberVisits;
            this.maxNumberCycles = maxNumberCycles;

            this.citiesData = citiesData;

            this.bees = new Bee[totalNumberBees];
            this.bestMemoryMatrix = GenerateRandomMemoryMatrix();
            this.bestMeasureOfQuality =
                    MeasureOfQuality(this.bestMemoryMatrix);

            this.indexesOfInactiveBees = new int[numberInactive];

            for (int i = 0; i < totalNumberBees; ++i) {
                int currStatus;
                if (i < numberInactive) {
                    currStatus = 0; // inactive
                    indexesOfInactiveBees[i] = i;
                }
                else if (i < numberInactive + numberScout)
                    currStatus = 2; // scout
                else
                    currStatus = 1; // active

                int[] randomMemoryMatrix = GenerateRandomMemoryMatrix();
                double mq = MeasureOfQuality(randomMemoryMatrix);
                int numberOfVisits = 0;

                bees[i] = new Bee(currStatus,
                        randomMemoryMatrix, mq, numberOfVisits);

                if (bees[i].measureOfQuality < bestMeasureOfQuality) {
                    System.arraycopy (bees[i].memoryMatrix, 0,
                            this.bestMemoryMatrix, 0, bees[i].memoryMatrix.length);
                    this.bestMeasureOfQuality = bees[i].measureOfQuality;
                }
            }
        }

        public int[] GenerateRandomMemoryMatrix() {
            int [] result = new int [this.citiesData.cities.length];
            for(int i=0; i< result.length; i++)  result[i] = i;
            for (int i = 0; i < result.length; i++) {
                int r = random.nextInt( result.length-i) +i;
                int temp = result[r];
                result[r] = result[i];
                result[i] = temp;
            }
            return result;
        }

        public int[] GenerateNeighborMemoryMatrix(int[] memoryMatrix) {
            int[] result = new int[memoryMatrix.length];
            System.arraycopy (memoryMatrix, 0,
                    result, 0, memoryMatrix.length);

            int ranIndex = random.nextInt(result.length);
            int adjIndex;
            if (ranIndex == result.length - 1)
                adjIndex = 0;
            else
                adjIndex = ranIndex + 1;

            int tmp = result[ranIndex];
            result[ranIndex] = result[adjIndex];
            result[adjIndex] = tmp;  return result;
        }

        public double MeasureOfQuality(int[] memoryMatrix) {
            double answer = 0.0;
            for (int i = 0; i < memoryMatrix.length - 1; ++i) {
                int c1 = memoryMatrix[i];
                int c2 = memoryMatrix[i + 1];
                double d = this.citiesData.Distance(c1, c2);
                answer += d;
            }
            return answer;
        }

        public void Solve(boolean doProgressBar) {
            boolean pb = doProgressBar;
            int numberOfSymbolsToPrint = 10;
            int increment = this.maxNumberCycles / numberOfSymbolsToPrint;
            if (pb) System.out.println("\nEntering SBC Traveling Salesman Problem algorithm main processing loop\n");
            if (pb) System.out.println("Progress: |==========|");
            if (pb) System.out.println("           ");
            int cycle = 0;
            
            while (cycle < this.maxNumberCycles) {
                for (int i = 0; i < totalNumberBees; ++i) {
                    if (this.bees[i].status == 1)
                        ProcessActiveBee(i);
                    else if (this.bees[i].status == 2)
                        ProcessScoutBee(i);
                    else if (this.bees[i].status == 0)
                        ProcessInactiveBee(i);
                }
                ++cycle;

                if (pb && cycle % increment == 0)
                    System.out.println("^");
            }
            int solutionValue =0;
            for(int i=0; i< this.bestMemoryMatrix.length-1; i++) {
                System.out.print(bestMemoryMatrix[i] + " -> ");
                solutionValue+= citiesData.Distance(bestMemoryMatrix[i], bestMemoryMatrix[i+1]);
            }
            System.out.println(bestMemoryMatrix[bestMemoryMatrix.length-1]);
            System.out.println(solutionValue);

            if (pb) System.out.println("");
        }

        private void ProcessActiveBee(int i) {
            int[] neighbor = GenerateNeighborMemoryMatrix(bees[i].memoryMatrix);
            double neighborQuality = MeasureOfQuality(neighbor);
            double prob = random.nextDouble();
            boolean memoryWasUpdated = false;
            boolean numberOfVisitsOverLimit = false;

            if (neighborQuality < bees[i].measureOfQuality) { // better
                if (prob < probMistake) { // mistake
                    ++bees[i].numberOfVisits;
                    if (bees[i].numberOfVisits > maxNumberVisits)
                        numberOfVisitsOverLimit = true;
                }
                else { // No mistake
                    System.arraycopy (neighbor, 0,
                            bees[i].memoryMatrix, 0, neighbor.length);
                    bees[i].measureOfQuality = neighborQuality;
                    bees[i].numberOfVisits = 0;
                    memoryWasUpdated = true;
                }
            }
            else { // Did not find better neighbor
                if (prob < probMistake) { // Mistake
                    System.arraycopy (neighbor, 0,
                            bees[i].memoryMatrix, 0, neighbor.length);
                    bees[i].measureOfQuality = neighborQuality;
                    bees[i].numberOfVisits = 0;
                    memoryWasUpdated = true;
                }
                else { // No mistake
                    ++bees[i].numberOfVisits;
                    if (bees[i].numberOfVisits > maxNumberVisits)
                        numberOfVisitsOverLimit = true;
                }
            }

            if (numberOfVisitsOverLimit) {
                bees[i].status = 0;
                bees[i].numberOfVisits = 0;
                int x = random.nextInt(numberInactive);
                bees[indexesOfInactiveBees[x]].status = 1;
                indexesOfInactiveBees[x] = i;
            }
            else if (memoryWasUpdated) {
                if (bees[i].measureOfQuality < this.bestMeasureOfQuality) {
                    System.arraycopy (bees[i].memoryMatrix, 0,
                            this.bestMemoryMatrix, 0, bees[i].memoryMatrix.length);
                    this.bestMeasureOfQuality = bees[i].measureOfQuality;
                }
                DoWaggleDance(i);
            }
            else {
                return;
            }
        }

        private void ProcessScoutBee(int i) {
            int[] randomFoodSource = GenerateRandomMemoryMatrix();
            double randomFoodSourceQuality =
                    MeasureOfQuality(randomFoodSource);
            if (randomFoodSourceQuality < bees[i].measureOfQuality) {
                System.arraycopy (randomFoodSource, 0,
                        bees[i].memoryMatrix, 0, randomFoodSource.length);
                bees[i].measureOfQuality = randomFoodSourceQuality;
                if (bees[i].measureOfQuality < bestMeasureOfQuality) {
                    System.arraycopy (bees[i].memoryMatrix, 0,
                            this.bestMemoryMatrix, 0, bees[i].memoryMatrix.length);
                    this.bestMeasureOfQuality = bees[i].measureOfQuality;
                }
                DoWaggleDance(i);
            }
        }

        private void ProcessInactiveBee(int i) {

        }

        private void DoWaggleDance(int i) {
            for (int ii = 0; ii < numberInactive; ++ii) {
                int b = indexesOfInactiveBees[ii];
                if (bees[i].measureOfQuality < bees[b].measureOfQuality) {
                    double p = random.nextDouble();
                    if (this.probPersuasion > p) {
                        System.arraycopy (bees[i].memoryMatrix, 0,
                                bees[b].memoryMatrix, 0, bees[i].memoryMatrix.length);
                        bees[b].measureOfQuality = bees[i].measureOfQuality;
                    }
                }
            }
        }

}
