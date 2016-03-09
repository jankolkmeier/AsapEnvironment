package asap.realizer.world;

import static hmi.testutil.math.Vec3fTestUtil.assertVec3fEquals;
import hmi.animation.VJoint;
import hmi.math.Quat4f;
import hmi.math.Vec3f;
import hmi.worldobjectenvironment.AbsolutePositionWorldObject;
import hmi.worldobjectenvironment.WorldObject;

import org.junit.Test;

/**
 * Unit tests for the AbsolutePositionWorldObject
 * @author hvanwelbergen
 *
 */
public class AbsolutePositionWorldObjectTest
{
    private static final float POSITION_PRECISION = 0.0001f;
    
    @Test
    public void testGetTranslation()
    {
        VJoint vj2 = new VJoint();
        VJoint vjWorld = new VJoint();
        vj2.setTranslation(-10, 0, 0);
        float q[] = new float[4];
        Quat4f.setFromAxisAngle4f(q, 0, 0, 1, (float) Math.PI * 0.5f);
        vj2.setRotation(q);
        vjWorld.addChild(vj2);
        WorldObject wj = new AbsolutePositionWorldObject(Vec3f.getVec3f(10,0,0));
        float[] trRef = { 0, -20, 0 };
        float[] tr = new float[3];
        wj.getTranslation(tr, vj2);
        assertVec3fEquals(tr, trRef, POSITION_PRECISION);
    }
}
