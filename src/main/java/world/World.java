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
import java.nio.DoubleBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;

import collision.AABB;
import entity.Entity;
import entity.Transform;
import entity.WorkerDisplay;
import io.Window;
import render.Camera;
import render.Shader;


public class World {
    private int viewX;
    private int viewY;
    private byte[] tiles;
    private AABB[] boudingBoxes;
    private List<Entity> entities;
    private int width;
    private int height;
    private int scale;
    private Matrix4f world;
    private WorkerDisplay worker;
    
    public World(String world, Camera camera) {
        try {
            BufferedImage tileSheet = ImageIO.read(new File("./levels/" + world + "/tiles.png"));
            BufferedImage entitySheet = ImageIO.read(new File("./levels/" + world + "/entities.png"));

            width = tileSheet.getWidth();
            height = tileSheet.getHeight();
            scale = 16;
            //System.out.println(width + " ; " + height);
            this.world = new Matrix4f().setTranslation(new Vector3f(0));
            this.world.scale(scale);

            int[] colorTileSheet = tileSheet.getRGB(0, 0, width, height, null, 0, width);
            int[] colorEntitySheet = entitySheet.getRGB(0, 0, width, height, null, 0, width);

            tiles = new byte[width * height];
            boudingBoxes = new AABB[width * height];
            entities = new ArrayList<Entity>();

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
                            case 1: worker = new WorkerDisplay(transform); 
                                    entities.add(worker); 
                                    camera.getPosition().set(transform.pos.mul(-scale, new Vector3f()));

                                    break;
                            default : break;
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

        for (Entity entity : entities) {
            entity.render(shader, cam, this);
        }
    }

    public void update(float delta, Window window, Camera camera) {
        for (Entity entity : entities) {
            entity.update(delta, window, camera, this);
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

        /*  DoubleBuffer posX = BufferUtils.createDoubleBuffer(1);
            DoubleBuffer posY = BufferUtils.createDoubleBuffer(1);

            GLFW.glfwGetCursorPos(window.getWindow(), posX, posY);

            int posXToTileX = (int)((posX.get(0)/window.getWidth()) * width);
            int posYToTileY = (int)((posY.get(0)/window.getHeight()) * height);
            
            System.out.println(posXToTileX + " ; " + posYToTileY);
           
            if (posXToTileX < width/2) {
                posXToTileX = (posXToTileX/2) * -1;
            } else {
                posXToTileX = posXToTileX/2;
            }

            if (posYToTileY < height/2) {
                posYToTileY = (posYToTileY/2) * -1;
            } else {
                posYToTileY = (posYToTileY/2);
            } */

    public WorkerDisplay getWorkerDisplay() {
        return worker;
    }
}
