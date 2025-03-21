public class GameLoop extends Thread {
    private GameManager gameManager;
    private boolean running;

    public GameLoop(GameManager gameManager) {
        this.gameManager = gameManager;
        running = true;
    }

    @Override
    public void run() {
        while (running) {
            gameManager.update();
            // Optionally, call repaint() for GamePanel here

            // Control the update frequency (e.g., approximately 60 frames per second)
            try {
                Thread.sleep(16);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void stopLoop() {
        running = false;
    }
}
