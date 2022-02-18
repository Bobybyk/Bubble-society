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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import collision.AABB;
import entity.Entity;
import entity.Transform;
import entity.WorkerDisplay;
import game.Game;
import io.Window;
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
    
    private HashMap<Entity, Double[]> entitiesBindShiftCoord = new HashMap<Entity, Double[]>();
    
    private ArrayList<Entity> alive = new ArrayList<Entity>();
    private ArrayList<Entity> dead = new ArrayList<Entity>();
    
    private List<Entity> entities = new ArrayList<Entity>();

    private Matrix4f world;
    private Entity entity;
    private boolean firstEntitiesSpecDefined = false;

    private static int COOLDOWN_MIN = 150;
    private static int COOLDOWN_MAX = 300;
    private static int SHIFTING_MIN = 150;
    private static int SHIFTING_MAX = 300;
    
    public World(String world, Camera camera) {
        try {
            BufferedImage tileSheet = ImageIO.read(new File("./levels/" + world + "/tiles.png"));
            BufferedImage entitySheet = ImageIO.read(new File("./levels/" + world + "/entities.png"));

            this.width = tileSheet.getWidth();
            this.height = tileSheet.getHeight();
            this.scale = 16;

            this.world = new Matrix4f().setTranslation(new Vector3f(0));
            this.world.scale(scale);

            int[] colorTileSheet = tileSheet.getRGB(0, 0, width, height, null, 0, width);
            int[] colorEntitySheet = entitySheet.getRGB(0, 0, width, height, null, 0, width);

            this.tiles = new byte[width * height];
            this.boudingBoxes = new AABB[width * height];
            this.entitiesBindShiftCoord = new HashMap<Entity, Double[]>();

            Transform transform;

            // level loader
            for (int y = 0 ; y < height ; y++) {
                for (int x = 0 ; x < width ; x++) {
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
                    if (entityAlpha > 0) {
                        transform = new Transform();
                        // set default position in the world for the entity
                        transform.pos.x = x*2;
                        transform.pos.y = -y*2;
                        switch(entityIndex) {
                            case 1: /* follower */
                                    entity = new WorkerDisplay(transform); 
                                    entitiesBindShiftCoord.put(entity, new Double[] {0.0, 0.0, 0.0}); 
                                    entities.add(entity);
                                    alive.add(entity);
                                    checkCollisions();
                                    camera.getPosition().set(transform.pos.mul(-scale, new Vector3f()));
                                    break;
                            case 2: /* insurgent */ 
                                    break;
                            default : /* no entities */
                                    break;
                        }
                    }
                }
            }     
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public World() {
        this.width = 128;
        this.height = 128;
        this.scale = 16;

        this.tiles = new byte[width * height];
        this.boudingBoxes = new AABB[width * height];
        this.world = new Matrix4f().setTranslation(new Vector3f(0));
        this.world.scale(scale);
    }

    public Entity spawnEntity(int id) {
        Transform transform = new Transform();
        transform.pos.x = 120;
        transform.pos.y = -120;
        switch(id) {
            case 1: 
                entity = new WorkerDisplay(transform);
                entitiesBindShiftCoord.put(entity, new Double[] {0.0, 0.0, 0.0});
                entities.add(entity);
                alive.add(entity);
        }

        return entity; 
    }

    public void killEntity(Entity entity) {
        dead.add(entity);
        alive.remove(entity);
    }

    private void setFirstEntitiesSpec(Game game) {
        for (HashMap.Entry<Entity, Double[]> entity : entitiesBindShiftCoord.entrySet()) {
            game.defineEntity((WorkerDisplay) entity.getKey());
        }
        firstEntitiesSpecDefined = true;
    }

    public void calculateView(Window window) {
        viewX = (window.getWidth() / (scale * 2)) + 4;
        viewY = (window.getHeight() / (scale * 2)) + 4;
    }

    public Matrix4f getWorldMatrix4f() {
        return world;
    }

    // render Tiles
    public void render(TileRenderer render, Shader shader, Camera cam) {
        int posX = (int)cam.getPosition().x / (scale * 2);
        int posY = (int)cam.getPosition().y / (scale * 2);

        for (int i = 0 ; i < viewX ; i++) {
            for (int j = 0 ; j < viewY ; j++) {
                Tile t = getTile(i-posX-(viewX/2)+1, j+posY-(viewY/2));
                if (t != null) {
                    render.renderTile(t, i-posX-(viewX/2)+1, -j-posY+(viewY/2), shader, world, cam);
                }
            }
        }

        for (HashMap.Entry<Entity, Double[]> entity : entitiesBindShiftCoord.entrySet()) {
            entity.getKey().render(shader, cam, this);
        }
    }

    public void entitiesUpdate(float delta, Game game) {
        
        // define entities spec for those which could be spawned by entities map parsing
        if(firstEntitiesSpecDefined == false) {
            setFirstEntitiesSpec(game);
        }

        // parse alive entities list and update their state (position, shifting, animation, etc.)
        Random rand = new Random();
        int x, y;
        double length;
        for (Entity entitiesAlive : alive) {
            if (!game.getWorkerBindView().get(entitiesAlive).getWanderState()) {
                entitiesAlive.wanderUpdate(delta, entitiesBindShiftCoord.get(entitiesAlive));
            }
            if (entitiesBindShiftCoord.get(entitiesAlive)[2] <= 0 && entitiesBindShiftCoord.get(entitiesAlive)[0] == 0) {
                x = rand.nextInt(2);
                y = rand.nextInt(2);
                length = SHIFTING_MIN + (SHIFTING_MAX - SHIFTING_MIN) * rand.nextDouble();
                x = x==0?-2:2;
                y = y==0?-2:2;
                entitiesBindShiftCoord.put(entitiesAlive, new Double[] {(double) x, (double) y, length});
                continue;
            }
            if (entitiesBindShiftCoord.get(entitiesAlive)[2] > 0){
                entitiesBindShiftCoord.put(entitiesAlive, new Double[] {entitiesBindShiftCoord.get(entitiesAlive)[0], entitiesBindShiftCoord.get(entitiesAlive)[1], entitiesBindShiftCoord.get(entitiesAlive)[2]-1});
            }
            if (entitiesBindShiftCoord.get(entitiesAlive)[2] <= 0 && entitiesBindShiftCoord.get(entitiesAlive)[0] != 0) {
                length = COOLDOWN_MIN + (COOLDOWN_MAX - COOLDOWN_MIN) * rand.nextDouble();
                entitiesBindShiftCoord.put(entitiesAlive, new Double[] {0.0, 0.0, length});
            }
            entitiesAlive.wanderUpdate(delta, entitiesBindShiftCoord.get(entitiesAlive));
        }

        // parse dead entities list and update their animation
        for(Entity deadEntities : dead) {
            deadEntities.deathUpdate();
        }

        // check colisions between every entity
        checkCollisions();
    }

    public void update(float delta, Window window, Camera camera) {
        List<Entity> entities = new ArrayList<Entity>();
        for (HashMap.Entry<Entity, Double[]> entity : entitiesBindShiftCoord.entrySet()) {
            entity.getKey().update(delta, window, camera, this);
            entities.add(entity.getKey());
        }
        // collision between tiles and entities
        for (int i = 0 ; i < entities.size() ; i++) {
            entities.get(i).collideWithTiles(this);
            for (int j = i+1 ; j < entities.size() ; j++) {
                entities.get(i).collideWithEntity(entities.get(j));
            }
            entities.get(i).collideWithTiles(this);
        }
    }

    private void checkCollisions() {
        // collision between tiles and entities
        for (int i = 0 ; i < entities.size() ; i++) {
            entities.get(i).collideWithTiles(this);
            for (int j = i+1 ; j < entities.size() ; j++) {
                entities.get(i).collideWithEntity(entities.get(j));
            }
            entities.get(i).collideWithTiles(this);
        }
    }

    public void removeEntity(Entity entity) {
        entities.remove(entity);
        entitiesBindShiftCoord.remove(entity);
        entity = null;
    }

    // to avoid going out the map
    public void correctCamera(Camera camera, Window window) {
        Vector3f pos = camera.getPosition();

        int w = -width * scale * 2;
        int h = height * scale * 2;

        if (pos.x > -(window.getWidth()/2) + scale) {
            pos.x = -(window.getWidth()/2) + scale;
        }
        if (pos.x < w + (window.getWidth()/2) + scale) {
            pos.x = w + (window.getWidth()/2) + scale;
        }
        if (pos.y < (window.getHeight()/2) - scale) {
            pos.y = (window).getHeight()/2 -scale;
        }
        if (pos.y > h - (window.getHeight()/2) - scale) {
            pos.y = h - (window.getHeight()/2) - scale;
        }
    }

    public void setTile(Tile tile, int x, int y) {
        tiles[x + y * width] = tile.getId();
        if (tile.isSolid()) {
            boudingBoxes[x + y * width] = new AABB(new Vector2f(x*2, -y*2), new Vector2f(1, 1));
        } else {
            boudingBoxes[x + y * width] = null;
        }
    }

    public Tile getTile(int x, int y) {
        try  {
            return Tile.tiles[tiles[x + y * width]];
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }
    public AABB getTileBoundingBox(int x, int y) {
        try  {
            return boudingBoxes[x + y * width];
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }
    public int getScale() {
        return scale;
    }
    public void setScale(int coef, Window window, Camera camera) {
        if (scale+coef >= 16) {
            this.scale += coef;
            this.world = new Matrix4f().setTranslation(new Vector3f(0));
            this.world.scale(scale);
        }
    }

    public Entity getEntityDisplay() {
        return entity;
    }
}
