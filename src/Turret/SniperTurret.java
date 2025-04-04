package Turret;

import Enemy;
import java.util.List;

public class SniperTurret extends TurretBase {

    public SniperTurret(int x, int y) {
        // fireRate = 5000 milliseconds, corresponding to 0.2 attacks per second
        super("Sniper Turret", 300, 100, 10, x, y, 5000);
    }

    @Override
    public void attack(Enemy enemy) {
        if (isInRange(enemy)) {
            enemy.takeDamage(damage);
        }
    }
}
