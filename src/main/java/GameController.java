import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class GameController {

    Snake                snake;
    GameField            field;
    Stars                stars         = new Stars();
    Teleport[]           ports         = new Teleport[9];

    private int          maxNumOfStars = 1;
    private int          score         = 0;

    protected final char WALL          = '#';
    protected final char HEAD          = '@';
    protected final char BODY          = '*';
    protected final char STAR          = '+';

    private boolean isPort(TextPoint point) {
        boolean isPort = false;
        for (int i = 0; i < ports.length; i++) {
            if (ports[i] != null && ports[i].isPort(point)) {
                isPort = true;
            }
        }
        return isPort;

    }

    public TextPoint getEmptyPoint() {
        int ports_size = 0;
        for (int i = 0; i < ports.length; i++) {
            if (ports[i] != null) {
                ports_size += ports[i].getNumOfPorts();
            }
        }

        if (field.getEffectiveSize() - snake.getSize() - stars.getStars().size() - ports_size == 0) {
            return null;
        }

        Random r = new Random(System.currentTimeMillis());
        int row = 0;
        int col = 0;
        TextPoint point = null;

        try {
            while (true) {
                row = r.nextInt(field.getRowsNum());
                col = r.nextInt(field.getColsNum());
                point = new TextPoint(row, col);

                if (!field.isWall(point) && !snake.isOnSnake(point) && !stars.isStar(point)
                        && !isPort(point))
                    break;
            }
        } catch (OutOfFieldException e) {
            System.out.println("This should not happen :)");
            System.out.println(e.getMessage());
        }
        return point;
    }

    @Override
    public String toString() {
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

        for (int i = 0; i < ports.length; i++) {
            if (ports[i] != null) {
                for (TextPoint p : ports[i].getPorts()) {
                    textField[p.row][p.col] = (char) ('0' + i);
                }
            }
        }

        StringBuilder str = new StringBuilder();
        for (int i = 0; i < textField.length; i++)
            str.append(textField[i]).append("\n");

        return str.toString();
    }

    public void move() throws SnakeOnWallException, OutOfFieldException, SnakeAddException,
            SnakeCollision {
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

        for (int i = 0; i < ports.length; i++) {
            if (ports[i] != null && ports[i].isPort(head)) {
                ports[i].updateHead(snake);
                break;
            }
        }

    }

    public boolean checkAndCreateStar() throws OutOfFieldException {
        boolean result = false;
        if (stars.getNumOfStars() < maxNumOfStars) {
            TextPoint newStar = getEmptyPoint();
            if (newStar == null) {
                result = true;
            }
            stars.add(newStar);
        }

        return result;
    }

    public void setDirection(int key) {
        switch (key) {
        case KeyEvent.VK_UP:
        case KeyEvent.VK_W:
            snake.turn(Direction.UP);
            break;
        case KeyEvent.VK_LEFT:
        case KeyEvent.VK_A:
            snake.turn(Direction.LEFT);
            break;
        case KeyEvent.VK_DOWN:
        case KeyEvent.VK_S:
            snake.turn(Direction.DOWN);
            break;
        case KeyEvent.VK_RIGHT:
        case KeyEvent.VK_D:
            snake.turn(Direction.RIGHT);
            break;
        }
    }

    public int getScore() {
        return score;
    }

    public TextPoint initField(String textField) throws IOException {
        field = new GameField(textField);
        return new TextPoint(field.getRowsNum(), field.getColsNum());
    }

    public void initSnake(TextPoint snakeHead, Direction snakeDirection, int snakeSize)
            throws OutOfFieldException, SnakeOnWallException {

        snake = new Snake(snakeHead, snakeDirection, snakeSize);
        for (TextPoint p : snake.getSnake()) {
            if (field.isWall(p)) {
                throw new SnakeOnWallException("Snake point " + snake.getSnake().indexOf(p) + ":"
                        + p + " is on the Wall");
            }
        }
    }

    public void addStars(int numOfStars) {
        for (int i = 0; i < numOfStars; i++) {
            stars.add(getEmptyPoint());
        }
        maxNumOfStars = numOfStars;
    }

    public void initPorts(TextPoint p1, Direction d1, TextPoint p2, Direction d2)
            throws TeleportInitException, OutOfFieldException {
        checkPortPosition(p1, d1);
        checkPortPosition(p2, d2);

        field.removeWall(p1);
        field.removeWall(p2);
        Teleport port = new Teleport(p1, d1, p2, d2);
        for (int i = 0; i < ports.length; i++) {
            if (ports[i] == null) {
                ports[i] = port;
                break;
            }
        }
    }

    private void checkPortPosition(TextPoint p, Direction d) throws TeleportInitException {
        int dirCol = 0;
        int dirRow = 0;
        switch (d) {
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
            throw new TeleportInitException("Wrong port direction " + d);
        }

        TextPoint portOut = new TextPoint(p.row + dirRow, p.col + dirCol);
        try {
            if (field.isWall(portOut))
                throw new TeleportInitException("Port " + p + " direction " + d
                        + " is looking at the wall");
        } catch (OutOfFieldException e) {
            throw new TeleportInitException("Port " + p + " direction " + d
                    + " is looking outside the field");
        }

        if (isPort(p)) {
            throw new TeleportInitException("Port " + p + " is already exists");
        }
    }
}

class SnakeOnWallException extends Exception {
    /**
     * 
     */
    private static final long serialVersionUID = 1097078667154590888L;

    public SnakeOnWallException() {
        super();
    }

    public SnakeOnWallException(String message) {
        super(message);
    }
}

class PortAddException extends Exception {
    /**
     * 
     */
    private static final long serialVersionUID = 743114839912596609L;

    public PortAddException() {
        super();
    }

    public PortAddException(String message) {
        super(message);
    }
}