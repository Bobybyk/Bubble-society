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
package application.debug;

import application.system.VisualEngine;

public class DebugType {
    public static int ERROR = -1;
    public static int ALL = 0;
    public static int UI = 1;
    public static int UIEXT = 2;
    public static int SYS = 3;
    public static int ENTITIES = 4;
    public static int RESIZE = 5;
    public static int ZONE = 6;
    public static int MOUSE = 7;
    public static VisualEngine gc;
}
