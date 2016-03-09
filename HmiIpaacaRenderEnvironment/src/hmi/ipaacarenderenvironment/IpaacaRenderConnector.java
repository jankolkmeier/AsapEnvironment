package hmi.ipaacarenderenvironment;

import hmi.animation.VJoint;
import hmi.animation.VJointUtils;
import hmi.faceanimation.FaceController;
import hmi.math.Quat4f;
import hmi.math.Vec3f;
import ipaaca.AbstractIU;
import ipaaca.HandlerFunctor;
import ipaaca.IUEventHandler;
import ipaaca.IUEventType;
import ipaaca.Initializer;
import ipaaca.InputBuffer;
import ipaaca.LocalIU;
import ipaaca.OutputBuffer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import lombok.extern.slf4j.Slf4j;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

/**
 * Steers a FaceController and a VJoint on the basis of ipaaca messages
 * @author Herwin
 * 
 */
@Slf4j
public class IpaacaRenderConnector
{
    private final FaceController faceController;
    private final VJoint rootJoint;
    private final String id;
    private InputBuffer inBuffer;
    private OutputBuffer outBuffer;
    private final Object ipaacaLock = new Object();
    private AtomicReference<ImmutableList<String>> usedMorphs = new AtomicReference<>();
    private AtomicReference<ImmutableList<String>> usedJoints = new AtomicReference<>();
    
    static
    {
        Initializer.initializeIpaacaRsb();
    }
    
    public IpaacaRenderConnector(String id, FaceController controller, VJoint rootJoint)
    {
        this.faceController = controller;
        this.rootJoint = rootJoint;
        this.id = id;
        usedMorphs.set(new ImmutableList.Builder<String>().build());
    }

    private void submitNotify(boolean isNew)
    {
        LocalIU notifyIU = new LocalIU();
        notifyIU.setCategory("componentNotify");
        notifyIU.getPayload().put("name", "IpaacaEmbodiment");
        notifyIU.getPayload().put("function", "realizer");
        notifyIU.getPayload().put("send_categories", "componentNotify,jointDataConfigRequest");
        notifyIU.getPayload().put("recv_categories", "componentNotify,jointDataConfigReply");
        notifyIU.getPayload().put("state", isNew ? "new" : "old");
        synchronized (ipaacaLock)
        {
            outBuffer.add(notifyIU);
        }
        log.debug("Notify submitted");
    }

    private String toCommaSeperatedString(Collection<String> strCol)
    {
        StringBuffer buf = new StringBuffer();
        for (String str : strCol)
        {
            buf.append(",");
            buf.append(str);
        }
        if(!buf.toString().isEmpty())
        {
            return buf.toString().substring(1);
        }
        else return "";
    }

    private List<String> getParentList(List<VJoint> vjList)
    {
        List<String> parentList = new ArrayList<>();
        parentList.add("-");
        Iterator<VJoint> vjIter = vjList.iterator();
        vjIter.next(); // skip first
        while (vjIter.hasNext())
        {
            VJoint vj = vjIter.next();
            if (vj.getParent().getSid() != null)
            {
                parentList.add(vj.getParent().getSid());
            }
            else if (vj.getParent().getId() != null)
            {
                parentList.add(vj.getParent().getId());
            }
            else if (vj.getParent().getName() != null)
            {
                parentList.add(vj.getParent().getName());
            }
        }
        return parentList;
    }
    
    private List<String>getJointRotations(List<VJoint> vjList)
    {
        List<String> rotationList = new ArrayList<>();
        for(VJoint vj: vjList)
        {
            float[] r= Quat4f.getQuat4f();
            vj.getRotation(r);
            rotationList.add(r[0]+" "+r[1]+" "+r[2]+" "+r[3]);
        }
        return rotationList;
    }
    
    private List<String>getJointTranslations(List<VJoint> vjList)
    {
        List<String> translationList = new ArrayList<>();
        for(VJoint vj: vjList)
        {
            float[] t= Vec3f.getVec3f();
            vj.getTranslation(t);
            translationList.add(t[0]+" "+t[1]+" "+t[2]);
        }
        return translationList;
    }

