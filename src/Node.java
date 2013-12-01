import java.util.Comparator;

public class Node {

	public final double x, y, angle;
	public final int index, n;
	
	int[] distances;
	int[] closest;
	
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
	
	public String toString() {
		return "(" + x + " " + y + ")";
	}
	
	public void calcClosest(int x) {
		if(x > n-1)
		    x = n-1;
		
		
		
		closest = new int[x];
		int i;
		int numPlaced = 0;
		for(i = 0; numPlaced < x; i++) {
			if(i != index) {
				closest[numPlaced] = i;
				numPlaced++;
			}
		}
		
		for(; i < n; i++) {
			if(i != index) {
				//int dist = distances[i];
				int worstDist = Integer.MIN_VALUE;
				int worstIndex = -1;
				for(int j = 0; j < x; j++) {
					if(distances[closest[j]] > worstDist) {
						worstDist = distances[closest[j]];
						worstIndex = j;
					}
				}
				
				if(distances[i] < worstDist) {
					closest[worstIndex] = i;
				}
			}
		}
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
