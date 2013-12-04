import java.util.ArrayList;
import java.util.Scanner;
import java.util.Random;

public class Main {
	private static boolean measureTime = true, drawSolution = true, printSolution = false;
	private static long startTime, endTime, partialTime;
	private static ArrayList<String> partialTimeNames;
	private static ArrayList<Long> partialTimes;

	/**
	 * Read data from System.in
	 * @return An array of all nodes.
	 */
	public static Node[] readData() {
		Scanner sc = new Scanner(System.in);
		int n = sc.nextInt(); //n = number of nodes
		
		//read data
		Node[] nodes = new Node[n];
		for(int i = 0; i < n; i++) {
			double x = sc.nextDouble();
			double y = sc.nextDouble();
			nodes[i] = new Node(x,y,n,i);
		}
		return nodes;
	}
	
	/**
	 * Call this to insert a measurement time stamp after a specific part
	 * @param a name of the part
	 */
	private static void createTimeStamp(String name) {
		if (measureTime) {
			endTime = System.nanoTime();
			partialTime = (endTime - startTime)/1000;
			startTime = endTime;
			partialTimeNames.add(name);
			partialTimes.add(new Long(partialTime));
		}
	}
	
	public static boolean sleep(int msDuration) {
		try {
			Thread.sleep(msDuration);
			return true;
		} catch (Exception e) {
			//Do something, maybe
			return false;
		}
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
			nodes[i] = new Node(x,y,n,i);
		}
		return nodes;
	}

	
	public static void main(String[] args) {
		if (measureTime) {
			startTime = System.nanoTime();
			partialTimeNames = new ArrayList<String>();
			partialTimes = new ArrayList<Long>();
		}
		
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
		
		TSPSolver tsp = new TSPSolver();
		
		ArrayList<Integer> initialPath;
		ArrayList<Integer> improvedPath;
		ArrayList<Integer> bestPath;

		// calculate all distances.
		Node.calcAllDistances(nodes);
		
		for(int i = 0; i < n; i++) {
			nodes[i].calcClosest(10);
		}
		
		createTimeStamp("distance computation");
	
		//get intial path
		initialPath = tsp.getInitialPath(nodes);
		createTimeStamp("initial path");

		//improve path
		improvedPath = tsp.improvePath(initialPath, nodes);
		//improvedPath = tsp.solve(nodes);
		//createTimeStamp("local search opt");
		createTimeStamp("2 opt");
		
		bestPath = tsp.s3opt(improvedPath, nodes);
		createTimeStamp("3 opt");
		System.err.println("Finished local search opt");
		
		//Compute convex hull
		ArrayList<Integer> convexHull;
		Node[] nodesCopy = (Node[])(nodes.clone());
		//convexHull = tsp.findConvexHull(nodesCopy);
		System.err.println("Finished hull");
		createTimeStamp("convex hull");

		
		
		//print path
		System.err.println("Path size is: " + improvedPath.size());
		if(printSolution)
			printPath(improvedPath);
		
		if(drawSolution) {
			Graphical g = new Graphical(nodes);
			g.updateContent(initialPath);
			//g.updateContent(convexHull);
			//g.updateContent(improvedPath);
			//g.updateContent(bestPath);
		}
		
		if (measureTime) {
			//Print the times for each part
			for(int i = 0; i < partialTimeNames.size(); i++) {
				System.out.print("Part \"" + partialTimeNames.get(i) + "\" took ");
				System.out.println(partialTimes.get(i) + " microseconds.");
			}
		}
	}
	
	/**
	 * Print the path
	 */
	public static void printPath(ArrayList<Integer> path) {
		for(int i = 0; i < path.size(); i++) {
			System.out.println(path.get(i));
		}
	}
	
}
