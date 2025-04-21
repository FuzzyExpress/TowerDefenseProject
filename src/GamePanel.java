import Entity.Enemy;
import Map.MapLoader;
import Map.PathFinder;
import Map.Tile;
import Map.TileMapper;
import Turret.*;
import Utility.GameSettings;

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
                int tileX = GameSettings.screenToTile(e.getX());
                int tileY = GameSettings.screenToTile(e.getY());
                int px = GameSettings.tileToScreenCentered(tileX);
                int py = GameSettings.tileToScreenCentered(tileY);

                // Don't allow turret on path tiles
                if (TileMapper.GetTile(rawMap[tileX][tileY]).isPath()) {
                    System.out.println("Cannot place turret on path!");
                    return;
                }

                // Don't allow stacking turrets
                for (TurretBase existing : gameManager.getTurrets()) {
                    if (Math.abs(existing.getX() - px) < GameSettings.getTileSize()/2 && 
                        Math.abs(existing.getY() - py) < GameSettings.getTileSize()/2) {
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

                // Attempt to place using ScoreManager
                if (!gameManager.tryPlaceTurret(turret)) {
                    JOptionPane.showMessageDialog(GamePanel.this,
                            "Not enough points to place a " + turret.getName() +
                                    " (" + turret.getCost() + " pts)",
                            "Insufficient Points",
                            JOptionPane.WARNING_MESSAGE);
                }

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
                    int hoverDistance = GameSettings.getTileSize()/2;
                    boolean hovered = Math.abs(mx - tx) < hoverDistance && Math.abs(my - ty) < hoverDistance;
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
        setBackground(Color.lightGray);
        for (Tile[] tileRow : tiles) {
            for (Tile tile : tileRow) {
                tile.draw(g);
            }
        }

        // if (path != null && !path.isEmpty()) {
        //     g.setColor(Color.YELLOW);
        //     for (Point p : path) {
        //         int dotSize = GameSettings.scaled(10);
        //         int dotOffset = GameSettings.scaled(15);
        //         g.fillOval(
        //             GameSettings.tileToScreen(p.x) + dotOffset, 
        //             GameSettings.tileToScreen(p.y) + dotOffset, 
        //             dotSize, dotSize
        //         );
        //     }
        // }

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
        g.fillRect(0, 0, GameSettings.scaled(250), GameSettings.scaled(50));

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 14));
        g.drawString("Base Health: " + gameManager.getBaseHealth(), 10, 20);
        g.drawString("Wave: " + gameManager.getWaveCount(), 10, 40);
        g.drawString("Points: " + gameManager.getScoreManager().getScore(), 150, 40);

        // Tower Table
        int Y =500;
        int X =5;
        g.setFont(new Font("Arial", Font.PLAIN, 12));
        g.setColor(Color.RED);
        g.drawString("Turret Costs:", X, Y);
        g.drawString("Basic: 800", X, Y + 15);
        g.drawString("Heavy: 1200", X, Y + 30);
        g.drawString("Rapid: 1000", X, Y + 45);
        g.drawString("Sniper: 2500", X, Y + 60);


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
