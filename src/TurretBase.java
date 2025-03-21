public class TurretBase {
    protected int damage;
    protected int range;
    protected int x, y;       // Tower position
    protected int fireRate;   // Attack interval (in milliseconds)

    public TurretBase(int damage, int range, int x, int y, int fireRate) {
        this.damage = damage;
        this.range = range;
        this.x = x;
        this.y = y;
        this.fireRate = fireRate;
    }

    // Check if an enemy is within range and perform an attack
    public void attack(Enemy enemy) {
        // Implement logic to verify whether the enemy is near (x, y)
        enemy.takeDamage(damage);
    }

    // Getters and setters omitted for brevity...
}
