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
		
		/* run server */
		CoapServer server = new CoapServer();
		server.start();
		System.err.println("[D] server started");
		
		/* subscribe to every node specified */
		System.err.println("[I] now subscribing to node [2; " + n + "] in " + prefix + "/64");
		
		ArrayList<Node> nodeList = new ArrayList<Node>();
		
		for (int i = 2; i <= n; i++) {	// node 1 is the gateway, skip. And mind the <=
			String uri_string = "coap://[" + prefix + ":" + COOJA_MAGIC + "::" + Integer.toString(i, 16) + "]" + resource;
			System.err.println("[I] subscribing to " + uri_string);
			
			Node node = new Node(i, uri_string);
			nodeList.add(node);
			server.add(node.getResource());
		}
		
		/* periodically check for offline nodes */
		while (true) {
			System.err.println("[D] check for offline nodes...");
			Node offline_node = null;
			
			for (Node node : nodeList) {
				if (node.getStatus() == Status.OFFLINE) {
					System.err.println("[I] detected offline node");
					offline_node = node;
					break;
				}
			}
			
			if (offline_node != null) {
				System.err.println("[I] tryng again to contact " + offline_node.getURI());
				nodeList.remove(offline_node);
				server.remove(offline_node.getResource());
				
				Node new_node = new Node(0, offline_node.getURI().toString());
				nodeList.add(new_node);
				server.add(new_node.getResource());
			}
			/* thread sleep */
			try {
				Thread.sleep(5 * 1000);	// 5 sec
			} catch (InterruptedException ex) {
				System.err.println(ex);
			}
		}
		
	}
}
