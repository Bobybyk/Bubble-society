/*
 *
 * Copyright (c) 2022 Matthieu Le Franc
 *
 * You are prohibited from sharing and distributing this creation without our prior authorization, more specifically:
 * 
 * TO PROVIDE A COPY OF OUR GAME TO ANY THIRD PARTY;
 * TO USE THIS CREATION FOR COMMERCIAL PURPOSES;
 * TO USE THIS CREATIONS FOR PROFIT;
 * TO ALLOW ANY THIRD PARTY TO ACCESS TO THIS CREATION IN AN UNFAIR OR ABUSIVE MANNER;
 * 
 */
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
