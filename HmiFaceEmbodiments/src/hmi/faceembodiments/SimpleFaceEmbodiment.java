package hmi.faceembodiments;

import hmi.faceanimation.FaceController;
import lombok.Getter;

/**
 * Simple implementation of a FaceEmbodiment using a provided FaceController
 * @author hvanwelbergen
 *
 */
public class SimpleFaceEmbodiment implements FaceEmbodiment
{
    private final FaceController faceController;
    @Getter
    private final String id;
    
    public SimpleFaceEmbodiment(String id, FaceController fc)
    {
        this.id = id;
        faceController = fc;
    }
    
    
    @Override
    public void copy()
    {
        faceController.copy();        
    }

    @Override
    public FaceController getFaceController()
    {
        return faceController;
    }    
}
