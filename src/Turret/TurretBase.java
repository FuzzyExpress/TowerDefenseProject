package Turret;

import Entity.Enemy;
import java.util.List;

public abstract class TurretBase {
    protected String name;    // Turret name
    protected int cost;       // Cost to build/purchase the turret
    protected int damage;     // Damage per attack
    protected int range;      // Attack range (pixels or grid units)
    protected int x, y;       // Turret's coordinates on the map
    protected int fireRate;   // Attack interval in milliseconds

    public TurretBase(String name, int cost, int damage, int range, int x, int y, int fireRate) {
        this.name = name;
        this.cost = cost;
        this.damage = damage;
        this.range = range;
        this.x = x;
        this.y = y;
        this.fireRate = fireRate;
    }

    /**
     * Attack a single enemy. Subclasses implement the specific logic.
     */
    public abstract void attack(Enemy enemy);

    /**
     * Optionally, a method to attack multiple enemies (e.g., area damage).
     * Default implementation does nothing.
     */
    public void attackEnemies(List<Enemy> enemies) {
        // Override in subclasses if needed
    }

    /**
     * Check if the target enemy is within range.
     */
    protected boolean isInRange(Enemy enemy) {
        double dx = enemy.getX() - x;
        double dy = enemy.getY() - y;
        double distance = Math.sqrt(dx * dx + dy * dy);
        return distance <= range;
    }

    // Getters
    public String getName() {
        return name;
    }

    public int getCost() {
        return cost;
    }

    public int getDamage() {
        return damage;
    }

    public int getRange() {
        return range;
    }

    public int getFireRate() {
        return fireRate;
    }
}
