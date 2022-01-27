package world;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import io.Window;
import render.Camera;
import render.Shader;

public class World {
    private final int view = 64;
    private byte[] tiles;
    private int width;
    private int height;
    private int scale;
    private Matrix4f world;
    
    public World() {
        this.width = 64;
        this.height = 64;
        this.scale = 16;

        this.tiles = new byte[width * height];

        this.world = new Matrix4f().setTranslation(new Vector3f(0));
        this.world.scale(scale);
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
    }

    public Tile getTile(int x, int y) {
        try  {
            return Tile.tiles[tiles[x + y * width]];
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }
}
