package Entity;

import java.awt.Graphics;
import java.awt.Point;
import java.util.List;

public class Enemy {
    private int health;
    private float speed;
    private int x, y;  // Screen pixel coordinates
    private float exactX, exactY;
    private List<Point> path;
    private int currentIndex = 0;
    private static final int TILE_SIZE = 40;

    public Enemy(int health, float speed, List<Point> path) {
        this.health = health;
        this.speed = speed;
        this.path = path;

        Point start = path.get(0);
        this.exactX = start.x * TILE_SIZE;
        this.exactY = start.y * TILE_SIZE;
        this.x = (int) exactX;
        this.y = (int) exactY;
    }

    public void update() {
        if (currentIndex >= path.size()) return;

        Point target = path.get(currentIndex);
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

    public boolean reachedEnd() {
        return currentIndex >= path.size();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void draw(Graphics g) {
        g.setColor(java.awt.Color.RED);
        g.fillOval(x, y, TILE_SIZE, TILE_SIZE);
    }
}
