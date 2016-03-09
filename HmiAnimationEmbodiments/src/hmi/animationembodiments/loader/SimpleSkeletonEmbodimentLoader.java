package hmi.animationembodiments.loader;

import hmi.animationembodiments.SimpleSkeletonEmbodiment;
import hmi.environmentbase.EmbodimentLoader;
import hmi.environmentbase.Environment;
import hmi.environmentbase.Loader;
import hmi.xml.XMLTokenizer;

import java.io.IOException;

import lombok.Getter;
import lombok.Setter;

/**
 * Provides a Loader wrapper around a SimpleSkeletonEmbodiment to be able to use it as a part in a CompoundLoader.
 * readXML functionality is currently not provided. 
 * @author hvanwelbergen
 */
public class SimpleSkeletonEmbodimentLoader implements EmbodimentLoader
{
    @Getter
    private String id;
    
    @Setter
    private SimpleSkeletonEmbodiment embodiment;
    
    public SimpleSkeletonEmbodimentLoader(String id, SimpleSkeletonEmbodiment embodiment)
    {
        this.id = id;
        this.embodiment = embodiment;
    }
    
    @Override
    public void readXML(XMLTokenizer tokenizer, String loaderId, String vhId, String vhName, Environment[] environments,
            Loader... requiredLoaders) throws IOException
    {
        this.id = loaderId;        
    }

    @Override
    public void unload()
    {
                
    }

    @Override
    public SimpleSkeletonEmbodiment getEmbodiment()
    {
        return embodiment;
    }

}
