import java.util.Random;
import java.util.function.DoubleConsumer;
import java.time.Instant;

import edu.macalester.graphics.Image;
import edu.macalester.graphics.Point;

/**
 * Represents a zombie.
 */
public class Zombie extends Entity {
    private FSM brain = new FSM();
    private Point desiredLocation;  // movement involves going to this point or changing it, depending on behavior
                                    // wander will set it to be a random point near it
                                    // chase will continually set it to player's location as long as they're nearby
    private int perceptionRange = 100;
    private double damage = 0.5;
    public static final int WANDER_RANGE = 50;
    public static final int CLIP_RANGE = 2; // distance from destination point that the zombie will teleport to destination
    public static final double DEFAULT_ZOMBIE_SPEED = 50;
    private Human target;   // the human that the zombie is targeting
    // if we switch to raytracing pathfinding we won't need this anymore
    
    public Zombie(Human target) {
        super();
        this.target = target;

        initialize();
    }

    public Zombie(Point location, Human target) {
        super(location);
        this.target = target;

        initialize();
    }

    public void update(double dt) {
        brain.update(dt);
    }
    
    /**
     * Defines the zombie's FSM states and sets it to a default "wander" state
     */
    private void initializeStates() {
        
        brain.defineState("wander", new Wander());
        brain.defineState("chase", new Chase());
        brain.defineState("fight", new Fight());
        brain.defineState("alert", new Alert());
        brain.setState("wander");
    }

    /**
     * Initializes FSM, sprite, and static parameters.
     */
    private void initialize() {
        this.desiredLocation = this.location;
        this.speed = DEFAULT_ZOMBIE_SPEED;

        this.sprite = new Image("testTopDownZombie.png");    // left of pic will be the front of zombie
        ((Image) this.sprite).setMaxHeight(size*2);
        ((Image) this.sprite).setMaxWidth(size*2);

        initializeStates();
    }

    /**
     * Returns true if this zombie's target human is within its PERCEPTION_RANGE
     * @return
     */
    public boolean targetInRange() {    //TODO figure out how well this actually finds range - TestGUI shows a little ugly
        return target.getLocation().distance(this.location) <= this.perceptionRange;
    }

    public Human getTarget() {
        return this.target;
    }

    public void setTarget(Human target) {
        this.target = target;
    }

    public int getPerceptionRange() {
        return this.perceptionRange;
    }

    public void setPerceptionRange(int perceptionRange) {
        this.perceptionRange = perceptionRange;
    }

    /**
     * Moves towards the destination point by this.speed, and if it is closer to the destination point than CLIP_RANGE, teleports it to the 
     * destination point. Also rotates the sprite to point towards the destination point
     */
    private void moveToDesiredLocation(double dt) {
        double xDistance = this.location.getX() - this.desiredLocation.getX();
        double yDistance = this.location.getY() - this.desiredLocation.getY();

        rotateSpriteToPoint(desiredLocation);   // maybe this needs to go in updateSprite?? view not model :/

        if(Math.abs(xDistance) < CLIP_RANGE && Math.abs(yDistance) < CLIP_RANGE) {
            // if it's close to desired point, put it at its desired point
            this.setLocation(this.desiredLocation);
            return;
        }
        else { 
            // get the difference between here and desired, then normalize to a 0-1 scale and multiply by speed
            // I think we don't have to check for length being 0 because we only call this if we are not already at the location

            Point direction = new Point(desiredLocation.getX() - location.getX(), desiredLocation.getY() - location.getY());
            double length = Math.sqrt((direction.getX() * direction.getX()) + (direction.getY() * direction.getY()));
            Point normalizedDirection = new Point(direction.getX() / length, direction.getY() / length);

            location = location.add(new Point(normalizedDirection.getX() * speed * dt, normalizedDirection.getY() * speed * dt));
        }
    }

    /**
     * Returns true if this has reached its destination point
     * @return
     */
    private boolean atDesiredLocation() {
        return this.location.equals(this.desiredLocation);
    }

    /**
     * Wander makes the zombie randomly pick a point within its WANDER_RANGE to be its desired point. If it's not at that point, it moves
     * towards that point at Zombie.SPEED. When it reaches that point, it picks a new desired point and repeats. If the human is ever in
     * range, it changes the FSM to be in Chase mode.
     */
    private class Wander implements DoubleConsumer {
        private Random rand = new Random();

