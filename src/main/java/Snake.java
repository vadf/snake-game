import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class Snake {
    private List<Point> snake  = new ArrayList<Point>();
    private int         dirCol = 0;                     // point.x
    private int         dirRow = 0;                     // point.y

    public Snake(Point head, int snakeSize, Direction direction) {
        snake.add(head);
        turn(direction);
        for (int i = 1; i < snakeSize; i++) {
            Point p = new Point(head.x - dirRow * i, head.y - dirCol * i);
            snake.add(p);
        }

    }

    public enum Direction {
        RIGHT, LEFT, UP, DOWN
    }

    public int getSize() {
        return snake.size();
    }

    public boolean isOnSnake(Point p) {
        return snake.contains(p);
    }

    public int add() {
        Point head = new Point(snake.get(0));
        head.x += dirRow;
        head.y += dirCol;
        snake.add(0, head);
        return snake.size();
    }

    protected List<Point> getSnake() {
        return snake;
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

        // if direction was changed to opposite
        Point head = new Point(snake.get(0));
        head.x += dirRow;
        head.y += dirCol;
        if (snake.contains(head)) {
            // don't change direction
            dirCol = oldCol;
            dirRow = oldRow;
        }
    }

    public void move() {
        add();
        snake.remove(snake.size() - 1);
    }

}
