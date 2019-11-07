package netpp;

import java.net.URI;
import org.eclipse.californium.core.*;

public class Handler implements CoapHandler {
	
	private URI uri; 
	
	public Handler(URI uri) {
		this.uri = uri;
	}

	@Override
	public void onLoad(CoapResponse response) {
		String content = response.getResponseText();
		System.out.println("[D] resource: " + uri);
		System.out.println("	new value: " + content);		
	}

	@Override
	public void onError() {
		System.err.println("[E] failed " + uri);
	}

}
