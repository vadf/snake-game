import static org.junit.Assert.*;

import java.io.IOException;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestGameField {
    private GameField field;
    private String    testField    = "src/test/resources/TestField.txt";
    private String    testFieldNok = "src/test/resources/TestFieldNok.txt";

    @Before
    public void setUp() throws Exception {
        field = new GameField(testField);
    }

    @After
    public void tearDown() throws Exception {
        field = null;
    }

    @Test(expected = FieldInitException.class)
    public void testInitNok() throws IOException, FieldInitException {
        field = new GameField(testFieldNok);
    }

    @Test
    public void testIsWallTrue() throws OutOfFieldException {
        TextPoint p = new TextPoint(3, 6);
        boolean actual = field.isWall(p);
        assertTrue("Check that point is on a wall.", actual);
    }

    @Test
    public void testIsWallFalse() throws OutOfFieldException {
        TextPoint p = new TextPoint(2, 5);
        boolean actual = field.isWall(p);
        assertFalse("Check that point is not on a wall.", actual);
    }

    @Test(expected = OutOfFieldException.class)
    public void testIsWallOutOfBoundary() throws OutOfFieldException {
        TextPoint p = new TextPoint(4, 1);
        field.isWall(p);
    }

    @Test
    public void testGetEffectiveSize() {
        int expected = 9;
        int actual = field.getEffectiveSize();
        assertEquals("Check that field effective size is correct.", expected, actual);
    }

    @Test
    public void testGetWalls() {
        int expected = 19;
        List<TextPoint> walls = field.getWalls();
        int actual = walls.size();
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

    @Test
    public void testAddWallOk() throws OutOfFieldException {
        TextPoint p = new TextPoint(1, 1);
        boolean result = field.addWall(p);
        assertTrue("Check that new wall is added.", result);
        int expected = 20;
        int actual = field.getWalls().size();
        assertEquals("Check number of walls.", expected, actual);
    }

    @Test
    public void testAddWallNok() throws OutOfFieldException {
        TextPoint p = new TextPoint(0, 0);
        boolean result = field.addWall(p);
        assertFalse("Check that new wall is not added.", result);
        int expected = 19;
        int actual = field.getWalls().size();
        assertEquals("Check number of walls.", expected, actual);
    }

    @Test(expected = OutOfFieldException.class)
    public void testAddWallException() throws OutOfFieldException {
        TextPoint p = new TextPoint(-1, 0);
        field.addWall(p);
    }

    @Test
    public void testRemoveWallOk() throws OutOfFieldException {
        TextPoint p = new TextPoint(0, 2);
        boolean result = field.removeWall(p);
        assertTrue("Check that new wall is added.", result);
        int expected = 18;
        int actual = field.getWalls().size();
        assertEquals("Check number of walls.", expected, actual);
    }

    @Test
    public void testRemoveWallNok() throws OutOfFieldException {
        TextPoint p = new TextPoint(2, 2);
        boolean result = field.removeWall(p);
        assertFalse("Check that new wall is not added.", result);
        int expected = 19;
        int actual = field.getWalls().size();
        assertEquals("Check number of walls.", expected, actual);
    }

    @Test(expected = OutOfFieldException.class)
    public void testRemoveWallException() throws OutOfFieldException {
        TextPoint p = new TextPoint(-1, 0);
        field.removeWall(p);
    }
}
