package application.commands.command_list;

import application.commands.Command;
import application.debug.DebugType;

public class CommandSetWander extends Command {

    public CommandSetWander() {
        super("140");
    }

    @Override
    public void execute(String[] args) {
        if (args.length == 2) {
            int value;
            try {
                value = Integer.parseInt(args[1]);
                if (DebugType.gc == null) {
                    System.out.println("grapique engine doesn't run");
                    return;
                }
                if (value == 0) {

                    DebugType.gc.getGame().changeWanderMode(false);
                    return;
                }
                if (value == 1) {
                    DebugType.gc.getGame().changeWanderMode(true);
                    return;
                }
            } catch (NumberFormatException e) {
            }
        }
        System.out.println("Command syntax error");
    }
}
