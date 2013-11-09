import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class Game {

    static int                   defaultSnakeSize      = 3;
    static Direction             defaultSnakeDirection = Direction.RIGHT;
    static TextPoint             defaultSnakeHead      = new TextPoint(1, 5);
    static String                defaultField          = "src/main/resources/defaultField.txt";
    static int                   defaultNumOfStars     = 3;
    static TextPoint             defaultP1             = new TextPoint(1, 1);
    static Direction             defaultD1             = Direction.RIGHT;
    static TextPoint             defaultP2             = new TextPoint(9, 17);
    static Direction             defaultD2             = Direction.UP;

    public static String         textField;
    public static int            key                   = 0;
    public static boolean        move                  = false;
    public static int            score                 = 0;

    public static GameController game;
    public static TextPoint      fieldSize;
    public static JFrame         frame;
    public static Timer          t                     = new Timer(1000,
                                                               new MoveTask());

    public static void main(String[] args) {

        game = new GameController();
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                loadGame();
            }
        });

    }

    public static void initGameField(int rows, int cols) {
        frame.add(new GameArea(rows, cols), BorderLayout.CENTER);
        frame.add(new ScorePanel(cols), BorderLayout.SOUTH);
        frame.pack();
        t.start();
    }

    public static void loadGame() {
        frame = new JFrame("Snake Game Loader");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel pLoad = new JPanel();
        pLoad.setLayout(new BoxLayout(pLoad, BoxLayout.Y_AXIS));

        JButton btnDefault = new JButton("Default Game");
        JButton btnGame = new JButton("New Game");

        btnDefault.setBounds(0, 0, 40, 10);
        btnGame.setBounds(0, 0, 40, 10);
        btnDefault.addActionListener(new DefaultGame());

        pLoad.add(btnDefault);
        pLoad.add(btnGame);
        frame.add(pLoad, BorderLayout.EAST);
        frame.pack();
        frame.setVisible(true);

    }
}

class DefaultGame implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            Game.fieldSize = Game.game.initField(Game.defaultField);
            Game.game.initSnake(Game.defaultSnakeHead,
                    Game.defaultSnakeDirection, Game.defaultSnakeSize);
            Game.game.initPorts(Game.defaultP1, Game.defaultD1, Game.defaultP2,
                    Game.defaultD2);
            Game.game.addStars(Game.defaultNumOfStars);
            Game.initGameField(Game.fieldSize.row, Game.fieldSize.col);
        } catch (IOException | OutOfFieldException | SnakeOnWallException
                | TeleportInitException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

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

    }

    @Override
    public void keyPressed(KeyEvent e) {
        Game.game.setDirection(e.getKeyCode());
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}

class ScorePanel extends JTextField {
    /**
     * 
     */
    private static final long serialVersionUID = 1519485961961948821L;

    public ScorePanel(int cols) {
        super(cols);
        setFont(new Font("Courier", getFont().getStyle(), getFont().getSize()));
    }

    public void paintComponent(Graphics g) {
        setText("Score " + Game.score);
        super.paintComponent(g);
    }
}

class MoveTask implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {

        try {
            Game.game.move();
            // generate new star if needed
            if (Game.game.checkAndCreateStar()) {
                System.out.println("Congratulations");
                Game.t.stop();
            }
        } catch (SnakeOnWallException | OutOfFieldException | SnakeAddException
                | SnakeCollision e1) {
            e1.printStackTrace();
            Game.t.stop();
        }
        Game.score = Game.game.getScore();
        Game.textField = Game.game.toString();
    }
}
