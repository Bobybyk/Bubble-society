package render;

public class TextureLoader {
    private Texture[] frames;

    public TextureLoader(int amount, String filename) {
        this.frames = new Texture[amount];
        for (int i = 0; i < amount; i++) {
            this.frames[i] = new Texture(filename + "/" + i + ".png");
        }
    }

    public Texture[] getTextures() {
        return frames;
    }
}
