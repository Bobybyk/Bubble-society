package application.commands.command_list;

import application.DevMode;
import application.commands.Command;

public class CommandCreateGraphicEngine extends Command{

    public CommandCreateGraphicEngine() {
        super("120");
    }

    @Override
    public void execute(String[] args) {
        DevMode.createGraphicEngine();
    }
    
}
