package application.commands.command_list;

import application.commands.Command;


public class CommandKill extends Command {

    public CommandKill() {
        super("666");
    }

    @Override
    public void execute(String[] args) {
        System.exit(0);

    }
    
}
