package asap.rsbembodiments.loader;

import hmi.animation.RenamingXMLMap;
import hmi.environmentbase.ClockDrivenCopyEnvironment;
import hmi.environmentbase.EmbodimentLoader;
import hmi.environmentbase.Environment;
import hmi.environmentbase.Loader;
import hmi.util.ArrayUtils;
import hmi.util.Resources;
import hmi.xml.XMLScanException;
import hmi.xml.XMLStructureAdapter;
import hmi.xml.XMLTokenizer;

import java.io.IOException;
import java.util.HashMap;

import lombok.Getter;
import asap.rsbembodiments.RsbBodyEmbodiment;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableList;

/**
 * Loads an RsbBodyEmbodiment, requires RsbEmbodiment
 * @author herwinvw
 *
 */
public class RsbBodyEmbodimentLoader implements EmbodimentLoader
{
    @Getter
    private String id;
    private XMLStructureAdapter adapter = new XMLStructureAdapter();
    private BiMap<String, String> skeletonRenaming = null;

    private RsbBodyEmbodiment embodiment;

    @Override
    public void readXML(XMLTokenizer tokenizer, String loaderId, String vhId, String vhName, Environment[] environments,
            Loader... requiredLoaders) throws IOException
    {
        id = loaderId;

        ClockDrivenCopyEnvironment copyEnv = ArrayUtils.getFirstClassOfType(environments, ClockDrivenCopyEnvironment.class);
        RsbEmbodimentLoader ldr = ArrayUtils.getFirstClassOfType(requiredLoaders, RsbEmbodimentLoader.class);
        if (copyEnv == null)
        {
            throw new XMLScanException("RsbBodyEmbodimentLoader requires an ClockDrivenCopyEnvironment");
        }

        if (ldr == null)
        {
            throw new XMLScanException("RsbBodyEmbodimentLoader requires an RsbEmbodimentLoader");
        }

        while (!tokenizer.atETag("Loader"))
        {
            readSection(tokenizer);
        }
        embodiment = new RsbBodyEmbodiment(id, vhId, ldr.getEmbodiment());
        
        if (skeletonRenaming == null)
        {
            throw new XMLScanException("RsbBodyEmbodimentLoader requires inner renaming element");
        }
        embodiment.initialize(skeletonRenaming, ImmutableList.copyOf(skeletonRenaming.values()));
        copyEnv.addCopyEmbodiment(embodiment);
    }

    private BiMap<String, String> getRenamingMap(String mappingFile) throws IOException
    {
        RenamingXMLMap map = new RenamingXMLMap();
        map.readXML(new XMLTokenizer(new Resources("").getInputStream(mappingFile)));
        return map.getRenamingMap();
    }

    protected void readSection(XMLTokenizer tokenizer) throws IOException
    {
        HashMap<String, String> attrMap = null;
        if (tokenizer.atSTag("renaming"))
        {
            attrMap = tokenizer.getAttributes();
            String skelRenamingFile = adapter.getRequiredAttribute("skeletonRenamingFile", attrMap, tokenizer);
            skeletonRenaming = getRenamingMap(skelRenamingFile);
        }
        else
        {
            throw tokenizer.getXMLScanException("Unknown tag in Loader content");
        }
        tokenizer.takeEmptyElement("renaming");
    }

    @Override
    public void unload()
    {
    }

    @Override
    public RsbBodyEmbodiment getEmbodiment()
    {
        return embodiment;
    }

}
