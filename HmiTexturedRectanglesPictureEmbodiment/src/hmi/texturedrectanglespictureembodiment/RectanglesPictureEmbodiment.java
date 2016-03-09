package hmi.texturedrectanglespictureembodiment;

import hmi.animation.VJoint;
import hmi.graphics.opengl.GLTextures;
import hmi.renderenvironment.HmiRenderEnvironment;

import java.util.HashMap;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

/**
 * Provides a 3D rectangle on which pictures can be shown 
 * @author Herwin
 */
public class RectanglesPictureEmbodiment
{
    private final HmiRenderEnvironment renderEnvironment;
    private final VJoint rootJoint;
    private NavigableMap<Float,String> layerMap = new TreeMap<Float,String>();
    private Map<String,String> textureMap = new HashMap<String,String>();
    
    
    public RectanglesPictureEmbodiment(HmiRenderEnvironment env, VJoint root)
    {
        renderEnvironment = env;
        rootJoint = root;
    }
    
    public void preloadImage(String imageId, String resourcePath, String fileName)
    {
        GLTextures.addTextureDirectory(resourcePath);
        GLTextures.getGLTexture(fileName);
        textureMap.put(imageId, fileName);
    }
    
    public void setImage(String imageId, float layer)
    {
        if(!textureMap.containsKey(imageId))
        {
            throw new RuntimeException("image "+imageId+" not preloaded");
        }
        VJoint vj = renderEnvironment.loadTexturedRectangle(constructId(imageId,layer), 1, 1, textureMap.get(imageId));
        rootJoint.addChild(vj);
        layerMap.put(layer, imageId);
        depthSort();
    }
    
    public void removeImage(float layer)
    {
        if(layerMap.containsKey(layer))
        {
            String id = constructId(layerMap.get(layer),layer);
            rootJoint.removeChild(renderEnvironment.getObjectRootJoint(id));
            renderEnvironment.unloadObject(id);
        }
        layerMap.remove(layer);
        depthSort();
    }
    
    private String constructId(String imageId, float layer)
    {
        return imageId+layer;
    }
    
    private void depthSort()
    {
        float depth = 0;
        for(Float f:layerMap.navigableKeySet())
        {
            renderEnvironment.getObjectRootJoint(constructId(layerMap.get(f),f)).setTranslation(0,0,depth);
            depth -= 0.001f;
        }
    }
}
