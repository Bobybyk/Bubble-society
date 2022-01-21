package controller.shell;

import java.util.HashMap;
import java.util.Scanner;

import application.commands.Command;
import application.commands.command_list.CommandDebug;

public class Console extends Thread {
    private HashMap<String,Command> commandList = new HashMap<String,Command>();

    public Console() {
        commandList.put("debug", new CommandDebug());
    }

    @Override
    public void run() {
        Scanner sc = new Scanner(System.in);
        while(true) {
            try {
                Thread.sleep(1);
                String input = sc.nextLine();
                processCommand(input);
            } catch(InterruptedException e) {

            }
        }   
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
