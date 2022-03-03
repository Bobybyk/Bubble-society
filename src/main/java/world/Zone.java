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
package world;

public class Zone {
    private int X1;
    private int X2;
    private int Y1;
    private int Y2;

    public Zone(int X1, int X2, int Y1, int Y2) {
        this.X1 = X1;
        this.X2 = X2;
        this.Y1 = Y1;
        this.Y2 = Y2;
    }

    public int getX1() {
        return X1;
    }
    public int getX2() {
        return X2;
    }
    public int getY1() {
        return Y1;
    }
    public int getY2() {
        return Y2;
    }

}
