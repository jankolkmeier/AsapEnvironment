package hmi.mixedanimationenvironment;

/**
 * Interface for a player that plays on a mixed physical/kinematic body
 * @author hvanwelbergen
 */
public interface MixedAnimationPlayer
{
    /**
     * Copy the physical rotations etc. from the physical body to the kinematic body
     */
    void copyPhysics();
    
    void playStep(double time);
}
