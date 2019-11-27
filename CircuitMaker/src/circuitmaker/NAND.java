/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package circuitmaker;

/**
 * @author Aaron
 */
public class NAND extends LogicGate {

    public NAND() {
        inputGates = new LogicGate[2];
    }

    public void calculate() {
        for (byte i = 0; i < inputs.length; i++) {
            if (inputs[i]) {
                continue;
            }
            this.output = true;
            return;
        }
        this.output = false;
    }

}
