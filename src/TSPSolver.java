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
			//System.err.println(allNodes[i].toString());
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
			//System.err.println(hull.get(i));
		}

		return hull;
	}
	
	/**
	 * Computes the signed area of the triangle oab
	 *
	 * The sign determines whether the turn oab is clockwise or counter-clockwise.
	 */
	double cross(Node o, Node a, Node b) {
		double res = (a.x-o.x)*(b.y-o.y)-(a.y-o.y)*(b.x-o.x);
		//String sign = (res >= 0) ? "non-negative" : "negative";
		//System.err.println("cross of " + o + ", " + a + " and " + b + " is: " + sign);
		return res;
	}
	
	/**
	 * Add the remaining nodes to a pre-existing subpath
	 */
	public ArrayList<Integer> addRemainingNodes(Node[] nodes, ArrayList<Integer> visited) {
		
		ArrayList<Integer> res = new ArrayList<Integer>();
		res.addAll(visited);
		int cheapest;
		int insNode, insNeighbor;
		int curLoss;
		
		ArrayList<Integer> remaining = new ArrayList<Integer>();
		for (int i = 0;i<nodes.length;i++) {
			if (visited.contains(new Integer(nodes[i].index))) {
				//Do nothing
			} else {
				remaining.add(new Integer(nodes[i].index));
			}
		}
		
		/*
		System.err.println("visited contains:");
		for (int i = 0;i<visited.size();i++) {
			System.err.println(visited.get(i));
		}
		
		System.err.println("remaining contains:");
		for (int i = 0;i<remaining.size();i++) {
			System.err.println(remaining.get(i));
		}
		*/
		
		while (!remaining.isEmpty()) {
		
			cheapest = Integer.MAX_VALUE;
			insNode = -1;
			insNeighbor = -1;
		
			for (int i = 0;i<remaining.size();i++) {
				for (int j = 0;j<res.size()-1;j++) {
					curLoss = nodes[remaining.get(i)].distances[res.get(j)];
					curLoss += nodes[remaining.get(i)].distances[res.get(j+1)];
					curLoss -= nodes[res.get(j)].distances[res.get(j+1)];
					
					if (curLoss < cheapest) {
						cheapest = curLoss;
						insNode = i;
						insNeighbor = j;
					}
				}
				
				curLoss = nodes[remaining.get(i)].distances[res.get(res.size()-1)];
				curLoss += nodes[remaining.get(i)].distances[res.get(0)];
				curLoss -= nodes[res.get(res.size()-1)].distances[res.get(0)];
				
				if (curLoss < cheapest) {
					cheapest = curLoss;
					insNode = i;
					insNeighbor = res.size()-1;
				}
				
			}
			
			//System.err.print("Want to visit node: " + remaining.get(insNode));
			//System.err.print(" between " + res.get(insNeighbor) + " and ");
			//System.err.println("" + res.get((insNeighbor+1)%(res.size())));
			res.add(insNeighbor+1, remaining.get(insNode));
			remaining.remove(insNode);
		}
		
		return res;
	}

	
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
		int n = path.size();
		for(int i = 1; i < n; i++) {
			len += nodes[path.get(i-1)].distances[path.get(i)];
		}
		len += nodes[path.get(n-1)].distances[path.get(0)];
		return len;
	}
	
	public ArrayList<Integer> s3opt(ArrayList<Integer> path, final Node[] nodes) {
		int n = nodes.length;
		//ArrayList<Integer> lpath = (ArrayList<Integer>) path.clone();
		int[] inIndex = new int[n];
		for(int i = 0; i < n; i++) {
			inIndex[path.get(i)] = i;
		}
		//System.err.println(path);
		///for(int i = 0; i < inIndex.length; i++) {
			//System.err.print(inIndex[i] + " ");
		//}
		//System.err.println();
		
		
		for(int i = 0; i < n; i++) {
			int s = nodes[i].closest.length;
			
			int n1 = i;
			int n1p = (inIndex[n1] == 0 ? path.get(n-1) : path.get(inIndex[n1]-1));
			int n1n = (inIndex[n1] == n-1 ? path.get(0) : path.get(inIndex[n1]+1));
			boolean swapped = false;
			for(int j = 0; j < s; j++) {
				int n2 = nodes[i].closest[j];
				int n2p = (inIndex[n2] == 0 ? path.get(n-1) : path.get(inIndex[n2]-1));
				int n2n = (inIndex[n2] == n-1 ? path.get(0) : path.get(inIndex[n2]+1));
				
				for(int k = j+1; k < s; k++) {
					int n3 = nodes[i].closest[k];
					int n3p = (inIndex[n3] == 0 ? path.get(n-1) : path.get(inIndex[n3]-1));
					int n3n = (inIndex[n3] == n-1 ? path.get(0) : path.get(inIndex[n3]+1));
					
					/*
					System.err.println("Full path is: " + path);
					System.err.println(n1p + " " + n1 + " " + n1n);
					System.err.println(n2p + " " + n2 + " " + n2n);
					System.err.println(n3p + " " + n3 + " " + n3n);
					System.err.println();
					*/
					
					int swap = triSwapOrNo(path,inIndex,nodes,new int[] {n1p,n1,n1n,n2p,n2,n2n,n3p,n3,n3n}); 
					//0 == dont swap, 1 == swap forward, 2 == swap backward
					
					if(swap != 0) {
						System.err.println("ARG "+ n1 + " " + n2 + " " + n3);
						//System.err.println("INdex "+ inIndex[n1] + " " + inIndex[n2] + " " + inIndex[n3]);
						System.err.println("LENGTH BEFORE: " + getPathLength(path,nodes));
						swapped = true;
					}
						
					
					if(swap == 1) {
						tripleSwap(path,inIndex[n1],inIndex[n2],inIndex[n3]);
						int tmp1 = inIndex[n1];
						inIndex[n1] = inIndex[n2];
						inIndex[n2] = inIndex[n3];
						inIndex[n3] = tmp1;
					} else if(swap == 2) {
						tripleSwap(path,inIndex[n3],inIndex[n2],inIndex[n1]);
						int tmp1 = inIndex[n1];
						inIndex[n1] = inIndex[n3];
						inIndex[n3] = inIndex[n2];
						inIndex[n2] = tmp1;
					}
					if(swap != 0) {
						//System.err.println("ARG "+ n1 + " " + n2 + " " + n3);
						System.err.println("LENGTH AFTER: " + getPathLength(path,nodes));
						//System.err.println(path);
						//for(int a = 0; a < inIndex.length; a++) {
						//	System.err.print(inIndex[a] + " ");
						//}
						//System.err.println();
					}
					
					//if(!legalPath(path,nodes))
					//	System.out.println("NOT LEGAL PATH WTF ARE U DOING"); //TODO remove this
					if(swapped)
						break;
					
					//if(swapped)
						//System.err.println("SWAPPED " + n1 + " " + n2 + " " + n3);
				}
				if(swapped)
					break;
			} 
			if(swapped) 
				i=0;
			
		}
		
		return path;
	}
	
	private int findPrev(ArrayList<Integer> path, int n) {
		if(path.get(0) == n) {
			return path.size()-1;
		}
		
		
		for(int i = 1; i < path.size(); i++) {
			if(path.get(i) == n) {
				return path.get(i-1);
			}
		}
		
		return -1;
	}
	
	public int triSwapOrNo(ArrayList<Integer> path, final int[] inIndex,final Node[] nodes, int[] rel) {
		int n1p = rel[0]; int n1 = rel[1]; int n1n = rel[2];
		int n2p = rel[3]; int n2 = rel[4]; int n2n = rel[5];
		int n3p = rel[6]; int n3 = rel[7]; int n3n = rel[8];
		
		
		int prevDist = nodes[n1p].distances[n1] + nodes[n1].distances[n1n]
			       + nodes[n2p].distances[n2] + nodes[n2].distances[n2n]
			       + nodes[n3p].distances[n3] + nodes[n3].distances[n3n];
	
		int newFDist = nodes[n1p].distances[n3] + nodes[n3].distances[n1n]
		       + nodes[n2p].distances[n1] + nodes[n1].distances[n2n]
		       + nodes[n3p].distances[n2] + nodes[n2].distances[n3n];
	
		int newBDist = nodes[n1p].distances[n2] + nodes[n2].distances[n1n]
		       + nodes[n2p].distances[n3] + nodes[n3].distances[n2n]
		       + nodes[n3p].distances[n1] + nodes[n1].distances[n3n];
		
		if(n1n == n2) {
			prevDist -= nodes[n1].distances[n2];
			newFDist -= nodes[n3].distances[n1];
			newBDist -= nodes[n2].distances[n3];
			return 0;
		} else if(n1p == n2) {
			prevDist -= nodes[n2].distances[n1];
			newFDist -= nodes[n3].distances[n2];
			newBDist -= nodes[n1].distances[n3];
			return 0;
		}
		
		if(n1n == n3) {
			prevDist -= nodes[n1].distances[n3];
			newFDist -= nodes[n2].distances[n1];
			newBDist -= nodes[n3].distances[n2];
			return 0;
		} else if(n1p == n3) {
			prevDist -= nodes[n3].distances[n1];
			newFDist -= nodes[n2].distances[n3];
			newBDist -= nodes[n1].distances[n2];
			return 0;
		}
		
		if(n2n == n3) {
			prevDist -= nodes[n2].distances[n3];
			newFDist -= nodes[n1].distances[n2];
			newBDist -= nodes[n3].distances[n1];
			return 0;
		} else if(n2p == n3) {
			prevDist -= nodes[n3].distances[n2];
			newFDist -= nodes[n1].distances[n3];
			newBDist -= nodes[n2].distances[n1];
			return 0;
		}
		
	
		int swap = 0;
		if(newFDist < prevDist) {
			//forward swap improves!
			swap = 1;
			prevDist = newFDist;
		}
	
		if(newBDist < prevDist) {
			swap = 2;
		}
		return swap;
	}
	

	private void tripleSwap(ArrayList<Integer> path, int n1, int n2, int n3) {
		//int tmp2 = path.get(n2);
		int tmp2 = path.get(n2);
		path.set(n2, path.get(n1));
		path.set(n1, path.get(n3));
		path.set(n3, tmp2);
	}
	
	//for debugging purposes only
	private boolean legalPath(ArrayList<Integer> path, Node[] nodes) {
		if(nodes.length != path.size())
			return false;
		
		int n = nodes.length;
		boolean[] b = new boolean[n];
		
		for(int i = 0; i < n; i++) {
			if(b[path.get(i)])
				return false;
			b[path.get(i)] = true;
		}
		return true;
	}
}
