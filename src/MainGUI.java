import javax.swing.JFrame;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import Entity.BasicBug;

public class MainGUI {
    private static final int FPS = 60;
    private static final int DELAY = 1000 / FPS;
    
    public static void main(String[] args) {
        GameManager gameManager = new GameManager();
        JFrame frame = new JFrame("Tower Defense Game");
        GamePanel gamePanel = new GamePanel(gameManager);

        frame.add(gamePanel);
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // Start the game
        gameManager.startGame();
        
        // Add a test enemy
        gameManager.addEnemy(new BasicBug(0, 0));
        
        // Create a game loop with Timer
        Timer gameTimer = new Timer(DELAY, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Update game state
                gameManager.update();
                
                // Repaint the panel
                gamePanel.repaint();
            }
        });
        
        // Start the game loop
        gameTimer.start();
    }
}
