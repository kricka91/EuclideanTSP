#ifndef _PATH_H_
#define _PATH_H_

#include <vector>
#include "TSPSolver.h"
#include "Node.h"

using namespace std;

class Path {

public:
	vector<short> p;
	vector<short> inIndex;
	int n;
	
	Path();
	Path(vector<short> path);
	Path(vector<short> path, vector<short> inIndex);
	void updateInIndex();
	int get(int i);
	void set(int i, int val);
	int inIndex(int nodeIndex);
	int size();
	bool isEmpty();
	int getPrevNode(int node);
	int getNextNode(int node);
	void move(int node, int to);
	void flipSmart(int from, int to);
	void flip(int from, int to);
	void swap(int i, int j);
	int pathDistForward(int from, int to);
	int pathDistBackward(int from, int to);
	
	short operator [] (const int i) const;
	

};

#endif

