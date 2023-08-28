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
package world;

import application.debug.DebugLogger;
import application.debug.DebugType;
import collision.AABB;
import entity.Entity;
import entity.FollowerDisplay;
import entity.InsurgentDisplay;
import entity.ShiftingVector;
import entity.Transform;
import game.Game;
import game.worker.Follower;
import game.worker.Worker;
import io.Window;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import javax.imageio.ImageIO;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import render.Camera;
import render.Shader;

public class World {

    private int viewX;
    private int viewY;
    private int width;
    private int height;
    private int scale;

    private byte[] tiles;

    private AABB[] boudingBoxes;

    private HashMap<Entity, ShiftingVector> entitiesBindShiftCoord =
            new HashMap<Entity, ShiftingVector>();

    private ArrayList<Entity> alive = new ArrayList<Entity>();
    private ArrayList<Entity> dead = new ArrayList<Entity>();

    private List<Entity> entities = new ArrayList<Entity>();

    private HashMap<Integer, Zone> bordersInterval = new HashMap<>();

    private Matrix4f world;
    private Entity entity;
    private boolean firstEntitiesSpecDefined = false;

    private static int COOLDOWN_MIN = 150;
    private static int COOLDOWN_MAX = 300;
    private static int SHIFTING_MIN = 150;
    private static int SHIFTING_MAX = 300;
    private static int RESIZE_COEF = 32;

