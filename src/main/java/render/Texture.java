package render;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*; // sampler

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;

import java.lang.Object;

public class Texture /* implements AutoCloseable */ {
    private int id;
    private int width;
    private int height;
    
    public Texture(String filename) {
        BufferedImage bi;
        try {
            bi = ImageIO.read(new File("./textures/" + filename));
            width = bi.getWidth();
            height = bi.getHeight();

            int[] pixels_raw = new int[width * height * 4];
            pixels_raw = bi.getRGB(0, 0, width, height, null, 0, width);

            ByteBuffer pixels = BufferUtils.createByteBuffer(width * height * 4);
            int pixel;

            for (int i=0; i<height; i++){
                for (int j=0; j< width; j++){
                 pixel = pixels_raw[i*width+j];
                 
                 pixels.put((byte) ((pixel >> 16) & 0xFF));//RED
                 pixels.put((byte) ((pixel >>  8) & 0xFF));//GREEN
                 pixels.put((byte) ((pixel      ) & 0xFF));//BLUE
                 pixels.put((byte) ((pixel >> 24) & 0xFF));//ALPHA
                 
                }
               }
            pixels.flip();
            id = glGenTextures();
            glBindTexture(GL_TEXTURE_2D, id);
            glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
            glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, pixels);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void finalyze() throws Throwable {
        glDeleteTextures(id);
        // close();
    }

    public void bind(int sampler) {
        if (sampler >= 0 && sampler <= 31) {
            glActiveTexture(GL_TEXTURE0 + sampler);
            glBindTexture(GL_TEXTURE_2D, id);
        }
    }

    /* @Override
    public void close() throws Exception {
        // TODO Auto-generated method stub
    } */
}
