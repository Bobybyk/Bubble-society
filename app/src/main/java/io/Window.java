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

    /** set the callbacks for the window */
    public static void setCallBacks() {
        glfwSetErrorCallback(
                new GLFWErrorCallback() {
                    @Override
                    public void invoke(int error, long description) {
                        throw new IllegalStateException(GLFWErrorCallback.getDescription(description));
                    }
                });
    }

    /** set the callbacks for the window */
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

    /**
     * create the window
     *
     * @param title window title
     */
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

    /** clean up the window */
    public void cleanUp() {
        windowSizeCallback.close();
    }

    /**
     * check if the window should close
     *
     * @return true if the window should close, false otherwise
     */
    public boolean shouldClose() {
        return glfwWindowShouldClose(window);
    }

    /** swap the buffers */
    public void swapBuffers() {
        glfwSwapBuffers(window);
    }

    /**
     * set the size of the window
     *
     * @param width new width
     * @param height new height
     */
    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    /**
     * set the fullscreen state of the window
     *
     * @param fullscreen new fullscreen state
     */
    public void setFullScreen(boolean fullscreen) {
        this.fullscreen = fullscreen;
    }

    /** change the screen mode (fullscreen/windowed) */
    public void changeScreenMode() {
        if (fullscreen) {
            fullscreen = false;
        } else {
            fullscreen = true;
        }
    }

    /** update the window and the input */
    public void update() {
        hasResized = false;
        input.update();
        glfwPollEvents();
    }

    /**
     * get the width of the window
     *
     * @return width of the window
     */
    public int getWidth() {
        return width;
    }

    /**
     * get the height of the window
     *
     * @return height of the window
     */
    public int getHeight() {
        return height;
    }

    /**
     * check if the window has been resized
     *
     * @return true if the window has been resized, false otherwise
     */
    public boolean hasResized() {
        return hasResized;
    }

    /**
     * check if the window is fullscreen
     *
     * @return true if the window is fullscreen, false otherwise
     */
    public boolean isFullScreen() {
        return fullscreen;
    }

    /**
     * get the window
     *
     * @return window
     */
    public long getWindow() {
        return window;
    }

    /**
     * get the input
     *
     * @return input
     */
    public Input getInput() {
        return input;
    }

    /**
     * get the mouse position
     *
     * @return mouse position as an array of int (x, y)
     */
    public int[] getMousePosition() {
        DoubleBuffer posX = BufferUtils.createDoubleBuffer(1);
        DoubleBuffer posY = BufferUtils.createDoubleBuffer(1);

        GLFW.glfwGetCursorPos(window, posX, posY);

        return new int[] {(int) posX.get(0), (int) posY.get(0)};
    }
}
