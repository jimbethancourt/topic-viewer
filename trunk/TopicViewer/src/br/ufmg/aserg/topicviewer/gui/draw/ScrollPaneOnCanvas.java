package br.ufmg.aserg.topicviewer.gui.draw;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;

public class ScrollPaneOnCanvas extends JFrame {
	class Canvas extends JPanel {
		public Canvas() {
			setSize(getPreferredSize());
			Canvas.this.setBackground(Color.white);
		}

		public Dimension getPreferredSize() {
			return new Dimension(600, 400);
		}

		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2d = (Graphics2D) g;
			Ellipse2D ellipse = new Ellipse2D.Double(0, 0, 100, 100);
			g2d.setColor(Color.cyan);
			g2d.translate(10, 10);
			g2d.draw(ellipse);
			g2d.fill(ellipse);
		}
	}

	public ScrollPaneOnCanvas() {
		super("Swing Application");
		setSize(300, 300);
		getContentPane().add(new JScrollPane(new Canvas()));
	}

	public static void main(String[] args) {
		ScrollPaneOnCanvas canvas = new ScrollPaneOnCanvas();
		canvas.show();
	}
}