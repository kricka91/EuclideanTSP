import java.util.ArrayList;
import java.util.Scanner;
import java.util.Random;

public class Main {
	private static boolean measureTime = true, drawSolution = true, printSolution = false;
	private static long startTime, endTime, partialTime;
	private static ArrayList<String> partialTimeNames;
	private static ArrayList<Long> partialTimes;
	private static ArrayList<Path> partRes;
	private static ArrayList<String> partResNames;

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
	
	private static void addPartRes(String phaseName, Path path) {
		if (drawSolution) {
			Path pathClone = (Path) path.clone();
			//pathClone.addAll(path);
			partRes.add(pathClone);
			partResNames.add(phaseName);
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
		partRes = new ArrayList<Path>();
		//partRes.add(new ArrayList<Integer>());	//Add empty path
		partResNames = new ArrayList<String>();
		//partResNames.add("Start");
		
		if(args.length == 0) {
			//read from System.in
			nodes = readData();
		} else {
			//generate random TSP problem!
			//syntax in arguments:
			// 1. gen - this generates a completely random problem with random amount of nodes (max 1000)
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
		Path path;


		// calculate all distances.
		Node.calcAllDistances(nodes);
		for(int i = 0; i < n; i++) {
			nodes[i].calcClosest(10);
		}
		createTimeStamp("distance computation");


		//get intial path
		path = tsp.getInitialPath(nodes);
		//path = tsp.solve(nodes);
		//addPartRes("initial path", path);
		//createTimeStamp("initial path");

		
		
		//improve path
		
		tsp.f2Opt(path, nodes);
		//improvedPath = tsp.solve(nodes);
		//createTimeStamp("local search opt");
		addPartRes("2 opt", path);
		createTimeStamp("2 opt");
		System.err.println("2opt length: " + tsp.getPathLength(path, nodes));
		
		
		tsp.full1Opt(path, nodes);
		addPartRes("1 opt", path);
		createTimeStamp("1 opt");
		System.err.println("1opt length: " + tsp.getPathLength(path, nodes));
		
		//path = tsp.s3opt(path, nodes);
		//addPartRes("3 opt", path);
		//createTimeStamp("3 opt");
		
		/*
		 * MST ALGORITHM HERE
		ArrayList<Integer> mstPath = tsp.mstAlg(nodes);
		addPartRes("MST",mstPath);
		createTimeStamp("MST tree");
		
		tsp.improvePath(mstPath, nodes);
		addPartRes("MST 2-opt",mstPath);
		createTimeStamp("MST tree 2-opt");
*/
		
		//Path torPath = new ArrayList<Integer>(); //tsp.nearestNeighbor(nodes, 0);
		//Path torPath = tsp.solve(nodes);
				//tsp.tornmentAlg(torPath, nodes);
		//addPartRes("TOR PATH",torPath);
		//createTimeStamp("TOR path");
		//System.err.println("TOR path length " + tsp.getPathLength(torPath,nodes));
		
		
		
		//improve path
		//path = tsp.improvePath(path, nodes);
		//path = tsp.solve(nodes);
		//createTimeStamp("solve with local search opt");
		/*
		//Compute convex hull
		ArrayList<Integer> convexHull;
		Node[] nodesCopy = (Node[])(nodes.clone());
		convexHull = tsp.findConvexHull(nodesCopy);
		addPartRes("convex hull", convexHull);
		createTimeStamp("convex hull");
		
		//Add the rest of the nodes
		ArrayList<Integer> completePath;
		completePath = tsp.addRemainingNodes(nodes, convexHull);
		addPartRes("adding remaining nodes to path", completePath);
		createTimeStamp("adding remaining nodes to path");
		*/
		
		
		//print path
		if(printSolution) {
			printPath(path);
		}
		
		//System.err.println("length of 2opt path: " + tsp.getPathLength(path, nodes));
		//System.err.println("length of convex path: " + tsp.getPathLength(completePath, nodes));
		
		//tsp.improvePath(completePath, nodes);
		
		//System.err.println("length of 2opted convex path: " + tsp.getPathLength(completePath, nodes));
		//tsp.s3opt(completePath, nodes);
		//System.err.println("length of 3opted convex path: " + tsp.getPathLength(completePath, nodes));
		
		
		
		
		
		
		if(drawSolution) {
			Graphical g = new Graphical(nodes);
			g.updateContent(partRes, partResNames);
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
	public static void printPath(Path path) {
		for(int i = 0; i < path.size(); i++) {
			System.out.println(path.get(i));
		}
	}
	
}
