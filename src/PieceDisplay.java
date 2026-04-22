import java.awt.Color;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

public class PieceDisplay extends Board{ 
//	private final int[][][] SHAPES = {
//		        {{1, 1, 1, 1}}, {{1, 1}, {1, 1}}, {{0, 1, 0}, {1, 1, 1}},
//		        {{0, 1, 1}, {1, 1, 0}}, {{1, 1, 0}, {0, 1, 1}},
//		        {{1, 0, 0}, {1, 1, 1}}, {{0, 0, 1}, {1, 1, 1}}
//	};
//	private final int WIDTH = 4, HEIGHT = 4, TILE_SIZE = 25;
//	private int[][] board = new int[HEIGHT][WIDTH];
//	private Point piecePos = new Point(0,0);
//	private int[][] currentPiece;
//	private int currentType;
//	private BufferedImage img;
	    
	public PieceDisplay() {
		super(4,4,25);
		super.piecePos = new Point(0,0);
	    setBackground(Color.BLACK);
	    //250 x 500
	    setPreferredSize(new Dimension(WIDTH * TILE_SIZE, HEIGHT * TILE_SIZE));
	    try {
	        // Provide the path to your image file
	        File file = new File("src/Face.jpeg");
	        img = ImageIO.read(file);
	        System.out.println("Image loaded successfully: " + img.getWidth() + "x" + img.getHeight());
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
    
    public void refreshPiece(int[][] currentPiece) {
    	this.currentPiece = currentPiece;
    	repaint();
    }
}