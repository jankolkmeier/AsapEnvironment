package hmi.mixedanimationenvironment;

import static org.hamcrest.Matchers.closeTo;
import static org.mockito.Matchers.anyDouble;
import static org.mockito.Matchers.anyFloat;
import static org.mockito.Matchers.doubleThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import hmi.animation.VJoint;

import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for the MixedAnimationPlayerManager
 * @author Herwin
 *
 */
public class MixedAnimationPlayerManagerTest
{
    private PhysicsCallback mockPhysicsCallback = mock(PhysicsCallback.class);
    private MixedAnimationPlayer mockAniPlayer = mock(MixedAnimationPlayer.class);
    private MixedAnimationPlayerManager apm;
    private static final float PRECISION = 0.001f;
    @Before
    public void setup()
    {
        apm = new MixedAnimationPlayerManager(mockPhysicsCallback);
        apm.addAnimationPlayer(mockAniPlayer, new VJoint(), new VJoint());
    }
    
    @Test
    public void testZeroTime()
    {
        apm.time(0);
        verify(mockPhysicsCallback,times(0)).time(anyFloat());
        verify(mockAniPlayer,times(0)).playStep(anyDouble());
    }
    
    @Test
    public void testTwoTimes()
    {
        apm.time(0.007f);
        verify(mockPhysicsCallback,times(2)).time(0.003f);
        verify(mockAniPlayer,times(2)).playStep(anyDouble());
        verify(mockAniPlayer,times(1)).playStep(doubleThat(closeTo(0.003d,PRECISION)));
        verify(mockAniPlayer,times(1)).playStep(doubleThat(closeTo(0.006d,PRECISION)));
    }
    
    @Test
    public void testH()
    {
        apm = new MixedAnimationPlayerManager(mockPhysicsCallback, 0.5f);
        apm.addAnimationPlayer(mockAniPlayer, new VJoint(), new VJoint());
        apm.time(1.1);
        verify(mockPhysicsCallback,times(2)).time(0.5f);
        verify(mockAniPlayer,times(2)).playStep(anyDouble());
        verify(mockAniPlayer,times(1)).playStep(doubleThat(closeTo(0.5d,PRECISION)));
        verify(mockAniPlayer,times(1)).playStep(doubleThat(closeTo(1d,PRECISION)));        
    }
}
