package hmi.environmentbase;

/**
 * loads a StatusInformer
 * @author herwinvw
 */
public interface StatusInformerLoader extends Loader
{
    StatusInformer getStatusInformer();
}
