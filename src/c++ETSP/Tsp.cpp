#include <cstdlib>
#include <cmath>
#include <cstdio>
#include <vector>
#include <random>

#include "Path.h"
#include "TSPSolver.h"
#include "Node.h"
#include "timing.h"

#define MIN_INT -2147483648
#define MAX_INT 2147483647
#define MIN_SHORT -32768
#define MAX_SHORT 32767

using namespace std;

void printPath(Path path) {
	for(int i = 0; i < path.size(); i++) {
		fprintf(stdout, "%d\n", path.get(i));
	}
}

void readData(vector<Node>& nodes) {
	int n;
	double x, y;
	scanf("%d", &n);
	nodes.reserve(n);
	for(int i = 0; i < n; i++) {
		scanf("%lg %lg", &x, &y);
		nodes.push_back(Node(x,y,n,i));
	}		
}

void genRandomProblem(vector<Node>& nodes, int n) {
	nodes.reserve(n);
	srand(getTime());
	double x, y;
	for(int i = 0; i < n; i++) {
		x = ((double)rand()/(double)(RAND_MAX))*1000000;
		y = ((double)rand()/(double)(RAND_MAX))*1000000;
		fprintf(stderr, "Generating node %d: (%d %d)\n", i, x, y);
		nodes.push_back(Node(x,y,n,i));
	}
	return nodes;
}

int main(int argc, char** argv) {
	startTime = getTime();
	srand(time(NULL));
	rand();

	vector<Node> nodes = new vector<Node>();
	
	if (argc > 1) {
		fprintf(stderr, "Generating 10 nodes\n");
		genRandomProblem(nodes, 10);
	} else {
		readData(nodes);
	}
	
	fprintf(stderr, "Checkpoint -2\n");
	
	if (measureTime) {

	}

	int n = nodes.size();
	
	Path path;
	Path solvepath;
	fprintf(stderr, "Checkpoint -1\n");

	// calculate all distances.
	calcAllDistances(nodes);
	//createTimeStamp("distance computation");
	for(int i = 0; i < n; i++) {
		nodes[i].calcClosest(10);
	}
	//createTimeStamp("closest");
	fprintf(stderr, "Checkpoint 0\n");

    path = getInitialPath(nodes);
    fprintf(stderr, "Checkpoint 0.1\n");
    f3Opt(path, nodes, 100);
    fprintf(stderr, "Checkpoint 0.2\n");
    
    long pLen = getPathLength(path, nodes);
    long stamp = time(NULL);
    Path ptmp = path;
  
    int iters = 0;
    int improvs = 0;
    
    fprintf(stderr, "Checkpoint 1\n");
    fprintf(stderr, "Time since start: %d\n", timeSinceStart());
    
    while(timeSinceStart() < 1950000) {
    	//fprintf(stderr, "One iteration %d\n", getTime());
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
    
    fprintf(stderr, "Checkpoint 2\n");
    fprintf(stderr, "Time since start: %d\n", timeSinceStart());

	printPath(path);
}


