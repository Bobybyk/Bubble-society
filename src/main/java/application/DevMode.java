package application;

import application.system.EngineCreator;

public class DevMode {
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
    
}
