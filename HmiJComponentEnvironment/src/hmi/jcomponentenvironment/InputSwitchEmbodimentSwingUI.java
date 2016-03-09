package hmi.jcomponentenvironment;

import hmi.environmentbase.InputSwitchEmbodiment;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;

/**
 * Combobox input selector for the VJointSwitchEmbodiment
 * @author hvanwelbergen
 *
 */
public class InputSwitchEmbodimentSwingUI 
{
    private JComboBox<String> switchBox;
    
    public JComponent getJComponent()
    {
        return switchBox;
    }
    
    public InputSwitchEmbodimentSwingUI(String id, final InputSwitchEmbodiment vjSwitch)
    {
        try
        {
            SwingUtilities.invokeAndWait(new Runnable()
            {
                public void run()
                {
                    switchBox = new JComboBox<>();
                    for (String input : vjSwitch.getInputs())
                    {
                        switchBox.addItem(input);
                    }
                    switchBox.addActionListener(new ActionListener()
                    {
                        public void actionPerformed(ActionEvent e)
                        {
                            vjSwitch.selectInput(switchBox.getSelectedItem().toString());
                        }
                    });
                }
            });
        }
        catch (InvocationTargetException | InterruptedException e)
        {
            throw new RuntimeException(e);
        }
    }    
}
