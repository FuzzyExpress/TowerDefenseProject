package Turret;

import Entity.Enemy;
import java.awt.*;
import java.util.List;

public class HeavyTurret extends TurretBase {
    public HeavyTurret(int x, int y) {
        super("Heavy Turret", 300, 50, 3, x, y, 3333);
    }

    @Override
    public void attack(Enemy enemy) {
        enemy.takeDamage(damage);
        System.out.println(name + " hit for " + damage);
    }

    @Override
    public void attackEnemies(List<Enemy> enemies) {
        long now = System.currentTimeMillis();
        if (now - lastFireTime < fireRate) return;

        for (Enemy enemy : enemies) {
            if (isInRange(enemy) && enemy.isAlive()) {
                enemy.takeDamage(damage);
            }
        }

        System.out.println(name + " AOE fired");
        lastFireTime = now;
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.RED);
        g.fillRect(x - 10, y - 10, 20, 20);
        g.setColor(Color.PINK);
        g.drawOval(x - range * 40, y - range * 40, range * 80, range * 80);
    }
}
