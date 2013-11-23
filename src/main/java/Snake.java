import java.util.ArrayList;
import java.util.List;

public class Snake implements HeadMove {
    private List<TextPoint> snake = new ArrayList<TextPoint>();
    private TextPoint       tail;
    private Direction       direction;
    private TextPoint       border;

    // Constructor w/o snake wrapping over field (can lead to OutOfBorder exception on field.isWall check)
    @Deprecated
    public Snake(TextPoint head, Direction direction, int snakeSize) {
        this(head, direction, snakeSize, new TextPoint(Integer.MAX_VALUE, Integer.MAX_VALUE));
    }

    // Constructor w/ snake wrapping over field (if border is correct)
    public Snake(TextPoint head, Direction direction, int snakeSize, TextPoint border) {
        this.border = border;
        Direction oposite = Direction.RIGHT;
        switch (direction) {
        case RIGHT:
            oposite = Direction.LEFT;
            break;
        case LEFT:
            oposite = Direction.RIGHT;
            break;
        case UP:
            oposite = Direction.DOWN;
            break;
        case DOWN:
            oposite = Direction.UP;
            break;
        }
        snake.add(head);
        turn(direction);
        for (int i = 1; i < snakeSize; i++) {
            TextPoint p = snake.get(i - 1).move(oposite);
            snake.add(p);
        }
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
        TextPoint head = snake.get(0).move(direction);
        if (snake.size() > 1 && (snake.get(1).equals(head) || snake.get(0).equals(head))) {            
            ; // don't change direction
        } else {
            this.direction = direction;
        }
    }

    public void move() throws SnakeCollisionException {
        TextPoint head = snake.get(0).move(this.direction);
        head.row = head.row % border.row;
        if (head.row < 0) head.row += border.row;
        head.col = head.col % border.col;
        if (head.col < 0) head.col += border.col;
        if (snake.contains(head)) {
            throw new SnakeCollisionException("Snake Head is now on Snake Body.");
        }
        snake.add(0, head);
        tail = snake.remove(snake.size() - 1);
    }

    @Override
    public TextPoint getHead() {
        return snake.get(0);
    }

    public List<TextPoint> getSnake() {
        return new ArrayList<TextPoint>(snake);
    }

    @Override
    public void changeHeadPosition(TextPoint newHead, Direction newDirection) {
        snake.remove(0);
        snake.add(0, newHead);
        turn(newDirection);
    }
}

enum Direction {
    RIGHT, LEFT, UP, DOWN
}

interface HeadMove {
    public void changeHeadPosition(TextPoint newHead, Direction newDirection);

    public TextPoint getHead();
}

class SnakeAddException extends Exception {
    /**
     * 
     */
    private static final long serialVersionUID = 91781899778607805L;

    public SnakeAddException() {
        super();
    }

    public SnakeAddException(String message) {
        super(message);
    }
}

class SnakeCollisionException extends Exception {
    /**
     * 
     */
    private static final long serialVersionUID = 9134731213963921113L;

    public SnakeCollisionException() {
        super();
    }

    public SnakeCollisionException(String message) {
        super(message);
    }
}
