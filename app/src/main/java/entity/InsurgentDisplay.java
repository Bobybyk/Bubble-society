package entity;

import render.Animation;
import render.TextureLoader;

public class InsurgentDisplay extends Entity {

    private static TextureLoader idleTexures =
            new TextureLoader(20, "insurgent/idle"); // Animation(number of frames, fps, name without id)
    private static TextureLoader movmentTexures = new TextureLoader(20, "insurgent/movement");
    private static TextureLoader dyingTexures = new TextureLoader(21, "insurgent/dying");
    private static TextureLoader deadTexures = new TextureLoader(1, "insurgent/dead");

    public InsurgentDisplay(Transform transform) {
        super(ANIM_SIZE, transform);
        idle = new Animation(9, idleTexures);
        movment = new Animation(8, movmentTexures);
        dying = new Animation(10, dyingTexures);
        dead = new Animation(1, deadTexures);

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
