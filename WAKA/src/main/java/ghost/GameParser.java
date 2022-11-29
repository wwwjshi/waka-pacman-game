package ghost;

import processing.core.PImage;
import processing.core.PApplet;
import java.util.ArrayList;
import java.util.HashMap;

public class GameParser {
    private ConfigReader conf;
    private App app;

    // For generating components
    private ArrayList<Integer> wallXs = new ArrayList<Integer>();
    private ArrayList<Integer> wallYs = new ArrayList<Integer>();
    private ArrayList<PImage> wallSprites = new ArrayList<PImage>();

    private ArrayList<Integer> ghostXs = new ArrayList<Integer>();
    private ArrayList<Integer> ghostYs = new ArrayList<Integer>();
    private ArrayList<Character> ghostTypes = new ArrayList<Character>();
    private HashMap<Character, PImage> ghostSprites = new HashMap<Character, PImage>();
    private int frightenedLength;
    private PImage frightSprite;

    private ArrayList<Integer> fruitXs = new ArrayList<Integer>();
    private ArrayList<Integer> fruitYs = new ArrayList<Integer>();
    private PImage fruitSprite;

    private ArrayList<Integer> superFruitXs = new ArrayList<Integer>();
    private ArrayList<Integer> superFruitYs = new ArrayList<Integer>();
    private PImage superFruitSprite;

    private int playerX;
    private int playerY;
    private ArrayList<PImage> playerSprites = new ArrayList<PImage>();

    private int lives;
    private int speed;
    private ArrayList<Integer> modeLengths = new ArrayList<Integer>();

    private ArrayList<Ghost> removedGhost = new ArrayList<Ghost>();
    private HashMap<Ghost, Boolean> ghostInMap = new HashMap<Ghost, Boolean>();

    // Componets to be generate

    /**
     * Waka object for the game
     */
    protected Waka waka;
    /**
     * List of Wall objects for the game
     */
    protected ArrayList<Wall> walls = new ArrayList<Wall>();
    /**
     * List of Fruit objects for the game
     */
    protected ArrayList<Fruit> fruits = new ArrayList<Fruit>();
    /**
     * List of SuperFruit objects for the game
     */
    protected ArrayList<SuperFruit> superFruits = new ArrayList<SuperFruit>();
    /**
     * List of Ghost objects for the game
     */
    protected ArrayList<Ghost> ghosts = new ArrayList<Ghost>();

    // game status
    /**
     * Indicates on and off of debug Mode
     */
    protected boolean debugMode;

    /**
     * Denotes the overall game time
     */
    protected int timePassed;

    /**
     * Denotes the end time of ghost in frightened mode
     */
    protected int superTime;

    /**
     * Denotes current index of in modeLength list
     */
    protected int modeIndex;

    /**
     * Denotes the end of mode time for current mode
     */
    protected int modeTime;

    /**
     * Indicates on and off of debug Mode
     */
    protected boolean ghostScatterMode;

    /**
     * Indicates if ghost is in frightened Mode
     */
    protected boolean frightenedMode;

    /**
     * Indicates if game has ended
     */
    protected boolean gameEnd;

    /**
     * Indicates if player wins the game
     */
    protected boolean win;

    /**
     * Indicates if player lose the game
     */
    protected boolean lose;

    /**
     * Denotes the game finishing time
     */
    protected int finishedTime;


