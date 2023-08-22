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
package fr.lefranc.society.application.commands.command_list;

import fr.lefranc.society.application.commands.Command;
import fr.lefranc.society.application.debug.DebugLogger;

public class CommandCreateGraphicEngine extends Command {

    public CommandCreateGraphicEngine() {
        super("120");
    }

    @Override
    public void execute(String[] args) {
        DebugLogger.createGraphicEngine();
    }
}
