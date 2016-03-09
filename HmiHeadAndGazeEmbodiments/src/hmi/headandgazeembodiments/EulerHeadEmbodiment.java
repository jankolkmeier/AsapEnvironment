package hmi.headandgazeembodiments;

import hmi.environmentbase.Embodiment;

/**
 * A simple head that can be steered with roll, pitch and yaw values
 * @author welberge
 *
 */
public interface EulerHeadEmbodiment extends Embodiment
{
    /**
     * Set the head roll, pitch and yaw (in degrees)
     * Implementations may omit setting one or more of these
     */
    void setHeadRollPitchYawDegrees(float roll, float pitch, float yaw);
    
    /**
     * Claim access to the head; other control on this embodiment may not override
     * anything set through setHeadRollPitchYawDegrees
     */
    void claimHeadResource();
    
    /**
     * Release head resouce claim set by claimHeadResource
     */
    void releaseHeadResource();    
}
