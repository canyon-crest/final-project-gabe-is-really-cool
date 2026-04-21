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

public class PieceDisplay extends JPanel{ 
	private final int[][][] SHAPES = {
		        {{1, 1, 1, 1}}, {{1, 1}, {1, 1}}, {{0, 1, 0}, {1, 1, 1}},
		        {{0, 1, 1}, {1, 1, 0}}, {{1, 1, 0}, {0, 1, 1}},
		        {{1, 0, 0}, {1, 1, 1}}, {{0, 0, 1}, {1, 1, 1}}
	};
	private final int WIDTH = 4, HEIGHT = 4, TILE_SIZE = 25;
	private int[][] board = new int[HEIGHT][WIDTH];
	private Timer timer;
	private Point piecePos;
	private int[][] currentPiece;
	private int currentType;
	private BufferedImage img;
	    
	public PieceDisplay() {
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
	    
	    protected void paintComponent(Graphics g) {
	        super.paintComponent(g);
	        // Draw static board
	        for (int r = 0; r < HEIGHT; r++)
	            for (int c = 0; c < WIDTH; c++)
	                if (board[r][c] != 0) drawTile(g, c, r);
	        // Draw active piece
	        if (currentPiece != null) {
	            for (int r = 0; r < currentPiece.length; r++)
	                for (int c = 0; c < currentPiece[0].length; c++)
	                    if (currentPiece[r][c] != 0) drawTile(g, c, r);
	        }
	    }

	    private void drawTile(Graphics g, int x, int y) {
	        g.drawImage(img ,x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE - 1, TILE_SIZE - 1, null);
	    }
}