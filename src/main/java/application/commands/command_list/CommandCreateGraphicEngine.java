package application.commands.command_list;

import application.commands.Command;
import application.debug.DebugLogger;

public class CommandCreateGraphicEngine extends Command{

    public CommandCreateGraphicEngine() {
        super("120");
    }

    @Override
    public void execute(String[] args) {
        DebugLogger.createGraphicEngine();
    }
    
}
