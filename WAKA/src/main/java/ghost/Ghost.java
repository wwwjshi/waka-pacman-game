package ghost;

import processing.core.PImage;
import processing.core.PApplet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.HashMap;
import java.util.Collections;
import processing.event.KeyEvent;


public class Ghost {

    private int x;
    private int y;
    private GameParser parser;
    private PImage sprite;
    private PImage frightSprite;
    private PImage displaySprite;
    private char type;
    private int speed;
    private int aimX;
    private int aimY;
    private int changeX;
    private int changeY;
    private Move preMove = Move.Stop;
    private int initalX;
    private int initalY;

    /**
     * Constructor of Ghost
     * @param x, the horizontal position of the ghost 
     * @param y, the vertical position of the ghost 
     * @param sprite, the normal sprite of the ghost 
     * @param type, the type of the ghost 
     * @param parser, the GameParser that called this constructor
     */
    public Ghost(int x, int y, PImage sprite, char type, GameParser parser) {
        this.sprite = sprite;
        this.initalX = x;
        this.initalY = y;
        this.x = x;
        this.y = y;
        this.type = type;
        this.parser = parser;
        this.speed = parser.getSpeed();
        this.frightSprite = parser.getFrightSprite();
    }

    /**
     * For handling of ghost logic flow
     * Determine the displaying sprite for ghost and move the ghost base on type it is and mode it is in. 
     * Update game status and pass the relevant information to parser(GameParser)
     */
    public void tick(){
        if(this.parser.frightenedMode){
            this.aimRandom();
            this.displaySprite = this.frightSprite;
        } else if(this.parser.ghostScatterMode){
            this.displaySprite = this.sprite;
            if(this.type == 'a'){
                this.aimAmbusher();
            } else if(this.type == 'c'){
                this.aimChaser();
            } else if(this.type == 'i'){
                this.aimIgnorant();
            } else if(this.type == 'w'){
                this.aimWhim();
            }
        } else{
            this.displaySprite = this.sprite;
            this.aimCorner();
        }
        this.nextMove();
        if(this.caughtWaka()){
            if(this.parser.frightenedMode){
                this.parser.removeGhost(this);
            } else {
                this.parser.reset();
                this.parser.waka.reduceLife();
            }
        }   
    }

    /**
     * For drawing ghost
     * @param app, the Application for drawing
     */
    public void draw(App app){
        app.image(this.displaySprite, this.x-5, this.y-5);
        if(parser.getDebugMode()){
            app.stroke(255);
            app.line(this.x, this.y, this.aimX, this.aimY);
        }
    }

    private boolean isWall(int spX, int spY){
        // consider ghost sprite size same as wall
        int xLeft = this.x + 1 + spX;
        int xRight = this.x + 15 + spX;
        int xTop = this.y + 1 + spY;
        int xBot = this.y + 15 + spY;
        // check out of bound
        if(xLeft < 16 || xRight > 432 || 48 > xTop || 528 < xBot){
            return true;
        }
        // check wall
        for(Wall w: this.parser.getWalls()){
            int wLeft = w.getLeft();
            int wRight = w.getRight();
            int wTop = w.getTop();
            int wBot = w.getBot();
            if(xLeft < wRight && xRight > wLeft && wBot > xTop && wTop < xBot){
                return true;
            }
        }
        return false;
    }

    private void aimCorner(){
        int[][] corners = {{0, 0}, {448, 0}, {0, 576}, {448, 576}};
        Character[] ts = {'c', 'a', 'i', 'w'};
        List<Character> type = Arrays.asList(ts);
        int aimCorner = type.indexOf(this.type);
        this.aimX = corners[aimCorner][0];
        this.aimY = corners[aimCorner][1];
    }

    private void aimChaser(){
        this.aimX = this.parser.waka.getX();
        this.aimY = this.parser.waka.getY();
    }

    private void aimAmbusher(){
        int valChange = 4*16;
        if(this.type == 'w'){
            valChange = 2*16;
        } else{
            this.aimChaser();
        }
        Move playerDir = this.parser.waka.getCurrDir();
        if (playerDir == Move.L){
            this.aimX -= valChange; 
        } else if (playerDir == Move.R){
            this.aimX += valChange; 
        } else if (playerDir == Move.U){
            this.aimY -= valChange; 
        } else if (playerDir == Move.D){
            this.aimY += valChange; 
        }
        if(this.aimX < 0){
            this.aimX = 0;
        } else if(this.aimX > 448){
            this.aimX = 448;
        }
        if(this.aimY < 0){
            this.aimY = 0;
        } else if(this.aimY > 576){
            this.aimY = 576;
        }
    }

