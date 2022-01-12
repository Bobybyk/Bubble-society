package model;

public class WorldTime extends Thread {
	long refTime = System.currentTimeMillis();
	long currentTime;
	long prevCurrentTime = 0;

    public void run() {
		while(true) {
			currentTime = System.currentTimeMillis()-refTime;
			if(currentTime%1000 == 0 && prevCurrentTime != currentTime) {
				prevCurrentTime = currentTime;
			}
		}
    }

	public long getCurrentTime() {
		return currentTime/1000;
	}
}
