package entity;

import render.Animation;
import render.Texture;
import render.TextureManager;
import render.TextureName;

public class InsurgentDisplay extends Entity {

    private Texture[] idleTexture = TextureManager.getTexture(TextureName.INSURGENT_IDLE);
    private Texture[] movementTexture = TextureManager.getTexture(TextureName.INSURGENT_MOVEMENT);
    private Texture[] dyingTexture = TextureManager.getTexture(TextureName.INSURGENT_DYING);
    private Texture[] deadTexture = TextureManager.getTexture(TextureName.INSURGENT_DEAD);

    public InsurgentDisplay(Transform transform) {
        super(ANIM_SIZE, transform);
        idle = new Animation(idleTexture);
        movment = new Animation(movementTexture);
        dying = new Animation(dyingTexture);
        dead = new Animation(deadTexture);

        setAnimation(ANIM_IDLE, idle);
        setAnimation(ANIM_MOVE, movment);
        setAnimation(ANIM_DYING, dying);
        setAnimation(ANIM_DEAD, dead);

        animationBindId.put(ANIM_IDLE, idle);
        animationBindId.put(ANIM_MOVE, movment);
        animationBindId.put(ANIM_DYING, dying);
        animationBindId.put(ANIM_DEAD, dead);
    }
}
