package asap.statusinformer;

import hmi.environmentbase.StatusInformer;
import ipaaca.LocalMessageIU;
import ipaaca.OutputBuffer;

/**
 * Submits status as a MessageIU on a specified category and under the key specified by statusKey 
 * @author herwinvw
 *
 */
public class IpaacaStatusInformer implements StatusInformer
{
    private final OutputBuffer outBuffer;
    private final String statusKey;
    private final String category;
    
    public IpaacaStatusInformer(String category, String statusKey)
    {
        this(category, statusKey, "default");
    }
    
    public IpaacaStatusInformer(String category, String statusKey, String channel)
    {
        this.category = category;
        this.statusKey = statusKey;
        outBuffer = new OutputBuffer("IpaacaStatusInformer", channel);
    }
    
    @Override
    public void setStatus(String status)
    {
        LocalMessageIU message = new LocalMessageIU(category);
        message.getPayload().put(statusKey, status);
        outBuffer.add(message);
    }
    
    public void close()
    {
        outBuffer.close();
    }
}