    /**
     * Constructor of GameParser
     * @param app, the Application that calls the it
     * @param configFile, the file name of configuration file
     */
    public GameParser(App app, String configFile){
        this.app = app;
        this.conf = new ConfigReader(configFile);
        ArrayList<String> map = conf.getMap();
        String mapAttr= "012345678paciw";
        String[] wallSpritesStr = {"horizontal", "vertical", "upLeft", "upRight", "downLeft", "downRight"};
        int currx = 0;
        int curry = 0;
        // parse locations and map sprite
        for(String row: map){
            char[] cs = row.toCharArray();
            curry += 15;
            currx = 0;
            for(char c: cs){
                currx += 15;
                int i = mapAttr.indexOf(c);
                if (i >= 0) {
                    if(i == 0) {
                        continue;
                    }
                    if(i == 9) {
                        this.playerX = currx;
                        this.playerY = curry;
                        continue;
                    }
                    if(i == 10 || i == 11 || i == 12 || i == 13) {
                        this.ghostXs.add(currx);
                        this.ghostYs.add(curry);
                        this.ghostTypes.add(mapAttr.charAt(i));
                        continue;
                    }
                    if(i == 7){
                        this.fruitXs.add(currx);
                        this.fruitYs.add(curry);
                        continue;
                    }
                    if(i == 8){
                        this.superFruitXs.add(currx);
                        this.superFruitYs.add(curry);
                        continue;
                    }
                    this.wallXs.add(currx);
                    this.wallYs.add(curry);
                    this.wallSprites.add(app.loadImage("src/main/resources/" + wallSpritesStr[i-1] + ".png"));
                }
            }
        }
        // parse sprites
        this.fruitSprite = app.loadImage("src/main/resources/fruit.png");
        this.superFruitSprite = this.fruitSprite.get();
        this.superFruitSprite.resize(fruitSprite.width * 2, fruitSprite.height * 2);
        this.playerSprites.add( app.loadImage("src/main/resources/playerClosed.png"));
        this.playerSprites.add( app.loadImage("src/main/resources/playerUp.png"));
        this.playerSprites.add( app.loadImage("src/main/resources/playerDown.png"));
        this.playerSprites.add( app.loadImage("src/main/resources/playerLeft.png"));
        this.playerSprites.add( app.loadImage("src/main/resources/playerRight.png"));
        this.ghostSprites.put('a', app.loadImage("src/main/resources/ambusher.png"));
        this.ghostSprites.put('c', app.loadImage("src/main/resources/chaser.png"));
        this.ghostSprites.put('i', app.loadImage("src/main/resources/ignorant.png"));
        this.ghostSprites.put('w', app.loadImage("src/main/resources/whim.png"));
        this.frightSprite = app.loadImage("src/main/resources/frightened.png");
        // other 
        this.lives = conf.getLives();
        this.speed = conf.getSpeed();
        this.frightenedLength = conf.getFrightenedLength();
        this.modeLengths = conf.getModeLengths();
        this.modeTime = this.modeLengths.get(0);
        // Generate componets 
        int i = 0;
        while(i < this.wallSprites.size()){
            Wall w = new Wall(this.wallXs.get(i), this.wallYs.get(i), this.wallSprites.get(i));
            this.walls.add(w);
            i += 1;
        }
        int j = 0;
        while(j < this.fruitYs.size()){
            Fruit f = new Fruit(this.fruitXs.get(j), this.fruitYs.get(j), this.fruitSprite);
            this.fruits.add(f);
            j += 1;
        }
        int l = 0;
        while(l < this.superFruitYs.size()){
            SuperFruit sf = new SuperFruit(this.superFruitXs.get(l), this.superFruitYs.get(l), this.superFruitSprite);
            this.superFruits.add(sf);
            l += 1;
        }
        int k = 0;
        while(k < this.ghostXs.size()){
            Ghost g = new Ghost(this.ghostXs.get(k), this.ghostYs.get(k), this.ghostSprites.get(this.ghostTypes.get(k)), this.ghostTypes.get(k), this);
            this.ghosts.add(g);
            this.ghostInMap.put(g, true);
            k += 1;
        }
    }

    /**
     * For handling of the overall game logic flow
     */
    public void tick(){
        if(this.superFruits.size() + this.fruits.size() == 0){
            this.win();
        }
        if(this.gameEnd){
            return;
        }
        this.setTime(app.frameCount);
        this.setGhostMode();
    }

    /**
     * For drawing game components
     */
    public void draw(){
        for(Wall w: this.walls){
            w.draw(this.app);
        }

        for(Fruit f: this.fruits){
            f.draw(this.app);
        }

        for(SuperFruit sf: this.superFruits){
            sf.draw(this.app);
        }

        for(Ghost rg: this.removedGhost){
            ghostInMap.replace(rg, false);
        }
        for(Ghost g: ghostInMap.keySet()){
            if(ghostInMap.get(g)){
                g.tick();
                g.draw(app);
            }
        }
    }

    /**
     * For getting the time game has been ran for
     */
    protected void setTime(int frameCount){
        this.timePassed = (int) frameCount/60;
    }

    /**
     * For setting the Player-controlled object.
     */
    protected void setWaka(Waka waka){
        this.waka = waka;
    }

    /**
     * @return wall objects of in list
     */
    public ArrayList<Wall> getWalls(){
        return this.walls;
    }

    /**
     * @return Fruit objects of in list
     */
    public ArrayList<Fruit> getFruits(){
        return this.fruits;
    }

    /**
     * For removing of fruits
     * @param f, the Fruit object to be removed
     */
    protected void removeFruit(Fruit f){
        this.fruits.remove(f);
    }

    /**
     * @return SuperFruit objects of in list
     */
    public ArrayList<SuperFruit> getSuperFruits(){
        return this.superFruits;
    }

