import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class Game {

    private static int             defaultSnakeSize      = 3;
    private static Snake.Direction defaultSnakeDirection = Snake.Direction.RIGHT;
    private static TextPoint       defaultSnakeHead      = new TextPoint(1, 5);
    private static String          defaultField          = "src/main/resources/defaultField.txt";
    private static int             defaultNumOfStars     = 3;
    private static TextPoint       fieldSize;

    public static String           textField;
    public static char             ch                    = 0;
    public static boolean          move                  = false;

    public static void main(String[] args) {
        Timer t = new Timer(1000, new MoveTask());
        try {
            fieldSize = GameController.initData(defaultField, defaultSnakeHead,
                    defaultSnakeDirection, defaultSnakeSize, defaultNumOfStars);
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    initGUI(fieldSize.row, fieldSize.col);
                }
            });

            t.start();
            while (true) {
                textField = GameController.toText();

                // read key and turn
                GameController.setDirection(ch);

                // move snake and check
                if (move) {
                    GameController.move();
                    move = false;
                }

                // generate new star if needed
                if (GameController.checkAndCreateStar()) {
                    System.out.println("Congratulations");
                    break;
                }
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        t.stop();
    }

    public static char readChar() {
        try {
            int ch = System.in.read();
            return (char) ch;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static void initGUI(int rows, int cols) {
        JFrame frame = new JFrame("Snake Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new GameArea(rows, cols));
        frame.pack();
        frame.setVisible(true);
    }
}

class GameArea extends JTextArea implements KeyListener {
    /**
     * 
     */
    private static final long serialVersionUID = -7949713835432906738L;

    public GameArea(int rows, int cols) {
        super(rows, cols);
        setFont(new Font("Courier", getFont().getStyle(), getFont().getSize()));
        setFocusable(true);
        addKeyListener(this);
    }

    public void paintComponent(Graphics g) {
        setText(Game.textField);
        super.paintComponent(g);
    }

    @Override
    public void keyTyped(KeyEvent e) {
        Game.ch = e.getKeyChar();
    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}

class MoveTask implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        Game.move = true;
    }
}
