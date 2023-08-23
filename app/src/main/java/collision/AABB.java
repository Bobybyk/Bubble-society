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
package collision;

import org.joml.Vector2f;

public class AABB {
    /** center of the box */
    private Vector2f center;
    /** half size of the box */
    private Vector2f halfExtent;

    /**
     * AABB = Axis Aligned Bounding Box
     *
     * @param center center of the box
     * @param halfExtent half size of the box
     */
    public AABB(Vector2f center, Vector2f halfExtent) {
        this.center = center;
        this.halfExtent = halfExtent;
    }

    /**
     * @param box2 the other box to test collision against
     * @return collision data, null if no collision
     */
    public Collision getCollision(AABB box2) {
        Vector2f distance = box2.center.sub(center, new Vector2f());
        distance.x = (float) Math.abs(distance.x);
        distance.y = (float) Math.abs(distance.y);

        distance.sub(halfExtent.add(box2.halfExtent, new Vector2f()));

        return new Collision(distance, distance.x < 0 && distance.y < 0);
    }

    /**
     * @param box2 the other box to test collision against
     * @param data collision data from previous frame
     */
    public void correctPosition(AABB box2, Collision data) {
        Vector2f correction = box2.center.sub(center, new Vector2f());
        if (data.distance.x > data.distance.y) {
            if (correction.x > 0) {
                center.add(data.distance.x, 0);
            } else {
                center.add(-data.distance.x, 0);
            }
        } else {
            if (correction.y > 0) {
                center.add(0, data.distance.y);
            } else {
                center.add(0, -data.distance.y);
            }
        }
    }

    /**
     * @return center of the box
     */
    public Vector2f getCenter() {
        return center;
    }

    /**
     * @return half size of the box
     */
    public Vector2f getHalfExtent() {
        return halfExtent;
    }
}
