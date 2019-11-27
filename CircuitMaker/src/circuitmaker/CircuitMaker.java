/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package circuitmaker;

public class CircuitMaker {

    private int generation = 0;
    private int maxGen;
    private Circuit[] currentPopulation;
    private Breeder breeder;
    //a single input list must be no greater than 8 booleans
    private boolean[][] inputListListing = {{false, false, false, false}, {false, false, false, true}, {false, false, true, false}, {false, true, false, false}, {true, false, false, false}, {false, false, true, true}, {false, true, false, true}, {true, false, false, true}, {false, true, true, false}, {true, false, true, false}, {true, true, false, false}, {false, true, true, true}, {true, false, true, true}, {true, true, false, true}, {true, true, true, false}, {true, true, true, true}};
    //a single output list must be no greater tha 8 booleans; additionally,
    //all output lists must be of equal size.
    private boolean[][] desiredOutputListing = {{false, false, false}, {false, false, true}, {false, false, true}, {false, false, true}, {false, false, true}, {false, true, false}, {false, true, false}, {false, true, false}, {false, true, false}, {false, true, false}, {false, true, false}, {false, true, true}, {false, true, true}, {false, true, true}, {false, true, true}, {true, false, false}};

    public void generateFirst(int maxGen, double mutationRate, int populationSize) {
        this.maxGen = maxGen;
        currentPopulation = new Circuit[populationSize];
        breeder = new Breeder(inputListListing.length, desiredOutputListing, mutationRate);
        //populationSize = Short.valueOf(cFrame.getPopulation_Text());
        //first generation is wasted (not graded)
        for (int n = 0; n < currentPopulation.length; n++) {
            currentPopulation[n] = breeder.createCircuit();
        }
        System.out.println("________________________________________________");
        feedInputs();
        currentPopulation = breeder.breed(populationSize, currentPopulation);
    }

    public String[] generateNext(int populationSize) {

        //more generations!
        for (; generation < maxGen; generation++) {
            System.out.println("________________________________________________");
            feedInputs(currentPopulation, inputListListing);
            currentPopulation = breeder.breed(populationSize, currentPopulation);
        }

        feedInputs(currentPopulation, inputListListing);
        gradeThem(breeder, currentPopulation, inputListListing);

        StringBuilder message1 = new StringBuilder("Given inputs: \n");
        for (int i = 0; i < inputListListing.length; i++) {
            message1.append(i + 1).append(": ");
            for (int j = 0; j < inputListListing[i].length; j++) {
                message1.append(inputListListing[i][j]).append(" ");
            }
            message1.append("\n");
        }

        StringBuilder message2 = new StringBuilder("The desired outputs were:\n");
        for (int i = 0; i < desiredOutputListing.length; i++) {
            message2.append(i + 1).append(": ");
            for (int j = 0; j < desiredOutputListing[i].length; j++) {
                message2.append(desiredOutputListing[i][j]).append(" ");
            }
            message2.append("\n");
        }

        StringBuilder message3 = new StringBuilder("The best circuit so far is:\n" + breeder.nextPopulation[0].id +
                " \nwith a fitness of " +
                breeder.nextPopulation[0].fitness +
                " \nand outputs: \n\n");

        for (int i = 0; i < breeder.nextPopulation[0].outputListListing.length; i++) { //could cause error because of length? careful!
            message3.append(i + 1).append(": ");
            for (int j = 0; j < breeder.nextPopulation[0].outputListListing[i].length; j++) {
                message3.append(breeder.nextPopulation[0].outputListListing[i][j]).append(" ");
            }
            message3.append("\n");
        }

        String[] strings = {message1.toString(), message2.toString(), message3.toString()};

        return strings;
    }

    private static void feedInputs(Circuit[] circuits, boolean[][] inputs) {
        for (int m = 0; m < inputs.length; m++) {
            for (int n = 0; n < circuits.length; n++) {
                circuits[n].propagate(inputs[m], m);
            }
        }
    }

    private static void gradeThem(Breeder breeder, Circuit[] circuits, boolean[][] inputs) {
        for (int m = 0; m < inputs.length; m++) {
            breeder.grade(circuits, m);
        }
    }

}
