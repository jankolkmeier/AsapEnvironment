package asap.statusinformer;

import hmi.environmentbase.StatusInformer;

public class AsapRealizerIpaacaStatusInformer implements StatusInformer
{
    private IpaacaStatusInformer informer;

    public AsapRealizerIpaacaStatusInformer()
    {
        informer = new IpaacaStatusInformer(AsapRealizerIpaacaStatus.CATEGORY, AsapRealizerIpaacaStatus.KEY);
    }

    public void unload()
    {
        informer.close();
    }
    
    @Override
    public void setStatus(String status)
    {
        informer.setStatus(status);
    }
}
