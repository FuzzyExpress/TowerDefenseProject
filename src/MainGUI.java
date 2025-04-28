import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import Entity.BasicBug;
import Turret.*;
import Utility.GameSettings;

public class MainGUI {
    private static final int FPS = 60;
    private static final int DELAY = 1000 / FPS;

    public static void main(String[] args) {
        // Set the tile size - default is 40
        GameSettings.setTileSize(60);
        
        GameManager gameManager = new GameManager();
        GamePanel gamePanel = new GamePanel(gameManager);
        JFrame frame = new JFrame("Tower Defense Game");

        // Create bottom panel for turret selection and settings
        JPanel controlPanel = new JPanel();
        
        // Turret selector
        String[] turretOptions = {"Basic", "Heavy", "Rapid", "Sniper"};
        JComboBox<String> turretSelector = new JComboBox<>(turretOptions);
        controlPanel.add(new JLabel("Select Turret:"));
        controlPanel.add(turretSelector);

        // Calculate window size based on tile size
        int windowWidth = 20 * GameSettings.getTileSize();  // 20 tiles wide
        int windowHeight = 10 * GameSettings.getTileSize(); // 15 tiles high
        gamePanel.setPreferredSize(new Dimension(windowWidth, windowHeight));
        
        frame.setLayout(new BorderLayout());
        frame.add(gamePanel, BorderLayout.CENTER);
        frame.add(controlPanel, BorderLayout.SOUTH);

        // Pass turret selector choice to GamePanel
        gamePanel.setTurretSelector(turretSelector);

        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(true);
        frame.setLocationRelativeTo(null);

        //Check for Pause
        frame.addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyPressed(KeyEvent e)
            {
                if (e.getKeyCode() == KeyEvent.VK_P)
                {
                    gameManager.togglePause();
                }
            }
        });
        frame.setFocusable(true);
        //Make P recognized anywhere in the window
        frame.requestFocusInWindow();

        frame.setVisible(true);

        gameManager.startGame();
        gameManager.addEnemy(new BasicBug(0, 0));

        Timer gameTimer = new Timer(DELAY, e -> {
            gameManager.update();
            gamePanel.repaint();
        });
        gameTimer.start();
    }
}
