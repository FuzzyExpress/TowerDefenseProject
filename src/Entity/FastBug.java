package Entity;

public class FastBug extends Enemy {
    private int health;
    private float speed;
    public FastBug(int startX, int startY) {
        super(startX, startY);
        this.health = 50;
        this.speed = 2.0f;
    }

    @Override
    public int getHealth() {
        return health;
    }

    @Override
    public float getSpeed() {
        return speed;
    }
    
    // You could override update() for special movement logic if needed.
}
