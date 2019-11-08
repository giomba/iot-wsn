package netpp;

import java.io.*;
import java.net.URI;
import java.util.Date;

import javax.json.*;
import org.eclipse.californium.core.*;

public class Handler implements CoapHandler {
	
	private URI uri; 
	
	public Handler(URI uri) {
		this.uri = uri;
		System.err.println("[D] new handler for " + this.uri);
	}

	@Override
	public void onLoad(CoapResponse response) {
		String content = response.getResponseText();
		// System.out.println("[D]  resource: " + uri);
		// System.out.println("    new value: " + content);
		System.out.println("update" + new Date());
		
		JsonArray jsonSenML = null;
		try {
			InputStream is = new ByteArrayInputStream(content.getBytes());
			System.out.println("0");
			JsonReader reader = Json.createReader(is);
			System.err.println("1: " + content);
			jsonSenML = reader.readArray();
			System.out.println("2");
			reader.close();
			
			System.out.println("Java Json Object");
			System.out.println(jsonSenML.getJsonObject(0).getString("n"));
			System.out.println(jsonSenML.getJsonObject(0).getJsonNumber("v"));
		} catch (Exception ex) {
			System.err.println("[E] " + ex);
		}
	}

	@Override
	public void onError() {
		System.err.println("[E] failed " + uri);
	}

}
