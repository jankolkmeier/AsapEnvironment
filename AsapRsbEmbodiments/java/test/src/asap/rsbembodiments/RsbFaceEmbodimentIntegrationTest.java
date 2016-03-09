package asap.rsbembodiments;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.Assert.assertThat;
import hmi.faceanimation.FaceController;

import org.hamcrest.collection.IsIterableContainingInAnyOrder;
import org.junit.Ignore;
import org.junit.Test;

import com.google.common.collect.ImmutableList;

import rsb.RSBException;

/**
 * Unit tests for RsbFaceEmbodiment
 * @author Herwin
 *
 */
public class RsbFaceEmbodimentIntegrationTest
{
    private FaceController mockFaceController = mock(FaceController.class);
    
    @Test(timeout = 6000)
    @Ignore //FIXME: broken for unknown reasons in junitAll
    public void test() throws RSBException, InterruptedException
    {
        StubFace sf = new StubFace(mockFaceController,"billie");
        ImmutableList<String> expectedMorphs = ImmutableList.of("face1","face2","face3");        
        when(mockFaceController.getPossibleFaceMorphTargetNames()).thenReturn(expectedMorphs);
        
        RsbEmbodiment rsbEmbodiment = new RsbEmbodiment();
        RsbFaceEmbodiment rsbFace = new RsbFaceEmbodiment("id1", new RsbFaceController("billie", rsbEmbodiment));
        rsbFace.initialize();
        assertThat(rsbFace.getFaceController().getPossibleFaceMorphTargetNames(), 
                IsIterableContainingInAnyOrder.containsInAnyOrder(expectedMorphs.toArray(new String[3])));
        Thread.sleep(500);
        assertThat(sf.getMorphList(), 
                IsIterableContainingInAnyOrder.containsInAnyOrder(expectedMorphs.toArray(new String[3])));
        rsbFace.getFaceController().setMorphTargets(expectedMorphs.toArray(new String[3]), new float[]{0.1f,0.2f,0.3f});
        rsbFace.copy();
        Thread.sleep(500);
        
        verify(mockFaceController).setMorphTargets(expectedMorphs.toArray(new String[3]), new float[]{0.1f,0.2f,0.3f});
        sf.deactivate();
        rsbEmbodiment.shutdown();
    }
}
