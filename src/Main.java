import java.util.ArrayList;
import java.util.Scanner;
import java.util.Random;

public class Main {
	

	/**
	 * Read data from System.in
	 * @return An array of all nodes.
	 */
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
	
	
	public static Node[] genRandomProblem() {
		Random r = new Random(); 
		int n = r.nextInt(1000); //generate random n between 0 and 999
		if(n < 10)
			n = 10;
		return genRandomProblem(n);
	}
	
	/**
	 * Generate a random problem with n nodes
	 */
	public static Node[] genRandomProblem(int n) {
		Node[] nodes = new Node[n];
		Random r = new Random();
		for(int i = 0; i < n; i++) {
			double x = r.nextDouble()*1000000; //scale up
			double y = r.nextDouble()*1000000;
			nodes[i] = new Node(x,y,n);
		}
		return nodes;
	}

	
	public static void main(String[] args) {
		
		boolean measureTime = true;
		boolean drawSolution = true;
		
		Node[] nodes = null;
		
		if(args.length == 0) {
			//read from System.in
			nodes = readData();
		} else {
			//generate random TSP problem!
			//syntax in arguments:
			// 1. gen      - this generates a completely random problem with random amount of nodes (max 1000)
			// 2. gen <integer> - this generates a random problem with <integer> number of nodes.
			if(args[0].equals("gen")) {
				int n = 100;
				if(args.length > 1) {
					nodes = genRandomProblem(Integer.parseInt(args[1]));
				} else {
					nodes = genRandomProblem();
				}
			}
		}
		
		int n = nodes.length;
		
	    //for(int i = 0; i < n; i++) {
	    //	System.out.print(nodes[i].x + "\t\t\t" + nodes[i].y + "\n");
	    //}
		
		
		// calculate all distances.
		for(int i = 0; i < n; i++) {
			nodes[i].calcDistances(nodes);
		}
		
		TSPSolver tsp = new TSPSolver();
		
		ArrayList<Integer> initialPath;
		ArrayList<Integer> improvedPath;
		
		if(measureTime) {
			long startTime = System.nanoTime();
			//get intial path
			initialPath = tsp.getInitialPath(nodes);
			long endTime = System.nanoTime();
			long timeElapsed = endTime-startTime;
			long microSeconds = timeElapsed/1000;
			//long milliSeconds = microSeconds/1000;
			System.err.println("Time taken for initial path calculation (microseconds): " + microSeconds);
			
			//improve path
			startTime = System.nanoTime();
			improvedPath = tsp.improvePath(initialPath, nodes);
			endTime = System.nanoTime();
			timeElapsed = endTime-startTime;
			microSeconds = timeElapsed/1000;
			System.err.println("Improving path took (microseconds): " + microSeconds);
			
		} else {
			//get intial path
			initialPath = tsp.getInitialPath(nodes);
			
			//improve path
			improvedPath = tsp.improvePath(initialPath, nodes);
		}
		
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
