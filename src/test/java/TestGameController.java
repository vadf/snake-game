import static org.junit.Assert.*;

import java.io.IOException;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestGameController {
    private String testField = "src/test/resources/TestField.txt";

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testInitData() throws OutOfFieldException, IOException,
            SnakeOnWallException {
        int snakeSize = 3;
        Snake.Direction snakeDirection = Snake.Direction.RIGHT;
        TextPoint snakeHead = new TextPoint(1, 4);
        int numOfStars = 3;
        GameController.initData(testField, snakeHead, snakeDirection,
                snakeSize, numOfStars);
        assertNotNull("Check that GameController Field created.",
                GameController.field);
        assertNotNull("Check that Snake created.", GameController.snake);
        assertNotNull("Check that Stars created.", GameController.stars);
    }

    @Test(expected = SnakeOnWallException.class)
    public void testInitData_HeadOnAWall() throws SnakeOnWallException,
            OutOfFieldException, IOException {
        int snakeSize = 3;
        Snake.Direction snakeDirection = Snake.Direction.RIGHT;
        TextPoint snakeHead = new TextPoint(1, 6);
        int numOfStars = 3;
        GameController.initData(testField, snakeHead, snakeDirection,
                snakeSize, numOfStars);
    }

    @Test(expected = OutOfFieldException.class)
    public void testInitData_HeadOutOfField() throws OutOfFieldException,
            IOException, SnakeOnWallException {
        int snakeSize = 3;
        Snake.Direction snakeDirection = Snake.Direction.RIGHT;
        TextPoint snakeHead = new TextPoint(2, 7);
        int numOfStars = 3;
        GameController.initData(testField, snakeHead, snakeDirection,
                snakeSize, numOfStars);
    }

    @Test
    public void testToText() throws OutOfFieldException, IOException,
            SnakeOnWallException {
        int snakeSize = 3;
        Snake.Direction snakeDirection = Snake.Direction.RIGHT;
        TextPoint snakeHead = new TextPoint(1, 4);
        int numOfStars = 3;
        GameController.initData(testField, snakeHead, snakeDirection,
                snakeSize, numOfStars);

        String textField = GameController.toText();
        int expectedLength = GameController.field.getRowsNum()
                * GameController.field.getColsNum()
                + GameController.field.getRowsNum();
        int actualLength = textField.length();
        assertEquals("Check the length of Text Field.", expectedLength,
                actualLength);

        assertTrue("Check that Text Field contains '**@'",
                textField.contains("**@"));

        List<TextPoint> stars = GameController.stars.getStars();
        for (TextPoint p : stars) {
            char star = textField.charAt(p.row
                    * GameController.field.getColsNum() + p.row + p.col);
            assertEquals("Check star.", '+', star);
        }
    }

    @Test
    public void testMove() throws OutOfFieldException, IOException,
            SnakeOnWallException, SnakeAddException, SnakeCollision {
        int snakeSize = 3;
        Snake.Direction snakeDirection = Snake.Direction.RIGHT;
        TextPoint snakeHead = new TextPoint(1, 4);
        int numOfStars = 3;
        GameController.initData(testField, snakeHead, snakeDirection,
                snakeSize, numOfStars);

        GameController.move();
        TextPoint actual = GameController.snake.getHead();
        TextPoint expected = new TextPoint(snakeHead.row, snakeHead.col + 1);
        assertEquals("Check Snake Head", expected, actual);
    }

    @Test(expected = SnakeOnWallException.class)
    public void testMoveOnWall() throws OutOfFieldException, IOException,
            SnakeOnWallException, SnakeAddException, SnakeCollision {
        int snakeSize = 3;
        Snake.Direction snakeDirection = Snake.Direction.RIGHT;
        TextPoint snakeHead = new TextPoint(1, 5);
        int numOfStars = 3;
        GameController.initData(testField, snakeHead, snakeDirection,
                snakeSize, numOfStars);

        GameController.move();
    }

    @Test
    public void testMoveOnStar() throws OutOfFieldException, IOException,
            SnakeOnWallException, SnakeAddException, SnakeCollision {
        int snakeSize = 4;
        Snake.Direction snakeDirection = Snake.Direction.RIGHT;
        TextPoint snakeHead = new TextPoint(1, 4);
        int numOfStars = 5;
        GameController.initData(testField, snakeHead, snakeDirection,
                snakeSize, numOfStars);

        GameController.move();
        TextPoint actualHead = GameController.snake.getHead();
        TextPoint expectedHead = new TextPoint(snakeHead.row, snakeHead.col + 1);
        assertEquals("Check Snake Head", expectedHead, actualHead);

        int expectedSize = 5;
        int actualSize = GameController.snake.getSize();
        assertEquals("Check Snake Size", expectedSize, actualSize);

        int expectedStars = 4;
        int actualStars = GameController.stars.getNumOfStars();
        assertEquals("Check Number of Stars", expectedStars, actualStars);
    }

    @Test
    public void testNewStar() throws OutOfFieldException, IOException,
            SnakeOnWallException, SnakeAddException {
        int snakeSize = 4;
        Snake.Direction snakeDirection = Snake.Direction.RIGHT;
        TextPoint snakeHead = new TextPoint(1, 4);
        int numOfStars = 4;
        GameController.initData(testField, snakeHead, snakeDirection,
                snakeSize, numOfStars);

        TextPoint star = GameController.newStar();
        assertNotNull("Check that new Star is created", star);
    }

    @Test
    public void testNewStar_NoSpace() throws OutOfFieldException, IOException,
            SnakeOnWallException, SnakeAddException {
        int snakeSize = 4;
        Snake.Direction snakeDirection = Snake.Direction.RIGHT;
        TextPoint snakeHead = new TextPoint(1, 4);
        int numOfStars = 5;
        GameController.initData(testField, snakeHead, snakeDirection,
                snakeSize, numOfStars);

        TextPoint star = GameController.newStar();
        assertNull("Check that new Star is not created", star);
    }
}
