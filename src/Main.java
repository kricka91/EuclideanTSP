import java.util.ArrayList;
import java.util.Scanner;


public class Main {
	
	
	public static void main(String[] args) {
		
		
		
		Scanner sc = new Scanner(System.in);
		int n = sc.nextInt(); //n = number of nodes
		double minx = Integer.MIN_VALUE;
		double miny = Integer.MIN_VALUE;
		
		Node[] nodes = new Node[n];
		for(int i = 0; i < n; i++) {
			double x = sc.nextDouble();
			double y = sc.nextDouble();
			if(x > minx) {
				minx = x;
			}
			if(y > miny) {
				miny = y;
			}
			nodes[i] = new Node(x,y,n);
		}
		
		// calculate all distances.
		for(int i = 0; i < n; i++) {
			nodes[i].calcDistances(nodes);
		}
		
	
	}
	
}
