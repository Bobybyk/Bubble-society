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
package game.model;

import java.util.Random;

public class Pathfinding {
    
    /*
     * return random coordinates applicated to
     * coordinates given by args
     */
    public Double[] wander(Double[] coordinates) {
        double factorX;
        double factorY;
        factorX = 0.1 + (0.3 - 0.1) * new Random().nextDouble();
        //define sign for X dimension (plus or minus)
        int sign = new Random().nextInt(2);
        if (sign == 0) {
            factorX *= -1;
        }
        factorY = 0.1 + (0.3 - 0.1) * new Random().nextDouble();
        //define sign for Y dimension (plus or minus)
        sign = new Random().nextInt(2);
        if (sign == 0) {
            factorY *= -1;
        }
        coordinates[0]+=factorX;
        coordinates[1]+=factorY;
        return coordinates;   
        
    }

}
