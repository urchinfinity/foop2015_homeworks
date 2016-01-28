import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

public class Test {
    public static void main(String[] argv) {
        JFrame frm = new MyFrame(90*9+120*2, 120*7);
        frm.setVisible(true);
	}
}

class ImagePanel extends JPanel {

    private BufferedImage image;

    public ImagePanel(int width, int height, int posX, int posY) {
    	try {                
			System.out.println("read image...");	
        	image = ImageIO.read(new File("img/testCard.png"));
       } catch (IOException ex) {
			System.out.println("Failed to read image...");	
       }
	    Dimension size = new Dimension(width, height);
	    // setPreferredSize(size);
	    // setMinimumSize(size);
	    // setMaximumSize(size);
	    setSize(size);
	    setLayout(null);
	    setPosition(posX, posY);
	    setBackground(new Color(0, 0, 0, 0));
    }

    public void setPosition(int posX, int posY) {
	    x = posX;
	    y = posY;
    }

    @Override
    public void paintComponent(Graphics g) {
System.out.println("draw image...");	
        super.paintComponent(g);
        g.drawImage(image, x+100, y+50, this); // see javadoc for more info on the parameters
        g.drawImage(image, x, y, this); // see javadoc for more info on the parameters     
    }
}

class MyFrame extends JFrame {
    public MyFrame(int width, int height) {
		setSize(width, height);
    	setLayout(new BorderLayout());

        ImagePanel imgpanel1 = new ImagePanel(width, height, 0, 0);
        ImagePanel imgpanel2 = new ImagePanel(width, height, 200, 100);

        add(imgpanel1);
    	imgpanel1.setVisible(true);
        add(imgpanel2);
    	imgpanel2.setVisible(true);

    // 	frm.addMouseListener(new MouseAdapter() {
	   //  	@Override
	   //  	public void mousePressed(MouseEvent e) {
	   //  		System.out.printf("x = %d, y = %d\n", e.getX(), e.getY());	
	   //   	}
	  	// });
    	
    	addMouseMotionListener(new MouseMotionListener() {
			public void mouseMoved(MouseEvent e) {
				// System.out.println("mouse moved!");
				// System.out.printf("x = %d, y = %d\n", e.getX(), e.getY());	
			}
			public void mouseDragged(MouseEvent e) {
				System.out.println("mouse dragged!");
				System.out.printf("x = %d, y = %d\n", e.getX(), e.getY());
				imgpanel1.setPosition(e.getX(), e.getY());
				update(getGraphics());
    			setVisible(true);
			}
	  	});

	
	// CountLabel lbl = new CountLabel();
	// this.add(lbl);

	// JButton btn = new IncreaseButton(lbl);
	// this.add(btn);

	// JButton btn2 = new DecreaseButton(lbl);
	// this.add(btn2);

	// FlowLayout layout = new FlowLayout();
	// this.setLayout(layout);

	// setVisible(true);
    }
}

class CountLabel extends JLabel{
    private int count;

    public CountLabel(){
	super("0");
    }

    private void setCount(int newcount){
	count = newcount;
	setText("" + count);
    }

    private void increaseCount(){
	setCount(count+1);
    }
    
    private void decreaseCount(){
	setCount(count-1);
    }

    public void setText(String s){
	try{
	    count = Integer.parseInt(s);
	}
	catch(NumberFormatException e){
	    count = 0;
	}
	super.setText("" + count);
    }

    public ActionListener getIncListener(){
	return new ActionListener(){
	    public void actionPerformed(ActionEvent e){
		(CountLabel.this).increaseCount();
	    }
	};
    }

    public ActionListener getDecListener(){
	return new ActionListener(){
	    public void actionPerformed(ActionEvent e){
		(CountLabel.this).decreaseCount();
	    }
	};
    }
}//class CountLabel


class IncreaseButton extends JButton{
    private CountLabel lbl;

    public IncreaseButton(CountLabel l){
	lbl = l;
	setText("increase");
	addActionListener(lbl.getIncListener());
    }
}

class DecreaseButton extends JButton{
    private CountLabel lbl;

    public DecreaseButton(CountLabel l){
	lbl = l;
	setText("decrease");
	addActionListener(lbl.getDecListener());
    }
}
