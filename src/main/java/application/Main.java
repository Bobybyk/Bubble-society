package application;

import java.util.Timer;

import controller.World;
import model.timer.ShiftTimer;

public class Main {
	public static void main(String[] args) {
		TestLoadAverage.testCompute();
		new World();
		/* Timer chrono = new Timer();
		chrono.schedule(new ShiftTimer(), 0, 100); */
	}
}
