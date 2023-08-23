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
package assets;

import render.VBO;

public class Assets {
    private static VBO model;

    public static VBO getModel() {
        return model;
    }

    public static void initAsset() {
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

        model = new VBO(vertices, texture, indices);
    }

    public static void deleteAsset() {
        model = null;
    }
}
