import java.util.Comparator;

public class Pair<K extends Comparable, V extends Comparable > {

	public final K key;
	public final V value;
	
	public Pair() {
		key = null;
		value = null;
	}
	
	public Pair(K k, V v) {
		key = k;
		value = v;
	}
	
	public String toString() {
		return "(" + key.toString() + ", " + value.toString() + ")";
	}
	
	public class KeyComparator implements Comparator<Pair<K,V>> {
   		public int compare(Pair p1, Pair p2) {
    	    return p1.key.compareTo(p2.key);
    	}
	}

	public class ValueComparator implements Comparator<Pair<K,V>> {
   		public int compare(Pair p1, Pair p2) {
    	    return p1.value.compareTo(p2.value);
    	}
	}

}

