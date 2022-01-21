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

		long window = glfwCreateWindow(1920, 1080, "view", 0, 0);
		glfwShowWindow(window);

		glfwMakeContextCurrent(window);

		GL.createCapabilities();

		//Texture tex = new Texture("./assets/text");

		float x = 0;
		float color_red = 1;
		float color_blue = 0;

		while(!glfwWindowShouldClose(window)) {

			if(glfwGetKey(window, GLFW_KEY_ESCAPE) == GL_TRUE) {
				glfwDestroyWindow(window);
				break;
			}

			if (glfwGetMouseButton(window, 0) == GL_TRUE) {
				color_red = 0.25f;
				color_blue = 1;
			} else {
				color_red = 1;
				color_blue = 0.25f;
			}

			if(glfwGetKey(window, GLFW_KEY_A) == GL_TRUE) {
				x+=0.001f;
			}

			glfwPollEvents();

			glClear(GL_COLOR_BUFFER_BIT);

			glBegin(GL_QUADS);
				glColor4f(color_red, 0, color_blue, 0);
				glVertex2f(-0.5f+x, 0.5f);
				glVertex2f(0.5f+x, 0.5f);
				glVertex2f(0.5f+x, -0.5f);
				glVertex2f(-0.5f+x, -0.5f);
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