package hmi.animationembodiments.loader;

import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;

import hmi.animation.VJoint;
import hmi.animation.XMLSkeleton;
import hmi.animationembodiments.SimpleSkeletonEmbodiment;
import hmi.environmentbase.Embodiment;
import hmi.environmentbase.EmbodimentLoader;
import hmi.environmentbase.Environment;
import hmi.environmentbase.Loader;
import hmi.util.Resources;
import hmi.xml.XMLStructureAdapter;
import hmi.xml.XMLTokenizer;

/**
 * Constructs a SkeletonEmbodiment from a SkeletonXML file
 * @author Herwin
 *
 */
public class XMLSkeletonEmbodimentLoader implements EmbodimentLoader
{
    private String id;
    private SimpleSkeletonEmbodiment embodiment;
    
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
        while (!tokenizer.atETag("Loader"))
        {
            readSection(tokenizer);
        }        
    }
    
    private class XMLSkeletonSection extends XMLStructureAdapter
    {
        private String resource;
        private String filename;
        private static final String XMLTAG = "XMLSkeletonSection";
        
        @Override
        public void decodeAttributes(HashMap<String, String> attrMap, XMLTokenizer tokenizer)
        {
            resource = getOptionalAttribute("resource", attrMap, "");
            filename = getRequiredAttribute("filename", attrMap, tokenizer);        
            super.decodeAttributes(attrMap, tokenizer);
        }
        
        public VJoint getVJoint()
        {
            XMLSkeleton skel = new XMLSkeleton(id);
            try
            {
                Reader r = new Resources(resource).getReader(filename);
                if(r==null)
                {
                    throw new RuntimeException("Cannnot find XMLSkeleton resource:"+resource+", filename:"+filename);
                }
                skel.readXML(r);
            }
            catch (IOException e)
            {
                throw new RuntimeException("Cannnot load Skeleton: " + e);
            }
            return skel.getRoots().get(0);
        }
        
        @Override
        public String getXMLTag()
        {
            return XMLTAG;
        }
    }
    private void readSection(XMLTokenizer tokenizer) throws IOException
    {
        if (tokenizer.atSTag("XMLSkeletonSection"))
        {
            XMLSkeletonSection skel = new XMLSkeletonSection();
            skel.readXML(tokenizer);
            embodiment = new SimpleSkeletonEmbodiment(id,skel.getVJoint());
        }
        else
        {
            throw tokenizer.getXMLScanException("Unknown tag in Loader content");
        }
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
