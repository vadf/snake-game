import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
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

    private static GameController gameController;
    private TextPoint             fieldSize;
    private JFrame                frame;
    private Timer                 t;
    private GameArea              gameArea;
    private JLabel                score, status;

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
        gameArea = new GameArea(rows, cols);
        // gameArea.setText(gameController.toString());
        frame.add(gameArea, BorderLayout.CENTER);

        score = new JLabel("Score " + gameController.getScore());
        frame.add(score, BorderLayout.SOUTH);
        
        status = new JLabel("Load game");
        frame.add(status, BorderLayout.NORTH);
        
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
                gameArea.setText(gameController.toString());
                gameArea.requestFocusInWindow();
                status.setText("Playing");
                t.start();
            }
        });

        btnPause.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                t.stop();
                status.setText("Game paused");
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
                gameController.initSnake(defaultSnakeHead, defaultSnakeDirection, defaultSnakeSize);
                gameController.initPorts(defaultP1, defaultD1, defaultP2, defaultD2);
                gameController.addStars(defaultNumOfStars);
                initGameField(fieldSize.row, fieldSize.col);
            } catch (IOException | OutOfFieldException | SnakeOnWallException
                    | TeleportInitException e1) {
                status.setText("Load failed: " + e1.getMessage());
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
                    fieldSize = gameController.initField(file.getAbsolutePath());
                    initGameField(fieldSize.row, fieldSize.col);
                    new SnakeParams();
                    gameArea.setText(gameController.toString());
                    new StarsParams();
                    gameArea.setText(gameController.toString());
                } catch (IOException e1) {
                    status.setText("Load failed: " + e1.getMessage());
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
            setFont(new Font("Courier", getFont().getStyle(), getFont().getSize()));
            setFocusable(true);
            addKeyListener(this);
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

    class MoveTask implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            try {
                gameController.move();
                // generate new star if needed
                if (gameController.checkAndCreateStar()) {
                    status.setText("Congratulations, You Win");
                    System.out.println("Congratulations");
                    t.stop();
                }
                gameArea.setText(gameController.toString());
            } catch (SnakeOnWallException | OutOfFieldException | SnakeAddException
                    | SnakeCollision e1) {
                status.setText("Game over: " + e1.getMessage());
                System.err.println(e1.getMessage());
                t.stop();
            }
            score.setText("Score " + gameController.getScore());
            ;
        }
    }

    class SnakeParams extends JDialog {
        int                       caret            = 0;
        /**
         * 
         */
        private static final long serialVersionUID = 4032564486821545199L;

        public SnakeParams() {
            super();
            setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

            add(new JLabel("Set Snake Head initial position"));
            final GameArea tmp = new GameArea(fieldSize.row, fieldSize.col);
            tmp.setText(gameController.toString());
            add(tmp);

            // add(new JLabel("Select Snake Head: row"));
            // int max_size = gameController.field.getRowsNum() - 1;
            // final ParamSlider sliderRow = new ParamSlider(0, max_size, 5);
            // add(sliderRow);
            //
            // add(new JLabel("Select Snake Head: col"));
            // max_size = gameController.field.getColsNum() - 1;
            // final ParamSlider sliderCol = new ParamSlider(0, max_size, 5);
            // add(sliderCol);

            add(new JLabel("Select Snake size"));
            int max_size = 3 + gameController.field.getEffectiveSize() / 30;
            final ParamSlider sliderSize = new ParamSlider(1, max_size, 3);
            add(sliderSize);

            add(new JLabel("Select Snake direction"));
            final ParamDirection list = new ParamDirection();
            add(list);

            JButton btnSave = new JButton("Save");
            btnSave.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        caret = tmp.getCaretPosition();
                        int row = caret / (fieldSize.col + 1);
                        int col = caret % (fieldSize.col + 1);

                        gameController.initSnake(new TextPoint(row, col), list.getSelectedValue(),
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
            super();
            setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

            add(new JLabel("Sel number of Stars"));
            int max_size = 5 + gameController.field.getEffectiveSize() / 20;
            final ParamSlider slider = new ParamSlider(1, max_size, 5);
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

    class ParamSlider extends JSlider {
        /**
         * 
         */
        private static final long serialVersionUID = -6772332188129643119L;

        public ParamSlider(int min, int max, int cur) {
            super(SwingConstants.HORIZONTAL, min, max, cur);
            setMinorTickSpacing(1);
            setMajorTickSpacing(cur);
            setPaintTicks(true);
            setPaintLabels(true);
        }
    }

    class ParamDirection extends JList<Direction> {
        /**
         * 
         */
        private static final long serialVersionUID = -3257361818985306762L;

        public ParamDirection() {
            setListData(Direction.values());
            setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            setSelectedIndex(0);
        }
    }
}
