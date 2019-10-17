/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package circuitmaker;

/**
 *
 * @author Aaron
 */
public class InputLayer{
    InputGate[] gates = new InputGate[8];
    
    public InputLayer(int n){
        gates = new InputGate[n];
    }
    
    public void feed(boolean[] inputs){
        for(int i = 0; i < gates.length; i++){
            gates[i].calculate();
        }
    }
}
