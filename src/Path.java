import java.util.ArrayList;
import java.util.Arrays;


public class Path {

	private ArrayList<Integer> p;
	private int[] inIndex;
	public final int n;
	
	public Path(ArrayList<Integer> path) {
		p = path;
		n = path.size();
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
	
	public String toString() {
		return p.toString();
	}
	
	
}
