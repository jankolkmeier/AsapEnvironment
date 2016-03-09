package hmi.jcomponentenvironment.loader;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import hmi.environmentbase.EmbodimentLoader;
import hmi.environmentbase.Environment;
import hmi.environmentbase.InputSwitchEmbodiment;
import hmi.environmentbase.Loader;
import hmi.jcomponentenvironment.JComponentEmbodiment;
import hmi.xml.XMLTokenizer;

import java.io.IOException;

import javax.swing.JComponent;

import org.junit.Test;


/**
 * Unit tests for the VJointSwitchEmbodimentSwingUILoader
 * @author hvanwelbergen
 *
 */
public class InputSwitchEmbodimentSwingUILoaderTest
{
    @Test
    public void test() throws IOException
    {
        //@formatter:off
        String str = "<Loader id=\"id1\" loader=\"asap.animationswitchuienvironment.loader.VJointSwitchEmbodimentSwingUILoader\"/>";
        //@formatter:on

        JComponentEmbodiment mockJCEmbodiment = mock(JComponentEmbodiment.class);
        JComponentEmbodimentLoader mockJCEmbodimentLoader = mock(JComponentEmbodimentLoader.class);
        when(mockJCEmbodimentLoader.getEmbodiment()).thenReturn(mockJCEmbodiment);
        
        EmbodimentLoader mockVJSwitchEmbodimentLoader = mock(EmbodimentLoader.class);
        InputSwitchEmbodiment mockvjSwitch = mock(InputSwitchEmbodiment.class);
        when(mockVJSwitchEmbodimentLoader.getEmbodiment()).thenReturn(mockvjSwitch);
        
        InputSwitchEmbodimentSwingUILoader loader = new InputSwitchEmbodimentSwingUILoader();
        XMLTokenizer tok = new XMLTokenizer(str);
        tok.takeSTag();
        loader.readXML(tok, "id1", "vh1", "vh1", new Environment[0],new Loader[]{mockJCEmbodimentLoader,mockVJSwitchEmbodimentLoader});
        verify(mockJCEmbodiment).addJComponent(any(JComponent.class));
    }
}
