package asap.rsbembodiments.loader;

import hmi.environmentbase.ClockDrivenCopyEnvironment;
import hmi.environmentbase.EmbodimentLoader;
import hmi.environmentbase.Environment;
import hmi.environmentbase.Loader;
import hmi.util.ArrayUtils;
import hmi.xml.XMLScanException;
import hmi.xml.XMLTokenizer;

import java.io.IOException;

import lombok.Getter;
import asap.rsbembodiments.RsbFaceController;
import asap.rsbembodiments.RsbFaceEmbodiment;

/**
 * Loads an RsbFaceEmbodiment, requires an RsbEmbodiment
 * @author Herwin
 *
 */
public class RsbFaceEmbodimentLoader implements EmbodimentLoader
{
    private RsbFaceEmbodiment embodiment;

    @Getter
    private String id;
    
    @Override
    public void readXML(XMLTokenizer tokenizer, String loaderId, String vhId, String vhName, Environment[] environments,
            Loader... requiredLoaders) throws IOException
    {
        this.id = loaderId;
        
        ClockDrivenCopyEnvironment copyEnv = ArrayUtils.getFirstClassOfType(environments, ClockDrivenCopyEnvironment.class);
        RsbEmbodimentLoader ldr = ArrayUtils.getFirstClassOfType(requiredLoaders, RsbEmbodimentLoader.class); 
        
        if(copyEnv == null)
        {
            throw new XMLScanException("IpaacaFaceEmbodimentLoader requires an ClockDrivenCopyEnvironment");
        }
        
        if(ldr == null)
        {
            throw new XMLScanException("IpaacaFaceEmbodimentLoader requires an IpaacaEmbodimentLoader");
        }
        RsbFaceController fc = new RsbFaceController(vhId, ldr.getEmbodiment());
        embodiment = new RsbFaceEmbodiment(vhId, fc);
        embodiment.initialize();
        copyEnv.addCopyEmbodiment(embodiment);
    }

    @Override
    public void unload()
    {
                
    }

    @Override
    public RsbFaceEmbodiment getEmbodiment()
    {
        return embodiment;
    }
}
