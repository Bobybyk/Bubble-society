package entity;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;


import collision.AABB;
import collision.Collision;

import io.Window;
import render.Animation;
import render.Camera;
import render.Shader;
import render.VBO;
import world.World;

public abstract class Entity {
    private static VBO modelTexture;
    protected AABB boudingBoxes;
    //private Texture texture;
    protected Animation[] animations;
    protected Transform transform;
    private int useAnimation;

    public Entity(int maxAnimations, Transform transform) {
        this.animations = new Animation[maxAnimations];
        this.transform = transform;
        this.useAnimation = 0;
        this.boudingBoxes = new AABB(new Vector2f(transform.pos.x, transform.pos.y), new Vector2f(transform.scale.x, transform.scale.y));
    }

    protected void setAnimation(int index, Animation animation) {
        animations[index] = animation;
    }

    public void useAnimation(int index) {
        this.useAnimation = index;
    }

    public void move(Vector2f direction) {
        transform.pos.add(new Vector3f(direction, 0));
        boudingBoxes.getCenter().set(transform.pos.x, transform.pos.y);
    }

    public void collideWithTiles(World world) {
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
    }

    public abstract void update(float delta, Window window, Camera camera, World world);

    public void render(Shader shader, Camera camera, World world) {
        Matrix4f target = camera.getProjection();
        target.mul(world.getWorldMatrix4f());
        shader.bind();
        shader.setUniform("sampler", 0);
        shader.setUniform("projection", transform.getProjection(target));
        animations[useAnimation].bind(0);
        modelTexture.render();
    }

    public static void initAsset() {
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

        modelTexture = new VBO(vertices, texture, indices);
    }

    public static void deleteAsset() {
        modelTexture = null;
    }

    public void collideWithEntity(Entity entity) {
        Collision collision = boudingBoxes.getCollision(entity.boudingBoxes);
        if (collision.isIntersecting) {
            collision.distance.x /= 2;
            collision.distance.y /= 2;
            boudingBoxes.correctPosition(entity.boudingBoxes, collision);
            transform.pos.set(boudingBoxes.getCenter().x, boudingBoxes.getCenter().y, 0);
            entity.boudingBoxes.correctPosition(boudingBoxes, collision);
            entity.transform.pos.set(entity.boudingBoxes.getCenter().x, entity.boudingBoxes.getCenter().y, 0);
        }
    }
}

