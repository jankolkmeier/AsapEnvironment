package hmi.faceembodiments;

import lombok.Data;

/**
 * Defines a single AU
 * @author welberge
 *
 */
@Data
public class AUConfig
{
    private final Side side;
    final int au;
    final float value;
	
    public Side getSide() {
		return side;
	}
	
	public int getAu() {
		return au;
	}

	public float getValue() {
		return value;
	}
}
