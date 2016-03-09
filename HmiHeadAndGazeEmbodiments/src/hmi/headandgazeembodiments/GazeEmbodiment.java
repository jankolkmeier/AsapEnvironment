package hmi.headandgazeembodiments;

import hmi.environmentbase.Embodiment;

/**
 * Sets the current position of the gaze target 
 * @author welberge
 */
public interface GazeEmbodiment extends Embodiment
{
    void setGazePosition(float[] target);
    void setGazeRollPitchYawDegrees(float roll, float pitch, float yaw);
}
