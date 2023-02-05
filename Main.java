import java.util.*;
import java.io.*;

class Pair {
	int dist;
	ArrayList<String> path;

	Pair(int dist, ArrayList<String> path) {
		this.dist = dist;
		this.path = path;
	}
}

class BuildNetWork {

	HashMap<String, ArrayList<String>> graph;
	HashMap<String, Boolean> visited;
	HashMap<String, Integer> nodeDist;

	BuildNetWork() {
		graph = new HashMap<>();
		visited = new HashMap<>();
		nodeDist = new HashMap<>();
	}

	void addNode(String node) {
		if (graph.containsKey(node))
			return;
		graph.put(node, new ArrayList<String>());
		visited.put(node, false);
		nodeDist.put(node, 5);
	}

	boolean addEdge(String node1, String node2) {
		for (String adj : graph.get(node1)) {
			if (adj.equals(node2))
				return false;
		}
		graph.get(node1).add(node2);
		graph.get(node2).add(node1);
		visited.put(node1, false);
		visited.put(node2, false);

		return true;
	}

	void updateDistance(String node, int dist) {
		nodeDist.replace(node, dist);
	}

	ArrayList<String> doBFS(String node1, String node2) {

		visited.replaceAll((k, v) -> v = false);

		Queue<Pair> q = new ArrayDeque<>();

		ArrayList<String> checkPath = new ArrayList<>();

		checkPath.add(node1);

		q.add(new Pair(nodeDist.get(node1), checkPath));
		visited.replace(node1, true);

		while (q.isEmpty() == false) {
			Pair peek = q.poll();

			ArrayList<String> currPath = peek.path;
			int currDist = peek.dist;
			
			if(currDist == 0) continue;				 
			
			String currNode = currPath.get(currPath.size() - 1);

			if (currNode.equals(node2)) return currPath;

			for (String adj : graph.get(currNode)) {
				if (visited.get(adj) == false) {
					currPath.add(adj);
					if (adj.substring(0, 1).equals("R"))
						q.add(new Pair(2 * currDist, new ArrayList<String>(currPath)));
					else
						q.add(new Pair(currDist - 1, new ArrayList<String>(currPath)));
					currPath.remove(currPath.size() - 1);
					visited.replace(adj, true);
				}
			}
		}
		return null;
	}

	boolean isNumeric(String dist) {
		for (char ch : dist.toCharArray()) {
			if (Character.isDigit(ch) == false)
				return false;
		}
		return true;
	}

	boolean checkNode(String node) {
		// System.out.println(graph.containsKey(node));
		return graph.containsKey(node);
	}

}

public class Main {
	public static void main(String[] args) {

		Scanner sc = new Scanner(System.in);

		try {

			// Uncomment line 114 to read input from input.txt
			// BufferedReader reader = new BufferedReader(new FileReader("input.txt"));

			System.out.println("------- LETS BUILD THE NETWORK -------\n");
			System.out.println("Press $ at anytime to exit from program!\n");

			BuildNetWork network = new BuildNetWork();

			while (true) {
				System.out.print("> ");

				// Comment line 124 if you uncommented line 114
				String input = sc.nextLine();

				// Uncomment line 128 to read input from input.txt. (Do this after commenting
				// line 125);
				// String input = reader.readLine();

				if (input == null)
					break;

				String split[] = input.split(" ");

				if (input.equals("$"))
					break;

				if (split[0].equals("ADD")) {

					if (split.length != 3 || (!split[1].equals("COMPUTER") && !split[1].equals("REPEATER")))
						System.out.println("Error : Invalid command syntax.");
					else if (network.checkNode(split[2]))
						System.out.println("Error : That name already exists.");
					else {
						network.addNode(split[2]);
						System.out.println("Successfully added " + split[2] + ".");
					}

				} else if (split[0].equals("SET_DEVICE_STRENGTH")) {

					if (split.length != 3 || !network.isNumeric(split[2]))
						System.out.println("Error : Invalid command syntax.");
					else {
						network.updateDistance(split[1], Integer.parseInt(split[2]));
						System.out.println("Successfully defined strength.");
					}

				} else if (split[0].equals("INFO_ROUTE")) {

					if (split.length != 3)
						System.out.println("Error : Invalid command syntax.");
					else if (split[1].charAt(0) == 'R' || split[2].charAt(0) == 'R')
						System.out.println("Error : Route cannot be calculated with a repeater.");
					else if (!network.checkNode(split[1]) || !network.checkNode(split[2]))
						System.out.println("Error : Node not found!");
					else {
						ArrayList<String> ans = network.doBFS(split[1], split[2]);
						if (ans == null)
							System.out.println("Route not found!");
						else {
							for (int i = 0; i < ans.size(); i++) {
								System.out.print(ans.get(i));
								if (i != ans.size() - 1)
									System.out.print(" -> ");
							}
							System.out.println();
						}
					}

				} else if (split[0].equals("CONNECT")) {

					if (split.length != 3)
						System.out.println("Error : Invalid command syntax.");

					else if (!network.checkNode(split[1]) || !network.checkNode(split[2]))
						System.out.println("Error : Node not found.");
					else if (split[1].equals(split[2]))
						System.out.println("Error : Cannot device to itself");
					else {
						boolean check = network.addEdge(split[1], split[2]);
						if (check == false)
							System.out.println("Error: Devices are already connected.");
						else
							System.out.println("Successfully connected.");
					}
				}
			}
		} catch (Exception e) {

		}
	}
}
