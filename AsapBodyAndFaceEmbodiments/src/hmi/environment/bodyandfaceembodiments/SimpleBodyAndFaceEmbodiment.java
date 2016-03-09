package hmi.environment.bodyandfaceembodiments;

import hmi.animation.VJoint;
import hmi.faceanimation.FaceController;
import lombok.Getter;

/**
 * Combination of VJoint and FaceController steering in a single embodiment
 * @author hvanwelbergen
 *
 */
public class SimpleBodyAndFaceEmbodiment implements BodyAndFaceEmbodiment
{
    private final VJoint aniJoint;
    private final FaceController  faceController;
    @Getter
    private final String id;
   
    public SimpleBodyAndFaceEmbodiment(String id, VJoint aniJoint, FaceController fc)
    {
        this.aniJoint = aniJoint;
        this.faceController = fc;
        this.id = id;
    }
    
    @Override
    public FaceController getFaceController()
    {
        return faceController;
    }

    @Override
    public void copy()
    {
        faceController.copy();
        aniJoint.calculateMatrices();        
    }

    @Override
    public VJoint getAnimationVJoint()
    {
        return aniJoint;
    }
    
}
