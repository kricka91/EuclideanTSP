import java.util.ArrayList;

/**
 * Class for algorithms for solving TSP
 *
 */
public class TSPSolver {
	
	private boolean improv;
	
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
		//int i = 0;
		for(int i = 0; i< 10;i++) {
			full2Opt(path,allNodes);
			if(!improv)
				break;
		}
		
		
		return path;
	}
	
	public ArrayList<Integer> full2Opt(ArrayList<Integer> path, Node[] nodes) {
		int n = nodes.length;
		improv = false;
		//@SuppressWarnings("unchecked")
		//ArrayList<Integer> lpath = (ArrayList<Integer>) path.clone();
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
					//these below stand for i-start, i-end, j-start, j-end.
					//We are trying to swap from the paths is-ie and js-je to is-js and ie-je
					int is = i;
					int ie = (i == n-1 ? 0 : i+1);  
					int js = j;
					int je = (j == n-1 ? 0 : j+1); 
					
					//see if swap is better
					int prevDist = nodes[path.get(is)].distances[path.get(ie)] +
							       nodes[path.get(js)].distances[path.get(je)];
					int newDist = nodes[path.get(is)].distances[path.get(js)] +
						          nodes[path.get(ie)].distances[path.get(je)];
					if(newDist < prevDist) {
						//swap improves!
						//number of nodes from ie to js
						int iToj = js-ie+1;
						int jToi = 0;
						if(je == 0) {
							jToi = is+1;
						} else {
							jToi = n-je + is+1;
						}
						swapped = true;
						if(iToj <= jToi) {
							int mid = iToj/2;
							for(int k = 0; k < mid; k++) {
								swap(path,ie+k,js-k);
							}
						} else {
							int mid = jToi/2;
							int jc = je;
							int ic = is;
							for(int k = 0; k < mid; k++) {
								swap(path,ic,jc);
								ic--;
								jc++;
								if(jc == n)
									jc = 0;
								if(ic == -1)
									ic = n-1;
							}
						}
						
						
					} 
				}
				

				if(swapped) {
					improv = true;
					i--;
					break;
				}
				
				
			}
		}
		
		return path;
	}
	
	private void swap(ArrayList<Integer> path, int i, int j) {
		int tmp = path.get(i);
		path.set(i,path.get(j));
		path.set(j, tmp);
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
