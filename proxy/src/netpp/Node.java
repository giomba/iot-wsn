package netpp;

import java.net.URI;
import java.net.URISyntaxException;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapObserveRelation;

enum Status { OFFLINE, WAIT, ONLINE };

public class Node {
	private URI uri;
	private int quickname;
	private CoapClient client;
	private CoapObserveRelation relation;
	private Handler handler;
	Resource resource;
	Status status = Status.OFFLINE;
	
	public Node(int quickname, String uriString) {
		try {
			this.uri = new URI(uriString);
			this.client = new CoapClient(uri);
			this.quickname = quickname;
			this.resource = new Resource(this.quickname); /* this name will be overwritten when an update is received */
			this.handler = new Handler(this);
			this.relation = client.observe(handler);
		} catch (URISyntaxException e) {
			System.err.println("[F] Invalid URI: " + e.getMessage());
			System.exit(1);
		}
	}
	
	Resource getResource() {
		return this.resource;
	}
	
	URI getURI() {
		return this.uri;
	}
	Status getStatus() {
		return this.status;
	}
	void setStatus(Status status) {
		this.status = status;
	}
	
	/* be kind with the poor resource constrained node */
	@Override
	public void finalize() {
		this.relation.proactiveCancel();
	}
	
}
