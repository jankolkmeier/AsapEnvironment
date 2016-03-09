package asap.rsbembodiments;

import java.util.Arrays;

import rsb.Factory;
import rsb.Informer;
import rsb.RSBException;
import rsb.converter.DefaultConverterRepository;
import rsb.converter.ProtocolBufferConverter;
import asap.rsbembodiments.Rsbembodiments.AnimationData;
import asap.rsbembodiments.Rsbembodiments.AnimationDataConfigReply;
import asap.rsbembodiments.Rsbembodiments.AnimationDataConfigRequest;
import asap.rsbembodiments.Rsbembodiments.AnimationSelection;

public class RsbSendExample
{
    public static void main(String args[]) throws RSBException, InterruptedException
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
        final Informer<AnimationData> informer = factory.createInformer("/example/informer/blah");
        informer.activate();

        AnimationData jd = AnimationData.newBuilder().addAllJointQuats(Arrays.asList(1f,2f,3f,4f)).build();
        informer.send(jd);
        informer.deactivate();
    }
}
