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

import application.shell.Console;
import application.system.EngineCreator;

public class DebugLogger {
    public static boolean debug = false;
    private static EngineCreator engine;

    public static void createGraphicEngine() {
        if (EngineCreator.gcEngine == null) {
            engine = new EngineCreator();
            engine.start();
        }
    }

    public static void destroyGraphicEngine() {
        engine.stop();
        EngineCreator.gcEngine = null;
    }

    public static void print(int debugType, String str) {
        if (debug) {
            System.out.println(str);
            Console.layout();
        }
    }
    
}