        //TODO make the logic for picking a new desired point better - avoid jitteriness - maybe have a minimum distance it must be?
        //TODO make the movement chiller - add a slight delay in picking new points and how quickly you snap to them
        @Override
        public void accept(double dt) {

            if(targetInRange()) {
                brain.setState("chase");
                return;
            }

            // if it has reached the destination, pick a new point in WANDER_RANGE to be the desired point
            if(atDesiredLocation()) {
                desiredLocation = pickWanderPoint();
                //System.out.println("REACHED POINT");
                //System.out.println("new desired point is: " + parent.desiredLocation);
            }
            else moveToDesiredLocation(dt);
        }

        /**
         * Returns a point by randomly selecting a point within WANDER_RANGE distance on both x and y axis.
         * Repeats until the generated point is on-screen.
         * @return
         */
        private Point pickWanderPoint() {
            // have to do wanderRange+1 because the upper limit is exclusive, 
            // the zombie would tend towards negative values we didn't adjust by one
            Point point = location.add(new Point(rand.nextInt(-Zombie.WANDER_RANGE, Zombie.WANDER_RANGE + 1), 
                                                        rand.nextInt(-Zombie.WANDER_RANGE, Zombie.WANDER_RANGE + 1)));
            if(!testPointOnScreen(point)) {
                point = pickWanderPoint();
            }
            return point;
        }
    }

    private class Chase implements DoubleConsumer {
        @Override
        public void accept(double dt) {
            //make the desired location the player's position
            desiredLocation = target.getLocation();

            // go on alert if the player leaves our range
            if(!targetInRange()) {
                brain.setState("alert");
                return;
            }

            // if close enough for bounding boxes of this and target to overlap, go to fight
            double okayDistance = Math.sqrt(2 * Math.pow(target.getSize()+size, 2));            
            if(desiredLocation.distance(location) < okayDistance) {   
                //System.out.println("Reached Player: Fighting");
                brain.setState("fight");
                return;
            }
            else moveToDesiredLocation(dt);
        }
    }

    private class Fight implements DoubleConsumer {
        @Override
        public void accept(double dt) {
            double okayDistance = Math.sqrt(2 * Math.pow(target.getSize()+size, 2));   

            if(target.getLocation().distance(location) < okayDistance) {
                rotateSpriteToPoint(target.getLocation());
                target.takeDamage(damage);
            }

            else {
                brain.setState("chase");
                // System.out.println("swapped to chase from fight");
            }

        }
    }

    private class Alert implements DoubleConsumer {
        private boolean running = false;
        private Instant startTime;
        private double waitAmount = 2500;
        private double desiredRotation;
        private double rotateAmount = 90;
        private Random rand = new Random();
        
        //TODO make zombie rotate in a pretty way while on alert
        @Override
        public void accept(double dt) {
            // checks for needing to go back to chase first
            if(targetInRange()) {
                brain.setState("chase");
                running = false;
                return;
            }

            // we know desired is the most recent logged player position because we don't change it after switching from chase
            if(!atDesiredLocation()) {
                moveToDesiredLocation(dt);
            }

            else {
                //TODO add desiredRotation and rotating logic here
                // check if our timer is running, if isn't then start our timer and up our perception range
                if(!running) {
                    startTime = Instant.now();
                    running = true;
                    desiredRotation = rand.nextDouble(sprite.getRotation() - rotateAmount, sprite.getRotation() + rotateAmount);
                    System.out.println(desiredRotation);
                }
                // check if the timer is longer than our allowed wait time
                // if it is, go back to wandering and reset the timer
                else if(Instant.now().toEpochMilli() - startTime.toEpochMilli() >= waitAmount) {
                    running = false;
                    brain.setState("wander");
                    return;
                }
                else {
                    if(sprite.getRotation() == desiredRotation) {
                        desiredRotation = rand.nextDouble(sprite.getRotation() - rotateAmount, sprite.getRotation() + rotateAmount);
                    }
                    sprite.rotateBy(Math.copySign(2, desiredRotation - sprite.getRotation()));
                    if(Math.abs(sprite.getRotation() - desiredRotation) < 5) sprite.setRotation(desiredRotation);
                }
            }
        }
    }
}
