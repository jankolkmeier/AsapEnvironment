package hmi.jcomponentenvironment.loader;

import hmi.environmentbase.EmbodimentLoader;
import hmi.environmentbase.Environment;
import hmi.environmentbase.InputSwitchEmbodiment;
import hmi.environmentbase.Loader;
import hmi.jcomponentenvironment.InputSwitchEmbodimentSwingUI;
import hmi.util.ArrayUtils;
import hmi.xml.XMLTokenizer;

import java.io.IOException;

import lombok.Getter;

/**
 * Loader for the VJointSwitchEmbodimentSwingUI
 * @author hvanwelbergen
 * 
 */
public class InputSwitchEmbodimentSwingUILoader implements Loader
{
    @Getter
    private String id;

    @Override
    public void readXML(XMLTokenizer tokenizer, String loaderId, String vhId, String vhName, Environment[] environments,
            Loader... requiredLoaders) throws IOException
    {
        InputSwitchEmbodiment switchEmbodiment = null;
        for (EmbodimentLoader ebl : ArrayUtils.getClassesOfType(requiredLoaders, EmbodimentLoader.class))
        {
            if (ebl.getEmbodiment() instanceof InputSwitchEmbodiment)
            {
                switchEmbodiment = (InputSwitchEmbodiment) ebl.getEmbodiment();
            }
        }
        if (switchEmbodiment == null)
        {
            throw new RuntimeException("VJointSwitchEmbodimentSwingUILoader requires a VJointSwitchEmbodimentLoader.");
        }

        JComponentEmbodimentLoader jceLoader = ArrayUtils.getFirstClassOfType(requiredLoaders, JComponentEmbodimentLoader.class);
        if (jceLoader == null || jceLoader.getEmbodiment() == null)
        {
            throw new RuntimeException("VJointSwitchEmbodimentSwingUILoader requires a JComponentEmbodimentLoader.");
        }

        InputSwitchEmbodimentSwingUI ui = new InputSwitchEmbodimentSwingUI(id, switchEmbodiment);
        jceLoader.getEmbodiment().addJComponent(ui.getJComponent());
    }

    @Override
    public void unload()
    {

    }
}
