package asap.rsbembodiments.loader;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.whenNew;
import hmi.environmentbase.Environment;
import hmi.xml.XMLTokenizer;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import asap.rsbembodiments.RsbEmbodiment;

/**
 * unit tests for the RsbEmbodimentLoader
 * @author Herwin
 *
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(RsbEmbodimentLoader.class)
public class RsbEmbodimentLoaderTest
{
    @Test
    public void test() throws Exception
    {
        RsbEmbodiment mockRsbEmbodiment = mock(RsbEmbodiment.class);
        whenNew(RsbEmbodiment.class).withNoArguments().thenReturn(mockRsbEmbodiment);
        
        String str = "<Loader id=\"rsbembodiment\" loader=\"asap.ipaacaembodiments.loader.IpaacaEmbodimentLoader\"/>";                
        RsbEmbodimentLoader loader = new RsbEmbodimentLoader();
        XMLTokenizer tok = new XMLTokenizer(str);
        tok.takeSTag();
        loader.readXML(tok, "id1", "id1", "id1", new Environment[0]);
        assertEquals(mockRsbEmbodiment, loader.getEmbodiment());
    }
    
    @Test
    public void testNoCharacterScope() throws Exception
    {
        RsbEmbodiment mockRsbEmbodiment = mock(RsbEmbodiment.class);
        whenNew(RsbEmbodiment.class).withNoArguments().thenReturn(mockRsbEmbodiment);
        
        String str = "<Loader id=\"rsbembodiment\" loader=\"asap.ipaacaembodiments.loader.IpaacaEmbodimentLoader\">"
        +"<characterScope characterScope=\"\"/>"
        +"</Loader>";                
        RsbEmbodimentLoader loader = new RsbEmbodimentLoader();
        XMLTokenizer tok = new XMLTokenizer(str);
        tok.takeSTag();
        loader.readXML(tok, "id1", "id1", "id1", new Environment[0]);
        assertEquals(mockRsbEmbodiment, loader.getEmbodiment());
        verify(mockRsbEmbodiment, times(1)).initialize("id1","");
    }
    
    @Test
    public void testInvalidXML() throws Exception
    {
        RsbEmbodiment mockRsbEmbodiment = mock(RsbEmbodiment.class);
        whenNew(RsbEmbodiment.class).withNoArguments().thenReturn(mockRsbEmbodiment);
        
        String str = "<Loader id=\"rsbembodiment\" loader=\"asap.ipaacaembodiments.loader.IpaacaEmbodimentLoader\"/>"
                +"<characterScope characterScope=\"characterx\"/>"
                + "</Loader>";                
        RsbEmbodimentLoader loader = new RsbEmbodimentLoader();
        XMLTokenizer tok = new XMLTokenizer(str);
        tok.takeSTag();
        loader.readXML(tok, "id1", "id1", "id1", new Environment[0]);
        assertEquals(mockRsbEmbodiment, loader.getEmbodiment());
        
    }
}
