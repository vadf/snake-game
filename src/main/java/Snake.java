import java.util.ArrayList;
import java.util.List;

public class Snake {
    private List<TextPoint> snake  = new ArrayList<TextPoint>();
    private int             dirCol = 0;
    private int             dirRow = 0;

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

    public void add() {
        TextPoint head = new TextPoint(snake.get(0));
        head.row += dirRow;
        head.col += dirCol;
        snake.add(0, head);
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

    public TextPoint getHead() {
        return snake.get(0);
    }

    public List<TextPoint> getSnake() {
        return new ArrayList<TextPoint>(snake);
    }
}
