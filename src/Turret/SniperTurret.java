package Turret;

import Entity.Enemy;

public class SniperTurret extends TurretBase {
    public SniperTurret(int x, int y) {
        // cost=300, damage=100, range=10, attack interval=5000ms (0.2 attacks/sec)
        super("Sniper Turret", 300, 100, 10, x, y, 5000);
    }

    @Override
    public void attack(Enemy enemy) {
        if (isInRange(enemy)) {
            enemy.takeDamage(enemy.getHealth(), damage);
            System.out.println(getName() + " sniped enemy for " + damage + " damage.");
        }
    }
}
