package hmi.environmentbase;

import java.util.Set;

/**
 * An embodiment that acts as an InputSwitch, redirecting input from a selected 
 * input to its output. 
 * @author hvanwelbergen
 *
 */
public interface InputSwitchEmbodiment extends CopyEmbodiment
{
    Set<String>getInputs();
    void selectInput(String name);
}
