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

import java.util.HashMap;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import render.Camera;
import render.Shader;
import render.Texture;
import render.VBO;

public class TileRenderer {
    private HashMap<String, Texture> tileTextures;
    private VBO modelTexture;

    /**
     * @brief create a tile renderer and load all the textures
     */
    public TileRenderer() {
        tileTextures = new HashMap<String, Texture>();

        float[] vertices =
                new float[] {
                    -1f, 1f, 0, // TOP LEFT       0
                    1f, 1f, 0, // TOP RIGHT      1
                    1f, -1f, 0, // BOTTOM RIGHT   2
                    -1f, -1f, 0, // BOTTOM LEFT   3
                };

        float[] texture =
                new float[] {
                    0, 0,
                    1, 0,
                    1, 1,
                    0, 1,
                };

        int[] indices =
                new int[] {
                    0, 1, 2,
                    2, 3, 0
                };

        modelTexture = new VBO(vertices, texture, indices);

        for (int i = 0; i < Tile.tiles.length; i++) {
            if (Tile.tiles[i] != null) {
                if (!tileTextures.containsKey(Tile.tiles[i].getTexture())) {
                    String tex = Tile.tiles[i].getTexture();
                    tileTextures.put(tex, new Texture(tex + ".png"));
                }
            }
        }
    }

    /**
     * @brief render a tile at a specific position with a specific shader
     * @param tile tile to render
     * @param x x position of the tile
     * @param y y position of the tile
     * @param shader shader to use
     * @param world world matrix
     * @param cam camera
     */
    public void renderTile(Tile tile, int x, int y, Shader shader, Matrix4f world, Camera cam) {
        shader.bind();
        if (tileTextures.containsKey(tile.getTexture())) {
            tileTextures.get(tile.getTexture()).bind(0);
        }

        Matrix4f tilePosition = new Matrix4f().translate(new Vector3f(x * 2, y * 2, 0));
        Matrix4f target = new Matrix4f();

        cam.getProjection().mul(world, target);
        target.mul(tilePosition);

        shader.setUniform("sampler", 0);
        shader.setUniform("projection", target);

        modelTexture.render();
    }
}
