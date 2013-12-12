import static org.junit.Assert.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestGameController {
    private GameController                game;
    private String                        testField        = "src/test/resources/TestField.txt";
    private HashMap<TextPoint, Direction> ports;

    private int                           snakeSize        = 3;
    private Direction                     snakeDirection   = Direction.RIGHT;
    private TextPoint                     snakeHead        = new TextPoint(1, 4);
    private int                           numOfStars       = 3;
    private TextPoint                     p1               = new TextPoint(1, 1);
    private TextPoint                     p2               = new TextPoint(1, 5);
    private Direction                     d1               = Direction.RIGHT;
    private Direction                     d2               = Direction.DOWN;
    private String                        gameConfigSingle = "src/test/resources/test_single.config";

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
    public void testInitAllSingle() throws OutOfFieldException, IOException, SnakeOnWallException,
            TeleportInitException, FieldInitException, SnakeCollisionException {
        game.initField(testField);
        game.initSnake(1, snakeHead, snakeDirection, snakeSize);
        ports.put(p1, d1);
        ports.put(p2, d2);
        game.initPorts(ports);
        game.addStars(numOfStars);
        assertNotNull("Check that GameController Field is initialized.", game.field);
        assertNotNull("Check that Snake1 is initialized.", game.snake[0]);
        assertNull("Check that Snake2 is not initialized.", game.snake[1]);
        assertEquals("Check number of stars", 3, game.stars.getNumOfStars());
    }

    @Test
    public void testInitSingleFromConfi() throws OutOfFieldException, IOException,
            SnakeOnWallException, TeleportInitException, FieldInitException,
            SnakeCollisionException {
        game.initFromConfig(gameConfigSingle);

        assertNotNull("Check that GameController Field is initialized.", game.field);
        assertNotNull("Check that Snake1 is initialized.", game.snake[0]);
        assertNull("Check that Snake2 is not initialized.", game.snake[1]);
        assertEquals("Check number of stars", 3, game.stars.getNumOfStars());
    }

    @Test
    public void testInitAllMulti() throws OutOfFieldException, IOException, SnakeOnWallException,
            TeleportInitException, FieldInitException, SnakeCollisionException {
        game.initField(testField);
        game.setType(GameType.MULTI);
        game.initSnake(1, snakeHead, snakeDirection, snakeSize);
        game.initSnake(2, new TextPoint(2, 2), snakeDirection, 2);
        ports.put(p1, d1);
        ports.put(p2, d2);
        game.initPorts(ports);
        game.addStars(numOfStars);
        assertNotNull("Check that GameController Field is initialized.", game.field);
        assertNotNull("Check that Snake1 is initialized.", game.snake[0]);
        assertNotNull("Check that Snake2 is initialized.", game.snake[1]);
    }

    @Test(expected = SnakeCollisionException.class)
    public void testInitAllMulti_SnakeCollision() throws OutOfFieldException, IOException,
            SnakeOnWallException, TeleportInitException, FieldInitException,
            SnakeCollisionException {
        game.initField(testField);
        game.setType(GameType.MULTI);
        game.initSnake(1, snakeHead, snakeDirection, snakeSize);
        game.initSnake(2, new TextPoint(1, 2), snakeDirection, 2);
    }

    @Test(expected = SnakeOnWallException.class)
    public void testInitData_HeadOnAWall() throws SnakeOnWallException, OutOfFieldException,
            IOException, FieldInitException, SnakeCollisionException {
        TextPoint snakeHead = new TextPoint(1, 6);
        game.initField(testField);
        game.initSnake(1, snakeHead, snakeDirection, snakeSize);
    }

    @Test(expected = OutOfFieldException.class)
    public void testInitData_HeadOutOfField() throws OutOfFieldException, IOException,
            SnakeOnWallException, FieldInitException, SnakeCollisionException {
        TextPoint snakeHead = new TextPoint(2, 7);
        game.initField(testField);
        game.initSnake(1, snakeHead, snakeDirection, snakeSize);
    }

    @Test
    public void testInit_PortOnWall() throws OutOfFieldException, IOException,
            TeleportInitException, FieldInitException {
        TextPoint p1 = new TextPoint(1, 0);

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
        Direction d1 = Direction.UP;

        game.initField(testField);
        ports.put(p1, d1);
        ports.put(p2, d2);
        game.initPorts(ports);
    }

    @Test
    public void testInit_2Teleports() throws OutOfFieldException, IOException, PortAddException,
            TeleportInitException, FieldInitException {

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
            TeleportInitException, FieldInitException, SnakeCollisionException {
        game.initField(testField);
        game.setType(GameType.MULTI);
        game.initSnake(1, snakeHead, snakeDirection, snakeSize);
        game.initSnake(2, new TextPoint(2, 2), snakeDirection, 1);
        ports.put(p1, d1);
        ports.put(p2, d2);
        game.initPorts(ports);
        game.addStars(numOfStars);

        String textField = game.toString();
        TextPoint fieldSize = game.field.getFieldSize();
        int expectedLength = fieldSize.row * fieldSize.col + fieldSize.row;
        int actualLength = textField.length();
        assertEquals("Check the length of Text Field.", expectedLength, actualLength);

        assertTrue("Check that Text Field contains Snake1('**@')", textField.contains("**@"));
        assertTrue("Check that Text Field contains Snake2('0')", textField.contains("0"));

        List<TextPoint> stars = game.stars.getStars();
        for (TextPoint p : stars) {
            char star = textField.charAt(p.row * fieldSize.col + p.row + p.col);
            assertEquals("Check star.", '+', star);
        }

        assertTrue("Check that Text Field contains '0'", textField.contains("0"));
    }

    @Test
    public void testMove() throws OutOfFieldException, IOException, SnakeOnWallException,
            SnakeAddException, SnakeCollisionException, FieldInitException {
        game.initField(testField);
        game.initSnake(1, snakeHead, snakeDirection, snakeSize);

        game.move();
        TextPoint actual = game.snake[0].getHead();
        TextPoint expected = new TextPoint(snakeHead.row, snakeHead.col + 1);
        assertEquals("Check Snake Head", expected, actual);

        int expectedScore = 0;
        int actualScore = game.getScore()[0];
        assertEquals("Check Score1", expectedScore, actualScore);
        actualScore = game.getScore()[1];
        assertEquals("Check Score2", expectedScore, actualScore);
    }

    @Test(expected = SnakeOnWallException.class)
    public void testMoveOnWall() throws OutOfFieldException, IOException, SnakeOnWallException,
            SnakeAddException, SnakeCollisionException, FieldInitException {
        TextPoint snakeHead = new TextPoint(1, 5);
        game.initField(testField);
        game.initSnake(1, snakeHead, snakeDirection, snakeSize);

        game.move();
    }

    @Test(expected = SnakeCollisionException.class)
    public void testMoveOnSnake() throws OutOfFieldException, IOException, SnakeOnWallException,
            SnakeAddException, SnakeCollisionException, FieldInitException {
        game.initField(testField);
        game.setType(GameType.MULTI);
        game.initSnake(1, snakeHead, snakeDirection, snakeSize);
        game.initSnake(2, new TextPoint(2, 5), Direction.UP, 1);
        game.move();
    }

    @Test
    public void testMoveOnPort() throws OutOfFieldException, IOException, SnakeOnWallException,
            SnakeAddException, SnakeCollisionException, TeleportInitException, FieldInitException {
        game.initField(testField);
        ports.put(p1, d1);
        ports.put(p2, d2);
        game.initPorts(ports);
        game.initSnake(1, snakeHead, snakeDirection, snakeSize);

        game.move();
        TextPoint actual = game.snake[0].getHead();
        assertEquals("Check new Head position", p1, actual);

        game.move();
        actual = game.snake[0].getHead();
        TextPoint expected = p1.move(d1);
        assertEquals("Check new Head position", expected, actual);
    }

    @Test
    public void testMoveOnStar() throws OutOfFieldException, IOException, SnakeOnWallException,
            SnakeAddException, SnakeCollisionException, FieldInitException {
        int snakeSize = 4;
        int numOfStars = 5;
        game.initField(testField);
        game.initSnake(1, snakeHead, snakeDirection, snakeSize);
        game.addStars(numOfStars);

        game.move();
        TextPoint actualHead = game.snake[0].getHead();
        TextPoint expectedHead = new TextPoint(snakeHead.row, snakeHead.col + 1);
        assertEquals("Check Snake Head", expectedHead, actualHead);

        int expectedSize = 5;
        int actualSize = game.snake[0].getSize();
        assertEquals("Check Snake Size", expectedSize, actualSize);

        int expectedStars = 4;
        int actualStars = game.stars.getNumOfStars();
        assertEquals("Check Number of Stars", expectedStars, actualStars);

        int expectedScore = 1;
        int actualScore = game.getScore()[0];
        assertEquals("Check Score", expectedScore, actualScore);
    }

    @Test
    public void testGetEmptyPointOk() throws OutOfFieldException, IOException,
            SnakeOnWallException, SnakeAddException, TeleportInitException, FieldInitException,
            SnakeCollisionException {
        game.initField(testField);
        game.initSnake(1, snakeHead, snakeDirection, snakeSize);
        ports.put(p1, d1);
        ports.put(p2, d2);
        game.initPorts(ports);
        game.addStars(numOfStars);

        TextPoint p = game.getEmptyPoint();
        assertNotNull("Check that empty point is found", p);
    }

    @Test
    public void testGetEmptyPointNok() throws OutOfFieldException, IOException,
            SnakeOnWallException, SnakeAddException, TeleportInitException, FieldInitException,
            SnakeCollisionException {
        int numOfStars = 4;

        game.initField(testField);
        game.initSnake(1, snakeHead, snakeDirection, snakeSize);
        ports.put(p1, d1);
        ports.put(p2, d2);
        game.initPorts(ports);
        game.addStars(numOfStars);

        TextPoint p = game.getEmptyPoint();
        assertNull("Check that there are no empty points", p);
    }

    @Test
    public void testBattle_snakeRestartsOnWall() throws OutOfFieldException, IOException,
            SnakeOnWallException, SnakeAddException, SnakeCollisionException, FieldInitException {
        game.initField(testField);
        game.setType(GameType.MULTI_BATTLE);
        game.initSnake(1, new TextPoint(1, 5), snakeDirection, snakeSize);
        game.initSnake(2, new TextPoint(2, 5), snakeDirection, 1);
        game.snake[1].turn(Direction.NONE);
        game.move();
        int actual = game.snake[0].getSize();
        int expected = 1;
        assertEquals(expected, actual);
    }

    @Test
    public void testBattle_snakesClash() throws OutOfFieldException, IOException,
            SnakeOnWallException, SnakeAddException, SnakeCollisionException, FieldInitException {
        game.initField(testField);
        game.setType(GameType.MULTI_BATTLE);
        game.initSnake(1, snakeHead, snakeDirection, snakeSize);
        game.initSnake(2, new TextPoint(2, 4), Direction.UP, 1);
        game.move();
        int actual1 = game.snake[0].getSize();
        int expected1 = snakeSize;
        assertEquals(expected1, actual1);
        int actual2 = game.snake[1].getSize();
        int expected2 = 1;
        assertEquals(expected2, actual2);
    }

    @Test
    public void testBattle_snakesEqualsClash1() throws OutOfFieldException, IOException,
            SnakeOnWallException, SnakeAddException, SnakeCollisionException, FieldInitException {
        game.initField(testField);
        game.setType(GameType.MULTI_BATTLE);
        game.initSnake(1, new TextPoint(1, 2), Direction.RIGHT, 1);
        game.initSnake(2, new TextPoint(1, 4), Direction.LEFT, 1);

        game.move();

        int actual1 = game.snake[0].getSize();
        int expected1 = 1;
        assertEquals(expected1, actual1);
        int actual2 = game.snake[1].getSize();
        int expected2 = 1;
        assertEquals(expected2, actual2);
    }

    @Test
    public void testBattle_snakesEqualsClash2() throws OutOfFieldException, IOException,
            SnakeOnWallException, SnakeAddException, SnakeCollisionException, FieldInitException {
        game.initField(testField);
        game.setType(GameType.MULTI_BATTLE);
        game.initSnake(1, new TextPoint(1, 2), Direction.RIGHT, 2);
        game.initSnake(2, new TextPoint(1, 4), Direction.LEFT, 2);
        game.move();
        int actual1 = game.snake[0].getSize();
        int expected1 = 1;
        assertEquals(expected1, actual1);
        int actual2 = game.snake[1].getSize();
        int expected2 = 1;
        assertEquals(expected2, actual2);
    }

    @Test
    public void testBattle_snakeCollision() throws OutOfFieldException, IOException,
            SnakeOnWallException, SnakeAddException, SnakeCollisionException, FieldInitException {
        game.initField(testField);
        game.setType(GameType.MULTI_BATTLE);
        game.initSnake(1, new TextPoint(1, 5), snakeDirection, 5);
        game.initSnake(2, new TextPoint(2, 2), snakeDirection, 1);

        game.snake[1].turn(Direction.NONE);
        game.snake[0].turn(Direction.DOWN);
        game.move();
        game.snake[0].turn(Direction.LEFT);
        game.move();
        game.snake[0].turn(Direction.UP);
        game.move();
        int actual1 = game.snake[0].getSize();
        int expected1 = 1;
        assertEquals(expected1, actual1);
    }

    @Test
    public void testBattle_CatchTail() throws OutOfFieldException, IOException,
            SnakeOnWallException, SnakeAddException, SnakeCollisionException, FieldInitException {
        game.initField(testField);
        game.setType(GameType.MULTI_BATTLE);
        game.initSnake(1, snakeHead, snakeDirection, snakeSize);
        game.initSnake(2, new TextPoint(2, 2), Direction.UP, 1);

        game.snake[0].turn(Direction.NONE);
        game.move();

        int actual1 = game.snake[0].getSize();
        int expected1 = 2;
        assertEquals(expected1, actual1);
        int actual2 = game.snake[1].getSize();
        int expected2 = 2;
        assertEquals(expected2, actual2);
    }

    @Test
    public void testBattle_CatchTail2() throws OutOfFieldException, IOException,
            SnakeOnWallException, SnakeAddException, SnakeCollisionException, FieldInitException {
        game.initField(testField);
        game.setType(GameType.MULTI_BATTLE);
        game.initSnake(1, new TextPoint(1, 3), snakeDirection, snakeSize);
        game.initSnake(2, new TextPoint(2, 2), Direction.UP, 1);

        game.move();

        int actual1 = game.snake[0].getSize();
        int expected1 = 2;
        assertEquals(expected1, actual1);
        int actual2 = game.snake[1].getSize();
        int expected2 = 2;
        assertEquals(expected2, actual2);
    }

    @Test
    public void testBattle_CatchTail3() throws OutOfFieldException, IOException,
            SnakeOnWallException, SnakeAddException, SnakeCollisionException, FieldInitException {
        game.initField(testField);
        game.setType(GameType.MULTI_BATTLE);
        game.initSnake(1, new TextPoint(1, 3), Direction.RIGHT, 2);
        game.initSnake(2, new TextPoint(1, 5), Direction.LEFT, 1);

        game.move();

        int actual1 = game.snake[0].getSize();
        int expected1 = 3;
        assertEquals(expected1, actual1);
        int actual2 = game.snake[1].getSize();
        int expected2 = 1;
        assertEquals(expected2, actual2);
    }
}
