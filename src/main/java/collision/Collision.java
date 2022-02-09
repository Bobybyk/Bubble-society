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
    public Vector2f distance;
    public boolean isIntersecting;
    
    public Collision(Vector2f distance, boolean intersects) {
        this.distance = distance;
        this.isIntersecting = intersects;
    }
} 
