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
package gui;

import assets.Assets;
import io.NewWindow;
import org.joml.Matrix4f;
import render.Camera;
import render.Shader;
import render.TileSheet;

/** Interface d'intéraction pour l'utilisateur (sélectionner un item, etc.) */
public class Gui {
    private Shader shader;
    private Camera camera;
    private TileSheet sheet;

    public Gui(NewWindow window) {
        this.shader = new Shader("gui");
        this.camera = new Camera(window.getWidth(), window.getHeight());
        this.sheet = new TileSheet("sheet_test_3d.png", 3);
    }

    public void resizeCamera(NewWindow window) {
        camera.setProjection(window.getWidth(), window.getHeight());
    }

    public void render() {
        Matrix4f matrix = new Matrix4f();
        camera.getProjection().scale(87, matrix);
        matrix.translate(-3, -3, 0);
        shader.bind();
        shader.setUniform("projection", matrix);
        // tile sheet dimensions
        sheet.bindTile(shader, 5);
        Assets.getModel().render();
    }
}
