package hmi.faceembodiments.loader;

import hmi.environmentbase.EmbodimentLoader;
import hmi.environmentbase.Environment;
import hmi.environmentbase.Loader;
import hmi.faceembodiments.EyelidMorpherEmbodiment;
import hmi.xml.XMLStructureAdapter;
import hmi.xml.XMLTokenizer;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import lombok.Getter;

/**
 * Loads and EyeLidMorpher
 * @author hvanwelbergen
 * 
 */
public class EyelidMorpherLoader implements EmbodimentLoader
{
    @Getter
    private String id;

    private EyelidMorpherEmbodiment embodiment;

    private static final class MorphsLoader extends XMLStructureAdapter
    {
        @Getter
        private List<String> ids;

        @Getter
        private float morpherWeight;

        public void decodeAttributes(HashMap<String, String> attrMap, XMLTokenizer tokenizer)
        {
            ids = decodeStringList(getRequiredAttribute("ids", attrMap, tokenizer));
            morpherWeight = getOptionalFloatAttribute("weight", attrMap, 1.0f);
        }

        public String getXMLTag()
        {
            return XMLTAG;
        }

        public static final String XMLTAG = "Morphs";
    }

    @Override
    public void readXML(XMLTokenizer tokenizer, String loaderId, String vhId, String vhName, Environment[] environments,
            Loader... requiredLoaders) throws IOException
    {
        this.id = loaderId;
        MorphsLoader ml = null;
        if (tokenizer.atSTag() && tokenizer.getTagName().equals(MorphsLoader.XMLTAG))
        {
            ml = new MorphsLoader();
            ml.readXML(tokenizer);
        }
        if (ml == null)
        {
            throw new RuntimeException("EyelidMorpherLoader requires an inner Morphs element");
        }
        embodiment = new EyelidMorpherEmbodiment(loaderId, ml.getIds(), ml.getMorpherWeight());
    }

    @Override
    public void unload()
    {

    }

    @Override
    public EyelidMorpherEmbodiment getEmbodiment()
    {
        return embodiment;
    }

}
