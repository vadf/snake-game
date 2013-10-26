import java.io.IOException;

public class Game {

    private static int             defaultSnakeSize      = 3;
    private static Snake.Direction defaultSnakeDirection = Snake.Direction.RIGHT;
    private static TextPoint       defaultSnakeHead      = new TextPoint(1, 5);

    private static String          defaultField          = "src/main/resources/defaultField.txt";

    private static int             defaultNumOfStars     = 3;

    public static void main(String[] args) {
        try {
            GameController.initData(defaultField, defaultSnakeHead,
                    defaultSnakeDirection, defaultSnakeSize, defaultNumOfStars);

            char ch = 0;
            while (true) {
                System.out.println(GameController.toText());

                // read key and turn
                ch = readChar();
                if (!GameController.setDirection(ch)) {
                    continue;
                }

                // move snake
                GameController.move();

                // generate new star if needed
                if (GameController.checkAndCreateStar()) {
                    System.out.println("Congratulations");
                    break;
                }
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
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
