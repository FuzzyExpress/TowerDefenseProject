package Entity;

import java.awt.image.BufferedImage;

public class BasicBug extends Enemy {
    private static final int DEFAULT_HEALTH = 100;
    private static final float DEFAULT_SPEED = 2.0f;
    
    public BasicBug(int x, int y) {
        super(DEFAULT_HEALTH, DEFAULT_SPEED, x, y);
    }
}
