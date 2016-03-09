package hmi.animationembodiments;

import hmi.animation.Hanim;
import hmi.animation.VJoint;
import hmi.math.Quat4f;
import hmi.testutil.animation.HanimBody;
import hmi.testutil.math.Quat4fTestUtil;

import org.junit.Test;

import com.google.common.collect.ImmutableList;

/**
 * 
 * @author hvanwelbergen
 * 
 */
public class VJointSpreadEmbodimentTest
{
    private static final float ROTATION_PRECISION = 0.001f;
    
    @Test
    public void test()
    {
        VJoint output1 = HanimBody.getLOA1HanimBody();
        VJoint output2 = HanimBody.getLOA1HanimBody();
        VJointSpreadEmbodiment emb = new VJointSpreadEmbodiment("spreademb","inputj",ImmutableList.of(output1,output2));
        float qsrc[]=Quat4f.getQuat4fFromAxisAngle(1,0,0,2);
        emb.getAnimationVJoint().getPart(Hanim.r_shoulder).setRotation(qsrc);
        emb.copy();
        float qtarget[]=Quat4f.getQuat4f();
        output1.getPart(Hanim.r_shoulder).getRotation(qtarget);
        Quat4fTestUtil.assertQuat4fRotationEquivalent(qtarget,qsrc,ROTATION_PRECISION);
        output2.getPart(Hanim.r_shoulder).getRotation(qtarget);
        Quat4fTestUtil.assertQuat4fRotationEquivalent(qtarget,qsrc,ROTATION_PRECISION);
    }
}
