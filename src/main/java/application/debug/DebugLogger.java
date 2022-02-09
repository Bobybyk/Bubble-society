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
