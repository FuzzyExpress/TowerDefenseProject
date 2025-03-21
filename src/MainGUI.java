import javax.swing.JFrame;

public class MainGUI {
    public static void main(String[] args) {
        GameManager gameManager = new GameManager();
        JFrame frame = new JFrame("Tower Defense Game");
        GamePanel gamePanel = new GamePanel(gameManager);

        frame.add(gamePanel);
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        gameManager.startGame();
    }
}
