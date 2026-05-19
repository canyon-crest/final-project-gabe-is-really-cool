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
import javax.swing.JLabel;

class HareBoard extends Board {
//    private final int WIDTH = 10, HEIGHT = 20, TILE_SIZE = 25;
//    private int[][] board = new int[HEIGHT][WIDTH];
	private Timer timer;
//    private Point piecePos;
//    private int[][] currentPiece;
	private int[][] nextPiece;
	private int currentType;
//    private BufferedImage img;
    private PieceDisplay thisPieceDisplay;
    private int score = 0;
    private JLabel scoreLabelUI; // Stores UI component reference pointer

 // Add this setup method 
 public void setScoreLabel(JLabel label) {
     this.scoreLabelUI = label;
 }


    // Tetromino definitions
    private final int[][][] SHAPES = {
    		{{1,0,1},{1,0,1},{1,1,1},{1,0,1},{1,0,1}},
    		{{0,1,0},{1,0,1},{1,1,1},{1,0,1},{1,0,1}},
    		{{1,1,0},{1,0,1},{1,1,0},{1,0,1},{1,0,1}},
    		{{1,1,1},{1,0,0},{1,1,1},{1,0,0},{1,1,1}}

    };
    public HareBoard() {
    	super(20, 20, 25);
        setBackground(Color.BLACK);
        currentType = (int) (Math.random() * SHAPES.length);
        nextPiece = SHAPES[currentType];
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

    public void startGame() {
        board = new int[HEIGHT][WIDTH];
        spawnPiece();
        if (timer != null) timer.stop();
        timer = new Timer(500, e -> { if (!movePiece(0, 1)) freeze(); repaint(); });
        timer.start();
    }
    
    public void addPieceDisplay(PieceDisplay pieceDisplay) {
    	thisPieceDisplay = pieceDisplay;
    }

    private void spawnPiece() {
    	currentPiece = nextPiece;
        currentType = (int) (Math.random() * SHAPES.length);
        nextPiece = SHAPES[currentType];
        thisPieceDisplay.refreshPiece(nextPiece);
        piecePos = new Point(WIDTH / 2 - currentPiece[0].length / 2, 0);
        if (intersects(piecePos.x, piecePos.y, currentPiece)) {
            timer.stop();
            gameFrame.setVisible(false);
            endFrame.setVisible(true);
        }
    }

    public boolean movePiece(int dx, int dy) {
        if (!intersects(piecePos.x + dx, piecePos.y + dy, currentPiece)) {
            piecePos.translate(dx, dy);
            repaint();
            return true;
        }
        return false;
    }

    public void rotate() {
        int[][] rotated = new int[currentPiece[0].length][currentPiece.length];
        for (int r = 0; r < currentPiece.length; r++)
            for (int c = 0; c < currentPiece[0].length; c++)
                rotated[c][currentPiece.length - 1 - r] = currentPiece[r][c];
        if (!intersects(piecePos.x, piecePos.y, rotated)) {
            currentPiece = rotated;
            repaint();
        }
    }

    private void freeze() {
        for (int r = 0; r < currentPiece.length; r++)
            for (int c = 0; c < currentPiece[0].length; c++)
                if (currentPiece[r][c] != 0) board[piecePos.y + r][piecePos.x + c] = currentType + 1;
        clearLines();
        spawnPiece();
    }

    private void clearLines() {
    	int linesCleared = 0;
        for (int r = HEIGHT - 1; r >= 0; r--) {
            boolean full = true;
            for (int c = 0; c < WIDTH; c++) if (board[r][c] == 0) full = false;
            if (full) {
                for (int i = r; i > 0; i--) board[i] = board[i - 1].clone();
                board[0] = new int[WIDTH];
                r++;
                linesCleared ++;
            }
        }
        if(linesCleared>0) {
        	score += linesCleared*100;
        	if (scoreLabelUI != null) {
                scoreLabelUI.setText("SCORE: " + score);
            }
        }
    }
 

    private boolean intersects(int nx, int ny, int[][] shape) {
        for (int r = 0; r < shape.length; r++)
            for (int c = 0; c < shape[0].length; c++)
                if (shape[r][c] != 0) {
                    if (nx + c < 0 || nx + c >= WIDTH || ny + r >= HEIGHT || (ny + r >= 0 && board[ny + r][nx + c] != 0))
                        return true;
                }
        return false;
    }

    public KeyAdapter getKeyAdapter() {
        return new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_A -> movePiece(-1, 0);
                    case KeyEvent.VK_D -> movePiece(1, 0);
                    case KeyEvent.VK_S -> movePiece(0, 1);
                    case KeyEvent.VK_W -> rotate();
                    case KeyEvent.VK_SPACE -> { while (movePiece(0, 1)); freeze(); }
                }
            }
        };
    }
 
}