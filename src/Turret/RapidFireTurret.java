package Turret;

import Entity.Enemy;

public class RapidFireTurret extends TurretBase {
    public RapidFireTurret(int x, int y) {
        // cost=200, damage=5, range=5, attack interval=250ms (4 attacks/sec)
        super("Rapid-Fire Turret", 200, 5, 5, x, y, 250);
    }

    @Override
    public void attack(Enemy enemy) {
        if (isInRange(enemy)) {
            enemy.takeDamage(damage);
            System.out.println(getName() + " attacked enemy for " + damage + " damage.");
        }
    }
}
