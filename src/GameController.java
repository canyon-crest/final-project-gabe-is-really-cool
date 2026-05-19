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
    JFrame startFrame, controlsFrame, gameFrame, hareFrame, endFrame; 
    TetrisBoard gameBoard; 
    HareBoard hareBoard; 
    
    // Step A: Declare your score label at the class level so it can be passed around
    private JLabel scoreLabel; 

    public void start() { 
        // 1. Start Frame 
        startFrame = new JFrame("Hare Tetris - Start"); 
        startFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        startFrame.setSize(300, 200); 
        startFrame.setLayout(new GridBagLayout()); 

        JButton startBtn = new JButton("Start Game"); 
        JButton hareBtn = new JButton("Hare Mode"); 
        JButton helpBtn = new JButton("Controls"); 

        // ACTION: STANDARD TETRIS LAUNCH
        startBtn.addActionListener(e -> { 
            gameFrame = new JFrame("Hare Tetris"); 
            gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
            gameFrame.setSize(600, 600); 
            gameFrame.setLayout(new BorderLayout()); 

            PieceDisplay normalDisplay = new PieceDisplay(); 
            JPanel normalRightPanel = buildSidePanel(normalDisplay);
            JPanel normalBottomPanel = new JPanel();
            normalBottomPanel.setBackground(Color.yellow);
            normalBottomPanel.setPreferredSize(new Dimension(250, 63));

            gameBoard = new TetrisBoard(); 
            gameFrame.add(gameBoard, BorderLayout.CENTER); 
            
            // Step B: Pass the label reference into the game engine
            gameBoard.setScoreLabel(scoreLabel); 
            
            gameBoard.addPieceDisplay(normalDisplay); 
            gameBoard.addFrames(gameFrame, endFrame); 
            gameBoard.setBorder(BorderFactory.createLineBorder(Color.GRAY)); 
            
            gameFrame.addKeyListener(gameBoard.getKeyAdapter()); 
            gameFrame.add(normalBottomPanel, BorderLayout.PAGE_END); 
            gameFrame.add(normalRightPanel, BorderLayout.LINE_END); 
            
            startFrame.setVisible(false); 
            gameFrame.setVisible(true); 
            gameBoard.startGame(); 
            SoundPlayer.playMusic("src/Hare_Tetris.wav"); 
        }); 

        helpBtn.addActionListener(e -> { 
            startFrame.setVisible(false); 
            controlsFrame.setVisible(true); 
        }); 

        // ACTION: HARE MODE LAUNCH
        hareBtn.addActionListener(e -> { 
            hareFrame = new JFrame("Hare Tetris - Hare Mode"); 
            hareFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
            hareFrame.setSize(850, 600); 
            hareFrame.setLayout(new BorderLayout()); 

            PieceDisplay hareDisplay = new PieceDisplay(); 
            JPanel hareRightPanel = buildSidePanel(hareDisplay);
            JPanel hareBottomPanel = new JPanel();
            hareBottomPanel.setBackground(Color.yellow);
            hareBottomPanel.setPreferredSize(new Dimension(250, 63));

            hareBoard = new HareBoard(); 
            hareFrame.add(hareBoard, BorderLayout.CENTER); 
            
            // Step C: Pass the label reference into the hare engine
            hareBoard.setScoreLabel(scoreLabel); 
            
            hareBoard.addPieceDisplay(hareDisplay); 
            hareBoard.addFrames(hareFrame, endFrame); 
            hareBoard.setBorder(BorderFactory.createLineBorder(Color.GRAY)); 
            
            hareFrame.addKeyListener(hareBoard.getKeyAdapter()); 
            hareFrame.add(hareBottomPanel, BorderLayout.PAGE_END); 
            hareFrame.add(hareRightPanel, BorderLayout.LINE_END); 
            
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
        JLabel helpLabel = new JLabel("<html><b>CONTROLS:</b><br>Arrows: Move & Rotate<br>Space: Hard Drop<br>L: Fire Laser (Hare)</html>", SwingConstants.CENTER); 
        JButton backBtn = new JButton("Back"); 
        backBtn.addActionListener(e -> { 
            controlsFrame.setVisible(false); 
            startFrame.setVisible(true); 
        }); 
        controlsFrame.setLayout(new BorderLayout()); 
        controlsFrame.add(helpLabel, BorderLayout.CENTER); 
        controlsFrame.add(backBtn, BorderLayout.SOUTH); 

        startFrame.setLocationRelativeTo(null); 
        startFrame.setVisible(true); 

        // 4. Game Over Frame Setup
        endFrame = new JFrame("End"); 
        endFrame.setSize(300, 200); 
        JLabel gameOver = new JLabel("GAME OVER", SwingConstants.CENTER); 
        JButton restartBtn = new JButton("Restart"); 
        restartBtn.addActionListener(e -> { 
            endFrame.setVisible(false); 
            startFrame.setVisible(true); 
            SoundPlayer.stopMusic(); 
        }); 
        endFrame.add(gameOver, BorderLayout.CENTER); 
        endFrame.add(restartBtn, BorderLayout.SOUTH); 
    } 

    private JPanel buildSidePanel(PieceDisplay displayInstance) {
        JPanel rightPanel = new JPanel(); 
        rightPanel.setBackground(Color.blue); 
        rightPanel.setPreferredSize(new Dimension(335, 600)); 

        JPanel nextPiece = new JPanel(); 
        nextPiece.setPreferredSize(new Dimension(300, 180)); 
        nextPiece.setBackground(Color.red); 
        rightPanel.add(nextPiece); 

        JLabel nextPieceLabel = new JLabel("NEXT PIECE:"); 
        nextPiece.add(nextPieceLabel); 
        nextPiece.add(displayInstance); 

        JPanel scoreBoard = new JPanel(); 
        scoreBoard.setPreferredSize(new Dimension(300, 300)); 
        scoreBoard.setBackground(Color.orange); 
        rightPanel.add(scoreBoard); 

        // Step D: Assign the instantiated label to our class property
        scoreLabel = new JLabel("SCORE: 0"); 
        scoreBoard.add(scoreLabel); 
        
        JLabel level = new JLabel("LEVEL: 1"); 
        scoreBoard.add(level); 
        JLabel lines = new JLabel("LINES: 0"); 
        scoreBoard.add(lines);

        return rightPanel;
    }
}
