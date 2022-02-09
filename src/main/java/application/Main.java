package application;

import application.shell.Console;
import application.system.TestLoadAverage;


public class Main {
	public static void main(String[] args) {
		TestLoadAverage.testCompute();
		new Console().start();	
	}

}