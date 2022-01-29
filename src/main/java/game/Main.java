package game;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import application.DevMode;
import application.TestLoadAverage;
import application.shell.Console;
import collision.AABB;
import entity.WorkerDisplay;

import org.lwjgl.opengl.GL;

import io.Timer;
import io.Window;
import render.Camera;
import render.Shader;
import render.Texture;
import render.VBO;
import world.Tile;
import world.TileRenderer;
import world.World;

import java.nio.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class Main {

	public Main(GM processor) {

		Window.setCallBacks();

		if (!glfwInit()) {
			System.err.println("Failed to initialize GLFW");
			System.exit(1);
		}

		Window window = new Window();
		// watch later to create an instance
		GLFWVidMode vid = glfwGetVideoMode(glfwGetPrimaryMonitor());
		window.setSize(vid.width(), vid.height());
		window.setFullScreen(true);
		window.createWindow("Society");

		GL.createCapabilities();

		Camera camera = new Camera(window.getWidth(), window.getHeight());

		glEnable(GL_TEXTURE_2D);

		TileRenderer tiles = new TileRenderer();

		Shader shader = new Shader("shader");

		World world = new World();

		WorkerDisplay worker = new WorkerDisplay();

		world.setTile(Tile.markerTile, 5, 0); // 0, 0 : coordonates into the map
		world.setTile(Tile.markerTile, 6, 0);
		
		double frameCap = 1.0/60.0; // 60fps
		
		double FrameTime = 0;
		int frames = 0;

		double time = Timer.getTime();
		double unprocessed = 0; // time while progam hasn't been processed 

		while(!window.shouldClose()) {
			boolean canRender = false;

			double time2 = Timer.getTime();
			double passed = time2 - time;
			unprocessed+=passed;
			FrameTime += passed;
			time = time2;

			// doesn't have to be rendered
			while(unprocessed >= frameCap) {
				unprocessed-=frameCap;
				canRender = true;

				if(window.getInput().isKeyReleased(GLFW_KEY_ESCAPE)) {
					glfwSetWindowShouldClose(window.getWindow(), true);
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
				worker.update((float)frameCap, window, camera, world, null);

				world.correctCamera(camera, window);

				window.update();

				if (FrameTime >= 1.0) {
					FrameTime = 0;
					if (DevMode.debug) {
						System.out.println("FPS: " + frames);
						Console.layout();
					}
					// DebugLogger.print(DebugType.ERROR, "FPS Dosabmed")
					frames = 0;
				}
			}
			/*
			 * Render system
			 */
			if(canRender) {
				glClear(GL_COLOR_BUFFER_BIT);

				world.render(tiles, shader, camera, window);

				worker.render(shader, camera);

				window.swapBuffers();
				frames++;
			}

		}

		glfwTerminate();

	}

	public static void main(String[] args) {
		TestLoadAverage.testCompute();
		new Console().start();
		new Main(new GM());
	}

}