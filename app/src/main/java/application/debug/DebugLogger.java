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
import io.NewWindow;
import java.util.HashMap;

public class DebugLogger {
    private static EngineCreator engine;
    // map with debug type (as integer) and related boolean value
    public static HashMap<Integer, Boolean> typeMap = new HashMap<Integer, Boolean>();
    // copy of typeMap with string macro instead of boolean value
    public static HashMap<Integer, String> typeMapDirectory = new HashMap<Integer, String>();

    private static String previousDebugMessage = "";

    public static void createGraphicEngine() {
        new Thread(
                        new Runnable() {
                            @Override
                            public void run() {
                                new NewWindow("Society");
                            }
                        })
                .start();
    }

    public static void setTypeMap() {
        typeMap.put(DebugType.ERROR, false);
        typeMapDirectory.put(DebugType.ERROR, "ERROR");
        typeMap.put(DebugType.ALL, false);
        typeMapDirectory.put(DebugType.ALL, "ALL");
        typeMap.put(DebugType.UI, false);
        typeMapDirectory.put(DebugType.UI, "UI");
        typeMap.put(DebugType.UIEXT, false);
        typeMapDirectory.put(DebugType.UIEXT, "UIEXT");
        typeMap.put(DebugType.SYS, false);
        typeMapDirectory.put(DebugType.SYS, "SYS");
        typeMap.put(DebugType.ENTITIES, false);
        typeMapDirectory.put(DebugType.ENTITIES, "ENTITIES");
        typeMap.put(DebugType.RESIZE, false);
        typeMapDirectory.put(DebugType.RESIZE, "RESIZE");
        typeMap.put(DebugType.ZONE, false);
        typeMapDirectory.put(DebugType.ZONE, "ZONE");
        typeMap.put(DebugType.MOUSE, false);
        typeMapDirectory.put(DebugType.MOUSE, "ZONE");
    }

    public static void destroyGraphicEngine() {
        engine.stop();
        EngineCreator.gcEngine = null;
        DebugType.gc = null;
    }

    // print debug message if debug type is enable
    public static void print(int debugType, String str) {
        if (!str.equals(previousDebugMessage)) {
            if (typeMap.get(debugType)) {
                System.out.println(str);
                Console.layout();
                previousDebugMessage = str;
            } else if (typeMap.get(DebugType.ALL)) {
                System.out.println(str);
                Console.layout();
                previousDebugMessage = str;
            }
        }
    }
}
