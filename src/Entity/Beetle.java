package Entity;

public class Beetle extends Enemy {
    public Beetle(int health, float speed, int startX, int startY) {
        super(health, speed, startX, startY);
        health = 400;
        speed = 0.3f;
    }
    
    @Override
    public void takeDamage(int damage) {
        super.takeDamage(damage);
        if (!isAlive()) {
            onDeath();
        }
    }

    // Method triggered on death to perform the collision charge.
    public void onDeath() {
        System.out.println("Beetle died. Charging toward the nearest turret to deal collision damage.");
        // TODO: Implement logic to find the nearest turret and apply collision damage.
    }
}
