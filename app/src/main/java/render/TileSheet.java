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
package render;

import org.joml.Matrix4f;

public class TileSheet {
    private Texture texture;

    private Matrix4f scale;
    private Matrix4f translation;

    private int amountOfTiles;

    public TileSheet(String texture, int amountOfTiles) {
        this.texture = new Texture("sheets/" + texture);
        // up right divide by amountOfTiles
        this.scale = new Matrix4f().scale(1.0f / (float) amountOfTiles);
        this.translation = new Matrix4f();
        this.amountOfTiles = amountOfTiles;
    }

    public void bindTile(Shader shader, int x, int y) {
        scale.translate(x, y, 0, translation);

        shader.setUniform("sampler", 0);
        shader.setUniform("texModifier", translation);

        texture.bind(0);
    }

    public void bindTile(Shader shader, int tile) {
        int x = tile % amountOfTiles;
        int y = tile / amountOfTiles;
        bindTile(shader, x, y);
    }
}
