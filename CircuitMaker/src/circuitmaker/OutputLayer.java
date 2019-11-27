/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package circuitmaker;

/**
 * @author aaron.donoho
 */
public class OutputLayer {
    OutputGate[] gates;

    public OutputLayer(int n) {
        gates = new OutputGate[n];
        for (int i = 0; i < n; i++) {
            gates[i] = new OutputGate();
        }
    }

    public void feed(boolean[] inputs) {
        for (int i = 0; i < gates.length; i++) {
            gates[i].calculate();
        }
    }
}
