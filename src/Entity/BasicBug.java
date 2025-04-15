package Entity;

public class BasicBug extends Enemy {
    public BasicBug(int health, float speed, int startX, int startY) {
        super(health, speed, startX, startY);
        health = 100;
        speed = 1.0f;
    }
}
