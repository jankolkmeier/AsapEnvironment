package hmi.jcomponentenvironment.loader;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import hmi.environmentbase.Environment;
import hmi.environmentbase.Loader;
import hmi.jcomponentenvironment.JComponentEnvironment;
import hmi.xml.XMLTokenizer;

import java.io.IOException;

import javax.swing.JComponent;

import org.junit.Test;

/**
 * Tests for the JFrameEmbodimentLoader
 * @author hvanwelbergen
 *
 */
public class JFrameLoaderTest
{
    private JComponentEnvironment mockjce = mock(JComponentEnvironment.class);
    
    @Test
    public void test() throws IOException
    {
        JFrameLoader loader = new JFrameLoader();
        String str = "<Loader id=\"frame1\"><JFrame title=\"testtitle\" width=\"500\" height=\"100\"/></Loader>";
        XMLTokenizer tok = new XMLTokenizer(str);
        tok.takeSTag();
        loader.readXML(tok, "frame1", "id1", "id1" , new Environment[]{mockjce}, new Loader[]{});
        verify(mockjce).registerComponent(eq("frame1"), any(JComponent.class));
    }
}
