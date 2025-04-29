package Turret;

import Entity.Enemy;
import Utility.GameSettings;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import static java.lang.Math.round;

public abstract class TurretBase {
    protected String name;
    protected int cost;
    protected int damage;
    protected int range;
    protected int x, y;
    protected int fireRate;
    protected long lastFireTime;
    private boolean hovered = false;
    private String imagePath;
    private double rotation = 0;
    private Boolean shooting = false;
    private HashMap<String, BufferedImage> cachedImages = new HashMap<>();

    public TurretBase(String name, int cost, int damage, int range, int x, int y, int fireRate, String imagePath) {
        this.name = name;
        this.cost = cost;
        this.damage = damage;
        this.range = range;
        this.x = x;
        this.y = y;
        this.fireRate = fireRate;
        this.lastFireTime = 0;
        this.imagePath = imagePath;
    }

    public abstract void attack(Enemy enemy);

    public void attackEnemies(List<Enemy> enemies) {
        long now = System.currentTimeMillis();
        if (now - lastFireTime < fireRate) return;

        for (Enemy enemy : enemies) {
            if (isInRange(enemy) && enemy.isAlive()) {
                attack(enemy);
                lastFireTime = now;

                // Calculate rotation angle to enemy
                double dx = enemy.getX() - x;
                double dy = enemy.getY() - y;
                rotation = -Math.atan2(dy, dx); // Negate the angle to reverse the rotation direction
                shooting = true;
                
                break;
            }
        }
    }

    protected boolean isInRange(Enemy enemy) {
        double dx = enemy.getX() - x;
        double dy = enemy.getY() - y;
        return Math.sqrt(dx * dx + dy * dy) <= range * GameSettings.getTileSize();
    }

    public void setHovered(boolean hovered) {
        this.hovered = hovered;
    }

    private BufferedImage lastGoodImage = null; // add this at the top of TurretBase

    public BufferedImage getImage(String path) {
        try {
            if (!cachedImages.containsKey(path)) {
                File file = new File(path);
                if (file.exists()) {
                    BufferedImage img = ImageIO.read(file);
                    cachedImages.put(path, img);
                    lastGoodImage = img; // Save the last successful load
                } else {
                    // System.out.println("TurretLoadFailed: " + path);
                    // Use the last successfully loaded image
                    if (lastGoodImage != null) {
                        cachedImages.put(path, lastGoodImage);
                    } else {
                        // If literally nothing has loaded yet, create a 1x1 blank
                        cachedImages.put(path, new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB));
                    }
                }
            }
            return cachedImages.get(path);
        } catch (IOException e) {
            e.printStackTrace();
            return new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        }
    }


    public void draw(Graphics g, Color turretColor) {
        // g.setColor(turretColor);
        // g.fillRect(x - 10, y - 10, 20, 20);

        

        String path = "art/" + imagePath + "/" + imagePath + "-" + 
                round( ((Math.toDegrees(rotation) - 90 + 360*2) % 360) / 10 ) * 10
                + "-" + 0 + ".png";
        BufferedImage image = getImage(path);
        int turretSize = image.getWidth();
        g.drawImage(image, x - turretSize/2, y - turretSize/2, turretSize, turretSize, null);   

        if (shooting) {
            shooting = false;
            String pathFlash = "art/" + imagePath + "/" + imagePath + "-" + 
                    round( ((Math.toDegrees(rotation) - 90 + 360*2) % 360) / 10 ) * 10
                    + "-flash.png";
            BufferedImage imageFlash = getImage(pathFlash);
            int turretSizeFlash = imageFlash.getWidth();
            g.drawImage(imageFlash, x - turretSizeFlash/2, y - turretSizeFlash/2, turretSizeFlash, turretSizeFlash, null);
        }

        if (hovered) {
            int rangeRadius = range * GameSettings.getTileSize();
            g.setColor(turretColor);
            g.drawOval(x - rangeRadius, y - rangeRadius, rangeRadius * 2, rangeRadius * 2);
        }
    }

    // Getters
    public int getX() { return x; }
    public int getY() { return y; }
    public String getName() { return name; }
    public int getCost() { return cost; }
    public int getRange() { return range; }

    public abstract void draw(Graphics g);
}
