import static org.junit.Assert.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestGameController {
    private GameController                game;
    private String                        testField = "src/test/resources/TestField.txt";
    private HashMap<TextPoint, Direction> ports;

    @Before
    public void setUp() throws Exception {
        game = new GameController();
        ports = new HashMap<TextPoint, Direction>();
    }

    @After
    public void tearDown() throws Exception {
        game = null;
        ports = null;
    }

    @Test
    public void testInitField() throws IOException, FieldInitException {
        TextPoint expected = new TextPoint(4, 7);
        TextPoint actual = game.initField(testField);

        assertNotNull("Check that GameController Field is initialized.", game.field);
        assertEquals("Check field size", expected, actual);

    }

    @Test
    public void testInitAll() throws OutOfFieldException, IOException, SnakeOnWallException,
            TeleportInitException, FieldInitException {
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
        ports.put(p1, d1);
        ports.put(p2, d2);
        game.initPorts(ports);
        game.addStars(numOfStars);
        assertNotNull("Check that GameController Field is initialized.", game.field);
        assertNotNull("Check that Snake is initialized.", game.snake);
    }

    @Test(expected = SnakeOnWallException.class)
    public void testInitData_HeadOnAWall() throws SnakeOnWallException, OutOfFieldException,
            IOException, FieldInitException {
        int snakeSize = 3;
        Direction snakeDirection = Direction.RIGHT;
        TextPoint snakeHead = new TextPoint(1, 6);
        game.initField(testField);
        game.initSnake(snakeHead, snakeDirection, snakeSize);
    }

    @Test(expected = OutOfFieldException.class)
    public void testInitData_HeadOutOfField() throws OutOfFieldException, IOException,
            SnakeOnWallException, FieldInitException {
        int snakeSize = 3;
        Direction snakeDirection = Direction.RIGHT;
        TextPoint snakeHead = new TextPoint(2, 7);
        game.initField(testField);
        game.initSnake(snakeHead, snakeDirection, snakeSize);
    }

    @Test
    public void testInit_PortOnWall() throws OutOfFieldException, IOException,
            TeleportInitException, FieldInitException {
        TextPoint p1 = new TextPoint(1, 0);
        TextPoint p2 = new TextPoint(2, 5);
        Direction d1 = Direction.RIGHT;
        Direction d2 = Direction.UP;

        game.initField(testField);
        ports.put(p1, d1);
        ports.put(p2, d2);
        game.initPorts(ports);
        int expected = 18;
        int actual = game.field.getWalls().size();
        assertEquals("Check number of walls", expected, actual);
    }

    @Test(expected = TeleportInitException.class)
    public void testInit_PortDirectionNok() throws OutOfFieldException, IOException,
            PortAddException, TeleportInitException, FieldInitException {
        TextPoint p1 = new TextPoint(1, 1);
        TextPoint p2 = new TextPoint(2, 5);
        Direction d1 = Direction.UP;
        Direction d2 = Direction.UP;

        game.initField(testField);
        ports.put(p1, d1);
        ports.put(p2, d2);
        game.initPorts(ports);
    }

    @Test
    public void testInit_2Teleports() throws OutOfFieldException, IOException, PortAddException,
            TeleportInitException, FieldInitException {
        TextPoint p1 = new TextPoint(1, 1);
        TextPoint p2 = new TextPoint(2, 5);
        Direction d1 = Direction.RIGHT;
        Direction d2 = Direction.UP;

        TextPoint p3 = new TextPoint(1, 2);
        TextPoint p4 = new TextPoint(1, 4);
        Direction d3 = Direction.DOWN;
        Direction d4 = Direction.DOWN;

        game.initField(testField);
        ports.put(p1, d1);
        ports.put(p2, d2);
        game.initPorts(ports);
        ports.clear();
        ports.put(p3, d3);
        ports.put(p4, d4);
        game.initPorts(ports);
        assertNotNull("Check that ports 0 created.", game.ports[0]);
        assertNotNull("Check that ports 1 created.", game.ports[1]);
    }

    @Test(expected = TeleportInitException.class)
    public void testInit_2SamePorts() throws OutOfFieldException, IOException, PortAddException,
            TeleportInitException, FieldInitException {
        TextPoint p1 = new TextPoint(1, 1);
        TextPoint p2 = new TextPoint(2, 5);
        Direction d1 = Direction.RIGHT;
        Direction d2 = Direction.UP;

        TextPoint p3 = new TextPoint(p1);
        TextPoint p4 = new TextPoint(1, 4);
        Direction d3 = Direction.DOWN;
        Direction d4 = Direction.DOWN;

        game.initField(testField);
        ports.put(p1, d1);
        ports.put(p2, d2);
        game.initPorts(ports);
        ports.clear();
        ports.put(p3, d3);
        ports.put(p4, d4);
        game.initPorts(ports);
    }

    @Test
    public void testToString() throws OutOfFieldException, IOException, SnakeOnWallException,
            TeleportInitException, FieldInitException {
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
        ports.put(p1, d1);
        ports.put(p2, d2);
        game.initPorts(ports);
        game.addStars(numOfStars);

        String textField = game.toString();
        int expectedLength = game.field.getRowsNum() * game.field.getColsNum()
                + game.field.getRowsNum();
        int actualLength = textField.length();
        assertEquals("Check the length of Text Field.", expectedLength, actualLength);

        assertTrue("Check that Text Field contains '**@'", textField.contains("**@"));

        List<TextPoint> stars = game.stars.getStars();
        for (TextPoint p : stars) {
            char star = textField.charAt(p.row * game.field.getColsNum() + p.row + p.col);
            assertEquals("Check star.", '+', star);
        }

        assertTrue("Check that Text Field contains '0'", textField.contains("0"));
    }

    @Test
    public void testMove() throws OutOfFieldException, IOException, SnakeOnWallException,
            SnakeAddException, SnakeCollision, FieldInitException {
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
    public void testMoveOnWall() throws OutOfFieldException, IOException, SnakeOnWallException,
            SnakeAddException, SnakeCollision, FieldInitException {
        int snakeSize = 3;
        Direction snakeDirection = Direction.RIGHT;
        TextPoint snakeHead = new TextPoint(1, 5);
        game.initField(testField);
        game.initSnake(snakeHead, snakeDirection, snakeSize);

        game.move();
    }

    @Test
    public void testMoveOnPort() throws OutOfFieldException, IOException, SnakeOnWallException,
            SnakeAddException, SnakeCollision, TeleportInitException, FieldInitException {
        int snakeSize = 3;
        Direction snakeDirection = Direction.RIGHT;
        TextPoint snakeHead = new TextPoint(1, 4);
        TextPoint p1 = new TextPoint(2, 1);
        TextPoint p2 = new TextPoint(1, 5);
        Direction d1 = Direction.UP;
        Direction d2 = Direction.LEFT;

        game.initField(testField);
        ports.put(p1, d1);
        ports.put(p2, d2);
        game.initPorts(ports);
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
    public void testMoveOnStar() throws OutOfFieldException, IOException, SnakeOnWallException,
            SnakeAddException, SnakeCollision, FieldInitException {
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
            SnakeOnWallException, SnakeAddException, TeleportInitException, FieldInitException {
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
        ports.put(p1, d1);
        ports.put(p2, d2);
        game.initPorts(ports);
        game.addStars(numOfStars);

        TextPoint p = game.getEmptyPoint();
        assertNotNull("Check that empty point is found", p);
    }

    @Test
    public void testGetEmptyPointNok() throws OutOfFieldException, IOException,
            SnakeOnWallException, SnakeAddException, TeleportInitException, FieldInitException {
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
        ports.put(p1, d1);
        ports.put(p2, d2);
        game.initPorts(ports);
        game.addStars(numOfStars);

        TextPoint p = game.getEmptyPoint();
        assertNull("Check that there are no empty points", p);
    }
}
