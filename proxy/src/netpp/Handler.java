package netpp;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URI;
import java.util.Date;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;

import org.eclipse.californium.core.CoapHandler;
import org.eclipse.californium.core.CoapResponse;

public class Handler implements CoapHandler {
	
	private URI uri;
	private Resource resource;
	
	public Handler(URI uri, Resource resource) {
		this.uri = uri;
		this.resource = resource;
		System.err.println("[D] new handler for " + this.uri);
	}

	@Override
	public void onLoad(CoapResponse response) {
		String content = response.getResponseText();
		System.out.println("[D] from " + this.uri + " update received at " + new Date() + " > " + content);
		
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

			resource.setName(res_name);
			resource.setValue(res_value);
		} catch (Exception ex) {
			ex.printStackTrace();
			System.err.println("[E] " + ex);
		}
	}

	@Override
	public void onError() {
		System.err.println("[E] failed " + uri);
	}

}
