import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class Game {

    static int                    defaultSnakeSize      = 3;
    static Direction              defaultSnakeDirection = Direction.RIGHT;
    static TextPoint              defaultSnakeHead      = new TextPoint(1, 5);
    static String                 defaultField          = "src/main/resources/defaultField.txt";
    static int                    defaultNumOfStars     = 3;
    static TextPoint              defaultP1             = new TextPoint(1, 1);
    static Direction              defaultD1             = Direction.RIGHT;
    static TextPoint              defaultP2             = new TextPoint(9, 17);
    static Direction              defaultD2             = Direction.UP;

    private String                textField;
    private int                   score                 = 0;

    private static GameController gameController;
    private TextPoint             fieldSize;
    private JFrame                frame;
    private Timer                 t;

    public static void main(String[] args) {

        final Game game = new Game();
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                game.loadGame();
            }
        });

    }

    public Game() {
        gameController = new GameController();
        t = new Timer(1000, this.new MoveTask());
    }

    public void initGameField(int rows, int cols) {
        frame.add(new GameArea(rows, cols), BorderLayout.CENTER);
        frame.add(new ScorePanel(cols), BorderLayout.SOUTH);
        frame.pack();
    }

    public void loadGame() {
        frame = new JFrame("Snake Game Loader");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel pLoad = new JPanel();
        pLoad.setLayout(new BoxLayout(pLoad, BoxLayout.Y_AXIS));

        Dimension btnSize = new Dimension(200, 25);
        JButton btnDefault = new JButton("Default Game");
        JButton btnGame = new JButton("Load Game");
        JButton btnStart = new JButton("Start Game");
        JButton btnPause = new JButton("Pause Game");
        JButton btnQuit = new JButton("Quit");
        btnDefault.setMaximumSize(btnSize);
        btnGame.setMaximumSize(btnSize);
        btnStart.setMaximumSize(btnSize);
        btnPause.setMaximumSize(btnSize);
        btnQuit.setMaximumSize(btnSize);

        btnDefault.addActionListener(this.new DefaultGame());
        btnGame.addActionListener(this.new LoadGame());
        btnStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                t.start();
            }
        });

        btnPause.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                t.stop();
            }
        });

        btnQuit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        pLoad.add(btnDefault);
        pLoad.add(btnGame);
        pLoad.add(btnStart);
        pLoad.add(btnPause);
        pLoad.add(btnQuit);

        frame.add(pLoad, BorderLayout.EAST);
        frame.pack();
        frame.setVisible(true);

    }

    class DefaultGame implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                fieldSize = gameController.initField(defaultField);
                gameController.initSnake(defaultSnakeHead,
                        defaultSnakeDirection, defaultSnakeSize);
                gameController.initPorts(defaultP1, defaultD1, defaultP2,
                        defaultD2);
                gameController.addStars(defaultNumOfStars);
                initGameField(fieldSize.row, fieldSize.col);
            } catch (IOException | OutOfFieldException | SnakeOnWallException
                    | TeleportInitException e1) {
                System.err.println(e1.getMessage());
            }
        }
    }

    class LoadGame implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser fileopen = new JFileChooser();
            if (fileopen.showDialog(frame, "Open") == JFileChooser.APPROVE_OPTION) {
                File file = fileopen.getSelectedFile();
                try {
                    fieldSize = gameController
                            .initField(file.getAbsolutePath());
                    initGameField(fieldSize.row, fieldSize.col);
                    new SnakeParams();
                    new StarsParams();
                } catch (IOException e1) {
                    System.err.println(e1.getMessage());
                }
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
            setFont(new Font("Courier", getFont().getStyle(), getFont()
                    .getSize()));
            setFocusable(true);
            addKeyListener(this);
        }

        public void paintComponent(Graphics g) {
            setText(textField);
            super.paintComponent(g);
        }

        @Override
        public void keyTyped(KeyEvent e) {

        }

        @Override
        public void keyPressed(KeyEvent e) {
            gameController.setDirection(e.getKeyCode());
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
            setFont(new Font("Courier", getFont().getStyle(), getFont()
                    .getSize()));
        }

        public void paintComponent(Graphics g) {
            setText("Score " + score);
            super.paintComponent(g);
        }
    }

    class MoveTask implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            try {
                gameController.move();
                // generate new star if needed
                if (gameController.checkAndCreateStar()) {
                    System.out.println("Congratulations");
                    t.stop();
                }
            } catch (SnakeOnWallException | OutOfFieldException
                    | SnakeAddException | SnakeCollision e1) {
                System.err.println(e1.getMessage());
                t.stop();
            }
            score = gameController.getScore();
            textField = gameController.toString();
        }
    }

    class SnakeParams extends JDialog {
        /**
         * 
         */
        private static final long serialVersionUID = 4032564486821545199L;

        public SnakeParams() {
            setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

            add(new JLabel("Select Snake Head: row"));
            int max_size = gameController.field.getRowsNum() - 1;
            final JSlider sliderRow = new JSlider(SwingConstants.HORIZONTAL, 0,
                    max_size, 5);
            sliderRow.setMinorTickSpacing(1);
            sliderRow.setMajorTickSpacing(5);
            sliderRow.setPaintTicks(true);
            sliderRow.setPaintLabels(true);
            add(sliderRow);

            add(new JLabel("Select Snake Head: col"));
            max_size = gameController.field.getColsNum() - 1;
            final JSlider sliderCol = new JSlider(SwingConstants.HORIZONTAL, 0,
                    max_size, 5);
            sliderCol.setMinorTickSpacing(1);
            sliderCol.setMajorTickSpacing(5);
            sliderCol.setPaintTicks(true);
            sliderCol.setPaintLabels(true);
            add(sliderCol);

            add(new JLabel("Select Snake size"));
            max_size = 3 + gameController.field.getEffectiveSize() / 30;
            final JSlider sliderSize = new JSlider(SwingConstants.HORIZONTAL,
                    1, max_size, 3);
            sliderSize.setMinorTickSpacing(1);
            sliderSize.setMajorTickSpacing(3);
            sliderSize.setPaintTicks(true);
            sliderSize.setPaintLabels(true);
            add(sliderSize);

            add(new JLabel("Select Snake direction"));
            final JList<Direction> list = new JList<Direction>(
                    Direction.values());
            list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            list.setSelectedIndex(0);
            add(list);

            JButton btnSave = new JButton("Save");
            btnSave.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        gameController.initSnake(
                                new TextPoint(sliderRow.getValue(), sliderCol
                                        .getValue()), list.getSelectedValue(),
                                sliderSize.getValue());
                        dispose();
                    } catch (OutOfFieldException | SnakeOnWallException e1) {
                        System.err
                                .println("Snake Init Failed. Please select another Snake params.");
                        System.err.println(e1.getMessage());
                    }

                }

            });
            add(btnSave);

            pack();

            setModalityType(ModalityType.APPLICATION_MODAL);
            setTitle("Select Snake params");
            setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            setLocationRelativeTo(null);
            setVisible(true);
        }
    }

    class StarsParams extends JDialog {
        /**
         * 
         */
        private static final long serialVersionUID = 1L;

        public StarsParams() {
            setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

            add(new JLabel("Sel number of Stars"));
            int max_size = 5 + gameController.field.getEffectiveSize() / 20;
            final JSlider slider = new JSlider(SwingConstants.HORIZONTAL, 0,
                    max_size, 3);
            slider.setMinorTickSpacing(1);
            slider.setMajorTickSpacing(5);
            slider.setPaintTicks(true);
            slider.setPaintLabels(true);
            add(slider);

            JButton btnSave = new JButton("Save");
            btnSave.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    gameController.addStars(slider.getValue());
                    dispose();
                }

            });
            add(btnSave);

            pack();

            setModalityType(ModalityType.APPLICATION_MODAL);
            setTitle("Select number of stars");
            setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            setLocationRelativeTo(null);
            setVisible(true);
        }
    }
}
