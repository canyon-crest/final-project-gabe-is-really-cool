import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.BorderLayout;

public class SwingTest {
    public static void main(String[] args) {
        // Create a new JFrame (main window)
        JFrame startFrame = new JFrame("Start");
        startFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        startFrame.setSize(300, 200);
        
        JFrame controlsFrame = new JFrame("Controls");
        controlsFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        controlsFrame.setSize(300, 200);
        
        JFrame gameFrame = new JFrame("Game");
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameFrame.setSize(1500, 800);

        // Create a JPanel to hold components
        JPanel startScreen = new JPanel();
        startFrame.add(startScreen);

        // Add a label and a button to the panel
        JLabel label = new JLabel("HARE TETRIS");
        startScreen.add(label);

        JButton start = new JButton("Start");
        startScreen.add(start);
        
        JButton help = new JButton("Help");
        startScreen.add(help);
        
        JPanel controlsScreen = new JPanel();
        controlsFrame.add(controlsScreen);
        
        JLabel controls = new JLabel("<html>CONTROLS: <br> LEFT and RIGHT - MOVE PIECE<br>A AND D - ROTATE PIECE<br>SPACE - HARD DROP<br>DOWN - SOFT DROP</html>");
        controlsScreen.add(controls);
        
        JButton back = new JButton("BACK");
        controlsScreen.add(back);
        
        JPanel gameScreen = new JPanel();
        gameScreen.setPreferredSize(new Dimension(500, 50));
        gameScreen.setBackground(Color.yellow);
        gameFrame.getContentPane().add(gameScreen, BorderLayout.PAGE_END);
        
        JButton back2 = new JButton("BACK");
        gameScreen.add(back2);
        
        JPanel gameBoard = new JPanel();
        gameBoard.setPreferredSize(new Dimension(500, 1000));
        gameBoard.setBackground(Color.blue);
        gameFrame.getContentPane().add(gameBoard, BorderLayout.CENTER);
        
        JPanel scoreBoard = new JPanel();
        scoreBoard.setPreferredSize(new Dimension(500, 100));
        scoreBoard.setBackground(Color.green);
        gameFrame.getContentPane().add(scoreBoard, BorderLayout.LINE_END);
        
        // Add action listener to the button
        start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startFrame.setVisible(false);
                gameFrame.setVisible(true);
            }
        });
        
        help.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	startFrame.setVisible(false);
            	controlsFrame.setVisible(true);
            }
        });
        
        back.addActionListener(new ActionListener() {
        	@Override
        	public void actionPerformed(ActionEvent e) {
        		controlsFrame.setVisible(false);
        		startFrame.setVisible(true);
        	}
        });
        
        back2.addActionListener(new ActionListener() {
        	@Override
        	public void actionPerformed(ActionEvent e) {
        		gameFrame.setVisible(false);
        		startFrame.setVisible(true);
        	}
        });

        // Display the window
        startFrame.setVisible(true);
    }
}
