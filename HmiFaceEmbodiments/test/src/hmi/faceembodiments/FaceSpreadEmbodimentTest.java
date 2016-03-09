package hmi.faceembodiments;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import hmi.faceanimation.FaceController;

import org.junit.Test;

import com.google.common.collect.ImmutableList;

/**
 * Unit tests for the FaceSpreadEmbodiment
 * @author hvanwelbergen
 *
 */
public class FaceSpreadEmbodimentTest
{
    private FaceController mockOutputController1 = mock(FaceController.class);
    private FaceController mockOutputController2 = mock(FaceController.class);
    
    @Test
    public void test()
    {
        FaceSpreadEmbodiment fse = new FaceSpreadEmbodiment("fs1","inputface",ImmutableList.of(mockOutputController1, mockOutputController2));
        fse.getFaceController().setMorphTargets(new String[]{"x"}, new float[]{1.0f});
        fse.copy();
        verify(mockOutputController1).setMorphTargets(eq(new String[]{"x"}), eq(new float[]{1.0f}));
        verify(mockOutputController2).setMorphTargets(eq(new String[]{"x"}), eq(new float[]{1.0f}));
    }
}
