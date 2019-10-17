/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package circuitmaker;

import javax.swing.JOptionPane;

public class CircuitMaker {

    short generation;
    short populationSize = 100;
    short maxGen;
    Circuit[] currentPopulation;
    Breeder breeder;
    //a single input list must be no greater than 8 booleans
    boolean[][] inputListListing = {{false, false, false, false}, {false, false, false, true}, {false, false, true, false}, {false, true, false, false}, {true, false, false, false}, {false, false, true, true}, {false, true, false, true}, {true, false, false, true}, {false, true, true, false}, {true, false, true, false}, {true, true, false, false}, {false, true, true, true}, {true, false, true, true}, {true, true, false, true}, {true, true, true, false}, {true, true, true, true}};
    //a single output list must be no greater tha 8 booleans; additionally,
    //all output lists must be of equal size.
    boolean[][] desiredOutputListing = {{false, false, false}, {false, false, true}, {false, false, true}, {false, false, true}, {false, false, true}, {false, true, false}, {false, true, false}, {false, true, false}, {false, true, false}, {false, true, false}, {false, true, false}, {false, true, true}, {false, true, true}, {false, true, true}, {false, true, true}, {true, false, false}};
    
    public void generateFirst(short maxGen, double mutationRate){
        generation = 0;
        this.maxGen = maxGen;
        currentPopulation = new Circuit[populationSize];
        breeder = new Breeder(inputListListing.length, desiredOutputListing, mutationRate);
        //populationSize = Short.valueOf(cFrame.getPopulation_Text());
        //first generation is wasted (not graded)
        for (int n = 0; n < populationSize; n++){
            currentPopulation[n] = breeder.createCircuit();
        }
        System.out.println("________________________________________________");
        feedThem();
        currentPopulation = breeder.breed(populationSize, currentPopulation);
    }
    
    public String[] generateNext(){
        
        //more generations!
        for (; generation < maxGen; generation++){
            System.out.println("________________________________________________");
            feedThem();
            currentPopulation = breeder.breed(populationSize, currentPopulation);
        }
        
        feedThem();
        
        String message1 = "Given inputs: \n";
        for(int i = 0; i < inputListListing.length; i++){
            message1 += (i + 1) + ": ";
            for(int j = 0; j < inputListListing[i].length; j++){
                message1 += inputListListing[i][j] + " ";
            }
            message1 += "\n";
        }
        String message2 = "The desired outputs were:\n";
        for(int i = 0; i < desiredOutputListing.length; i++){
            message2 += (i + 1) + ": ";
            for(int j = 0; j < desiredOutputListing[i].length; j++){
                message2 += desiredOutputListing[i][j] + " ";
            }
            message2 += "\n";
        }
        String message3 = "The best circuit so far is:\n" + breeder.nextPopulation[0].id +
                " \nwith a fitness of " + 
                breeder.nextPopulation[0].fitness + 
                " \nand outputs: \n\n";
        for (int i = 0; i < breeder.nextPopulation[0].outputListListing.length; i++){ //could cause error because of length? careful!
            message3 += (i + 1) + ": ";
            for (int j = 0; j < breeder.nextPopulation[0].outputListListing[i].length; j++){
                message3 += breeder.nextPopulation[0].outputListListing[i][j] + " ";
            }
            message3 += "\n";
        }
        
        String[] strings = {message1, message2, message3};
        
        return strings;
    }
    
    public void feedThem(){
        for (int m = 0; m < inputListListing.length; m++){
            for(int n = 0; n < populationSize; n++){
                currentPopulation[n].propagate(inputListListing[m], m);            //every circuit is fed the same input
            }
            breeder.grade(currentPopulation, m);                                   //and then graded
        }
    }
    
}
