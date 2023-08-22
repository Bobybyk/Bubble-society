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

public class Collision {
    /**
     * distance between the two boxes
     */
    public Vector2f distance;
    /**
     * true if the boxes are intersecting
     */
    public boolean isIntersecting;

    /**
     * @param distance   distance between the two boxes
     * @param intersects true if the boxes are intersecting
     */
    public Collision(Vector2f distance, boolean intersects) {
        this.distance = distance;
        this.isIntersecting = intersects;
    }
}
