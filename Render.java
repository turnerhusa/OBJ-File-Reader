import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.event.*;

import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.*;

public class Render extends JPanel implements KeyListener, ActionListener{

	private JSlider headingSlider;
	private JSlider pitchSlider;
	private JSlider rollSlider;
	private TriObject file_TriObject;
	
	private Timer timer = new Timer(10, this);
	
	Matrix4 translate_x = new Matrix4(new double[] {
            1, 0, 0, 1,
            0, 1, 0, 0,
            0, 0, 1, 0,
            0, 0, 0, 1
        });
	
	Matrix4 translate_y = new Matrix4(new double[] {
            1, 0, 0, 0,
            0, 1, 0, 1,
            0, 0, 1, 0,
            0, 0, 0, 1
        });
	
	Matrix4 translate_z = new Matrix4(new double[] {
            1, 0, 0, 0,
            0, 1, 0, 0,
            0, 0, 1, 1,
            0, 0, 0, 1
        });
	
	private boolean[] keys = {false,false,false,false,false,false}; // 0w, 1a, 2s, 3d, 4q, 5e
	
	public Render(JSlider headingSlider, JSlider pitchSlider, JSlider rollSlider, TriObject file_TriObject){
		super();
		
		this.timer.start();
		
		this.headingSlider = headingSlider;
		this.pitchSlider = pitchSlider;
		this.rollSlider = rollSlider;
		this.file_TriObject = file_TriObject;
	}
	
	public void paintComponent(Graphics g){
		
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(new Color(0,0,0));
		g2.fillRect(0, 0, getWidth(), getHeight());
		
		double heading = Math.toRadians(headingSlider.getValue());
		Matrix4 headingTransform = new Matrix4(new double[] {
				Math.cos(heading), 0, -Math.sin(heading), 0,
                0, 1, 0, 0,
                Math.sin(heading), 0, Math.cos(heading), 0,
                0, 0, 0, 1
		    });
		double pitch = Math.toRadians(pitchSlider.getValue());
		Matrix4 pitchTransform = new Matrix4(new double[] {
				1, 0, 0, 0,
                0, Math.cos(pitch), Math.sin(pitch), 0,
                0, -Math.sin(pitch), Math.cos(pitch), 0,
                0, 0, 0, 1
		    });
		double roll = Math.toRadians(0);
		Matrix4 rollTransform = new Matrix4(new double[] {
				Math.cos(roll), -Math.sin(roll), 0, 0,
                Math.sin(roll), Math.cos(roll), 0, 0,
                0, 0, 1, 0,
                0, 0, 0, 1
		    });
		
		
		Matrix4 transform = headingTransform.multiply(pitchTransform).multiply(rollTransform);
		displayObject(g2, transform, file_TriObject);
		
		g2.setColor(Color.WHITE);
		g2.drawString("Triangles:"+file_TriObject.getTriangles().size()+" Verteces:"+(file_TriObject.getTriangles().size() * 3), 10, 10);
	}
	
