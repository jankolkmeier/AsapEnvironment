package asap.rsbembodiments;

import rsb.Factory;
import rsb.converter.DefaultConverterRepository;
import rsb.converter.ProtocolBufferConverter;
import rsb.patterns.RemoteServer;
import asap.rsbembodiments.Rsbembodiments.AnimationData;
import asap.rsbembodiments.Rsbembodiments.AnimationDataConfigReply;
import asap.rsbembodiments.Rsbembodiments.AnimationDataConfigRequest;
import asap.rsbembodiments.Rsbembodiments.AnimationSelection;

public class RsbRpcSend
{
    public static void main(final String[] args) throws Throwable
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
        
        // Get remote server object to call exposed request methods of
        // participants
        final RemoteServer server = Factory.getInstance().createRemoteServer("/example/server");
        server.activate();

        // Call remote method and deactivate the server.
        try
        {
            AnimationDataConfigReply reply = server.call("jointDataConfigRequest", Rsbembodiments.AnimationDataConfigRequest
                    .newBuilder().setCharacterId("billie").build());

            System.out.println("Server replied: " + reply.getSkeleton().getJoints(0));
        }
        finally
        {
            server.deactivate();
        }
    }
}
