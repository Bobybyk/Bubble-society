package model;

import java.util.Random;

public class Pathfinding {
    
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
