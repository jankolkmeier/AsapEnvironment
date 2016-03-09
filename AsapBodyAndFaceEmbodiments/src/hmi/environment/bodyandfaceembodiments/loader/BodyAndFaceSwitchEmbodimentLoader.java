package hmi.environment.bodyandfaceembodiments.loader;

import hmi.environment.bodyandfaceembodiments.BodyAndFaceEmbodiment;
import hmi.environment.bodyandfaceembodiments.BodyAndFaceSwitchEmbodiment;
import hmi.environmentbase.CompoundLoader;
import hmi.environmentbase.CopyEnvironment;
import hmi.environmentbase.EmbodimentLoader;
import hmi.environmentbase.Environment;
import hmi.environmentbase.InputsLoader;
import hmi.environmentbase.Loader;
import hmi.util.ArrayUtils;
import hmi.xml.XMLScanException;
import hmi.xml.XMLTokenizer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import lombok.Getter;

/**
 * Loader for the BodyAndFaceSwitchEmbodiment
 * @author hvanwelbergen
 *
 */
public class BodyAndFaceSwitchEmbodimentLoader implements EmbodimentLoader, CompoundLoader
{
    @Getter
    private String id;
    private BodyAndFaceSwitchEmbodiment embodiment;
    private List<SimpleBodyAndFaceEmbodimentLoader> parts;
    
    @Override
    public void readXML(XMLTokenizer tokenizer, String loaderId, String vhId, String vhName, Environment[] environments,
            Loader... requiredLoaders) throws IOException
    {
        id = loaderId;
        BodyAndFaceEmbodiment output = null;
        for (EmbodimentLoader el : ArrayUtils.getClassesOfType(requiredLoaders, EmbodimentLoader.class))
        {
            if (el.getEmbodiment() instanceof BodyAndFaceEmbodiment)
            {
                output = (BodyAndFaceEmbodiment) el.getEmbodiment();
            }            
        }
        CopyEnvironment env = ArrayUtils.getFirstClassOfType(environments, CopyEnvironment.class);
        if (env == null)
        {
            throw new RuntimeException("BodyAndFaceSwitchEmbodimentLoader requires a CopyEnvironment");
        }
        
        if (output == null)
        {
            throw new RuntimeException("BodyAndFaceSwitchEmbodimentLoader requires an EmbodimentLoader containing a BodyAndFaceEmbodiment for its output");
        }
        
        
        InputsLoader inputs = new InputsLoader();        
        while (tokenizer.atSTag())
        {
            String tag = tokenizer.getTagName();
            switch (tag)
            {
            case InputsLoader.XMLTAG:
                inputs.readXML(tokenizer);                
                break;
            default:
                throw new XMLScanException("Invalid tag " + tag);
            }
        }
       
        embodiment = new BodyAndFaceSwitchEmbodiment(loaderId, inputs.getIds(), output);
        
        parts = new ArrayList<SimpleBodyAndFaceEmbodimentLoader>();
        for(String partId:inputs.getIds())
        {
            parts.add(new SimpleBodyAndFaceEmbodimentLoader(partId,embodiment.getInput(partId)));
        }        
        env.addCopyEmbodiment(embodiment);
    }

    @Override
    public void unload()
    {
                
    }

    @Override
    public Collection<? extends Loader> getParts()
    {
        return parts;
    }

    @Override
    public BodyAndFaceSwitchEmbodiment getEmbodiment()
    {
        return embodiment;
    }

}
