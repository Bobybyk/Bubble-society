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

import java.util.HashMap;

import org.joml.Vector2f;
import org.joml.Vector3f;

import io.Window;
import render.Animation;
import render.Camera;
import render.TextureLoader;
import world.World;

import org.lwjgl.glfw.GLFW;

import game.Game;
import game.worker.Worker;


public class FollowerDisplay extends Entity {
    public static final int ANIM_IDLE = 0;
    public static final int ANIM_MOVE = 1;
    public static final int ANIM_DYING = 2;
    public static final int ANIM_DEAD = 3;
    public static final int ANIM_CONVERSION = 4;
    public static final int ANIM_SIZE = 5;

    private HashMap<Integer, Animation> animationBindId = new HashMap<Integer, Animation>();

    private static TextureLoader idleTexures = new TextureLoader(20, "follower/idle"); // Animation(number of frames, fps, name without id)
    private static TextureLoader movmentTexures = new TextureLoader(15, "follower/movement");
    private static TextureLoader dyingTexures = new TextureLoader(20, "follower/dying");
    private static TextureLoader deadTexures = new TextureLoader(1, "follower/dead");
    private static TextureLoader conversionTexures = new TextureLoader(20, "follower/conversion");

    private Animation idle;
    private Animation movment;
    private Animation dying;
    private Animation dead;
    private Animation conversion;

    private boolean cameraOnWorker;
    private Worker worker;

    
    public FollowerDisplay(Transform transform) {
        super(ANIM_SIZE, transform);
        this.idle = new Animation(9, idleTexures);
        this.movment = new Animation(8, movmentTexures);
        this.dying = new Animation(10, dyingTexures);
        this.dead = new Animation(1, deadTexures);
        this.conversion = new Animation(9, conversionTexures);

        setAnimation(ANIM_IDLE, idle); 
        setAnimation(ANIM_MOVE, movment);
        setAnimation(ANIM_DYING, dying);
        setAnimation(ANIM_DEAD, dead);
        setAnimation(ANIM_CONVERSION, conversion);

        animationBindId.put(ANIM_IDLE, idle);
        animationBindId.put(ANIM_MOVE, movment);
        animationBindId.put(ANIM_DYING, dying);
        animationBindId.put(ANIM_DEAD, dead);
        animationBindId.put(ANIM_CONVERSION, conversion);
    }


    @Override
    public void wanderUpdate(float delta, Double[] coords) {
        Vector2f movement = new Vector2f();

        movement.add((int)(double)coords[0]*delta, (int)(double)coords[1]*delta);
        move(movement);

        casualAnimUpdate(movement);
    }

    public void casualAnimUpdate(Vector2f movement) {
        if (movement.x != 0 || movement.y != 0) {
            useAnimation(ANIM_MOVE);
        } else {
            useAnimation(ANIM_IDLE);
        }
    }

    @Override
    public void deathUpdate() {
        if (dying.hasMadeACycle()) {
            useAnimation(ANIM_DEAD);
        } else {
            useAnimation(ANIM_DYING);
        }   
    }

    public void conversionUpdate() {
        useAnimation(ANIM_CONVERSION);
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

    @Override
    public void changeCameraMod() {
        if(cameraOnWorker) {
            cameraOnWorker = false;
        } else {
            cameraOnWorker = true;
        }
    }

    public boolean getCycle(int id) {
        return animationBindId.get(id).hasMadeACycle();
    }

    public void setWorker(Worker worker) {
        this.worker = worker;
    }

}