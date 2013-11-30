import java.util.ArrayList;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Graphical extends JFrame {
	ArrayList<ArrayList<Integer>> steps;
	int sizea = 600;
	int curStep;
	Node[] nodes;
	Node[] scaledNodes;
	ArrayList<Integer> path;
	int maxCoord;
	JButton nextStep, animate;
	
	public Graphical(Node[] nodes) {
		setTitle("drawing");
		setSize(sizea+80,sizea+90);
		setVisible(true);
		
		JPanel buttonPane = new JPanel();
		add(buttonPane, BorderLayout.PAGE_END);
		
		nextStep = new JButton("Show first step");
		nextStep.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				showNextStep();
			}
		});
		buttonPane.add(nextStep);
		
		animate = new JButton("Animate");
		animate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				animate();
			}
		});
		buttonPane.add(animate);

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		steps = new ArrayList<ArrayList<Integer>>();
		steps.add(new ArrayList<Integer>());

		this.nodes = nodes;
		this.curStep = -1;
		
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
		
		maxCoord = (int)(max+1);
		
		//scale now
		scaledNodes = new Node[nodes.length];
		double scaleFactor = max/sizea;

		for(int i = 0; i < nodes.length; i++) {
			Node n = nodes[i];
			scaledNodes[i] = new Node(n.x/scaleFactor, n.y/scaleFactor, n.n);
			//System.err.println(scaledNodes[i]);
		}
	}

	public void updateContent(ArrayList<Integer> path) {
		steps.add(path);
		this.path = steps.get(0);
		repaint();
	}
	
	public void paint(Graphics g) {
		if (path == null || curStep == -1) {
			return;
		}
		
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, sizea+80, sizea+50);

		g.setColor(Color.BLACK);
		
		//g.fillOval(100, 100,5,5); //-2 is to place it in center
		int offset = 35;
		
		//draw nodes
		for(int i = 0; i < nodes.length; i++) {
			Node n = scaledNodes[i];
			g.drawOval((int)n.x-2+offset, (int)n.y-2+offset, 4, 4); //-2 is to place it in center
		}

		if (!path.isEmpty()) {
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
	
	/**
	 * Doesn't work yet...
	 */
	public void animate() {
		curStep = -1;
		for (int i = 0;i<steps.size()-1;i++) {
			showNextStep();
			Main.sleep(1000);
		}
		showNextStep();
	}
	
	public void showNextStep() {
		curStep = (curStep+1) % steps.size();
		if (curStep == steps.size()-1) {
			nextStep.setText("Show first step.");
		} else {
			nextStep.setText("Show next step.");
		}
		path = steps.get(curStep);
		repaint();
	}
}
