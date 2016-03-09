package hmi.environment.bodyandfaceembodiments;

import hmi.animation.VJoint;
import hmi.animationembodiments.SkeletonEmbodiment;
import hmi.animationembodiments.VJointSpreadEmbodiment;
import hmi.faceanimation.FaceController;
import hmi.faceembodiments.FaceEmbodiment;
import hmi.faceembodiments.FaceSpreadEmbodiment;

import java.util.Collection;

import lombok.Getter;

/**
 * Spreads the rotation from one input over multiple outputs. The input is constructed upon creation.
 * @author hvanwelbergen
 *
 */
public class BodyAndFaceSpreadEmbodiment implements FaceEmbodiment, SkeletonEmbodiment
{
    @Getter
    private final String id;
    private VJointSpreadEmbodiment vse;
    private FaceSpreadEmbodiment fse;
    
    public BodyAndFaceSpreadEmbodiment(String id, String input, Collection<BodyAndFaceEmbodiment> outputs)
    {
        this.id = id;
        if(outputs.isEmpty())
        {
            throw new RuntimeException("Cannot construct BodyAndFaceSpreadEmbodiment with empty output list");
        }
        vse = VJointSpreadEmbodiment.createFromEmbodiments(id,input, outputs);
        fse = FaceSpreadEmbodiment.createFromEmbodiments(id,input, outputs);
    }

    @Override
    public void copy()
    {
        vse.copy();
        fse.copy();        
    }

    @Override
    public VJoint getAnimationVJoint()
    {
        return vse.getAnimationVJoint();
    }

    @Override
    public FaceController getFaceController()
    {
        return fse.getFaceController();
    }
}
