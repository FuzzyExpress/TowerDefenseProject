package Entity;

import java.awt.image.BufferedImage;

public class BasicBug extends Enemy {
    private static final int DEFAULT_HEALTH = 200;
    private static final float DEFAULT_SPEED = 1.0f;
    
    public BasicBug(int x, int y) {
        super(DEFAULT_HEALTH, DEFAULT_SPEED, x, y,"art/Bugs/Bug.png");
    }
}
