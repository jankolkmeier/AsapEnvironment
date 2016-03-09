package asap.rsbembodiments.loader;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.whenNew;
import hmi.environmentbase.ClockDrivenCopyEnvironment;
import hmi.environmentbase.Environment;
import hmi.xml.XMLTokenizer;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import asap.rsbembodiments.RsbBodyEmbodiment;
import asap.rsbembodiments.RsbEmbodiment;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableList;

/**
 * Unit tests for RsbBodyEmbodimentLoader
 * @author herwinvw
 * 
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(RsbBodyEmbodimentLoader.class)
public class RsbBodyEmbodimentLoaderTest
{
    private RsbEmbodimentLoader mockEmbodimentLoader = mock(RsbEmbodimentLoader.class);
    
    @SuppressWarnings("unchecked")
    @Test
    public void test() throws Exception
    {
        RsbBodyEmbodiment mockBody = mock(RsbBodyEmbodiment.class);
        whenNew(RsbBodyEmbodiment.class).withArguments(any(String.class),any(String.class), any(RsbEmbodiment.class)).thenReturn(mockBody);
        
        String str = "<Loader id=\"ipaacabodyembodiment\" loader=\"asap.rsbembodiments.loader.RsbBodyEmbodimentLoader\">"
        + "<renaming skeletonRenamingFile=\"billieskeletonrenaming.xml\"/>"
        +"</Loader>";
        ClockDrivenCopyEnvironment env = new ClockDrivenCopyEnvironment(20);
        RsbBodyEmbodimentLoader loader = new RsbBodyEmbodimentLoader();
        XMLTokenizer tok = new XMLTokenizer(str);
        tok.takeSTag();
        loader.readXML(tok, "id1", "id1", "id1", ImmutableList.of(env).toArray(new Environment[0]), mockEmbodimentLoader);
        assertEquals(mockBody, loader.getEmbodiment());
        verify(mockBody).initialize(any(BiMap.class), any(List.class));
        env.time(0);
        verify(mockBody).copy();
    }
    
    
}
