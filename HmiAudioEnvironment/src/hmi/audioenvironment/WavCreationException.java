package hmi.audioenvironment;

public class WavCreationException extends Exception
{
    private static final long serialVersionUID = 1L;
    private final Wav w;
    
    public WavCreationException(String str, Wav w,Exception ex)
    {
        this(str,w);
        initCause(ex);
    }
    
    public WavCreationException(String str, Wav w)
    {
        super(str);
        this.w = w;        
    }
    
    public final Wav getWav()
    {
        return w;
    }
}
