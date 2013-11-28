import java.util.ArrayList;

/**
 * Class for algorithms for solving TSP
 *
 */
public class TSPSolver {
	
	public TSPSolver() {
		
	}
	
	public ArrayList<Integer> getInitialPath(Node[] allNodes) {
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
	public ArrayList<Integer> improvePath(ArrayList<Integer> path, Node[] allNodes) {
		return path; //TODO
	}
}
