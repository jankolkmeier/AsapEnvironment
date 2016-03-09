package asap.realizer.world;

import static hmi.testutil.math.Vec3fTestUtil.assertVec3fEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import hmi.animation.VJoint;
import hmi.math.Vec3f;
import hmi.worldobjectenvironment.VJointWorldObject;
import hmi.worldobjectenvironment.WorldObject;
import hmi.worldobjectenvironment.WorldObjectManager;

import org.junit.Test;

/**
 * Unit tests for the WorldObjectManager
 * @author hvanwelbergen
 * 
 */
public class WorldObjectManagerTest
{
    private WorldObjectManager woManager = new WorldObjectManager();
    private static final float POSITION_PRECISION = 0.0001f;

    @Test
    public void testGetWorldObjectAbsolute()
    {
        WorldObject wo = woManager.getWorldObject("1,2, 3");
        assertNotNull(wo);
        float tr[]=Vec3f.getVec3f();
        wo.getTranslation(tr,null);
        assertVec3fEquals(1,2,3,tr,POSITION_PRECISION);
    }
    
    @Test
    public void testGetWorldInvalid()
    {
        WorldObject wo = woManager.getWorldObject("1,2, 3b");
        assertNull(wo);
    }
    
    @Test
    public void testGetVJointWorldObject()
    {
        woManager.addWorldObject("testObject", new VJointWorldObject(new VJoint("vj")));
        assertNotNull(woManager.getWorldObject("testObject"));
    }
}
