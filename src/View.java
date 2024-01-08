import edu.macalester.graphics.CanvasWindow;
import edu.macalester.graphics.FontStyle;
import edu.macalester.graphics.GraphicsText;
import edu.macalester.graphics.Rectangle;
import edu.macalester.graphics.Image;
import edu.macalester.graphics.Point;
import edu.macalester.graphics.events.Key;
import edu.macalester.graphics.ui.Button;
import java.util.ArrayList;
import java.util.Random;

import edu.macalester.graphics.ui.TextField;

import java.awt.Color;
import java.time.Instant;
import java.time.Duration;

/**
 * Runs an instance of Braiiiins
 * @author Gavin Davis - gdavis2@macalester.edu
 * @author Hadley Wilkins - hwilkins@macalester.edu
 */
public class View {

    // Game actors
    private CanvasWindow canvas;
    private Human human;
    private Instant startTime;
    private ArrayList<Zombie> zombieList = new ArrayList<Zombie>();

    // Menu UI
    private Button start;
    private TextField zombieSelector;
    private GraphicsText directions;

    // Game UI
    private ArrayList<Rectangle> healthBar = new ArrayList<Rectangle>();
    private GraphicsText time;

    // Background
    public static final int CANVAS_WIDTH = 650;
    public static final int CANVAS_HEIGHT = 650;
    public static final Point BG_OFFSET = new Point(500, 500);
    private Image backgroundImg = new Image("background.jpg");

    public View() {
        canvas = new CanvasWindow("Braaaiiins: A Zombie Survival Game", CANVAS_WIDTH, CANVAS_HEIGHT);

        canvas.add(backgroundImg);
        backgroundImg.setPosition(new Point(0,0).subtract(BG_OFFSET));

        start = new Button("Start!");
        start.setPosition(CANVAS_WIDTH/2 - start.getWidth()/2, CANVAS_HEIGHT*.5);
        canvas.add(start);

        createMainMenu();
    }

    /**
     * Creates the main menu UI
     */
    private void createMainMenu(){
        zombieSelector = new TextField();
        zombieSelector.setPosition(CANVAS_WIDTH/2 - zombieSelector.getWidth()/2, CANVAS_HEIGHT*.45);

        directions = new GraphicsText("Enter the number of zombies you would like to create and press Start! to begin ");
        directions.setPosition(CANVAS_WIDTH/2 - directions.getWidth()/2, CANVAS_HEIGHT*.4);
        directions.setFillColor(Color.WHITE);
        directions.setFontStyle(FontStyle.BOLD);

        canvas.add(zombieSelector);
        canvas.add(directions);
    }

    /**
     * Runs the game
     */
    public void run() {
        setup();

        // this lambda loop runs every frame. dt is the time passed since the last frame
        canvas.animate(dt -> {
            if (canvas.getKeysPressed().contains(Key.LEFT_ARROW) || canvas.getKeysPressed().contains(Key.A)) human.moveLeft(dt);
            if (canvas.getKeysPressed().contains(Key.RIGHT_ARROW) || canvas.getKeysPressed().contains(Key.D)) human.moveRight(dt);
            if (canvas.getKeysPressed().contains(Key.UP_ARROW) || canvas.getKeysPressed().contains(Key.W)) human.moveUp(dt);
            if (canvas.getKeysPressed().contains(Key.DOWN_ARROW) || canvas.getKeysPressed().contains(Key.S)) human.moveDown(dt);
            for(Zombie z: zombieList){
                z.update(dt);
                z.updateSprite();
            }
            updateHealth();
            updateTimer();
        });
    }

    /**
     * Takes the values entered in directions and zombieNumber and uses them to update the screen
     */
    private void setup() {
        canvas.remove(start);
        canvas.remove(zombieSelector);
        canvas.remove(directions);

        addChar(checkText(zombieSelector.getText()));

        createHealthBar();
        startTimer();
    }

    /**
     * Returns the string parsed as an int, defaulting to 1 if it can't be parsed.
     * @param text
     * @return
     */
    private int checkText(String text) {
        int zombieNumber = 1;  
        try {
            zombieNumber = Integer.parseInt(text);
          } catch (Exception e) {
            System.out.println("Something went wrong.");
          }
        
        return zombieNumber;
    }

    /**
     * Updates player health bar and triggers lose method when player dies.. 
     */
    private void updateHealth() { //thinking of two implementations - add ten squares subtracted with corresponding heath
        Rectangle curRect;
        if(human.getHealth() == 0){
            lose();
        }
        if (human.getHealth()/10 < healthBar.size()){
            curRect = healthBar.get(healthBar.size()-1);
            curRect.setFillColor(Color.LIGHT_GRAY);
            healthBar.remove(curRect);
        }
    }

    /**
     * Ends game and shows loss screen
     */
    private void lose(){
        canvas.removeAll();
        canvas.setBackground(Color.GRAY);

        Image loseBG = new Image("falloutLoseScreen.png");
        loseBG.setScale(2);
        loseBG.setMaxHeight(CANVAS_HEIGHT);
        canvas.add(loseBG);

        GraphicsText text = new GraphicsText("YOU LOSE");
        text.setFontSize(50);
        text.setPosition(CANVAS_WIDTH/2 - text.getWidth()/2, CANVAS_HEIGHT/2);

        canvas.add(text);
        canvas.draw();
        canvas.pause(5000);
        canvas.closeWindow();
    }

    /**
     * Creates health bar visual and adds to canvas
     */
    private void createHealthBar(){
        Rectangle rect;
        GraphicsText text = new GraphicsText("Health: ", CANVAS_WIDTH - 175, 35);
        text.setFillColor(Color.RED);
        text.setFontStyle(FontStyle.BOLD);
        canvas.add(text);
        for (int i= 0; i < 10; i++) {
            rect = new Rectangle((CANVAS_WIDTH - 110) + (10 * healthBar.size()), 25, 10, 10);
            rect.setFillColor(Color.RED);
            healthBar.add(rect);
            canvas.add(rect);
        }
    }

    /**
     * Starts the game timer and creates the label.
     */
    private void startTimer(){
        startTime = Instant.now();
        
        time = new GraphicsText();
        time.setFillColor(Color.RED);
        time.setFontStyle(FontStyle.BOLD);
        time.setPosition(CANVAS_WIDTH - 170, 50);
        this.time.setText("00:00:00");

        canvas.add(this.time);
    }

    /**
     * Updates the timer label for the game.
     */
    private void updateTimer(){
        String newTime;
        Instant currentTime = Instant.now();
        Duration duration = Duration.between(startTime, currentTime);
        newTime = new String(String.format("Time Elapsed: %02d:%02d:%02d", duration.toHours(), duration.toMinutes(), duration.toSeconds()));
        this.time.setText(newTime);
    }

    /**
     * Adds the entities to the screen, including one human and described number of zombies
     * @param numZombies number of zombies to be placed on the screen
     */
    private void addChar (int numZombies){ //TODO add different zombie speeds
        human = new Human(new Point(100, 400));
        canvas.add(human.getSprite());

        Random rand = new Random();
        for (int i = 0; i < numZombies; i++){
            Zombie zombie = new Zombie(new Point(rand.nextInt(CANVAS_WIDTH), rand.nextInt(CANVAS_HEIGHT)), human);
            zombieList.add(zombie);
            canvas.add(zombie.getSprite());
        }  
    }

    public static void main(String[] args) {
        View game = new View();
        game.start.onClick(() -> game.run());

    }
}