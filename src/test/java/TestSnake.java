import static org.junit.Assert.*;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestSnake {
    private Snake     snake;
    private int       snakeSize = 3;
    private Direction direction = Direction.RIGHT;
    private TextPoint head      = new TextPoint(1, 5);
    private TextPoint border    = new TextPoint(6, 6);

    @Before
    public void setUp() throws Exception {
        snake = new Snake(head, direction, snakeSize);
    }

    @After
    public void tearDown() throws Exception {
        snake = null;
    }

    @Test
    public void testInitSnakeWithBorder() {
        assertNotNull("Check Snake size.", new Snake(head, direction, snakeSize, border));
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
        assertTrue("Check Is point on Snake.", snake.isOnSnake(p));
    }

    @Test
    public void testIsOnSnakeFalse() {
        TextPoint p = new TextPoint(head.row, head.col + 1);
        assertFalse("Check Is point on Snake.", snake.isOnSnake(p));
    }

    @Test
    public void testGetHead() {
        TextPoint expected = new TextPoint(head);
        TextPoint actual = snake.getHead();
        assertEquals("Check Snake Head.", expected, actual);
    }

    @Test
    public void testMoveAdd() throws SnakeAddException, SnakeCollision {
        List<TextPoint> expectedSnake = snake.getSnake();
        snake.move();
        snake.add();

        TextPoint expectedHead = new TextPoint(head.row, head.col + 1);
        TextPoint actualHead = snake.getHead();
        assertEquals("Check Snake.", expectedHead, actualHead);

        int expectedSize = 4;
        int actualSize = snake.getSize();
        assertEquals("Check Snake.", expectedSize, actualSize);

        expectedSnake.add(0, expectedHead);
        ;
        List<TextPoint> actualSnake = snake.getSnake();
        assertEquals("Check Snake.", expectedSnake, actualSnake);
    }

    @Test(expected = SnakeAddException.class)
    public void testAddTwice() throws SnakeAddException, SnakeCollision {
        snake.move();
        snake.add();
        snake.add();
    }

    @Test(expected = SnakeAddException.class)
    public void testAddNull() throws SnakeAddException {
        snake.add();
    }

    @Test
    public void testTurnUp() throws SnakeCollision {
        snake.turn(Direction.UP);
        snake.move();

        TextPoint expected = new TextPoint(head.row - 1, head.col);
        TextPoint actual = snake.getHead();
        assertEquals("Check Snake Head.", expected, actual);
    }

    @Test
    public void testTurnDown() throws SnakeCollision {
        snake.turn(Direction.DOWN);
        snake.move();

        TextPoint expected = new TextPoint(head.row + 1, head.col);
        TextPoint actual = snake.getHead();
        assertEquals("Check Snake Head.", expected, actual);
    }

    @Test
    public void testTurnRight() throws SnakeCollision {
        snake = new Snake(head, Direction.UP, snakeSize);
        snake.turn(Direction.RIGHT);
        snake.move();

        TextPoint expected = new TextPoint(head.row, head.col + 1);
        TextPoint actual = snake.getHead();
        assertEquals("Check Snake Head.", expected, actual);
    }

    @Test
    public void testTurnLeft() throws SnakeCollision {
        snake = new Snake(head, Direction.UP, snakeSize);
        snake.turn(Direction.LEFT);
        snake.move();

        TextPoint expected = new TextPoint(head.row, head.col - 1);
        TextPoint actual = snake.getHead();
        assertEquals("Check Snake Head.", expected, actual);
    }

    @Test
    public void testTurnSameDirection() throws SnakeCollision {
        snake.turn(direction);
        snake.move();

        TextPoint expected = new TextPoint(head.row, head.col + 1);
        TextPoint actual = snake.getHead();
        assertEquals("Check Snake Head.", expected, actual);
    }

    @Test
    public void testTurnOppositeDirection() throws SnakeCollision {
        snake.turn(Direction.LEFT);
        snake.move();

        TextPoint expected = new TextPoint(head.row, head.col + 1);
        TextPoint actual = snake.getHead();
        assertEquals("Check Snake.", expected, actual);
    }

    @Test
    public void testMove() throws SnakeCollision {
        snake.move();

        TextPoint expected = new TextPoint(head.row, head.col + 1);
        TextPoint actual = snake.getHead();
        assertEquals("Check Snake Head.", expected, actual);
    }

    @Test(expected = SnakeCollision.class)
    public void testMoveSnakeCollision() throws SnakeAddException, SnakeCollision {
        snake.move();
        snake.add();

        snake.turn(Direction.DOWN);
        snake.move();
        snake.add();

        snake.turn(Direction.LEFT);
        snake.move();
        snake.add();

        snake.turn(Direction.UP);
        snake.move(); // collision with snake body
        System.out.println(snake.getSnake() + "");
    }

    @Test
    public void testMoveOverBorderRight() throws SnakeCollision {
        snake = new Snake(head, direction, snakeSize, border);
        snake.move();

        TextPoint expected = new TextPoint(head.row, 0);
        TextPoint actual = snake.getHead();
        assertEquals("Check Snake Head.", expected, actual);
    }

    @Test
    public void testMoveOverBorderUp() throws SnakeCollision {
        snake = new Snake(head, Direction.UP, snakeSize, border);
        snake.move();
        snake.move();

        TextPoint expected = new TextPoint(border.row - 1, head.col);
        TextPoint actual = snake.getHead();
        assertEquals("Check Snake Head.", expected, actual);
    }

    @Test
    public void testChangeHead() throws SnakeCollision {
        TextPoint newHead = new TextPoint(head.row + 2, head.col + 3);
        snake.changeHeadPosition(newHead, Direction.DOWN);

        snake.move();
        TextPoint expected = new TextPoint(newHead.row + 1, newHead.col);
        TextPoint actual = snake.getHead();
        assertEquals("Check new Snake Head", expected, actual);
    }
}
