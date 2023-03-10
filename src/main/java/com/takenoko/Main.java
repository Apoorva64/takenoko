package com.takenoko;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.takenoko.bot.ColletBot;
import com.takenoko.engine.BotManager;
import com.takenoko.engine.GameEngine;
import com.takenoko.ui.ConsoleUserInterface;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;

public class Main {
    @Parameter(
            names = {"--2thousands"},
            description =
                    "Run 2*1000 games: best_Bot VS second_best_Bot && best_Bot VS best_Bot | no"
                            + " logs")
    private boolean thousands = false;

    @Parameter(
            names = {"--demo"},
            description = "Run one game as Demo | show all logs")
    private boolean demo = false;

    @Parameter(
            names = {"--csv"},
            description = "Append the results in a csv file")
    private boolean csv = false;

    private final Logger logger = LogManager.getLogger(Main.class);
    private static final int TWO_THOUSANDS_NB_GAMES = 1000;

    public static void main(String[] args) {
        Main main = new Main();
        JCommander.newBuilder().addObject(main).build().parse(args);
        main.run();
    }

    @SuppressWarnings("java:S4792")
    public void run() {
        GameEngine gameEngine = new GameEngine();
        LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
        Configuration config = ctx.getConfiguration();
        LoggerConfig loggerConfig = config.getLoggerConfig(LogManager.ROOT_LOGGER_NAME);
        if (thousands) {
            loggerConfig.setLevel(ConsoleUserInterface.GAMESTATS);
            ctx.updateLoggers();
            gameEngine.runGame(TWO_THOUSANDS_NB_GAMES);
            GameEngine gameEngine2 =
                    new GameEngine(
                            new ArrayList<>(
                                    List.of(
                                            new BotManager(new ColletBot(), "Collet Bot 01"),
                                            new BotManager(new ColletBot(), "Collet Bot 02"))));
            gameEngine2.runGame(TWO_THOUSANDS_NB_GAMES, csv);
        } else if (demo) {
            loggerConfig.setLevel(Level.INFO);
            ctx.updateLoggers();
            new GameEngine().runGame(1, csv);
        } else {
            logger.log(Level.INFO, "no parameters");
        }
    }
}
