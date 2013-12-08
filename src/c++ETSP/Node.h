#ifndef _NODE_H_
#define _NODE_H_

#include "Path.h"
#include "TSPSolver.h"

using namespace std;

class Node {

public:
	double x, y;
	int index, n;
	vector<int> distances;
	vector<short> closest;
	
	//Constructor
	//n = number of nodes
	Node(double x, double y, int n, int index);
	int calcDistanceTo(Node n);
	void calcClosest(int x);
	
};

void calcAllDistances(vector<Node> allNodes);

#endif

