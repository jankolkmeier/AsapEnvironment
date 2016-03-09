package hmi.faceembodiments.loader;
import static org.junit.Assert.assertNotNull;
import hmi.environmentbase.Environment;
import hmi.environmentbase.Loader;
import hmi.xml.XMLTokenizer;

import java.io.IOException;

import org.junit.Test;

/**
 * Unit tests for the EyelidMorpherLoader
 * @author hvanwelbergen
 *
 */
public class EyelidMorpherLoaderTest
{
    
    
    @Test
    public void test() throws IOException
    {
        //@formatter:off
        String str = "<Loader id=\"id1\" loader=\"hmi.faceembodiments.loader.EyelidMorpherLoader\">" +
                     "<Morphs ids=\"eyelid1,eyelid2,eyelid3\"/>"+
                     "</Loader>";
        //@formatter:on
        EyelidMorpherLoader loader = new EyelidMorpherLoader();
        XMLTokenizer tok = new XMLTokenizer(str);
        tok.takeSTag();
        loader.readXML(tok, "id1", "vh1", "vh1", new Environment[0],new Loader[]{});
        assertNotNull(loader.getEmbodiment());
    }
}
