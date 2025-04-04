package Turret;

import Entity.Enemy;

public class SniperTurret extends TurretBase {
    public SniperTurret(int x, int y) {
        // fireRate = 5000 milliseconds
        super("Sniper Turret", 300, 100, 10, x, y, 5000);
    }

    @Override
    public void attack(Enemy enemy) {
        if (isInRange(enemy)) {
            enemy.takeDamage(damage);
            System.out.println(getName() + " sniped enemy for " + damage + " damage.");
        }
    }
}
