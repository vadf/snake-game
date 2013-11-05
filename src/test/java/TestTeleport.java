import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestTeleport {
    private Teleport  port;
    private TextPoint p1    = new TextPoint(1, 1);
    private TextPoint p2    = new TextPoint(p1.row * 3, p1.col * 4);
    private TextPoint p3    = new TextPoint(p1.row * 3, p1.col + 1);
    private Direction right = Direction.RIGHT;
    private Direction left  = Direction.LEFT;
    private Direction down  = Direction.DOWN;
    private Direction up    = Direction.UP;

    @Before
    public void setUp() throws Exception {
        port = new Teleport(p1, right, p2, down);
    }

    @After
    public void tearDown() throws Exception {
        port = null;
    }

    @Test(expected = TeleportInitFailed.class)
    public void testTeleport() throws TeleportInitFailed {
        new Teleport(p1, right, new TextPoint(p1), up);
    }

    @Test
    public void testAddPortOk() {
        assertTrue("Check that new port is added", port.add(p3, right));
    }

    @Test
    public void testAddPortNok() {
        assertFalse("Check that new port is not added",
                port.add(new TextPoint(p1), left));
    }

    @Test
    public void testIsPortTrue() {
        assertTrue("Check that Point is Port", port.isPort(new TextPoint(p1)));
    }

    @Test
    public void testIsPortFalse() {
        assertFalse("Check that Point is not Port", port.isPort(p3));
    }

    @Test
    public void testUpdateHead() {
        Snake snake = new Snake(p2, right, 3);
        port.updateHead(snake);
        TextPoint actual = snake.getHead();
        assertEquals("Check new Snake Head.", p1, actual);
    }

    @Test
    public void testUpdateHead_severalPorts() {
        port.add(p3, left);
        Snake snake = new Snake(p2, left, 3);
        port.updateHead(snake);
        TextPoint actual = snake.getHead();
        assertNotEquals("Check that Snake Head is changed.", p2, actual);
    }

    @Test
    public void testUpdateHead_headNotOnPort() {
        Snake snake = new Snake(p3, right, 3);
        port.updateHead(snake);
        TextPoint actual = snake.getHead();
        assertEquals("Check new Snake Head.", p3, actual);
    }
}
