import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import edu.macalester.graphics.Point;
import java.time.Instant;

public class TestZombie {
    @Test
    public void testPerceptionRange() {
        Human human = new Human(new Point(51, 51));
        Zombie zombie = new Zombie(new Point(50, 50), human);

        assertTrue(zombie.targetInRange());

        Human human2 = new Human(new Point(1000, 1000));
        zombie.setTarget(human2);
        assertFalse(zombie.targetInRange());

        //TODO add edge case for very close situations
    }

    @Test
    public void testPointLogic() {
        Point point1 = new Point(50, 50);
        Point point2 = new Point(10, -10);
        Point point3 = point1.add(point2);
        Point point4 = new Point(60, 40);
        System.out.println(point3);
        System.out.println(point1);
        System.out.println(point3.equals(point4));
    }

    @Test
    public void testTimeLogic() {
        Instant startTime = Instant.now();
        System.out.println(startTime.toString());
        long j = 0;
        for (long i = 0; i < 1000000000; i++) {
            j += 1;
        }
        System.out.println(startTime.toEpochMilli() - Instant.now().toEpochMilli());
        //System.out.println(Instant.now().minus(startTime.getEpochSecond()));
    }

    @Test
    public void testAngleRotation() {
        Point point1 = new Point(50, 50);
        Point point2 = new Point(100, 100);
        Point point3 = new Point(50, 0 );
        System.out.println(point1.angle());
        System.out.println(point2.angle());
        System.out.println(point3.angle());
        
        System.out.println(0.7853981633974483 / Math.PI);
    }

    // @Test
    // public void zombieRotationMemory() {
    //     Human human = new Human(new Point(500,500));
    //     Zombie zombie = new Zombie(new Point(200,200),human);

    //     zombie.update();
    //     System.out.println(zombie.getDesired());
    //     System.out.println(zombie.getLocation());
    // }
}
