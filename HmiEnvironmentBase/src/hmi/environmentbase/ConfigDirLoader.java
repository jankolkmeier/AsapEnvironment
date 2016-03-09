package hmi.environmentbase;

import java.util.HashMap;

import lombok.Getter;
import hmi.xml.XMLStructureAdapter;
import hmi.xml.XMLTokenizer;

/**
 * Provides a way to specify a directory for e.g. a Loader. 
 * The directory is specified either by localdir (relative to shared.project.root), by dir (absolute path), 
 * or not at all (in which case its defaults to /lib/&ltCONFIGNAME&gt ).
 * @author hvanwelbergen
 */
public class ConfigDirLoader extends XMLStructureAdapter
{
    private final String CONFIGNAME;
    private final String XMLTAG;
    
    @Getter
    private String configDir;
    
    public ConfigDirLoader(String configName, String configTag)
    {
        CONFIGNAME = configName;
        XMLTAG = configTag;
        configDir = System.getProperty("user.dir") + "/lib/"+CONFIGNAME;
    }
    
    @Override
    public void decodeAttributes(HashMap<String, String> attrMap, XMLTokenizer tokenizer)
    {
        String localDir = getOptionalAttribute("localdir", attrMap);
        String dir = getOptionalAttribute("dir", attrMap);
        if (dir == null)
        {
            if (localDir != null)
            {
                String spr = System.getProperty("shared.project.root");
                if (spr == null)
                {
                    throw tokenizer.getXMLScanException("the use of the localdir setting "
                            + "requires a shared.project.root system variable (often: -Dshared.project.root=\"../..\" "
                            + "but this may depend on your system setup).");
                }
                configDir = System.getProperty("shared.project.root") + "/" + localDir;
            }
        }
        else
        {
            configDir = dir;
        }
    }
    
    public String getXMLTag()
    {
        return XMLTAG;
    }
}
