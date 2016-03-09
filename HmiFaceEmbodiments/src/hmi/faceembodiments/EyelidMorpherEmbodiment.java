package hmi.faceembodiments;

import hmi.environmentbase.Embodiment;
import hmi.faceanimation.EyeLidMorpher;
import hmi.faceanimation.FaceController;

import java.util.List;

import lombok.Getter;

/**
 * Embodiment wrapper of an EyeLidMorpher
 * @author hvanwelbergen
 *
 */
public class EyelidMorpherEmbodiment implements Embodiment
{
    @Getter
    private String id;
    
    private final EyeLidMorpher morpher;
    
    public EyelidMorpherEmbodiment(String id, List<String>morphs)
    {
        this.id = id;
        morpher = new EyeLidMorpher(morphs.toArray(new String[morphs.size()]));
    }
    public EyelidMorpherEmbodiment(String id, List<String>morphs, float morpherWeight)
    {
        this.id = id;
        morpher = new EyeLidMorpher(morphs.toArray(new String[morphs.size()]), morpherWeight);
    }
    
    public void setEyeLidMorph(float []qLeftEye, float[]qRightEye, FaceController fc)
    {
        morpher.setEyeLidMorph(qLeftEye, qRightEye, fc);
    }
}
