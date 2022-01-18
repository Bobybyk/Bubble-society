package application;

import controller.World;
import static org.lwjgl.glfw.GLFW.*;


public class Main {

	public Main() {
		if (!glfwInit()) {
			System.err.println("Failed to initialize GLFW");
			System.exit(1);
		}
		long window = glfwCreateWindow(640, 480, "view", 0, 0);
		glfwShowWindow(window);

		while(!glfwWindowShouldClose(window)) {
			glfwPollEvents();
		}

		glfwTerminate();

	}
	public static void main(String[] args) {
		//TestLoadAverage.testCompute();
		//new World();
		
		new Main();
	}

}