package ghost;

import processing.core.PImage;
import processing.core.PApplet;
import java.util.ArrayList;

public class Wall extends FixedComponent {
    /**
     * Constructor of Wall, inherits FixedComponet
     * @param x, the horizontal position of the wall
     * @param y, the vertical position of the wall
     * @param sprite, the sprite of the super wall
     * @see FixedComponent
     */
    public Wall(int x, int y, PImage sprite) {
        super(x, y, sprite);
    }
}