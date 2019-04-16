package hmi.unityembodiments;

import static nl.utwente.hmi.middleware.helpers.JsonNodeBuilders.object;

import java.util.Timer;
import java.util.TimerTask;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import hmi.textembodiments.TextEmbodiment;
import nl.utwente.hmi.middleware.Middleware;

public class UnityTextEmbodiment implements TextEmbodiment {
	
	private Middleware middleware;
	private String id;
	private String charId;
	private Timer timer;
	public long lastUpdate;
	
	private String lastText;
	
	public UnityTextEmbodiment(Middleware middleware, String id, String charId) {
		this.middleware = middleware;
		this.id = id;
		this.charId = charId;
		timer = new Timer();
		timer.schedule(new ClearSubs(this), 1000, 1000);
		lastText = "";
	}
	
	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return id;
	}

	@Override
	public void setText(String text) {
		if (lastText.equals(text)) return;
		lastText = text;
		/*
		{
		    "msgType": "subtitles",
		    "cmd": "ShowSubtitle",
		    "agentId": "COUCH_M_1",
		    "subtitle": "The subtitle to depict on the GUI."
		}
		*/
    	ObjectNode msg = object(
    			UnityEmbodimentConstants.AUPROT_PROP_MSGTYPE, UnityEmbodimentConstants.AUPROT_MSGTYPE_SUBTITLES,
    			UnityEmbodimentConstants.AUPROT_PROP_CMD, UnityEmbodimentConstants.AUPROT_SUBTITLES_SHOW,
    			UnityEmbodimentConstants.AUPROT_PROP_AGENTID, this.charId).end();
    	msg.put(UnityEmbodimentConstants.AUPROT_SUBTITLES_CONTENT, text);
		middleware.sendData(msg);
		lastUpdate = System.currentTimeMillis();
		/*
		timer.cancel();
		timer.purge();
		timer = new Timer();*/
	}
	
	public void hideText() {
		/*
		{
		    "msgType": "subtitles",
		    "cmd": "HideSubtitle",
		    "agentId": "COUCH_M_1"
		}
		*/
    	JsonNode msg = object(
    			UnityEmbodimentConstants.AUPROT_PROP_MSGTYPE, UnityEmbodimentConstants.AUPROT_MSGTYPE_SUBTITLES,
    			UnityEmbodimentConstants.AUPROT_PROP_CMD, UnityEmbodimentConstants.AUPROT_SUBTITLES_HIDE,
    			UnityEmbodimentConstants.AUPROT_PROP_AGENTID, this.charId).end();
		middleware.sendData(msg);
	}
	
}

class ClearSubs extends TimerTask {
	UnityTextEmbodiment subs;
	public ClearSubs(UnityTextEmbodiment subs) {
		this.subs = subs;
	}
    public void run() {
    	if (System.currentTimeMillis() - subs.lastUpdate > 3000) {
    		subs.hideText();
    	}
    }
}
