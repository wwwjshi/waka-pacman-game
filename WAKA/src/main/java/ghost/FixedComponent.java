package ghost;

import processing.core.PImage;
import processing.core.PApplet;
import java.util.ArrayList;

public class FixedComponent {
    /**
     * The horizontal position of the object 
     */
    protected int x;

    /**
     * The vertical position of the object 
     */
    protected int y;

    /**
     * The sprite of the object 
     */
    protected PImage sprite;

    /**
     * Constructor for fixed game component
     * @param x, the horizontal position of the object 
     * @param y, the vertical position of the object 
     * @param sprite, the sprite of the object 
     */
    public FixedComponent(int x, int y, PImage sprite) {
        this.sprite = sprite;
        this.x = x;
        this.y = y;
    }

    /**
     * For drawing of the object
     */
    public void draw(App app){
        app.image(this.sprite, this.x, this.y);
    }

    /**
     * @return horizontal position of this object - left side
     */
    public int getLeft(){
        return this.x;
    }

    /**
     * @return horizontal position of this object - right side
     */
    public int getRight(){
        return this.x + this.sprite.width;
    }

    /**
     * @return vertical position of this object - top side
     */
    public int getTop(){
        return this.y;
    }

    /**
     * @return vertical position of this object - bottom side
     */
    public int getBot(){
        return this.y + this.sprite.height;
    }
}