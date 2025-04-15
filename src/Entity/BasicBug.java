package Entity;

public class BasicBug extends Enemy {
    private int health;
    private float speed;
    public BasicBug(int startX, int startY) {
        super(startX, startY);
        this.health = 100;
        this.speed = 1.0f;
    }

    @Override
    public int getHealth() {
        return health;
    }

    @Override
    public float getSpeed() {
        return speed;
    }
}
