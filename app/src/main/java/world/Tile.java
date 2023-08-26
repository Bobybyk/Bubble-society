/*
 *
 * Copyright (c) 2022 Matthieu Le Franc
 *
 * You are prohibited from sharing and distributing this creation without our prior authorization, more specifically:
 *
 * TO PROVIDE A COPY OF OUR GAME TO ANY THIRD PARTY;
 * TO USE THIS CREATION FOR COMMERCIAL PURPOSES;
 * TO USE THIS CREATIONS FOR PROFIT;
 * TO ALLOW ANY THIRD PARTY TO ACCESS TO THIS CREATION IN AN UNFAIR OR ABUSIVE MANNER;
 *
 */
package world;

public class Tile {
    public static Tile tiles[] = new Tile[255];
    public static byte numberOfTiles = 0;

    public static final Tile backgroundTile = new Tile("background");
    public static final Tile zoneTile = new Tile("zone_border").setSolid();

    private byte id;
    private boolean solid;
    private String texture;

    /**
     * create a tile
     *
     * @param texture texture of the tile
     */
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

    /**
     * set the tile as solid
     *
     * @return the tile
     */
    public Tile setSolid() {
        this.solid = true;
        return this;
    }

    /**
     * check if the tile is solid
     *
     * @return true if the tile is solid, false otherwise
     */
    public boolean isSolid() {
        return solid;
    }

    /**
     * get the id of the tile
     *
     * @return id of the tile
     */
    public byte getId() {
        return this.id;
    }

    /**
     * get the texture of the tile
     *
     * @return texture of the tile
     */
    public String getTexture() {
        return this.texture;
    }
}
