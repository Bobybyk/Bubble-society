package application;

public class DevMode {
    public static boolean debug = false;
    private static EngineCreator engine;

    public static void createView() {
        if (EngineCreator.gcEngine == null) {
            engine = new EngineCreator();
            engine.start();
        }
    }

    public static void destroyView() {
        engine.stop();
        EngineCreator.gcEngine = null;
    }
    
}
