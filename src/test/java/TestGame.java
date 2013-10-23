import static org.junit.Assert.*;

import java.io.IOException;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestGame {
    private String testField = "src/test/resources/TestField.txt";

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testInitData() throws OutOfFieldException, IOException,
            GameSnakeOnWallException {
        int snakeSize = 3;
        Snake.Direction snakeDirection = Snake.Direction.RIGHT;
        TextPoint snakeHead = new TextPoint(1, 4);
        int numOfStars = 3;
        Game.initData(testField, snakeHead, snakeDirection, snakeSize,
                numOfStars);
        assertNotNull("Check that Game Field created.", Game.field);
        assertNotNull("Check that Snake created.", Game.snake);
        assertNotNull("Check that Stars created.", Game.stars);
    }

    @Test(expected = GameSnakeOnWallException.class)
    public void testInitData_HeadOnAWall() throws GameSnakeOnWallException,
            OutOfFieldException, IOException {
        int snakeSize = 3;
        Snake.Direction snakeDirection = Snake.Direction.RIGHT;
        TextPoint snakeHead = new TextPoint(1, 6);
        int numOfStars = 3;
        Game.initData(testField, snakeHead, snakeDirection, snakeSize,
                numOfStars);
    }

    @Test(expected = OutOfFieldException.class)
    public void testInitData_HeadOutOfField() throws OutOfFieldException,
            IOException, GameSnakeOnWallException {
        int snakeSize = 3;
        Snake.Direction snakeDirection = Snake.Direction.RIGHT;
        TextPoint snakeHead = new TextPoint(2, 7);
        int numOfStars = 3;
        Game.initData(testField, snakeHead, snakeDirection, snakeSize,
                numOfStars);
    }

    @Test
    public void testToText() throws OutOfFieldException, IOException,
            GameSnakeOnWallException {
        int snakeSize = 3;
        Snake.Direction snakeDirection = Snake.Direction.RIGHT;
        TextPoint snakeHead = new TextPoint(1, 4);
        int numOfStars = 3;
        Game.initData(testField, snakeHead, snakeDirection, snakeSize,
                numOfStars);

        String textField = Game.toText();
        int expectedLength = Game.field.getRowsNum() * Game.field.getColsNum()
                + Game.field.getRowsNum();
        int actualLength = textField.length();
        assertEquals("Check the length of Text Field.", expectedLength,
                actualLength);

        assertTrue("Check that Text Field contains '**@'",
                textField.contains("**@"));

        List<TextPoint> stars = Game.stars.getStars();
        for (TextPoint p : stars) {
            char star = textField.charAt(p.row * Game.field.getColsNum()
                    + p.row + p.col);
            assertEquals("Check star.", '+', star);
        }
    }

    @Test
    public void testMove() throws OutOfFieldException, IOException,
            GameSnakeOnWallException, SnakeAddException, SnakeCollision {
        int snakeSize = 3;
        Snake.Direction snakeDirection = Snake.Direction.RIGHT;
        TextPoint snakeHead = new TextPoint(1, 4);
        int numOfStars = 3;
        Game.initData(testField, snakeHead, snakeDirection, snakeSize,
                numOfStars);

        Game.move();
        TextPoint actual = Game.snake.getHead();
        TextPoint expected = new TextPoint(snakeHead.row, snakeHead.col + 1);
        assertEquals("Check Snake Head", expected, actual);
    }

    @Test(expected = GameSnakeOnWallException.class)
    public void testMoveOnWall() throws OutOfFieldException, IOException,
            GameSnakeOnWallException, SnakeAddException, SnakeCollision {
        int snakeSize = 3;
        Snake.Direction snakeDirection = Snake.Direction.RIGHT;
        TextPoint snakeHead = new TextPoint(1, 5);
        int numOfStars = 3;
        Game.initData(testField, snakeHead, snakeDirection, snakeSize,
                numOfStars);

        Game.move();
    }

    @Test
    public void testMoveOnStar() throws OutOfFieldException, IOException,
            GameSnakeOnWallException, SnakeAddException, SnakeCollision {
        int snakeSize = 4;
        Snake.Direction snakeDirection = Snake.Direction.RIGHT;
        TextPoint snakeHead = new TextPoint(1, 4);
        int numOfStars = 5;
        Game.initData(testField, snakeHead, snakeDirection, snakeSize,
                numOfStars);

        Game.move();
        TextPoint actualHead = Game.snake.getHead();
        TextPoint expectedHead = new TextPoint(snakeHead.row, snakeHead.col + 1);
        assertEquals("Check Snake Head", expectedHead, actualHead);
        int expectedSize = 5;
        int actualSize = Game.snake.getSize();
        assertEquals("Check Snake Size", expectedSize, actualSize);
    }

    @Test
    public void testNewStar() throws OutOfFieldException, IOException,
            GameSnakeOnWallException, SnakeAddException {
        int snakeSize = 4;
        Snake.Direction snakeDirection = Snake.Direction.RIGHT;
        TextPoint snakeHead = new TextPoint(1, 4);
        int numOfStars = 4;
        Game.initData(testField, snakeHead, snakeDirection, snakeSize,
                numOfStars);

        TextPoint star = Game.newStar();
        assertNotNull("Check that new Star is created", star);
    }

    @Test
    public void testNewStar_NoSpace() throws OutOfFieldException, IOException,
            GameSnakeOnWallException, SnakeAddException {
        int snakeSize = 4;
        Snake.Direction snakeDirection = Snake.Direction.RIGHT;
        TextPoint snakeHead = new TextPoint(1, 4);
        int numOfStars = 5;
        Game.initData(testField, snakeHead, snakeDirection, snakeSize,
                numOfStars);

        TextPoint star = Game.newStar();
        assertNull("Check that new Star is not created", star);
    }
}
