package hmi.animationembodiments.loader;

import java.io.IOException;

import hmi.animationembodiments.SkeletonEmbodiment;
import hmi.animationembodiments.VJointSynchronizedCopyEmbodiment;
import hmi.environmentbase.Embodiment;
import hmi.environmentbase.EmbodimentLoader;
import hmi.environmentbase.Environment;
import hmi.environmentbase.Loader;
import hmi.util.ArrayUtils;
import hmi.xml.XMLTokenizer;

/**
 * Loads a VJointSynchronizedCopyEmbodiment from XML
 * @author hvanwelbergen
 * 
 */
public class VJointSynchronizedCopyEmbodimentLoader implements EmbodimentLoader
{
    private String id;
    private VJointSynchronizedCopyEmbodiment embodiment;

    @Override
    public String getId()
    {
        return id;
    }

    @Override
    public void readXML(XMLTokenizer tokenizer, String loaderId, String vhId, String vhName, Environment[] environments,
            Loader... requiredLoaders) throws IOException
    {
        id = loaderId;
        SkeletonEmbodiment src = null;

        for (EmbodimentLoader el : ArrayUtils.getClassesOfType(requiredLoaders, EmbodimentLoader.class))
        {
            if (el.getEmbodiment() instanceof SkeletonEmbodiment)
            {
                src = (SkeletonEmbodiment) el.getEmbodiment();
            }
        }
        if (src == null)
        {
            throw new RuntimeException(
                    "VJointSynchronizedCopyEmbodimentLoader requires an EmbodimentLoader containing a SkeletonEmbodiment");
        }
        embodiment = new VJointSynchronizedCopyEmbodiment(id, src.getAnimationVJoint());
    }

    @Override
    public void unload()
    {

    }

    @Override
    public Embodiment getEmbodiment()
    {
        return embodiment;
    }

}
