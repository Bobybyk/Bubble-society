package entity;

import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import java.util.Random;

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

    public void update(float delta, Window window, Camera camera, World world, Vector3f function) {
       /* int vect = new Random().nextInt(4);
        switch(vect) {
            case 0: transform.pos.add(new Vector3f(-5*delta, 0, 0)); break;
            case 1: transform.pos.add(new Vector3f(5*delta, 0, 0)); break;
            case 2: transform.pos.add(new Vector3f(0, 5*delta, 0)); break;
            case 3: transform.pos.add(new Vector3f(0, -5*delta, 0)); break;
        } */

        // follows a trend (passed by function to transform position)
        if (function != null) {
            transform.pos.add(function);
        }
            
        if (window.getInput().isKeyDown(GLFW.GLFW_KEY_LEFT)) {
            transform.pos.add(new Vector3f(-5*delta, 0, 0));
        }
        if (window.getInput().isKeyDown(GLFW.GLFW_KEY_RIGHT)) {
            transform.pos.add(new Vector3f(5*delta, 0, 0));
        }
        if (window.getInput().isKeyDown(GLFW.GLFW_KEY_UP)) {
            transform.pos.add(new Vector3f(0, 5*delta, 0));
        }
        if (window.getInput().isKeyDown(GLFW.GLFW_KEY_DOWN)) {
            transform.pos.add(new Vector3f(0, -5*delta, 0));
        }
        



        //to follow the worker
        camera.setPosition(transform.pos.mul(-world.getScale(), new Vector3f()));
    }

    public void render(Shader shader, Camera camera) {
        shader.bind();
        shader.setUniform("sampler", 0);
        shader.setUniform("projection", transform.getProjection(camera.getProjection()));
        texture.bind(0);
        modelTexture.render();
    }
}
