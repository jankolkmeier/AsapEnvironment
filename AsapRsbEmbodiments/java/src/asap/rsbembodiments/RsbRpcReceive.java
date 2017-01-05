package asap.rsbembodiments;

import rsb.Event;
import rsb.Factory;
import rsb.RSBException;
import rsb.converter.DefaultConverterRepository;
import rsb.converter.ProtocolBufferConverter;
import rsb.patterns.EventCallback;
import rsb.patterns.LocalServer;
import asap.rsbembodiments.Rsbembodiments.AnimationData;
import asap.rsbembodiments.Rsbembodiments.AnimationDataConfigReply;
import asap.rsbembodiments.Rsbembodiments.AnimationDataConfigRequest;
import asap.rsbembodiments.Rsbembodiments.AnimationSelection;
import asap.rsbembodiments.Rsbembodiments.Skeleton;

import com.google.common.primitives.Floats;

public class RsbRpcReceive
{
    public static class EchoCallback extends EventCallback
    {
        @Override
        public Event invoke(final Event request) //throws Throwable
        {
            System.out.println("invoke");
            AnimationDataConfigRequest jdcr = (AnimationDataConfigRequest) request.getData();
            Skeleton skel = Skeleton.newBuilder().addJoints("HumanoidRoot").addParents("root")
                    .addAllLocalTransformation(Floats.asList(new float[16])).build();
            return new Event(AnimationDataConfigReply.class, AnimationDataConfigReply.newBuilder()
                    .setSkeleton(skel).build());
        }

    }

    public static void main(String args[]) throws RSBException
    {
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

        final LocalServer server = Factory.getInstance().createLocalServer("/example/server");
        server.activate();

        // Add method an "echo" method, implemented by EchoCallback.
        server.addMethod("jointDataConfigRequest", new EchoCallback());

        // Block until server.deactivate or process shutdown
        try {
            server.waitForShutdown();
        }
        catch (InterruptedException e)
        {
            Thread.interrupted();
        }
    }
}
