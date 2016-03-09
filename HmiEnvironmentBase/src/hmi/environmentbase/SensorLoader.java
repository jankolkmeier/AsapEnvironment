package hmi.environmentbase;


/**
 * Loader that provides a sensor
 * @author welberge
 */
public interface SensorLoader extends Loader
{
    Sensor getSensor();
}
