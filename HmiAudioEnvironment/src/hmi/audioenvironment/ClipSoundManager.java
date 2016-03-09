package hmi.audioenvironment;

import java.io.InputStream;

/**
 * Sound manager that creates ClipWavs
 * @author hvanwelbergen
 *
 */
public class ClipSoundManager implements SoundManager
{

    @Override
    public void init()
    {
        
    }

    @Override
    public Wav createWav(InputStream inputStream, String source) throws WavCreationException
    {
        return new ClipWav(inputStream);
    }

    @Override
    public Wav createWav(InputStream inputStream) throws WavCreationException
    {
        return new ClipWav(inputStream);
    }

    @Override
    public void shutdown()
    {
                
    }

}
