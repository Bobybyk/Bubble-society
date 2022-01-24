package application;

import org.joml.Matrix4f;
import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;
import org.lwjgl.opengl.GL;

import controller.World;
import controller.shell.Console;
import model.visual_engine.Shader;
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

		long window = glfwCreateWindow(640, 480, "view", 0, 0);
		glfwShowWindow(window);

		glfwMakeContextCurrent(window);

		GL.createCapabilities();

		glEnable(GL_TEXTURE_2D);

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
		Shader shader = new Shader("shader");
		Texture tex = new Texture("./resources/assets/logo.png");

		Matrix4f projection = new Matrix4f().ortho2D(-640/2, 640/2, -480/2, 480/2);
		Matrix4f scale = new Matrix4f().scale(64);
		Matrix4f target = new Matrix4f();

		projection.mul(scale, target);

		while(!glfwWindowShouldClose(window)) {

			if(glfwGetKey(window, GLFW_KEY_ESCAPE) == GL_TRUE) {
				glfwDestroyWindow(window);
				break;
			}

			glfwPollEvents();

			glClear(GL_COLOR_BUFFER_BIT);

			shader.bind();
			shader.setUniform("sampler", 0);
			shader.setUniform("projection", target);
			tex.bind(0);
			modelTexture.render();

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