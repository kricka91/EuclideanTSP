import java.util.ArrayList;
import java.awt.*;

import javax.swing.*;

public class Graphical {
	private JFrame frame;
	
	public Graphical(Node[] nodes, ArrayList<Integer> path) {
		frame = new Ganvas(nodes,path);
	}
	
	private class Ganvas extends JFrame {
		int sizea = 600;
		Node[] nodes;
		Node[] scaledNodes;
		ArrayList<Integer> path;
		int maxCoord;
		
		public Ganvas(Node[] nodes, ArrayList<Integer> path) {
			setTitle("drawing");
			setSize(sizea+50,sizea+50);
			setVisible(true);
			this.nodes = nodes;
			this.path=path;
			
			//calculate max coordinate
			double max = 0;
			for(int i = 0; i < nodes.length; i++) {
				if(nodes[i].x > max) {
					max = nodes[i].x;
				}
				if(nodes[i].y > max) {
					max = nodes[i].y;
				}
			}
			
			maxCoord = (int) (max+1);
			
			//scale now
			scaledNodes = new Node[nodes.length];
			double scaleFactor = max/sizea;
			
			
			for(int i = 0; i < nodes.length; i++) {
				Node n = nodes[i];
				scaledNodes[i] = new Node(n.x/scaleFactor, n.y/scaleFactor, n.n);
				//System.err.println(scaledNodes[i]);
			}
			
			
			
			
			setDefaultCloseOperation(EXIT_ON_CLOSE);
		}
		
		public void paint(Graphics g) {
			g.setColor(Color.BLACK);
			
			//g.fillOval(100, 100,5,5); //-2 is to place it in center
			int offset = 35;
			
			//draw nodes
			for(int i = 0; i < nodes.length; i++) {
				Node n = scaledNodes[i];
				g.drawOval((int)n.x-2+offset, (int)n.y-2+offset, 4, 4); //-2 is to place it in center
			}
			//draw path
			g.setColor(Color.red);
			
			for(int i = 1; i < path.size(); i++) {
				Node p = scaledNodes[path.get(i-1)];
				Node n = scaledNodes[path.get(i)];
				
				g.drawLine((int)p.x+offset, (int)p.y+offset, (int)n.x+offset, (int)n.y+offset);
			}
			Node p = scaledNodes[path.get(0)];
			Node n = scaledNodes[path.get(path.size()-1)];
			g.drawLine((int)p.x+offset, (int)p.y+offset, (int)n.x+offset, (int)n.y+offset);
			
			g.setColor(Color.GREEN);
			g.drawOval((int)p.x-5+offset, (int)p.y-5+offset, 10, 10);
			g.drawOval((int)p.x-5+offset, (int)p.y-5+offset, 11, 11);
			g.drawOval((int)p.x-6+offset, (int)p.y-6+offset, 12, 12);
		}
	}
	

}
