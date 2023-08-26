package entity;

import game.worker.Worker;
import io.Window;
import java.util.HashMap;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import render.Animation;
import render.Camera;
import render.TextureLoader;
import world.World;

public class InsurgentDisplay extends Entity {

    private HashMap<Integer, Animation> animationBindId = new HashMap<Integer, Animation>();

    private static TextureLoader idleTexures =
            new TextureLoader(20, "insurgent/idle"); // Animation(number of frames, fps, name without id)
    private static TextureLoader movmentTexures = new TextureLoader(20, "insurgent/movement");
    private static TextureLoader dyingTexures = new TextureLoader(21, "insurgent/dying");
    private static TextureLoader deadTexures = new TextureLoader(1, "insurgent/dead");

    private Animation idle;
    private Animation movment;
    private Animation dying;
    private Animation dead;

    private boolean cameraOnWorker;

    private Worker worker;

    public InsurgentDisplay(Transform transform) {
        super(ANIM_SIZE, transform);
        this.idle = new Animation(9, idleTexures);
        this.movment = new Animation(8, movmentTexures);
        this.dying = new Animation(10, dyingTexures);
        this.dead = new Animation(1, deadTexures);

        setAnimation(ANIM_IDLE, idle);
        setAnimation(ANIM_MOVE, movment);
        setAnimation(ANIM_DYING, dying);
        setAnimation(ANIM_DEAD, dead);

        animationBindId.put(ANIM_IDLE, idle);
        animationBindId.put(ANIM_MOVE, movment);
        animationBindId.put(ANIM_DYING, dying);
        animationBindId.put(ANIM_DEAD, dead);
    }

    @Override
    public void update(float delta, Window window, Camera camera, World world) {
        Vector2f movement = new Vector2f();

        if (window.getInput().isKeyDown(GLFW.GLFW_KEY_LEFT)) {
            movement.add(-20 * delta, 0);
        }
        if (window.getInput().isKeyDown(GLFW.GLFW_KEY_RIGHT)) {
            movement.add(20 * delta, 0);
        }
        if (window.getInput().isKeyDown(GLFW.GLFW_KEY_UP)) {
            movement.add(0, 20 * delta);
        }
        if (window.getInput().isKeyDown(GLFW.GLFW_KEY_DOWN)) {
            movement.add(0, -20 * delta);
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
    public void wanderUpdate(float delta, ShiftingVector coords) {
        Vector2f movement = new Vector2f();

        movement.add((int) (double) coords.getX() * delta, (int) (double) coords.getY() * delta);
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

    @Override
    public void changeCameraMod() {
        if (cameraOnWorker) {
            cameraOnWorker = false;
        } else {
            cameraOnWorker = true;
        }
    }

    public void setWorker(Worker worker) {
        this.worker = worker;
    }
}
