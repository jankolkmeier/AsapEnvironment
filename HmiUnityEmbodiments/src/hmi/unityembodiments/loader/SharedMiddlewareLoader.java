package hmi.unityembodiments.loader;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;

import hmi.environmentbase.Environment;
import hmi.xml.XMLTokenizer;
import nl.utwente.hmi.middleware.Middleware;
import nl.utwente.hmi.middleware.loader.GenericMiddlewareLoader;
import lombok.Getter;

public class SharedMiddlewareLoader implements Environment {

	private String id;
    private HashMap<String, String> attrMap = null;
	private XMLTokenizer tokenizer;
	
	@Getter
    private Middleware middleware;
    
	public void load(String resources, String file) throws IOException {
		tokenizer = XMLTokenizer.forResource(resources, file);
        attrMap = tokenizer.getAttributes();
        for (Entry<String, String> kvp : attrMap.entrySet()) {
        	if (kvp.getKey().equals("id")) {
        		this.id = kvp.getValue();
        	}
        }

        tokenizer.takeSTag("SharedMiddlewareLoader");
        middleware = GenericMiddlewareLoader.load(tokenizer);
        tokenizer.takeETag("SharedMiddlewareLoader");
        
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}

	@Override
	public void requestShutdown() {
		// TODO MA - should we try to close middleware here?
	}

	@Override
	public boolean isShutdown() {
		return true;
	}

}
