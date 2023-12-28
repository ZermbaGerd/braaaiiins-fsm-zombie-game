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
 * Creates a visual representation of a Zombie Game handling player interaction. 
 */
public class View {
    private CanvasWindow canvas;
    private Human human;
    private Button start;
    private TextField zomNum;
    private GraphicsText direct;
    private Instant startTime;

    private ArrayList<Rectangle> healthBar = new ArrayList<Rectangle>();
    private GraphicsText time;
    private ArrayList<Zombie> zombieList = new ArrayList<Zombie>(); //DATA STRuc


    public static final int WIDTH = 650;
    public static final int HEIGHT = 650;
    private Image background = new Image("background.jpg");
    public static final Point BG_OFFSET = new Point(500, 500);

    public View() {
        canvas = new CanvasWindow("ZOMBIE BRAINS", WIDTH, HEIGHT);

        canvas.add(background);
        background.setPosition(new Point(0,0).subtract(BG_OFFSET));

        start = new Button("Start!");
        start.setPosition(WIDTH/2 - start.getWidth()/2, HEIGHT*.5);
        canvas.add(start);

        createZomNum();
        createDirect();
    }

    /**
     * Creates the text field and adds to canvas
     */
    private void createZomNum(){
        zomNum = new TextField();
        zomNum.setPosition(WIDTH/2 - zomNum.getWidth()/2, HEIGHT*.45);
        canvas.add(zomNum);

    }

    /**
     * Creates and formats inital game directions
     */
    private void createDirect(){
        direct = new GraphicsText("Enter the number of zombies you would like to create and press Start! to begin ");
        direct.setPosition(WIDTH/2 - direct.getWidth()/2, HEIGHT*.4);
        direct.setFillColor(Color.WHITE);
        direct.setFontStyle(FontStyle.BOLD);
        canvas.add(direct);
    }

    /**
     * Runs the game updating player based on arrow/AWD interaction and updating the zombies and their visual depiction
     */
    public void run() {
        setup();

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
     * Takes the values entered in TextField and uses to update the screen
     */
    private void setup(){ //uses values entered to begin screen to setup the game
        canvas.remove(start);
        canvas.remove(zomNum);
        canvas.remove(direct);
         //test this logic with fractional input + just text
        addChar(checkText(zomNum.getText()));
        createHealthBar();
        startTimer();
    }

    private int checkText(String text){ //TODO fix so rounds double input
        int i = 1;
        try {
            i = Integer.parseInt(text);
          } catch (Exception e) {
            System.out.println("Something went wrong.");
          }
        
        return i;
    }

    /**
     * Updates player health bar and triggers lose method when player dies.. 
     */
    private void updateHealth(){ //thinking of two implementations - add ten squares subtracted with corresponding heath
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
     * Closes canvas window 
     */
    private void lose(){
        canvas.removeAll();
        canvas.setBackground(Color.GRAY);
        GraphicsText text = new GraphicsText("YOU LOSE", WIDTH/2, HEIGHT/2);
        //TODO: switch background here

        canvas.add(text);
        canvas.draw();
        canvas.pause(3000);
        canvas.closeWindow();
    }

    /**
     * Creates health bar visual and adds to canvas
     */
    private void createHealthBar(){
        Rectangle rect;
        GraphicsText text = new GraphicsText("Health: ", WIDTH - 175, 35);
        text.setFillColor(Color.RED);
        text.setFontStyle(FontStyle.BOLD);
        canvas.add(text);
        for (int i= 0; i < 10; i++) {
            rect = new Rectangle((WIDTH - 110) + (10 * healthBar.size()), 25, 10, 10);
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
        time.setPosition(WIDTH - 170, 50);
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
            Zombie zombie = new Zombie(new Point(rand.nextInt(WIDTH), rand.nextInt(HEIGHT)), human);
            zombieList.add(zombie);
            canvas.add(zombie.getSprite());
        }  
    }

    public static void main(String[] args) {
        View game = new View();
        game.start.onClick(() -> game.run());

    }
}