    private void submitJointDataConfigRequest()
    {
        log.info("submitJointDataConfigRequest");
        
        LocalIU iu = new LocalIU();
        iu.setCategory("jointDataConfigRequest");
        iu.getPayload().put("morphs", toCommaSeperatedString(faceController.getPossibleFaceMorphTargetNames()));
        List<VJoint> jointList = rootJoint.getParts();
        iu.getPayload().put("joints", toCommaSeperatedString(VJointUtils.transformToSidList(jointList)));
        iu.getPayload().put("joint_parents", toCommaSeperatedString(getParentList(jointList)));
        iu.getPayload().put("joint_translations", toCommaSeperatedString(getJointTranslations(jointList)));
        iu.getPayload().put("joint_rotations", toCommaSeperatedString(getJointRotations(jointList)));
        synchronized (ipaacaLock)
        {
            outBuffer.add(iu);
        }
    }

    public void initialize()
    {
        ImmutableSet<String> categories = new ImmutableSet.Builder<String>().add("jointData", "jointDataConfigReply", "componentNotify")
                .build();
        synchronized (ipaacaLock)
        {
            inBuffer = new InputBuffer("ipaacarenderenvironment", categories);
            inBuffer.registerHandler(new IUEventHandler(new JointDataHandler(), EnumSet.of(IUEventType.ADDED), ImmutableSet.of("jointData")));
            inBuffer.registerHandler(new IUEventHandler(new JointDataConfigReplyHandler(), EnumSet.of(IUEventType.ADDED), ImmutableSet
                    .of("jointDataConfigReply")));
            inBuffer.registerHandler(new IUEventHandler(new JointComponentNotifyHandler(), EnumSet.of(IUEventType.ADDED), ImmutableSet
                    .of("componentNotify")));
            outBuffer = new OutputBuffer("ipaacarenderenvironment" + id);
        }
        submitNotify(true);
        submitJointDataConfigRequest();
    }

    class JointComponentNotifyHandler implements HandlerFunctor
    {
        @Override
        public void handle(AbstractIU iu, IUEventType type, boolean local)
        {
            log.info("Received componentNotify");
            if (iu.getPayload().get("state").equals("new"))
            {
                submitNotify(false);
                String recvCats = iu.getPayload().get("recv_categories");
                if (Arrays.asList(recvCats.split("\\s*,\\s*")).contains("jointDataConfigRequest"))
                {
                    submitJointDataConfigRequest();
                }
            }
        }
    }

    class JointDataConfigReplyHandler implements HandlerFunctor
    {
        @Override
        public void handle(AbstractIU iu, IUEventType type, boolean local)
        {
            String morphs = iu.getPayload().get("morphs_provided");
            String joints = iu.getPayload().get("joints_provided");
            usedMorphs.set(ImmutableList.copyOf(morphs.split("\\s*,\\s*")));
            usedJoints.set(ImmutableList.copyOf(joints.split("\\s*,\\s*")));
        }
    }

    class JointDataHandler implements HandlerFunctor
    {
        @Override
        public void handle(AbstractIU iu, IUEventType type, boolean local)
        {
            String morphData = iu.getPayload().get("morph_data");
            String jointData = iu.getPayload().get("joint_data");
            String jointValues[] = jointData.split("\\s+");
            String morphValues[] = morphData.split("\\s+");
            float[] values = new float[morphValues.length];
            if (morphValues.length == usedMorphs.get().size() && !morphData.equals(""))
            {
                for (int i = 0; i < morphValues.length; i++)
                {
                    values[i] = Float.parseFloat(morphValues[i])/100f;
                }
            }
            else
            {
                // TODO: exception?
            }
            faceController.setMorphTargets(usedMorphs.get().toArray(new String[morphValues.length]), values);

            if(jointData.equals(""))return;
            float m[] = new float[jointValues.length];
            for (int i = 0; i < jointValues.length; i++)
            {
                m[i] = Float.parseFloat(jointValues[i]);
            }

            int i = 0;
            for (String id : usedJoints.get())
            {
                float q[] = Quat4f.getQuat4f();
                Quat4f.setFromMat4f(q, 0, m, i * 32);
                i++;
                rootJoint.getPart(id).setRotation(q);
            }
        }
    }
}
