package asap.statusinformer;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.junit.Test;

/**
 * Integration tests for the AsapRealizerIpaacaStatusListener
 * @author herwinvw
 *
 */
public class AsapRealizerIpaacaStatusListenerIntegrationTest
{
    AsapRealizerIpaacaStatusListener listener = new AsapRealizerIpaacaStatusListener();
    
    @Test(timeout=1000)
    public void testWaitForStatus() throws InterruptedException
    {
        AsapRealizerIpaacaStatusInformer informer = new AsapRealizerIpaacaStatusInformer();
        informer.setStatus("initialized");
        listener.waitForStatus("initialized");
        informer.unload();
    }
    
    @Test(expected=TimeoutException.class)
    public void testWaitForStatusTimeout() throws InterruptedException, TimeoutException
    {
        AsapRealizerIpaacaStatusInformer informer = new AsapRealizerIpaacaStatusInformer();
        informer.setStatus("initialized");
        listener.waitForStatus("started",500, TimeUnit.MILLISECONDS);
        informer.unload();        
    }
}
