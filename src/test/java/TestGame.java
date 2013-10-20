import static org.junit.Assert.*;

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
    public void testInitData() throws Exception {
        int snakeSize = 3;
        Snake.Direction snakeDirection = Snake.Direction.RIGHT;
        TextPoint snakeHead = new TextPoint(1, 5);
        int numOfStars = 3;
        Game.InitData(testField, snakeHead, snakeDirection, snakeSize,
                numOfStars);
        assertNotNull("Check that Game Field created.", Game.field);
        assertNotNull("Check that Snake created.", Game.snake);
        assertNotNull("Check that Stars created.", Game.stars);
    }

    @Test(expected = Exception.class)
    public void testInitData_HeadOnAWall() throws Exception {
        int snakeSize = 3;
        Snake.Direction snakeDirection = Snake.Direction.RIGHT;
        TextPoint snakeHead = new TextPoint(1, 7);
        int numOfStars = 3;
        Game.InitData(testField, snakeHead, snakeDirection, snakeSize,
                numOfStars);
    }

    @Test(expected = Exception.class)
    public void testInitData_HeadOutOfField() throws Exception {
        int snakeSize = 3;
        Snake.Direction snakeDirection = Snake.Direction.RIGHT;
        TextPoint snakeHead = new TextPoint(2, 8);
        int numOfStars = 3;
        Game.InitData(testField, snakeHead, snakeDirection, snakeSize,
                numOfStars);
    }

    @Test
    public void testToText() throws Exception {
        int snakeSize = 3;
        Snake.Direction snakeDirection = Snake.Direction.RIGHT;
        TextPoint snakeHead = new TextPoint(1, 5);
        int numOfStars = 3;
        Game.InitData(testField, snakeHead, snakeDirection, snakeSize,
                numOfStars);

        String textField = Game.toText();
        System.out.println(textField);
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

}