    /**
     * create a world
     *
     * @param world world
     * @param camera camera
     */
    public World(String world, Camera camera) {
        try {
            BufferedImage tileSheet =
                    ImageIO.read(new File("src/main/resources/levels/" + world + "/tiles.png"));
            BufferedImage entitySheet =
                    ImageIO.read(new File("src/main/resources/levels/" + world + "/entities.png"));

            this.width = tileSheet.getWidth();
            this.height = tileSheet.getHeight();
            this.scale = 16;

            this.world = new Matrix4f().setTranslation(new Vector3f(0));
            this.world.scale(scale);

            int[] colorTileSheet = tileSheet.getRGB(0, 0, width, height, null, 0, width);
            int[] colorEntitySheet = entitySheet.getRGB(0, 0, width, height, null, 0, width);

            this.tiles = new byte[width * height];
            this.boudingBoxes = new AABB[width * height];
            this.entitiesBindShiftCoord = new HashMap<Entity, ShiftingVector>();

            Transform transform;
            List<Integer> tmpBorderCordsX = new LinkedList<>();
            List<Integer> tmpBorderCordsY = new LinkedList<>();

            // level loader
            for (int y = 0; y < height; y++) {

                for (int x = 0; x < width; x++) {

                    int red = (colorTileSheet[x + y * width] >> 16) & 0xFF;
                    int entityIndex = (colorEntitySheet[x + y * width] >> 16) & 0xFF;
                    int entityAlpha = (colorEntitySheet[x + y * width] >> 24) & 0xFF;

                    Tile t;

                    try {
                        t = Tile.tiles[red];
                    } catch (ArrayIndexOutOfBoundsException e) {
                        t = null;
                    }
                    if (t != null) {
                        setTile(t, x, y);
                    }
                    if (t.isSolid()) {
                        tmpBorderCordsX.add(x * 2);
                        tmpBorderCordsY.add(-y * 2);
                    }
                    if (entityAlpha > 0) {
                        transform = new Transform();
                        // set default position in the world for the entity
                        transform.pos.x = x * 2;
                        transform.pos.y = -y * 2;
                        switch (entityIndex) {
                            case 1: /* follower */
                                entity = new FollowerDisplay(transform);
                                entitiesBindShiftCoord.put(entity, new ShiftingVector(0.0, 0.0, 0.0));
                                entities.add(entity);
                                alive.add(entity);
                                checkCollisions();
                                camera.getPosition().set(transform.pos.mul(-scale, new Vector3f()));
                                break;
                            case 2: /* insurgent */
                                break;
                            default: /* no entities */
                                break;
                        }
                    }
                }
            }
            fillBorderCoords(tmpBorderCordsX, tmpBorderCordsY);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** create a world */
    public World() {
        this.width = 128;
        this.height = 128;
        this.scale = 16;

        this.tiles = new byte[width * height];
        this.boudingBoxes = new AABB[width * height];
        this.world = new Matrix4f().setTranslation(new Vector3f(0));
        this.world.scale(scale);
    }

    /**
     * spawn an entity
     *
     * @param id id of the entity
     * @return the entity
     */
    public Entity spawnEntity(int id) {
        Transform transform = new Transform();
        transform.pos.x = 120;
        transform.pos.y = -120;
        switch (id) {
            case 1:
                entity = new FollowerDisplay(transform);
                entitiesBindShiftCoord.put(entity, new ShiftingVector(0.0, 0.0, 0.0));
                entities.add(entity);
                alive.add(entity);
                break;
            case 2:
                entity = new InsurgentDisplay(transform);
                entitiesBindShiftCoord.put(entity, new ShiftingVector(0.0, 0.0, 0.0));
                entities.add(entity);
                alive.add(entity);
                break;
        }

        return entity;
    }

    /**
     * get the tile at a specific position
     *
     * @param pos position of the tile
     */
    public void defineZoneBorder(Vector3f pos) {
        setTile(Tile.zoneTile, (int) -(pos.x), (int) (pos.y));
    }

    /**
     * kill the specified entity
     *
     * @param entity entity to kill
     */
    public void killEntity(Entity entity) {
        dead.add(entity);
        alive.remove(entity);
    }

    /**
     * define the first entities spec
     *
     * @param game game
     */
    private void setFirstEntitiesSpec(Game game) {
        for (HashMap.Entry<Entity, ShiftingVector> entity : entitiesBindShiftCoord.entrySet()) {
            game.defineEntity(entity.getKey());
        }
        firstEntitiesSpecDefined = true;
    }

    /**
     * calculate view values (x, y) with the window size
     *
     * @param window
     */
    public void calculateView(Window window) {
        viewX = (window.getWidth() / (scale * 2)) + 4;
        viewY = (window.getHeight() / (scale * 2)) + 4;
    }

    /**
     * get the world matrix
     *
     * @return world matrix
     */
    public Matrix4f getWorldMatrix4f() {
        return world;
    }

    /**
     * Returns the tile on which the player clicked regardless of where the camera is positioned
     *
     * @param camera The camera object which must be used as reference for placement
     * @param window The window (which the player clicks onto) in which the tile must be placed
     * @return The coordinates of the tile the player clicked onto
     */
    public Vector3f getMousePositionOnWorld(Camera camera, Window window) {

        /*
         * The horizontal position of the Top-left corner of the camera in the world
         *
         * Obtained by subtracting half of the width of the window to position of the center of the window
         */
        float cameraPosX = camera.getPosition().x + (window.getWidth() / 2);

        /*
         * The vertical position of the Top-left corner of the camera in the world
         *
         * Obtained by subtracting half of the height of the window to position of the center of the window
         */
        float cameraPosY = camera.getPosition().y - (window.getHeight() / 2);

        /**
         * The horizontal coordinate of the mouse click location on the whole map
         *
         * <p>Obtained by adding (the camera position) to (the mouse click position relative to the
         * window)
         */
        float clickPosX = (-window.getMousePosition()[0] + cameraPosX);

        /**
         * The vertical coordinate of the mouse click location on the whole map
         *
         * <p>Obtained by adding (the camera position) to (the mouse click position relative to the
         * window)
         */
        float clickPosY = (window.getMousePosition()[1] + cameraPosY);

        /** Scale coordinates to fit zoom level */
        float scaledPosX = clickPosX / (scale * 2);
        float scaledPosY = clickPosY / (scale * 2);

        /** Round coordinates to obtain integer values */
        int mousePositionOnWorldX = Math.round(scaledPosX);
        int mousePositionOnWorldY = Math.round(scaledPosY);

        DebugLogger.print(
                DebugType.MOUSE,
                "X MOUSE : "
                        + mousePositionOnWorldX
                        + " ; Y MOUSE : "
                        + mousePositionOnWorldY
                        + "\n"
                        + "X CAMERA : "
                        + cameraPosX
                        + " ; Y CAMERA : "
                        + cameraPosY);

        return new Vector3f(mousePositionOnWorldX, mousePositionOnWorldY, 0);
    }

    /**
     * render tile
     *
     * @param render tile renderer
     * @param shader shader
     * @param cam camera
     */
    public void render(TileRenderer render, Shader shader, Camera cam) {
        int posX = (int) cam.getPosition().x / (scale * 2);
        int posY = (int) cam.getPosition().y / (scale * 2);

        for (int i = 0; i < viewX; i++) {
            for (int j = 0; j < viewY; j++) {
                Tile t = getTile(i - posX - (viewX / 2) + 1, j + posY - (viewY / 2));
                if (t != null) {
                    render.renderTile(
                            t, i - posX - (viewX / 2) + 1, -j - posY + (viewY / 2), shader, world, cam);
                }
            }
        }

        for (HashMap.Entry<Entity, ShiftingVector> entity : entitiesBindShiftCoord.entrySet()) {
            entity.getKey().render(shader, cam, this);
        }
    }

    /**
     * update entities on the window
     *
     * @param delta delta between the last update and now
     * @param game game
     * @param window window
     */
    public void entitiesUpdate(float delta, Game game, Window window) {

        // define entities spec for those which could be spawned by entities map parsing
        if (firstEntitiesSpecDefined == false) {
            setFirstEntitiesSpec(game);
        }

        Random rand = new Random();
        int x, y;
        double length;
        List<Entity> entitiesToConvert = new ArrayList<Entity>();

        // for each entity alive
        for (Entity entityAlive : alive) {

            // declare stats for initial entities (given by the entities.png)
            if (!game.getWorkerBindView().get(entityAlive).getWanderState()) {
                entityAlive.wanderUpdate(delta, entityShiftVector(entityAlive));
            }

            // give shifting vector components (coords, translation...)
            if (entityTranslation(entityAlive) <= 0 && entityShiftX(entityAlive) == 0) {
                x = rand.nextInt(2);
                y = rand.nextInt(2);
                length = SHIFTING_MIN + (SHIFTING_MAX - SHIFTING_MIN) * rand.nextDouble();
                x = x == 0 ? -2 : 2;
                y = y == 0 ? -2 : 2;
                entitiesBindShiftCoord.put(entityAlive, new ShiftingVector((double) x, (double) y, length));
                continue;
            }

            // decrease translation if it's not equals to 0
            if (entityTranslation(entityAlive) > 0) {
                entityShiftVector(entityAlive).decreaseTranslation();
            }

            // reset coords and define a new translation for shifting vector
            if (entityTranslation(entityAlive) <= 0 && entityShiftX(entityAlive) != 0) {
                length = COOLDOWN_MIN + (COOLDOWN_MAX - COOLDOWN_MIN) * rand.nextDouble();
                entityShiftVector(entityAlive).resetX();
                entityShiftVector(entityAlive).resetY();
                entityShiftVector(entityAlive).setTranslation(length);
            }

            // if entity hasn't anought anymore, add it to the conversion list
            if (workerInstanceOfFollower(game, entityAlive)
                    && entityHpLowerThanEntityWill(game, entityAlive)) {
                entitiesToConvert.add(entityAlive);
            }

            // update entity position and animation
            entityAlive.wanderUpdate(delta, entitiesBindShiftCoord.get(entityAlive));
        }

        for (int i = 0; i < entitiesToConvert.size(); i++) {

            DebugLogger.print(DebugType.ENTITIES, "CONVERSION");

            if (entitiesToConvert.get(i).getCycle(FollowerDisplay.ANIM_CONVERSION)) {
                game.changeWorkerState(
                        game.removeEntity(entitiesToConvert.get(i)),
                        entityConversion(entitiesToConvert.get(i)));
            } else {
                ((FollowerDisplay) entitiesToConvert.get(i)).conversionUpdate();
            }
        }

        // parse dead entities list and update their animation
        for (Entity deadEntities : dead) {
            deadEntities.deathUpdate();
        }

        // check colisions between every entity
        checkCollisions();
    }

    /**
     * check if the entity is an instance of follower
     *
     * @param game game
     * @param entity entity to check
     * @return true if the entity is an instance of follower, false otherwise
     */
    public boolean workerInstanceOfFollower(Game game, Entity entity) {
        return entityAlive(game, entity) instanceof Follower;
    }

    /**
     * get the current shifting vector of an entity
     *
     * @param entity entity
     * @return shifting vector of the entity
     */
    public ShiftingVector entityShiftVector(Entity entity) {
        return entitiesBindShiftCoord.get(entity);
    }

    /**
     * get the current x coord of the shifting vector of an entity
     *
     * @param entity entity
     * @return x coord of the shifting vector of the entity
     */
    public double entityShiftX(Entity entity) {
        return entitiesBindShiftCoord.get(entity).getX();
    }

    /**
     * get the current translation of an entity
     *
     * @param entity entity
     * @return translation of the entity
     */
    public double entityTranslation(Entity entity) {
        return entitiesBindShiftCoord.get(entity).getTranslation();
    }

    /**
     * check if the hp of an entity is lower than its will
     *
     * @param game game
     * @param entity entity
     * @return true if the hp of the entity is lower than its will, false otherwise
     */
    public boolean entityHpLowerThanEntityWill(Game game, Entity entity) {
        return entityHp(game, entity) < entityWill(game, entity);
    }

    /**
     * get the will of an entity
     *
     * @param game game
     * @param entity entity
     * @return will of the entity
     */
    public int entityWill(Game game, Entity entity) {
        return game.getWorkerBindView().get(entity).getWill();
    }

    /**
     * get the hp of an entity
     *
     * @param game game
     * @param entity entity
     * @return hp of the entity
     */
    public int entityHp(Game game, Entity entity) {
        return game.getWorkerBindView().get(entity).getHp();
    }

    /**
     * check if the entity is alive in the game
     *
     * @param game game
     * @param entity entity
     * @return true if the entity is alive in the game, false otherwise
     */
    public Worker entityAlive(Game game, Entity entity) {
        return game.getWorkerBindView().get(entity);
    }

    /**
     * update the world
     *
     * @param delta delta between the last update and now
     * @param window window
     * @param camera camera
     */
    public void update(float delta, Window window, Camera camera) {
        List<Entity> entities = new ArrayList<Entity>();
        for (HashMap.Entry<Entity, ShiftingVector> entity : entitiesBindShiftCoord.entrySet()) {
            entity.getKey().update(delta, window, camera, this);
            entities.add(entity.getKey());
        }
        // collision between tiles and entities
        for (int i = 0; i < entities.size(); i++) {
            entities.get(i).collideWithTiles(this);
            for (int j = i + 1; j < entities.size(); j++) {
                entities.get(i).collideWithEntity(entities.get(j));
            }
            entities.get(i).collideWithTiles(this);
        }
    }

    /** check colisions between every entity */
    private void checkCollisions() {
        // collision between tiles and entities
        for (int i = 0; i < entities.size(); i++) {
            entities.get(i).collideWithTiles(this);
            for (int j = i + 1; j < entities.size(); j++) {
                entities.get(i).collideWithEntity(entities.get(j));
            }
            entities.get(i).collideWithTiles(this);
        }
    }

    /**
     * check if the specified entity is in a zone
     *
     * @param entity entity
     * @param game game
     * @return true if the entity is in a zone, false otherwise
     */
    public boolean entityIsInZone(Entity entity, Game game) {
        if (!game.getWorkerBindView().get(entity).getZone()) {
            return false;
        }
        if (entity.getTransform().getPosition().x
                < bordersInterval.get(game.getWorkerBindView().get(entity).getZoneId()).getX1()) {
            return false;
        }
        if (entity.getTransform().getPosition().x
                > bordersInterval.get(game.getWorkerBindView().get(entity).getZoneId()).getX2()) {
            return false;
        }
        if (entity.getTransform().getPosition().y
                < -bordersInterval.get(game.getWorkerBindView().get(entity).getZoneId()).getY1()) {
            return false;
        }
        if (entity.getTransform().getPosition().y
                > -bordersInterval.get(game.getWorkerBindView().get(entity).getZoneId()).getY2()) {
            return false;
        }
        return true;
    }

    /**
     * convert an entity
     *
     * @param entity entity to convert
     * @return the converted entity
     */
    public Entity entityConversion(Entity entity) {
        Entity trans = null;
        ShiftingVector entityShiftCoords = entitiesBindShiftCoord.get(entity);
        if (entity instanceof FollowerDisplay) {
            trans = new InsurgentDisplay(entity.getTransform());
        }
        if (entity instanceof InsurgentDisplay) {
            trans = new FollowerDisplay(entity.getTransform());
        }
        entities.remove(entity);
        entities.add(trans);
        alive.remove(entity);
        alive.add(trans);
        entitiesBindShiftCoord.remove(entity);
        entitiesBindShiftCoord.put(trans, entityShiftCoords);
        return trans;
    }

    /**
     * remove an entity of the world
     *
     * @param entity entity to remove
     */
    public void removeEntity(Entity entity) {
        entities.remove(entity);
        entitiesBindShiftCoord.remove(entity);
        entity = null;
    }

    /**
     * to avoid going out the map
     *
     * @param camera camera
     * @param window window
     */
    public void correctCamera(Camera camera, Window window) {
        Vector3f pos = camera.getPosition();

        int w = -width * scale * 2;
        int h = height * scale * 2;

        if (pos.x > -(window.getWidth() / 2) + scale) {
            pos.x = -(window.getWidth() / 2) + scale;
        }
        if (pos.x < w + (window.getWidth() / 2) + scale) {
            pos.x = w + (window.getWidth() / 2) + scale;
        }
        if (pos.y < (window.getHeight() / 2) - scale) {
            pos.y = (window).getHeight() / 2 - scale;
        }
        if (pos.y > h - (window.getHeight() / 2) - scale) {
            pos.y = h - (window.getHeight() / 2) - scale;
        }
    }

    /**
     * to avoid going out the map
     *
     * @param window window
     */
    public void correctMapSize(Window window) {
        Vector3f pos = null;

        for (int i = 0; i < entities.size(); i++) {
            pos = entities.get(i).getTransform().getPosition();
            DebugLogger.print(DebugType.ENTITIES, "X : " + pos.x + " ; Y : " + pos.y);
            if (pos.x > width * 2) {
                DebugLogger.print(DebugType.RESIZE, "RIGHT : " + pos.x + " ; " + width);
                this.width += RESIZE_COEF;
                this.height += RESIZE_COEF;
                repaintTiles();
            }
            if (pos.x < 0) {
                DebugLogger.print(
                        DebugType.RESIZE, "LEFT : " + pos.x + " ; " + width + " - can't be resized");
                pos.x = 0;
            }
            if (pos.y < -height * 2) {
                DebugLogger.print(DebugType.RESIZE, "DOWN : " + pos.y + " ; " + height);
                this.width += RESIZE_COEF;
                this.height += RESIZE_COEF;
                repaintTiles();
            }
            if (pos.y > 0) {
                DebugLogger.print(
                        DebugType.RESIZE, "UP : " + pos.y + " ; " + height + " - can't be resized");
                pos.y = 0;
            }
        }
    }

    /** repaint tiles */
    public void repaintTiles() {
        byte[] tilesBis = this.tiles;
        this.tiles = new byte[width * height];
        setBoundingBoxes();
        for (int y = RESIZE_COEF; y < height - RESIZE_COEF; y++) {
            for (int x = RESIZE_COEF; x < width - RESIZE_COEF; x++) {
                setTile(
                        Tile.tiles[tilesBis[(x - RESIZE_COEF) + (y - RESIZE_COEF) * (width - RESIZE_COEF)]],
                        x - RESIZE_COEF,
                        y - RESIZE_COEF);
            }
        }
    }

    /**
     * set a tile at a specific position
     *
     * @param tile tile to set
     * @param x x position
     * @param y y position
     */
    public void setTile(Tile tile, int x, int y) {
        tiles[x + y * width] = tile.getId();
        if (tile.isSolid()) {
            boudingBoxes[x + y * width] = new AABB(new Vector2f(x * 2, -y * 2), new Vector2f(1, 1));
        } else {
            boudingBoxes[x + y * width] = null;
        }
    }

    /** set bounding boxes */
    private void setBoundingBoxes() {
        boudingBoxes = new AABB[width * height];
    }

    /**
     * fill border coords
     *
     * @param tmpBorderCordsX tmp border coords x
     * @param tmpBorderCordsY tmp border coords y
     */
    private void fillBorderCoords(List<Integer> tmpBorderCordsX, List<Integer> tmpBorderCordsY) {
        bordersInterval.put(
                bordersInterval.size(),
                new Zone(
                        tmpBorderCordsX.get(0),
                        tmpBorderCordsX.get(tmpBorderCordsX.size() - 1),
                        tmpBorderCordsY.get(0),
                        tmpBorderCordsY.get(tmpBorderCordsY.size() - 1)));
        DebugLogger.print(
                DebugType.ZONE,
                "new Zone Created : \n    X1 : "
                        + tmpBorderCordsX.get(0)
                        + "\n   X2 : "
                        + tmpBorderCordsX.get(tmpBorderCordsX.size() - 1)
                        + "\n   Y1 : "
                        + tmpBorderCordsY.get(0)
                        + "\n   Y2 : "
                        + tmpBorderCordsY.get(tmpBorderCordsY.size() - 1));
    }

    /**
     * get the tile at a specific position
     *
     * @param x x position
     * @param y y position
     * @return tile at the specific position
     */
    public Tile getTile(int x, int y) {
        try {
            return Tile.tiles[tiles[x + y * width]];
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    /**
     * get the tile bounding box at a specific position
     *
     * @param x x position
     * @param y y position
     * @return tile bounding box at the specific position
     */
    public AABB getTileBoundingBox(int x, int y) {
        try {
            return boudingBoxes[x + y * width];
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    /**
     * get the world scale
     *
     * @return world scale
     */
    public int getScale() {
        return scale;
    }

    /**
     * set the world scale (zoom in or zoom out)
     *
     * @param coef coef to add to the scale
     * @param window window
     * @param camera camera
     */
    public void setScale(int coef, Window window, Camera camera) {
        if (scale + coef >= 16) {
            this.scale += coef;
            this.world = new Matrix4f().setTranslation(new Vector3f());
            this.world.scale(scale);

            Vector3f mousePos = getMousePositionOnWorld(camera, window);
            int xPos = (int) (mousePos.x * (scale * 2));
            int yPos = (int) (mousePos.y * (scale * 2));
        }
    }

    /**
     * get the entity display
     *
     * @return entity display
     */
    public Entity getEntityDisplay() {
        return entity;
    }
}
