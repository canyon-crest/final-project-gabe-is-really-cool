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
import javax.swing.JFrame;
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
    private int level = 1;
    private int totalLines = 0;
    private int blub = 500;
    private Timer dvdTimer;
    private int moveX = 3; // Horizontal speed
    private int moveY = 3; // Vertical speed
    // Tetromino definitions
    private final int[][][] SHAPES = {
    	    // 'H' - Standard
    	    {{1, 0, 1}, 
    	     {1, 1, 1}, 
    	     {1, 0, 1}},
    	     
    	    // 'A' - Pointed
    	    {{0, 1, 0}, 
    	     {1, 1, 1}, 
    	     {1, 0, 1}},
    	     
    	    // 'R' - Blocky
    	    {{1, 1, 0}, 
    	     {1, 1, 0}, 
    	     {1, 0, 1}},
    	    
    	    {
    	     {0, 1, 0}, 
    	     {0, 1, 0}, 
    	     {1, 1, 1}},
    	     
    	    // 'E' - Standard
    	    {{1, 1, 1}, 
    	     {1, 1, 0}, 
    	     {1, 1, 1}},
    	    
    	    
    	 // '67' - Block
    	    {{1, 1, 1, 0, 1, 1, 1},
    	     {1, 0, 0, 0, 0, 0, 1},
    	     {1, 1, 1, 0, 0, 0, 1},
    	     {1, 0, 1, 0, 0, 0, 1},
    	     {1, 1, 1, 0, 0, 0, 1}}
    	};
    public HareBoard() {
    	super(15, 20, 25);
        setBackground(Color.BLACK);
        currentType = (int) (Math.random() * SHAPES.length);
        nextPiece = SHAPES[currentType];
        //250 x 500
        setPreferredSize(new Dimension(WIDTH * TILE_SIZE, HEIGHT * TILE_SIZE));
        try {
            // Provide the path to your image file
            File file = new File("src/Face.jpeg");
            img = ImageIO.read(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startGame() {
        board = new int[HEIGHT][WIDTH];
        spawnPiece();
        if (timer != null) timer.stop();
        //repeatedly moves piece, freezes game, and repaints the board
        timer = new Timer(blub, e -> { if (!movePiece(0, 1)) freeze(); repaint(); });
        timer.start();
        startDvdBounce(gameFrame);
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
            finalScoreLabel.setText("Score:" + score);
            endFrame.setVisible(true);
            SoundPlayer.playSoundEffect("./src/gameover.wav");
            SoundPlayer.stopMusic();
        }
    }

    public boolean movePiece(int dx, int dy) {
        SoundPlayer.playSoundEffect("./src/boops.wav");
        if (!intersects(piecePos.x + dx, piecePos.y + dy, currentPiece)) {
            piecePos.translate(dx, dy);
            repaint();
            return true;
        }
        return false;
    }

    public void rotate() {
        SoundPlayer.playSoundEffect("./src/boops.wav");
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
    	int rows = 0;
        for (int r = HEIGHT - 1; r >= 0; r--) {
            boolean full = true;
            for (int c = 0; c < WIDTH; c++) if (board[r][c] == 0) full = false;
            if (full) {
                for (int i = r; i > 0; i--) board[i] = board[i - 1].clone();
                board[0] = new int[WIDTH];
                r++;
                rows++;
            }
        }
        if(rows == 1) {
            SoundPlayer.playSoundEffect("./src/single.wav");
        }
        else if (rows == 2) {
            SoundPlayer.playSoundEffect("./src/double.wav");
        }
        else if (rows == 3) {
            SoundPlayer.playSoundEffect("./src/triple.wav");
        }
        else if (rows == 4) {
            SoundPlayer.playSoundEffect("./src/four.wav");
        }
        if(rows>0) {
        	//updates score
        	score += (rows^2)*100*level;
        	totalLines += rows;
        	level = totalLines/10+1;
        	if (scoreLabelUI != null) {
                scoreLabelUI.setText("SCORE: " + score);
                levelLabel.setText("LEVEL:" + level);
                rowsLabel.setText("ROWS:" + totalLines);
            }
        	shakeScreen(gameFrame, rows*40);
        	blub = 25*(int)(20 * Math.pow(0.9, level));
        	timer.stop();
            timer = new Timer(blub, e -> { if (!movePiece(0, 1)) freeze(); repaint(); });
            timer.start();

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
    //jiggle physics
    public void shakeScreen(JFrame frame, int intensity) {
        final Point originalLocation = frame.getLocation();
        final int shakeDuration = 25*intensity; // milliseconds
        int shakeIntensity = intensity;  // pixels
        final long startTime = System.currentTimeMillis();

        Timer shakeTimer = new Timer(20, null); // Run every 20ms
        shakeTimer.addActionListener(e -> {
            long elapsed = System.currentTimeMillis() - startTime;
            
            if (elapsed < shakeDuration) {
            	double multiplier =  (25*intensity-elapsed)/1000.0;
            	System.out.println(elapsed);
            	System.out.println(multiplier);
                int xOffset = (int)(multiplier* ((Math.random() * shakeIntensity)- (shakeIntensity / 2)));
                int yOffset = (int)(multiplier* ((Math.random() * shakeIntensity)- (shakeIntensity / 2)));
                
                frame.setLocation(originalLocation.x + xOffset, originalLocation.y + yOffset);
            } else {
                frame.setLocation(originalLocation);
                ((Timer) e.getSource()).stop();
            }
        });
        shakeTimer.start();
    }
    //WHY KEN WHY
    public void startDvdBounce(JFrame frame) {
        if (dvdTimer != null) dvdTimer.stop();

        dvdTimer = new Timer(50, e -> {
            Point loc = frame.getLocation();
            Dimension size = frame.getSize();
            Dimension screen = java.awt.Toolkit.getDefaultToolkit().getScreenSize();

            
            int nextX = loc.x + level*moveX;
            int nextY = loc.y + level*moveY;

            if (nextX <= 0 || nextX*level + size.width >= screen.width) {
                moveX *= -1; 
                nextX = loc.x + moveX*level;
            }

            if (nextY <= 0 || nextY*level + size.height >= screen.height) {
                moveY *= -1;
                nextY = loc.y + moveY*level;
            }

            frame.setLocation(nextX, nextY);
        });

        dvdTimer.start();
    }
    public int getScore() {
    	System.out.println(score);
    	return score;
    	
    }

}