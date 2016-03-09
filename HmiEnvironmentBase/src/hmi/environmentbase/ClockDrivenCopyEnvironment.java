package hmi.environmentbase;

import hmi.util.AnimationSync;
import hmi.util.ClockListener;
import hmi.util.SystemClock;

import java.util.HashSet;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.jcip.annotations.GuardedBy;

/** For example, when no HmiRender env, and we need to copy vjoint info to relion.... */
@Slf4j
public class ClockDrivenCopyEnvironment implements CopyEnvironment, ClockListener
{
    @Getter
    @Setter
    private String id = null;

    private SystemClock theClock = null;

    private boolean isShutdown = false;

    @GuardedBy("itself")
    private Set<CopyEmbodiment> copyEmbodiments = new HashSet<CopyEmbodiment>();
    private long tickSize;

    public ClockDrivenCopyEnvironment(long tickSize)
    {
        this.tickSize = tickSize;
    }

    public void initTime(double initTime)
    {
        copy();
    }

    public void time(double currentTime)
    {
        copy();
    }

    @Override
    public void requestShutdown()
    {
    	log.debug("Shutdown initiated");
        theClock.terminate();
        isShutdown = true;
    	log.debug("Shutdown finished");
    }

    @Override
    public boolean isShutdown()
    {
        return isShutdown;
    }

    protected void copy()
    {
        synchronized (AnimationSync.getSync())
        {
            synchronized (copyEmbodiments)
            {
                for (CopyEmbodiment ce : copyEmbodiments)
                {
                    ce.copy();
                }
            }
        }
    }

    public void addCopyEmbodiment(CopyEmbodiment ce)
    {
        synchronized (copyEmbodiments)
        {
            copyEmbodiments.add(ce);
        }
    }

    public void removeCopyEmbodiment(CopyEmbodiment ce)
    {
        synchronized (copyEmbodiments)
        {
            copyEmbodiments.remove(ce);
        }
    }

    /**
     * Creates a SystemClock with tickSize and hooks up this ClockDrivenCopyEnvironment to it (that is, the ClockDrivenCopyEnvironment will now
     * call copy at all its CopyEmbodiments at rate tickSize).
     */
    public void init()
    {
        theClock = new SystemClock(tickSize);
        theClock.addClockListener(this);
        theClock.start();

    }
}
