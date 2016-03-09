package hmi.audioenvironment;

import java.io.InputStream;

/**
 * Manages wav creation. Wavs are linked to sources; 
 * only one Wav can be played back at the same time. 
 * @author hvanwelbergen
 */
public interface SoundManager
{
    void init();
    /**
     * Create a wav on a given sound source
     */
    Wav createWav(InputStream inputStream, String source) throws WavCreationException;
    
    /**
     * Create a wav on a new (undefined) source 
     */
    Wav createWav(InputStream inputStream)  throws WavCreationException;
    
    /** Shutdown the sound manager */
    void shutdown();
}
