package netpp;

import java.net.URI;
import java.net.URISyntaxException;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapObserveRelation;
import org.eclipse.californium.core.CoapResource;

public class Node {
	private URI uri;
	private int quickname;
	private CoapClient client;
	private CoapObserveRelation relation;
	private Handler handler;
	Resource resource;
	
	public Node(int quickname, String uriString) {
		try {
			this.uri = new URI(uriString);
			this.client = new CoapClient(uri);
			this.quickname = quickname;
			this.resource = new Resource(this.quickname); /* this name will be overwritten when an update is received */
			this.handler = new Handler(this.uri, resource);
			this.relation = client.observe(handler);
		} catch (URISyntaxException e) {
			System.err.println("[F] Invalid URI: " + e.getMessage());
			System.exit(1);
		}
	}
	
	CoapResource getResource() {
		return this.resource;
	}
	
	/* be kind with the poor resource constrained node */
	@Override
	public void finalize() {
		this.relation.proactiveCancel();
	}
	
}
