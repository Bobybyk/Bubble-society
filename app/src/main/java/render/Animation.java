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
package render;

import io.Timer;

public class Animation {
    private Texture[] frames;
    private int pointer;
    private boolean madeACycle;

    private double elapsedTime;
    private double currentTime;
    private double lastTime;
    private double fps;

    public Animation(int fps, TextureLoader textureLoaded) {
        this.pointer = 0;
        this.elapsedTime = 0;
        this.currentTime = 0;
        this.lastTime = Timer.getTime();
        this.fps = 1.0 / (double) fps;

        this.frames = textureLoaded.getTextures();
        this.madeACycle = false;
    }

    public void bind() {
        bind(0);
    }

    public void bind(int sampler) {
        this.currentTime = Timer.getTime();
        this.elapsedTime += currentTime - lastTime;

        if (elapsedTime >= fps) {
            elapsedTime = 0;
            pointer++;
        }

        if (pointer >= frames.length - 1 && madeACycle == false) {
            madeACycle = true;
        }
        if (pointer >= frames.length) {
            pointer = 0;
        }

        this.lastTime = currentTime;

        frames[pointer].bind(sampler);
    }

    public boolean hasMadeACycle() {
        return madeACycle;
    }
}
