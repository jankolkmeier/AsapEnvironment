package asap.rsbembodiments.loader;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.whenNew;
import hmi.environmentbase.ClockDrivenCopyEnvironment;
import hmi.environmentbase.Environment;
import hmi.xml.XMLTokenizer;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import asap.rsbembodiments.RsbEmbodiment;
import asap.rsbembodiments.RsbFaceController;

import com.google.common.collect.ImmutableList;

/**
 * unit tests for the RsbFaceEmbodimentLoader
 * @author Herwin
 *
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(RsbFaceEmbodimentLoader.class)
public class RsbFaceEmbodimentLoaderTest
{
    private RsbEmbodimentLoader mockEmbodimentLoader = mock(RsbEmbodimentLoader.class);
    private RsbEmbodiment mockEmbodiment = mock(RsbEmbodiment.class);
    
    @Test
    public void test() throws Exception
    {
        RsbFaceController mockRsbFc = mock(RsbFaceController.class);
        whenNew(RsbFaceController.class).withArguments(any(String.class),any(RsbEmbodiment.class)).thenReturn(mockRsbFc);
        
        when(mockEmbodimentLoader.getEmbodiment()).thenReturn(mockEmbodiment);
        String str = "<Loader id=\"ipaacafaceembodiment\" loader=\"asap.rsbembodiments.loader.RsbFaceEmbodimentLoader\"/>";
        ClockDrivenCopyEnvironment env = new ClockDrivenCopyEnvironment(20);
        RsbFaceEmbodimentLoader loader = new RsbFaceEmbodimentLoader();
        XMLTokenizer tok = new XMLTokenizer(str);
        tok.takeSTag();
        loader.readXML(tok, "id1", "id1", "id1", ImmutableList.of(env).toArray(new Environment[0]), mockEmbodimentLoader);
        assertNotNull(loader.getEmbodiment());
        env.time(0);
        verify(mockRsbFc).copy();
    }
}
