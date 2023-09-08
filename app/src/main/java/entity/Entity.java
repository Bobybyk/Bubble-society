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

import assets.Assets;
import collision.AABB;
import collision.Collision;
import game.worker.Worker;
import io.NewWindow;
import java.util.HashMap;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import render.Animation;
import render.Camera;
import render.Shader;
import world.World;

public abstract class Entity {
    /** id de l'animation de l'entité qui ne bouge pas */
    public static final int ANIM_IDLE = 0;
    /** id de l'Animation de l'entité qui bouge */
    public static final int ANIM_MOVE = 1;
    /** id de l'Animation de l'entité qui meurt */
    public static final int ANIM_DYING = 2;
    /** id de l'Animation de l'entité qui est morte */
    public static final int ANIM_DEAD = 3;
    /** id de l'Animation de l'entité qui est en train de se convertir */
    public static final int ANIM_CONVERSION = 4;
    /** id de l'Nombre d'animations */
    public static final int ANIM_SIZE = 5;

    /** Liste des animations */
    protected HashMap<Integer, Animation> animationBindId = new HashMap<Integer, Animation>();
    /** Animation de l'entité qui ne bouge pas */
    protected Animation idle;
    /** Animation de l'entité qui bouge */
    protected Animation movment;
    /** Animation de l'entité qui meurt */
    protected Animation dying;
    /** Animation de l'entité qui est morte */
    protected Animation dead;
    /** Animation de l'entité qui est en train de se convertir */
    protected Animation conversion;

    /** bounding boxes of the entity */
    protected AABB boudingBoxes;
    /** animations of the entity */
    protected Animation[] animations;
    /** transform of the entity */
    protected Transform transform;
    /** index of the animation to use */
    private int useAnimation;

    private boolean cameraOnWorker;

    private Worker worker;

    /**
     * @param maxAnimations maximum number of animations
     * @param transform transform of the entity
     */
    public Entity(int maxAnimations, Transform transform) {
        this.animations = new Animation[maxAnimations];
        this.transform = transform;
        this.useAnimation = 0;
        this.boudingBoxes =
                new AABB(
                        new Vector2f(transform.pos.x, transform.pos.y),
                        new Vector2f(transform.scale.x, transform.scale.y));
    }

    /**
     * @param index index of the animation
     * @param animation animation to set
     */
    protected void setAnimation(int index, Animation animation) {
        animations[index] = animation;
    }

    /**
     * @param index index of the animation
     */
    public void useAnimation(int index) {
        this.useAnimation = index;
    }

    /**
     * permet de déplacer l'entité dans une direction donnée
     *
     * @param direction direction to move
     */
    public void move(Vector2f direction) {
        transform.pos.add(new Vector3f(direction, 0));
        boudingBoxes.getCenter().set(transform.pos.x, transform.pos.y);
    }

