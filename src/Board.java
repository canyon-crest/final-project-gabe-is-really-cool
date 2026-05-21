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
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

public abstract class Board extends JPanel{
	protected final int WIDTH;
	protected final int HEIGHT;
	protected final int TILE_SIZE;
	protected int[][] board;
	protected Point piecePos;
	protected int[][] currentPiece;
	protected int currentType;
	protected BufferedImage img;
    protected JFrame gameFrame, endFrame;
    
    //for live updates to scoreboard
    private JLabel scoreLabelUI; // Stores UI component reference pointer
    private JLabel finalScoreLabel;
    private JLabel levelLabel;
    private JLabel rowsLabel;
    public void setScoreLabel(JLabel label) {
        this.scoreLabelUI = label;
    }
    public void setLevelLabel(JLabel label) {
   	 this.levelLabel = label;
    }
    public void setRowsLabel(JLabel label) {
   	 this.rowsLabel = label;
    }
    public void setFinalScoreLabel(JLabel label) {
   	 this.finalScoreLabel = label;
    }
	public Board(int WIDTH, int HEIGHT, int TILE_SIZE) {
		this.WIDTH = WIDTH;
		this.HEIGHT = HEIGHT;
		this.TILE_SIZE = TILE_SIZE;
		board = new int[WIDTH][HEIGHT];
	}
	
	  public void addFrames(JFrame gameFrame, JFrame endFrame) {
	    	this.gameFrame = gameFrame;
	    	this.endFrame = endFrame;
	    }
    //default tetris shapes
	  protected final int[][][] SHAPES = {
            {{1, 1, 1, 1}}, {{1, 1}, {1, 1}}, {{0, 1, 0}, {1, 1, 1}},
            {{0, 1, 1}, {1, 1, 0}}, {{1, 1, 0}, {0, 1, 1}},
            {{1, 0, 0}, {1, 1, 1}}, {{0, 0, 1}, {1, 1, 1}}
        };
	  //overriding JFrame paintcomponent to paint tetris pieces
    @Override
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
                    if (currentPiece[r][c] != 0) drawTile(g, piecePos.x + c, piecePos.y + r);
        }
    }
    //draw one glorious square of Mr. Hare's face
    private void drawTile(Graphics g, int x, int y) {
        g.drawImage(img ,x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE - 1, TILE_SIZE - 1, null);
    }
}