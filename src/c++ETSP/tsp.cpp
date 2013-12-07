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

class Main {

public:

	long startTime, endTime, partialTime;

	Node[] readData() {
		int n;
		double x, y;
		scanf("%d", &n);
		Node[] nodes = Node[n];
		for(int i = 0; i < n; i++) {
			scanf("%f %f", x, y);
			nodes[i] = Node(x,y,n,i);
		}
		return nodes;		
	}
	

	int main() {
		if (measureTime) {
			startTime = System.nanoTime();
			partialTimeNames = new ArrayList<String>();
			partialTimes = new ArrayList<Long>();
		}
		
		Node nodes[];
		int n = nodes.length;
		
		Path path;

		Path solvepath;



		// calculate all distances.
		Node.calcAllDistances(nodes);
		createTimeStamp("distance computation");
		for(int i = 0; i < n; i++) {
			nodes[i].calcClosest(10);
		}
		createTimeStamp("closest");



        path = getInitialPath(nodes);
        f3Opt(path, nodes, 100);


        
        long pLen = getPathLength(path, nodes);
        long stamp = System.currentTimeMillis();
        Random r = new Random();
        Path ptmp = (Path) path.clone();

      
        int iters = 0;
        int improvs = 0;
        while(System.currentTimeMillis() - stamp < 1000) {
            //make a few... adjustments huehue
            //Path ptmp = (Path) path.clone();
            ptmp.swap(r.nextInt(n), r.nextInt(n));
            f3Opt(ptmp, nodes, 50);
            long tmpLen = getPathLength(ptmp, nodes);
            if(tmpLen < pLen) {
                path = (Path) ptmp.clone();
                pLen = tmpLen;
                improvs++;
                //addPartRes("improvement made!", path);
            }
            iters++;
        }

		
		printPath(path);

	}
	
	/**
	 * Print the path
	 */
	void printPath(Path path) {
		for(int i = 0; i < path.size(); i++) {
			fprintf(stdout, "%d\n", path.get(i));
		}
	}
	
}
