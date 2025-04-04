public class TurretBase {
    protected int turretOriginX;
    protected int turretOriginY;
    protected int damage;
    protected int range;
    protected int x, y;       // Tower position
    protected int fireRate;   // Attack interval (in milliseconds)
    protected Enemy target;

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

    public Enemy getTarget(Enemy enemy) {


        return target;
    }

    public int getTurretOriginX() {
        return turretOriginX;
    }

    public int getTurretOriginY() {
        return turretOriginY;
    }

    public int getFireRate() {
        return fireRate;
    }

    public int getDamage() {
        return damage;
    }

    public int getRange() {
        return range;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setTurretOriginX(int x) {
        this.turretOriginX = (x * 40) - 20;
    }

    public void setTurretOriginY(int y) {
        this.turretOriginY = (y * 40) - 20;
    }
}
