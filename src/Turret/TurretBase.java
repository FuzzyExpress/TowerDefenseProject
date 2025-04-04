package Turret;

import java.util.List;

// Make sure to import the Enemy class according to your project structure
import Enemy;

public abstract class TurretBase {
    protected String name;    // Turret name
    protected int cost;       // Cost to build or purchase the turret
    protected int damage;     // Damage dealt per attack
    protected int range;      // Attack range (measured in pixels or grid units)
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
     * Attack a single enemy. Specific logic is implemented by subclasses.
     */
    public abstract void attack(Enemy enemy);

    /**
     * Optionally, provide a method to attack multiple enemies (e.g., area damage).
     * The default implementation is empty; subclasses like HeavyTurret can override this.
     */
    public void attackEnemies(List<Enemy> enemies) {
        // Implement area damage logic in subclasses if needed
    }

    /**
     * Check if the target enemy is within range.
     * Here we assume that "range" is measured in pixels or grid units.
     */
    protected boolean isInRange(Enemy enemy) {
        double dx = enemy.getX() - x;
        double dy = enemy.getY() - y;
        double distance = Math.sqrt(dx * dx + dy * dy);
        return distance <= range;
    }

    // Getters and setters (if needed)
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

    public String getName() {
        return name;
    }
}
