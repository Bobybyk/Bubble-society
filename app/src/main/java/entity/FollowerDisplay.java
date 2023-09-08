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
import render.Texture;
import render.TextureManager;
import render.TextureName;

public class FollowerDisplay extends Entity {

    private Texture[] idleTexture = TextureManager.getTexture(TextureName.FOLLOWER_IDLE);
    private Texture[] movementTexture = TextureManager.getTexture(TextureName.FOLLOWER_MOVEMENT);
    private Texture[] dyingTexture = TextureManager.getTexture(TextureName.FOLLOWER_DYING);
    private Texture[] deadTexture = TextureManager.getTexture(TextureName.FOLLOWER_DEAD);
    private Texture[] conversionTextures = TextureManager.getTexture(TextureName.FOLLOWER_CONVERSION);

    public FollowerDisplay(Transform transform) {
        super(ANIM_SIZE, transform);
        idle = new Animation(idleTexture);
        movment = new Animation(movementTexture);
        dying = new Animation(dyingTexture);
        dead = new Animation(deadTexture);
        conversion = new Animation(conversionTextures);

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
