package br.ufmg.aserg.topicviewer.gui.draw;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.ToolTipManager;
import javax.swing.border.BevelBorder;

class GraphicsExample extends JFrame {

	private static final long serialVersionUID = -3614907914802421075L;
	private int tool = 1;
    int currentX, currentY, oldX, oldY;

    public GraphicsExample() {
        initComponents();
    }

    private void initComponents() {
        // we want a custom Panel2, not a generic JPanel!
        jPanel2 = new TestPanel();

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        jPanel2.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent evt) {
                jPanel2MousePressed(evt);
            }
            public void mouseReleased(MouseEvent evt) {
                jPanel2MouseReleased(evt);
            }
        });
        jPanel2.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent evt) {
                jPanel2MouseDragged(evt);
            }
        });

        // add the component to the frame to see it!
        this.setContentPane(jPanel2);
        // be nice to testers..
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
    }// </editor-fold>

    private void jPanel2MouseDragged(MouseEvent evt) {
        if (tool == 1) {
            currentX = evt.getX();
            currentY = evt.getY();
            oldX = currentX;
            oldY = currentY;
            System.out.println(currentX + " " + currentY);
            System.out.println("PEN!!!!");
        }
    }

    private void jPanel2MousePressed(MouseEvent evt) {
        oldX = evt.getX();
        oldY = evt.getY();
        System.out.println(oldX + " " + oldY);
    }


    //mouse released//
    private void jPanel2MouseReleased(MouseEvent evt) {
        if (tool == 2) {	
            currentX = evt.getX();
            currentY = evt.getY();
            System.out.println("line!!!! from" + oldX + "to" + currentX);
        }
    }

    //set ui visible//
    public static void main(String args[]) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                new GraphicsExample().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify
    private JPanel jPanel2;
    // End of variables declaration

    // This class name is very confusing, since it is also used as the
    // name of an attribute!
    //class jPanel2 extends JPanel {
    class Panel2 extends JPanel {

        Panel2() {
            // set a preferred size for the custom panel.
            setPreferredSize(new Dimension(420,420));
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);

            g.drawString("BLAH", 20, 20);
            g.drawRect(200, 200, 200, 200);
            g.fillRect(200, 200, 200, 200);
            g.fillRect(100, 100, 10, 10);
        }
    }
    
    public class TestPanel extends JPanel {

    	private Rectangle rect=new Rectangle();
    	
    	public TestPanel() {
    		rect.setBounds(20,30,100,100);
    		addMouseMotionListener(new MouseMotionListener() {
    			
    			@Override
    			public void mouseMoved(MouseEvent e) {
    				if(rect.contains(e.getPoint()))
    					setToolTipText("Inside rect");
    				else
    					setToolTipText("Outside rect");
    				
    				ToolTipManager.sharedInstance().mouseMoved(e);
    			}
    			
    			@Override
    			public void mouseDragged(MouseEvent e) {
    				// TODO Auto-generated method stub
    				
    			}
    		});
    	}
    	
    	@Override
    	protected void paintComponent(Graphics g) {
    		super.paintComponent(g);
    		Graphics2D g2=(Graphics2D) g;
    		g2.draw(rect);
    	}
    }
}