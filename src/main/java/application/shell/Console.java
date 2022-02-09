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
package application.shell;

import java.util.HashMap;
import java.util.Scanner;

import application.commands.Command;
import application.commands.command_list.CommandCreateGraphicEngine;
import application.commands.command_list.CommandDebug;
import application.commands.command_list.CommandHelp;
import application.commands.command_list.CommandKill;
import application.debug.DebugLogger;


public class Console extends Thread {
    private HashMap<String,Command> commandList = new HashMap<String,Command>();

    public Console() {
        commandList.put("debug", new CommandDebug());
        commandList.put("help", new CommandHelp());
        commandList.put("kill", new CommandKill());
        commandList.put("run", new CommandCreateGraphicEngine());
    }

    @Override
    public void run() {
        Scanner sc = new Scanner(System.in);
        DebugLogger.setTypeMap();
        while(true) {
            try {
                Thread.sleep(1);
                layout();
                String input = sc.nextLine();
                processCommand(input);
            } catch(InterruptedException e) {
            }
        }   
    }



    public static void layout() {
        System.out.print("\u001B[31m");
        System.out.print("Society_debug> ");
        System.out.print("\u001B[37m");
    }

    private void processCommand(String command) {
		String[] args = breakCommand(command);
		for(String s : commandList.keySet()) {
			if(s.equals(args[0])) {
				commandList.get(s).execute(args);
				return;
			}
		}
    }

	private String[] breakCommand(String command) {
		String delims = "[ ]+";	
		String[] args = command.split(delims);
		return args;
	}

}
