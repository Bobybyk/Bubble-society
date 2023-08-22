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
package fr.lefranc.society.application.system;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_F10;
import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glViewport;

import fr.lefranc.society.application.debug.DebugLogger;
import fr.lefranc.society.application.debug.DebugType;
import fr.lefranc.society.assets.Assets;
import fr.lefranc.society.game.Game;
import fr.lefranc.society.io.Timer;
import fr.lefranc.society.io.Window;
import fr.lefranc.society.render.Camera;
import fr.lefranc.society.render.Shader;
import fr.lefranc.society.world.TileRenderer;
import fr.lefranc.society.world.World;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

public class VisualEngine {

    public VisualEngine(Game processor) {

        Window.setCallBacks();

        if (!glfwInit()) {
            System.err.println("Failed to initialize GLFW");
            System.exit(1);
        }

        Window window = new Window();
        GLFWVidMode vid = glfwGetVideoMode(glfwGetPrimaryMonitor());
        window.setSize(vid.width(), vid.height());
        // window.setFullScreen(true);
        window.createWindow("Society");

        GL.createCapabilities();

        // for texture transparency
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        Camera camera = new Camera(window.getWidth(), window.getHeight());

        glEnable(GL_TEXTURE_2D);

        TileRenderer tiles = new TileRenderer();
        Assets.initAsset();

        Shader shader = new Shader("shader");

        World world = new World("vanilla", camera);
        world.calculateView(window);

        // Gui gui = new Gui(window);

        double frameCap = 1.0 / 60.0; // 60fps

        double FrameTime = 0;
        int frames = 0;

        double time = Timer.getTime();

        // time while progam hasn't been processed
        double unprocessed = 0;

        while (!window.shouldClose()) {
            boolean canRender = false;

            double time2 = Timer.getTime();
            double passed = time2 - time;
            unprocessed += passed;
            FrameTime += passed;
            time = time2;

            // doesn't have to be rendered
            while (unprocessed >= frameCap) {

                if (window.hasResized()) {
                    camera.setProjection(window.getWidth(), window.getHeight());
                    // gui.resizeCamera(window);
                    world.calculateView(window);
                    glViewport(0, 0, window.getWidth(), window.getHeight());
                }

                unprocessed -= frameCap;
                canRender = true;

                if (window.getInput().isKeyReleased(GLFW_KEY_ESCAPE)) {
                    glfwSetWindowShouldClose(window.getWindow(), true);
                    DebugLogger.destroyGraphicEngine();
                }
                if (window.getInput().isKeyReleased(GLFW_KEY_F10)) {
                    if (world.getWorkerDisplay() != null) {
                        world.getWorkerDisplay().changeCameraMod();
                    }
                }

                if (window.getInput().isKeyDown(GLFW.GLFW_KEY_A)) {
                    camera.getPosition().sub(new Vector3f(-5, 0, 0));
                }
                if (window.getInput().isKeyDown(GLFW.GLFW_KEY_D)) {
                    camera.getPosition().sub(new Vector3f(5, 0, 0));
                }
                if (window.getInput().isKeyDown(GLFW.GLFW_KEY_W)) {
                    camera.getPosition().sub(new Vector3f(0, 5, 0));
                }
                if (window.getInput().isKeyDown(GLFW.GLFW_KEY_S)) {
                    camera.getPosition().sub(new Vector3f(0, -5, 0));
                }

                // blocks camera shifting
                world.update((float) frameCap, window, camera);

                world.correctCamera(camera, window);

                window.update();

                if (FrameTime >= 1.0) {
                    FrameTime = 0;
                    DebugLogger.print(DebugType.ALL, ("FPS: " + frames));
                    frames = 0;
                }
            }

            /*
             * Render system
             */
            if (canRender) {
                glClear(GL_COLOR_BUFFER_BIT);

                world.render(tiles, shader, camera);

                // gui.render();

                window.swapBuffers();
                frames++;
            }
        }

        Assets.deleteAsset();

        glfwTerminate();
    }
}
