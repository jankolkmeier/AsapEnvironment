package asap.rsbembodiments;

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

import com.google.common.primitives.Floats;

/**
 * Testing stub for a FaceController steered through rsb
 * @author hvanwelbergen
 */
public class StubFace
{
    private final LocalServer server;
    private final FaceController faceController;

    @Getter
    private List<String> morphList = new ArrayList<String>();

    private class MorphDataConfigCallback extends EventCallback
    {
        @Override
        public Event invoke(final Event request) throws Throwable
        {

            return new Event(AnimationDataConfigReply.class, AnimationDataConfigReply.newBuilder()
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
            faceController.setMorphTargets(morphList.toArray(new String[morphList.size()]), Floats.toArray(aData.getMorphWeightsList()));
        }

    }

    private class AnimationSelectionHandler extends AbstractDataHandler<AnimationSelection>
    {
        @Override
        public void handleEvent(AnimationSelection aSelection)
        {
            morphList = new ArrayList<String>(aSelection.getSelectedMorphsList());
        }
    }

    public StubFace(FaceController fc, String characterId) throws RSBException, InterruptedException
    {
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

        final Listener faceDataListener = factory.createListener(RSBEmbodimentConstants.ANIMATIONDATA_CATEGORY+"/"+characterId);
        final Listener morphSelectionListener = factory.createListener(RSBEmbodimentConstants.ANIMATIONSELECTION_CATEGORY+"/"+characterId);
        faceDataListener.activate();
        morphSelectionListener.activate();
        faceDataListener.addHandler(new AnimationDataHandler(), true);
        morphSelectionListener.addHandler(new AnimationSelectionHandler(), true);

        server.addMethod(RSBEmbodimentConstants.ANIMATIONDATACONFIG_REQUEST_FUNCTION, new MorphDataConfigCallback());
    }
}
