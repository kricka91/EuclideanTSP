public class Node {

	final double x;
	final double y;
	
	final int n;
	
	int[] distances;
	
	//Constructor
	//n = number of nodes
	public Node(double x, double y, int n) {
		this.x = x;
		this.y = y;
		this.n = n;
		distances = new int[n];
	}
	
	/**
	 * Calculate all distances to all nodes.
	 * Sets "distances" array.
	 * @param allNodes array of all nodes (including this one).
	 */
	public void calcDistances(Node[] allNodes) {
		for(int i = 0; i < allNodes.length; i++) {
			distances[i] = calcDistanceTo(allNodes[i]);
		}
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
	
	
	
}
