package Turret;

import Entity.Enemy;
import Utility.GameSettings;

import java.awt.*;

public class SniperTurret extends TurretBase {
    public SniperTurret(int x, int y) {
        super("Sniper Turret", 300, 200, 10, x, y, 5000, "SniperTurret");
    }

    @Override
    public void attack(Enemy enemy) {
        enemy.takeDamage(damage);
        System.out.println(name + " sniped for " + damage);
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.PINK);
        g.fillRect(x - 10, y - 10, 20, 20);
        g.setColor(Color.WHITE);
        g.drawOval(x - range * GameSettings.getTileSize(), y - range * GameSettings.getTileSize(), range * 2 * GameSettings.getTileSize(), range * 2 * GameSettings.getTileSize());
    }
}
