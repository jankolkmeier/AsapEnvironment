package hmi.environmentbase;

/**
 * Informs of the status of some component. Could, for example, be used to inform connected components in the ipaaca middleware of the startup/failure/initialized status of AsapRealizer 
 * @author herwinvw
 */
public interface StatusInformer 
{
    void setStatus(String status);
}
