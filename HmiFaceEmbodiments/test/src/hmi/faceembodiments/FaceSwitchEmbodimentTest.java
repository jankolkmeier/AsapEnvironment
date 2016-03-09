package hmi.faceembodiments;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import hmi.faceanimation.FaceController;

import org.hamcrest.collection.IsIterableContainingInAnyOrder;
import org.junit.Test;

import com.google.common.collect.ImmutableList;

/**
 * Unit tests for the FaceSwitchEmbodiment
 * @author hvanwelbergen
 *
 */
public class FaceSwitchEmbodimentTest
{
    private FaceController mockOutputController = mock(FaceController.class);
    private FaceSwitchEmbodiment switchEmb = new FaceSwitchEmbodiment("id1", ImmutableList.of("input1", "input2", "input3"), mockOutputController);
    @Test
    public void testInit()
    {
        assertEquals("input1",switchEmb.getCurrentInput());
        assertThat(switchEmb.getInputs(), IsIterableContainingInAnyOrder.containsInAnyOrder("input1","input2","input3"));        
    }
    
    @Test
    public void testCopy()
    {
        switchEmb.getInput("input2").setMorphTargets(new String[]{"x"},new float[]{1.0f});
        switchEmb.selectInput("input2");
        switchEmb.copy();
        verify(mockOutputController).setMorphTargets(eq(new String[]{"x"}), eq(new float[]{1.0f}));
    }
}
