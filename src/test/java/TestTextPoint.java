import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestTextPoint {
    private int       row = 2;
    private int       col = 4;
    private TextPoint point;

    @Before
    public void setUp() throws Exception {
        point = new TextPoint(row, col);
    }

    @After
    public void tearDown() throws Exception {
        point = null;
    }

    @Test
    public void testTextPoint() {
        assertEquals("Check TextPoint row value.", row, point.row);
        assertEquals("Check TextPoint col value.", col, point.col);
    }

    @Test
    public void testTextPointCopy() {
        TextPoint test = new TextPoint(point);
        assertEquals("Check TextPoint row value.", row, test.row);
        assertEquals("Check TextPoint col value.", col, test.col);
    }

    @Test
    public void testEqualsTrue() {
        TextPoint test = new TextPoint(row, col);
        assertTrue("Check TextPoints are equal.", point.equals(test));
    }

    @Test
    public void testEqualsFalse() {
        TextPoint test = new TextPoint(row, col - 1);
        assertNotEquals("Check TextPoint are not equal.", test, point);
    }
    
    @Test
    public void testMoveRight() {        
        TextPoint actual = point.move(Direction.RIGHT);
        TextPoint expected = new TextPoint(point.row, point.col + 1);
        assertEquals("Check TextPoint col value.", expected, actual);
    }
    
    @Test
    public void testMoveLeft() {        
        TextPoint actual = point.move(Direction.LEFT);
        TextPoint expected = new TextPoint(point.row, point.col - 1);
        assertEquals("Check TextPoint col value.", expected, actual);
    }
    
    @Test
    public void testMoveUp() {        
        TextPoint actual = point.move(Direction.UP);
        TextPoint expected = new TextPoint(point.row - 1, point.col);
        assertEquals("Check TextPoint col value.", expected, actual);
    }
    
    @Test
    public void testMoveDown() {        
        TextPoint actual = point.move(Direction.DOWN);
        TextPoint expected = new TextPoint(point.row + 1, point.col);
        assertEquals("Check TextPoint col value.", expected, actual);
    }
}
