import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GameRunner {
    public static void main(String[] args) {
        // Run the GUI on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> new GameController().start());
    }
}