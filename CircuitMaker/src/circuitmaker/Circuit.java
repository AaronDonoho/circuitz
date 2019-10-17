/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package circuitmaker;
import java.math.*;
import java.util.Iterator;

public class Circuit {
    String id;
    short generation;
    int fitness;
    short NANDcount;
    int propagationDelay;
    boolean[] outputs;
    boolean[][] outputListListing;
    Layer[] layers;
    InputLayer inputLayer;
    OutputLayer outputLayer;
    int layerCount;
    int inputCount;
    int outputCount;
    String gatesPerLayer;
    
    public Circuit(String id, String gatesPerLayer, int inputListNumber){
        this.fitness = 1000000;
        this.id = id;
        this.NANDcount = 0;
        this.gatesPerLayer = gatesPerLayer;
        this.inputCount = Character.getNumericValue(id.charAt(0));
        this.outputCount = Character.getNumericValue(id.charAt(2));
        this.outputs = new boolean[outputCount];
        this.layerCount = Character.getNumericValue(id.charAt(4));
        
        this.outputListListing = new boolean[inputListNumber][];
        for (int i = 0; i < inputListNumber; i++){
            this.outputListListing[i] = new boolean[outputCount];
        }
        
        layers = new Layer[layerCount];
        inputLayer = new InputLayer(inputCount);
        outputLayer = new OutputLayer(outputCount);
        
        for (int i = 0; i < inputCount; i++){
             inputLayer.gates[i] = new InputGate();
        }
        
        
        for (int i = 0; i < layerCount; i++){
            layers[i] = new Layer( ( (int) (gatesPerLayer.charAt(i) ) ) - 48 );
            NANDcount += ((int)(gatesPerLayer.charAt(i))) - 48 ;
        }
        
        int l = -1;  //THIS layer
        int g = 0;  //THIS gate
        int w = -1; //layer 1 to connect to
        int x = -1; //gate 1 to connect to
        int y = -1; //layer 2 to connect to
        int z = -1; //gate 2 to connect to
        int o = 0;  //output gate
        int q = 0;  //connection to THIS gate (0 or 1)
        for(int k = 6; k < id.length(); k++){
            //if it's a digit, put in an empty variable.
            if (Character.isDigit(id.charAt(k))){
                //if it's the second input, place in y and z instead of w and x
                if (q == 1){
                    if (y == -1){
                        y = ((int) id.charAt(k)) - 48;                                  
                    }
                    else if (z == -1){          
                        z = ((int) id.charAt(k)) - 48;
                    }
                }else
                if (w == -1){
                    w = ((int) id.charAt(k)) - 48;
                }
                else if (x == -1){
                    x = ((int) id.charAt(k)) - 48;
                }        
                else if (y == -1){      
                    y = ((int) id.charAt(k)) - 48;                                  
                }
                else if (z == -1){          
                    z = ((int) id.charAt(k)) - 48;
                }
            }
            else{
                switch(id.charAt(k)){
                    //i specifies connection to input layer
                    case 'i':   layers[l].gates[g].inputGates[q] = inputLayer.gates[((int) id.charAt(k + 1)) - 48];
                                //if k + 2 is ;, then this is the last input of the current gate
                                if(id.charAt(k + 2) == ';' && w != -1){
                                    layers[l].gates[g].inputGates[q] = layers[w].gates[x%layers[w].gates.length];
                                    layers[l].gates[g].inputGates[q + 1] = inputLayer.gates[((int) id.charAt(k + 1)) - 48];
                                    w = x = -1;
                                }
                                if(id.charAt(k + 2) == ';'){g++; k++; q = -1;}
                                k++;
                                q++;
                                break;
                    //; denotes end of a gate's inputs
                    case ';':   q = 0;
                                //gate, 1st or 2nd connect, first value, second value, third value, fourth value
                                //if it's a 2-input affair, (ie neither connects to the input layer), do this.
                                if (w != -1){
                                    layers[l].gates[g].inputGates[q] = layers[w].gates[x%layers[w].gates.length];
                                    if(y != -1){
                                        layers[l].gates[g].inputGates[q + 1] = layers[y].gates[z%layers[y].gates.length];
                                    }
                                    w = x = y = z = -1;
                                }
                                //if it's a 1-input affair, do this instead.
                                if (y != -1){
                                    layers[l].gates[g].inputGates[q + 1] = layers[y].gates[z%layers[y].gates.length];
                                }
                                g++;
                                break; 
                    //: denotes end of a layer's gates
                    case ':':   l++;
                                if (id.charAt(k+1) != 'i' && id.charAt(k + 1) != '|'){
                                    for(int i = 0; i < ((int) id.charAt(k + 1)) - 48; i++){
                                        layers[l].gates[i] = new NAND();
                                    }
                                    k++;
                                }           
                                else{
                                    
                                }
                                g = 0;
                                break;
                    //| denotes end an output-layer gate's inputs
                    case '|':   if(id.charAt(k + 1) == 'i'){
                                    outputLayer.gates[o].inputGate = 
                                            inputLayer.gates[((int) id.charAt(k + 2)) - 48];
                                }else{
                                    outputLayer.gates[o].inputGate = 
                                        layers[((int) id.charAt(k + 1)) - 48]
                                        .gates[(((int) id.charAt(k + 2)) - 48)%layers[((int) id.charAt(k + 1)) - 48]
                                        .gates.length];
                                }
                                k+=2;
                                o++;
                                break;
                        
                    default:    break;
                }
            }
        }
    }
    
    public void propagate(boolean[] InputList, int inputListNumber){
        for (byte i = 0; i < InputList.length && i < inputCount; i++){
            inputLayer.gates[i].input = InputList[i];
            inputLayer.gates[i].calculate();
        }
        for (byte l = 0; l < layers.length; l++){
            for (byte g = 0; g < layers[l].gates.length; g++){
                layers[l].gates[g].retrieveInputs();
                layers[l].gates[g].calculate();
            }
        }
        for (byte o = 0; o < outputCount; o++){
            outputLayer.gates[o].retrieveInputs();
            outputLayer.gates[o].calculate();
            outputListListing[inputListNumber][o] = outputLayer.gates[o].output;
        }
        
    }
    
    public void findPDelay(){
        this.propagationDelay = this.layers.length;
    }
}
