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
        Point p = new Point(head.x, head.y - snakeSize + 1);
        boolean expected = true;
        boolean actual = snake.isOnSnake(p);
        assertEquals("Check Is point on Snake.", expected, actual);
    }

    @Test
    public void testIsOnSnakeFalse() {
        Point p = new Point(head.x, head.y + 1);
        boolean expected = false;
        boolean actual = snake.isOnSnake(p);
        assertEquals("Check Is point on Snake.", expected, actual);
    }

    @Test
    public void testGetSnake() {
        List<Point> expected = new ArrayList<Point>();
        expected.add(head);
        expected.add(new Point(head.x, head.y - 1));
        expected.add(new Point(head.x, head.y - 2));
        List<Point> actual = snake.getSnake();
        assertEquals("Check Snake.", expected, actual);
    }

    @Test
    public void testAdd() {
        snake.add();

        List<Point> expected = new ArrayList<Point>();
        expected.add(head);
        expected.add(new Point(head.x, head.y - 1));
        expected.add(new Point(head.x, head.y - 2));
        expected.add(0, new Point(head.x, head.y + 1));
        List<Point> actual = snake.getSnake();
        assertEquals("Check Snake.", expected, actual);
    }

    @Test
    public void testTurnUp() {
        snake.turn(Snake.Direction.UP);
        snake.add();

        List<Point> expected = new ArrayList<Point>();
        expected.add(head);
        expected.add(new Point(head.x, head.y - 1));
        expected.add(new Point(head.x, head.y - 2));
        expected.add(0, new Point(head.x - 1, head.y));
        List<Point> actual = snake.getSnake();
        assertEquals("Check Snake.", expected, actual);
    }

    @Test
    public void testTurnDown() {
        snake.turn(Snake.Direction.DOWN);
        snake.add();

        List<Point> expected = new ArrayList<Point>();
        expected.add(head);
        expected.add(new Point(head.x, head.y - 1));
        expected.add(new Point(head.x, head.y - 2));
        expected.add(0, new Point(head.x + 1, head.y));
        List<Point> actual = snake.getSnake();
        assertEquals("Check Snake.", expected, actual);
    }

    @Test
    public void testTurnRight() {
        snake = new Snake(head, snakeSize, Snake.Direction.UP);
        snake.turn(Snake.Direction.RIGHT);
        snake.add();

        List<Point> expected = new ArrayList<Point>();
        expected.add(head);
        expected.add(new Point(head.x + 1, head.y));
        expected.add(new Point(head.x + 2, head.y));
        expected.add(0, new Point(head.x, head.y + 1));
        List<Point> actual = snake.getSnake();
        assertEquals("Check Snake.", expected, actual);
    }

    @Test
    public void testTurnLeft() {
        snake = new Snake(head, snakeSize, Snake.Direction.UP);
        snake.turn(Snake.Direction.LEFT);
        snake.add();

        List<Point> expected = new ArrayList<Point>();
        expected.add(head);
        expected.add(new Point(head.x + 1, head.y));
        expected.add(new Point(head.x + 2, head.y));
        expected.add(0, new Point(head.x, head.y - 1));
        List<Point> actual = snake.getSnake();
        assertEquals("Check Snake.", expected, actual);
    }

    @Test
    public void testTurnSameDirection() {
        snake.turn(direction);
        snake.add();

        List<Point> expected = new ArrayList<Point>();
        expected.add(head);
        expected.add(new Point(head.x, head.y - 1));
        expected.add(new Point(head.x, head.y - 2));
        expected.add(0, new Point(head.x, head.y + 1));
        List<Point> actual = snake.getSnake();
        assertEquals("Check Snake.", expected, actual);
    }

    @Test
    public void testTurnOppositeDirection() {
        snake.turn(Snake.Direction.LEFT);
        snake.add();

        List<Point> expected = new ArrayList<Point>();
        expected.add(head);
        expected.add(new Point(head.x, head.y - 1));
        expected.add(new Point(head.x, head.y - 2));
        expected.add(0, new Point(head.x, head.y + 1));
        List<Point> actual = snake.getSnake();
        assertEquals("Check Snake.", expected, actual);
    }

    @Test
    public void testMove() {
        snake.move();

        List<Point> expected = new ArrayList<Point>();
        expected.add(new Point(head.x, head.y + 1));
        expected.add(new Point(head.x, head.y));
        expected.add(new Point(head.x, head.y - 1));
        List<Point> actual = snake.getSnake();
        assertEquals("Check Snake.", expected, actual);
    }
}
