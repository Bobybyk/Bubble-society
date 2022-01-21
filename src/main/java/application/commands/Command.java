package application.commands;

import java.io.PrintWriter;

public abstract class Command {

	private String label;
	private PrintWriter out;

	public Command(String label,PrintWriter pw) {
		this.label = label;
		this.out = pw;
	}

	public void sendMessage(String message) {
		out.println(message);
		out.flush();
	}

	public abstract void execute(/* SomeObject obj, */ String[] args);
}
