import static org.junit.Assert.*;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestSnake {
    private Snake           snake;
    private int             snakeSize = 3;
    private Snake.Direction direction = Snake.Direction.RIGHT;
    private Point           head      = new Point(1, 5);

    @Before
    public void setUp() throws Exception {
        snake = new Snake(head, snakeSize, direction);
    }

    @After
    public void tearDown() throws Exception {
        snake = null;
    }

    @Test
    public void testGetSize() {
        int expected = snakeSize;
        int actual = snake.getSize();
        assertEquals("Check Snake size.", expected, actual);
    }

    @Test
    public void testIsOnSnakeTrue() {
        Point p = new Point(1, 5);
        boolean expected = true;
        boolean actual = snake.isOnSnake(p);
        assertEquals("Check Is point on Snake.", expected, actual);
    }

    @Test
    public void testIsOnSnakeFalse() {
        Point p = new Point(1, 6);
        boolean expected = false;
        boolean actual = snake.isOnSnake(p);
        assertEquals("Check Is point on Snake.", expected, actual);
    }

    @Test
    public void testAdd() {
        int expected = snakeSize + 1;
        int actual = snake.add();
        assertEquals("Check Snake size.", expected, actual);
    }

    @Test
    public void testSnake() {
        List<Point> expected = new ArrayList<Point>();
        expected.add(head);
        expected.add(new Point(head.x, head.y - 1));
        expected.add(new Point(head.x, head.y - 2));
        List<Point> actual = snake.getSnake();
        assertEquals("Check Snake.", expected, actual);

        snake.add();
        expected.add(0, new Point(head.x, head.y + 1));
        actual = snake.getSnake();
        assertEquals("Check Snake.", expected, actual);
    }

}
