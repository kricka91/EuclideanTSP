import java.util.ArrayList;
import java.util.Scanner;


public class Main {
	

	
	public static Node[] readData() {
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
		return nodes;
	}

	
	public static void main(String[] args) {
		
		Node[] nodes = null;
		if(args.length == 0) {
			//read from System.in
			nodes = readData();
		}
		
		int n = nodes.length;
		
		// calculate all distances.
		for(int i = 0; i < n; i++) {
			nodes[i].calcDistances(nodes);
		}
		
		TSPSolver tsp = new TSPSolver();
		
		//get intial path
		ArrayList<Integer> initialPath = tsp.getInitialPath(nodes);
		
		//improve path
		ArrayList<Integer> improvedPath = tsp.improvePath(initialPath, nodes);
		
		
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
