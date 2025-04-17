package Utility;

public class GameSettings {
    // Default tile size
    private static int TILE_SIZE = 60;
    
    // Getters and setters
    public static int getTileSize() {
        return TILE_SIZE;
    }
    
    public static void setTileSize(int size) {
        // TILE_SIZE = size;
    }
    
    // Utility methods
    public static int scaled(int value) {
        return value * TILE_SIZE / 60;
    }
    
    // Convert screen coordinates to tile coordinates
    public static int screenToTile(int screenCoord) {
        return screenCoord / TILE_SIZE;
    }
    
    // Convert tile coordinates to screen coordinates (centered in tile)
    public static int tileToScreenCentered(int tileCoord) {
        return tileCoord * TILE_SIZE + (TILE_SIZE / 2);
    }
    
    // Convert tile coordinates to screen coordinates (top-left of tile)
    public static int tileToScreen(int tileCoord) {
        return tileCoord * TILE_SIZE;
    }
} 