package hmi.audioenvironment;


/**
 * Interface for the playback of (wav) audio
 * @author Herwin van Welbergen
 * @author Dennis Reidsma
 *
 */
public interface Wav
{

    float getVolume();
    void setVolume(float vol);
    /**
     * @param relTime time relative to the start of the Wav
     */
    void start(double relTime);
    
    /**
     * Stops and cleans up the Wav
     */
    void stop();
    
    /**
     * Play
     * @param relTime relative to start of Wav
     */
    void play(double relTime) throws WavPlayException;
    
    /**
     * Get the duration of the Wav in seconds
     * @return
     */
    double getDuration();
}
