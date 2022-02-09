package application.commands.command_list;

import application.commands.Command;


public class CommandHelp extends Command implements Runnable {

    public CommandHelp() {
        super("100");
    }

    @Override
    public void execute(String[] args) {
        Thread printHelp = new Thread(this);
        printHelp.start();
       try {
            printHelp.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized void run() {
        System.out.println("####################################################");
        System.out.println("#                                                  #");
        System.out.println("#                  List of commands                #");
        System.out.println("#                                                  #");
        System.out.println("#--------------------------------------------------#");
        System.out.println("#                                                  #");
        System.out.println("# DEBUG :                                          #");
        System.out.println("#   debug 0 : disable debug feedback               #");
        System.out.println("#   debug 1 : enable debug feedback                #");
        System.out.println("#   debug disable : disable debug feedback         #");
        System.out.println("#   debug enable : enable debug feedback           #");
        System.out.println("# HELP :                                           #");
        System.out.println("#   help : you just ran this command...            #");
        System.out.println("# KILL :                                           #");
        System.out.println("#   kill : kill signal (hard way, exit program)    #");
        System.out.println("#                                                  #");
        System.out.println("####################################################");
    }
    
}