    /**
     * For removing of super fruits
     * @param sf, the SuperFruit object to be removed
     */
    protected void removeSuperFruit(SuperFruit sf){
        this.superFruits.remove(sf);
        this.superTime = this.timePassed + this.frightenedLength;
        this.modeTime += (this.superTime - this.timePassed);
    }

    /**
     * For activation/ deactivation of debug mode
     */
    protected void setDebugMode(){
        this.debugMode = !this.debugMode;
    }

    /**
     * @return boolean denoting on(True) and off(False) of debug mode
     */
    protected boolean getDebugMode(){
        return this.debugMode;
    }

    private boolean determineScaMode(){
        if(this.modeIndex % 2 == 0){
            return true;
        }
        return false;
    }

    /**
     * Used to set ghost mode base on time informations
     */
    protected void setGhostMode(){
        if(this.superTime > this.timePassed){
            this.frightenedMode = true;
            return;
        }
        this.frightenedMode = false;
        if(this.timePassed < this.modeTime){
            this.ghostScatterMode = this.determineScaMode();
            return;
        }
        if(this.modeIndex + 1 < this.modeLengths.size()){
            this.modeIndex += 1;
            this.modeTime += this.modeLengths.get(this.modeIndex);
            this.ghostScatterMode = this.determineScaMode();
            return;
        }
        if(this.modeIndex + 1 >= this.modeLengths.size()){
            this.modeIndex = 0;
            this.modeTime += this.modeLengths.get(modeIndex);
            this.ghostScatterMode = this.determineScaMode();
            return;
        }
    }

    /**
     * @return player(Waka) horizontal position
     */
    public int getPlayerX(){
        return this.playerX;
    }

    /**
     * @return player(Waka) vertical position
     */
    public int getPlayerY(){
        return this.playerY;
    }

    /**
     * @return speed value
     */
    public int getSpeed(){
        return this.speed;
    }

    /**
     * @return lives value
     */
    public int getLives(){
        return this.lives;
    }

    /**
     * @return list of Waka object sprites
     */
    public ArrayList<PImage> getPlayerSprites(){
        return this.playerSprites;
    }

    /**
     * @return mode length values in list
     */
    public ArrayList<Integer> getModeLengths(){
        return this.modeLengths;
    }

    /**
     * @return frightend sprites of Ghost object
     */
    public PImage getFrightSprite(){
        return this.frightSprite;
    }

    /**
     * For removing of Ghosts
     * @param g, the Ghost object to be removed
     */
    protected void removeGhost(Ghost g){
        this.removedGhost.add(g);
    }

    /**
     * Losing of game, mehod called when player loses all lives
     */
    protected void lose(){
        this.gameEnd = true;
        this.lose = true;
        this.finishedTime = this.timePassed;
    }

    /**
     * Winning of game, method called when player eats all fruits including super fruit
     */
    protected void win(){
        this.gameEnd = true;
        this.win = true;
        this.finishedTime = this.timePassed;
    }

    /**
     * @return if player wins the game
     */
    public boolean hasWin(){
        return this.win;
    }

    /**
     * @return if player lose the game
     */
    public boolean hasLose(){
        return this.lose;
    }

    /**
     * For partial resetting of game status, method called when player(waka) has been caught by ghost.
     * Ghost mode is restarts. 
     * Ghost and waka return to intital position. 
     * Any removed ghost is restored, while fruits and super fruit is not. 
     */
    protected void reset(){
        this.removedGhost.clear();
        for(Ghost g: this.ghosts){
            g.reset();
            ghostInMap.replace(g, true);
        }
        this.waka.reset();
        this.modeIndex = 0;
        this.modeTime = (int) app.frameCount/60;
    }

    /**
     * For resetting all game status. 
     * Method called 10 second after previous game ends and restarts the game.
     */
    protected void resetAll(){
        this.waka.setLife(this.lives);
        this.reset();
        this.win = false;
        this.lose = false;
        this.gameEnd = false;
        this.frightenedMode = false;
        int j = 0;
        while(j < this.fruitYs.size()){
            Fruit f = new Fruit(this.fruitXs.get(j), this.fruitYs.get(j), this.fruitSprite);
            this.fruits.add(f);
            j += 1;
        }
        int l = 0;
        while(l < this.superFruitYs.size()){
            SuperFruit sf = new SuperFruit(this.superFruitXs.get(l), this.superFruitYs.get(l), this.superFruitSprite);
            this.superFruits.add(sf);
            l += 1;
        }
    }

    /**
     * @return, the time taken to win or lose the game.
     */
    public int getFinishedTime(){
        return this.finishedTime;
    }
}