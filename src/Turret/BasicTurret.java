package Turret;

import Entity.Enemy;

public class BasicTurret extends TurretBase {
    public BasicTurret(int x, int y) {
        super("Basic Turret", 100, 10, 5, x, y, 1000);
    }

    @Override
    public void attack(Enemy enemy) {
        if (isInRange(enemy)) {
            enemy.takeDamage(damage);
            System.out.println(getName() + " attacked enemy for " + damage + " damage.");
        }
    }
}
