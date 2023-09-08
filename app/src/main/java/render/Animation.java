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

    public Animation(Texture[] textures) {

        this.pointer = 0;
        this.elapsedTime = 0;
        this.currentTime = 0;
        this.lastTime = Timer.getTime();
        this.fps = 1.0 / (double) textures.length;

        this.frames = textures;
        this.madeACycle = false;
    }

    /** bind the current frame */
    public void bind() {
        bind(0);
    }

    /**
     * bind the current frame with a sampler
     *
     * @param sampler sampler
     */
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

    /**
     * tell if the animation has made a cycle
     *
     * @return true if made a cycle, false otherwise
     */
    public boolean hasMadeACycle() {
        return madeACycle;
    }
}
