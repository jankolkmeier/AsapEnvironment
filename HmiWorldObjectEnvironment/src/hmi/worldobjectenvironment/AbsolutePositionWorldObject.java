package hmi.worldobjectenvironment;

import hmi.animation.VJoint;
import hmi.math.Mat4f;
import hmi.math.Vec3f;
import hmi.util.AnimationSync;

/**
 * A WorldObject specified by a global position.
 * @author hvanwelbergen
 */
public class AbsolutePositionWorldObject implements WorldObject
{
    private float position[] = Vec3f.getVec3f();

    public AbsolutePositionWorldObject(float pos[])
    {
        Vec3f.set(position,pos);
    }

    @Override
    public void getWorldTranslation(float[] tr)
    {
        Vec3f.set(tr, position);
    }

    @Override
    public void getTranslation(float[] tr, VJoint vj)
    {
        float[] mTemp1 = Mat4f.getIdentity();
        synchronized(AnimationSync.getSync())
        {
            if(vj!=null)
            {
                vj.getPathTransformMatrix(null, mTemp1);
            }
        }
        Mat4f.invertRigid(mTemp1);
        Mat4f.transformPoint(mTemp1, tr, position); 
    }

    @Override
    public void getTranslation2(float[] tr, VJoint vj)
    {
        float trTempToParent[] = Vec3f.getVec3f(0,0,0);
        float[] mTemp1 = Mat4f.getIdentity();
        
        synchronized(AnimationSync.getSync())
        {
            if(vj!=null)
            {
                vj.getParent().getPathTransformMatrix(null, mTemp1);            
                vj.getTranslation(trTempToParent);
            }           
        }
        Mat4f.invertRigid(mTemp1);
        Mat4f.transformPoint(mTemp1, tr, position);
        Vec3f.sub(tr, trTempToParent);

    }

    @Override
    public void setTranslation(float[] tr)
    {
        Vec3f.set(position,tr);        
    }
}
