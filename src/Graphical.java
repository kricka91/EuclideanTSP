import java.util.ArrayList;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Graphical extends JFrame {
	ArrayList<ArrayList<Integer>> steps;
	ArrayList<String> stepNames;
	int sizea = 600;
	int curStep;
	Node[] nodes;
	Node[] scaledNodes;
	ArrayList<Integer> path;
	String curStepName;
	int maxCoord;
	JButton nextStep, animate;
	
	public Graphical(Node[] nodes) {
		setTitle("TSP path");
		setSize(sizea+80,sizea+90);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
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
			scaledNodes[i] = new Node(n.x/scaleFactor, n.y/scaleFactor, n.n,i);
			//System.err.println(scaledNodes[i]);
		}
	}

	public void updateContent(ArrayList<ArrayList<Integer>> paths, ArrayList<String> names) {
		this.steps = paths;
		this.path = steps.get(steps.size()-1);
		this.stepNames = names;
		this.curStepName = stepNames.get(stepNames.size()-1);
		repaint();
		
		System.err.println("Size of parameters: " + paths.size() + ", and " + names.size());
		
	}
	
	public void paint(Graphics g) {
		if (path == null || curStep == -1) {
			return;
		}
		
		boolean writeNumbers = (nodes.length > 100) ? false :  true;
		
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, sizea+80, sizea+50);

		g.setColor(Color.BLACK);
		g.drawString(curStepName, 4, 40);
		
		if (!path.isEmpty()) {
			long length = new TSPSolver().getPathLength(path, nodes);
			g.drawString("Path length: " + length, 4, 60);
		}
		
		//g.fillOval(100, 100,5,5); //-2 is to place it in center
		int offset = 35;
		
		//draw nodes
		for(int i = 0; i < nodes.length; i++) {
			Node n = scaledNodes[i];
			g.drawOval((int)n.x-2+offset, (sizea-(int)n.y)-2+offset, 4, 4); //-2 is to place it in center
			if (writeNumbers) {
				g.drawString("" + n.index, (int)n.x+offset-8, (sizea-(int)n.y)+offset-8);
			}
		}

		if (!path.isEmpty()) {
			//draw path
			g.setColor(Color.red);
			for(int i = 1; i < path.size(); i++) {
				Node p = scaledNodes[path.get(i-1)];
				Node n = scaledNodes[path.get(i)];
			
				g.drawLine((int)p.x+offset, (sizea-(int)p.y)+offset, (int)n.x+offset, (sizea-(int)n.y)+offset);
			}

			Node p = scaledNodes[path.get(0)];
			Node n = scaledNodes[path.get(path.size()-1)];
			g.drawLine((int)p.x+offset, (sizea-(int)p.y)+offset, (int)n.x+offset, (sizea-(int)n.y)+offset);
			
			g.setColor(Color.GREEN);
			g.drawOval((int)p.x-5+offset, (sizea-(int)p.y)-5+offset, 10, 10);
			g.drawOval((int)p.x-5+offset, (sizea-(int)p.y)-5+offset, 11, 11);
			g.drawOval((int)p.x-6+offset, (sizea-(int)p.y)-6+offset, 12, 12);
			
			g.setColor(Color.BLUE);
			p = scaledNodes[path.get(1)];
			g.drawOval((int)p.x-5+offset, (sizea-(int)p.y)-5+offset, 10, 10);
			g.drawOval((int)p.x-5+offset, (sizea-(int)p.y)-5+offset, 11, 11);
			g.drawOval((int)p.x-6+offset, (sizea-(int)p.y)-6+offset, 12, 12);
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
		curStepName = stepNames.get(curStep);
		repaint();
	}
}

