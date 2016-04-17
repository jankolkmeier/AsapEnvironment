package asap.statusinformer;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;

import com.google.common.collect.ImmutableSet;

import ipaaca.AbstractIU;
import ipaaca.HandlerFunctor;
import ipaaca.IUEventType;
import ipaaca.InputBuffer;
import ipaaca.util.communication.FutureIUs;

public class AsapRealizerIpaacaStatusListener
{
    private final FutureIUs futures;
    private final InputBuffer inBuffer;
    private AtomicReference<String> status = new AtomicReference<>("initialized");
    
    public AsapRealizerIpaacaStatusListener()
    {
        inBuffer = new InputBuffer("IpaacaStatusListener", ImmutableSet.of(AsapRealizerIpaacaStatus.CATEGORY));
        inBuffer.registerHandler(new HandlerFunctor()
        {
            @Override
            public void handle(AbstractIU iu, IUEventType type, boolean local)
            {
                status.set(iu.getPayload().get(AsapRealizerIpaacaStatus.KEY));                
            }
        
        });
        futures = new FutureIUs(AsapRealizerIpaacaStatus.CATEGORY,AsapRealizerIpaacaStatus.KEY);
    }
    
    public String getStatus()
    {
        return status.get();
    }
    
    public void waitForStatus(String status) throws InterruptedException
    {
        futures.take(status);
    }
    
    public void waitForStatus(String status, long timeout, TimeUnit unit) throws InterruptedException, TimeoutException
    {
       if(futures.take(status, timeout, unit)==null)
       {
           throw new TimeoutException();
       }
    }
    
    public void close()
    {
        futures.close();
        inBuffer.close();
    }
}