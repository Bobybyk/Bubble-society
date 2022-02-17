package render;

public class TextureLoader {
 
    public TextureLoader(int amount, String filename) {
        for(int i = 0 ; i < amount ; i++) {
            new Texture(filename + "/" + i + ".png");
        }
    }
}
