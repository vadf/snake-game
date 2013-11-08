import static org.junit.Assert.*;

import java.io.IOException;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestGameController {
    private GameController game;
    private String         testField = "src/test/resources/TestField.txt";

    @Before
    public void setUp() throws Exception {
        game = new GameController();
    }

    @After
    public void tearDown() throws Exception {
        game = null;
    }

    @Test
    public void testInitField() throws IOException {
        TextPoint expected = new TextPoint(4, 7);
        TextPoint actual = game.initField(testField);

        assertNotNull("Check that GameController Field is initialized.",
                game.field);
        assertEquals("Check field size", expected, actual);

    }

    @Test
    public void testInitAll() throws OutOfFieldException, IOException,
            SnakeOnWallException, TeleportInitFailed {
        int snakeSize = 3;
        Direction snakeDirection = Direction.RIGHT;
        TextPoint snakeHead = new TextPoint(1, 4);
        int numOfStars = 3;
        TextPoint p1 = new TextPoint(1, 1);
        TextPoint p2 = new TextPoint(2, 5);
        Direction d1 = Direction.RIGHT;
        Direction d2 = Direction.UP;

        game.initField(testField);
        game.initSnake(snakeHead, snakeDirection, snakeSize);
        game.initPorts(p1, d1, p2, d2);
        game.addStars(numOfStars);
        assertNotNull("Check that GameController Field is initialized.",
                game.field);
        assertNotNull("Check that Snake is initialized.", game.snake);
    }

    @Test(expected = SnakeOnWallException.class)
    public void testInitData_HeadOnAWall() throws SnakeOnWallException,
            OutOfFieldException, IOException {
        int snakeSize = 3;
        Direction snakeDirection = Direction.RIGHT;
        TextPoint snakeHead = new TextPoint(1, 6);
        game.initField(testField);
        game.initSnake(snakeHead, snakeDirection, snakeSize);
    }

    @Test(expected = OutOfFieldException.class)
    public void testInitData_HeadOutOfField() throws OutOfFieldException,
            IOException, SnakeOnWallException {
        int snakeSize = 3;
        Direction snakeDirection = Direction.RIGHT;
        TextPoint snakeHead = new TextPoint(2, 7);
        game.initField(testField);
        game.initSnake(snakeHead, snakeDirection, snakeSize);
    }

    @Test
    public void testInit_PortOnWall() throws OutOfFieldException, IOException,
            TeleportInitFailed {
        TextPoint p1 = new TextPoint(1, 0);
        TextPoint p2 = new TextPoint(2, 5);
        Direction d1 = Direction.RIGHT;
        Direction d2 = Direction.UP;

        game.initField(testField);
        game.initPorts(p1, d1, p2, d2);
        int expected = 18;
        int actual = game.field.getWalls().size();
        assertEquals("Check number of walls", expected, actual);
    }

    @Test(expected = TeleportInitFailed.class)
    public void testInit_PortDirectionNok() throws OutOfFieldException,
            IOException, PortAddException, TeleportInitFailed {
        TextPoint p1 = new TextPoint(1, 1);
        TextPoint p2 = new TextPoint(2, 5);
        Direction d1 = Direction.UP;
        Direction d2 = Direction.UP;

        game.initField(testField);
        game.initPorts(p1, d1, p2, d2);
    }

    @Test
    public void testToString() throws OutOfFieldException, IOException,
            SnakeOnWallException, TeleportInitFailed {
        int snakeSize = 3;
        Direction snakeDirection = Direction.RIGHT;
        TextPoint snakeHead = new TextPoint(1, 4);
        int numOfStars = 3;
        TextPoint p1 = new TextPoint(1, 1);
        TextPoint p2 = new TextPoint(2, 5);
        Direction d1 = Direction.RIGHT;
        Direction d2 = Direction.UP;

        game.initField(testField);
        game.initSnake(snakeHead, snakeDirection, snakeSize);
        game.initPorts(p1, d1, p2, d2);
        game.addStars(numOfStars);

        String textField = game.toString();
        int expectedLength = game.field.getRowsNum() * game.field.getColsNum()
                + game.field.getRowsNum();
        int actualLength = textField.length();
        assertEquals("Check the length of Text Field.", expectedLength,
                actualLength);

        assertTrue("Check that Text Field contains '**@'",
                textField.contains("**@"));

        List<TextPoint> stars = game.stars.getStars();
        for (TextPoint p : stars) {
            char star = textField.charAt(p.row * game.field.getColsNum()
                    + p.row + p.col);
            assertEquals("Check star.", '+', star);
        }

        assertTrue("Check that Text Field contains '0'",
                textField.contains("0"));
    }

    @Test
    public void testMove() throws OutOfFieldException, IOException,
            SnakeOnWallException, SnakeAddException, SnakeCollision {
        int snakeSize = 3;
        Direction snakeDirection = Direction.RIGHT;
        TextPoint snakeHead = new TextPoint(1, 4);
        game.initField(testField);
        game.initSnake(snakeHead, snakeDirection, snakeSize);

        game.move();
        TextPoint actual = game.snake.getHead();
        TextPoint expected = new TextPoint(snakeHead.row, snakeHead.col + 1);
        assertEquals("Check Snake Head", expected, actual);

        int expectedScore = 0;
        int actualScore = game.getScore();
        assertEquals("Check Score", expectedScore, actualScore);
    }

    @Test(expected = SnakeOnWallException.class)
    public void testMoveOnWall() throws OutOfFieldException, IOException,
            SnakeOnWallException, SnakeAddException, SnakeCollision {
        int snakeSize = 3;
        Direction snakeDirection = Direction.RIGHT;
        TextPoint snakeHead = new TextPoint(1, 5);
        game.initField(testField);
        game.initSnake(snakeHead, snakeDirection, snakeSize);

        game.move();
    }

    @Test
    public void testMoveOnPort() throws OutOfFieldException, IOException,
            SnakeOnWallException, SnakeAddException, SnakeCollision,
            TeleportInitFailed {
        int snakeSize = 3;
        Direction snakeDirection = Direction.RIGHT;
        TextPoint snakeHead = new TextPoint(1, 4);
        TextPoint p1 = new TextPoint(2, 1);
        TextPoint p2 = new TextPoint(1, 5);
        Direction d1 = Direction.UP;
        Direction d2 = Direction.LEFT;

        game.initField(testField);
        game.initPorts(p1, d1, p2, d2);
        game.initSnake(snakeHead, snakeDirection, snakeSize);

        game.move();
        TextPoint actual = game.snake.getHead();
        assertEquals("Check new Head position", p1, actual);

        game.move();
        actual = game.snake.getHead();
        TextPoint expected = new TextPoint(p1.row - 1, p1.col);
        assertEquals("Check new Head position", expected, actual);
    }

    @Test
    public void testMoveOnStar() throws OutOfFieldException, IOException,
            SnakeOnWallException, SnakeAddException, SnakeCollision {
        int snakeSize = 4;
        Direction snakeDirection = Direction.RIGHT;
        TextPoint snakeHead = new TextPoint(1, 4);
        int numOfStars = 5;
        game.initField(testField);
        game.initSnake(snakeHead, snakeDirection, snakeSize);
        game.addStars(numOfStars);

        game.move();
        TextPoint actualHead = game.snake.getHead();
        TextPoint expectedHead = new TextPoint(snakeHead.row, snakeHead.col + 1);
        assertEquals("Check Snake Head", expectedHead, actualHead);

        int expectedSize = 5;
        int actualSize = game.snake.getSize();
        assertEquals("Check Snake Size", expectedSize, actualSize);

        int expectedStars = 4;
        int actualStars = game.stars.getNumOfStars();
        assertEquals("Check Number of Stars", expectedStars, actualStars);

        int expectedScore = 1;
        int actualScore = game.getScore();
        assertEquals("Check Score", expectedScore, actualScore);
    }

    @Test
    public void testGetEmptyPointOk() throws OutOfFieldException, IOException,
            SnakeOnWallException, SnakeAddException, TeleportInitFailed {
        int snakeSize = 3;
        Direction snakeDirection = Direction.RIGHT;
        TextPoint snakeHead = new TextPoint(1, 4);
        int numOfStars = 3;
        TextPoint p1 = new TextPoint(1, 1);
        TextPoint p2 = new TextPoint(2, 5);
        Direction d1 = Direction.RIGHT;
        Direction d2 = Direction.UP;

        game.initField(testField);
        game.initSnake(snakeHead, snakeDirection, snakeSize);
        game.initPorts(p1, d1, p2, d2);
        game.addStars(numOfStars);

        TextPoint p = game.getEmptyPoint();
        assertNotNull("Check that empty point is found", p);
    }

    @Test
    public void testGetEmptyPointNok() throws OutOfFieldException, IOException,
            SnakeOnWallException, SnakeAddException, TeleportInitFailed {
        int snakeSize = 3;
        Direction snakeDirection = Direction.RIGHT;
        TextPoint snakeHead = new TextPoint(1, 4);
        int numOfStars = 4;
        TextPoint p1 = new TextPoint(1, 1);
        TextPoint p2 = new TextPoint(2, 5);
        Direction d1 = Direction.RIGHT;
        Direction d2 = Direction.UP;

        game.initField(testField);
        game.initSnake(snakeHead, snakeDirection, snakeSize);
        game.initPorts(p1, d1, p2, d2);
        game.addStars(numOfStars);

        TextPoint p = game.getEmptyPoint();
        assertNull("Check that there are no empty points", p);
    }
}
