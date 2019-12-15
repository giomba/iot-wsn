package netpp;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Date;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;

import org.eclipse.californium.core.CoapHandler;
import org.eclipse.californium.core.CoapResponse;

public class Handler implements CoapHandler {
	
	private Node node;
	
	public Handler(Node node) {
		this.node = node;
		node.setStatus(Status.WAIT);
		System.err.println("[D] new handler for " + this.node.getURI());
	}

	@Override
	public void onLoad(CoapResponse response) {
		String content = response.getResponseText();
		System.out.println("[D] from " + this.node.getURI() + " update received at " + new Date() + " > " + content);
		
		this.node.setStatus(Status.ONLINE);
		
		JsonArray jsonSenML = null;
		try {
			InputStream is = new ByteArrayInputStream(content.getBytes());
			JsonReader reader = Json.createReader(is);
			jsonSenML = reader.readArray();
			reader.close();
			
			/* in this application, SenML contains only one resource -- see RFC8428 for further details */
			JsonObject res = jsonSenML.getJsonObject(0);
			String res_name = res.getString("n");	// name of resource
			int res_value = res.getJsonNumber("v").intValue();	// value of resource

			this.node.getResource().setName(res_name);
			this.node.getResource().setValue(res_value);
		} catch (Exception ex) {
			ex.printStackTrace();
			System.err.println("[E] " + ex);
		}
	}

	@Override
	public void onError() {
		node.setStatus(Status.OFFLINE);
		System.err.println("[E] failed " + this.node.getURI());
	}

}
