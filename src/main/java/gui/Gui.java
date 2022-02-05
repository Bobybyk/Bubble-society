package gui;

import org.joml.Matrix4f;
import org.joml.Vector4f;

import assets.Assets;
import render.Camera;
import render.Shader;

public class Gui {
    private Shader shader;

    public Gui() {
        this.shader = new Shader("gui");
    }

    public void render(Camera camera) {
        Matrix4f matrix = new Matrix4f();
        camera.getUntransformedProjection().scale(87, matrix);
        matrix.translate(-3, -3, 0);
        shader.bind();
        shader.setUniform("projection", matrix);
        shader.setUniform("color", new Vector4f(0, 0, 0, 0.4f));
        Assets.getModel().render();
    }
}
