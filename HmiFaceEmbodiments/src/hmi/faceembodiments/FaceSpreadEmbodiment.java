package hmi.faceembodiments;

import hmi.faceanimation.FaceController;
import hmi.faceanimation.FaceControllerPose;
import hmi.faceanimation.model.MPEG4Configuration;

import java.util.ArrayList;
import java.util.Collection;

import lombok.Getter;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;

/**
 * Spreads the settings of the input face controller over all outputs. The input controller is constructed on creation.
 * @author hvanwelbergen
 * 
 */
public class FaceSpreadEmbodiment implements FaceEmbodiment
{
    private static final class CompoundFaceController implements FaceController
    {
        private Collection<? extends FaceController> controllers;

        public CompoundFaceController(Collection<? extends FaceController> controllers)
        {
            this.controllers = controllers;
        }

        @Override
        public void setMorphTargets(String[] targetNames, float[] weights)
        {
            for (FaceController c : controllers)
            {
                c.setMorphTargets(targetNames, weights);
            }
        }

        @Override
        public void addMorphTargets(String[] targetNames, float[] weights)
        {
            for (FaceController c : controllers)
            {
                c.addMorphTargets(targetNames, weights);
            }
        }

        @Override
        public void removeMorphTargets(String[] targetNames, float[] weights)
        {
            for (FaceController c : controllers)
            {
                c.removeMorphTargets(targetNames, weights);
            }
        }

        @Override
        public Collection<String> getPossibleFaceMorphTargetNames()
        {
            return controllers.iterator().next().getPossibleFaceMorphTargetNames();
        }

        @Override
        public void copy()
        {
            for (FaceController c : controllers)
            {
                c.copy();
            }
        }

        @Override
        public void setMPEG4Configuration(MPEG4Configuration config)
        {
            for (FaceController c : controllers)
            {
                c.setMPEG4Configuration(config);
            }

        }

        @Override
        public void addMPEG4Configuration(MPEG4Configuration config)
        {
            for (FaceController c : controllers)
            {
                c.addMPEG4Configuration(config);
            }
        }

        @Override
        public void removeMPEG4Configuration(MPEG4Configuration config)
        {
            for (FaceController c : controllers)
            {
                c.removeMPEG4Configuration(config);
            }
        }

        @Override
        public float getCurrentWeight(String targetName)
        {
            for (FaceController c : controllers)
            {
                if(c.getPossibleFaceMorphTargetNames().contains(targetName))
                {
                    return c.getCurrentWeight(targetName);
                }
            }
            return 0;
        }
    }

    @Getter
    private final String id;
    private final CompoundFaceController inputController;
    private final Collection<FaceControllerPose> outputCopiers;

    public static FaceSpreadEmbodiment createFromEmbodiments(String id, String input, Collection<? extends FaceEmbodiment> outputs)
    {
        return new FaceSpreadEmbodiment(id, input, Collections2.transform(outputs, new Function<FaceEmbodiment, FaceController>()
        {
            @Override
            public FaceController apply(FaceEmbodiment se)
            {
                return se.getFaceController();
            }
        }));
    }

    public FaceSpreadEmbodiment(String id, String input, Collection<FaceController> outputs)
    {
        this.id = id;
        if (outputs.isEmpty())
        {
            throw new RuntimeException("Cannot construct FaceSpreadEmbodiment with empty output list");
        }
        outputCopiers = new ArrayList<FaceControllerPose>();
        for (FaceController fc : outputs)
        {
            outputCopiers.add(new FaceControllerPose(fc));
        }
        inputController = new CompoundFaceController(outputCopiers);
    }

    @Override
    public void copy()
    {
        for (FaceControllerPose fp : outputCopiers)
        {
            fp.toTarget();
        }
    }

    @Override
    public FaceController getFaceController()
    {
        return inputController;
    }
}
