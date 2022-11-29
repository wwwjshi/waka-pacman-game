package ghost;

import processing.core.PImage;
import processing.core.PApplet;
import java.util.ArrayList;

public class SuperFruit extends Fruit{
    /**
     * Constructor of Super Fruit, inherits Fruit, FixedCompoent
     * @param x, the horizontal position of the super fruit 
     * @param y, the vertical position of the super fruit 
     * @param sprite, the sprite of the super fruit 
     * @see FixedComponent
     * @see Fruit
     */
    public SuperFruit(int x, int y, PImage sprite){
        super(x, y, sprite);
        this.x -= 8;
        this.y -= 8;
    }
}