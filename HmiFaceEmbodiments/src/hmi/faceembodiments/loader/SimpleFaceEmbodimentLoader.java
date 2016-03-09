package hmi.faceembodiments.loader;

import hmi.environmentbase.EmbodimentLoader;
import hmi.environmentbase.Environment;
import hmi.environmentbase.Loader;
import hmi.faceembodiments.SimpleFaceEmbodiment;
import hmi.xml.XMLTokenizer;

import java.io.IOException;

import lombok.Getter;

public class SimpleFaceEmbodimentLoader implements EmbodimentLoader
{
    @Getter
    private String id;
    
    private SimpleFaceEmbodiment embodiment;
    
    public SimpleFaceEmbodimentLoader(String id, SimpleFaceEmbodiment embodiment)
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
    
    public SimpleFaceEmbodiment getEmbodiment()
    {
        return embodiment;
    }
}
