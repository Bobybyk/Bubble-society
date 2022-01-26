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

    public void update() {
        for(int i = 32 ; i<GLFW_KEY_LAST ; i++) {    
            keys[i] = isKeyDown(i);
        }
    }
}
