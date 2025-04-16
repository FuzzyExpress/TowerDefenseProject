import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import Entity.BasicBug;
import Turret.*;

public class MainGUI {
    private static final int FPS = 60;
    private static final int DELAY = 1000 / FPS;

    public static void main(String[] args) {
        GameManager gameManager = new GameManager();
        GamePanel gamePanel = new GamePanel(gameManager);
        JFrame frame = new JFrame("Tower Defense Game");

        // Create bottom panel for turret selection
        JPanel controlPanel = new JPanel();
        String[] turretOptions = {"Basic", "Heavy", "Rapid", "Sniper"};
        JComboBox<String> turretSelector = new JComboBox<>(turretOptions);
        controlPanel.add(new JLabel("Select Turret:"));
        controlPanel.add(turretSelector);

        gamePanel.setPreferredSize(new Dimension(800, 550));
        frame.setLayout(new BorderLayout());
        frame.add(gamePanel, BorderLayout.CENTER);
        frame.add(controlPanel, BorderLayout.SOUTH);

        // Pass turret selector choice to GamePanel
        gamePanel.setTurretSelector(turretSelector);

        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
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
