package entity;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import collision.AABB;
import collision.Collision;

import java.util.Random;

import io.Window;
import render.Animation;
import render.Camera;
import render.Shader;
import render.VBO;
import world.World;
import render.Texture;

public class WorkerDisplay {
    private VBO modelTexture;
    private AABB boudingBoxes;
    //private Texture texture;
    private Animation texture;
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
        this.texture = new Animation(10, 6, "follower"); // Animation(number of frames, fps, name without id)
        this.transform = new Transform();
        this.transform.scale = new Vector3f(16, 16, 1);
        this.boudingBoxes = new AABB(new Vector2f(transform.pos.x, transform.pos.y), new Vector2f(1, 1));
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
        /* if (function != null) {
            transform.pos.add(function);
        } */
            
        if (window.getInput().isKeyDown(GLFW.GLFW_KEY_LEFT)) {
            transform.pos.add(new Vector3f(-10*delta, 0, 0));
        }
        if (window.getInput().isKeyDown(GLFW.GLFW_KEY_RIGHT)) {
            transform.pos.add(new Vector3f(10*delta, 0, 0));
        }
        if (window.getInput().isKeyDown(GLFW.GLFW_KEY_UP)) {
            transform.pos.add(new Vector3f(0, 10*delta, 0));
        }
        if (window.getInput().isKeyDown(GLFW.GLFW_KEY_DOWN)) {
            transform.pos.add(new Vector3f(0, -10*delta, 0));
        }
        
        boudingBoxes.getCenter().set(transform.pos.x, transform.pos.y);

        //set hitboxe : 5*5 around
        AABB[] boxes = new AABB[25];
        for (int i = 0 ; i < 5 ; i++) {
            for (int j = 0 ; j < 5 ; j++) {
                boxes[i + j * 5] = world.getTileBoundingBox(
                    (int)(((transform.pos.x / 2) + 0.5f) - (5/2)) + i,
                    (int)(((-transform.pos.y / 2) + 0.5f) - (5/2)) + j);
            }
        }

        // START OF UNCLIPING SYSTEM
        AABB box = null;
        for (int i = 0 ; i < boxes.length ; i++) {
            if (boxes[i] != null) {
                if (box == null) {
                    box = boxes[i];
                }
                Vector2f length1 = box.getCenter().sub(transform.pos.x, transform.pos.y, new Vector2f());
                Vector2f length2 = boxes[i].getCenter().sub(transform.pos.x, transform.pos.y, new Vector2f());

                if (length1.lengthSquared() > length2.lengthSquared()) {
                    box = boxes[i];
                }
            }
        }
        if (box != null) {
            Collision data = boudingBoxes.getCollision(box);
            if (data.isIntersecting) {
                boudingBoxes.correctPosition(box, data);
                transform.pos.set(boudingBoxes.getCenter(), 0);
            }

            for (int i = 0 ; i < boxes.length ; i++) {
                if (boxes[i] != null) {
                    if (box == null) {
                        box = boxes[i];
                    }
                    Vector2f length1 = box.getCenter().sub(transform.pos.x, transform.pos.y, new Vector2f());
                    Vector2f length2 = boxes[i].getCenter().sub(transform.pos.x, transform.pos.y, new Vector2f());
    
                    if (length1.lengthSquared() > length2.lengthSquared()) {
                        box = boxes[i];
                    }
                }
            }
            data = boudingBoxes.getCollision(box);
            if (data.isIntersecting) {
                boudingBoxes.correctPosition(box, data);
                transform.pos.set(boudingBoxes.getCenter(), 0);
            }
        } // END OF UNCLIPING SYSTEM

        //to follow the worker (comment it to be able to move the camera)
        camera.getPosition().lerp(transform.pos.mul(-world.getScale(), new Vector3f()), 0.05f); //decrease the float value to have even more smoother camera following the object
    }

    public void render(Shader shader, Camera camera) {
        shader.bind();
        shader.setUniform("sampler", 0);
        shader.setUniform("projection", transform.getProjection(camera.getProjection()));
        texture.bind(0);
        modelTexture.render();
    }
}
