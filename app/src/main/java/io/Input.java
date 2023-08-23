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
package io;

import static org.lwjgl.glfw.GLFW.*;

import java.util.Arrays;

public class Input {
    private long window;
    private boolean[] keys;

    public Input(long window) {
        this.window = window;
        this.keys = new boolean[GLFW_KEY_LAST];
        Arrays.fill(keys, false);
    }

    public boolean isKeyDown(int key) {
        return glfwGetKey(window, key) == GLFW_TRUE;
    }

    public boolean isMouseButtonDown(int button) {
        return glfwGetMouseButton(window, button) == GLFW_TRUE;
    }

    public boolean isKeyPressed(int key) {
        return (isKeyDown(key) && !keys[key]);
    }

    public boolean isKeyReleased(int key) {
        return (!isKeyDown(key) && keys[key]);
    }

    public boolean isMouseDown(int key) {
        return glfwGetMouseButton(window, key) == GLFW_TRUE;
    }

    public void update() {
        for (int i = 32; i < GLFW_KEY_LAST; i++) {
            keys[i] = isKeyDown(i);
        }
    }
}
