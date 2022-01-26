package model.visual_engine;

import static org.lwjgl.glfw.GLFW.*;

import org.lwjgl.glfw.GLFWVidMode;

public class Window {
    private long window;
    private int width, height;
    private boolean fullscreen;

    public Window() {
        setSize(640, 480);
        setFullScreen(false);
    }

    public void createWindow(String title) {

        this.window = glfwCreateWindow(
                        width, 
                        height, 
                        title, 
                        fullscreen ? glfwGetPrimaryMonitor() : 0, 
                        0);

        if (window == 0) {
            throw new IllegalStateException("Failed to create window...");
        }

        if(!fullscreen) {
            GLFWVidMode vid = glfwGetVideoMode(glfwGetPrimaryMonitor());
            glfwSetWindowPos(window, 
                            (vid.width()-width)/2, 
                            (vid.height()-height)/2);
    
            glfwShowWindow(window);
    
            
        }
        glfwMakeContextCurrent(window);
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

    public int getWidth() {
        return width;
    }
    public int getHeight() {
        return height;
    }
    public boolean isFullScreen() {
        return fullscreen;
    }
    public long getWindow() {
        return window;
    }
}
