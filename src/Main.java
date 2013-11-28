import java.util.ArrayList;
import java.util.Scanner;


public class Main {
	
	public static ArrayList<Integer> getInitialPath(Node[] allNodes) {
		//TODO - now just returns a path 0,1,2,3,4...
		ArrayList<Integer> path = new ArrayList<Integer>();
		for(int i = 0; i < allNodes.length; i++) {
			path.add(i);
		}
		return path;
	}
	
	
	/**
	 * Improve intial path
	 * @param path The path
	 * @param allNodes
	 * @return
	 */
	public static ArrayList<Integer> improvePath(ArrayList<Integer> path, Node[] allNodes) {
		return path; //TODO
	}
	
	public static void main(String[] args) {
		
		
		
		Scanner sc = new Scanner(System.in);
		int n = sc.nextInt(); //n = number of nodes
		double minx = Integer.MIN_VALUE;
		double miny = Integer.MIN_VALUE;
		
		//read data
		Node[] nodes = new Node[n];
		for(int i = 0; i < n; i++) {
			double x = sc.nextDouble();
			double y = sc.nextDouble();
			if(x > minx) {
				minx = x;
			}
			if(y > miny) {
				miny = y;
			}
			nodes[i] = new Node(x,y,n);
		}
		
		// calculate all distances.
		for(int i = 0; i < n; i++) {
			nodes[i].calcDistances(nodes);
		}
		
		//get intial path
		ArrayList<Integer> initialPath = getInitialPath(nodes);
		
		//improve path
		ArrayList<Integer> improvedPath = improvePath(initialPath, nodes);
		
		
		//print path
		printPath(improvedPath);
	}
	
	/**
	 * Print final path
	 */
	public static void printPath(ArrayList<Integer> path) {
		for(int i = 0; i < path.size(); i++) {
			System.out.println(path.get(i));
		}
	}
	
}
