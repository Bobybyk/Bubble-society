package render;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Loads and manages all textures used in the game
 *
 * <p>All frames of an animation named CHARACTER_MOVING must be put under the folder
 * resources/textures/character/moving
 */
public class TextureManager {

    private static Texture[][] textures = new Texture[TextureName.values().length][];

    /** Initializes the texture bank and loads all textures into memory */
    public static void init() {

        // for all existing textures
        for (int i = 0; i < TextureName.values().length; i++) {
            try {

                // get current texture name
                String texture_name = TextureName.values()[i].name();

                // build folder path
                String folders[] = texture_name.split("_");
                String path = "";
                for (String s : folders) {
                    path += s.toLowerCase() + "/";
                }

                // retrieve the URL
                URL texFolderPath =
                        Thread.currentThread().getContextClassLoader().getResource("textures/" + path);

                // load the folder
                File f = new File(texFolderPath.toURI());

                // get number of frames in the animation
                int count = f.listFiles().length;

                textures[i] = new Texture[count];

                // fill the slots with textures
                for (int j = 0; j < count; j++) {
                    textures[i][j] = new Texture(path + j + ".png");
                }
            } catch (NullPointerException | URISyntaxException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Retrieves a texture via its name
     *
     * @param name The name of the texture wanted
     * @return The requested texture
     */
    public static Texture[] getTexture(TextureName name) {

        return textures[name.ordinal()];
    }
}
