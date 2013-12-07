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

class Node {

public:
	double x, y;
	int index, n;
	
	int distances[];
	int closest[];
	
	//Constructor
	//n = number of nodes
	Node(double x, double y, int n, int index) {
		this->x = x;
		this->y = y;
		this->n = n;
		this->index = index;
		distances = int[n];
	}
	

	int calcDistanceTo(Node n) {
		return (int)round(sqrt(pow(n.x-x, 2) + pow(n.y-y, 2)));
	}
	
	void calcClosest(int x) {
		if(x > n-1) {
		    x = n-1;
		}
		
		closest = int[x];
		int i;
		int numPlaced = 0;
		int worst = Integer.MIN_VALUE;
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

				int worstDist = Integer.MIN_VALUE;
				int worstIndex = -1;
				for(int j = 0; j < x; j++) {
					if(distances[closest[j]] > worstDist) {
						worstDist = distances[closest[j]];
						worstIndex = j;
					}
				}
				
				if(distances[i] < worstDist) {
					closest[worstIndex] = i;
					
					worst = Integer.MIN_VALUE;
					for(int j = 0; j < x; j++) {
						if(distances[closest[j]] > worst) {
							worst = distances[closest[j]];
						}
					}	
				}
			}
		}
	}

	void calcAllDistances(Node[] allNodes) {
		int n = allNodes.length;
		for(int i = 0; i < n; i++) {
			for(int j = i+1; j < n; j++) {
				int dist = allNodes[i].calcDistanceTo(allNodes[j]);
				allNodes[i].distances[j] = dist;
				allNodes[j].distances[i] = dist;
			}
		}
	}

}

