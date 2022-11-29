package ghost;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import processing.core.PApplet;

public class WakaTest {

    @Test
    public void wakaConstructor(){
        App app = new App();
        app.main("ghost.App");
        PApplet.runSketch(new String[]{""}, app);
        app.setup();
        GameParser parser = new GameParser(app, "./config.json");
        Waka waka = new Waka(parser);
        assertNotNull(waka);
    }

    @Test
    public void wakaMove(){
        App app = new App();
        app.main("ghost.App");
        PApplet.runSketch(new String[]{""}, app);
        app.setup();
        GameParser parser = new GameParser(app, "./config.json");
        Waka waka = new Waka(parser);

        // move instructions recieved
        waka.moveUp();
        assertTrue(Move.U == waka.nextMove);

        waka.moveDown();
        assertTrue(Move.D == waka.nextMove);

        waka.moveLeft();
        assertTrue(Move.L == waka.nextMove);

        waka.moveRight();
        assertTrue(Move.R == waka.nextMove);

        // check if possible to move
        assertFalse(waka.isWall(0, 0)); // initial location does not collide with wall
        assertTrue(waka.isWall(999, 999)); // out of bound
        waka.y -= 3;
        assertTrue(waka.isWall(0, 0)); // deliberately make waka to collide on wall


    }


    @Test
    public void wakaLife(){
        App app = new App();
        app.main("ghost.App");
        PApplet.runSketch(new String[]{""}, app);
        app.setup();
        GameParser parser = new GameParser(app, "./config.json");
        Waka waka = new Waka(parser);


        waka.setLife(3);
        assertTrue(waka.lives == 3);

        waka.reduceLife();
        assertTrue(waka.lives == 2);
    }



}