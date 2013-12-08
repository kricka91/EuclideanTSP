#include <cstdlib>
#include <cmath>
#include <cstdio>
#include <vector>
#include <random>
#include <ctime>

#include "Path.h"
#include "TSPSolver.h"
#include "Node.h"

#define MIN_INT -2147483648
#define MAX_INT 2147483647
#define MIN_SHORT -32768
#define MAX_SHORT 32767

using namespace std;

long startTime, endTime, partialTime;
bool measureTime = true;

void printPath(Path path) {
	for(int i = 0; i < path.size(); i++) {
		fprintf(stdout, "%d\n", path.get(i));
	}
}

void readData(vector<Node> nodes) {
	int n;
	double x, y;
	scanf("%d", &n);
	nodes.reserve(n);
	for(int i = 0; i < n; i++) {
		scanf("%lg %lg", x, y);
		nodes[i] = Node(x,y,n,i);
	}		
}


int main() {
	srand(time(NULL));
	if (measureTime) {

	}
	
	vector<Node> nodes;
	readData(nodes);
	int n = nodes.size();
	
	Path path;
	Path solvepath;



	// calculate all distances.
	calcAllDistances(nodes);
	//createTimeStamp("distance computation");
	for(int i = 0; i < n; i++) {
		nodes[i].calcClosest(10);
	}
	//createTimeStamp("closest");



    path = getInitialPath(nodes);
    f3Opt(path, nodes, 100);


    
    long pLen = getPathLength(path, nodes);
    long stamp = time(NULL);
    Path ptmp = path;

  
    int iters = 0;
    int improvs = 0;
    while(time(NULL) - stamp < 1000) {
        //make a few... adjustments huehue
        //Path ptmp = (Path) path.clone();
        ptmp.swap(rand()%n, rand()%n);
        f3Opt(ptmp, nodes, 50);
        long tmpLen = getPathLength(ptmp, nodes);
        if(tmpLen < pLen) {
            path = ptmp;
            pLen = tmpLen;
            improvs++;
            //addPartRes("improvement made!", path);
        }
        iters++;
    }

	
	printPath(path);

}


