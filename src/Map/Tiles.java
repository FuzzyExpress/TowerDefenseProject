package Map;

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

    public boolean isPath() {
        return this == PATH;
    }
}