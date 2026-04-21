import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SwingTest {
    public static void main(String[] args) {
        // Run the GUI on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> new GameController().start());
    }
}

class GameController {
    JFrame startFrame, controlsFrame, gameFrame;
    TetrisBoard gameBoard;

    public void start() {
        // 1. Start Frame
        startFrame = new JFrame("Hare Tetris - Start");
        startFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        startFrame.setSize(300, 200);
        startFrame.setLayout(new GridBagLayout());

        JButton startBtn = new JButton("Start Game");
        JButton helpBtn = new JButton("Controls");
        
        startBtn.addActionListener(e -> { startFrame.setVisible(false); gameFrame.setVisible(true); gameBoard.startGame(); SoundPlayer.playMusic("src/Hare_Tetris.wav");});
        helpBtn.addActionListener(e -> { startFrame.setVisible(false); controlsFrame.setVisible(true); });

        JPanel startPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        startPanel.add(startBtn);
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
        gameFrame = new JFrame("Hare Tetris");
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameFrame.setSize(300, 600);
        gameFrame.setLayout(new BorderLayout()); 
        
        gameBoard = new TetrisBoard();
        gameFrame.add(gameBoard);
        gameFrame.addKeyListener(gameBoard.getKeyAdapter());

        startFrame.setLocationRelativeTo(null);
        startFrame.setVisible(true);
    }
}

class TetrisBoard extends JPanel {
    private final int WIDTH = 10, HEIGHT = 20, TILE_SIZE = 25;
    private int[][] board = new int[HEIGHT][WIDTH];
    private Timer timer;
    private Point piecePos;
    private int[][] currentPiece;
    private int currentType;

    // Tetromino definitions
    private final int[][][] SHAPES = {
        {{1, 1, 1, 1}}, {{1, 1}, {1, 1}}, {{0, 1, 0}, {1, 1, 1}},
        {{0, 1, 1}, {1, 1, 0}}, {{1, 1, 0}, {0, 1, 1}},
        {{1, 0, 0}, {1, 1, 1}}, {{0, 0, 1}, {1, 1, 1}}
    };
    private final Color[] COLORS = {Color.CYAN, Color.YELLOW, Color.MAGENTA, Color.GREEN, Color.RED, Color.BLUE, Color.ORANGE};

    public TetrisBoard() {
        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(WIDTH * TILE_SIZE, HEIGHT * TILE_SIZE));
    }

    public void startGame() {
        board = new int[HEIGHT][WIDTH];
        spawnPiece();
        if (timer != null) timer.stop();
        timer = new Timer(500, e -> { if (!movePiece(0, 1)) freeze(); repaint(); });
        timer.start();
    }

    private void spawnPiece() {
        currentType = (int) (Math.random() * SHAPES.length);
        currentPiece = SHAPES[currentType];
        piecePos = new Point(WIDTH / 2 - currentPiece[0].length / 2, 0);
        if (intersects(piecePos.x, piecePos.y, currentPiece)) {
            timer.stop();
            JOptionPane.showMessageDialog(this, "Game Over!");
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
        for (int r = HEIGHT - 1; r >= 0; r--) {
            boolean full = true;
            for (int c = 0; c < WIDTH; c++) if (board[r][c] == 0) full = false;
            if (full) {
                for (int i = r; i > 0; i--) board[i] = board[i - 1].clone();
                board[0] = new int[WIDTH];
                r++;
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

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Dwraw static board
        for (int r = 0; r < HEIGHT; r++)
            for (int c = 0; c < WIDTH; c++)
                if (board[r][c] != 0) drawTile(g, c, r, COLORS[board[r][c] - 1]);
        // Draw active piece
        if (currentPiece != null) {
            for (int r = 0; r < currentPiece.length; r++)
                for (int c = 0; c < currentPiece[0].length; c++)
                    if (currentPiece[r][c] != 0) drawTile(g, piecePos.x + c, piecePos.y + r, COLORS[currentType]);
        }
    }

    private void drawTile(Graphics g, int x, int y, Color color) {
        g.setColor(color);
        g.fillRect(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE - 1, TILE_SIZE - 1);
        g.setColor(color.darker());
        g.drawRect(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE - 1, TILE_SIZE - 1);
    }
}
