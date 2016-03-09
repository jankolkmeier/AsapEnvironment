package hmi.animationembodiments;

import hmi.animation.VJoint;
import hmi.animation.VObjectTransformCopier;
import hmi.util.AnimationSync;

/**
 * Creates a new VJoint that is a copy of a source joint. The copy action copies source to this joint, 
 * synchronized by AnimationSync.
 * @author hvanwelbergen
 */
public class VJointSynchronizedCopyEmbodiment implements SkeletonEmbodiment
{
    private final VJoint dst;
    private final String id;
    private final VObjectTransformCopier votc;
    
    public VJointSynchronizedCopyEmbodiment(String id, VJoint src)
    {
        this.id = id;
        dst = src.copyTree(id);
        votc = VObjectTransformCopier.newInstanceFromVJointTree(src,dst,"T1R");               
    }
    
    @Override
    public void copy()
    {
        synchronized(AnimationSync.getSync())
        {
            votc.copyConfig();
        }
    }

    @Override
    public String getId()
    {
        return id;
    }

    @Override
    public VJoint getAnimationVJoint()
    {
        return dst;
    }
}
