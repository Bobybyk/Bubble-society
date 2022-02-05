package gui;

import org.joml.Matrix4f;
import org.joml.Vector4f;

import assets.Assets;
import io.Window;
import render.Camera;
import render.Shader;

public class Gui {
    private Shader shader;
    private Camera camera;

    public Gui(Window window) {
        this.shader = new Shader("gui");
        camera = new Camera(window.getWidth(), window.getHeight());
    }

    public void resizeCamera(Window window) {
        camera.setProjection(window.getWidth(), window.getHeight());
    }

    public void render() {
        Matrix4f matrix = new Matrix4f();
        camera.getProjection().scale(87, matrix);
        matrix.translate(-3, -3, 0);
        shader.bind();
        shader.setUniform("projection", matrix);
        shader.setUniform("color", new Vector4f(0, 0, 0, 0.4f));
        Assets.getModel().render();
    }
}
