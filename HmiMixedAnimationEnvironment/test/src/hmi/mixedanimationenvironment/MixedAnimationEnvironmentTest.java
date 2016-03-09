package hmi.mixedanimationenvironment;

import static org.hamcrest.Matchers.closeTo;
import static org.mockito.Matchers.anyDouble;
import static org.mockito.Matchers.doubleThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import hmi.animation.VJoint;
import hmi.physicsenvironment.OdePhysicsEnvironment;

import org.junit.Test;

/**
 * Unit tests for the MixedAnimationEnvironment
 * @author Herwin
 */
public class MixedAnimationEnvironmentTest
{
    private OdePhysicsEnvironment mockOdePhysicsEnvironment = mock(OdePhysicsEnvironment.class);
    private MixedAnimationPlayer mockAniPlayer = mock(MixedAnimationPlayer.class);
    private static final float PRECISION = 0.001f;
    
    @Test
    public void testTime()
    {
        MixedAnimationEnvironment env = new MixedAnimationEnvironment();
        env.init(mockOdePhysicsEnvironment, 0.5f);
        env.addAnimationPlayer(mockAniPlayer, new VJoint(), new VJoint());
        env.theAnimationPlayerManager.time(1.1);
        verify(mockOdePhysicsEnvironment,times(2)).physicsTick(0.5f);
        verify(mockAniPlayer,times(2)).playStep(anyDouble());
        verify(mockAniPlayer,times(1)).playStep(doubleThat(closeTo(0.5d,PRECISION)));
        verify(mockAniPlayer,times(1)).playStep(doubleThat(closeTo(1d,PRECISION))); 
    }
}
