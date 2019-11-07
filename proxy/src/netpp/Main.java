package netpp;

public class Main {
	public final static String COOJA_MAGIC = "c30c";
	public final static String resource = "/test/myresource";
	
	public static void main(String[] args) {
		if (args.length < 2) {
			System.err.println("[F] Bad parameters");
			System.err.println("Usage: ");
			System.err.println("$ exe <prefix> <n>");
			System.err.println("	prefix	IPv6 /64 prefix (mind the format!)");
			System.err.println("	n		number of nodes");
			System.err.println();
			System.err.println("Example:");
			System.err.println("$ exe 2001:db8:0:0 10");
			System.exit(1);
		}
		
		final String prefix = args[0];
		final int n = Integer.parseInt(args[1]);
		System.err.println("[I] now subscribing to node [2; " + n + "] in " + prefix + "/64");
		
		// TODO pay attention to this for-cycle and mind the hexadecimal values!
		/* subscribe to every node */
		for (int i = 2; i <= n; i++) {	// node 1 is the gateway, skip. And mind the <=
			String uri_string = "coap://[" + prefix + ":" + COOJA_MAGIC + "::" + Integer.toString(i) + "]" + resource;
			System.err.println("[I] subscribing to " + uri_string);
			Node node = new Node(uri_string);
		}
		
		
		try {
			Thread.sleep(5 * 60 * 1000);	// 5 min
		} catch (InterruptedException ex) {
			System.err.println(ex);
		}
		
	}
}
