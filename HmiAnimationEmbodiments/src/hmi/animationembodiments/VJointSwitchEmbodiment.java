package hmi.animationembodiments;

import hmi.animation.VJoint;
import hmi.animation.VObjectTransformCopier;
import hmi.environmentbase.InputSwitchEmbodiment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import lombok.Getter;

/**
 * Redirects the rotations/translations from selected input VJoint tree to an output joint. Input joints are constructed on creation. 
 * @author hvanwelbergen
 *
 */
public class VJointSwitchEmbodiment implements SkeletonEmbodiment, InputSwitchEmbodiment
{
    private Map<String, VJoint> inputJoints = new HashMap<String, VJoint>();
    private VObjectTransformCopier copier;
    private VJoint currentJoint;
    private final VJoint outputJoint;
    private String currentJointName;
    
    @Getter
    private final String id;
    
    public VJointSwitchEmbodiment(String id, List<String> inputIds, VJoint outputJoint)
    {
        if(inputIds.isEmpty())
        {
            throw new RuntimeException("Cannot construct VJointSwitchEmbodiment with empty input list");
        }
        for(String input: inputIds)
        {
            VJoint copy = outputJoint.copyTree(input);
            inputJoints.put(input,copy);
        }
        this.outputJoint = outputJoint;
        this.id = id;
        selectInput(inputIds.get(0));        
    }
    
    public void selectInput(String name)
    {
        currentJoint = inputJoints.get(name);
        currentJointName = name;
        copier = VObjectTransformCopier.newInstanceFromVJointTree(currentJoint,outputJoint,"T1R");
    }
    
    public String getCurrentInput()
    {
        return currentJointName;
    }
    
    public VJoint getInput(String name)
    {
        return inputJoints.get(name);
    }
    
    public Set<String>getInputs()
    {
        return inputJoints.keySet();
    }
    
    @Override
    public void copy()
    {
        copier.copyConfig();        
    }    

    @Override
    public VJoint getAnimationVJoint()
    {
        return currentJoint;
    }

}
