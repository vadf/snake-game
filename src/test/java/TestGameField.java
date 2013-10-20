import static org.junit.Assert.*;

import java.util.List;

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
    public void testIsWallTrue() throws Exception {
        TextPoint p = new TextPoint(3, 6);
        boolean actual = field.isWall(p);
        assertTrue("Check that point is on a wall.", actual);
    }

    @Test
    public void testIsWallFalse() throws Exception {
        TextPoint p = new TextPoint(2, 5);
        boolean actual = field.isWall(p);
        assertFalse("Check that point is not on a wall.", actual);
    }

    @Test(expected = Exception.class)
    public void testIsWallOutOfBoundary() throws Exception {
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
    public void testGetWalls() {
        int actual = 19;
        List<TextPoint> walls = field.getWalls();
        int expected = walls.size();
        assertEquals("Check number of walls.", expected, actual);
    }

    @Test
    public void testGetRowsNum() {
        int expected = 4;
        int actual = field.getRowsNum();
        assertEquals("Check number of rows.", expected, actual);
    }

    @Test
    public void testGetColsNum() {
        int expected = 7;
        int actual = field.getColsNum();
        assertEquals("Check number of columns.", expected, actual);
    }

    @Test
    public void testIsInFieldTrue() {
        TextPoint p = new TextPoint(0, 6);
        boolean actual = field.isInField(p);
        assertTrue("Check number of columns.", actual);
    }

    @Test
    public void testIsInFieldFalse() {
        TextPoint p = new TextPoint(4, 0);
        boolean actual = field.isInField(p);
        assertFalse("Check number of columns.", actual);
    }
}
