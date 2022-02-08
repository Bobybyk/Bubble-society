package application.shell;

import java.util.HashMap;
import java.util.Scanner;

import application.VisualEngine;
import application.commands.Command;
import application.commands.command_list.CommandDebug;
import application.commands.command_list.CommandHelp;
import application.commands.command_list.CommandKill;
import game.GM;


public class Console extends Thread {
    private HashMap<String,Command> commandList = new HashMap<String,Command>();

    public Console() {
        commandList.put("debug", new CommandDebug());
        commandList.put("help", new CommandHelp());
        commandList.put("kill", new CommandKill());
    }

    @Override
    public void run() {
        Scanner sc = new Scanner(System.in);
        createView();
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

    private void createView() {
        new VisualEngine(new GM());
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
