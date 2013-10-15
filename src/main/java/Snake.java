import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class Snake {
    private List<Point> snake  = new ArrayList<Point>();
    private Direction   direction;
    private int         dirCol = 0;                     // point.x
    private int         dirRow = 0;                     // point.y

    public Snake(Point head, int snakeSize, Direction direction)
            throws Exception {
        switch (direction) {
        case RIGHT:
            dirCol = 1;
            break;
        case LEFT:
            dirCol = -1;
            break;
        case UP:
            dirRow = 1;
            break;
        case DOWN:
            dirRow = -1;
            break;
        default:
            throw new Exception("Incorrect Snake Direction");
        }
        this.direction = direction;

        snake.add(head);
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

}
