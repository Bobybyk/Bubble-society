package application.commands.command_list;

import application.commands.Command;
import application.debug.DebugLogger;


public class CommandDebug extends Command {

    public CommandDebug() {
        super("110");
    }

    @Override
    public void execute(String[] args) {
        if (args.length == 2) {
            int value;
            try {
                value = Integer.parseInt(args[1]);
            } catch(NumberFormatException e) {
                if (args[1].equals("enable")) {
                    DebugLogger.debug = true;
                    System.out.println("debug mode : enabled");
                    return;
                }
                if (args[1].equals("disable")) {
                    DebugLogger.debug = false;
                    System.out.println("debug mode : disabled");
                    return;   
                } else {
                    value = -1;
                }
            }
            if (value == 0) {
                DebugLogger.debug = false;
                System.out.println("debug mode : disabled");
                return;
            }
            if (value == 1) {
                DebugLogger.debug = true;
                System.out.println("debug mode : enabled");
                return;
            }
        }
        System.out.println("Command syntax error");
    }
    
}
