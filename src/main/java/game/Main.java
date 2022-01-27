package game;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import application.TestLoadAverage;
import application.shell.Console;

import org.lwjgl.opengl.GL;

import io.Timer;
import io.Window;
import render.Camera;
import render.Shader;
import render.Texture;
import render.VBO;
import world.TileRenderer;

import java.nio.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class Main {

	public Main() {

		Window.setCallBacks();

		if (!glfwInit()) {
			System.err.println("Failed to initialize GLFW");
			System.exit(1);
		}

		Window win = new Window();
		// watch later to create an instance
		GLFWVidMode vid = glfwGetVideoMode(glfwGetPrimaryMonitor());
		win.setSize(vid.width(), vid.height());
		win.setFullScreen(true);
		win.createWindow("Society");

		GL.createCapabilities();

		Camera camera = new Camera(win.getWidth(), win.getHeight());

		glEnable(GL_TEXTURE_2D);

		TileRenderer tiles = new TileRenderer();

		/* float[] vertices = new float[] {
			-0.5f, 0.5f, 0, // TOP LEFT       0
			0.5f, 0.5f, 0,	// TOP RIGHT      1
			0.5f, -0.5f, 0, // BOTTOM RIGHT   2
			-0.5f, -0.5f, 0, // BOTTOM LEFT   3
		};

		float[] texture = new float[] {
			0, 0,
			1, 0,
			1, 1,
			0, 1,
		};

		int[] indices = new int[] {
			0,1,2,
			2,3,0
		};
		VBO modelTexture = new VBO(vertices, texture, indices); */

		Shader shader = new Shader("shader");
		Texture tex = new Texture("background");

		Matrix4f scale = new Matrix4f()
				.translate(new Vector3f(0, 0, 0))
				.scale(16);
		Matrix4f target = new Matrix4f();

		camera.setPosition(new Vector3f(-100, 0, 0));

		double frameCap = 1.0/60.0; // 60fps
		
		double FrameTime = 0;
		int frames = 0;

		double time = Timer.getTime();
		double unprocessed = 0; // time while progam hasn't been processed 

		while(!win.shouldClose()) {
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
				target = scale;

				if(win.getInput().isKeyReleased(GLFW_KEY_ESCAPE)) {
					glfwSetWindowShouldClose(win.getWindow(), true);
				}
				win.update();
				if (FrameTime >= 1.0) {
					FrameTime = 0;
					System.out.println("FPS: " + frames);
					frames = 0;
				}
			}
			/*
			 * Render system
			 */
			if(canRender) {
				glClear(GL_COLOR_BUFFER_BIT);

				/* shader.bind();
				shader.setUniform("sampler", 0);
				shader.setUniform("projection", camera.getProjection().mul(target));
				modelTexture.render();
				tex.bind(0); */

				for (int i = 0 ; i < 8 ; i++) {
					for (int j = 0 ; j < 4 ; j++) {
						tiles.renderTile((byte)0, i, j, shader, scale, camera);
					}
				}

				win.swapBuffers();
				frames++;
			}

		}

		glfwTerminate();

	}

	public static void main(String[] args) {
		TestLoadAverage.testCompute();
		//new World();
		new Console().start();
		new Main();
	}

}