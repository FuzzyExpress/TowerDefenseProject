package Turret;

import Entity.Enemy;
import java.util.List;

public class HeavyTurret extends TurretBase {
    public HeavyTurret(int x, int y) {
        // cost=300, damage=40, range=3, attack interval=3333ms (~0.3 attacks/sec)
        super("Heavy Turret", 300, 40, 3, x, y, 3333);
    }

    @Override
    public void attack(Enemy enemy) {
        if (isInRange(enemy)) {
            enemy.takeDamage(damage);
            System.out.println(getName() + " attacked enemy for " + damage + " damage.");
        }
    }

    @Override
    public void attackEnemies(List<Enemy> enemies) {
        // Example: deal damage to every enemy within range
        for (Enemy enemy : enemies) {
            if (isInRange(enemy)) {
                enemy.takeDamage(damage);
                System.out.println(getName() + " area-attacked enemy for " + damage + " damage.");
            }
        }
    }
}
