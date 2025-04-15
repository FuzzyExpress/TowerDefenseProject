package Entity;

public class Beetle extends Enemy {
    public Beetle(int startX, int startY) {
        super(400, 0.3f, startX, startY, "art/Bugs/Beetle.png");
    }
    
    @Override
    public void takeDamage(int health, int damage) {
        super.takeDamage(health, damage);
        if (!isAlive(health)) {
            onDeath();
        }
    }

    // Method triggered on death to perform the collision charge.
    public void onDeath() {
        System.out.println("Beetle died. Charging toward the nearest turret to deal collision damage.");
        // TODO: Implement logic to find the nearest turret and apply collision damage.
    }
}
