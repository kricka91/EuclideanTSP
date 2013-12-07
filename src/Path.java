import java.util.ArrayList;
import java.util.Arrays;


public class Path {

	private ArrayList<Integer> p;
	private int[] inIndex;
	public final int n;
	
	public Path(ArrayList<Integer> path) {
		p = path;
		n = path.size();
		inIndex = new int[n];
		updateInIndex();
	}
	
	public Path(ArrayList<Integer> path, int[] inIndex) {
		p = path;
		this.inIndex = inIndex;
		n = path.size();
	}
	
	private void updateInIndex() {
		for(int i = 0; i < n; i++) {
			int node = p.get(i);
			inIndex[node] = i;
		}
	}
	
	/*
	 * Get the i:th node in the path.
	 */
	public int get(int i) {
		return p.get(i);
	}
	
	/*
	 * Set the i:th node in the path
	 */
	public void set(int i, int val) {
		p.set(i,val);
		inIndex[val] = i;
	}
	
	/*
	 * Get the index in the path of the node nodeIndex.
	 */
	public int inIndex(int nodeIndex) {
		return inIndex[nodeIndex];
	}
	
	/*
	 * Get n
	 */
	public int size() {
		return n;
	}
	
	@Override
	public Object clone() {
		return new Path((ArrayList<Integer>) p.clone(), Arrays.copyOf(inIndex,n));
	}
	
	/*
	 * Only for compatability with Graphical
	 */
	public boolean isEmpty() {
		if(n == 0)
			return true;
		else
			return false;
	}
	
	/*
	 * Returns the previous node according to the path.
	 */
	public int getPrevNode(int node) {
		int i = inIndex[node]-1;
		if(i == -1)
			i = n-1;
		return p.get(i);
	}
	
	/*
	 * Returns the next node according to the path.
	 */
	public int getNextNode(int node) {
		int i = inIndex[node]+1;
		if(i == n)
			i = 0;
		return p.get(i);
	}
	
	public void move(int node, int to) {
		int nodeIndex = inIndex[node];
		int toIndex = inIndex[to];
		if(nodeIndex > toIndex) {
			int s = toIndex+1;
			for(int i = nodeIndex-1; i >= s; i--) {
				set(i+1, p.get(i));
			}
			set(s,node);
		} else { //toIndex > nodeIndex
			for(int i = nodeIndex+1; i <= toIndex; i++) {
				set(i-1,p.get(i));
			}
			set(toIndex,node);
		}
	}
	
	
	public void flipSmart(int from, int to) {
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
	 * Flip all nodes between path indices from and to.
	 * Example 1:
	 * input: p={0,1,2,3,4},from=1,to=4, output= {0,4,3,2,1}
	 * input: p={0,1,2,3,4},from=3,to=1, output= {4,3,2,1,0}
	 */
	public void flip(int from, int to) {
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
	
	private void swap(int i, int j) {
		int tmp = p.get(i);
		set(i,p.get(j));
		set(j, tmp);
	}
	
	/*
	 * Some methods for distances between nodes along the path.
	 * The metric of the distances is number of edges to traverse to get to the other node.
	 * input are nodes!
	 */
	
	//distance of moving forward from from to to.
	public int pathDistForward(int from, int to) {
		if(from == to)
			return 0;
		
		if(inIndex[from] < inIndex[to]) {
			return inIndex[to] - inIndex[from];
		} else {
			return n - (inIndex[from] - inIndex[to]);
		}
	}
	
	public int pathDistBackward(int from, int to) {
		return pathDistForward(to,from);
	}
	
	
	public String toString() {
		return p.toString();
	}
	
	
}
