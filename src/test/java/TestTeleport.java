import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestTeleport {
    private Teleport                      port;
    private HashMap<TextPoint, Direction> testPorts;
    private TextPoint                     p1    = new TextPoint(1, 1);
    private TextPoint                     p2    = new TextPoint(p1.row * 3, p1.col * 4);
    private TextPoint                     p3    = new TextPoint(p1.row * 3, p1.col + 1);
    private Direction                     right = Direction.RIGHT;
    private Direction                     left  = Direction.LEFT;
    private Direction                     down  = Direction.DOWN;
    private Direction                     up    = Direction.UP;

    @Before
    public void setUp() throws Exception {
        testPorts = new HashMap<TextPoint, Direction>();
        testPorts.put(p1, right);
        testPorts.put(p2, down);
        port = new Teleport(testPorts);
    }

    @After
    public void tearDown() throws Exception {
        port = null;
        testPorts = null;
    }

    @Test(expected = TeleportInitException.class)
    public void testInitFailed() throws TeleportInitException {
        new Teleport(p1, right, new TextPoint(p1), up);
    }

    @Test(expected = TeleportInitException.class)
    public void testInitFailed2() throws TeleportInitException {
        testPorts = new HashMap<TextPoint, Direction>();
        testPorts.put(p1, right);
        testPorts.put(new TextPoint(p1), up);
        new Teleport(testPorts);
    }

    @Test
    public void testInitTeleport() throws TeleportInitException {
        new Teleport(p1, right, p2, up);
        int actual = port.getNumOfPorts();
        int expected = 2;
        assertEquals("Check number of ports after init.", expected, actual);
    }

    @Test
    public void testInitTeleport2() throws TeleportInitException {
        testPorts.put(p3, up);
        port = new Teleport(testPorts);
        int actual = port.getNumOfPorts();
        int expected = 3;
        assertEquals("Check number of ports after init.", expected, actual);
    }

    @Test
    public void testAddPortOk() {
        assertTrue("Check that new port is added", port.add(p3, right));

        int expected = 3;
        int actual = port.getNumOfPorts();
        assertEquals("Check number of ports", expected, actual);
    }

    @Test
    public void testAddPortNok() {
        assertFalse("Check that new port is not added", port.add(new TextPoint(p1), left));

        int expected = 2;
        int actual = port.getNumOfPorts();
        assertEquals("Check number of ports", expected, actual);
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
        Snake snake = new Snake(p2, right, 3, new TextPoint(100, 100));
        port.updateHead(snake);
        TextPoint actual = snake.getHead();
        assertEquals("Check new Snake Head.", p1, actual);
    }

    @Test
    public void testUpdateHead_severalPorts() {
        port.add(p3, left);
        Snake snake = new Snake(p2, left, 3, new TextPoint(100, 100));
        port.updateHead(snake);
        TextPoint actual = snake.getHead();
        assertNotEquals("Check that Snake Head is changed.", p2, actual);
    }

    @Test
    public void testUpdateHead_headNotOnPort() {
        Snake snake = new Snake(p3, right, 3, new TextPoint(100, 100));
        port.updateHead(snake);
        TextPoint actual = snake.getHead();
        assertEquals("Check new Snake Head.", p3, actual);
    }
}
