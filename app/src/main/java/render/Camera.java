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

    /**
     * @brief create a camera
     * @param width
     * @param height
     */
    public Camera(int width, int height) {
        this.position = new Vector3f(0, 0, 0);
        setProjection(width, height);
    }

    /**
     * @brief set the projection of the camera
     * @param width
     * @param height
     */
    public void setProjection(int width, int height) {
        this.projection = new Matrix4f().setOrtho2D(-width / 2, width / 2, -height / 2, height / 2);
    }

    /**
     * @brief set the position of the camera with a vector position
     * @param position vector position
     */
    public void setPosition(Vector3f position) {
        this.position = position;
    }

    /**
     * @brief set the position of the camera with a x, y, z position
     * @param position x, y, z position
     */
    public void addPosition(Vector3f position) {
        this.position.add(position);
    }

    /**
     * @brief get the current camera position
     * @return current camera position as a vector
     */
    public Vector3f getPosition() {
        return this.position;
    }

    /**
     * @brief get the current camera projection
     * @return current camera projection as a matrix
     */
    public Matrix4f getUntransformedProjection() {
        return projection;
    }

    /**
     * @brief get the current camera projection translated by the camera position
     * @return current camera projection as a matrix
     */
    public Matrix4f getProjection() {
        return projection.translate(position, new Matrix4f());
    }
}
