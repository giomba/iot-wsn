package netpp;

import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.server.resources.CoapExchange;

public class Resource extends CoapResource {

	private int value;

	public Resource(int quickname) {
		super(Integer.toString(quickname, 16));
		// setObservable(true); // TODO -- do we need to make this observable?
		System.err.println("[D] resource created: " + super.getName());
	}
	
	public void handleGET(CoapExchange exchange) {
		// TODO build the proper JSON object
		exchange.respond("TODO: " + this.getName() + " : " + this.getValue());
	}
	
	public int getValue() {
		return this.value;
	}

	public void setValue(int value) {
		this.value = value;
	}

}
