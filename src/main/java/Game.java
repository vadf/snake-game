import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Game {

    static Snake                   snake;
    private static int             defaultSnakeSize      = 3;
    private static Snake.Direction defaultSnakeDirection = Snake.Direction.RIGHT;
    private static TextPoint       defaultSnakeHead      = new TextPoint(1, 5);

    static GameField               field;
    private static String          defaultField          = "src/main/resources/defaultField.txt";

    static Stars                   stars;
    private static int             defaultNumOfStars     = 3;

    public final static char       WALL                  = '#';
    public final static char       HEAD                  = '@';
    public final static char       BODY                  = '*';
    public final static char       STAR                  = '+';

    public static void main(String[] args) {
        try {
            initData(defaultField, defaultSnakeHead, defaultSnakeDirection,
                    defaultSnakeSize, defaultNumOfStars);
            TextPoint newStar = null;
            char ch = 0;
            while (true) {
                System.out.println(toText());

                // read key and turn
                ch = readChar();
                switch (ch) {
                case 'w':
                    snake.turn(Snake.Direction.UP);
                    break;
                case 'a':
                    snake.turn(Snake.Direction.LEFT);
                    break;
                case 's':
                    snake.turn(Snake.Direction.DOWN);
                    break;
                case 'd':
                    snake.turn(Snake.Direction.RIGHT);
                    break;
                case '\n':
                    continue;
                }

                // move snake
                move();

                // generate new star if needed
                if (stars.getNumOfStars() < defaultNumOfStars) {
                    newStar = newStar();
                    if (newStar == null) {
                        System.out.println("Congratulations");
                        break;
                    }
                    stars.add(newStar);
                }

                // sleep
                // Thread.sleep(1000);
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public static void initData(String textField, TextPoint snakeHead,
            Snake.Direction snakeDirection, int snakeSize, int numOfStars)
            throws OutOfFieldException, IOException, GameSnakeOnWallException {
        field = new GameField(textField);
        if (field.isWall(snakeHead)) {
            throw new GameSnakeOnWallException("Snake Head is on the Wall");
        }
        snake = new Snake(snakeHead, snakeDirection, snakeSize);
        stars = new Stars();
        for (int i = 0; i < numOfStars; i++) {
            stars.add(newStar());
        }
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

    public static void move() throws GameSnakeOnWallException,
            OutOfFieldException, SnakeAddException, SnakeCollision {
        snake.move();
        TextPoint head = snake.getHead();
        if (field.isWall(head)) {
            throw new GameSnakeOnWallException("Snake Head is on the Wall");
        }
        if (stars.isStar(head)) {
            snake.add();
            stars.remove(head);
        }

    }

    public static char readChar() {
        try {
            int ch = System.in.read();
            return (char) ch;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return 0;
    }
}

class GameSnakeOnWallException extends Exception {
    /**
     * 
     */
    private static final long serialVersionUID = 1097078667154590888L;

    public GameSnakeOnWallException() {
    }

    public GameSnakeOnWallException(String message) {
        super(message);
    }
}
