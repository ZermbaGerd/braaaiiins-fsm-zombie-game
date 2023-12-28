import edu.macalester.graphics.CanvasWindow;
import edu.macalester.graphics.Ellipse;
import edu.macalester.graphics.Point;

import java.awt.Color;

public class TestGUI {
    public CanvasWindow canvas = new CanvasWindow("Testing Movement", 500, 500);

    public TestGUI() {
        Human player = new Human(new Point(50,50));
        Zombie zombie1 = new Zombie(new Point (175,175), player);
        Ellipse perceptionExample = new Ellipse(zombie1.location.getX(), zombie1.location.getY(), zombie1.getPerceptionRange(), zombie1.getPerceptionRange());
        perceptionExample.setStrokeColor(Color.RED);

        canvas.add(zombie1.sprite);
        canvas.add(player.sprite);
        canvas.add(perceptionExample);

        canvas.animate(dt -> {
            zombie1.update(dt);
            zombie1.getSprite().setCenter(zombie1.getLocation());
            perceptionExample.setCenter(zombie1.getLocation());
        });
    }

    public static void main(String[] args) {
        new TestGUI();
    }
}