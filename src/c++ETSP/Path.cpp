#include <cstdlib>
#include <cmath>
#include <cstdio>

#include "Path.cpp"
#include "TSPSolver.cpp"

#define MIN_INT -2147483648
#define MAX_INT 2147483647
#define MIN_SHORT -32768
#define MAX_SHORT 32767

using namespace std;

class Path {

public:

	vector<short> p;
	int[] inIndex;
	int n;
	
	Path(vector<short> path) {
		p = path;
		n = path.size();
		inIndex = new int[n];
		updateInIndex();
	}
	
	Path(vector<short> path, int inIndex[]) {
		p = path;
		this->inIndex = inIndex;
		n = path.size();
	}
	
	void updateInIndex() {
		for(int i = 0; i < n; i++) {
			int node = p[i];
			inIndex[node] = i;
		}
	}
	
	/*
	  Get the i:th node in the path.
	 */
	int get(int i) {
		return p[i];
	}
	
	/*
	  Set the i:th node in the path
	 */
	void set(int i, int val) {
		p[i] = val;
		inIndex[val] = i;
	}
	
	/*
	  Get the index in the path of the node nodeIndex.
	 */
	int inIndex(int nodeIndex) {
		return inIndex[nodeIndex];
	}
	
	/*
	  Get n
	 */
	int size() {
		return n;
	}

	
	/*
	  Only for compatability with Graphical
	 */
	bool isEmpty() {
		if(n == 0)
			return true;
		else
			return false;
	}
	
	/*
	  Returns the previous node according to the path.
	 */
	int getPrevNode(int node) {
		int i = inIndex[node]-1;
		if(i == -1)
			i = n-1;
		return p[i];
	}
	
	/*
	  Returns the next node according to the path.
	 */
	int getNextNode(int node) {
		int i = inIndex[node]+1;
		if(i == n)
			i = 0;
		return p[i];
	}
	
	void move(int node, int to) {
		int nodeIndex = inIndex[node];
		int toIndex = inIndex[to];
		if(nodeIndex > toIndex) {
			int s = toIndex+1;
			for(int i = nodeIndex-1; i >= s; i--) {
				set(i+1, p[i]);
			}
			set(s,node);
		} else { //toIndex > nodeIndex
			for(int i = nodeIndex+1; i <= toIndex; i++) {
				set(i-1,p[i]);
			}
			set(toIndex,node);
		}
	}
	
	
	void flipSmart(int from, int to) {
		int otherTo = from-1;
		if(otherTo == -1)
			otherTo = n-1;
		int otherFrom = to+1;
		if(otherFrom == n)
			otherFrom = 0;
		
		
		if(pathDistForward(from,to) <= pathDistForward(otherFrom,otherTo)) {
			flip(from, to);
		} else {
			flip(otherFrom,otherTo);
		}
	}
	
	/*
	  Flip all nodes between path indices from and to.
	  Example 1:
	  input: p={0,1,2,3,4},from=1,to=4, output= {0,4,3,2,1}
	  input: p={0,1,2,3,4},from=3,to=1, output= {4,3,2,1,0}
	 */
	void flip(int from, int to) {
		if(from == to)
			return;
		
		if(from <= to) {
			int mid = ((to-from)/2)+1;
			for(int k = 0; k < mid; k++) {
				swap(from+k,to-k);
			}
		} else {
			int mid = ((n-(from-to))/2)+1;
			int jc = from;
			int ic = to;
			for(int k = 0; k < mid; k++) {
				swap(ic,jc);
				ic--;
				jc++;
				if(jc == n)
					jc = 0;
				if(ic == -1)
					ic = n-1;
			}
		}
	}
	
	void swap(int i, int j) {
		int tmp = p[i];
		set(i,p.get(j));
		set(j, tmp);
	}
	
	/*
	  Some methods for distances between nodes along the path.
	  The metric of the distances is number of edges to traverse to get to the other node.
	  input are nodes!
	 */
	
	//distance of moving forward from from to to.
	int pathDistForward(int from, int to) {
		if(from == to)
			return 0;
		
		if(inIndex[from] < inIndex[to]) {
			return inIndex[to] - inIndex[from];
		} else {
			return n - (inIndex[from] - inIndex[to]);
		}
	}
	
	int pathDistBackward(int from, int to) {
		return pathDistForward(to,from);
	}

}
