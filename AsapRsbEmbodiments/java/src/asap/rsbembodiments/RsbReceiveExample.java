package asap.rsbembodiments;

import rsb.AbstractDataHandler;
import rsb.Factory;
import rsb.Listener;
import rsb.converter.DefaultConverterRepository;
import rsb.converter.ProtocolBufferConverter;
import asap.rsbembodiments.Rsbembodiments.AnimationData;
import asap.rsbembodiments.Rsbembodiments.AnimationDataConfigReply;
import asap.rsbembodiments.Rsbembodiments.AnimationDataConfigRequest;
import asap.rsbembodiments.Rsbembodiments.AnimationSelection;

public class RsbReceiveExample extends AbstractDataHandler<AnimationData>
{
    @Override
    public void handleEvent(final AnimationData data)
    {
        System.out.println("jointData: "+data.getJointQuatsList());
    }

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
        
        final Factory factory = Factory.getInstance();
        final Listener listener = factory.createListener("/example/informer");
        listener.activate();

        try
        {
            listener.addHandler(new RsbReceiveExample(), true);

            // Wait for events.
            while (true)
            {
                Thread.sleep(1);
            }
        }
        finally
        {
            // Deactivate the listener after use.
            listener.deactivate();
        }
    }
}
