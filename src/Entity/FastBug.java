package Entity;

public class FastBug extends Enemy {
    public FastBug(int startX, int startY) {
        super(50, 2.0f, startX, startY);
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
