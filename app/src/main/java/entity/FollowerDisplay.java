/*
 *
 * Copyright (c) 2022 Matthieu Le Franc
 *
 * You are prohibited from sharing and distributing this creation without our prior authorization, more specifically:
 *
 * TO PROVIDE A COPY OF OUR GAME TO ANY THIRD PARTY;
 * TO USE THIS CREATION FOR COMMERCIAL PURPOSES;
 * TO USE THIS CREATIONS FOR PROFIT;
 * TO ALLOW ANY THIRD PARTY TO ACCESS TO THIS CREATION IN AN UNFAIR OR ABUSIVE MANNER;
 *
 */
package entity;

import render.Animation;
import render.TextureLoader;

public class FollowerDisplay extends Entity {

    private static TextureLoader idleTexures =
            new TextureLoader(20, "follower/idle"); // Animation(number of frames, fps, name without id)
    private static TextureLoader movmentTexures = new TextureLoader(15, "follower/movement");
    private static TextureLoader dyingTexures = new TextureLoader(20, "follower/dying");
    private static TextureLoader deadTexures = new TextureLoader(1, "follower/dead");
    private static TextureLoader conversionTexures = new TextureLoader(20, "follower/conversion");

    public FollowerDisplay(Transform transform) {
        super(ANIM_SIZE, transform);
        idle = new Animation(9, idleTexures);
        movment = new Animation(8, movmentTexures);
        dying = new Animation(10, dyingTexures);
        dead = new Animation(1, deadTexures);
        conversion = new Animation(9, conversionTexures);

        setAnimation(ANIM_IDLE, idle);
        setAnimation(ANIM_MOVE, movment);
        setAnimation(ANIM_DYING, dying);
        setAnimation(ANIM_DEAD, dead);
        setAnimation(ANIM_CONVERSION, conversion);

        animationBindId.put(ANIM_IDLE, idle);
        animationBindId.put(ANIM_MOVE, movment);
        animationBindId.put(ANIM_DYING, dying);
        animationBindId.put(ANIM_DEAD, dead);
        animationBindId.put(ANIM_CONVERSION, conversion);
    }
}
