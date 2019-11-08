package netpp;

import java.net.URI;
import java.net.URISyntaxException;

import org.eclipse.californium.core.*;

public class Node {
	private URI uri;
	private CoapClient client;
	private CoapObserveRelation relation;
	private Handler handler;
	
	public Node(String uriString) {
		try {
			this.uri = new URI(uriString);
			this.client = new CoapClient(uri);
			this.handler = new Handler(this.uri);
			this.relation = client.observe(handler);
		} catch (URISyntaxException e) {
			System.err.println("[F] Invalid URI: " + e.getMessage());
			System.exit(1);
		}
	}
	
	/* be kind with the poor resource constrained node */
	@Override
	public void finalize() {
		this.relation.proactiveCancel();
	}
	
}
