import java.util.Set;

import edu.macalester.graphics.Image;
import edu.macalester.graphics.Point;
import edu.macalester.graphics.events.Key;

public class Human extends Entity{
    public static final double START_HEALTH = 100;
    public static final double DEFAULT_HUMAN_SPEED = Zombie.DEFAULT_ZOMBIE_SPEED * 1.7;
    // TODO find right movement speed ratio for good balance
    private double health = START_HEALTH;

    public Human() {
        super();
        initialize();
    }

    /**
     * Contructs human at given point
     * @param location
     */
    public Human(Point location) {
        super(location);
        initialize();
    }

    /**
     * Sets the inital speed and visual depiction of the human
     */
    private void initialize() {
        this.speed = DEFAULT_HUMAN_SPEED; 

        this.sprite = new Image("mac-the-scot.png");
        ((Image) this.sprite).setMaxHeight(size*2.5);
        ((Image) this.sprite).setMaxWidth(size*2.5);
    }

    /**
     * Checks if value is greater than zero, and if so updates player's health variable
     * @param health
     */
    public void setHealth(double health) {
        if(health >= 0) this.health = health;
    }

    /**
     * 
     * @return the player's health stats
     */
    public double getHealth() {
        return this.health;
    }

    /**
     * 
     * @param damage
     */
    public void takeDamage(double damage) {
        if (this.health > 0) this.health -= damage;
        if (this.health < 0) this.health = 0;
    }


    /**
     * Move the human and rotate its sprite based on what keys are pressed.
     * @param dt
     * @param keysPressed
     */
    public void move(double dt, Set<Key> keysPressed) {
        /* used as "point to rotate to". idea is that you just add this as an offset to the center 
           and it should rotate in the 45 degree segments we need it to for the movement logic to work */
        Point angle = new Point(0,0);

        // same idea here, except you multiply by speed before applying the offset
        Point movementOffset = new Point(0,0);

        if (keysPressed.contains(Key.LEFT_ARROW) || keysPressed.contains(Key.A)) {
            movementOffset = movementOffset.add(new Point (-1, 0));
            angle = angle.add(new Point(-1,0));
        }
        if (keysPressed.contains(Key.RIGHT_ARROW) || keysPressed.contains(Key.D)) {
            movementOffset = movementOffset.add(new Point (1, 0));
            angle = angle.add(new Point(1,0));
        }
        if (keysPressed.contains(Key.UP_ARROW) || keysPressed.contains(Key.W)) {
            movementOffset = movementOffset.add(new Point (0, -1));
            angle = angle.add(new Point(0,-1));
        }
        if (keysPressed.contains(Key.DOWN_ARROW) || keysPressed.contains(Key.S)) {
            movementOffset = movementOffset.add(new Point (0, 1));
            angle = angle.add(new Point(0,1));
        }

        if(!movementOffset.equals(new Point(0,0))) {
            // add the movement offset to our location - multiply by dt to avoid fps scaling on speed
            // divide by magnitude to avoid diagonal movement being faster than horizontal/vertical because of adding to both axes
            this.setLocation(this.getLocation().add(movementOffset.scale((speed*dt) / movementOffset.magnitude())));
        }
        if(!angle.equals(new Point(0,0))) {
            rotateSpriteToPoint(this.getLocation().add(angle));
        } 
        updateSprite();
    }

}