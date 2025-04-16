import Entity.Enemy;
import Map.MapLoader;
import Map.PathFinder;
import Map.Tile;
import Map.TileMapper;
import Turret.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class GamePanel extends JPanel {
    private GameManager gameManager;
    private Tile[][] tiles;
    private List<Point> path;
    private JComboBox<String> turretSelector;
    private int[][] rawMap;

    public GamePanel(GameManager gameManager) {
        this.gameManager = gameManager;
        MapLoader mapLoader = new MapLoader();
        mapLoader.loadMap("src/Map/Test.png");
        rawMap = mapLoader.getMapData();

        tiles = new Tile[mapLoader.getWidth()][mapLoader.getHeight()];
        for (int x = 0; x < mapLoader.getWidth(); x++) {
            for (int y = 0; y < mapLoader.getHeight(); y++) {
                tiles[x][y] = new Tile(x, y, rawMap);
            }
        }

        path = new PathFinder(rawMap, mapLoader.getWidth(), mapLoader.getHeight()).findPath();
        System.out.println("Path found with " + path.size() + " points");

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int tileX = e.getX() / 40;
                int tileY = e.getY() / 40;
                int px = tileX * 40 + 20;
                int py = tileY * 40 + 20;

                // Don't allow turret on path tiles
                if (TileMapper.GetTile(rawMap[tileX][tileY]).isPath()) {
                    System.out.println("Cannot place turret on path!");
                    return;
                }

                // Don't allow stacking turrets
                for (TurretBase existing : gameManager.getTurrets()) {
                    if (Math.abs(existing.getX() - px) < 20 && Math.abs(existing.getY() - py) < 20) {
                        return;
                    }
                }

                // Add selected turret
                String selected = turretSelector.getSelectedItem().toString();
                TurretBase turret = switch (selected) {
                    case "Heavy" -> new HeavyTurret(px, py);
                    case "Rapid" -> new RapidFireTurret(px, py);
                    case "Sniper" -> new SniperTurret(px, py);
                    default -> new BasicTurret(px, py);
                };

                gameManager.addTurret(turret);
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                int mx = e.getX();
                int my = e.getY();

                for (TurretBase turret : gameManager.getTurrets()) {
                    int tx = turret.getX();
                    int ty = turret.getY();
                    boolean hovered = Math.abs(mx - tx) < 20 && Math.abs(my - ty) < 20;
                    turret.setHovered(hovered);
                }

                repaint();
            }
        });
    }

    public void setTurretSelector(JComboBox<String> selector) {
        this.turretSelector = selector;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (Tile[] tileRow : tiles) {
            for (Tile tile : tileRow) {
                tile.draw(g);
            }
        }

        if (path != null && !path.isEmpty()) {
            g.setColor(Color.YELLOW);
            for (Point p : path) {
                g.fillOval(p.x * 40 + 15, p.y * 40 + 15, 10, 10);
            }
        }

        for (Enemy enemy : gameManager.getEnemies()) {
            enemy.draw(g);
        }

        for (TurretBase turret : gameManager.getTurrets()) {
            Color color = switch (turret.getName()) {
                case "Heavy Turret" -> Color.RED;
                case "Rapid-Fire Turret" -> Color.ORANGE;
                case "Sniper Turret" -> Color.MAGENTA;
                default -> Color.BLUE;
            };
            turret.draw(g, color);
        }

        drawHUD(g);
    }

    private void drawHUD(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, 250, 50);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 14));
        g.drawString("Base Health: " + gameManager.getBaseHealth(), 10, 20);
        g.drawString("Wave: " + gameManager.getWaveCount(), 10, 40);

        int messageX = 90;
        int messageY = 105;
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.setColor(Color.RED);

        if (gameManager.isFirstWave()) {
            g.drawString("First Wave...", messageX, messageY);
        } else if (gameManager.isWaitingForNextWave()) {
            int timeLeft = gameManager.getCountdownTimeLeft();
            g.drawString("Next wave in: " + timeLeft + "s", messageX, messageY);
        }
    }
}
