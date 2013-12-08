#include <cstdlib>
#include <cmath>
#include <cstdio>
#include <vector>

#include "Path.h"
#include "TSPSolver.h"
#include "Node.h"

#define MIN_INT -2147483648
#define MAX_INT 2147483647
#define MIN_SHORT -32768
#define MAX_SHORT 32767

using namespace std;

Path::Path() {
}

Path::Path(vector<short> path) {
	p = path;
	n = path.size();
	inIndex = vector<short>();
	inIndex.resize(n);
	updateInIndex();
}

Path::Path(vector<short> path, vector<short> inIndex) {
	p = path;
	this->inIndex = inIndex;
	n = path.size();
}

void Path::updateInIndex() {
	for(int i = 0; i < n; i++) {
		int node = p[i];
		inIndex[node] = i;
	}
}

/*
  Get the i:th node in the path.
 */
int Path::get(int i) {
	return p[i];
}

/*
  Set the i:th node in the path
 */
void Path::set(int i, int val) {
	p[i] = val;
	inIndex[val] = i;
}

/*
  Get the index in the path of the node nodeIndex.
 */
int Path::inIndex(int nodeIndex) {
	return inIndex[nodeIndex];
}

/*
  Get n
 */
int Path::size() {
	return n;
}


/*
  Only for compatability with Graphical
 */
bool Path::isEmpty() {
	if(n == 0)
		return true;
	else
		return false;
}

/*
  Returns the previous node according to the path.
 */
int Path::getPrevNode(int node) {
	int i = inIndex[node]-1;
	if(i == -1)
		i = n-1;
	return p[i];
}

/*
  Returns the next node according to the path.
 */
int Path::getNextNode(int node) {
	int i = inIndex[node]+1;
	if(i == n)
		i = 0;
	return p[i];
}

void Path::move(int node, int to) {
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


void Path::flipSmart(int from, int to) {
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
void Path::flip(int from, int to) {
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

void Path::swap(int i, int j) {
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
int Path::pathDistForward(int from, int to) {
	if(from == to)
		return 0;
	
	if(inIndex[from] < inIndex[to]) {
		return inIndex[to] - inIndex[from];
	} else {
		return n - (inIndex[from] - inIndex[to]);
	}
}

int Path::pathDistBackward(int from, int to) {
	return pathDistForward(to,from);
}

short Path::operator [] (const int i) const {
	return p[i];
}

