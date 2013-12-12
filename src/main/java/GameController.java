import java.awt.event.KeyEvent;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;

public class GameController {

    GameField                     field;
    Snake[]                       snake         = new Snake[2];
    private int[]                 score         = { 0, 0 };
    Stars                         stars         = new Stars();
    Teleport[]                    ports         = new Teleport[9];
    private int                   maxNumOfStars = 1;
    private GameType              gameType      = GameType.SINGLE;

    protected static final char   WALL          = '#';
    protected static final char[] HEAD          = { '@', '0' };
    protected static final char[] BODY          = { '*', 'o' };
    protected static final char   STAR          = '+';

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

        int restSize = field.getEffectiveSize() - snake[0].getSize() - stars.getStars().size()
                - ports_size;
        restSize -= gameType == GameType.SINGLE ? 0 : snake[1].getSize();
        if (restSize == 0) {
            return null;
        }

        Random r = new Random(System.currentTimeMillis());
        int row = 0;
        int col = 0;
        TextPoint point = null;

        try {
            while (true) {
                row = r.nextInt(field.getFieldSize().row);
                col = r.nextInt(field.getFieldSize().col);
                point = new TextPoint(row, col);

                if (!field.isWall(point) && !snake[0].isOnSnake(point) && !stars.isStar(point)
                        && !isPort(point)
                        && !(gameType != GameType.SINGLE && snake[1].isOnSnake(point))) {
                    break;
                }
            }
        } catch (OutOfFieldException e) {
            System.out.println("This should not happen :)");
            System.out.println(e.getMessage());
        }
        return point;
    }

    @Override
    public String toString() {
        char[][] textField = new char[field.getFieldSize().row][field.getFieldSize().col];
        for (int i = 0; i < textField.length; i++) {
            Arrays.fill(textField[i], ' ');
        }

        if (field != null) {
            List<TextPoint> wallsList = field.getWalls();
            for (TextPoint p : wallsList) {
                textField[p.row][p.col] = WALL;
            }
        }

        for (int i = 0; i < snake.length; i++) {
            if (snake[i] != null) {
                if (gameType == GameType.SINGLE && i >= 1) {
                    break;
                }
                List<TextPoint> snakeList = snake[i].getSnake();
                for (TextPoint p : snakeList) {
                    textField[p.row][p.col] = BODY[i];
                }
                TextPoint head = snake[i].getHead();
                textField[head.row][head.col] = HEAD[i];
            }
        }

        if (stars != null) {
            List<TextPoint> starsList = stars.getStars();
            for (TextPoint p : starsList) {
                textField[p.row][p.col] = STAR;
            }
        }

        for (int i = 0; i < ports.length; i++) {
            if (ports[i] != null) {
                for (TextPoint p : ports[i].getPorts()) {
                    textField[p.row][p.col] = (char) ('0' + i);
                }
            }
        }

        StringBuilder str = new StringBuilder();
        for (int i = 0; i < textField.length; i++) {
            str.append(textField[i]).append("\n");
        }

        return str.toString();
    }

    public void move() throws SnakeOnWallException, OutOfFieldException, SnakeAddException,
            SnakeCollisionException {
        // TODO: re-factor, too messy
        for (int j = 0; j < snake.length; j++) {
            if (gameType == GameType.SINGLE && j >= 1) {
                break;
            }
            try {
                snake[j].move();
            } catch (SnakeCollisionException e) {
                if (gameType == GameType.MULTI_BATTLE) {
                    // TODO: choose direction randomly
                    initSnake(j + 1, getEmptyPoint(), Direction.RIGHT, 1);
                    continue;
                } else {
                    throw e;
                }
            }
            TextPoint head = snake[j].getHead();
            if (field.isWall(head) && gameType != GameType.MULTI_BATTLE) {
                throw new SnakeOnWallException("Snake Head is on the Wall");
            } else if (field.isWall(head) && gameType == GameType.MULTI_BATTLE) {
                initSnake(j + 1, getEmptyPoint(), Direction.RIGHT, 1);
                continue;
            }
            if (stars.isStar(head)) {
                snake[j].add();
                stars.remove(head);
                score[j]++;
            }
            for (int i = 0; i < ports.length; i++) {
                if (ports[i] != null && ports[i].isPort(head)) {
                    ports[i].updateHead(snake[j]);
                    break;
                }
            }
        }

        if (gameType != GameType.SINGLE) {
            boolean isClash = snake[0].isOnSnake(snake[1].getHead())
                    || snake[1].isOnSnake(snake[0].getHead());
            boolean isNotBattle = gameType == GameType.MULTI;
            if (isClash) {
                // eat other snake tail or restart smaller snake on clash (if
                // battle)
                if (isNotBattle) {
                    throw new SnakeCollisionException("Snakes clash");
                } else if (snake[0].getSize() == 1 && snake[1].getSize() == 1) {
                    initSnake(1, getEmptyPoint(), Direction.RIGHT, 1);
                    initSnake(2, getEmptyPoint(), Direction.RIGHT, 1);
                } else if (snake[0].isTail(snake[1].getHead())) {
                    if (snake[0].getSize() == 1) {
                        initSnake(1, getEmptyPoint(), Direction.RIGHT, 1);
                    } else {
                        snake[0].cutTail();
                    }
                    score[1]++;
                    snake[1].add();
                } else if (snake[1].isTail(snake[0].getHead())) {
                    if (snake[1].getSize() == 1) {
                        initSnake(2, getEmptyPoint(), Direction.RIGHT, 1);
                    } else {
                        snake[1].cutTail();
                    }
                    score[0]++;
                    snake[0].add();
                } else if (snake[0].getSize() > snake[1].getSize()) {
                    initSnake(2, getEmptyPoint(), Direction.RIGHT, 1);
                } else if (snake[0].getSize() < snake[1].getSize()) {
                    initSnake(1, getEmptyPoint(), Direction.RIGHT, 1);
                } else if (snake[0].getSize() == snake[1].getSize()) {
                    initSnake(1, getEmptyPoint(), Direction.RIGHT, 1);
                    initSnake(2, getEmptyPoint(), Direction.RIGHT, 1);
                }
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
            snake[0].turn(Direction.UP);
            break;
        case KeyEvent.VK_W:
            snake[1].turn(Direction.UP);
            break;
        case KeyEvent.VK_LEFT:
            snake[0].turn(Direction.LEFT);
            break;
        case KeyEvent.VK_A:
            snake[1].turn(Direction.LEFT);
            break;
        case KeyEvent.VK_DOWN:
            snake[0].turn(Direction.DOWN);
            break;
        case KeyEvent.VK_S:
            snake[1].turn(Direction.DOWN);
            break;
        case KeyEvent.VK_RIGHT:
            snake[0].turn(Direction.RIGHT);
            break;
        case KeyEvent.VK_D:
            snake[1].turn(Direction.RIGHT);
            break;
        }
    }

    public int[] getScore() {
        return score;
    }

    public TextPoint initField(String textField) throws IOException, FieldInitException {
        field = new GameField(textField);
        return field.getFieldSize();
    }

    public void initSnake(int num, TextPoint snakeHead, Direction snakeDirection, int snakeSize)
            throws OutOfFieldException, SnakeOnWallException, SnakeCollisionException {
        snake[num - 1] = new Snake(snakeHead, snakeDirection, snakeSize, field.getFieldSize());
        for (TextPoint p : snake[num - 1].getSnake()) {
            if (field.isWall(p)) {
                throw new SnakeOnWallException("Snake point "
                        + snake[num - 1].getSnake().indexOf(p) + ":" + p + " is on the Wall");
            }
        }

        if (gameType != GameType.SINGLE && snake[0] != null && snake[1] != null) {
            for (TextPoint p : snake[0].getSnake()) {
                if (snake[1].isOnSnake(p)) {
                    throw new SnakeCollisionException("Snake Collision during Init");
                }
            }
        }
    }

    public void addStars(int numOfStars) {
        for (int i = 0; i < numOfStars; i++) {
            stars.add(getEmptyPoint());
        }
        maxNumOfStars = numOfStars;
    }

    public void initPorts(HashMap<TextPoint, Direction> newPorts) throws TeleportInitException,
            OutOfFieldException {
        for (Entry<TextPoint, Direction> entry : newPorts.entrySet()) {
            checkPortPosition(entry.getKey(), entry.getValue());
            field.removeWall(entry.getKey());
        }

        Teleport port = new Teleport(newPorts);
        for (int i = 0; i < ports.length; i++) {
            if (ports[i] == null) {
                ports[i] = port;
                break;
            }
        }
    }

    private void checkPortPosition(TextPoint p, Direction d) throws TeleportInitException {
        TextPoint portOut = p.move(d);
        try {
            if (field.isWall(portOut)) {
                throw new TeleportInitException("Port " + p + " direction " + d
                        + " is looking at the wall");
            }
        } catch (OutOfFieldException e) {
            throw new TeleportInitException("Port " + p + " direction " + d
                    + " is looking outside the field");
        }

        if (isPort(p)) {
            throw new TeleportInitException("Port " + p + " is already exists");
        }
    }

    public void setType(GameType gameType) {
        this.gameType = gameType;
    }

    public TextPoint initFromConfig(String filename) throws IOException, FieldInitException,
            OutOfFieldException, SnakeOnWallException, SnakeCollisionException,
            TeleportInitException {
        Charset ENCODING = StandardCharsets.UTF_8;
        Path path = Paths.get(filename);
        List<String> tmpField = Files.readAllLines(path, ENCODING);

        HashMap<String, String> gameParams = new HashMap<String, String>();
        for (String line : tmpField) {
            String param, value = null;
            String[] tmp = line.split("=");
            param = tmp[0].trim();
            if (tmp.length > 1) {
                value = tmp[1].trim();
            }
            gameParams.put(param, value);
        }

        String type = gameParams.get("type");
        if ("MULTI".equals(type)) {
            setType(GameType.MULTI);
        } else if ("MULTI_BATTLE".equals(type)) {
            setType(GameType.MULTI_BATTLE);
        }

        TextPoint fieldSize = initField(gameParams.get("field_path"));

        String[] snake = gameParams.get("snake_1").split(",");
        TextPoint head = new TextPoint(Integer.valueOf(snake[0]), Integer.valueOf(snake[1]));
        Direction dir = textToDirection(snake[1]);
        int size = Integer.valueOf(snake[3]);
        initSnake(1, head, dir, size);
        if (gameType != GameType.SINGLE) {
            snake = gameParams.get("snake_2").split(",");
            head = new TextPoint(Integer.valueOf(snake[0]), Integer.valueOf(snake[1]));
            dir = textToDirection(snake[2]);
            size = Integer.valueOf(snake[3]);
            initSnake(2, head, dir, size);
        }

        String[] teleports = gameParams.get("teleports").split("\\|");
        for (int i = 0; i < teleports.length; i++) {
            String[] ports = teleports[i].split(";");
            HashMap<TextPoint, Direction> p = new HashMap<TextPoint, Direction>();
            for (int j = 0; j < ports.length; j++) {
                String[] port = ports[j].split(",");
                p.put(new TextPoint(Integer.valueOf(port[0]), Integer.valueOf(port[1])),
                        textToDirection(port[2]));
            }
            initPorts(p);
        }

        addStars(Integer.valueOf(gameParams.get("stars")));

        return fieldSize;
    }

    private Direction textToDirection(String direction) {
        Direction dir = Direction.RIGHT;
        if (direction.equals("RIGHT")) {
            dir = Direction.RIGHT;
        } else if (direction.equals("LEFT")) {
            dir = Direction.LEFT;
        } else if (direction.equals("UP")) {
            dir = Direction.UP;
        } else if (direction.equals("DOWN")) {
            dir = Direction.DOWN;
        }
        return dir;
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

enum GameType {
    SINGLE, MULTI, MULTI_BATTLE
}