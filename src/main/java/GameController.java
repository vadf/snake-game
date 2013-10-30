import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class GameController {

    static Snake                snake;
    static GameField            field;
    static Stars                stars;

    private static int          maxNumOfStars;
    private static int          score = 0;

    protected final static char WALL  = '#';
    protected final static char HEAD  = '@';
    protected final static char BODY  = '*';
    protected final static char STAR  = '+';

    public static TextPoint initData(String textField, TextPoint snakeHead,
            Snake.Direction snakeDirection, int snakeSize, int numOfStars)
            throws OutOfFieldException, IOException, SnakeOnWallException {
        field = new GameField(textField);
        if (field.isWall(snakeHead)) {
            throw new SnakeOnWallException("Snake Head is on the Wall");
        }
        snake = new Snake(snakeHead, snakeDirection, snakeSize);
        stars = new Stars();
        for (int i = 0; i < numOfStars; i++) {
            stars.add(newStar());
        }

        maxNumOfStars = numOfStars;
        return new TextPoint(field.getRowsNum(), field.getColsNum());
    }

    public static TextPoint newStar() throws OutOfFieldException {
        Random r = new Random(System.currentTimeMillis());
        int row = 0;
        int col = 0;
        TextPoint point = null;
        while (true) {
            if (field.getEffectiveSize() - snake.getSize()
                    - stars.getStars().size() == 0) {
                break;
            }
            row = r.nextInt(field.getRowsNum());
            col = r.nextInt(field.getColsNum());
            point = new TextPoint(row, col);
            if (!field.isWall(point) && !snake.isOnSnake(point)
                    && !stars.isStar(point))
                break;
        }
        return point;
    }

    public static String toText() {
        List<TextPoint> wallsList = field.getWalls();
        List<TextPoint> snakeList = snake.getSnake();
        List<TextPoint> starsList = stars.getStars();
        char[][] textField = new char[field.getRowsNum()][field.getColsNum()];
        for (int i = 0; i < textField.length; i++) {
            Arrays.fill(textField[i], ' ');
        }

        for (TextPoint p : wallsList) {
            textField[p.row][p.col] = WALL;
        }

        for (TextPoint p : snakeList) {
            textField[p.row][p.col] = BODY;
        }
        TextPoint head = snake.getHead();
        textField[head.row][head.col] = HEAD;

        for (TextPoint p : starsList) {
            textField[p.row][p.col] = STAR;
        }
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < textField.length; i++)
            str.append(textField[i]).append("\n");

        return str.toString();
    }

    public static void move() throws SnakeOnWallException, OutOfFieldException,
            SnakeAddException, SnakeCollision {
        snake.move();
        TextPoint head = snake.getHead();
        if (field.isWall(head)) {
            throw new SnakeOnWallException("Snake Head is on the Wall");
        }
        if (stars.isStar(head)) {
            snake.add();
            stars.remove(head);
            score++;
        }

    }

    public static boolean checkAndCreateStar() throws OutOfFieldException {
        boolean result = false;
        if (stars.getNumOfStars() < maxNumOfStars) {
            TextPoint newStar = newStar();
            if (newStar == null) {
                result = true;
            }
            stars.add(newStar);
        }

        return result;
    }

    public static void setDirection(int key) {
        switch (key) {
        case KeyEvent.VK_UP:
        case KeyEvent.VK_W:
            snake.turn(Snake.Direction.UP);
            break;
        case KeyEvent.VK_LEFT:
        case KeyEvent.VK_A:
            snake.turn(Snake.Direction.LEFT);
            break;
        case KeyEvent.VK_DOWN:
        case KeyEvent.VK_S:
            snake.turn(Snake.Direction.DOWN);
            break;
        case KeyEvent.VK_RIGHT:
        case KeyEvent.VK_D:
            snake.turn(Snake.Direction.RIGHT);
            break;
        }
    }

    public static int getScore() {
        return score;
    }
}

class SnakeOnWallException extends Exception {
    /**
     * 
     */
    private static final long serialVersionUID = 1097078667154590888L;

    public SnakeOnWallException() {
    }

    public SnakeOnWallException(String message) {
        super(message);
    }
}