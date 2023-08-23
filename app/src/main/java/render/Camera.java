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
package render;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Camera {
    private Vector3f position;
    private Matrix4f projection;

    public Camera(int width, int height) {
        this.position = new Vector3f(0, 0, 0);
        setProjection(width, height);
    }

    public void setProjection(int width, int height) {
        this.projection = new Matrix4f().setOrtho2D(-width / 2, width / 2, -height / 2, height / 2);
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public void addPosition(Vector3f position) {
        this.position.add(position);
    }

    public Vector3f getPosition() {
        return this.position;
    }

    public Matrix4f getUntransformedProjection() {
        return projection;
    }

    public Matrix4f getProjection() {
        return projection.translate(position, new Matrix4f());
    }
}
