package hmi.unityembodiments.loader;

import hmi.environmentbase.Embodiment;
import hmi.environmentbase.EmbodimentLoader;
import hmi.environmentbase.Environment;
import hmi.environmentbase.Loader;
import hmi.util.ArrayUtils;
import hmi.xml.XMLTokenizer;

import hmi.unityembodiments.UnityEmbodiment;
import hmi.unityembodiments.UnityTextEmbodiment;

import java.io.IOException;

public class UnityTextEmbodimentLoader implements EmbodimentLoader {

	private String id = "";
    private UnityTextEmbodiment ute = null;
    private UnityEmbodiment ue = null;
    
	@Override
	public String getId() {
		return id;
	}

	@Override
    public void readXML(XMLTokenizer tokenizer, String loaderId, String vhId, String vhName, Environment[] environments,
            Loader... requiredLoaders) throws IOException {
        id = loaderId;
        
        for (EmbodimentLoader e : ArrayUtils.getClassesOfType(requiredLoaders, EmbodimentLoader.class)) {
            if (e.getEmbodiment() instanceof UnityEmbodiment) {
                ue = (UnityEmbodiment) e.getEmbodiment();
            }
        }
        if (ue == null) {
            throw new RuntimeException("UnityTextEmbodimentLoader requires an EmbodimentLoader containing a MiddlewareEmbodiment");
        }
        while (!tokenizer.atETag("Loader")) {
            readSection(tokenizer);
        }
        
        ute = new UnityTextEmbodiment(ue.getMiddleware(), id, ue.getCharId());
	}
	
    protected void readSection(XMLTokenizer tokenizer) throws IOException  {	
    	throw tokenizer.getXMLScanException("Unknown tag in UnityTextEmbodimentLoader content");
    }

	@Override
	public void unload() {
	}

	@Override
	public Embodiment getEmbodiment() {
		return ute;
	}

}
