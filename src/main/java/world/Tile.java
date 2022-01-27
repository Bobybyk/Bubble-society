package world; 

public class Tile {
    public static Tile tiles[] = new Tile[16];

    public static final Tile tile = new Tile((byte) 0, "background");

    private byte id;
    private String texture;

    public Tile(byte id, String texture) {
        this.id = id;
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
