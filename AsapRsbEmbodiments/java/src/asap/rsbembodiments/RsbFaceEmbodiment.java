package asap.rsbembodiments;

import hmi.faceembodiments.FaceEmbodiment;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

import com.google.common.collect.ImmutableMap;

/**
 * FaceEmbodiment that makes use of an RsbFaceController
 * @author hvanwelbergen
 *
 */
public class RsbFaceEmbodiment implements FaceEmbodiment
{
    private final RsbFaceController fc;
    @Getter @Setter
    private String id;
    
    public RsbFaceEmbodiment(String id, RsbFaceController fc)
    {
        this.id = id;
        this.fc = fc;         
    }
    
    public void initialize()
    {
        fc.initialize();
    }
    
    @Override
    public void copy()
    {
        fc.copy();        
    }
    
    public ImmutableMap<String, Float> getDesiredMorphTargets()
    {
        return fc.getDesiredMorphTargets();
    }
    
    @Override
    public RsbFaceController getFaceController()
    {
        return fc;
    }
    
    public List<Float> getMorphValues()
    {
        return fc.getMorphValues();
    }
}
