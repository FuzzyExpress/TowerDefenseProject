package Entity;

public class FastBug extends Enemy {
    public FastBug(int startX, int startY) {
        super(50, 2.0f, startX, startY);
    }
    
    // You could override update() for special movement logic if needed.
}
