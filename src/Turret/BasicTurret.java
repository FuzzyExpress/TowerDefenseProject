package Turret;

  /**
     * Cost: 100
     * Damage: 10
     * Range: 5
     * Attack Interval: 1 attack per second (approximately 1000 milliseconds)
     */
    
import Enemy;
import java.util.List;

public class BasicTurret extends TurretBase {

    // Initialize the turret properties in the constructor
    public BasicTurret(int x, int y) {
        super("Basic Turret", 100, 10, 5, x, y, 1000);
    }

    @Override
    public void attack(Enemy enemy) {
        // Check if the enemy is within range before attacking
        if (isInRange(enemy)) {
            enemy.takeDamage(damage);
        }
    }
}

   