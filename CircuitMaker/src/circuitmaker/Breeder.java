/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package circuitmaker;

/**
 * @author Aaron
 */
public class Breeder {
    Circuit[] nextPopulation;
    private boolean[][] desiredOutputsListing;
    private int inputListNumber;
    private double mutationRate;

    public Breeder(int inputListNumber, boolean[][] desiredOutputsListing, double mutationRate) {
        this.mutationRate = mutationRate;
        this.desiredOutputsListing = desiredOutputsListing;
        this.inputListNumber = inputListNumber;
    }

    Circuit[] breed(int populationSize, Circuit[] currentPopulation) {
        nextPopulation = new Circuit[populationSize];
        selection(currentPopulation);                                           //sets nextPopulation to the selected circuits

        return nextPopulation;
    }

    public Circuit createCircuit() {
        String id;
        String gatesPerLayer = "";
        int g;                                                                  //number of gates in a layer, and later the specified input gate
        int i = (int) Math.ceil(8 * Math.random());                             //number of inputs
        int o = (int) Math.ceil(8 * Math.random());                             //number of outputs
        int l = (int) (9 * Math.random());                                      //number of layers

        id = Integer.toString(i) + "I" + Integer.toString(o) + "O" +
                Integer.toString(l) + "L:";

        for (byte s = 0; s < l; s++) {                                           //for each layer
            g = (int) Math.ceil(8 * Math.random());                             //choose the number of gates
            id += g;
            gatesPerLayer += g + "";
            for (byte t = 0; t < g; t++) {                                       //for each gate
                id += pickUpConnection(s, i, g) + pickUpConnection(s, i, g) +   //pick 2 connections to add to the string 
                        ";";
            }
            id += ":";
        }
        for (byte u = 0; u < o; u++) {
            id += "|" + pickUpConnection((byte) l, i, o);
        }
        System.out.println(id);
        return new Circuit(id, gatesPerLayer, inputListNumber);
    }

    private static String pickUpConnection(byte s, int i, int g) {
        int h;
        String id = "";

        h = (int) ((s + 1) * Math.random());                                    //layer of input; add one to include inputLayer
        if (h == 0) {
            id += "i" + Integer.toString((int) (i * Math.random()));            //now pick an input from the input layer
        } else {
            h -= 1;                                                             //sub1 to allow the 8 layers to return to values 0 through 7
            id += Integer.toString(h);                                          //layer is put into the id
            h = (int) (g * Math.random());                                      //now pick a gate
            id += Integer.toString(h);
        }
        return id;
    }

    void trimCircuit(Circuit c) {
        LogicGate gate;
        Layer layer;

        NEXT:
        for (byte l = (byte) c.layers.length; l >= 0; l++) {
            layer = c.layers[l];
            for (byte g = 0; g < c.layers[l].gates.length; g++) {
                gate = layer.gates[g];
                if (gate.outputGates.equals(null)) {
                    c.NANDcount -= 1;
                    layer.gates[g] = null;
                }
            }
            for (int i = 0; i < layer.gates.length; i++) {
                if (layer.gates[i] != null) continue;
                continue NEXT;
            }
        }
    }

    void grade(Circuit[] currentPopulation, int inputListNumber) {
        for (short c = 0; c < currentPopulation.length; c++) {
            if (currentPopulation[c].fitness == 1000000) {
//                currentPopulation[c].fitness = 1 * currentPopulation[c].NANDcount + 
//                    2 * currentPopulation[c].propagationDelay;
                currentPopulation[c].fitness = 0;
            }
            for (int i = 0; i < currentPopulation[c].outputListListing[inputListNumber].length &&
                    i < desiredOutputsListing[inputListNumber].length; i++) {
                if (currentPopulation[c].outputListListing[inputListNumber][i] ==
                        desiredOutputsListing[inputListNumber][i]) {
                    currentPopulation[c].fitness += 1;
                } else currentPopulation[c].fitness -= 1;
            }
            currentPopulation[c].fitness -= Math.abs(desiredOutputsListing[inputListNumber].length -
                    currentPopulation[c].outputListListing[inputListNumber].length);
        }
    }

    private void selection(Circuit[] currentPopulation) {
        currentPopulation = insertionSort(currentPopulation);

        //selection of top 20%, produces 20%
        //int x = (int) Math.ceil(currentPopulation.length/5);
        int x = 20;
        for (int i = 0; i < 20; i++) {
            nextPopulation[i] = new Circuit(currentPopulation[i].id,
                    currentPopulation[i].gatesPerLayer, inputListNumber);
//        }
//        for (int i = 0; i < 80; i++){
//            nextPopulation[currentPopulation.length - (i + 1)] = createCircuit();
//        }
        }
        //selection of to 30%, produces 60%
        for (int i = 0; i < 30; i++) {
            nextPopulation[x + 2 * i] = mutate(currentPopulation[i]);
            nextPopulation[x + 2 * i + 1] = mutate(currentPopulation[i]);
        }
        //production of 20%
        for (int i = 0; i < 20; i++) {
            nextPopulation[currentPopulation.length - (i + 1)] = createCircuit();
        }
    }

    private Circuit mutate(Circuit c) {
        String x = "";
        int[] m = new int[((int) ((double) c.id.length() * mutationRate))];
        int y = 0;
        int previousLayers = 0;
        //m will be an array of indices which shall be mutated
        for (int i = 0; i < mutationRate; i++) {
            m[i] = (int) (Math.random() * c.id.length());
        }
        for (int i = 0; i < c.id.length() && y < m.length; i++) {
            if (c.id.charAt(i) == ':' && c.id.charAt(i + 1) != '|') {
                previousLayers++;
            }
            if (!(((Character.isDigit(c.id.charAt(i)) || c.id.charAt(i) == 'i') && (i > 7) && m[y] == i))) {
                x += c.id.charAt(i);
            } else {
                if (c.id.charAt(i - 1) == 'i') {
                    x += ((int) (Math.random() * 8)) % Character.getNumericValue(c.id.charAt(0));
                    y++;
                } else if (c.id.charAt(i - 1) == ':') {
                    x += c.id.charAt(i);
                } else if (c.id.charAt(i - 1) == ';' || c.id.charAt(i - 1) == '|') {
                    if (c.id.charAt(i) == 'i') {
                        if (c.id.charAt(4) == '0') {
                            x += 'i';
                        } else {
                            x += ((int) (Math.random() * 8)) % previousLayers;
                        }
                    } else {
                        x += ((int) (Math.random() * 8)) % previousLayers;
                    }
                    y++;
                } else {
                    if (c.id.charAt(i) == 'i') {
                        if (c.id.charAt(4) == '0') {
                            x += 'i';
                        } else {
                            x += ((int) (Math.random() * 8)) % previousLayers;
                        }
                    } else {
                        x += (int) (Math.random() * 8) % previousLayers;
                    }
                    y++;
                }
            }
        }
        for (int i = x.length(); i < c.id.length(); i++) {
            x += c.id.charAt(i);
        }
        Circuit z = new Circuit(x, c.gatesPerLayer, inputListNumber);
        return z;
    }

    private Circuit[] insertionSort(Circuit[] circuitz) {
        Circuit item;
        int hole;
        for (int i = 1; i < circuitz.length; i++) {
            item = circuitz[i];
            hole = i;
            while (hole > 0 && circuitz[hole - 1].fitness < item.fitness) {
                circuitz[hole] = circuitz[hole - 1];
                hole--;
            }
            circuitz[hole] = item;
        }
        return circuitz;
    }


}
