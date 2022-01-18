package application;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.opengl.GL;

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

		// define custom init color
		//glClearColor(red, green, blue, alpha);

		while(!glfwWindowShouldClose(window)) {
			glfwPollEvents();
			
			glClear(GL_COLOR_BUFFER_BIT);

			glBegin(GL_QUADS);
				glColor4f(1, 0, 0, 0);
				glVertex2f(-0.5f, 0.5f);
				
				glColor4f(0, 1, 0, 0);
				glVertex2f(0.5f, 0.5f);
				
				glColor4f(0, 0, 1, 0);
				glVertex2f(0.5f, -0.5f);
				
				glColor4f(1, 1, 1, 0);
				glVertex2f(-0.5f, -0.5f);
			glEnd();

			glfwSwapBuffers(window);
		}

		glfwTerminate();

	}
	public static void main(String[] args) {
		//TestLoadAverage.testCompute();
		//new World();
		
		new Main();
	}

}