import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestGameField {
    private GameField field;
    private String    testField = "src/test/resources/TestField.txt";

    @Before
    public void setUp() throws Exception {
        field = new GameField(testField);
    }

    @After
    public void tearDown() throws Exception {
        field = null;
    }

    @Test
    public void testIsWallTrue() {
        TextPoint p = new TextPoint(3, 6);
        boolean expected = true;
        boolean actual = field.isWall(p);
        assertEquals("Check that point is on a wall.", expected, actual);
    }

    @Test
    public void testIsWallFalse() {
        TextPoint p = new TextPoint(2, 5);
        boolean expected = false;
        boolean actual = field.isWall(p);
        assertEquals("Check that point is not on a wall.", expected, actual);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testIsWallOutOfBoundary() {
        TextPoint p = new TextPoint(4, 1);
        field.isWall(p);
    }

    @Test
    public void testGetEffectiveSize() {
        int expected = 9;
        int actual = field.getEffectiveSize();
        assertEquals("Check that field effective size is correct.", expected,
                actual);
    }

    @Test
    public void testGetField() {
        char[][] expected = { { '#', '#', '#', '#', '#', '#', '#' },
                { '#', ' ', ' ', ' ', ' ', ' ', '#' },
                { '#', ' ', ' ', '#', ' ', ' ', '#' },
                { '#', '#', '#', '#', '#', '#', '#' } };
        char[][] actual = field.getField();
        for (int i = 0; i < actual.length; i++) {
            assertArrayEquals("Check that field line " + i + " is correct.",
                    expected[i], actual[i]);
        }
    }
}
