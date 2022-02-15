package application.system.timers;

import game.Game;

import static org.lwjgl.glfw.GLFW.*;


public class GameTimer {
	private int lastTimeProccessed = 0;
	private int timeProccessed = 0;

    private Game game;
    

    public GameTimer(Game game) {
        this.game = game;
    }

    public void gameProcess() {
		timeProccessed = (int)glfwGetTime();
		if (timeProccessed%2==0 && timeProccessed != lastTimeProccessed) {
			//game.multiSpawn();
			lastTimeProccessed = timeProccessed;
			//System.out.println(timeProccessed);
			//System.out.println(lastTimeProccessed);
		}
		if (timeProccessed%1==0 && timeProccessed != lastTimeProccessed) {
			game.decreaseWorkersHp();
			lastTimeProccessed = timeProccessed;
		}
	}

} 
