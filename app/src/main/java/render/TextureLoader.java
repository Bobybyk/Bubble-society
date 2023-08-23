package render;

public class TextureLoader {
    private Texture[] frames;

    /**
     * @brief create a texture loader
     * @param amount amount of textures
     * @param filename path to the textures
     */
    public TextureLoader(int amount, String filename) {
        this.frames = new Texture[amount];
        for (int i = 0; i < amount; i++) {
            this.frames[i] = new Texture(filename + "/" + i + ".png");
        }
    }

    /**
     * @brief get the textures
     * @return textures as an array of textures
     */
    public Texture[] getTextures() {
        return frames;
    }
}
