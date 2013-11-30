public class Node {

	final double x;
	final double y;
	
	final int index;
	final int n;
	
	int[] distances;
	
	//Constructor
	//n = number of nodes
	public Node(double x, double y, int n, int index) {
		this.x = x;
		this.y = y;
		this.n = n;
		distances = new int[n];
		this.index = index;
	}
	
	/**
	 * Calculate euclidian distance between this node and the given node, rounded to the nearest integer.
	 * @param n Other node
	 * @return Euclidian distance
	 */
	public int calcDistanceTo(Node n) {
		return (int) Math.round(Math.sqrt(Math.pow(n.x-x, 2) + Math.pow(n.y-y, 2)));
	}
	
	public String toString() {
		return "(" + x + " " + y + ")";
	}
	
	public static void calcAllDistances(Node[] allNodes) {
		int n = allNodes.length;
		for(int i = 0; i < n; i++) {
			for(int j = i+1; j < n; j++) {
				int dist = allNodes[i].calcDistanceTo(allNodes[j]);
				allNodes[i].distances[j] = dist;
				allNodes[j].distances[i] = dist;
			}
		}
	}
	
	
	
}
