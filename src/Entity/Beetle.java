package Entity;

public class Beetle extends Enemy {
    private int health;
    private float speed;
    public Beetle(int startX, int startY) {
        super(startX, startY);
        this.health = 400;
        this.speed = 0.3f;
    }
    
    @Override
    public void takeDamage(int health, int damage) {
        super.takeDamage(health, damage);
        if (!isAlive(health)) {
            onDeath();
        }
    }

    @Override
    public int getHealth() {
        return health;
    }

    @Override
    public float getSpeed() {
        return speed;
    }

    // Method triggered on death to perform the collision charge.
    public void onDeath() {
        System.out.println("Beetle died. Charging toward the nearest turret to deal collision damage.");
        // TODO: Implement logic to find the nearest turret and apply collision damage.
    }
}
