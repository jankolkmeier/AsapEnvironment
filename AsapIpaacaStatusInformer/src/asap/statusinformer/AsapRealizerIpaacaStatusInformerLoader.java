package asap.statusinformer;

import java.io.IOException;

import hmi.environmentbase.Environment;
import hmi.environmentbase.Loader;
import hmi.environmentbase.StatusInformerLoader;
import hmi.xml.XMLTokenizer;
import lombok.Getter;

public class AsapRealizerIpaacaStatusInformerLoader implements StatusInformerLoader
{
    @Getter
    private String id;
    private IpaacaStatusInformer informer;
    
    @Override
    public void readXML(XMLTokenizer tokenizer, String loaderId, String vhId, String vhName, Environment[] environments,
            Loader... requiredLoaders) throws IOException
    {
        this.id = loaderId;
        informer = new IpaacaStatusInformer(AsapRealizerIpaacaStatus.CATEGORY,AsapRealizerIpaacaStatus.KEY);
    }

    @Override
    public void unload()
    {
        informer.close();
    }

    @Override
    public IpaacaStatusInformer getStatusInformer()
    {
        return informer;
    }

}
