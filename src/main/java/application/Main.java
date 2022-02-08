package application;

import application.shell.Console;

public class Main {
	public static void main(String[] args) {
		TestLoadAverage.testCompute();
		new Console().start();	
	}

}