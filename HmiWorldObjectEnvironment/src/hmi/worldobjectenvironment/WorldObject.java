package hmi.worldobjectenvironment;

import hmi.animation.VJoint;

/**
 * Contains an entity in the world that can be pointed at, talked about etc.
 * @author welberge
 */
public interface WorldObject
{
    /**
     * Get the world position of the object
     * @param tr output: the world position of the object
     */
    void getWorldTranslation(float[] tr);

    /**
     * Get the position of the world object in the coordinate system of vj.
     * This joint and vj are assumed to be in the same joint tree, but can be in different branches of the tree.
     * @param tr output: the position of the world object
     */
    void getTranslation(float tr[], VJoint vj);

    /**
     * Get the position of the world object in the coordinate system of vj, minus the rotation of vj.
     * This joint and vj are assumed to be in the same joint tree, but can be in different branches of the tree.
     * @param tr output: the position of the world object
     */
    void getTranslation2(float tr[], VJoint vj);
    
    /**
     * Set the worldObject to position tr
     */
    void setTranslation(float tr[]);
}
