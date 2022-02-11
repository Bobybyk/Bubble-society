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
import application.debug.DebugType;


public class CommandDebug extends Command {

    public CommandDebug() {
        super("110");
    }

    @Override
    public void execute(String[] args) {
        if (args.length == 3) {
            int value;
            String type;
            try {
                value = Integer.parseInt(args[2]);
                if (args[1] instanceof String) {
                    type = args[1];
                    switch(type) {
                        case "ERROR": 
                            if (value == 0) {
                                System.out.println("debug ERROR mode : disabled");  
                                DebugLogger.typeMap.replace(DebugType.ERROR, false);
                            }
                            else if (value == 1) {
                                System.out.println("debug ERROR mode : enabled");  
                                DebugLogger.typeMap.replace(DebugType.ERROR, true);
                            }
                            return;
                        case "ALL": 
                            if (value == 0) {
                                System.out.println("debug ALL modes : disabled");  
                                DebugLogger.typeMap.replace(DebugType.ALL, false);
                            }
                            else if (value == 1) {
                                System.out.println("debug ALL modes : enabled");  
                                DebugLogger.typeMap.replace(DebugType.ALL, true);
                            }
                            return;
                        case "UI": 
                            if (value == 0) {
                                System.out.println("debug UI mode : disabled");  
                                DebugLogger.typeMap.replace(DebugType.UI, false);
                            }
                            else if (value == 1) {
                                System.out.println("debug UI mode : enabled");  
                                DebugLogger.typeMap.replace(DebugType.UI, true);
                            }
                            return;
                        case "UIEXT": 
                            if (value == 0) {
                                System.out.println("debug extended UI mode : disabled");  
                                DebugLogger.typeMap.replace(DebugType.UIEXT, false);
                            }
                            else if (value == 1) {
                                System.out.println("debug extended UI mode : enabled");  
                                DebugLogger.typeMap.replace(DebugType.UIEXT, true);
                            }
                            return;
                        case "SYS": 
                            if (value == 0) {
                                System.out.println("debug SYS mode : disabled");  
                                DebugLogger.typeMap.replace(DebugType.SYS, false);
                            }
                            else if (value == 1) {
                                System.out.println("debug SYS mode : enabled");  
                                DebugLogger.typeMap.replace(DebugType.SYS, true);
                            }
                            return;
                        case "ENTITIES": 
                            if (value == 0) {
                                System.out.println("debug ENTITIES mode : disabled");  
                                DebugLogger.typeMap.replace(DebugType.ENTITIES, false);
                            }
                            else if (value == 1) {
                                System.out.println("debug ENTITIES mode : enabled");  
                                DebugLogger.typeMap.replace(DebugType.ENTITIES, true);
                            }
                            return;
                    }
                }
            } catch(NumberFormatException e) {}
        }
        // list every debug type state
        else if (args.length == 2) {
            if (args[1].equals("LIST")) {
                for(int i = -1 ; i<DebugLogger.typeMap.size()-1 ; i++) {
                    System.out.println("Debug type (" + DebugLogger.typeMapDirectory.get(i) + ", " + i + ") : " + DebugLogger.typeMap.get(i));
                }
                return;
            }
        }
        System.out.println("Command syntax error");  
    }
    
}
