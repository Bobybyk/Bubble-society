package entity;

import org.joml.Vector3f;

import io.Window;
import render.Camera;
import render.Shader;
import render.VBO;
import world.World;
import render.Texture;

public class WorkerDisplay {
    private VBO modelTexture;
    private Texture texture;
    private Transform transform;

    public WorkerDisplay() {
        float[] vertices = new float[] {
			-1f, 1f, 0, // TOP LEFT       0
			1f, 1f, 0,	// TOP RIGHT      1
			1f, -1f, 0, // BOTTOM RIGHT   2
			-1f, -1f, 0, // BOTTOM LEFT   3
		};

		float[] texture = new float[] {
			0, 0,
			1, 0,
			1, 1,
			0, 1,
		};

		int[] indices = new int[] {
			0,1,2,
			2,3,0
		};

        this.modelTexture = new VBO(vertices, texture, indices);
        this.texture = new Texture("follower.png");
        this.transform = new Transform();
        this.transform.scale = new Vector3f(16, 16, 1);
    }

    public void update(float delta, Window window, Camera camera, World world) {

    }

    public void render(Shader shader, Camera camera) {
        shader.bind();
        shader.setUniform("sampler", 0);
        shader.setUniform("projection", transform.getProjection(camera.getProjection()));
        texture.bind(0);
        modelTexture.render();
    }
}
