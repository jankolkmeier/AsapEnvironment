package asap.rsbembodiments;

import hmi.animation.Hanim;
import hmi.animation.Skeleton;
import hmi.animation.VJoint;
import hmi.animation.VJointUtils;
import hmi.animationembodiments.SkeletonEmbodiment;
import hmi.math.Mat3f;
import hmi.math.Mat4f;
import hmi.math.Quat4f;
import hmi.math.Vec3f;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.concurrent.GuardedBy;

import lombok.Getter;
import asap.rsbembodiments.Rsbembodiments.AnimationData;
import asap.rsbembodiments.util.VJointRsbUtils;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.primitives.Floats;

/**
 * Interfaces with an rsb graphical environment.
 * Currently rsb graphical environments are assumed to contain only one character.
 * @author hvanwelbergen
 * 
 */

public class RsbBodyEmbodiment implements SkeletonEmbodiment
{
    @Getter
    private String id;

    private final String characterId;
    private Object submitJointLock = new Object();
    private BiMap<String, String> renamingMap;
    private final RsbEmbodiment rsbEmbodiment;    

    @GuardedBy("submitJointLock")
    private VJoint submitJoint;

    private List<String> jointList = new ArrayList<String>();// same order as availableJoints
    @GuardedBy("submitJointLock")
    private Skeleton skel;
    private float[][] transformMatrices;

    public RsbBodyEmbodiment(String id, String characterId, RsbEmbodiment rsbEmbodiment)
    {
        this.id = id;
        this.characterId = characterId;
        this.rsbEmbodiment = rsbEmbodiment;
    }

    private void selectJoints()
    {
        List<String> jointSelection = new ArrayList<String>();
        for (String jointId : jointList)
        {
            if (renamingMap.inverse().containsKey(jointId))
            {
                jointSelection.add(renamingMap.inverse().get(jointId));
            }
            else
            {
                jointSelection.add(jointId);
            }
        }
        rsbEmbodiment.selectJoints(jointSelection);
    }

    private void updateJointLists(List<String> jointFilter)
    {
        for (String j : VJointUtils.transformToSidList(submitJoint.getParts()))
        {
            VJoint vj = submitJoint.getPart(j);
            if (vj == null)
            {
                vj = submitJoint.getPart(renamingMap.get(j));
            }

            if (vj != null && jointFilter.contains(vj.getSid()))
            {
                jointList.add(vj.getSid());
            }
        }
        selectJoints();
    }

    private void initJoints(BiMap<String, String> renamingMap, List<String> jointFilter)
    {
        this.renamingMap = renamingMap;
        synchronized (submitJointLock)
        {
            submitJoint = VJointRsbUtils.toVJoint(rsbEmbodiment.getSkeleton());
            // apply renaming
            for (VJoint vj : submitJoint.getParts())
            {
                if (renamingMap.get(vj.getSid()) != null)
                {
                    vj.setSid(renamingMap.get(vj.getSid()));
                }
            }

            submitJoint = submitJoint.getPart(Hanim.HumanoidRoot);

            VJoint vjDummy = new VJoint("dummy");
            vjDummy.addChild(submitJoint);
            VJointUtils.setHAnimPose(vjDummy);

            skel = new Skeleton(submitJoint.getId() + "skel", submitJoint);
            updateJointLists(jointFilter);
            skel.setJointSids(jointList);

            skel.setNeutralPose();
            transformMatrices = skel.getTransformMatricesRef();
            skel.setUpdateOnWrite(true);
        }
    }

    public void initialize(List<String> jointFilter)
    {
        rsbEmbodiment.initialize(characterId);
        initialize(HashBiMap.<String, String> create(), jointFilter);
    }

    public void initialize(BiMap<String, String> renamingMap, List<String> jointFilter)
    {
        initJoints(renamingMap, jointFilter);
    }

    public List<Float> getRootTranslation()
    {
        List<Float> rootTranslation = new ArrayList<Float>();
        if (jointList.contains(Hanim.HumanoidRoot))
        {
            float tr[] = Vec3f.getVec3f();
            submitJoint.getPart(Hanim.HumanoidRoot).getTranslation(tr);
            rootTranslation.addAll(Floats.asList(tr));
        }
        return rootTranslation;
    }

    public List<Float> getJointQuats()
    {
        List<Float> jointData = new ArrayList<>();
        synchronized (submitJointLock)
        {
            skel.putData();
            skel.getData();
            float q[] = Quat4f.getQuat4f();
            float m[] = Mat4f.getMat4f();

            // convert global transformations into local ones
            for (int i = 0; i < jointList.size(); i++)
            {
                VJoint vj = submitJoint.getPartBySid(jointList.get(i));
                VJoint vjParent = vj.getParent();

                if (vjParent == null || vj.getSid().equals(Hanim.HumanoidRoot))
                {
                    Mat4f.set(m, transformMatrices[i]);
                }
                else
                {
                    float[] pInverse = Mat4f.getMat4f();
                    if (jointList.contains(vjParent.getSid()))
                    {
                        Mat4f.invertRigid(pInverse, transformMatrices[jointList.indexOf(vjParent.getSid())]);                        
                    }
                    else
                    {
                        // FIXME: does not take into account inverse binds between parent and root
                        Mat4f.invertRigid(pInverse, vjParent.getGlobalMatrix());
                    }
                    Mat4f.mul(m, pInverse, transformMatrices[i]);                    
                }
                
                //Quat4f.setFromMat4f(q, m); -> this somehow doesn't work well for some rotations...
                float scalemat[] = Mat3f.getMat3f();
                float translation[] = Vec3f.getVec3f();
                Mat4f.decomposeToTRSMat3f(m, translation, q, scalemat);                
                jointData.addAll(Floats.asList(q));
            }
        }
        return jointData;
    }

    @Override
    public void copy()
    {
        // construct float list for rotations, send with informer
        AnimationData jd = AnimationData.newBuilder().addAllRootTranslation(getRootTranslation()).addAllJointQuats(getJointQuats()).build();
        rsbEmbodiment.sendAnimationData(jd);
    }

    @Override
    public VJoint getAnimationVJoint()
    {
        return submitJoint;
    }

    public void shutdown()
    {
        rsbEmbodiment.shutdown();
    }
}
