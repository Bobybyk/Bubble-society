package application.commands;


public abstract class Command {

	private String commandId;

	public Command(String id) {
		this.commandId = id;
	}

	public abstract void execute(String[] args);
}
