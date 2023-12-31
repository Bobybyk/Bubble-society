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
import io.Window;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import render.Animation;
import render.Camera;
import render.Shader;
import world.World;

public abstract class Entity {
    /** bounding boxes of the entity */
    protected AABB boudingBoxes;
    /** animations of the entity */
    protected Animation[] animations;
    /** transform of the entity */
    protected Transform transform;
    /** index of the animation to use */
    private int useAnimation;

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
     * @param direction direction to move
     */
    public void move(Vector2f direction) {
        transform.pos.add(new Vector3f(direction, 0));
        boudingBoxes.getCenter().set(transform.pos.x, transform.pos.y);
    }

    /**
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

    public abstract void update(float delta, Window window, Camera camera, World world);

    public abstract void wanderUpdate(float delta, ShiftingVector coords);

    public abstract void deathUpdate();

    public abstract void changeCameraMod();

    public abstract void setWorker(Worker worker);

    /**
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

    public boolean getCycle(int animConversion) {
        return false;
    }

    public Transform getTransform() {
        return transform;
    }
}
