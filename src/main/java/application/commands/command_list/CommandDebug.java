package application.commands.command_list;

import application.DevMode;
import application.commands.Command;

public class CommandDebug extends Command {

    public CommandDebug() {
        super("100");
    }

    @Override
    public void execute(String[] args) {
        if (args.length == 2) {
            int value;
            try {
                value = Integer.parseInt(args[1]);
            } catch(NumberFormatException e) {
                value = -1;
            }
            if (value == 0) {
                DevMode.debug = false;
                System.out.println("debug mode : disabled");
                return;
            }
            if (value == 1) {
                DevMode.debug = true;
                System.out.println("debug mode : enabled");
                return;
            }
        }
        System.out.println("Command syntax error");
    }
    
}
