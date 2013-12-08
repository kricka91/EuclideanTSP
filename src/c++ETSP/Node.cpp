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


//Constructor
//n = number of nodes
Node::Node(double x, double y, int n, int index) {
	this->x = x;
	this->y = y;
	this->n = n;
	this->index = index;
	distances = vector<int>();
	distances.resize(n);
}


int Node::calcDistanceTo(Node n) {
	return (int)round(sqrt(pow(n.x-x, 2) + pow(n.y-y, 2)));
}

void Node::calcClosest(int x) {
	if(x > n-1) {
	    x = n-1;
	}

	closest.resize(x);
	int i;
	int numPlaced = 0;
	int worst = MIN_INT;
	for(i = 0; numPlaced < x; i++) {
		if(i != index) {
			closest[numPlaced] = i;
			numPlaced++;
			if(distances[i] > worst)
				worst = distances[i];
		}
	}
	
	for(; i < n; i++) {
		if(i != index) {
			if(distances[i] > worst) {
				continue;
			}

			int worstDist = MIN_INT;
			int worstIndex = -1;
			for(int j = 0; j < x; j++) {
				if(distances[closest[j]] > worstDist) {
					worstDist = distances[closest[j]];
					worstIndex = j;
				}
			}
			
			if(distances[i] < worstDist) {
				closest[worstIndex] = i;
				
				worst = MIN_INT;
				for(int j = 0; j < x; j++) {
					if(distances[closest[j]] > worst) {
						worst = distances[closest[j]];
					}
				}	
			}
		}
	}
}

void calcAllDistances(vector<Node> allNodes) {
	int n = allNodes.size();
	for(int i = 0; i < n; i++) {
		for(int j = i+1; j < n; j++) {
			int dist = allNodes[i].calcDistanceTo(allNodes[j]);
			allNodes[i].distances[j] = dist;
			allNodes[j].distances[i] = dist;
		}
	}
}


