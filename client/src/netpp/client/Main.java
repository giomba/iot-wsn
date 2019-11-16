package netpp.client;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.elements.exception.ConnectorException;

public class Main {

	public static void main(String[] args) {
		if (args.length < 1) {
			System.err.println("[F] Bad parameters");
			System.err.println("Usage: ");
			System.err.println("$ exe <url>");
			System.err.println();
			System.err.println("Example:");
			System.err.println("$ exe coap://[2001:db8::0]/urn:it.unipi.ing.ce.netpp:c00000003");
			System.exit(1);
		}
		
		/* getting parameter from CLI */
		final String url = args[0];
		
		/* subscribe to every node specified */
		System.err.println("[I] GET " + url);
		
		CoapClient client = new CoapClient(url);
		CoapResponse response = null;
		try {
			response = client.get();
			if (!response.isSuccess()) {
				System.err.println("[E] can not access resource");
				System.exit(1);
			}
		} catch (ConnectorException | IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		/* parse SenML JSON */
		byte[] payload = response.getPayload();
		ByteArrayInputStream bis = new ByteArrayInputStream(payload);
		JsonReader reader = Json.createReader(bis);
		JsonArray senml = reader.readArray();
		JsonObject measurement = senml.getJsonObject(0);
		String resourceName = measurement.getString("n");
		int resourceValue = measurement.getInt("v");
		
		/* output result */
		System.out.println("Read Resource");
		System.out.println("name:  " + resourceName);
		System.out.println("value: " + resourceValue);
		
		System.exit(0);
	}
}
