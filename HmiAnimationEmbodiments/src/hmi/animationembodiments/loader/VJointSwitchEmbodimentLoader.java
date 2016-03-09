package hmi.animationembodiments.loader;

import hmi.animationembodiments.SimpleSkeletonEmbodiment;
import hmi.animationembodiments.SkeletonEmbodiment;
import hmi.animationembodiments.VJointSwitchEmbodiment;
import hmi.environmentbase.CompoundLoader;
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

public class VJointSwitchEmbodimentLoader implements EmbodimentLoader, CompoundLoader
{
    @Getter
    private String id;
    private VJointSwitchEmbodiment embodiment;
    private List<SimpleSkeletonEmbodimentLoader> parts;
    
    @Override
    public void readXML(XMLTokenizer tokenizer, String loaderId, String vhId, String vhName, Environment[] environments,
            Loader... requiredLoaders) throws IOException
    {
        this.id = loaderId;

        SkeletonEmbodiment output = null;
        for (EmbodimentLoader el : ArrayUtils.getClassesOfType(requiredLoaders, EmbodimentLoader.class))
        {
            if (el.getEmbodiment() instanceof SkeletonEmbodiment)
            {
                output = (SkeletonEmbodiment) el.getEmbodiment();
            }
        }

        if (output == null)
        {
            throw new RuntimeException("VJointSwitchEmbodiment requires an EmbodimentLoader containing a SkeletonEmbodiment for its output");
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
        embodiment = new VJointSwitchEmbodiment(loaderId, inputs.getIds(), output.getAnimationVJoint());
        parts = new ArrayList<SimpleSkeletonEmbodimentLoader>();
        for(String partId:inputs.getIds())
        {
            SimpleSkeletonEmbodiment body = new SimpleSkeletonEmbodiment(partId,embodiment.getInput(partId));
            parts.add(new SimpleSkeletonEmbodimentLoader(partId, body));
        }
    }
    
    @Override
    public void unload()
    {

    }

    @Override
    public Collection<SimpleSkeletonEmbodimentLoader> getParts()
    {
        return parts;
    }

    @Override
    public VJointSwitchEmbodiment getEmbodiment()
    {
        return embodiment;
    }

}
