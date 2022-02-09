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
