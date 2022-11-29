package ghost;

import processing.core.PImage;
import processing.core.PApplet;
import java.util.ArrayList;


public class Waka {
    private GameParser parser;

    /**
     * The horizontal position of waka 
     */
    protected int x;

    /**
     * The vertical position of waka
     */
    protected int y;

    private ArrayList<PImage> sprites = new ArrayList<PImage>();
    private int speed;
    private int speedX;
    private int speedY;

    /**
     * The number of lives waka have
     */
    protected int lives;

    private PImage currentSprite;
    private PImage newSprite;
    private PImage previousSprite;
    private int preSpeedX;
    private int preSpeedY;
    private int changeX;
    private int changeY;

    /**
     * The current movement direction of waka
     */
    protected Move currMove;

    /**
     * The next movement direction of waka
     */
    protected Move nextMove;

    /**
     * The previous movement direction of waka
     */
    protected Move preMove;

    /**
     * The intial horizontal position of waka
     */
    protected int initalX;

    /**
     * The intial vertical position of waka
     */
    protected int initalY;

    /**
     * Constructor of Waka
     * @param parser, the GameParser that is used to for this game
     */
    public Waka(GameParser parser){
        this.parser = parser;
        this.x = parser.getPlayerX()-4;
        this.y = parser.getPlayerY()-4;
        this.initalX = x;
        this.initalY = y;
        this.sprites = parser.getPlayerSprites();
        this.speed = parser.getSpeed();
        this.lives = parser.getLives();
        this.newSprite = this.sprites.get(4);
        this.previousSprite = this.sprites.get(4); 
        this.currentSprite = this.previousSprite;
        parser.setWaka(this);
    }

    /**
     * For handling of waka logic flow
     * Determine the displaying sprite for waka. 
     * Perform possible actions of movements, fruit eating.
     * Update game status and pass the relevant information to parser(GameParser)
     */
    public void tick(){
        ArrayList<Wall> walls = this.parser.getWalls();
        if(isWall(this.speedX, this.speedY) && ((this.nextMove == this.currMove)||(isWall(this.preSpeedX, this.preSpeedY) ))){
            changeX = 0;
            changeY = 0;
            if(! isWall(this.preSpeedX, this.preSpeedY)){
                this.currentSprite = this.newSprite;
            }
            this.currMove = this.nextMove;
        } else if (isWall(this.speedX, this.speedY)){
            changeX = this.preSpeedX;
            changeY = this.preSpeedY;
            this.currMove = this.preMove;
            this.currentSprite = this.previousSprite;
        } else{
            changeX = this.speedX;
            changeY = this.speedY;
            this.currentSprite = this.newSprite;
            this.currMove = this.nextMove;
        }
        this.x +=changeX;
        this.y +=changeY;
        eatfruit();
        eatSuperfruit();
    }

    /**
     * For drawing waka
     * @param app, the Application for drawing
     * @param count, the applicaiton frames counted
     */
    public void draw(App app, int count){
        if (count < 8){
            app.image(this.sprites.get(0), this.x, this.y);
            return;
        }
        app.image(this.currentSprite, this.x, this.y);
    }

    /**
     * For drawing lives remaining of waka
     * @param app, the Application for drawing
     */
    public void drawLives(App app){
        int i = 1;
        while(i <= this.lives){
            app.image(this.sprites.get(4), i*30, 540);
            i += 1;
        }
    }

    /**
     * Checks if next movement would collide with a wall object
     * @param spX, the horizontal moving speed of waka
     * @param spY, the vertical moving speed of waka
     */
    protected boolean isWall(int spX, int spY){
        //consider waka sprite size same as wall
        int xLeft = this.x + 5 + spX;
        int xRight = this.x + 19 +spX ;
        int xTop = this.y + 5 + spY ;
        int xBot = this.y + 19 + spY ;
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

    private void eatfruit(){
        int xLeft = this.x + 5;
        int xRight = this.x + 19;
        int xTop = this.y + 5;
        int xBot = this.y + 19;
        ArrayList<Fruit> toRemove = new ArrayList<Fruit>();
        for(Fruit f: this.parser.getFruits()){
            int fLeft = f.getLeft();
            int fRight = f.getRight();
            int fTop = f.getTop();
            int fBot = f.getBot();
            if(xLeft < fRight && xRight > fLeft && fBot > xTop && fTop < xBot){
                toRemove.add(f);
            }
        }
        for(Fruit rf: toRemove){
            this.parser.removeFruit(rf);
        }
    }

    private void eatSuperfruit(){
        int xLeft = this.x + 5;
        int xRight = this.x + 19;
        int xTop = this.y + 5;
        int xBot = this.y + 19;
        ArrayList<SuperFruit> toRemove = new ArrayList<SuperFruit>();
        for(SuperFruit sf: this.parser.getSuperFruits()){
            int sfLeft = sf.getLeft() + 8;
            int sfRight = sf.getRight() - 8;
            int sfTop = sf.getTop() + 8;
            int sfBot = sf.getBot() -8;
            if(xLeft < sfRight && xRight > sfLeft && sfBot > xTop && sfTop < xBot){
                toRemove.add(sf);
            }
        }
        for(SuperFruit rsf: toRemove){
            this.parser.removeSuperFruit(rsf);
        }
    }

    private void resetNext(){
        this.previousSprite = this.currentSprite;
        this.preMove = this.currMove;
        this.preSpeedX = this.changeX;
        this.preSpeedY = this.changeY;
    }

    /**
     * Instructs waka to move right
     */
    public void moveRight(){
        this.resetNext();
        this.speedX = this.speed;
        this.speedY = 0;    
        this.newSprite = this.sprites.get(4);
        this.nextMove = Move.R;
    }

    /**
     * Instructs waka to move left
     */
    public void moveLeft(){
        this.resetNext();
        this.speedX = -(this.speed);
        this.speedY = 0;
        this.newSprite = this.sprites.get(3);
        this.nextMove = Move.L;
    }

    /**
     * Instructs waka to move up
     */
    public void moveUp(){
        this.resetNext();
        this.speedX = 0;
        this.speedY = -(this.speed);
        this.newSprite = this.sprites.get(1);
        this.nextMove = Move.U;
    }

    /**
     * Instructs waka to move down
     */
    public void moveDown(){
        this.resetNext();
        this.speedX = 0;
        this.speedY = this.speed;
        this.newSprite = this.sprites.get(2);
        this.nextMove = Move.D;
    }

    /**
     * @return, the current movement direction of waka
     */
    public Move getCurrDir(){
        return this.currMove;
    }

    /**
     * @return horizontal position of waka
     */
    public int getX(){
        return this.x;
    }

    /**
     * @return vertical position of waka
     */
    public int getY(){
        return this.y;
    }

    /**
     * @return the width of waka
     */
    public int getWidth(){
        return this.currentSprite.width;
    }

    /**
     * @return height waka
     */
    public int getHeight(){
        return this.currentSprite.height;
    }

    /**
     * Reset the position of waka to the intial position
     */
    protected void reset(){
        this.x = this.initalX;
        this.y = this.initalY;
    }

    /**
     * Reduce the life of waka by 1
     * calls the lose() method of GameParser if life equals 0
     */
    protected void reduceLife(){
        this.lives -= 1;
        if (this.lives == 0){
            this.parser.lose();
        }
    }

    /**
     * Set the lives value of waka
     */
    protected void setLife(int lives){
        this.lives = lives;
    }

}