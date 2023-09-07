package application.system;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

import application.debug.DebugLogger;
import application.debug.DebugType;
import application.system.timers.GameTimer;
import game.Game;
import io.NewWindow;
import io.Timer;
import java.util.Arrays;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWScrollCallback;
import org.lwjgl.glfw.GLFWVidMode;
import render.Camera;
import world.World;

public class NewEngine extends Thread {

    /** contient les éléments de jeu et agit sur leurs états */
    private Game game;
    /** bat la mesure du temps */
    private GameTimer spawner;
    /** contient les éléments de position et projection de la caméra */
    private Camera camera;
    /** contient les règles du jeu et s'assure de leur respect */
    private World world;
    /** contient les éléments de rendu global */
    private NewWindow window;
    /** s'occupe de la partie système du rendu */
    private GLFWVidMode vid;

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

    public NewEngine(World w, Camera c, GLFWVidMode v, NewWindow win) {

        world = w;
        camera = c;
        vid = v;

        window = win;

        game = new Game(world);

        spawner = new GameTimer(game);
    }

    @Override
    public void run() {

        // 60fps
        frameCap = 1.0 / 60.0;

        FrameTime = 0;
        frames = 0;

        time = Timer.getTime();

        unprocessed = 0;

        while (!window.shouldClose()) {
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
                    // world.calculateView(window);
                    glViewport(0, 0, window.getWidth(), window.getHeight());
                }

                unprocessed -= frameCap;

                if (window.getInput().isKeyReleased(GLFW_KEY_ESCAPE)) {
                    glfwSetWindowShouldClose(window.getHandle(), true);
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
                        window.getHandle(),
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
        }
    }
}
