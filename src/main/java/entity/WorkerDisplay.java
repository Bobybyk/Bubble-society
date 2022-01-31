package entity;

import org.joml.Vector2f;
import org.joml.Vector3f;

import io.Window;
import render.Animation;
import render.Camera;
import world.World;

import org.lwjgl.glfw.GLFW;

public class WorkerDisplay extends Entity {

    public WorkerDisplay(Transform transform) {
        super(new Animation(10, 6, "follower"), transform);// Animation(number of frames, fps, name without id)
    }

    @Override
    public void update(float delta, Window window, Camera camera, World world) {
        Vector2f movement = new Vector2f();

        if (window.getInput().isKeyDown(GLFW.GLFW_KEY_LEFT)) {
            movement.add(-20*delta, 0);
        }
        if (window.getInput().isKeyDown(GLFW.GLFW_KEY_RIGHT)) {
            movement.add(20*delta, 0);
        }
        if (window.getInput().isKeyDown(GLFW.GLFW_KEY_UP)) {
            movement.add(0, 20*delta);
        }
        if (window.getInput().isKeyDown(GLFW.GLFW_KEY_DOWN)) {
            movement.add(0, -20*delta);
        }

        move(movement);
        
        //to follow the worker (comment it to be able to move the camera)
        camera.getPosition().lerp(transform.pos.mul(-world.getScale(), new Vector3f()), 0.05f); //decrease the float value to have even more smoother camera following the object
    }
}