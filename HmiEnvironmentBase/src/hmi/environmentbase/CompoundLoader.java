package hmi.environmentbase;

import java.util.Collection;

/**
 * A loader that contains other loaders 
 * @author hvanwelbergen
 *
 */
public interface CompoundLoader extends Loader
{
    Collection<? extends Loader> getParts();
}
