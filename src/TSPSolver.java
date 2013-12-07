import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.lang.Math;

/**
 * Class for algorithms for solving TSP
 *
 */
public class TSPSolver {
	
	private boolean improv;
	
	
	public TSPSolver() {
		
	}
	
	public Path solve(Node[] allNodes) {
		
		int n = allNodes.length;
		int iters = 3;
		Path bestPath = null;
		int[] starts = new int[iters];
		//Main.createTimeStamp("Initial variables");
		int hop = n/iters;
		//Main.createTimeStamp("division");
		//int hop = 2;
		int current = 0;
		for(int i = 0; i < iters; i++) {
			starts[i] = current;
			current += hop;
		}
		//Main.createTimeStamp("starts loop");
		long bestPathLength = Long.MAX_VALUE;
		for(int i = 0; i < iters; i++) {
			Path path = nearestNeighbor(allNodes,starts[i]);
			//Main.createTimeStamp("NN");
			//Path path = nearestNeighbor(allNodes,0);
			/*ArrayList<Integer> path = new ArrayList<Integer>();
			int t = starts[i];
			for(int j = 0; j < n; j++) {
				path.add(t);
				t++;
				if(t == n)
					t = 0;
			}*/
			
			
			f2Opt(path,allNodes);
			//Main.createTimeStamp("f2opt");
			full1Opt(path,allNodes);
			//Main.createTimeStamp("f1opt");
			//long len = 1000;
			long len = getPathLength(path,allNodes);
			//Main.createTimeStamp("path lengt callculation");
			if(len < bestPathLength) {
				bestPathLength = len;
				bestPath = path;
			}
			//Main.createTimeStamp("setting best variables");
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

	
	public Path getInitialPath(final Node[] allNodes) {
		//TODO - now just returns a path 0,1,2,3,4...
		/*ArrayList<Integer> path = new ArrayList<Integer>();
		for(int i = 0; i < allNodes.length; i++) {
			path.add(i);
		}*/
		return nearestNeighbor(allNodes,0);
	}
	
	public Path greedyEdge(final Node[] allNodes) {
		//TODO
		
		
		
		
		
		
		
		//Path res = new Path();
		return null;
	}
	
	public Path nearestNeighbor(final Node[] allNodes, int startNode) {
		//start node is 0
		//assumes calcClosest have been called on all
		int n = allNodes.length;
		
		ArrayList<Integer> pathA = new ArrayList<Integer>();
		boolean[] used = new boolean[n];
		int[] inIndex = new int[n];
		
		//int startNode = 0;
		pathA.add(startNode);
		inIndex[startNode] = 0;
		int last = startNode;
		used[startNode] = true;
		
		for(int i = 1; i < n; i++) {
			//find closest
			int closest = findClosest(allNodes[last].closest, allNodes[last], used);
			if(closest == -1) {
				closest = findClosest(allNodes[last], used);
			}
			inIndex[closest] = i;
			pathA.add(closest);
			used[closest] = true;
			last = closest;
		}
		return new Path(pathA,inIndex);
	}
	
	private int findClosest(Node node, boolean[] used) {
		int n = node.n;
		Pair<Integer, Integer>[] possibilities = (Pair<Integer, Integer>[])new Pair<?,?>[n];
		possibilities[0] = new Pair(new Integer(0), null);
		for(int i = 1; i < n; i++) {
			possibilities[i] = new Pair(new Integer(i), null);
		}
		return findClosest(possibilities, node, used);
	}
	
	private int findClosest(Pair<Integer,Integer>[] possibilities, Node node, boolean[] used) {
		int minDist = Integer.MAX_VALUE;
		int minIndex = -1;
		//System.err.println("possibilities is of length: " + possibilities.length);
		for(int i = 0; i < possibilities.length; i++) {
			//System.err.println("i is: " + i);
			Pair<Integer, Integer> tmpPair = possibilities[i];
			Integer tmpi = tmpPair.key;
			int tmpint = tmpi.intValue();
			if(!used[tmpint]) {
				if(node.distances[possibilities[i].key.intValue()] < minDist) {
					minDist = node.distances[possibilities[i].key.intValue()];
					minIndex = possibilities[i].key.intValue();
				}
			}

		}
		return minIndex;
	}
	
	/***********
	 * This is the Minimum Spanning Tree algorithm
	 */
	
	public ArrayList<Integer> mstAlg(final Node[] nodes) {
		int n = nodes.length;
		ArrayList<Integer>[] tree = mst(nodes);
		ArrayList<Integer> path = new ArrayList<Integer>();
		//path.add(0)
		recDFS(tree,path,0);
		return path;
	}
	
	private void recDFS(ArrayList<Integer>[] tree, ArrayList<Integer> path, int node) {
		path.add(node);
		for(int i = 0; i < tree[node].size(); i++) {
			recDFS(tree,path,tree[node].get(i));
		}
	}
	
	private ArrayList<Integer>[] mst(final Node[] nodes) {
		int n = nodes.length;
		ArrayList<Integer>[] tree = new ArrayList[n];
		
		
		boolean[] visited = new boolean[n];
		ArrayList<Integer> nodesInTree = new ArrayList<Integer>();
		//start in node 0
		nodesInTree.add(0);
		visited[0] = true;
		for(int i = 0; i < n; i++) {
			tree[i] = new ArrayList<Integer>();
		}
		
		for(int i = 1; i < n; i++) {
			int nextNode = -1;
			int treeNode = -1;
			int minDist = Integer.MAX_VALUE;
			
			for(int j = 0; j < nodesInTree.size(); j++) {
				int nodeI = nodesInTree.get(j);
				int s = nodes[nodeI].closest.length;
				//int localMinDist = Integer.MAX_VALUE;
				//int localNextNode = -1;
				//int localTreeNode = -1;
				boolean checkedClose = false;
				for(int k = 0; k < s; k++) {
					int cn = nodes[nodeI].closest[k].key.intValue();
					if(!visited[cn]) {
						//System.err.println("CLOSE");
						checkedClose = true;
						if(nodes[nodeI].distances[cn] < minDist) {
							minDist = nodes[nodeI].distances[cn];
							nextNode = cn;
							treeNode = nodeI;
						}
					}
				}
				
				if(!checkedClose) {
					//System.err.println("NO CLOSE");
					//all in "close" have already been looked through
					//check every node then
					for(int k = 0; k < n; k++) {
						if(!visited[k]) {
							if(nodes[nodeI].distances[k] < minDist) {
								nextNode = k;
								minDist = nodes[nodeI].distances[k];
								treeNode = nodeI;
							}
						}
					}
				}
			}
			
			//treeNode is now a node from current tree
			//nextNode is the node to connect the tree to
			
			visited[nextNode] = true;
			nodesInTree.add(nextNode);
			tree[treeNode].add(nextNode);
			//tree[nextNode].add(treeNode); //TODO
			
		}
        return tree;
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
	public void f2Opt(Path path, final Node[] allNodes) {
		//return path; //TODO
		//int i = 0;
		for(int i = 0; i< 10;i++) {
			part2Opt(path,allNodes);
			if(!improv)
				return;
		}
	}
	
	public void part2Opt(Path path, final Node[] nodes) {
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
	}
	
	private void swap(Path path, int i, int j) {
		int tmp = path.get(i);
		path.set(i,path.get(j));
		path.set(j, tmp);
	}
	
	public long getPathLength(Path path, final Node[] nodes) {
		long len = 0;
		int n = path.size();
		for(int i = 1; i < n; i++) {
			len += nodes[path.get(i-1)].distances[path.get(i)];
		}
		len += nodes[path.get(n-1)].distances[path.get(0)];
		return len;
	}
	
	/*
	 * DONT USE
	 */
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
				int n2 = nodes[i].closest[j].key.intValue();
				int n2p = (inIndex[n2] == 0 ? path.get(n-1) : path.get(inIndex[n2]-1));
				int n2n = (inIndex[n2] == n-1 ? path.get(0) : path.get(inIndex[n2]+1));
				
				for(int k = j+1; k < s; k++) {
					int n3 = nodes[i].closest[k].key.intValue();
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
						//System.err.println("ARG "+ n1 + " " + n2 + " " + n3);
						//System.err.println("INdex "+ inIndex[n1] + " " + inIndex[n2] + " " + inIndex[n3]);
						//System.err.println("LENGTH BEFORE: " + getPathLength(path,nodes));
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
						//System.err.println("LENGTH AFTER: " + getPathLength(path,nodes));
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
	
	/*
	 * PART OF DEPRECATED 3OPT, DO NOT USE
	 */
	private int triSwapOrNo(ArrayList<Integer> path, final int[] inIndex,final Node[] nodes, int[] rel) {
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
	
	/*
	 * 2 OPT with worsening - the tornment algorithm
	 * DO NOT USE FOR NOW
	 */
	public Path tornmentAlg(Path path, final Node[] nodes) {
		//improvePath(path,nodes);
		Path bestPath = (Path) path.clone();
		long bestPathLen = getPathLength(bestPath,nodes);
		final int numIters = 10;
		final int thresh = 1000;
		int n = nodes.length;
		
		long curPathLen = bestPathLen;
		for(int iter = 0; iter < numIters; iter++) {
			
			for(int i = iter%n; i < n; i++) {
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
						if(newDist - thresh < prevDist) {
							
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
							curPathLen = curPathLen - prevDist + newDist;
							
							
							if(curPathLen < bestPathLen) {
								bestPathLen = curPathLen;
								bestPath = (Path) path.clone();
							}
							
						} 
					}
					

					if(swapped) {
						//improv = true;
						//i--;
						
						
						break;
					}
				}
			}
		}
		f2Opt(bestPath,nodes);
		return bestPath;
	}
	
	
	public void full1Opt(Path path, final Node[] nodes) {
		int n = nodes.length;
		//int numSwaps = 0;
		int numIters = 10;
		
		
		for(int iter = 0; iter < numIters; iter++) {
			improv = false;
			for(int i = 0; i < n; i++) {
				int s = nodes[i].closest.length;
				int pi = path.getPrevNode(i);
				int ni = path.getNextNode(i);
				boolean swapped = false;
				
				for(int j = 0; j < s; j++) {
					int jn, js, je;
					jn = nodes[i].closest[j].key.intValue();
					if(path.inIndex(jn) < path.inIndex(i)) {
						je = jn;
						js = path.getPrevNode(je);
						if(je == i || js == i)
							continue;
					} else { //j > i
						js = jn;
						je = path.getNextNode(js);
						if(js == i || je == i)
							continue;
					}
					//System.err.println(path);
					//System.err.println("i: " + i + ", js: " + js + ", je: " + je);
					//System.err.println();
					
					
					int prevDist = nodes[js].distances[je] + nodes[pi].distances[i] + nodes[i].distances[ni];
					int newDist = nodes[js].distances[i] + nodes[i].distances[je] + nodes[pi].distances[ni];
					
					if(newDist < prevDist) {
						//swap improves
						//System.err.println("Moving node " + i + " into between " + js + " " + je);
						//System.err.println(path);
						path.move(i, js);
						//System.err.println(path);
						
						swapped = true;
						improv = true;
						//numSwaps++;
						break;
					}
					
				}
				if(swapped) {
					i--;
				}
				
			}
			if(!improv) {
				//System.err.println("did " + iter + " iters");
				break;
			}
		}

		//System.err.println("1pot num swaps: " + numSwaps);
	}
	
	/***
	 * ACTUAL AND HOPEFULLY USEFUL 3OPT
	 * running time is in milliseconds
	 */
	public void f3Opt(Path path, final Node[] nodes, int runningTime) {
		int n = nodes.length;
		int timeLeft = runningTime;
		//long prevPathLen = getPathLength(path,nodes);
		long stamp = System.currentTimeMillis();
		for(int j = 0; j < 4; j++) {
			improv = false;
			long tmp = System.currentTimeMillis();
			for(int i = 0; i < n && timeLeft > 0; i++) {
				
				if(oneEdge3Opt(path,nodes,i)) {
					improv = true;
					i--;
				}
				long nStamp = System.currentTimeMillis();
				timeLeft -= (nStamp - stamp);
				stamp = nStamp;
				//if(oneEdge3Opt(path,nodes,i))
					//return;
				//if(oneEdge3Opt(path,nodes,i))
					//System.err.println("IMPROV");
				
				//long pLen = getPathLength(path,nodes);
				//if(pLen >= prevPathLen) {
					//System.out.println("3opt made worse:");
					//System.out.println("old: " + prevPathLen);
					//System.out.println("new: " + pLen);
					
				//} else {
					//System.out.println(3opt)
					//improvs++;
				//}
			}
			if(!improv) {
				//System.err.println("3-opt ran " + j + " times");
				break;
			}
				
		}
		//System.err.println("Improvements made: " + improvs);
	}
	
	/*
	 * The edge is the index of the start node of the edge - so if it is x it means the edge x to x+1
	 * well, almost, as we have to check the indices and such etc.
	 */
	public boolean oneEdge3Opt(Path path, final Node[] nodes, int edge) {
		//pick out the closest edges to edge
		ArrayList<Integer> closestEdges = new ArrayList<Integer>();
		int edgeS = edge;
		int edgeE = path.getNextNode(edgeS);
		
		//ArrayList<Integer> allClosest = new ArrayList<Integer>();
		int s = nodes[edge].closest.length;
		for(int i = 0; i < s; i++) {
			int node = nodes[edgeS].closest[i].key.intValue();
			if(node != edgeE) {
				addIfNotIn(closestEdges,node);
				addIfNotIn(closestEdges,path.getPrevNode(node));
			}
			node = nodes[edgeE].closest[i].key.intValue();
			if(node != edgeS) {
				addIfNotIn(closestEdges,node);
				addIfNotIn(closestEdges,path.getPrevNode(node));
			}
			
		}
		
		int numEdges = closestEdges.size();
		for(int i = 0; i < numEdges; i++) {
			for(int j = i+1; j < numEdges; j++) {
				if(try3opt(path,nodes,edgeS,closestEdges.get(i),closestEdges.get(j))) {
					return true;
				}
				
			}
		}
		
		return false;
	}
	
	//returns true if changed anything
	private boolean try3opt(Path path, final Node[] nodes, int edge1, int edge2, int edge3) {
		int e1h1 = edge1;
		int e1h2 = path.getNextNode(edge1);
		int e2h1 = edge2;
		int e2h2 = path.getNextNode(edge2);
		int e3h1 = edge3;
		int e3h2 = path.getNextNode(edge3);
		
		int c1s,c1e,c2s,c2e,c3s,c3e;
		//long pLen = getPathLength(path,nodes);
		c1e = e1h1;
		c2s = e1h2;
		
		int dtmp2 = path.pathDistForward(e2h2, c1e);
		int dtmp3 = path.pathDistForward(e3h2, c1e);
		
		if(dtmp2 < dtmp3) {
			c1s = e2h2;
			c3e = e2h1;
			c2e = e3h1;
			c3s = e3h2;
		} else {
			c1s = e3h2;
			c3e = e3h1;
			c2e = e2h1;
			c3s = e2h2;
		}
		//now that we have sorted out the three remaining components, there are 8 possible ways to put
		//them together to one resulting path:
		//c1e-c2s, c2e-c3s, c3e-c1s (current)
		//c1e-c2s, c2e-c3e, c3s-c1s (2-opt between edges 2 and 3)
		//c1e-c2e, c2s-c3s, c3e-c1s (2-opt between edges 1 and 2 (or 3 depending on situation))
		//c1e-c2e, c2s-c3e, c3s-c1s (3-opt)
		//c1e-c3s, c3e-c2s, c2e-c1s (3-opt)
		//c1e-c3s, c3e-c2e, c2s-c1s (3-opt)
		//c1e-c3e, c3s-c2s, c2e-c1s (3-opt)
		//c1e-c3e, c3s-c2e, c2s-c1s (2-opt between edges 1 and 3 (or 2 depending on situation))
		
		//these distances appear in order in the array here
		int[] allDists = new int[8];
		
		allDists[0] = nodes[c1e].distances[c2s] + nodes[c2e].distances[c3s] + nodes[c3e].distances[c1s];
		allDists[1] = nodes[c1e].distances[c2s] + nodes[c2e].distances[c3e] + nodes[c3s].distances[c1s];
		allDists[2] = nodes[c1e].distances[c2e] + nodes[c2s].distances[c3s] + nodes[c3e].distances[c1s];
		allDists[3] = nodes[c1e].distances[c2e] + nodes[c2s].distances[c3e] + nodes[c3s].distances[c1s];
		allDists[4] = nodes[c1e].distances[c3s] + nodes[c3e].distances[c2s] + nodes[c2e].distances[c1s];
		allDists[5] = nodes[c1e].distances[c3s] + nodes[c3e].distances[c2e] + nodes[c2s].distances[c1s];
		allDists[6] = nodes[c1e].distances[c3e] + nodes[c3s].distances[c2s] + nodes[c2e].distances[c1s];
		allDists[7] = nodes[c1e].distances[c3e] + nodes[c3s].distances[c2e] + nodes[c2s].distances[c1s];
		
		//find best configuration:
		
		
		//TODO try worst configuration of those that improve?
		int bestConfig = -1;
		int bestPath = Integer.MAX_VALUE;
		for(int i = 0; i < 8; i++) {
			if(allDists[i] < bestPath) {
				bestPath = allDists[i];
				bestConfig = i;
			}
		}
		if(bestConfig == 0) {
			//no configuration improved
			return false;
		}
		switch(bestConfig) {
		case 0:
			return false;
		case 1: 
			path.flipSmart(path.inIndex(c3s),path.inIndex(c3e));
			break;
		case 2:
			path.flipSmart(path.inIndex(c2s),path.inIndex(c2e));
			break;
		case 3:
			path.flip(path.inIndex(c2s),path.inIndex(c2e));
			path.flipSmart(path.inIndex(c3s),path.inIndex(c3e));
			break;
		case 4:
			path.flip(path.inIndex(c1s),path.inIndex(c1e));
			path.flip(path.inIndex(c2s),path.inIndex(c2e));
			path.flipSmart(path.inIndex(c3s),path.inIndex(c3e));
			break;
		case 5:
			path.flip(path.inIndex(c1s),path.inIndex(c1e));
			path.flipSmart(path.inIndex(c3s),path.inIndex(c3e));
			break;
		case 6:
			path.flip(path.inIndex(c1s),path.inIndex(c1e));
			path.flipSmart(path.inIndex(c2s),path.inIndex(c2e));
			break;
		case 7:
			path.flipSmart(path.inIndex(c1s),path.inIndex(c1e));
			break;
		}
		/*
		long pLen2 = getPathLength(path,nodes);
		if(pLen2 > pLen) {
			System.err.println("3opt made worse:");
			System.err.println("best config: " + bestConfig);
			for(int i = 0; i < 8; i++) {
				System.err.println(allDists[i]);
			}
			System.err.println(e1h1+"-"+e1h2+" "+e2h1+"-"+e2h2+" "+e3h1+"-"+e3h2);
			System.err.println("c1: " + c1s + " " + c1e);
			System.err.println("c2: " + c2s + " " + c2e);
			System.err.println("c3: " + c3s + " " + c3e);
			//System.err.println(path.inIndex(edge1)+" "+path.inIndex(edge2)+" "+path.inIndex(edge3));
			//System.out.println("old: " + pLen);
			//System.out.println("new: " + pLen2);
		}
		*/
		return true;
	}
	
	
	private void addIfNotIn(ArrayList<Integer> al, int i) {
		for(int j = 0; j < al.size(); j++) {
			if(al.get(j) == i)
				return;
		}
		al.add(i);
	}
	
}
