package hmi.animationembodiments;

import hmi.animation.VJoint;

/**
 * Simple implementation of a SkeletonEmbodiment with a VJoint.
 * @author Herwin
 *
 */
public class SimpleSkeletonEmbodiment implements SkeletonEmbodiment
{
    private final String id;
    private final VJoint animationJoint;

    public SimpleSkeletonEmbodiment(String id, VJoint vj)
    {
        this.id = id;
        this.animationJoint = vj;
    }

    @Override
    public void copy()
    {
        animationJoint.calculateMatrices();
    }

    @Override
    public String getId()
    {
        return id;
    }

    @Override
    public VJoint getAnimationVJoint()
    {
        return animationJoint;
    }

}
