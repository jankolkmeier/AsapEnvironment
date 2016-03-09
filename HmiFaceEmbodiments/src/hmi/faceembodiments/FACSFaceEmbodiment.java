package hmi.faceembodiments;

import hmi.environmentbase.Embodiment;

/**
 * Steers a face using Ekman's FACS
 * @author welberge
 * 
 */
public interface FACSFaceEmbodiment extends Embodiment
{
    /**
     * Set multiple aus
     */
    void setAUs(AUConfig... configs);
}
