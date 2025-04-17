package Turret;

import Entity.Enemy;
import Utility.GameSettings;

import java.awt.*;

public class BasicTurret extends TurretBase {
    public BasicTurret(int x, int y) {
        super("Basic Turret", 100, 20, 5, x, y, 1000, "SimpleTurret");
    }

    @Override
    public void attack(Enemy enemy) {
        enemy.takeDamage(damage);
        System.out.println(name + " hit for " + damage);
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.BLUE);
        g.fillRect(x - 10, y - 10, 20, 20);
        g.setColor(Color.CYAN);
        g.drawOval(x - range * GameSettings.getTileSize(), y - range * GameSettings.getTileSize(), range * 2 * GameSettings.getTileSize(), range * 2 * GameSettings.getTileSize());
    }
}
