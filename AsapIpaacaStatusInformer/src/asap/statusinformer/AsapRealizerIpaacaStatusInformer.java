package asap.statusinformer;

import hmi.environmentbase.StatusInformer;

/**
 * Submits AsapRealizer status information over ipaaca messages
 * @author hvanwelbergen
 *
 */
public class AsapRealizerIpaacaStatusInformer implements StatusInformer
{
    private IpaacaStatusInformer informer;

    public AsapRealizerIpaacaStatusInformer()
    {
        informer = new IpaacaStatusInformer(AsapRealizerIpaacaStatus.CATEGORY, AsapRealizerIpaacaStatus.KEY);
    }

    @Override
    public void close()
    {
        informer.close();
    }
    
    @Override
    public void setStatus(String status)
    {
        informer.setStatus(status);
    }
}
