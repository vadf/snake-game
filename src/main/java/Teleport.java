import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class Teleport {
    private HashMap<TextPoint, Direction> ports = new HashMap<TextPoint, Direction>();

    public Teleport(TextPoint p1, Direction d1, TextPoint p2, Direction d2)
            throws TeleportInitException {
        if (p1.equals(p2)) {
            throw new TeleportInitException("Initial Teleport points are equal.");
        }
        ports.put(p1, d1);
        ports.put(p2, d2);
    }

    public Teleport(HashMap<TextPoint, Direction> ports) throws TeleportInitException {
        if (ports.size() < 2) {
            throw new TeleportInitException("There are should be at least two ports for init.");
        }
        this.ports = new HashMap<TextPoint, Direction>(ports);
    }

    public boolean add(TextPoint port, Direction d) {
        if (ports.containsKey(port)) {
            return false;
        }
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
        ArrayList<TextPoint> keys = new ArrayList<TextPoint>(ports.keySet());
        keys.remove(head);
        TextPoint newHead = keys.get(r.nextInt(keys.size()));

        snake.changeHeadPosition(newHead, ports.get(newHead));
    }

    @Override
    public String toString() {
        return ports.toString();
    }

    protected List<TextPoint> getPorts() {
        return new ArrayList<TextPoint>(ports.keySet());
    }

    public int getNumOfPorts() {
        return ports.keySet().size();
    }
}

class TeleportInitException extends Exception {
    /**
     * 
     */
    private static final long serialVersionUID = 9017525059536876319L;

    public TeleportInitException() {
        super();
    }

    public TeleportInitException(String message) {
        super(message);
    }

}