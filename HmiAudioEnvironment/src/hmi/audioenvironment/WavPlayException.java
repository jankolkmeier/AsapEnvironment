package hmi.audioenvironment;

public class WavPlayException extends Exception
{
    private static final long serialVersionUID = 1L;
    private final Wav w;
    
    public WavPlayException(String str, Wav w, Exception ex)
    {
        this(str,w);
        initCause(ex);
    }
    
    public WavPlayException(String str, Wav w)
    {
        super(str);
        this.w = w;        
    }
    
    public final Wav getWav()
    {
        return w;
    }
}