	public void displayObject(Graphics2D g2, Matrix4 transform, TriObject obj){
		
		BufferedImage img = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
		double[] zBuffer = new double[img.getWidth() * img.getHeight()];
		for (int q = 0; q < zBuffer.length; q++) {
		    zBuffer[q] = Double.NEGATIVE_INFINITY;
		}
		
		for (Triangle t : obj.getTriangles()){
			/*t.v1 = translate_z.translate(t.v1,-10);
		    t.v2 = translate_z.translate(t.v2,-10);
		    t.v3 = translate_z.translate(t.v3,-10);*/
		    
		    if (keys[0] && !keys[2]){
		    	t.v1 = translate_z.translate(t.v1,10);
		    	t.v2 = translate_z.translate(t.v2,10);
		    	t.v3 = translate_z.translate(t.v3,10);
		    }else if (!keys[0] && keys[2]){
		    	t.v1 = translate_z.translate(t.v1,-10);
		    	t.v2 = translate_z.translate(t.v2,-10);
		    	t.v3 = translate_z.translate(t.v3,-10);
		    }
		}
		
		for (Triangle t : obj.getTriangles()){
			Vertex v1 = transform.transform(t.v1);
		    Vertex v2 = transform.transform(t.v2);
		    Vertex v3 = transform.transform(t.v3);
		    
		    
		    
		    v1.x += getWidth() / 2;
		    v1.y += getHeight() / 2;
		    v2.x += getWidth() / 2;
		    v2.y += getHeight() / 2;
		    v3.x += getWidth() / 2;
		    v3.y += getHeight() / 2;
		    
		    //calculate normals
		    
		    Vertex ab = new Vertex(v2.x - v1.x, v2.y - v1.y, v2.z - v1.z, v2.w - v1.w);
            Vertex ac = new Vertex(v3.x - v1.x, v3.y - v1.y, v3.z - v1.z, v3.w - v1.w);
			Vertex norm = new Vertex(
				ab.y * ac.z - ab.z * ac.y,
				ab.z * ac.x - ab.x * ac.z,
				ab.x * ac.y - ab.y * ac.x,
				1
			);
			double normalLength = Math.sqrt(norm.x * norm.x + norm.y * norm.y + norm.z * norm.z);
			norm.x /= normalLength;
			norm.y /= normalLength;
			norm.z /= normalLength;
			
			double angleCos = Math.abs(norm.z);
		    
		    int minX = (int) Math.max(0, Math.ceil(Math.min(v1.x, Math.min(v2.x, v3.x))));
		    int maxX = (int) Math.min(img.getWidth() - 1, Math.floor(Math.max(v1.x, Math.max(v2.x, v3.x))));
		    int minY = (int) Math.max(0, Math.ceil(Math.min(v1.y, Math.min(v2.y, v3.y))));
		    int maxY = (int) Math.min(img.getHeight() - 1, Math.floor(Math.max(v1.y, Math.max(v2.y, v3.y))));
			
			double triArea = (v1.y - v3.y) * (v2.x - v3.x) + (v2.y - v3.y) * (v3.x - v1.x);
			
			for (int y = minY; y <= maxY; y++) {
				for (int x = minX; x <= maxX; x++) {
					double b1 = ((y - v3.y) * (v2.x - v3.x) + (v2.y - v3.y) * (v3.x - x)) / triArea;
					double b2 = ((y - v1.y) * (v3.x - v1.x) + (v3.y - v1.y) * (v1.x - x)) / triArea;
					double b3 = ((y - v2.y) * (v1.x - v2.x) + (v1.y - v2.y) * (v2.x - x)) / triArea;
					if (b1 >= 0 && b1 <= 1 && b2 >= 0 && b2 <= 1 && b3 >= 0 && b3 <= 1) {
						//img.setRGB(x, y, t.color.getRGB());
						double depth = b1 * v1.z + b2 * v2.z + b3 * v3.z;
					    int zIndex = y * img.getWidth() + x;
					    if (zBuffer[zIndex] < depth) {
					        img.setRGB(x, y, getShade(t.color, angleCos).getRGB());
					        zBuffer[zIndex] = depth;
					    }
					}
				}
			}
		}
		
		g2.drawImage(img, 0, 0, null);
	}
	
	private Color getShade(Color c, double shade){
		double redLinear = Math.pow(c.getRed(), 2.4) * shade;
		double greenLinear = Math.pow(c.getGreen(), 2.4) * shade;
		double blueLinear = Math.pow(c.getBlue(), 2.4) * shade;
		
		int red = (int) Math.pow(redLinear, 1/2.4);
		int green = (int) Math.pow(greenLinear, 1/2.4);
		int blue = (int) Math.pow(blueLinear, 1/2.4);
		
		return new Color(red, green, blue);
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		char keyChar = e.getKeyChar();
		
		System.out.println("Key Press");
		switch(keyChar){
		case 'w':
			keys[0] = true;
		case 'a':
			keys[1] = true;
		case 's':
			keys[2] = true;
		case 'd':
			keys[3] = true;
		case 'q':
			keys[4] = true;
		case 'e':
			keys[5] = true;
		default:
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		char keyChar = e.getKeyChar();
		switch(keyChar){
		case 'w':
			keys[0] = false;
		case 'a':
			keys[1] = false;
		case 's':
			keys[2] = false;
		case 'd':
			keys[3] = false;
		case 'q':
			keys[4] = false;
		case 'e':
			keys[5] = false;
		default:
			break;
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==timer){
			repaint();
		}
		
	}
}
