/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package circuitmaker;

/**
 *
 * @author Aaron
 */
public class InputGate extends LogicGate {
    
    boolean input;
    
    public void calculate(){
        this.output = this.input;
    }
    
}
