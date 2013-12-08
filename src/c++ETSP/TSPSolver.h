#ifndef _TSPSOLVER_H_
#define _TSPSOLVER_H_

#include "Path.h"
#include "Node.h"
#include <vector>

class Path;
class Node;

using namespace std;

Path solve(vector<Node> allNodes);
Path getInitialPath(vector<Node> allNodes);
Path nearestNeighbor(vector<Node> allNodes, int startNode);
int findClosest(Node node, bool used[]);
int findClosest(vector<short> possibilities, Node node, bool used[]);
void f2Opt(Path path, vector<Node> allNodes);
void part2Opt(Path path, vector<Node> nodes);
void swap(Path path, int i, int j);
long getPathLength(Path path, vector<Node> nodes);
void tripleSwap(vector<short> path, int n1, int n2, int n3);
void full1Opt(Path path, vector<Node> nodes);
void f3Opt(Path path, vector<Node> nodes, int runningTime);
bool oneEdge3Opt(Path path, vector<Node> nodes, int edge);
bool try3opt(Path path, vector<Node> nodes, int edge1, int edge2, int edge3);
void addIfNotIn(vector<short> al, int i);

#endif

