package Turret;

import Entity.Enemy;
import Utility.GameSettings;

import java.awt.*;

public class RapidFireTurret extends TurretBase {
    public RapidFireTurret(int x, int y) {
        super("Rapid-Fire Turret", 1000, 10, 5, x, y, 250, "ChainTurret");
    }

    @Override
    public void attack(Enemy enemy) {
        enemy.takeDamage(damage);
        // System.out.println(name + " rapid-hit for " + damage);
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.YELLOW);
        g.fillRect(x - 10, y - 10, 20, 20);
        g.setColor(Color.ORANGE);
        g.drawOval(x - range * GameSettings.getTileSize(), y - range * GameSettings.getTileSize(), range * 2 * GameSettings.getTileSize(), range * 2 * GameSettings.getTileSize());
    }
}
