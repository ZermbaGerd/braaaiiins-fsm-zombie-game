import edu.macalester.graphics.Ellipse;
import edu.macalester.graphics.GraphicsObject;
import edu.macalester.graphics.Point;

/**
 * Parent class for two types of entities Human and Zombies
 */
public class Entity {
    //TODO refactor these to be private
    protected Point location; // represents the center of the sprite
    protected int size; // represents the radius of the bounding circle of the sprite
    protected GraphicsObject sprite;    // the image that represents this thing
    protected double speed;
    public static final double DEFAULT_ENTITY_SPEED = 50;
    public static final int DEFAULT_ENTITY_SIZE = 15;



    /**
     * Creates entity and sets inital sprite to ellipse
     */
    public Entity() {
        this.location = new Point(0,0);
        this.size = DEFAULT_ENTITY_SIZE;
        this.speed = DEFAULT_ENTITY_SPEED;
        this.sprite = new Ellipse(this.location.getX() + this.size, this.location.getY() + this.size, size*2, size*2);
        ((Ellipse) this.sprite).setFilled(true);
        this.sprite.setCenter(this.location);
    }

    /**
     * Creates entity represented as ellipse at given location point
     * @param location 
     */
    public Entity(Point location) {
        this.location = location;
        this.size = DEFAULT_ENTITY_SIZE;
        this.speed = DEFAULT_ENTITY_SPEED;
        this.sprite = new Ellipse(this.location.getX() + this.size, this.location.getY() + this.size, size*2, size*2);
        ((Ellipse) this.sprite).setFilled(true);
        this.sprite.setCenter(this.location);

    }

    /**
     * Contructs entity represented as ellipse at given location point
     * @param location
     * @param size
     * @param speed
     * @param sprite
     */
    public Entity(Point location, int size, double speed, GraphicsObject sprite) {
        this.location = location;
        this.size = size;
        this.speed = speed;
        this.sprite = sprite;
        this.sprite.setCenter(this.location);
    }

    /**
     * Returns the center point of this entity
     * @return Point location
     */
    public Point getLocation() {
        return this.location;
    }

    /**
     * Moves entity to given point checking if the new location is offscreen, if it is off on either axis, just use the max value
     * @param location
     */
    public void setLocation(Point location) {
        // Might have to change to current location instead
        double x, y = 0;
        x = location.getX();
        y = location.getY();

        if(x < 0) x = 0;
        if(x > View.CANVAS_WIDTH) x = View.CANVAS_WIDTH;

        if(y < 0) y = 0;
        if(y > View.CANVAS_HEIGHT) y = View.CANVAS_HEIGHT;

        this.location = new Point(x, y);        // TODO see if we can refactor to use testPointOnScreen
    }

    /**
     * Returns true if a given point is within the screen's bounds, and false if it is not
     * @param point
     * @return
     */
    public boolean testPointOnScreen(Point point) {
        if(point.getX() < 0) return false;
        if(point.getY() < 0) return false;
        if(point.getX() > View.CANVAS_WIDTH) return false;
        if(point.getY() > View.CANVAS_HEIGHT) return false;
        return true;
    }

    /**
     * 
     * @return size of the entity represented as int
     */
    public int getSize() {
        return this.size;
    }

    /**
     * resets the size to given int
     * @param size
     */
    public void setSize(int size) {
        // TODO sanitize the suggested new size here, and maybe adjust sprite stuff
        this.size = size;
    }


    /**
     * Updates the Graphics Object representation of the entity
     * @param sprite
     */
    public void setSprite(GraphicsObject sprite) {
        // TODO sanitize this for sizing, basically just reality-checking that it doesn't break scaling
        this.sprite = sprite;
    }

    /**
     * 
     * @return the graphical depiction of the entity
     */
    public GraphicsObject getSprite() {
        return this.sprite;
    }

    /**
     * Updates the entity's movement
     * @param speed
     */
    public void setSpeed(double speed) {
        this.speed = speed;
    }

    /**
     * 
     * @return the speed of the entity
     */
    public double getSpeed() {
        return this.speed;
    }
    
    /**
     * updates the graphical depiction of the entity
     */
    public void updateSprite(){
        this.sprite.setCenter(this.location);
    }

    /**
     * Rotate the sprite of this entity so that it is pointing towards the given point. The left side of the sprite will be considered
     * the front.
     * @param goal
     */
    public void rotateSpriteToPoint(Point goal) {   // THIS WORKS YIPPEEEEEE
        Point diff = this.location.subtract(goal);
        this.sprite.setRotation(Math.toDegrees(Math.atan2(diff.getY(), diff.getX())));
    }
}
