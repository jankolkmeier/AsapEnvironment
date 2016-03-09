package asap.rsbembodiments.loader;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.powermock.api.mockito.PowerMockito.whenNew;
import hmi.environmentbase.ClockDrivenCopyEnvironment;
import hmi.environmentbase.Environment;
import hmi.testutil.animation.HanimBody;
import hmi.xml.XMLScanException;
import hmi.xml.XMLTokenizer;

import java.io.IOException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import asap.rsbembodiments.RsbBodyEmbodiment;
import asap.rsbembodiments.RsbEmbodiment;
import asap.rsbembodiments.RsbFaceController;
import asap.rsbembodiments.Rsbembodiments.AnimationData;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableList;

/**
 * Unit tests for RsbBodyAndFaceEmbodiment
 * @author herwinvw
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(RsbBodyAndFaceEmbodimentLoader.class)
public class RsbBodyAndFaceEmbodimentLoaderTest
{
    private RsbEmbodimentLoader mockEmbodimentLoader = mock(RsbEmbodimentLoader.class);
    private RsbEmbodiment mockEmbodiment = mock(RsbEmbodiment.class);
    private RsbBodyEmbodiment mockBody = mock(RsbBodyEmbodiment.class);
    private RsbFaceController mockRsbFc = mock(RsbFaceController.class);

    @Before
    public void setup() throws Exception
    {
        when(mockEmbodimentLoader.getEmbodiment()).thenReturn(mockEmbodiment);
        whenNew(RsbBodyEmbodiment.class).withArguments(any(String.class), any(String.class), any(RsbEmbodiment.class)).thenReturn(mockBody);
        whenNew(RsbFaceController.class).withArguments(any(String.class), any(RsbEmbodiment.class), any(BiMap.class)).thenReturn(mockRsbFc);
        when(mockBody.getAnimationVJoint()).thenReturn(HanimBody.getLOA1HanimBody());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void test() throws IOException
    {
        String str = "<Loader id=\"ipaacabodyandfaceembodiment\" loader=\"asap.ipaacaembodiments.loader.IpaacaFaceAndBodyEmbodimentLoader\">"
                + "<renaming skeletonRenamingFile=\"billieskeletonrenaming.xml\" morphRenamingFile=\"billiemorphsrenaming.xml\"/>"
                + "</Loader>";
        ClockDrivenCopyEnvironment env = new ClockDrivenCopyEnvironment(20);
        RsbBodyAndFaceEmbodimentLoader loader = new RsbBodyAndFaceEmbodimentLoader();
        XMLTokenizer tok = new XMLTokenizer(str);
        tok.takeSTag();
        loader.readXML(tok, "id1", "id1", "id1", ImmutableList.of(env).toArray(new Environment[0]), mockEmbodimentLoader);
        assertNotNull(loader.getEmbodiment());
        verify(mockBody,times(1)).initialize(any(BiMap.class), any(List.class));
        verify(mockRsbFc,times(1)).initialize();
        env.time(0);
        verify(mockRsbFc,times(0)).copy();
        verify(mockBody,times(0)).copy();
        verify(mockEmbodiment).sendAnimationData(any(AnimationData.class));
    }

    @Test(expected = XMLScanException.class)
    public void testNonExistingSkeletonRenamingFile() throws IOException
    {
        String str = "<Loader id=\"rsbbodyandfaceembodiment\" loader=\"asap.rsbembodiments.loader.RsbBodyAndFaceEmbodimentLoader\">"
                + "<renaming skeletonRenamingFile=\"nonexistantskel.xml\"/>" + "</Loader>";
        ClockDrivenCopyEnvironment env = new ClockDrivenCopyEnvironment(20);
        RsbBodyAndFaceEmbodimentLoader loader = new RsbBodyAndFaceEmbodimentLoader();
        XMLTokenizer tok = new XMLTokenizer(str);
        tok.takeSTag();
        loader.readXML(tok, "id1", "id1", "id1", ImmutableList.of(env).toArray(new Environment[0]), mockEmbodimentLoader);
    }

    @Test(expected = XMLScanException.class)
    public void testNonExistingMorphRenamingFile() throws IOException
    {
        String str = "<Loader id=\"ipaacabodyandfaceembodiment\" loader=\"asap.ipaacaembodiments.loader.IpaacaFaceAndBodyEmbodimentLoader\">"
                + "<renaming skeletonRenamingFile=\"billieskeletonrenaming.xml\" morphRenamingFile=\"nonexistantskel.xml\"/>" + "</Loader>";
        ClockDrivenCopyEnvironment env = new ClockDrivenCopyEnvironment(20);
        RsbBodyAndFaceEmbodimentLoader loader = new RsbBodyAndFaceEmbodimentLoader();
        XMLTokenizer tok = new XMLTokenizer(str);
        tok.takeSTag();
        loader.readXML(tok, "id1", "id1", "id1", ImmutableList.of(env).toArray(new Environment[0]), mockEmbodimentLoader);
    }

}
