import java.util.ArrayList;
import java.util.Arrays;
import java.lang.Math;

/**
 * Class for algorithms for solving TSP
 *
 */
public class TSPSolver {
	
	private boolean improv;
	
	public TSPSolver() {
		
	}
	
	public ArrayList<Integer> solve(Node[] allNodes) {
		int n = allNodes.length;
		int iters = 3;
		ArrayList<Integer> bestPath = new ArrayList<Integer>();
		int[] starts = new int[iters];
		int hop = n/iters;
		int current = 0;
		for(int i = 0; i < iters; i++) {
			starts[i] = current;
			current += hop;
		}
		
		long bestPathLength = Long.MAX_VALUE;
		for(int i = 0; i < iters; i++) {
			ArrayList<Integer> path = nearestNeighbor(allNodes,starts[i]);
			improvePath(path,allNodes);
			long len = getPathLength(path,allNodes);
			if(len < bestPathLength) {
				bestPathLength = len;
				bestPath = path;
			}
		}
		return bestPath;
	}
	
	/**
	 * Computes the convex hull. Not complete yet.
	 */
	public ArrayList<Integer> findConvexHull(final Node[] allNodes) {
		Arrays.sort(allNodes, allNodes[0].new NodeLexComparator());
		for (int i = 0;i<allNodes.length;i++) {
			System.err.println(allNodes[i].toString());
		}
		ArrayList<Node> upper = new ArrayList<Node>(), lower = new ArrayList<Node>();
		Node current, last, secLast;
		
		for (int i = 0;i<allNodes.length;i++) {
			current = allNodes[i];
			while (lower.size() >= 2) {
				//Check whether clockwise or counter-clockwise path
				last = lower.get(lower.size()-1);
				secLast = lower.get(lower.size()-2);
				if (cross(secLast, last, current) <= 0) {
					lower.remove(lower.size()-1);
				} else {
					break;
				}
			}
			lower.add(current);
		}

		for (int i = allNodes.length-1; i>=0 ;i--) {
			current = allNodes[i];
			while (upper.size() >= 2) {
				//Check whether clockwise or counter-clockwise path
				last = upper.get(upper.size()-1);
				secLast = upper.get(upper.size()-2);
				if (cross(secLast, last, current) <= 0) {
					upper.remove(upper.size()-1);
				} else {
					break;
				}
			}
			upper.add(current);
		}

		ArrayList<Integer> hull = new ArrayList<Integer>();
		for (int i = 0;i<lower.size()-1;i++) {
			hull.add(lower.get(i).index);
		}
		for (int i = 0;i<upper.size()-1;i++) {
			hull.add(upper.get(i).index);
		}
		
		for (int i = 0;i<hull.size();i++) {
			System.err.println(hull.get(i));
		}

		return hull;
	}
	
	double cross(Node o, Node a, Node b) {
		double res = (a.x-o.x)*(b.y-o.y)-(a.y-o.y)*(b.x-o.x);
		String sign = (res >= 0) ? "non-negative" : "negative";
		System.err.println("cross of " + o + ", " + a + " and " + b + " is: " + sign);
		
		return res;
	}
	
	/*
	//Divide and conquer
	public ArrayList<Integer> findConvexHull(Node[] allNodes) {
		ArrayList<Node> all = new ArrayList<Node>().addAll(allNodes);
		Collections.sort(all, new NodeLexComparator());
		
		ArrayList<Node> hull = findConvexHull();
		ArrayList<Integer> res = new ArrayList<Integer>();
		for(int i = 0; i < hull.size(); i++) {
			res.add(hull.get(i).index);
		}
	}
	
	public ArrayList<Node> findConvexHull(ArrayList<Node> nodes) {
		//For three or fewer points, the convex hull has to be all points
		if (nodes.size() <= 3) {
			return nodes;
		}
		
		ArrayList<Node> leftHull, rightHull;
		int half = nodes.size()/2;
		leftHull = nodes.subList(0, half);
		rightHull = nodes.subList(half, nodes.size()-1);
		
		
		//TODO
		
	}
	*/
	
	public ArrayList<Integer> getInitialPath(final Node[] allNodes) {
		//TODO - now just returns a path 0,1,2,3,4...
		/*ArrayList<Integer> path = new ArrayList<Integer>();
		for(int i = 0; i < allNodes.length; i++) {
			path.add(i);
		}*/
		return nearestNeighbor(allNodes,0);
	}
	
	
	public ArrayList<Integer> nearestNeighbor(final Node[] allNodes, int startNode) {
		//start node is 0
		//assumes calcClosest have been called on all
		int n = allNodes.length;
		
		ArrayList<Integer> path = new ArrayList<Integer>();
		boolean[] used = new boolean[n];
		
		//int startNode = 0;
		path.add(startNode);
		int last = startNode;
		used[startNode] = true;
		
		for(int i = 1; i < n; i++) {
			//find closest
			int closest = findClosest(allNodes[last].closest, allNodes[last], used);
			if(closest == -1) {
				closest = findClosest(allNodes[last], used);
			}
			path.add(closest);
			used[closest] = true;
			last = closest;
		}
		return path;
	}
	
	private int findClosest(Node node, boolean[] used) {
		int n = node.n;
		int[] possibilities = new int[n];
		for(int i = 1; i < n; i++) {
			possibilities[i] = i;
		}
		return findClosest(possibilities, node, used);
	}
	
	private int findClosest(int[] possibilities, Node node, boolean[] used) {
		int minDist = Integer.MAX_VALUE;
		int minIndex = -1;
		for(int i = 0; i < possibilities.length; i++) {
			if(!used[possibilities[i]]) {
				if(node.distances[possibilities[i]] < minDist) {
					minDist = node.distances[possibilities[i]];
					minIndex = possibilities[i];
				}
			}

		}
		return minIndex;
	}
	
	
	/*''''''''''''''''''''''''''''**********************************
	 * Improving path below
	 * *********************************************************
	 */
	
	/**
	 * Improve intial path
	 * @param path The path
	 * @param allNodes
	 * @return
	 */
	public ArrayList<Integer> improvePath(ArrayList<Integer> path, final Node[] allNodes) {
		//return path; //TODO
		//int i = 0;
		for(int i = 0; i< 10;i++) {
			full2Opt(path,allNodes);
			if(!improv)
				break;
		}
		
		
		return path;
	}
	
	public ArrayList<Integer> full2Opt(ArrayList<Integer> path, final Node[] nodes) {
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
	
	public long getPathLength(ArrayList<Integer> path, final Node[] nodes) {
		long len = 0;
		int n = nodes.length;
		for(int i = 1; i < n; i++) {
			len += nodes[path.get(i-1)].distances[path.get(i)];
		}
		len += nodes[path.get(n-1)].distances[path.get(0)];
		return len;
	}
	
	
}
