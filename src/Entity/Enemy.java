package Entity;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.List;
import Utility.GameSettings;

public abstract class Enemy {
    private int health;
    private int maxHealth;
    private float speed;
    private int x, y;// Screen pixel coordinates
    private float exactX, exactY;
    private List<Point> path;
    private int currentIndex = 0;
    private BufferedImage image;
    private String imagePath;
    public abstract int getPointValue();

    public Enemy(int health, float speed, int x, int y, String imagePath) {
        this.health = health;
        this.maxHealth = health;  // Store the initial health as max health
        this.speed = speed;
        this.exactX = x;
        this.exactY = y;
        this.x = (int) exactX;
        this.y = (int) exactY;
        this.imagePath = imagePath;
        
        try {
            // Default image if not specified
            this.image = ImageIO.read(new File(imagePath));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Could not load enemy image");
        }
    }
    
    public void setPath(List<Point> path) {
        this.path = path;
        this.currentIndex = 0;
    }
    
    public void setPosition(int x, int y) {
        this.exactX = x;
        this.exactY = y;
        this.x = (int) exactX;
        this.y = (int) exactY;
    }

    public void update() {
        if (path == null || currentIndex >= path.size() - 1) return;

        Point target = path.get(currentIndex + 1);
        float targetX = GameSettings.tileToScreen(target.x);
        float targetY = GameSettings.tileToScreen(target.y);

        float dx = targetX - exactX;
        float dy = targetY - exactY;
        float dist = (float) Math.sqrt(dx * dx + dy * dy);

        if (dist < speed) {
            exactX = targetX;
            exactY = targetY;
            currentIndex++;
        } else {
            exactX += speed * dx / dist;
            exactY += speed * dy / dist;
        }

        this.x = (int) exactX;
        this.y = (int) exactY;
    }

    public void takeDamage(int damage) {
        health -= damage;
    }

    public boolean isAlive() {
        return health > 0;
    }

    public boolean hasReachedEnd() {
        return path != null && currentIndex >= path.size() - 1;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getHealth() {
        return health;
    }

    public float getSpeed() {
        return speed;
    }

    public void draw(Graphics g) {
        int tileSize = GameSettings.getTileSize();
        
        if (image != null) {
            g.drawImage(image, x, y, tileSize, tileSize, null);
        } else {
            g.setColor(java.awt.Color.RED);
            g.fillOval(x, y, tileSize, tileSize);
        }
        
        // Draw health bar
        g.setColor(java.awt.Color.RED);
        g.fillRect(x, y - GameSettings.scaled(10), tileSize, GameSettings.scaled(5));
        g.setColor(java.awt.Color.GREEN);
        g.fillRect(x, y - GameSettings.scaled(10), (int)MapRange.remap(0, maxHealth, 0, tileSize, health), GameSettings.scaled(5));
    }
}
