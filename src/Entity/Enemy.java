package Entity;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.List;

public class Enemy {
    private int health;
    private float speed;
    private int x, y;  // Screen pixel coordinates
    private float exactX, exactY;
    private List<Point> path;
    private int currentIndex = 0;
    private static final int TILE_SIZE = 40;
    private BufferedImage image;

    public Enemy(int health, float speed, int x, int y) {
        this.health = health;
        this.speed = speed;
        this.exactX = x;
        this.exactY = y;
        this.x = (int) exactX;
        this.y = (int) exactY;
        
        try {
            // Default image if not specified
            this.image = ImageIO.read(new File("art/enemy/bug.png"));
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
    
    public int getHealth() {
        return health;
    }

    public void update() {
        if (path == null || currentIndex >= path.size() - 1) return;

        Point target = path.get(currentIndex + 1);
        float targetX = target.x * TILE_SIZE;
        float targetY = target.y * TILE_SIZE;

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

    public void draw(Graphics g) {
        if (image != null) {
            g.drawImage(image, x, y, TILE_SIZE, TILE_SIZE, null);
        } else {
            g.setColor(java.awt.Color.RED);
            g.fillOval(x, y, TILE_SIZE, TILE_SIZE);
        }
        
        // Draw health bar
        g.setColor(java.awt.Color.RED);
        g.fillRect(x, y - 10, TILE_SIZE, 5);
        g.setColor(java.awt.Color.GREEN);
        g.fillRect(x, y - 10, (int)(TILE_SIZE * (health / 100.0)), 5);
    }
}
