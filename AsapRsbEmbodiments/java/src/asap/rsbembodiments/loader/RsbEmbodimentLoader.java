package asap.rsbembodiments.loader;

import hmi.environmentbase.EmbodimentLoader;
import hmi.environmentbase.Environment;
import hmi.environmentbase.Loader;
import hmi.xml.XMLStructureAdapter;
import hmi.xml.XMLTokenizer;

import java.io.IOException;
import java.util.HashMap;

import lombok.Getter;
import asap.rsbembodiments.RsbEmbodiment;

/**
 * Loads an RsbEmbodiment
 * @author Herwin
 *
 */
public class RsbEmbodimentLoader implements EmbodimentLoader
{
    @Getter
    private String id;
    private RsbEmbodiment embodiment;
    private XMLStructureAdapter adapter = new XMLStructureAdapter();
    private String characterScope;
    
    @Override
    public void readXML(XMLTokenizer tokenizer, String loaderId, String vhId, String vhName, Environment[] environments,
            Loader... requiredLoaders) throws IOException
    {
        this.id = loaderId;
        this.characterScope = vhId;
        embodiment = new RsbEmbodiment();
        
        while (!tokenizer.atETag("Loader"))
        {
            readSection(tokenizer);
        }
        embodiment.initialize(vhId,characterScope);
    }

    protected void readSection(XMLTokenizer tokenizer) throws IOException
    {
        HashMap<String, String> attrMap = null;
        if (tokenizer.atSTag("characterScope"))
        {
            attrMap = tokenizer.getAttributes();
            characterScope = adapter.getRequiredAttribute("characterScope", attrMap, tokenizer);            
        }
        else
        {
            throw tokenizer.getXMLScanException("Unknown tag in Loader content");
        }
        tokenizer.takeSTag("characterScope");
        tokenizer.takeETag("characterScope");
    }
    
    @Override
    public void unload()
    {
        embodiment.shutdown();        
    }

    @Override
    public RsbEmbodiment getEmbodiment()
    {
        return embodiment;
    }
}
