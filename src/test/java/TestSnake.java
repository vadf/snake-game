import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestSnake {
    private Snake           snake;
    private int             snakeSize = 3;
    private Snake.Direction direction = Snake.Direction.RIGHT;
    private TextPoint       head      = new TextPoint(1, 5);

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
        TextPoint p = new TextPoint(head.row, head.col - snakeSize + 1);
        boolean expected = true;
        boolean actual = snake.isOnSnake(p);
        assertEquals("Check Is point on Snake.", expected, actual);
    }

    @Test
    public void testIsOnSnakeFalse() {
        TextPoint p = new TextPoint(head.row, head.col + 1);
        boolean expected = false;
        boolean actual = snake.isOnSnake(p);
        assertEquals("Check Is point on Snake.", expected, actual);
    }

    @Test
    public void testGetSnake() {
        List<TextPoint> expected = new ArrayList<TextPoint>();
        expected.add(head);
        expected.add(new TextPoint(head.row, head.col - 1));
        expected.add(new TextPoint(head.row, head.col - 2));
        List<TextPoint> actual = snake.getSnake();
        assertEquals("Check Snake.", expected, actual);
    }

    @Test
    public void testAdd() {
        snake.add();

        List<TextPoint> expected = new ArrayList<TextPoint>();
        expected.add(head);
        expected.add(new TextPoint(head.row, head.col - 1));
        expected.add(new TextPoint(head.row, head.col - 2));
        expected.add(0, new TextPoint(head.row, head.col + 1));
        List<TextPoint> actual = snake.getSnake();
        assertEquals("Check Snake.", expected, actual);
    }

    @Test
    public void testTurnUp() {
        snake.turn(Snake.Direction.UP);
        snake.add();

        List<TextPoint> expected = new ArrayList<TextPoint>();
        expected.add(head);
        expected.add(new TextPoint(head.row, head.col - 1));
        expected.add(new TextPoint(head.row, head.col - 2));
        expected.add(0, new TextPoint(head.row - 1, head.col));
        List<TextPoint> actual = snake.getSnake();
        assertEquals("Check Snake.", expected, actual);
    }

    @Test
    public void testTurnDown() {
        snake.turn(Snake.Direction.DOWN);
        snake.add();

        List<TextPoint> expected = new ArrayList<TextPoint>();
        expected.add(head);
        expected.add(new TextPoint(head.row, head.col - 1));
        expected.add(new TextPoint(head.row, head.col - 2));
        expected.add(0, new TextPoint(head.row + 1, head.col));
        List<TextPoint> actual = snake.getSnake();
        assertEquals("Check Snake.", expected, actual);
    }

    @Test
    public void testTurnRight() {
        snake = new Snake(head, snakeSize, Snake.Direction.UP);
        snake.turn(Snake.Direction.RIGHT);
        snake.add();

        List<TextPoint> expected = new ArrayList<TextPoint>();
        expected.add(head);
        expected.add(new TextPoint(head.row + 1, head.col));
        expected.add(new TextPoint(head.row + 2, head.col));
        expected.add(0, new TextPoint(head.row, head.col + 1));
        List<TextPoint> actual = snake.getSnake();
        assertEquals("Check Snake.", expected, actual);
    }

    @Test
    public void testTurnLeft() {
        snake = new Snake(head, snakeSize, Snake.Direction.UP);
        snake.turn(Snake.Direction.LEFT);
        snake.add();

        List<TextPoint> expected = new ArrayList<TextPoint>();
        expected.add(head);
        expected.add(new TextPoint(head.row + 1, head.col));
        expected.add(new TextPoint(head.row + 2, head.col));
        expected.add(0, new TextPoint(head.row, head.col - 1));
        List<TextPoint> actual = snake.getSnake();
        assertEquals("Check Snake.", expected, actual);
    }

    @Test
    public void testTurnSameDirection() {
        snake.turn(direction);
        snake.add();

        List<TextPoint> expected = new ArrayList<TextPoint>();
        expected.add(head);
        expected.add(new TextPoint(head.row, head.col - 1));
        expected.add(new TextPoint(head.row, head.col - 2));
        expected.add(0, new TextPoint(head.row, head.col + 1));
        List<TextPoint> actual = snake.getSnake();
        assertEquals("Check Snake.", expected, actual);
    }

    @Test
    public void testTurnOppositeDirection() {
        snake.turn(Snake.Direction.LEFT);
        snake.add();

        List<TextPoint> expected = new ArrayList<TextPoint>();
        expected.add(head);
        expected.add(new TextPoint(head.row, head.col - 1));
        expected.add(new TextPoint(head.row, head.col - 2));
        expected.add(0, new TextPoint(head.row, head.col + 1));
        List<TextPoint> actual = snake.getSnake();
        assertEquals("Check Snake.", expected, actual);
    }

    @Test
    public void testMove() {
        snake.move();

        List<TextPoint> expected = new ArrayList<TextPoint>();
        expected.add(new TextPoint(head.row, head.col + 1));
        expected.add(new TextPoint(head.row, head.col));
        expected.add(new TextPoint(head.row, head.col - 1));
        List<TextPoint> actual = snake.getSnake();
        assertEquals("Check Snake.", expected, actual);
    }
}
