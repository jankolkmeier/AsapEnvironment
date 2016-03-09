package hmi.environmentbase;

import java.util.HashMap;
import java.util.List;

import lombok.Getter;
import hmi.xml.XMLStructureAdapter;
import hmi.xml.XMLTokenizer;

/**
 * Loader for &lt;Inputs ids="input1,input2,input3"/&gt;
 * @author hvanwelbergen
 */
public class InputsLoader extends XMLStructureAdapter
{
    @Getter
    private List<String> ids;

    public void decodeAttributes(HashMap<String, String> attrMap, XMLTokenizer tokenizer)
    {
        ids = decodeStringList(getRequiredAttribute("ids", attrMap, tokenizer));
    }

    public String getXMLTag()
    {
        return XMLTAG;
    }

    public static final String XMLTAG = "Inputs";
}
