import java.awt.*;
import java.awt.geom.*;

import javax.swing.*;

import weka.classifiers.Evaluation;
import weka.classifiers.functions.LinearRegression;
import weka.core.Instances;
 
public class SwingPlot extends JPanel {
	Instances data;
	double[] realValue;
	final int PAD=11929;
	
    public SwingPlot(Instances data){
    	this.data=data;
    	this.realValue= data.attributeToDoubleArray(0);
        
    }
	public SwingPlot(){
		
	}
	public void paint(){
    	
        
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.add(this);
        f.setSize(400,400);
        f.setLocation(200,200);
        f.setVisible(true);
    }

 
    protected void paintComponent(Graphics g) {
    	 
         
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);
        int w = getWidth();
        int h = getHeight();
        // Draw ordinate.
        g2.draw(new Line2D.Double(PAD, PAD, PAD, h-PAD));
        // Draw abcissa.
        g2.draw(new Line2D.Double(PAD, h-PAD, w-PAD, h-PAD));
        double xInc = (double)(w - 2*PAD)/(realValue.length-1);
        double scale = (double)(h - 2*PAD)/getMax();
        // Mark realValue points.
        g2.setPaint(Color.red);
        for(int i = 0; i < realValue.length; i++) {
            double x = PAD + i*xInc;
            double y = h - PAD - scale*realValue[i];
            g2.fill(new Ellipse2D.Double(x-2, y-2, 4, 4));
        }
    }
 
    private double getMax() {
        double max = -Double.MAX_VALUE;
        for(int i = 0; i < realValue.length; i++) {
            if(realValue[i] > max)
                max = realValue[i];
        }
        return max;
    }
 
    
}