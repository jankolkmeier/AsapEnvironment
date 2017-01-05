package asap.rsbembodiments;

import hmi.animation.VJoint;
import hmi.faceanimation.FaceController;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import rsb.AbstractDataHandler;
import rsb.Event;
import rsb.Factory;
import rsb.Listener;
import rsb.RSBException;
import rsb.converter.DefaultConverterRepository;
import rsb.converter.ProtocolBufferConverter;
import rsb.patterns.EventCallback;
import rsb.patterns.LocalServer;
import asap.rsbembodiments.Rsbembodiments.AnimationData;
import asap.rsbembodiments.Rsbembodiments.AnimationDataConfigReply;
import asap.rsbembodiments.Rsbembodiments.AnimationDataConfigRequest;
import asap.rsbembodiments.Rsbembodiments.AnimationSelection;
import asap.rsbembodiments.util.VJointRsbUtils;

import com.google.common.primitives.Floats;

public class StubBodyAndFace
{
    private final LocalServer server;
    private final VJoint vjoint;
    private final FaceController faceController;

    @Getter
    private List<String> jointList = new ArrayList<String>();
    @Getter
    private List<String> morphList = new ArrayList<String>();
    
    private class JointDataConfigCallback extends EventCallback
    {
        @Override
        public Event invoke(final Event request) //throws Throwable
        {
            return new Event(AnimationDataConfigReply.class, AnimationDataConfigReply.newBuilder()
                    .setSkeleton(VJointRsbUtils.toRsbSkeleton(vjoint))
                    .addAllAvailableMorphs(faceController.getPossibleFaceMorphTargetNames()).build());
        }
    }

    public void deactivate() throws RSBException, InterruptedException
    {
        server.deactivate();
    }

    private class AnimationDataHandler extends AbstractDataHandler<AnimationData>
    {
        @Override
        public void handleEvent(AnimationData aData)
        {
            float q[] = Floats.toArray(aData.getJointQuatsList());
            for (int i = 0; i < jointList.size(); i++)
            {
                vjoint.getPart(jointList.get(i)).setRotation(q, i * 4);
            }
            if(aData.getRootTranslationCount()>0)
            {
                float tRoot[] = Floats.toArray(aData.getRootTranslationList());
                vjoint.setTranslation(tRoot);
            } 
            faceController.setMorphTargets(morphList.toArray(new String[morphList.size()]), Floats.toArray(aData.getMorphWeightsList()));
        }

    }

    private class AnimationSelectionHandler extends AbstractDataHandler<AnimationSelection>
    {
        @Override
        public void handleEvent(AnimationSelection aSelection)
        {
            jointList = new ArrayList<String>(aSelection.getSelectedJointsList());
            morphList = new ArrayList<String>(aSelection.getSelectedMorphsList());
        }
    }

    public StubBodyAndFace(VJoint root, FaceController fc, String characterId) throws RSBException, InterruptedException
    {
        this.vjoint = root;
        this.faceController = fc;
        final ProtocolBufferConverter<AnimationData> jointDataConverter = new ProtocolBufferConverter<AnimationData>(
                AnimationData.getDefaultInstance());
        final ProtocolBufferConverter<AnimationDataConfigRequest> jointDataReqConverter = new ProtocolBufferConverter<AnimationDataConfigRequest>(
                AnimationDataConfigRequest.getDefaultInstance());
        final ProtocolBufferConverter<AnimationDataConfigReply> jointDataConfigReplyConverter = new ProtocolBufferConverter<AnimationDataConfigReply>(
                AnimationDataConfigReply.getDefaultInstance());
        final ProtocolBufferConverter<AnimationSelection> animationSelection = new ProtocolBufferConverter<AnimationSelection>(
                AnimationSelection.getDefaultInstance());
        DefaultConverterRepository.getDefaultConverterRepository().addConverter(jointDataConverter);
        DefaultConverterRepository.getDefaultConverterRepository().addConverter(jointDataReqConverter);
        DefaultConverterRepository.getDefaultConverterRepository().addConverter(jointDataConfigReplyConverter);
        DefaultConverterRepository.getDefaultConverterRepository().addConverter(animationSelection);

        final Factory factory = Factory.getInstance();
        server = factory.createLocalServer(RSBEmbodimentConstants.ANIMATIONDATACONFIG_CATEGORY+"/"+characterId);
        server.activate();

        final Listener jointDataListener = factory.createListener(RSBEmbodimentConstants.ANIMATIONDATA_CATEGORY+"/"+characterId);
        final Listener jointSelectionListener = factory.createListener(RSBEmbodimentConstants.ANIMATIONSELECTION_CATEGORY+"/"+characterId);
        jointDataListener.activate();
        jointSelectionListener.activate();
        jointDataListener.addHandler(new AnimationDataHandler(), true);
        jointSelectionListener.addHandler(new AnimationSelectionHandler(), true);

        server.addMethod(RSBEmbodimentConstants.ANIMATIONDATACONFIG_REQUEST_FUNCTION, new JointDataConfigCallback());
    }
}
