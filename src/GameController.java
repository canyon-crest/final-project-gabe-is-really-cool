import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.BorderFactory;

class GameController {
    JFrame startFrame, controlsFrame, gameFrame, hareFrame;
    TetrisBoard gameBoard;
    HareBoard hareBoard;
    PieceDisplay nextPieceDisplay = new PieceDisplay();

    public void start() {
        // 1. Start Frame
        startFrame = new JFrame("Hare Tetris - Start");
        startFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        startFrame.setSize(300, 200);
        startFrame.setLayout(new GridBagLayout());

        JButton startBtn = new JButton("Start Game");
        JButton hareBtn = new JButton("Hare Mode");
        JButton helpBtn = new JButton("Controls");
        
        JPanel rightPanel = new JPanel();
        rightPanel.setBackground(Color.blue);
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(Color.yellow);
        rightPanel.setPreferredSize(new Dimension(335, 600));
        bottomPanel.setPreferredSize(new Dimension(250, 63));
        
        JPanel nextPiece = new JPanel();
        nextPiece.setPreferredSize(new Dimension(400, 300));
        nextPiece.setBackground(Color.red);
        rightPanel.add(nextPiece);
        
        JLabel nextPieceLabel = new JLabel("NEXT PIECE:");
        nextPiece.add(nextPieceLabel);
        
        JPanel scoreBoard = new JPanel();
        
        scoreBoard.setPreferredSize(new Dimension(400, 400));
        scoreBoard.setBackground(Color.orange);
        rightPanel.add(scoreBoard);
        
        JLabel score = new JLabel("SCORE:");
        scoreBoard.add(score);
        
        JLabel level = new JLabel("LEVEL:");
        scoreBoard.add(level);
        
        JLabel lines = new JLabel("LINES:");
        scoreBoard.add(lines);
        
        nextPiece.add(nextPieceDisplay);
        
        startBtn.addActionListener(e -> { 
            gameFrame = new JFrame("Hare Tetris");
            gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            gameFrame.setSize(600, 600);
            gameFrame.setLayout(new BorderLayout());
            gameBoard = new TetrisBoard();
            gameFrame.add(gameBoard);
            gameBoard.addPieceDisplay(nextPieceDisplay);
            gameBoard.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            gameFrame.addKeyListener(gameBoard.getKeyAdapter());
            gameFrame.add(bottomPanel, BorderLayout.PAGE_END);
            gameFrame.add(rightPanel, BorderLayout.LINE_END);
        	startFrame.setVisible(false); 
        	gameFrame.setVisible(true); 
        	gameBoard.startGame(); 
        	SoundPlayer.playMusic("src/Hare_Tetris.wav");
        });
        helpBtn.addActionListener(e -> { 
        	startFrame.setVisible(false); 
        	controlsFrame.setVisible(true); 
        });
        hareBtn.addActionListener(e -> {            
        	hareFrame = new JFrame("Hare Tetris");
        	hareFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        	hareFrame.setSize(850, 600);
        	hareFrame.setLayout(new BorderLayout());
        	hareBoard = new HareBoard();
	        hareFrame.add(hareBoard);
	        hareBoard.addPieceDisplay(nextPieceDisplay);
	        hareBoard.setBorder(BorderFactory.createLineBorder(Color.GRAY));
	        hareFrame.addKeyListener(hareBoard.getKeyAdapter());
	        hareFrame.add(bottomPanel, BorderLayout.PAGE_END);
	        hareFrame.add(rightPanel, BorderLayout.LINE_END);
        	startFrame.setVisible(false);
        	hareFrame.setVisible(true);
        	hareBoard.startGame();
        	SoundPlayer.playMusic("src/Hare_Tetris.wav");
        	
        });
        

        JPanel startPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        startPanel.add(startBtn);
        startPanel.add(hareBtn);
        startPanel.add(helpBtn);
        startFrame.add(startPanel);

        // 2. Controls Frame
        controlsFrame = new JFrame("Controls");
        controlsFrame.setSize(300, 200);
        JLabel helpLabel = new JLabel("<html><b>CONTROLS:</b><br>Arrows: Move & Rotate<br>Space: Hard Drop</html>", SwingConstants.CENTER);
        JButton backBtn = new JButton("Back");
        backBtn.addActionListener(e -> { controlsFrame.setVisible(false); startFrame.setVisible(true); });
        
        controlsFrame.setLayout(new BorderLayout());
        controlsFrame.add(helpLabel, BorderLayout.CENTER);
        controlsFrame.add(backBtn, BorderLayout.SOUTH);

        // 3. Game Frame 

        startFrame.setLocationRelativeTo(null);
        startFrame.setVisible(true);
    }
}