package asap.rsbembodiments;

import hmi.animation.Hanim;
import hmi.animation.VJoint;
import hmi.environment.bodyandfaceembodiments.BodyAndFaceEmbodiment;
import hmi.faceanimation.FaceController;
import hmi.faceembodiments.EyelidMorpherEmbodiment;
import hmi.math.Quat4f;

import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;
import asap.rsbembodiments.Rsbembodiments.AnimationData;

/**
 * Steers a body and a face through a rsb renderer
 * @author hvanwelbergen
 * 
 */
public class RsbBodyAndFaceEmbodiment implements BodyAndFaceEmbodiment
{
    @Getter
    private final String id;
    private final String characterId;
    private final RsbFaceEmbodiment faceEmbodiment;
    private final RsbBodyEmbodiment bodyEmbodiment;
    private final RsbEmbodiment rsbEmbodiment;

    @Setter
    private EyelidMorpherEmbodiment eyelidMorpher = new EyelidMorpherEmbodiment("", new ArrayList<String>());

    public RsbBodyAndFaceEmbodiment(String id, String characterId, RsbEmbodiment rsbEmbodiment, RsbFaceEmbodiment faceEmbodiment,
            RsbBodyEmbodiment bodyEmbodiment)
    {
        this.id = id;
        this.characterId = characterId;
        this.rsbEmbodiment = rsbEmbodiment;
        this.faceEmbodiment = faceEmbodiment;
        this.bodyEmbodiment = bodyEmbodiment;
    }

    @Override
    public FaceController getFaceController()
    {
        return faceEmbodiment.getFaceController();
    }

    @Override
    public void copy()
    {
        synchronized (faceEmbodiment.getFaceController())
        {
            VJoint vjRightEye = getAnimationVJoint().getPartBySid(Hanim.r_eyeball_joint);
            VJoint vjLeftEye = getAnimationVJoint().getPartBySid(Hanim.l_eyeball_joint);
            if (vjRightEye != null && vjLeftEye != null)
            {
                float qRight[] = Quat4f.getQuat4f();
                float qLeft[] = Quat4f.getQuat4f();
                vjRightEye.getRotation(qRight);
                vjLeftEye.getRotation(qLeft);
                eyelidMorpher.setEyeLidMorph(qLeft, qRight, faceEmbodiment.getFaceController());
            }
            rsbEmbodiment.sendAnimationData(AnimationData.newBuilder().setCharacterId(characterId)
                    .addAllRootTranslation(bodyEmbodiment.getRootTranslation()).addAllJointQuats(bodyEmbodiment.getJointQuats())
                    .addAllMorphWeights(faceEmbodiment.getMorphValues()).build());
        }
    }

    @Override
    public VJoint getAnimationVJoint()
    {
        return bodyEmbodiment.getAnimationVJoint();
    }
}
