package hmi.animationembodiments.loader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import hmi.animation.Hanim;
import hmi.animationembodiments.SkeletonEmbodiment;
import hmi.environmentbase.EmbodimentLoader;
import hmi.environmentbase.Environment;
import hmi.testutil.animation.HanimBody;
import hmi.xml.XMLTokenizer;

import java.io.IOException;

import org.hamcrest.collection.IsIterableContainingInAnyOrder;
import org.junit.Test;

/**
 * Unit tests for the VJointSwitchEmbodimentLoader
 * @author hvanwelbergen
 * 
 */
public class VJointSwitchEmbodimentLoaderTest
{
    private EmbodimentLoader mockEmbodimentLoader = mock(EmbodimentLoader.class);
    private SkeletonEmbodiment mockSkeletonEmbodiment = mock(SkeletonEmbodiment.class);
    
    @Test
    public void test() throws IOException
    {
        when(mockEmbodimentLoader.getEmbodiment()).thenReturn(mockSkeletonEmbodiment);
        when(mockSkeletonEmbodiment.getAnimationVJoint()).thenReturn(HanimBody.getLOA1HanimBody());
        //@formatter:off
        String str = "<Loader id=\"id1\" loader=\"hmi.animationembodiments.loader. VJointSwitchEmbodimentLoader\">" +
                     "<Inputs ids=\"input1,input2,input3\"/>"+
        		     "</Loader>";
        //@formatter:on

        VJointSwitchEmbodimentLoader loader = new VJointSwitchEmbodimentLoader();
        XMLTokenizer tok = new XMLTokenizer(str);
        tok.takeSTag();
        loader.readXML(tok, "id1", "vh1", "vh1", new Environment[0],mockEmbodimentLoader);
        assertNotNull(loader.getEmbodiment());
        assertThat(loader.getEmbodiment().getInputs(), IsIterableContainingInAnyOrder.containsInAnyOrder("input1","input2","input3"));
        assertEquals(3, loader.getParts().size());
        assertNotNull(loader.getParts().iterator().next().getEmbodiment().getAnimationVJoint().getPart(Hanim.r_shoulder));
    }
}
