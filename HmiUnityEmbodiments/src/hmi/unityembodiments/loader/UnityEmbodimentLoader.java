package hmi.unityembodiments.loader;

import hmi.unityembodiments.UnityEmbodiment;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

import hmi.environmentbase.CopyEnvironment;
import hmi.environmentbase.Embodiment;
import hmi.environmentbase.EmbodimentLoader;
import hmi.environmentbase.Environment;
import hmi.environmentbase.Loader;
import hmi.worldobjectenvironment.WorldObjectEnvironment;
import hmi.xml.XMLScanException;
import hmi.xml.XMLStructureAdapter;
import hmi.xml.XMLTokenizer;
import lombok.extern.slf4j.Slf4j;
import nl.utwente.hmi.middleware.Middleware;
import nl.utwente.hmi.middleware.loader.GenericMiddlewareLoader;

/**
 * Loads a UnityEmbodiment
 * @author jankolkmeier@gmail.com
 */
@Slf4j
public class UnityEmbodimentLoader implements EmbodimentLoader
{

    UnityEmbodiment ue;

    @Override
    public String getId()
    {
        return ue.getId();
    }

    @Override
    public void readXML(XMLTokenizer tokenizer, String loaderId, String vhId, String vhName, Environment[] environments,
            Loader... requiredLoaders) throws IOException
    {
        boolean useBinary = false;
        WorldObjectEnvironment woe = null;
        CopyEnvironment ce = null;
        
        String sharedMiddlewareId = null;

        Middleware m = null;

        while (!tokenizer.atETag("Loader")) {
	        if (tokenizer.atSTag("MiddlewareOptions")) {
	        	m = readMiddlewareOptions(tokenizer);
	        } else if (tokenizer.atSTag("SharedMiddleware")) {
	        	sharedMiddlewareId = tokenizer.getAttribute("id");
                tokenizer.takeSTag("SharedMiddleware");
                tokenizer.takeETag("SharedMiddleware");
	        } else {
		    	throw new XMLScanException("UnityMechanimEmbodimentLoader found unknown option: "+tokenizer.getTagName());
	        }
        }
        
        for (Environment e : environments)
        {
            if (e instanceof CopyEnvironment) ce = (CopyEnvironment) e;
            else if (e instanceof WorldObjectEnvironment) woe = (WorldObjectEnvironment) e;
            if (e instanceof SharedMiddlewareLoader && ((SharedMiddlewareLoader) e).getId().equals(sharedMiddlewareId)) {
            	m = ((SharedMiddlewareLoader) e).getMiddleware();
            }
        }
        if (ce == null)
        {
            throw new RuntimeException("UnityMechanimEmbodiment requires an Environment of type CopyEnvironment");
        }
        if (woe == null)
        {
            throw new RuntimeException("UnityMechanimEmbodiment requires an Environment of type WorldObjectEnvironment");
        }
        
        if (m == null && sharedMiddlewareId != null) {
	    	throw new XMLScanException("UnityMechanimEmbodiment didn't load Middleware. No SharedMiddleware with id "+sharedMiddlewareId+" in Environment");
        } else if (m == null) {
	    	throw new XMLScanException("UnityMechanimEmbodiment didn't load Middleware. User MiddleWareOptions or SharedMiddleware");
        }

        ue = new UnityEmbodiment(vhId, loaderId, m, useBinary, woe, ce);
        log.info("Waiting for AgentSpec...");
        
        while (!ue.isConfigured())
        {
            ue.SendAgentSpecRequest(vhId, "/scene");
            try
            {
                Thread.sleep(1000);
            }
            catch (InterruptedException e1)
            {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
        ce.addCopyEmbodiment(ue);
        log.info("Registered unityembodiment with copyenvironment");
    }

    
    private Middleware readMiddlewareOptions(XMLTokenizer tokenizer) throws IOException {
        HashMap<String, String> attrMap = tokenizer.getAttributes();
        XMLStructureAdapter adapter = new XMLStructureAdapter();
        String loaderclass = adapter.getRequiredAttribute("loaderclass", attrMap, tokenizer);
        tokenizer.takeSTag("MiddlewareOptions");
        Properties props = new Properties();
        while (tokenizer.atSTag("MiddlewareProperty"))
        {
            XMLStructureAdapter adapter2 = new XMLStructureAdapter();
            props.put(adapter2.getRequiredAttribute("name", attrMap, tokenizer),
                    adapter2.getRequiredAttribute("value", attrMap, tokenizer));
            tokenizer.takeSTag("MiddlewareProperty");
            tokenizer.takeETag("MiddlewareProperty");
        }
        GenericMiddlewareLoader gml = new GenericMiddlewareLoader(loaderclass, props);
        tokenizer.takeETag("MiddlewareOptions");
        return gml.load();
    }
    
    @Override
    public void unload()
    {
        ue.shutdown();
    }

    @Override
    public Embodiment getEmbodiment()
    {
        return ue;
    }

}
