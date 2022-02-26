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

import org.joml.Matrix4f;
import org.joml.Vector3f;


public class Transform {
    public Vector3f pos;
    public Vector3f scale;

    public Transform() {
        this.pos = new Vector3f();
        this.scale = new Vector3f(1, 1, 1);
    }

    public Matrix4f getProjection(Matrix4f target) {
        target.translate(pos);
        target.scale(scale);
        return target;
    }

    public Vector3f getPosition() {
        return pos;
    }
}
