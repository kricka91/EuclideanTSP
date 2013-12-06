import java.util.Comparator;
import java.util.Arrays;
import java.lang.Math;

public class Node {

	public final double x, y, angle;
	public final int index, n;
	public static final double INVSQRT2 = 1.0/Math.sqrt(2.0);
	
	int[] distances;
	Pair<Integer, Integer>[] closest;
	
	//Constructor
	//n = number of nodes
	public Node(double x, double y, int n, int index) {
		this.x = x;
		this.y = y;
		this.n = n;
		distances = new int[n];
		this.index = index;
		//this.angle = atan2(y,x);	//Might not be needed
		this.angle = 0.0;
	}
	
	/**
	 * Calculate euclidian distance between this node and the given node, rounded to the nearest integer.
	 * @param n Other node
	 * @return Euclidian distance
	 */
	public int calcDistanceTo(Node n) {
		return (int) Math.round(Math.sqrt(Math.pow(n.x-x, 2) + Math.pow(n.y-y, 2)));
	}
	
	/**
	 * Calculate euclidian distance between this node and the given node, rounded to the nearest integer.
	 * @param n Other node
	 * @return Euclidian distance
	 */
	public int calcApproxDistanceTo(Node n) {
		return (int)(Math.abs(n.x-x)+Math.abs(n.y-y));
		/*
		double absxd = Math.abs(n.x-x);
		double absyd = Math.abs(n.y-y);
		double approx1 = Math.max(absxd, absyd);
		double approx2 = INVSQRT2*(absxd+absyd);
		double approx = Math.max(approx1, approx2);
		return (int)Math.round(approx);
		*/
	}
	
	public String toString() {
		//return "(" + x + " " + y + ")";
		//return "Node: {index = " + index + ", position: {x = " + x + ", y = " + y + "} }";
		return "Node " + index;
	}
	
	public void calcClosest(int x) {
		if(x > n-1) {
		    x = n-1;
		}

		closest = (Pair<Integer, Integer>[])new Pair<?,?>[x];
		int i;
		int numPlaced = 0;
		for(i = 0; numPlaced < x; i++) {
			if(i != index) {
				closest[numPlaced] = new Pair(new Integer(i), new Integer(distances[i]));
				numPlaced++;
			}
		}
		
		for(; i < n; i++) {
			if(i != index) {
				//int dist = distances[i];
				int worstDist = Integer.MIN_VALUE;
				int worstIndex = -1;
				for(int j = 0; j < x; j++) {
					if(distances[closest[j].key] > worstDist) {
						worstDist = distances[closest[j].key];
						worstIndex = j;
					}
				}
				
				if(distances[i] < worstDist) {
					closest[worstIndex] = new Pair(new Integer(i), new Integer(distances[i]));
				}
			}
		}
		
		Arrays.sort(closest, new Pair().new ValueComparator());
		
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
	
	public class NodeLexComparator implements Comparator<Node> {
   		public int compare(Node node1, Node node2) {
    	    if (node1.x > node2.x) {
    	    	return 1;
    	    } else if (node1.x > node2.x) {
				if (node1.y > node2.y) {
			    	return 1;
			    } else if (node1.y > node2.y) {
					return 0;
			    } else {
			    	return -1;
    	    	}
    	    } else {
    	    	return -1;
    	    }
    	}
	}

	public class NodeAngleComparator implements Comparator<Node> {
   		public int compare(Node node1, Node node2) {
    	    if (node1.angle > node2.angle) {
    	    	return 1;
    	    } else if (node1.angle == node2.angle) {
    	    	return 0;
    	    } else {
    	    	return -1;
    	    }
    	}
	}
}
