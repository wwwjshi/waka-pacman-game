package ghost;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import processing.core.PApplet;

public class IntegrationTest {

    @Test
    public void integrationMoving(){
        App app = new App();
        app.main("ghost.App");
        PApplet.runSketch(new String[]{""}, app);
        app.setup();
        GameParser parser = new GameParser(app, "./config.json");
        Waka waka = new Waka(parser);
        int numOfFruits = parser.fruits.size();

        // invalid move since there is wall on TOP & BELOW
        waka.moveUp();
        assertTrue(Move.U == waka.nextMove);
        waka.tick();
        parser.tick();
        assertTrue(waka.x == waka.initalX);
        assertTrue(waka.y == waka.initalY);
        assertTrue(numOfFruits == parser.fruits.size()); // no fruit been ate since no move made

        waka.moveDown();
        waka.tick();
        parser.tick();
        assertTrue(waka.x == waka.initalX);
        assertTrue(waka.y == waka.initalY);

        // valid move & eat fruit
        waka.moveRight();
        waka.tick();
        parser.tick();
        assertTrue(waka.x == waka.initalX+1);
        assertTrue(waka.y == waka.initalY);
        numOfFruits -= 1;
        assertTrue(numOfFruits == parser.fruits.size()); // fruit been ate
        waka.tick();
        parser.tick();
        assertTrue(numOfFruits == parser.fruits.size()); // another right move does not ate fruit as does not collide yet

        // continue previous move when given invalide next move
        waka.moveUp();
        waka.tick();
        parser.tick();
        assertTrue(waka.currMove == waka.preMove);
        assertTrue(waka.x != waka.initalX);
        assertTrue(waka.y == waka.initalY);


        // trapped at intersection
        waka.x = 147;
        int locX = waka.x - 1;
        waka.moveLeft();
        waka.tick();
        assertTrue(locX == waka.x);
        waka.moveLeft(); // continue to hit wall
        waka.tick();
        assertTrue(146 == waka.x); // no move should be made

    }

    @Test
    public void integrationMode(){
        App app = new App();
        app.main("ghost.App");
        PApplet.runSketch(new String[]{""}, app);
        app.setup();
        GameParser parser = new GameParser(app, "./config.json");
        Waka waka = new Waka(parser);
        int numOfSuperFruits = parser.superFruits.size();
        int size = parser.getModeLengths().size();

        // initial ghost mode
        parser.setGhostMode();
        assertTrue(parser.ghostScatterMode); // scatter mode initially

        // passed initial mode
        parser.timePassed = parser.modeTime + 1;
        parser.setGhostMode();
        assertFalse(parser.ghostScatterMode);

        // restart mode from beginning when reached end
        parser.modeIndex = size; 
        parser.timePassed = parser.modeTime + 1;
        parser.setGhostMode();
        assertTrue(parser.ghostScatterMode);
        assertTrue(parser.modeIndex == 0);// && parser.modeTime == parser.modeLengths.get(0));
        //assertTrue(parser.modeTime == parser.modeLengths.get(modeIndex);)

        // eat superfruit
        waka.x = 250; // move waka to superfruit location
        numOfSuperFruits -= 1;
        waka.tick();
        parser.tick();
        assertTrue(numOfSuperFruits == parser.superFruits.size()); // superfruit been ate
        assertTrue(parser.frightenedMode); // frightenedMode began

    }

}