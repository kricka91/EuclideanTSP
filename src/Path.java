import java.util.ArrayList;


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
	
	
}
