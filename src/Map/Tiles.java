package Map;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import Utility.GameSettings;

public enum Tiles {
    PATH,
    GRASS,
    TREES,
    WATER,
    SAND,
    STONE,
    SPAWNER,
    END,
    UNKNOWN,
    ;

    public boolean isPlaceable() {
        return !(this == PATH || this == SPAWNER || this == END || this == WATER || this == STONE);
    }

    public void draw(Graphics g, String heading, int x, int y) 
    {
        BufferedImage image;

        if (this == Tiles.UNKNOWN) {
            try {
                image = ImageIO.read(new File( "art/unknown/tile.png" ));
            } catch (IOException e) {
                System.err.println("Unknown Image: art/unknown/tile.png");
                return;
            }
        }

        String tileName = this.toString().toLowerCase();
        
        

        String path = "art/" + tileName + "/" + tileName + "-" + heading + ".png";

        try {
            image = ImageIO.read(new File( path ));
        } catch (IOException e) {
            System.err.println("Unknown Image: " + path);
            try {
                image = ImageIO.read(new File( "art/unknown/image.png" ));
            } catch (IOException E) {
                System.err.println("Unknown Image: art/unknown/image.png");
                return;
            }
        }
        
        int tileSize = GameSettings.getTileSize();
        g.drawImage(image, x * tileSize, y * tileSize, tileSize, tileSize, null);
    }
}