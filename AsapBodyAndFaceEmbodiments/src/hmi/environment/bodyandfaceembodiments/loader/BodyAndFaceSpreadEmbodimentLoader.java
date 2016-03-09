package hmi.environment.bodyandfaceembodiments.loader;

import hmi.environment.bodyandfaceembodiments.BodyAndFaceEmbodiment;
import hmi.environment.bodyandfaceembodiments.BodyAndFaceSpreadEmbodiment;
import hmi.environmentbase.CopyEnvironment;
import hmi.environmentbase.EmbodimentLoader;
import hmi.environmentbase.Environment;
import hmi.environmentbase.Loader;
import hmi.util.ArrayUtils;
import hmi.xml.XMLTokenizer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

public class BodyAndFaceSpreadEmbodimentLoader implements EmbodimentLoader
{
    @Getter
    private String id;
    private BodyAndFaceSpreadEmbodiment embodiment;
    
    @Override
    public void readXML(XMLTokenizer tokenizer, String loaderId, String vhId, String vhName, Environment[] environments,
            Loader... requiredLoaders) throws IOException
    {
        this.id = loaderId;
        List<BodyAndFaceEmbodiment> outputs = new ArrayList<BodyAndFaceEmbodiment>();
        for (EmbodimentLoader el : ArrayUtils.getClassesOfType(requiredLoaders, EmbodimentLoader.class))
        {
            if (el.getEmbodiment() instanceof BodyAndFaceEmbodiment)
            {
                outputs.add((BodyAndFaceEmbodiment)el.getEmbodiment());
            }
        }
        
        if(outputs.isEmpty())
        {
            throw new RuntimeException("BodyAndFaceSwitchEmbodimentLoader requires output BodyAndFaceEmbodiments");
        }
        
        CopyEnvironment env = ArrayUtils.getFirstClassOfType(environments, CopyEnvironment.class);
        if (env == null)
        {
            throw new RuntimeException("BodyAndFaceSwitchEmbodimentLoader requires a CopyEnvironment");
        }        
        embodiment = new BodyAndFaceSpreadEmbodiment(id, id, outputs);
        env.addCopyEmbodiment(embodiment);
    }

    @Override
    public void unload()
    {
                
    }    

    @Override
    public BodyAndFaceSpreadEmbodiment getEmbodiment()
    {
        return embodiment;
    }

}
