package netpp;

import java.util.ArrayList;

import org.eclipse.californium.core.CoapServer;

public class Main {
	public final static String COOJA_MAGIC = "c30c";
	public final static String resource = "/PreciousResource";
	
	public static void main(String[] args) {
		if (args.length < 2) {
			System.err.println("[F] Bad parameters");
			System.err.println("Usage: ");
			System.err.println("$ exe <prefix> <n>");
			System.err.println("	prefix	IPv6 /64 prefix (mind the format!)");
			System.err.println("	n		last node number (dec)");
			System.err.println();
			System.err.println("Example:");
			System.err.println("$ exe 2001:db8:0:0 10");
			System.exit(1);
		}
		
		/* getting parameters from CLI */
		final String prefix = args[0];
		final int n = Integer.parseInt(args[1]);
		
		/* subscribe to every node specified */
		System.err.println("[I] now subscribing to node [2; " + n + "] in " + prefix + "/64");
		
		ArrayList<Node> nodeList = new ArrayList<Node>();
		
		for (int i = 2; i <= n; i++) {	// node 1 is the gateway, skip. And mind the <=
			String uri_string = "coap://[" + prefix + ":" + COOJA_MAGIC + "::" + Integer.toString(i, 16) + "]" + resource;
			System.err.println("[I] subscribing to " + uri_string);
			nodeList.add(new Node(i, uri_string));
		}
		
		/* run server */
		CoapServer server = new CoapServer();
		for (Node node : nodeList) {
			server.add(node.getResource());
		}
		server.start();
		System.err.println("[D] server started");		
		
		/* thread sleep */
		try {
			Thread.sleep(5 * 60 * 1000);	// 5 min
		} catch (InterruptedException ex) {
			System.err.println(ex);
		}
		
		/* shutting down everything */
		for (Node node : nodeList) {
			node.finalize();
		}
		
	}
}
