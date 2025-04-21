package Entity;

public class FastBug extends Enemy {
    public FastBug(int startX, int startY) {
        super(50, 2.0f, startX, startY, "art/Bugs/Beetle.png");
    }
    
    // You could override update() for special movement logic if needed.
    @Override
    public int getPointValue() {
        return 200;
    }

}
