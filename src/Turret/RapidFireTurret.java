package Turret;

/**
     * Cost: 200
     * Damage: 5
     * Range: 5
     * Attack Interval: 4 attacks per second (approximately 250 milliseconds)
     */


import Enemy;
import java.util.List;

public class RapidFireTurret extends TurretBase {

    public RapidFireTurret(int x, int y) {
        // fireRate = 250 milliseconds, corresponding to 4 attacks per second
        super("Rapid-Fire Turret", 200, 5, 5, x, y, 250);
    }

    @Override
    public void attack(Enemy enemy) {
        if (isInRange(enemy)) {
            enemy.takeDamage(damage);
        }
    }
}
