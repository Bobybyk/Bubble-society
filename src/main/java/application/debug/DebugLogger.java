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
package application.debug;

import java.util.HashMap;

import application.shell.Console;
import application.system.EngineCreator;

public class DebugLogger {
    private static EngineCreator engine;
    public static HashMap<Integer, Boolean> typeMap = new HashMap<Integer, Boolean>();

    public static void createGraphicEngine() {
        if (EngineCreator.gcEngine == null) {
            engine = new EngineCreator();
            engine.start();
        }
    }

    public static void setTypeMap() {
        typeMap.put(DebugType.ERROR, false);
        typeMap.put(DebugType.ALL, false);
        typeMap.put(DebugType.UI, false);
    }

    public static void destroyGraphicEngine() {
        engine.stop();
        EngineCreator.gcEngine = null;
    }

    public static void print(int debugType, String str) {
        if(typeMap.get(debugType)) {
            Console.layout();
            System.out.println(str);
        } 
        else if(typeMap.get(DebugType.ALL)) {
            Console.layout();
            System.out.println(str);
        }

    }
    
}