    private void aimIgnorant(){
        int playerX = this.parser.waka.getX();
        int playerY = this.parser.waka.getY();
        double distDiff = Math.hypot(playerX - this.x, playerY - this.y);
        double maxDiff = 8*16;
        if(distDiff > maxDiff){
            this.aimChaser();
            return;
        }
        this.aimCorner();
    }

    private void aimWhim(){
        for(Ghost g: this.parser.ghosts){
            if(g.getType() == 'c'){
                this.aimX = g.getAimX();
                this.aimY = g.getAimY();
                this.aimAmbusher();

                int playerX = this.parser.waka.getX();
                int playerY = this.parser.waka.getY();
                int diffX = (playerX - g.getX());
                int diifY = (playerY - g.getY());
                this.aimX += diffX;
                this.aimY += diifY;

                if(this.aimX < 0){
                    this.aimX = 0;
                } else if(this.aimX > 448){
                    this.aimX = 448;
                }
        
                if(this.aimY < 0){
                    this.aimY = 0;
                } else if(this.aimY > 576){
                    this.aimY = 576;
                }
                return;
            }
        }
        this.aimCorner();
    }

    private void aimRandom(){
        this.aimX = (int) (Math.random() * 448) + 1;
        this.aimY = (int) (Math.random() * 576) + 1;
    }

    private void nextMove(){
        double mL = Math.hypot((this.aimX - (this.x - this.speed)), (this.aimY-this.y));
        double mR = Math.hypot((this.aimX - (this.x + this.speed)), (this.aimY-this.y));
        double mU = Math.hypot((this.aimX -this.x), (this.aimY - (this.y - this.speed)));
        double mD = Math.hypot((this.aimX -this.x), (this.aimY - (this.y + this.speed)));
        double[] distAfterMove = {mL, mR, mU, mD};
        Move[] moves = {Move.L, Move.R, Move.U, Move.D};
        int[][] speeds = {{-this.speed, 0}, {this.speed, 0}, {0, -this.speed}, {0, this.speed}};

        // sort 
        double temp_dist;
        Move temp_move;
        int[] temp_speed;
        int i = 0;
        int j;
        while (i < 4){
            j = i + 1;
            while(j < 4){
                if (distAfterMove[i] > distAfterMove[j]){
                    temp_dist = distAfterMove[i];
                    temp_move = moves[i];
                    temp_speed = speeds[i];
                    distAfterMove[i] = distAfterMove[j];
                    moves[i] = moves[j];
                    speeds[i] = speeds[j];
                    distAfterMove[j] = temp_dist;
                    moves[j] = temp_move;
                    speeds[j] = temp_speed;
                }  
                j += 1;
            }
            i += 1;
        }
        // check move is possibe
        i = 0;
        while(i < 4){
            if(isWall(speeds[i][0], speeds[i][1]) == false && moves[i] != preMove.opposite() ){
                this.changeX = speeds[i][0];
                this.changeY = speeds[i][1];
                this.preMove = moves[i];
                break;
            }
            i += 1;
        }
        this.x += this.changeX;
        this.y += this.changeY;
    }

    /**
     * @return, char that denote the type of this ghost
     */
    public char getType(){
        return this.type;
    }

    /**
     * @return horizontal position of this ghost
     */
    public int getX(){
        return this.x;
    }

    /**
     * @return vertical position of this ghost
     */
    public int getY(){
        return this.y;
    }

    /**
     * @return the horizontal targeting position
     */
    public int getAimX(){
        return this.aimX;
    }

    /**
     * @return the vertical targeting position
     */
    public int getAimY(){
        return this.aimY;
    }

    private boolean caughtWaka(){
        int xLeft = this.x;
        int xRight = this.x + this.sprite.width;
        int xTop = this.y;
        int xBot = this.y + this.sprite.height;

        Waka w = parser.waka;
        int wLeft = w.getX();
        int wRight = w.getX() + w.getWidth();
        int wTop = w.getY();
        int wBot = w.getY() + w.getHeight();

        if(xLeft < wRight && xRight > wLeft && wBot > xTop && wTop < xBot){
            return true;
        }
        return false;
    }

    /**
     * Reset the position of this ghost to the intial position
     */
    protected void reset(){
        this.x = this.initalX;
        this.y = this.initalY;
    }
}