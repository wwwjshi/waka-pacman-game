package ghost;

import processing.core.PApplet;
import processing.core.PImage;
import processing.event.KeyEvent;
import processing.core.PFont;

public class App extends PApplet {
    /**
     * Specifies game window width
     */
    public static final int WIDTH = 448;

    /**
     * Specifies game window height
     */
    public static final int HEIGHT = 576;

    /**
     * Uses to parse game data
     */
    private GameParser parser;

    /**
     * The character Player controls
     */
    private Waka waka;

    /**
     * Text font used for the application
     */
    public PFont font;

    public App() {
    }

    /**
     * For game setup, used to define inital environment properities and load medias
     */
    public void setup() {
        frameRate(60);
        this.font = this.createFont("src/main/resources/PressStart2P-Regular.ttf", 16);
        this.parser = new GameParser(this, "./config.json"); 
        this.waka = new Waka(this.parser);
    }

    /**
     * Define the game settings, only used for defining application window size for this game application
     */
    public void settings() {
        size(WIDTH, HEIGHT);
    }

    /**
     * For drawing of all game components
     */
    public void draw() { 
        background(0, 0, 0);
        textFont(font, 16);
        if(this.parser.hasWin() || this.parser.hasLose()){
            textFont(font, 30);
            textAlign(CENTER);
            if(this.parser.hasWin()){
                text("YOU WIN", WIDTH/2, HEIGHT/3);
            } else if(this.parser.hasLose()){
                text("GAME OVER", WIDTH/2, HEIGHT/3);
            }
            int time = (int) this.frameCount/60;
            if(time > (this.parser.getFinishedTime() + 10)){
                this.parser.resetAll();
            }
            return;
        }
        this.waka.tick();
        this.parser.tick();
        this.parser.draw();
        this.waka.drawLives(this);
        this.waka.draw(this, this.frameCount%16);
    }

    /**
     * Detect for key pressed
     * user can to press up, down, left, right arrow on keyboard to control the movement of waka
     * also allow for activate/deactivate of debug mode when pressed space bar 
     * @param e, the key that has been pressed
     */
    public void keyPressed(KeyEvent e){
        if (e.getKeyCode() == 39) {
            this.waka.moveRight();
        } else if (e.getKeyCode() == 37) {
            this.waka.moveLeft();
        } else if (e.getKeyCode() == 38) {
            this.waka.moveUp();
        } else if (e.getKeyCode() == 40) {
            this.waka.moveDown();
        }
        if(e.getKeyCode() == 32){
            this.parser.setDebugMode();
        }
    }

    /**
     * To run the game application
     */
    public static void main(String[] args) {
        PApplet.main("ghost.App");
    }
}
