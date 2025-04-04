package Turret;

/**
     * Cost: 300
     * Damage: 40
     * Range: 3
     * Attack Interval: 1 attack per second (approximately 1000 milliseconds)
     */

import Enemy;
import java.util.List;

public class HeavyTurret extends TurretBase {

    public HeavyTurret(int x, int y) {
        // fireRate = 3333 milliseconds, corresponding to about 0.3 attacks per second
        super("Heavy Turret", 300, 40, 3, x, y, 3333);
    }

    @Override
    public void attack(Enemy enemy) {
        // This turret can also perform a single-target attack if needed
        if (isInRange(enemy)) {
            enemy.takeDamage(damage);
        }
    }

    @Override
    public void attackEnemies(List<Enemy> enemies) {
        // Example: Deal damage to all enemies within range and possibly apply a burning effect
        for (Enemy enemy : enemies) {
            if (isInRange(enemy)) {
                enemy.takeDamage(damage);
                // To add a burning effect, you could implement a status in the Enemy class
                // or periodically apply extra damage in the GameManager.
            }
        }
    }
}
