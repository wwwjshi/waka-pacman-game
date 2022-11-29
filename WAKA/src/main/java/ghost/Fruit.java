package ghost;

import processing.core.PImage;
import processing.core.PApplet;
import java.util.ArrayList;

public class Fruit extends FixedComponent {
    /**
     * Constructor of Fruit, inherits FixedComponet
     * @param x, the horizontal position of the fruit 
     * @param y, the vertical position of the fruit 
     * @param sprite, the sprite of the fruit 
     * @see FixedComponent
     */
    public Fruit(int x, int y, PImage sprite) {
        super(x, y, sprite);
    }
}