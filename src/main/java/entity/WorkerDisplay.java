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
package entity;

import org.joml.Vector2f;
import org.joml.Vector3f;

import io.Window;
import render.Animation;
import render.Camera;
import world.World;

import org.lwjgl.glfw.GLFW;


public class WorkerDisplay extends Entity {
    public static final int ANIM_IDLE = 0;
    public static final int ANIM_MOVE = 1;
    public static final int ANIM_SIZE = 2;

    private static Animation idle = new Animation(20, 9, "follower/idle");
    private static Animation movment = new Animation(15, 8, "follower/movement");

    private boolean cameraOnWorker;

    public WorkerDisplay(Transform transform) {
        super(ANIM_SIZE, transform);
        setAnimation(ANIM_IDLE, idle); // Animation(number of frames, fps, name without id)
        setAnimation(ANIM_MOVE, movment);
    }


    @Override
    public void wanderUpdate(float delta, Double[] coords) {
        Vector2f movement = new Vector2f();

        movement.add((int)(double)coords[0]*delta, (int)(double)coords[1]*delta);

        move(movement);

        if (movement.x != 0 || movement.y != 0) {
            useAnimation(ANIM_MOVE);
        } else {
            useAnimation(ANIM_IDLE);
        }
        
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

        if (movement.x != 0 || movement.y != 0) {
            useAnimation(ANIM_MOVE);
        } else {
            useAnimation(ANIM_IDLE);
        }

        followWorker(world, camera);
        
    }

    public void followWorker(World world, Camera camera) {
        if (cameraOnWorker) {
            camera.getPosition().lerp(transform.pos.mul(-world.getScale(), new Vector3f()), 0.01f);
        }
    }

    public void changeCameraMod() {
        if(cameraOnWorker) {
            cameraOnWorker = false;
        } else {
            cameraOnWorker = true;
        }
    }

}