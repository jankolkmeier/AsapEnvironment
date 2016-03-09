package hmi.audioenvironment;

import java.io.InputStream;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Map;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;

/**
 * SoundManager that creates LWJGLJoalWav wavs
 * @author hvanwelbergen
 *
 */
public class LWJGLJoalSoundManager implements SoundManager
{
    private Map<String, IntBuffer> sourceMap = new HashMap<String, IntBuffer>();

    @Override
    public void init()
    {
        try
        {
            AL.create(null, 15, 22050, true);            
        }
        catch (LWJGLException e)
        {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Wav createWav(InputStream inputStream, String sourceId) throws WavCreationException
    {
        IntBuffer source = sourceMap.get(sourceId);
        if (source == null)
        {
            source = BufferUtils.createIntBuffer(1);
            AL10.alGenSources(source);
            
            /*
            int error = AL10.alGetError();
            if (error != AL10.AL_NO_ERROR)
            {
                throw new WavCreationException("AL10 error " + error, null);
            }
            */
            sourceMap.put(sourceId, source);
        }
        return new LWJGLJoalWav(inputStream, source.get(0));
    }

    @Override
    public Wav createWav(InputStream inputStream) throws WavCreationException
    {
        return new LWJGLJoalWav(inputStream);
    }

    @Override
    public void shutdown()
    {
        for (IntBuffer source : sourceMap.values())
        {
            AL10.alDeleteSources(source);
        }
        AL.destroy();
    }

}
