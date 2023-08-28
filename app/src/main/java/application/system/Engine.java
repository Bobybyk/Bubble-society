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
package application.system;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

import application.debug.DebugLogger;
import application.debug.DebugType;
import application.system.timers.GameTimer;
import assets.Assets;
import game.Game;
import io.Timer;
import io.Window;
import java.util.Arrays;
import org.joml.Vector3f;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.GL;
import render.Camera;
import render.Shader;
import world.TileRenderer;
import world.World;

public class Engine {

    /** contient les éléments de jeu et agit sur leurs états */
    private Game game;
    /** bat la mesure du temps */
    private GameTimer spawner;
    /** contient les éléments de position et projection de la caméra */
    private Camera camera;
    /** contient les règles du jeu et s'assure de leur respect */
    private World world;
    /** contient les éléments de rendu global */
    private Window window;
    /** s'occupe de la partie système du rendu */
    private GLFWVidMode vid;
    /** s'occupe du rendu de chaque entité visuelle (texture) */
    private TileRenderer tiles;
    /** gestion des shaders */
    private Shader shader;

    /** nombre de frames par seconde */
    private double frameCap;
    /** compte le nombre de frames passées jusqu'à frameCap */
    private double FrameTime;
    /** première valeur de temps */
    private double time;
    /** deuxième valeur de temps */
    private double time2;
    /** temps écoulé entre time et time2 pour déterminer le nombre de frames à rendre */
    private double passed;
    /** temps non processé */
    private double unprocessed;
    /** nombre de frames rendues */
    private int frames;
    /** détermine quand le rendu est possible */
    private boolean canRender = false;

    public Engine() {

        Window.setCallBacks();

        if (!glfwInit()) {
            System.err.println("Failed to initialize GLFW");
            System.exit(1);
        }

        // Définition des paramètres de la fenêtre
        window = new Window();
        vid = glfwGetVideoMode(glfwGetPrimaryMonitor());
        window.setSize(vid.width(), vid.height());
        window.setFullScreen(false);
        window.createWindow("Society");

        GL.createCapabilities();

        // initialisation de paramètres pour la transparence
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        // initialisation de la caméra
        camera = new Camera(window.getWidth(), window.getHeight());

        glEnable(GL_TEXTURE_2D);

        // initialisation des textures
        tiles = new TileRenderer();
        Assets.initAsset();

        // initialisation des shaders
        shader = new Shader("shader");

        DebugType.gc = this;

        // initialisation du monde
        world = new World("vanilla", camera);
        world.calculateView(window);

        // initialisation du jeu
        game = new Game(world);
        spawner = new GameTimer(game);

        // 60fps
        frameCap = 1.0 / 60.0;

        FrameTime = 0;
        frames = 0;

        time = Timer.getTime();

        unprocessed = 0;

        while (!window.shouldClose()) {

            // temps écoulé que le programme n'a pas rendu
            canRender = false;
            time2 = Timer.getTime();
            passed = time2 - time;
            unprocessed += passed;
            FrameTime += passed;
            time = time2;

            // calculs relatifs au jeu (spawn...)
            spawner.gameProcess();

            // frame qui n'ont pas à être rendues
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
                    if (world.getEntityDisplay() != null) {
                        world.getEntityDisplay().changeCameraMod();
                        DebugLogger.print(DebugType.UI, "camera mode has been updated");
                    }
                }

                // mise à jour de la mosition de la souris
                if (window.getMousePosition()[0] <= 15) {
                    camera.getPosition().sub(new Vector3f(-5, 0, 0));
                }
                if (window.getMousePosition()[1] <= 15) {
                    camera.getPosition().sub(new Vector3f(0, 5, 0));
                }
                if (window.getMousePosition()[0] >= vid.width() - 15
                        && window.getMousePosition()[0] <= vid.width() + 15) {
                    camera.getPosition().sub(new Vector3f(5, 0, 0));
                }
                if (window.getMousePosition()[1] >= vid.height() - 15
                        && window.getMousePosition()[0] <= vid.width() + 15) {
                    camera.getPosition().sub(new Vector3f(0, -5, 0));
                }
                DebugLogger.print(
                        DebugType.UIEXT,
                        "mouse cursor position : " + Arrays.toString(window.getMousePosition()));

                // zoom
                GLFW.glfwSetScrollCallback(
                        window.getWindow(),
                        new GLFWScrollCallback() {
                            @Override
                            public void invoke(long win, double dx, double dy) {
                                // System.out.println(dy);
                                world.setScale((int) dy, window, camera);
                                DebugLogger.print(DebugType.RESIZE, "world scale : " + world.getScale());
                            }
                        });

                // déplacement de la caméra
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

                if (window.getInput().isMouseDown(GLFW.GLFW_MOUSE_BUTTON_1)) {
                    world.defineZoneBorder(world.getMousePositionOnWorld(camera, window));
                }

                // spawn followers
                if (window.getInput().isKeyDown(GLFW.GLFW_KEY_F)) {
                    game.spawnWorker(1);
                }
                // spawn insurgents
                if (window.getInput().isKeyDown(GLFW.GLFW_KEY_I)) {
                    game.spawnWorker(2);
                }

                // bloque le déplacement de la caméra
                // world.update((float)frameCap, window, camera);
                world.entitiesUpdate((float) frameCap, game, window);
                world.correctCamera(camera, window);
                world.correctMapSize(window);

                window.update();

                if (FrameTime >= 1.0) {
                    FrameTime = 0;
                    DebugLogger.print(DebugType.SYS, ("FPS: " + frames));
                    frames = 0;
                }
            }

            /*
             * logique pour le rendu des frames
             */
            if (canRender) {
                glClear(GL_COLOR_BUFFER_BIT);

                world.render(tiles, shader, camera);

                window.swapBuffers();
                frames++;
            }
        }

        Assets.deleteAsset();
        glfwTerminate();
    }

    public Game getGame() {
        return game;
    }
}
