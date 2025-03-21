public class Enemy {
    private int health;
    private float speed;
    private int x, y;  // Current position of the enemy

    public Enemy(int health, float speed, int startX, int startY) {
        this.health = health;
        this.speed = speed;
        this.x = startX;
        this.y = startY;
    }

    // Update the enemy's position (move along the path)
    public void update() {
        // Implement logic for moving along the Path
        // For example, update x and y based on speed
    }

    // Apply damage to the enemy
    public void takeDamage(int damage) {
        health -= damage;
    }

    // Check if the enemy is still alive
    public boolean isAlive() {
        return health > 0;
    }

    // Getters and setters omitted for brevity...
}
