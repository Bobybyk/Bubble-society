package render;

import static org.lwjgl.opengl.GL20.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;

import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;


public class Shader {
    private int program;
    private int vertexShader; // vertices for shader
    private int fragmentShader; // colors/texture for shader

    public Shader(String filename) {
        program = glCreateProgram();

        this.vertexShader = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertexShader, readFile(filename+".vs"));
        glCompileShader(vertexShader);
        if(glGetShaderi(vertexShader, GL_COMPILE_STATUS) != 1) {
            System.err.println(glGetShaderInfoLog(vertexShader));
            System.exit(1);
        } 

        this.fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragmentShader, readFile(filename+".fs"));
        glCompileShader(fragmentShader);
        if(glGetShaderi(fragmentShader, GL_COMPILE_STATUS) != 1) {
            System.err.println(glGetShaderInfoLog(fragmentShader));
            System.exit(1);
        } 

        glAttachShader(program, vertexShader);
        glAttachShader(program, fragmentShader);

        glBindAttribLocation(program, 0, "vertices");
        glBindAttribLocation(program, 1, "textures");
        
        glLinkProgram(program);
        if(glGetProgrami(program, GL_LINK_STATUS) != 1) {
            System.err.println(glGetProgramInfoLog(program));
            System.exit(1);
        }
        glValidateProgram(program);
        if(glGetProgrami(program, GL_VALIDATE_STATUS) != 1) {
            System.err.println(glGetProgramInfoLog(program));
            System.exit(1);
        }
    }

    public void setUniform(String name, int value) {
        int gcLocation = glGetUniformLocation(program, name); // location in the graphic card
        if (gcLocation != -1) {
            glUniform1i(gcLocation, value);
        }
    }

    public void setUniform(String uniformName, Vector4f value) {
        int gcLocation = glGetUniformLocation(program, uniformName); // location in the graphic card
        if (gcLocation != -1) {
            glUniform4f(gcLocation, value.x, value.y, value.z, value.w);
        }
    }

    public void setUniform(String name, Matrix4f value) {
        int gcLocation = glGetUniformLocation(program, name); // location in the graphic card
        FloatBuffer buffer = BufferUtils.createFloatBuffer(16); // 4*4 colums*rows of data
        value.get(buffer);
        if (gcLocation != -1) {
            glUniformMatrix4fv(gcLocation, false, buffer);
        }
    }

    public void bind() {
        glUseProgram(program);
    }

    private String readFile(String filename) {
        StringBuilder string = new StringBuilder();
        BufferedReader br;

        try {
            br = new BufferedReader(new FileReader(new File("./shaders/" + filename)));
            String line;
            while((line = br.readLine()) != null) {
                string.append(line);
                string.append("\n");
            }
            br.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
        return string.toString();
    }

}
