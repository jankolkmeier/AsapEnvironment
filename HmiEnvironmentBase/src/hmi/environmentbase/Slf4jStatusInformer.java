package hmi.environmentbase;

import lombok.extern.slf4j.Slf4j;

/**
 * Sends status as a slf4j info message
 * @author hvanwelbergen
 *
 */
@Slf4j
public class Slf4jStatusInformer implements StatusInformer
{

    @Override
    public void setStatus(String status)
    {
        log.info(status);
    }

    @Override
    public void close()
    {
                
    }
}
