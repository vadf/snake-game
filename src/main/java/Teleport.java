import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;

public class Teleport {
    private HashMap<TextPoint, Direction> ports = new HashMap<TextPoint, Direction>();

    public Teleport(TextPoint p1, Direction d1, TextPoint p2, Direction d2)
            throws TeleportInitFailed {
        if (p1.equals(p2))
            throw new TeleportInitFailed("Initial Teleport points are equal.");
        ports.put(p1, d1);
        ports.put(p2, d2);
    }

    public boolean add(TextPoint port, Direction d) {
        if (ports.containsKey(port))
            return false;
        ports.put(port, d);
        return true;
    }

    public boolean isPort(TextPoint port) {
        return ports.containsKey(port);
    }

    public void updateHead(HeadMove snake) {
        TextPoint head = snake.getHead();
        if (!ports.containsKey(head)) {
            System.err.println("Try to port head, while it is not in port!");
            System.err.println("Ports: " + ports + ", head " + head);
            return;
        }

        Random r = new Random(System.currentTimeMillis());
        Set<TextPoint> keySet = ports.keySet();
        keySet.remove(head);
        ArrayList<TextPoint> keys = new ArrayList<TextPoint>(keySet);
        TextPoint newHead = keys.get(r.nextInt(keys.size()));

        snake.changeHeadPosition(newHead, ports.get(newHead));
    }

    @Override
    public String toString() {
        return ports.toString();
    }
}

class TeleportInitFailed extends Exception {
    /**
     * 
     */
    private static final long serialVersionUID = 9017525059536876319L;

    public TeleportInitFailed() {
        super();
    }

    public TeleportInitFailed(String message) {
        super(message);
    }

}