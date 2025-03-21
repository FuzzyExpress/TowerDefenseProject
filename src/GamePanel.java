import java.awt.Graphics;
import javax.swing.JPanel;

public class GamePanel extends JPanel {
    private GameManager gameManager;

    public GamePanel(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Draw enemies, towers, and the path based on the gameManager state
        // For example, loop through the enemy list and draw each enemy
    }
}
