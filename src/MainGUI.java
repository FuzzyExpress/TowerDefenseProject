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
        controlPanel.setBackground(new Color(40, 40, 40));
        controlPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Turret selector
        String[] turretOptions = {"Basic", "Heavy", "Rapid", "Sniper"};
        JComboBox<String> turretSelector = new JComboBox<>(turretOptions);
        JLabel selectorLabel = new JLabel("Select Turret:");
        selectorLabel.setForeground(Color.WHITE);
        controlPanel.add(selectorLabel);
        controlPanel.add(Box.createHorizontalStrut(10));
        controlPanel.add(turretSelector);

        // Calculate window size based on tile size
        int windowWidth = 20 * GameSettings.getTileSize();  // 20 tiles wide
        int windowHeight = 15 * GameSettings.getTileSize(); // 15 tiles high
        gamePanel.setPreferredSize(new Dimension(windowWidth, windowHeight));
        
        // Create main panel with GridBagLayout
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(new Color(30, 30, 30));
        
        // Add game panel to center with constraints
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(20, 20, 20, 20); // Add padding around game panel
        mainPanel.add(gamePanel, gbc);
        
        frame.setLayout(new BorderLayout());
        frame.add(mainPanel, BorderLayout.CENTER);
        frame.add(controlPanel, BorderLayout.SOUTH);

        // Pass turret selector choice to GamePanel
        gamePanel.setTurretSelector(turretSelector);

        // Set minimum size for the frame
        frame.setMinimumSize(new Dimension(windowWidth + 100, windowHeight + 150));
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(true);
        frame.setLocationRelativeTo(null);

        //Check for Pause
        frame.getRootPane().addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_P) {
                    gameManager.togglePause();
                }
            }
        });
        frame.getRootPane().setFocusable(true);
        frame.getRootPane().requestFocusInWindow();

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
