import javax.swing.*;
import javax.swing.filechooser.FileFilter;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class TestViewer {
	
	final static int WINDOW_WIDTH = 800;
	final static int WINDOW_HEIGHT = 900;
	
	public static void main(String[] args) throws FileNotFoundException {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.addChoosableFileFilter(new FileFilter() {
			public String getDescription() {
				return "Object Files (*.obj)";
			}
			 
			public boolean accept(File f) {
				if (f.isDirectory()) {
					return true;
				} else {
					return f.getName().toLowerCase().endsWith(".obj");
				}
			}
		});
	        

		int result = fileChooser.showOpenDialog(null);
		int lastR = 100;
		int lastG = 100;
		int lastB = 100;
		if (result == JFileChooser.APPROVE_OPTION){
			File chosenFile = fileChooser.getSelectedFile();
			System.out.println(chosenFile.getName());
		
			Scanner fileReader = new Scanner(chosenFile);
			ArrayList<Vertex> vertices = new ArrayList<Vertex>();
			TriObject file_TriObject = new TriObject();
			while (fileReader.hasNextLine()){
        		String line = fileReader.nextLine();
        		if (line.substring(0, 1).equals("v")){
        			//is vertex
        			String[] coor = line.split(" ");
        			
        			vertices.add(new Vertex(Double.parseDouble(coor[1]),Double.parseDouble(coor[2]),Double.parseDouble(coor[3]), 1));
        		}else if (line.substring(0, 1).equals("f")){
        			//is face
        			String[] verts = line.split(" ");
        			if (lastR < 255 && lastG < 255 && lastB < 255){
	        			if (lastR > lastG){
	        				lastG++;
	        			}else if(lastG > lastB){
	        				lastB++;
	        			}else{
	        				lastR++;
	        			}
        			}
        			file_TriObject.addTriangle(new Triangle(
        					vertices.get(Integer.parseInt(verts[1])-1),
        					vertices.get(Integer.parseInt(verts[2])-1),
        					vertices.get(Integer.parseInt(verts[3])-1),
        					new Color(lastR,lastG,lastB)
        			));
        		}
			}
			
			JFrame frame = new JFrame();
			Container pane = frame.getContentPane();
			pane.setLayout(new BorderLayout());
			
			frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
			
	        JSlider headingSlider = new JSlider(0, 360, 180);
	        pane.add(headingSlider, BorderLayout.SOUTH);
	
	        JSlider pitchSlider = new JSlider(SwingConstants.VERTICAL, -90, 90, 0);
	        pane.add(pitchSlider, BorderLayout.EAST);
	        
	        JSlider rollSlider = new JSlider(SwingConstants.VERTICAL, -90, 90, 0);
	        pane.add(rollSlider, BorderLayout.WEST);
			
			//TriObject tetrahedron = basicObject_tetrahedron_inflate();
			
	        
	        
			JPanel render = new Render(headingSlider, pitchSlider, rollSlider, file_TriObject);
			/*headingSlider.addChangeListener(e -> render.repaint());
			pitchSlider.addChangeListener(e -> render.repaint());
			rollSlider.addChangeListener(e -> render.repaint());*/
			
			pane.add(render, BorderLayout.CENTER);
			
			frame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
			
	        frame.setVisible(true);
		}else{
			System.out.println("Please choose a file");
		}
	}
	
	
	
	/*public static TriObject basicObject_tetrahedron(){
		TriObject tetrahedron = new TriObject();
		tetrahedron.addTriangle(new Triangle(
				new Vertex(100, 100, 100),
                new Vertex(-100, -100, 100),
                new Vertex(-100, 100, -100),
                Color.WHITE)
		);
		
		tetrahedron.addTriangle(new Triangle(
				new Vertex(100, 100, 100),
                new Vertex(-100, -100, 100),
                new Vertex(100, -100, -100),
                Color.RED)
		);
		
		tetrahedron.addTriangle(new Triangle(
				new Vertex(-300, 300, -300),
                new Vertex(100, -100, -100),
                new Vertex(100, 100, 100),
                Color.GREEN)
		);
		
		tetrahedron.addTriangle(new Triangle(
				new Vertex(-100, 100, -100),
                new Vertex(100, -100, -100),
                new Vertex(-100, -100, 100),
                Color.BLUE)
		);
		return tetrahedron;
	}*/
	
	/*public static TriObject basicObject_tetrahedron_inflate(){
		return basicObject_tetrahedron().inflated_x(0);
	}*/
}
