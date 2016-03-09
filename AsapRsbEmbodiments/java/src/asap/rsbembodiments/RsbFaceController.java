package asap.rsbembodiments;

import hmi.faceanimation.FaceController;
import hmi.faceanimation.NullMPEG4FaceController;

import java.util.Collection;
import java.util.List;

import lombok.Delegate;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.ImmutableMap;

/**
 * Implements morph based face animation through rsb (MPEG4 animation is ignored)
 * @author hvanwelbergen
 * 
 */
public class RsbFaceController implements FaceController
{
    public RsbFaceController(String characterId, RsbEmbodiment env)
    {
        this(characterId, env, HashBiMap.<String,String>create());
    }
    
    public RsbFaceController(String characterId, RsbEmbodiment env, BiMap<String, String> renamingMap)
    {
        mfc = new RsbMorphFaceController(characterId, env, renamingMap);
    }
    
    public void initialize()
    {
        mfc.initialize();
    }
    
    private interface Excludes
    {
        void copy();
    }
    @Delegate(excludes = Excludes.class)
    private NullMPEG4FaceController mpegfc = new NullMPEG4FaceController();
    
    
    private RsbMorphFaceController mfc;
    
    @Override
    public void setMorphTargets(String[] targetNames, float[] weights)
    {
        mfc.setMorphTargets(targetNames, weights);        
    }

    @Override
    public float getCurrentWeight(String targetName)
    {
        return mfc.getCurrentWeight(targetName);
    }

    @Override
    public void addMorphTargets(String[] targetNames, float[] weights)
    {
        mfc.addMorphTargets(targetNames, weights);        
    }

    @Override
    public void removeMorphTargets(String[] targetNames, float[] weights)
    {
        mfc.removeMorphTargets(targetNames, weights);        
    }

    @Override
    public Collection<String> getPossibleFaceMorphTargetNames()
    {
        return mfc.getPossibleFaceMorphTargetNames();
    }

    public ImmutableMap<String, Float> getDesiredMorphTargets()
    {
        return mfc.getDesiredMorphTargets();
    }    
    
    @Override
    public void copy()
    {
        mfc.copy();        
    }    
    
    public List<Float> getMorphValues()
    {
        return mfc.getMorphValues();
    }
}
