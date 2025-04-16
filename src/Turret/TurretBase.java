package Turret;

import Entity.Enemy;
import java.awt.*;
import java.util.List;

public abstract class TurretBase {
    protected String name;
    protected int cost;
    protected int damage;
    protected int range;
    protected int x, y;
    protected int fireRate;
    protected long lastFireTime;
    private boolean hovered = false;

    public TurretBase(String name, int cost, int damage, int range, int x, int y, int fireRate) {
        this.name = name;
        this.cost = cost;
        this.damage = damage;
        this.range = range;
        this.x = x;
        this.y = y;
        this.fireRate = fireRate;
        this.lastFireTime = 0;
    }

    public abstract void attack(Enemy enemy);

    public void attackEnemies(List<Enemy> enemies) {
        long now = System.currentTimeMillis();
        if (now - lastFireTime < fireRate) return;

        for (Enemy enemy : enemies) {
            if (isInRange(enemy) && enemy.isAlive()) {
                attack(enemy);
                lastFireTime = now;
                break;
            }
        }
    }

    protected boolean isInRange(Enemy enemy) {
        double dx = enemy.getX() - x;
        double dy = enemy.getY() - y;
        return Math.sqrt(dx * dx + dy * dy) <= range * 40;
    }

    public void setHovered(boolean hovered) {
        this.hovered = hovered;
    }

    public void draw(Graphics g, Color turretColor) {
        g.setColor(turretColor);
        g.fillRect(x - 10, y - 10, 20, 20);

        if (hovered) {
            g.setColor(turretColor);
            g.drawOval(x - range * 40, y - range * 40, range * 80, range * 80);
        }
    }

    // Getters
    public int getX() { return x; }
    public int getY() { return y; }
    public String getName() { return name; }
    public int getCost() { return cost; }
    public int getRange() { return range; }

    public abstract void draw(Graphics g);
}
