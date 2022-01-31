package world;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import collision.AABB;
import entity.Entity;
import entity.Transform;
import entity.WorkerDisplay;
import io.Window;
import render.Animation;
import render.Camera;
import render.Shader;

public class World {
    private final int view = 64;
    private byte[] tiles;
    private AABB[] boudingBoxes;
    private List<Entity> entities;
    private int width;
    private int height;
    private int scale;
    private Matrix4f world;
    
    public World(String world) {
        try {
            BufferedImage tileSheet = ImageIO.read(new File("./levels/" + world + "/tiles.png"));
            // BufferedImage entitySheet = ImageIO.read(new File("./levels/" + world + "_entity.png")); // unuse for now

            width = tileSheet.getWidth();
            height = tileSheet.getHeight();
            scale = 48;

            this.world = new Matrix4f().setTranslation(new Vector3f(0));
            this.world.scale(scale);

            int[] colorTileSheet = tileSheet.getRGB(0, 0, width, height, null, 0, width);

            tiles = new byte[width * height];
            boudingBoxes = new AABB[width * height];
            entities = new ArrayList<Entity>();

            // level loader
            for (int y = 0 ; y < height ; y++) {
                for (int x = 0 ; x < width ; x++) {
                    int red = (colorTileSheet[x + y * width] >> 16) & 0xFF;
                    
                    Tile t;
                    try {
                        t = Tile.tiles[red];
                    } catch (ArrayIndexOutOfBoundsException e) {
                        t = null;
                    }
                    if (t != null) {
                        setTile(t, x, y);
                    }
                }
            }

            entities.add(new WorkerDisplay(new Transform()));
            

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

    public Matrix4f getWorldMatrix4f() {
        return world;
    }

    public void render(TileRenderer render, Shader shader, Camera cam, Window window) {
        int posX = ((int)cam.getPosition().x + (window.getWidth()/2)) / (scale * 2);
        int posY = ((int)cam.getPosition().y - (window.getHeight()/2)) / (scale * 2);

        for (int i = 0 ; i < view ; i++) {
            for (int j = 0 ; j < view ; j++) {
                Tile t = getTile(i-posX, j+posY);
                if (t != null) {
                    render.renderTile(t, i-posX, -j-posY, shader, world, cam);
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
}
