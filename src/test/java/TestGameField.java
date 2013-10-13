import static org.junit.Assert.*;

import java.awt.Point;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class TestGameField {
    private GameField field;
    private String testField = "src/test/resources/TestField.txt";
	
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
		Point p = new Point(0,0);
	    boolean expected = true;
		boolean actual = field.isWall(p);
		assertEquals("Check that point is on a wall.", expected, actual);
	}
	
    @Test
    public void testIsWallFalse() {
        Point p = new Point(1, 1);
        boolean expected = false;
        boolean actual = field.isWall(p);
        assertEquals("Check that point is not on a wall.", expected, actual);
    }
    
    @Test (expected = ArrayIndexOutOfBoundsException.class)
    public void testIsWallOutOfBoundary() {
        Point p = new Point(25, 1);
        field.isWall(p);
    }

}
