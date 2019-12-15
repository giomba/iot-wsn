package netpp;

import java.io.ByteArrayOutputStream;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonWriter;

import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.server.resources.CoapExchange;

public class Resource extends CoapResource {

	private int value;

	public Resource(int quickname) {
		super(Integer.toString(quickname, 16));
		System.err.println("[D] resource created: " + super.getName());
	}
	
	public void handleGET(CoapExchange exchange) {
		/* an RFC8428 (SenML) compliant JSON */
		JsonArray senml = Json.createArrayBuilder()
			.add(
				Json.createObjectBuilder()
					.add("n", this.getName())
					.add("v", this.getValue())
					.build()
			).build();

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		JsonWriter writer = Json.createWriter(bos);
		writer.writeArray(senml);
		exchange.respond(bos.toString());
	}
	
	public int getValue() {
		return this.value;
	}

	public void setValue(int value) {
		this.value = value;
	}

}
