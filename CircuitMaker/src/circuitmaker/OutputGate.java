/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package circuitmaker;

/**
 *
 * @author aaron.donoho
 */
public class OutputGate extends LogicGate{
    
    boolean input;
    boolean output;
    LogicGate inputGate;
    
    public void calculate(){
        this.output = this.input;
    }
    
    public void retrieveInputs()
    {
        this.input = inputGate.output;
    }
    
}
