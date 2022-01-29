package world; 

public class Tile {
    public static Tile tiles[] = new Tile[16];
    public static byte numberOfTiles = 0;

    public static final Tile backgroundTile = new Tile("background");
    public static final Tile markerTile = new Tile("UI_button_clicked").setSolid();

    private byte id;
    private boolean solid;
    private String texture;

    public Tile(String texture) {
        this.id = numberOfTiles;
        numberOfTiles++;
        this.texture = texture;
        this.solid = false;
        if (tiles[id] != null) {
            throw new IllegalStateException("Tiles at: [" + id + "] is already being used...");
        }
        tiles[id] = this;
    }

    public Tile setSolid() {
        this.solid = true;
        return this;
    }

    public boolean isSolid() {
        return solid;
    }
    public byte getId() {
        return this.id;
    }
    public String getTexture() {
        return this.texture;
    }

}
