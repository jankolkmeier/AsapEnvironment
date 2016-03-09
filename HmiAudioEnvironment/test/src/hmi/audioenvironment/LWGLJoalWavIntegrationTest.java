package hmi.audioenvironment;
import static org.junit.Assert.*;

import java.nio.IntBuffer;

import hmi.util.Resources;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;

/**
 * Integration tests (with actual wav files) for the LWGLJoalWav
 * @author Herwin
 *
 */
public class LWGLJoalWavIntegrationTest
{
    @Before
    public void setup() throws LWJGLException
    {
        AL.create(null, 15, 22050, true);
    }
    
    @After
    public void tearDown()
    {
        AL.destroy();
    }
    private Resources res = new Resources("");
    
    private int getSourceState(int source)
    {
        return AL10.alGetSourcei(source, AL10.AL_SOURCE_STATE);
    }
    
    @Test
    public void testAnonymousSource() throws WavCreationException, InterruptedException
    {
        //doesn't really test anything, but you should hear "Well hello mister fanycpants" when running this        
        LWJGLJoalWav wav = new LWJGLJoalWav(res.getInputStream("FancyPants.wav"));
        wav.start(0);
        Thread.sleep(5000);
        wav.stop();        
    }
        
    @Test 
    public void testWithSource() throws InterruptedException, WavCreationException
    {
        IntBuffer source = BufferUtils.createIntBuffer(1);
        AL10.alGenSources(source);
        LWJGLJoalWav wav = new LWJGLJoalWav(res.getInputStream("FancyPants.wav"),source.get(0));
        wav.start(0);
        assertEquals(AL10.AL_PLAYING,getSourceState(source.get(0)));
        Thread.sleep(500);
        wav.stop();
        assertEquals(AL10.AL_STOPPED,getSourceState(source.get(0)));
    }
    
    @Test (expected=WavCreationException.class)
    public void testInvalidSource() throws WavCreationException
    {
        IntBuffer source = BufferUtils.createIntBuffer(1);
        AL10.alGenSources(source);
        new LWJGLJoalWav(res.getInputStream("invalid.wav"),source.get(0));
    }
}
