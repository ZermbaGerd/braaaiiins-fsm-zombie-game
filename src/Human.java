import edu.macalester.graphics.Image;
import edu.macalester.graphics.Point;

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
     * Updates the player location to the left
     * @param dt
     */
    public void moveLeft(double dt){
        setLocation(new Point(getLocation().getX() - this.speed * dt, getLocation().getY()));
        updateSprite();
    }

    /**
     * updates player location to the right
     * @param dt
     */
    public void moveRight(double dt){
        setLocation(new Point(getLocation().getX() + this.speed * dt, getLocation().getY()));
        updateSprite();

    }

    /**
     * Updates player location up
     * @param dt
     */
    public void moveUp(double dt){
        setLocation(new Point(getLocation().getX(), getLocation().getY() - this.speed * dt));
        updateSprite();
    }

    /**
     * Updates player location downward
     * @param dt
     */
    public void moveDown(double dt){
        setLocation(new Point(getLocation().getX(), getLocation().getY() + this.speed * dt));
        updateSprite();

    }

}