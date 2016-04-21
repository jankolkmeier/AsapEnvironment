package hmi.environmentbase;

/**
 * Sents status to stdout
 * @author hvanwelbergen
 *
 */
public class StdOutStatusInformer implements StatusInformer
{

    @Override
    public void setStatus(String status)
    {
        System.out.println(status);        
    }

    @Override
    public void close()
    {
                
    }
}
