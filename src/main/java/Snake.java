import java.util.ArrayList;
import java.util.List;

public class Snake {
    private List<TextPoint> snake  = new ArrayList<TextPoint>();
    private int             dirCol = 0;
    private int             dirRow = 0;
    private TextPoint       tail;

    public Snake(TextPoint head, Direction direction, int snakeSize) {
        snake.add(head);
        turn(direction);
        for (int i = 1; i < snakeSize; i++) {
            TextPoint p = new TextPoint(head.row - dirRow * i, head.col
                    - dirCol * i);
            snake.add(p);
        }
    }

    public enum Direction {
        RIGHT, LEFT, UP, DOWN
    }

    public int getSize() {
        return snake.size();
    }

    public boolean isOnSnake(TextPoint p) {
        return snake.contains(p);
    }

    public void add() throws SnakeAddException {
        if (tail == null) {
            throw new SnakeAddException("Impossible to add new point ot Snake");
        }
        snake.add(tail);
        tail = null;
    }

    public void turn(Direction direction) {
        int oldCol = dirCol;
        int oldRow = dirRow;
        switch (direction) {
        case RIGHT:
            dirCol = 1;
            dirRow = 0;
            break;
        case LEFT:
            dirCol = -1;
            dirRow = 0;
            break;
        case UP:
            dirCol = 0;
            dirRow = -1;
            break;
        case DOWN:
            dirCol = 0;
            dirRow = 1;
            break;
        default:
            dirCol = 0;
            dirRow = 0;
            System.err.println("Incorrect Snake Direction was sent - "
                    + direction);
        }

        // if direction was changed to opposite (new point will be on snake)
        TextPoint head = new TextPoint(snake.get(0));
        head.row += dirRow;
        head.col += dirCol;
        if (snake.size() > 1 && snake.get(1).equals(head)) {
            // don't change direction
            dirCol = oldCol;
            dirRow = oldRow;
        }
    }

    public void move() throws SnakeCollision {
        TextPoint head = new TextPoint(snake.get(0));
        head.row += dirRow;
        head.col += dirCol;
        if (snake.contains(head)) {
            throw new SnakeCollision("Snake Head is now on Snake Body.");
        }
        snake.add(0, head);
        tail = snake.remove(snake.size() - 1);
    }

    public TextPoint getHead() {
        return snake.get(0);
    }

    public List<TextPoint> getSnake() {
        return new ArrayList<TextPoint>(snake);
    }
}

class SnakeAddException extends Exception {
    /**
     * 
     */
    private static final long serialVersionUID = 91781899778607805L;

    public SnakeAddException() {
    }

    public SnakeAddException(String message) {
        super(message);
    }
}

class SnakeCollision extends Exception {
    /**
     * 
     */
    private static final long serialVersionUID = 9134731213963921113L;

    public SnakeCollision() {
    }

    public SnakeCollision(String message) {
        super(message);
    }
}
