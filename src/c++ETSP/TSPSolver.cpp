#include <cstdlib>
#include <cmath>
#include <cstdio>
#include <vector>
#include <ctime>

#include "Path.h"
#include "TSPSolver.h"
#include "Node.h"
#include "timing.h"

#define MIN_INT -2147483648
#define MAX_INT 2147483647
#define MIN_SHORT -32768
#define MAX_SHORT 32767


using namespace std;

class Path;
class Node;

bool improv;

Path solve(vector<Node> allNodes) {
	
	int n = allNodes.size();
	int iters = 3;
	Path bestPath;
	int starts[iters];
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
	long bestPathLength = MAX_INT;
	for(int i = 0; i < iters; i++) {
		Path path = nearestNeighbor(allNodes,starts[i]);
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


Path getInitialPath(vector<Node> allNodes) {
	return nearestNeighbor(allNodes,0);
}


Path nearestNeighbor(vector<Node> allNodes, int startNode) {
	//start node is 0
	//assumes calcClosest have been called on all
	fprintf(stderr, "Checkpoint NN1\n");
	fprintf(stderr, "allNodes.size() is %d\n", (int)allNodes.size());
	int n = allNodes.size();
	fprintf(stderr, "Checkpoint NN2\n");
	
	vector<short> pathA = vector<short>();
	bool used[n];
	vector<short> inIndex = vector<short>();
	inIndex.resize(n);
	fprintf(stderr, "Checkpoint NN3\n");
	
	//int startNode = 0;
	pathA.push_back(startNode);
	fprintf(stderr, "Checkpoint NN4\n");
	inIndex[startNode] = 0;
	fprintf(stderr, "Checkpoint NN5\n");
	int last = startNode;
	fprintf(stderr, "Checkpoint NN6\n");
	used[startNode] = true;
	fprintf(stderr, "Checkpoint NN7\n");
	
	for(int i = 1; i < n; i++) {
		//find closest
		int closest = findClosest(allNodes[last].closest, allNodes[last], used);
		if(closest == -1) {
			closest = findClosest(allNodes[last], used);
		}
		inIndex[closest] = i;
		pathA.push_back(closest);
		used[closest] = true;
		last = closest;
	}
	return Path(pathA,inIndex);
}

int findClosest(vector<short> possibilities, Node node, bool used[]) {
	int minDist = MAX_INT;
	int minIndex = -1;
	//System.err.println("possibilities is of length: " + possibilities.size());
	for(int i = 0; i < possibilities.size(); i++) {
		//System.err.println("i is: " + i);
		if(!used[possibilities[i]]) {
			if(node.distances[possibilities[i]] < minDist) {
				minDist = node.distances[possibilities[i]];
				minIndex = possibilities[i];
			}
		}

	}
	return minIndex;
}

int findClosest(Node node, bool used[]) {
	int n = node.n;
	vector<short> possibilities;
	possibilities = vector<short>();
	possibilities.resize(n);
	possibilities[0] = 0;
	for(int i = 1; i < n; i++) {
		possibilities[i] = i;
	}
	return findClosest(possibilities, node, used);
}


void f2Opt(Path path, vector<Node> allNodes) {
	//return path; //TODO
	//int i = 0;
	for(int i = 0; i< 10;i++) {
		part2Opt(path,allNodes);
		if(!improv)
			return;
	}
}

void part2Opt(Path path, vector<Node> nodes) {
	int n = nodes.size();
	improv = false;
	
	for(int i = 0; i < n; i++) {
		for(int j = i+2; j < n; j++) {
			bool swapped = false;
			if(!(i == 0 && j == n-1)) {
				//these below stand for i-start, i-end, j-start, j-end.
				//We are trying to swap from the paths is-ie and js-je to is-js and ie-je
				int is = i;
				int ie = (i == n-1 ? 0 : i+1);  
				int js = j;
				int je = (j == n-1 ? 0 : j+1); 
				
				//see if swap is better
				int prevDist = nodes[path[is]].distances[path[ie]] +
						       nodes[path[js]].distances[path[je]];
				int newDist = nodes[path[is]].distances[path[js]] +
					          nodes[path[ie]].distances[path[je]];
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

void swap(Path path, int i, int j) {
	int tmp = path[i];
	path.set(i,path[j]);
	path.set(j, tmp);
}

long getPathLength(Path path, vector<Node> nodes) {
	long len = 0;
	int n = path.size();
	for(int i = 1; i < n; i++) {
		len += nodes[path[i-1]].distances[path[i]];
	}
	len += nodes[path[n-1]].distances[path[0]];
	return len;
}


void tripleSwap(vector<short> path, int n1, int n2, int n3) {
	//int tmp2 = path[n2];
	int tmp2 = path[n2];
	path[n2] = path[n1];
	path[n1] = path[n3];
	path[n3] = tmp2;
}

void full1Opt(Path path, vector<Node> nodes) {
	int n = nodes.size();
	//int numSwaps = 0;
	int numIters = 10;
	
	for(int iter = 0; iter < numIters; iter++) {
		improv = false;
		for(int i = 0; i < n; i++) {
			int s = nodes[i].closest.size();
			int pi = path.getPrevNode(i);
			int ni = path.getNextNode(i);
			bool swapped = false;
			
			for(int j = 0; j < s; j++) {
				int jn, js, je;
				jn = nodes[i].closest[j];
				if(path.inIndexGet(jn) < path.inIndexGet(i)) {
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


void f3Opt(Path path, vector<Node> nodes, int runningTime) {
	int n = nodes.size();
	int timeLeft = runningTime;
	//long prevPathLen = getPathLength(path,nodes);
	long stamp = getTime();
	for(int j = 0; j < 4; j++) {
		improv = false;
		long tmp = getTime();
		for(int i = 0; i < n && timeLeft > 0; i++) {
			
			if(oneEdge3Opt(path,nodes,i)) {
				improv = true;
				i--;
			}
			long nStamp = getTime();
			timeLeft -= (nStamp - stamp);
			stamp = nStamp;
		}
		if(!improv) {
			//fprintf(stderr, "3-opt ran %d times\n", j);
			break;
		}
			
	}
	//System.err.println("Improvements made: " + improvs);
}


bool oneEdge3Opt(Path path, vector<Node> nodes, int edge) {
	//pick out the closest edges to edge
	vector<short> closestEdges = vector<short>();
	int edgeS = edge;
	int edgeE = path.getNextNode(edgeS);

	int s = nodes[edge].closest.size();
	for(int i = 0; i < s; i++) {
		int node = nodes[edgeS].closest[i];
		if(node != edgeE) {
			addIfNotIn(closestEdges,node);
			addIfNotIn(closestEdges,path.getPrevNode(node));
		}
		node = nodes[edgeE].closest[i];
		if(node != edgeS) {
			addIfNotIn(closestEdges,node);
			addIfNotIn(closestEdges,path.getPrevNode(node));
		}
		
	}
	
	int numEdges = closestEdges.size();
	for(int i = 0; i < numEdges; i++) {
		for(int j = i+1; j < numEdges; j++) {
			if(try3opt(path,nodes,edgeS,closestEdges[i],closestEdges[j])) {
				return true;
			}
			
		}
	}
	
	return false;
}

//returns true if changed anything
bool try3opt(Path path, vector<Node> nodes, int edge1, int edge2, int edge3) {
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
	vector<int> allDists = vector<int>();
	allDists.resize(8);
	
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
	int bestPath = MAX_INT;
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
			path.flipSmart(path.inIndexGet(c3s),path.inIndexGet(c3e));
			break;
		case 2:
			path.flipSmart(path.inIndexGet(c2s),path.inIndexGet(c2e));
			break;
		case 3:
			path.flip(path.inIndexGet(c2s),path.inIndexGet(c2e));
			path.flipSmart(path.inIndexGet(c3s),path.inIndexGet(c3e));
			break;
		case 4:
			path.flip(path.inIndexGet(c1s),path.inIndexGet(c1e));
			path.flip(path.inIndexGet(c2s),path.inIndexGet(c2e));
			path.flipSmart(path.inIndexGet(c3s),path.inIndexGet(c3e));
			break;
		case 5:
			path.flip(path.inIndexGet(c1s),path.inIndexGet(c1e));
			path.flipSmart(path.inIndexGet(c3s),path.inIndexGet(c3e));
			break;
		case 6:
			path.flip(path.inIndexGet(c1s),path.inIndexGet(c1e));
			path.flipSmart(path.inIndexGet(c2s),path.inIndexGet(c2e));
			break;
		case 7:
			path.flipSmart(path.inIndexGet(c1s),path.inIndexGet(c1e));
			break;
	}

	return true;
}


void addIfNotIn(vector<short> al, int i) {
	for(int j = 0; j < al.size(); j++) {
		if(al[j] == i) {
			return;
		}
	}
	al.push_back(i);
}

