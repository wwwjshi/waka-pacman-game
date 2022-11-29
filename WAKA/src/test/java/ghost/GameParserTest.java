package ghost;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import processing.core.PApplet;

public class GameParserTest {

    GameParser parser;

    @Test
    public void parserConstructor(){
        App app = new App();
        app.main("ghost.App");
        PApplet.runSketch(new String[]{""}, app);
        app.setup();
        GameParser parser = new GameParser(app, "./config.json");
        assertNotNull(parser);
    }

    @Test
    public void endGame(){
        App app = new App();
        app.main("ghost.App");
        PApplet.runSketch(new String[]{""}, app);
        app.setup();
        GameParser parser = new GameParser(app, "./config.json");
        Waka waka = new Waka(parser);
        assertNotNull(parser);

        // test win
        assertFalse(parser.hasWin());
        parser.win();
        assertTrue(parser.win);

        // test lose
        assertFalse(parser.hasLose());
        parser.lose();
        assertTrue(parser.lose);

        // test Reset
        parser.resetAll();
        boolean win = parser.hasWin();
        boolean lose = parser.hasLose();
        boolean end = parser.gameEnd;
        boolean fmode = parser.frightenedMode;
        boolean reseted = !(win || lose || end || fmode);
        assertTrue(reseted);
    }

    @Test
    public void debugMode(){
        App app = new App();
        app.main("ghost.App");
        PApplet.runSketch(new String[]{""}, app);
        app.setup();
        GameParser parser = new GameParser(app, "./config.json");
        Waka waka = new Waka(parser);

        assertFalse(parser.debugMode); // not on initially;

        parser.setDebugMode();
        assertTrue(parser.debugMode); // on after set

        parser.setDebugMode();
        assertFalse(parser.debugMode); // another call to turn off
    }


    @Test
    public void componets(){
        App app = new App();
        app.main("ghost.App");
        PApplet.runSketch(new String[]{""}, app);
        app.setup();
        GameParser parser = new GameParser(app, "./config.json");
        Waka waka = new Waka(parser);

        // componets exist
        assertTrue(parser.ghosts != null && parser.ghosts.size() > 0);
        assertTrue(parser.walls != null && parser.walls.size() > 0);
        assertTrue(parser.fruits != null && parser.fruits.size() > 0);
        assertTrue(parser.superFruits != null && parser.superFruits.size() > 0);

    }


}

