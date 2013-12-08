import java.util.Comparator;

public class Pair<K extends Comparable, V extends Comparable > implements Comparable {

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
	
	public Pair<V,K> getFlipped() {
		return new Pair<V,K>(value, key);
	}
	
	public String toString() {
		return "(" + key.toString() + ", " + value.toString() + ")";
	}
	
	public boolean equals(Pair other) {
		return (key.equals(other.key) && value.equals(other.value));
	}
	
	public int compareTo(Object other) {
		return key.compareTo(((Pair<K,V>)other).key);
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
	
	public class KeyInvComparator implements Comparator<Pair<K,V>> {
   		public int compare(Pair p1, Pair p2) {
    	    return p2.key.compareTo(p1.key);
    	}
	}

	public class ValueInvComparator implements Comparator<Pair<K,V>> {
   		public int compare(Pair p1, Pair p2) {
    	    return p2.value.compareTo(p1.value);
    	}
	}

}