    /**
     * permet de gérer les collisions avec les tiles
     *
     * @param world world to collide with
     */
    public void collideWithTiles(World world) {
        // set hitboxe : 5*5 around
        AABB[] boxes = new AABB[25];
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                boxes[i + j * 5] =
                        world.getTileBoundingBox(
                                (int) (((transform.pos.x / 2) + 0.5f) - (5 / 2)) + i,
                                (int) (((-transform.pos.y / 2) + 0.5f) - (5 / 2)) + j);
            }
        }

        // START OF UNCLIPING SYSTEM
        AABB box = null;
        for (int i = 0; i < boxes.length; i++) {
            if (boxes[i] != null) {
                if (box == null) {
                    box = boxes[i];
                }
                Vector2f length1 = box.getCenter().sub(transform.pos.x, transform.pos.y, new Vector2f());
                Vector2f length2 =
                        boxes[i].getCenter().sub(transform.pos.x, transform.pos.y, new Vector2f());

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

            for (int i = 0; i < boxes.length; i++) {
                if (boxes[i] != null) {
                    if (box == null) {
                        box = boxes[i];
                    }
                    Vector2f length1 = box.getCenter().sub(transform.pos.x, transform.pos.y, new Vector2f());
                    Vector2f length2 =
                            boxes[i].getCenter().sub(transform.pos.x, transform.pos.y, new Vector2f());

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
    }

    /**
     * permet de déplacer l'entité avec les touches du clavier
     *
     * @param delta
     * @param window
     * @param camera
     * @param world
     */
    public void update(float delta, NewWindow window, Camera camera, World world) {
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

    /**
     * permet de suivre l'entité avec la caméra
     *
     * @param world
     * @param camera
     */
    public void followWorker(World world, Camera camera) {
        if (cameraOnWorker) {
            camera.getPosition().lerp(transform.pos.mul(-world.getScale(), new Vector3f()), 0.01f);
        }
    }

    /** permet de changer le mode de la caméra (suivre ou non l'entité) */
    public void changeCameraMod() {
        if (cameraOnWorker) {
            cameraOnWorker = false;
        } else {
            cameraOnWorker = true;
        }
    }

    /**
     * change la direction de l'entité en fonction des coordonnées données
     *
     * @param delta temps écoulé depuis le dernier update
     * @param coords coordonnées de la direction à prendre
     */
    public void wanderUpdate(float delta, ShiftingVector coords) {
        Vector2f movement = new Vector2f();

        movement.add((int) (double) coords.getX() * delta, (int) (double) coords.getY() * delta);
        move(movement);

        casualAnimUpdate(movement);
    }

    /**
     * change l'animation de l'entité en fonction de son mouvement
     *
     * @param movement
     */
    public void casualAnimUpdate(Vector2f movement) {
        if (movement.x != 0 || movement.y != 0) {
            useAnimation(ANIM_MOVE);
        } else {
            useAnimation(ANIM_IDLE);
        }
    }

    /** change l'animation de l'entité en fonction de son état de vie */
    public void deathUpdate() {
        if (dying.hasMadeACycle()) {
            useAnimation(ANIM_DEAD);
        } else {
            useAnimation(ANIM_DYING);
        }
    }

    /** change l'animation de l'entité en fonction de son état de conversion */
    public void conversionUpdate() {
        useAnimation(ANIM_CONVERSION);
    }

    /**
     * permet de savoir si l'animation a fait un cycle
     *
     * @param id
     * @return true si l'animation a fait un cycle, false sinon
     */
    public boolean getCycle(int id) {
        return animationBindId.get(id).hasMadeACycle();
    }

    /**
     * permet de rendre graphiquement l'entité
     *
     * @param shader shader to use
     * @param camera camera to use
     * @param world world to use
     */
    public void render(Shader shader, Camera camera, World world) {
        Matrix4f target = camera.getProjection();
        target.mul(world.getWorldMatrix4f());
        shader.bind();
        shader.setUniform("sampler", 0);
        shader.setUniform("projection", transform.getProjection(target));
        animations[useAnimation].bind(0);
        Assets.getModel().render();
    }

    /**
     * @biref permet de gérer les collisions entre entités
     * @param entity entity to collide with
     */
    public void collideWithEntity(Entity entity) {
        Collision collision = boudingBoxes.getCollision(entity.boudingBoxes);
        if (collision.isIntersecting) {
            collision.distance.x /= 2;
            collision.distance.y /= 2;
            boudingBoxes.correctPosition(entity.boudingBoxes, collision);
            transform.pos.set(boudingBoxes.getCenter().x, boudingBoxes.getCenter().y, 0);
            entity.boudingBoxes.correctPosition(boudingBoxes, collision);
            entity.transform.pos.set(
                    entity.boudingBoxes.getCenter().x, entity.boudingBoxes.getCenter().y, 0);
        }
    }

    public Transform getTransform() {
        return transform;
    }

    public void setWorker(Worker worker) {
        this.worker = worker;
    }
}
