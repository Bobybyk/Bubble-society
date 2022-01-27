package world; 

public class Tile {
    public static Tile tiles[] = new Tile[16];
    public static byte numberOfTiles = 0;

    public static final Tile backgroundTile = new Tile("background");
    public static final Tile markerTile = new Tile("UI_button_clicked");

    private byte id;
    private String texture;

    public Tile(String texture) {
        this.id = numberOfTiles;
        numberOfTiles++;
        this.texture = texture;
        if (tiles[id] != null) {
            throw new IllegalStateException("Tiles at: [" + id + "] is already being used...");
        }
        tiles[id] = this;
    }

    public byte getId() {
        return this.id;
    }
    public String getTexture() {
        return this.texture;
    }

}
