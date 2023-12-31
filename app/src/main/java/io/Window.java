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

import java.nio.DoubleBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWWindowSizeCallback;

public class Window {
    private long window;
    private int width, height;
    private boolean fullscreen;
    private boolean hasResized;
    private GLFWWindowSizeCallback windowSizeCallback;
    private Input input;

    public static void setCallBacks() {
        glfwSetErrorCallback(
                new GLFWErrorCallback() {
                    @Override
                    public void invoke(int error, long description) {
                        throw new IllegalStateException(GLFWErrorCallback.getDescription(description));
                    }
                });
    }

    private void setLocalCallbacks() {
        windowSizeCallback =
                new GLFWWindowSizeCallback() {
                    @Override
                    public void invoke(long argWindow, int argWidth, int argHeight) {
                        width = argWidth;
                        height = argHeight;
                        hasResized = true;
                    }
                };
        glfwSetWindowSizeCallback(window, windowSizeCallback);
    }

    public Window() {
        setSize(640, 480);
        setFullScreen(false);
        hasResized = false;
    }

    public void createWindow(String title) {

        this.window =
                glfwCreateWindow(width, height, title, fullscreen ? glfwGetPrimaryMonitor() : 0, 0);

        if (window == 0) {
            throw new IllegalStateException("Failed to create window...");
        }

        if (!fullscreen) {
            GLFWVidMode vid = glfwGetVideoMode(glfwGetPrimaryMonitor());
            glfwSetWindowPos(window, (vid.width() - width) / 2, (vid.height() - height) / 2);

            glfwShowWindow(window);
        }
        glfwMakeContextCurrent(window);
        input = new Input(window);
        setLocalCallbacks();
    }

    public void cleanUp() {
        windowSizeCallback.close();
    }

    public boolean shouldClose() {
        return glfwWindowShouldClose(window);
    }

    public void swapBuffers() {
        glfwSwapBuffers(window);
    }

    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void setFullScreen(boolean fullscreen) {
        this.fullscreen = fullscreen;
    }

    public void changeScreenMode() {
        if (fullscreen) {
            fullscreen = false;
        } else {
            fullscreen = true;
        }
    }

    public void update() {
        hasResized = false;
        input.update();
        glfwPollEvents();
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean hasResized() {
        return hasResized;
    }

    public boolean isFullScreen() {
        return fullscreen;
    }

    public long getWindow() {
        return window;
    }

    public Input getInput() {
        return input;
    }

    public int[] getMousePosition() {
        DoubleBuffer posX = BufferUtils.createDoubleBuffer(1);
        DoubleBuffer posY = BufferUtils.createDoubleBuffer(1);

        GLFW.glfwGetCursorPos(window, posX, posY);

        return new int[] {(int) posX.get(0), (int) posY.get(0)};
    }
}
