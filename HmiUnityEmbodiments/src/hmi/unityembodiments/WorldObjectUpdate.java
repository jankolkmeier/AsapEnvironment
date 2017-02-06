package hmi.unityembodiments;

/**
 * Updates from the WorldObjects tracked in Untiy.
 * @author jankolkmeier@gmail.com
 */
public class WorldObjectUpdate
{
    public String id;
    public float[] data;

    public WorldObjectUpdate(String id, float[] data)
    {
        this.id = id;
        this.data = data;
    }
}
