package hmi.faceembodiments;

import hmi.environmentbase.InputSwitchEmbodiment;
import hmi.faceanimation.FaceController;
import hmi.faceanimation.FaceControllerPose;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import lombok.Getter;

/**
 * Redirects the MPEG4/morph values from a selected input FaceController to a provided output FaceController. Input FaceControllers are constructed on creation.
 * @author hvanwelbergen
 * 
 */
public class FaceSwitchEmbodiment implements FaceEmbodiment, InputSwitchEmbodiment
{
    @Getter
    private final String id;
    private FaceControllerPose currentFaceController;
    private Map<String, FaceControllerPose> inputControllers = new HashMap<String, FaceControllerPose>();
    private String currentControllerName;
    
    public FaceSwitchEmbodiment(String id, List<String> inputIds, FaceController outputController)
    {
        if (inputIds.isEmpty())
        {
            throw new RuntimeException("Cannot construct FaceSwitchEmbodiment with empty input list");
        }
        this.id = id;
        
        for(String input: inputIds)
        {
            FaceControllerPose fp = new FaceControllerPose(outputController);
            inputControllers.put(input,fp);
        }
        selectInput(inputIds.get(0));
    }

    @Override
    public void copy()
    {
        currentFaceController.toTarget();
        //currentFaceController.copy();
    }

    public void selectInput(String name)
    {
        currentFaceController = inputControllers.get(name);
        currentControllerName = name;
    }

    public FaceController getInput(String name)
    {
        return inputControllers.get(name);
    }
    
    public Set<String>getInputs()
    {
        return inputControllers.keySet();
    }
    
    public String getCurrentInput()
    {
        return currentControllerName;
    }
    
    @Override
    public FaceController getFaceController()
    {
        return currentFaceController;
    }

}
