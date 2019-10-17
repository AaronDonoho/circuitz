/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package circuitmaker;

/*
 *
 * @author Aaron
 */
public class Layer {
    LogicGate[] gates;
    
    public Layer(int n){
        gates = new LogicGate[n];
    }
    
    public void feed(boolean[] inputs){
        for(int i = 0; i < gates.length; i++){
            gates[i].calculate();
        }
    }
}
