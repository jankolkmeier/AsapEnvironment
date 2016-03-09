package hmi.animationembodiments.loader;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import hmi.animation.VJoint;
import hmi.animationembodiments.SkeletonEmbodiment;
import hmi.environmentbase.EmbodimentLoader;
import hmi.environmentbase.Environment;
import hmi.xml.XMLTokenizer;

import java.io.IOException;

import org.junit.Test;

/**
 * Unit tests for the VJointSynchronizedCopyEmbodimentLoader
 * @author hvanwelbergen
 *
 */
public class VJointSynchronizedCopyEmbodimentLoaderTest
{
    private EmbodimentLoader mockEmbodimentLoader = mock(EmbodimentLoader.class);
    private SkeletonEmbodiment mockSkeletonEmbodiment = mock(SkeletonEmbodiment.class);
    
    @Test
    public void test() throws IOException
    {
        when(mockEmbodimentLoader.getEmbodiment()).thenReturn(mockSkeletonEmbodiment);
        when(mockSkeletonEmbodiment.getAnimationVJoint()).thenReturn(new VJoint());
        
        //@formatter:off
        String str = "<Loader id=\"id1\" loader=\"hmi.animationembodiments.loader.VJointSynchronizedCopyEmbodimentLoader\"/>";
        //@formatter:on
        
        VJointSynchronizedCopyEmbodimentLoader loader = new VJointSynchronizedCopyEmbodimentLoader();
        XMLTokenizer tok = new XMLTokenizer(str);
        tok.takeSTag();
        loader.readXML(tok, "id1", "vh1", "vh1", new Environment[0],mockEmbodimentLoader);
        assertNotNull(loader.getEmbodiment());
    }
}
