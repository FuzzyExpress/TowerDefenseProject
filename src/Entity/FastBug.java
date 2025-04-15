package Entity;

public class FastBug extends Enemy {
    public FastBug(int health, float speed, int startX, int startY) {
        super(health, speed, startX, startY);
        health = 50;
        speed = 2.0f;
    }
    
    // You could override update() for special movement logic if needed.
}
