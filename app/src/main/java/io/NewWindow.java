package io;

import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glViewport;

import application.debug.DebugLogger;
import application.debug.DebugType;
import application.system.NewEngine;
import assets.Assets;
import game.Game;
import imgui.ImGui;
import imgui.app.Configuration;
import imgui.flag.ImGuiWindowFlags;
import java.nio.DoubleBuffer;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWScrollCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import render.Camera;
import render.Shader;
import world.TileRenderer;
import world.World;

public class NewWindow extends imgui.app.Window {

    private Configuration config;

    private World world;
    private Camera camera;
    private TileRenderer tiles;
    private Shader shader;
    private Game game;
    private NewEngine engine;
    private GLFWVidMode vid;

    private Input input;

    public NewWindow() {

        config = new Configuration();
        this.init(config);
        this.setCallBacks();

        camera = new Camera(this.getWidth(), this.getHeight());

        tiles = new TileRenderer();
        Assets.initAsset();

        shader = new Shader("shader");

        world = new World("vanilla", camera);

        game = new Game(world);

        input = new Input(this.getHandle());

        vid = glfwGetVideoMode(glfwGetPrimaryMonitor());

        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        engine = new NewEngine(world, camera, vid, this, game);

        this.launch();
    }

    public void setCallBacks() {
        NewWindow nWin = this;
        GLFW.glfwSetScrollCallback(
                this.getHandle(),
                new GLFWScrollCallback() {
                    @Override
                    public void invoke(long win, double dx, double dy) {
                        // System.out.println(dy);
                        world.setScale((int) dy, nWin, camera);
                        DebugLogger.print(DebugType.RESIZE, "world scale : " + world.getScale());
                    }
                });

        GLFW.glfwSetWindowSizeCallback(
                handle,
                new GLFWWindowSizeCallback() {
                    @Override
                    public void invoke(final long window, final int width, final int height) {
                        camera.setProjection(width, height);
                        glViewport(0, 0, width, height);
                        world.calculateView(nWin);
                        config.setWidth(width);
                        config.setHeight(height);
                    }
                });
    }

    public int getWidth() {
        return config.getWidth();
    }

    public int getHeight() {
        return config.getHeight();
    }

    public Input getInput() {
        return input;
    }

    public boolean shouldClose() {
        return glfwWindowShouldClose(this.getHandle());
    }

    public void swapBuffers() {
        glfwSwapBuffers(this.getHandle());
    }

    public void update() {
        input.update();
        glfwPollEvents();
    }

    public int[] getMousePosition() {
        DoubleBuffer posX = BufferUtils.createDoubleBuffer(1);
        DoubleBuffer posY = BufferUtils.createDoubleBuffer(1);

        GLFW.glfwGetCursorPos(this.getHandle(), posX, posY);

        return new int[] {(int) posX.get(0), (int) posY.get(0)};
    }

    public void launch() {

        engine.start();

        this.run();
        this.dispose();
        Assets.deleteAsset();
    }

    public void checkInputs() {
        // déplacement de la caméra
        if (this.getInput().isKeyDown(GLFW.GLFW_KEY_A)) {
            camera.getPosition().sub(new Vector3f(-5, 0, 0));
        }
        if (this.getInput().isKeyDown(GLFW.GLFW_KEY_D)) {
            camera.getPosition().sub(new Vector3f(5, 0, 0));
        }
        if (this.getInput().isKeyDown(GLFW.GLFW_KEY_W)) {
            camera.getPosition().sub(new Vector3f(0, 5, 0));
        }
        if (this.getInput().isKeyDown(GLFW.GLFW_KEY_S)) {
            camera.getPosition().sub(new Vector3f(0, -5, 0));
        }
        // spawn followers
        if (this.getInput().isKeyDown(GLFW.GLFW_KEY_F)) {
            game.spawnWorker(1);
        }
        // spawn insurgents
        if (this.getInput().isKeyDown(GLFW.GLFW_KEY_I)) {
            game.spawnWorker(2);
        }
    }

    private int count = 0;

    @Override
    public void process() {

        /*
         * All renders must be done here
         * ==============================
         */

        world.calculateView(this);
        world.render(tiles, shader, camera);
        this.update();

        /*
         * ImGui window
         * ====================
         */
        if (ImGui.begin("Demo", ImGuiWindowFlags.AlwaysAutoResize)) {
            ImGui.text("Hello, World!");
            if (ImGui.button("Save")) {
                count++;
                System.out.println("button clicked");
            }
            ImGui.sameLine();
            ImGui.text(String.valueOf(count));
        }

        /*
         * Detect inputs
         * =====================
         */
        this.checkInputs();

        /* If the click is not on an ImGui window */
        if (!ImGui.isWindowHovered()) {

            if (this.getInput().isMouseDown(GLFW.GLFW_MOUSE_BUTTON_1)) {
                world.defineZoneBorder(world.getMousePositionOnWorld(camera, this));
            }
        }

        ImGui.end();
    }
}
