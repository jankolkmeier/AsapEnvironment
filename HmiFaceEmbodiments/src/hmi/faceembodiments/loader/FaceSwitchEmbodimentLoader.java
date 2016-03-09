package hmi.faceembodiments.loader;

import hmi.environmentbase.CompoundLoader;
import hmi.environmentbase.EmbodimentLoader;
import hmi.environmentbase.Environment;
import hmi.environmentbase.InputsLoader;
import hmi.environmentbase.Loader;
import hmi.faceembodiments.FaceEmbodiment;
import hmi.faceembodiments.FaceSwitchEmbodiment;
import hmi.faceembodiments.SimpleFaceEmbodiment;
import hmi.util.ArrayUtils;
import hmi.xml.XMLScanException;
import hmi.xml.XMLTokenizer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import lombok.Getter;

/**
 * Loader for the FaceSwitchEmbodiment
 * @author hvanwelbergen
 *
 */
public class FaceSwitchEmbodimentLoader implements EmbodimentLoader, CompoundLoader
{
    @Getter
    private String id;
    private FaceSwitchEmbodiment embodiment;
    private List<SimpleFaceEmbodimentLoader> parts;
    
    @Override
    public void readXML(XMLTokenizer tokenizer, String loaderId, String vhId, String vhName, Environment[] environments,
            Loader... requiredLoaders) throws IOException
    {
        this.id = loaderId;        
        
        FaceEmbodiment output = null;
        for (EmbodimentLoader el : ArrayUtils.getClassesOfType(requiredLoaders, EmbodimentLoader.class))
        {
            if (el.getEmbodiment() instanceof FaceEmbodiment)
            {
                output = (FaceEmbodiment) el.getEmbodiment();
            }
        }

        if (output == null)
        {
            throw new RuntimeException("VJointSwitchEmbodiment requires an EmbodimentLoader containing a FaceEmbodiment for its output");
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
        
        embodiment = new FaceSwitchEmbodiment(loaderId, inputs.getIds(), output.getFaceController());
        parts = new ArrayList<SimpleFaceEmbodimentLoader>();
        for(String partId:inputs.getIds())
        {
            SimpleFaceEmbodiment fe = new SimpleFaceEmbodiment(partId, embodiment.getInput(partId));
            parts.add(new SimpleFaceEmbodimentLoader(partId, fe));
        }
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
    public FaceSwitchEmbodiment getEmbodiment()
    {
        return embodiment;
    }
    
}
