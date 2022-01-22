package model.visual_engine;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;

public class VBO {
    private int draw_count;
    private int vertexId;
    private int textureId;

    public VBO(float[] vertices, float[] textureCoords) {
        this.draw_count = vertices.length / 3;

        this.vertexId = glGenBuffers();

        glBindBuffer(GL_ARRAY_BUFFER, vertexId); 
        glBufferData(GL_ARRAY_BUFFER, createBuffer(vertices), GL_STATIC_DRAW);

        textureId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, textureId);
        glBufferData(GL_ARRAY_BUFFER, createBuffer(textureCoords), GL_STATIC_DRAW);

        glBindBuffer(GL_ARRAY_BUFFER, 0); 
    }

    public void render() {
        glEnableClientState(GL_VERTEX_ARRAY);
        glEnableClientState(GL_TEXTURE_COORD_ARRAY);
        
        glBindBuffer(GL_ARRAY_BUFFER, vertexId);
        glVertexPointer(3, GL_FLOAT, 0, 0); // 3 for 3D

        glBindBuffer(GL_ARRAY_BUFFER, textureId);
        glTexCoordPointer(2, GL_FLOAT, 0, 0);

        glDrawArrays(GL_TRIANGLES, 0, draw_count);

        glBindBuffer(GL_ARRAY_BUFFER, 0);

        glDisableClientState(GL_VERTEX_ARRAY);
        glDisableClientState(GL_TEXTURE_COORD_ARRAY);
    }

    private FloatBuffer createBuffer(float[] data) {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }
}
