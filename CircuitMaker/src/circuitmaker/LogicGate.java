/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package circuitmaker;

/**
 * @author Aaron
 */
public class LogicGate {
    LogicGate[] inputGates;
    LogicGate[] outputGates = new LogicGate[72];
    boolean[] inputs = new boolean[2];
    boolean output;

    public void retrieveInputs() {
        for (byte i = 0; i < inputs.length; i++) {
            inputs[i] = inputGates[i].output;
        }
    }

    public void calculate() {
    }

}
