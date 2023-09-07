package io;

import static org.lwjgl.glfw.GLFW.*;

import application.system.NewEngine;
import assets.Assets;
import imgui.ImGui;
import imgui.app.Configuration;
import imgui.flag.ImGuiWindowFlags;
import java.nio.DoubleBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWVidMode;
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
    private NewEngine engine;
    private GLFWVidMode vid;

    private Input input;

    private boolean hasResized = false;

    public NewWindow() {

        config = new Configuration();
        this.init(config);

        camera = new Camera(this.getWidth(), this.getHeight());

        tiles = new TileRenderer();
        Assets.initAsset();

        shader = new Shader("shader");

        world = new World("vanilla", camera);

        input = new Input(this.getHandle());

        vid = glfwGetVideoMode(glfwGetPrimaryMonitor());

        engine = new NewEngine(world, camera, vid, this);

        this.launch();
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

    public boolean hasResized() {
        return hasResized;
    }

    public void update() {
        hasResized = false;
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

        /*
         * ===============================
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

        ImGui.end();
    }

    @Override
    public void dispose() {
        Assets.deleteAsset();
    }
}
