package hmi.animationembodiments.loader;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import hmi.environmentbase.Environment;
import hmi.xml.XMLTokenizer;

import org.junit.Test;

/**
 * Unit tests for the XMLSkeletonEmbodimentLoader
 * @author Herwin
 *
 */
public class XMLSkeletonEmbodimentLoaderTest
{
    @Test
    public void test() throws IOException
    {
        //@formatter:off
        String str = "<Loader id=\"id1\" loader=\"hmi.animationembodiments.loader.XMLSkeletonEmbodimentLoader\">" +
        		        "<XMLSkeletonSection resources=\"\" filename=\"Humanoids/armandia/skeleton/armandia_skel.xml\"/>" +
        		     "</Loader>";
        //@formatter:on
        XMLSkeletonEmbodimentLoader loader = new XMLSkeletonEmbodimentLoader();
        XMLTokenizer tok = new XMLTokenizer(str);
        tok.takeSTag();
        loader.readXML(tok, "id1", "vh1", "vh1", new Environment[0]);
        assertNotNull(loader.getEmbodiment());
    }
}
