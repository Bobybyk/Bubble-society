package application.commands.command_list;

import application.DevMode;
import application.commands.Command;

public class CommandCreateView extends Command{

    public CommandCreateView() {
        super("120");
    }

    @Override
    public void execute(String[] args) {
        DevMode.createView();
    }
    
}
