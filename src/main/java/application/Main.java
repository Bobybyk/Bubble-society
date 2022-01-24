package application;

import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;
import org.lwjgl.opengl.GL;

import controller.World;
import controller.shell.Console;
import model.visual_engine.Texture;
import model.visual_engine.VBO;

import java.nio.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

import controller.World;

public class Main {

	public Main() {

		if (!glfwInit()) {
			System.err.println("Failed to initialize GLFW");
			System.exit(1);
		}

		long window = glfwCreateWindow(1920, 1080, "view", 0, 0);
		glfwShowWindow(window);

		glfwMakeContextCurrent(window);

		GL.createCapabilities();

		glEnable(GL_TEXTURE_2D);

		Texture tex = new Texture("./src/main/assets/logo.png");

		float[] vertices = new float[] {
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

		VBO modelTexture = new VBO(vertices, texture, indices);

		while(!glfwWindowShouldClose(window)) {

			if(glfwGetKey(window, GLFW_KEY_ESCAPE) == GL_TRUE) {
				glfwDestroyWindow(window);
				break;
			}

			glfwPollEvents();

			glClear(GL_COLOR_BUFFER_BIT);

			tex.bind();
			
			modelTexture.render();

			/* glBegin(GL_QUADS);
				glTexCoord2f(0, 0);
				glVertex2f(-0.5f, 0.5f);

				glTexCoord2f(1, 0);
				glVertex2f(0.5f, 0.5f);

				glTexCoord2f(1, 1);
				glVertex2f(0.5f, -0.5f);

				glTexCoord2f(0, 1);
				glVertex2f(-0.5f, -0.5f);
			glEnd(); */

			glfwSwapBuffers(window);
		}

		glfwTerminate();

	}

	public static void main(String[] args) {
		TestLoadAverage.testCompute();
		new World();
		new Console().start();
		new Main();
	}

}