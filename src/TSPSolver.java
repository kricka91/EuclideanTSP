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
		//return path; //TODO
		return full2Opt(path,allNodes);
	}
	
	public ArrayList<Integer> full2Opt(ArrayList<Integer> path, Node[] nodes) {
		int n = nodes.length;
		
		@SuppressWarnings("unchecked")
		ArrayList<Integer> lpath = (ArrayList<Integer>) path.clone();
		/*ArrayList<Integer> toCheck = new ArrayList<Integer>();
		for(int i = 0; i < n; i++) {
			toCheck.add(i);
		}
		
		ArrayList<Integer> toCheckNext = new ArrayList<Integer>();*/
		
		//long pathLength = getPathLength(lpath,nodes);
		
		for(int i = 0; i < n; i++) {
			for(int j = i+2; j < n; j++) {
				boolean swapped = false;
				if(!(i == 0 && j == n-1)) {
					int is = i;
					int ie = (i == n-1 ? 0 : i+1);  
					int js = j;
					int je = (j == n-1 ? 0 : j+1); 
					
					//see if swap is better
					int prevDist = nodes[lpath.get(is)].distances[lpath.get(ie)] +
							       nodes[lpath.get(js)].distances[lpath.get(je)];
					int newDist = nodes[lpath.get(is)].distances[lpath.get(js)] +
						          nodes[lpath.get(ie)].distances[lpath.get(je)];
					if(newDist < prevDist) {
						//swap improves!
						ArrayList<Integer> newPath = new ArrayList<Integer>();
						for(int k = 0; k < is; k++) {
							newPath.add(lpath.get(k)); 
						}
						newPath.add(lpath.get(is));
						//newPath.add(lpath.get(js));
						for(int k = js; k > ie; k--) {
							newPath.add(lpath.get(k)); 
						}
						newPath.add(lpath.get(ie));
						for(int k = je; k < n; k++) {
							newPath.add(lpath.get(k));
						}
						lpath = newPath;
						swapped = true;
					}
				}
				

				if(swapped) {
					i--;
					break;
				}
				
				
			}
		}
		
		return lpath;
	}
	
	public long getPathLength(ArrayList<Integer> path, Node[] nodes) {
		long len = 0;
		int n = nodes.length;
		for(int i = 1; i < n; i++) {
			len += nodes[path.get(i-1)].distances[path.get(i)];
		}
		len += nodes[path.get(n-1)].distances[path.get(0)];
		return len;
	}
	
	
}
