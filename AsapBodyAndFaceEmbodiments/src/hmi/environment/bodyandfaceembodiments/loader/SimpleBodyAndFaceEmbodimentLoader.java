package hmi.environment.bodyandfaceembodiments.loader;

import hmi.environment.bodyandfaceembodiments.SimpleBodyAndFaceEmbodiment;
import hmi.environmentbase.EmbodimentLoader;
import hmi.environmentbase.Environment;
import hmi.environmentbase.Loader;
import hmi.xml.XMLTokenizer;

import java.io.IOException;

import lombok.Getter;
import lombok.Setter;

/**
 * Provides a Loader wrapper around a  SimpleBodyAndFaceEmbodiment to be able to use it as a part in a CompoundLoader.
 * readXML functionality is currently not provided. 
 * @author hvanwelbergen
 */
public class SimpleBodyAndFaceEmbodimentLoader implements EmbodimentLoader
{
    @Getter
    private String id;
    
    @Setter
    private SimpleBodyAndFaceEmbodiment embodiment;
    
    
    public SimpleBodyAndFaceEmbodimentLoader(String id, SimpleBodyAndFaceEmbodiment embodiment)
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
    public SimpleBodyAndFaceEmbodiment getEmbodiment()
    {
        return embodiment;
    }
}
