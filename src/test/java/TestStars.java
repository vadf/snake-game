import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestStars {
    private Stars stars;

    @Before
    public void setUp() throws Exception {
        stars = new Stars();
    }

    @After
    public void tearDown() throws Exception {
        stars = null;
    }

    @Test
    public void testAdd() {
        TextPoint point = new TextPoint(1, 1);
        boolean actual = stars.add(point);
        assertTrue("Check that new Star is added.", actual);

    }

    @Test
    public void testAddTwice() {
        TextPoint point1 = new TextPoint(1, 1);
        stars.add(point1);
        TextPoint point2 = new TextPoint(1, 1);
        boolean actual = stars.add(point2);
        assertFalse(
                "Check that Star is not added, if such Star already exists.",
                actual);

        int numExpected = 1;
        int numActual = stars.getNumOfStars();
        assertEquals("Check that number of Stars is correct", numExpected,
                numActual);
    }

    @Test
    public void testRemoveTrue() {
        TextPoint point = new TextPoint(1, 1);
        stars.add(point);
        boolean actual = stars.remove(point);
        assertTrue("Check that Star is removed.", actual);
    }

    @Test
    public void testRemoveFalse() {
        TextPoint point = new TextPoint(1, 1);
        stars.remove(point);
        boolean actual = stars.remove(point);
        assertFalse("Check that False is returned if there is no such Star.",
                actual);
    }

    @Test
    public void testRemoveTwice() {
        TextPoint point = new TextPoint(2, 2);
        stars.add(point);

        TextPoint point1 = new TextPoint(1, 1);
        stars.add(point1);
        stars.remove(point1);
        TextPoint point2 = new TextPoint(1, 1);
        boolean actual = stars.remove(point2);
        assertFalse("Check that False is returned if there is no such Star.",
                actual);

        int numExpected = 1;
        int numActual = stars.getNumOfStars();
        assertEquals("Check that number of Stars is correct", numExpected,
                numActual);
    }

    @Test
    public void testGetNumOfStars() {
        int expected = 0;
        int actual = stars.getNumOfStars();
        assertEquals("Check number of Stars", expected, actual);

        TextPoint point1 = new TextPoint(1, 1);
        stars.add(point1);
        TextPoint point2 = new TextPoint(1, 3);
        stars.add(point2);
        expected = 2;
        actual = stars.getNumOfStars();
        assertEquals("Check number of Stars", expected, actual);

        stars.remove(point2);
        expected = 1;
        actual = stars.getNumOfStars();
        assertEquals("Check number of Stars", expected, actual);
    }

    @Test
    public void testIsStarTrue() {
        TextPoint point = new TextPoint(1, 1);
        stars.add(point);
        assertTrue("Check is such Star exists.", stars.isStar(point));

        TextPoint point2 = new TextPoint(1, 1);
        assertTrue("Check is such Star exists.", stars.isStar(point2));
    }

    @Test
    public void testIsStarFalse() {
        TextPoint point = new TextPoint(1, 1);
        stars.add(point);
        TextPoint point2 = new TextPoint(1, 2);
        assertFalse("Check is such Star exists.", stars.isStar(point2));
    }
}
