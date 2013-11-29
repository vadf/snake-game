import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class Game {
    private static final String   defaultSingle = "src/main/resources/single_game.config";
    private static final String   defaultMulti  = "src/main/resources/multi_game.config";

    private GameType              gameType      = GameType.SINGLE;
    private static GameController gameController;
    private TextPoint             fieldSize;
    private JFrame                frame;
    private Timer                 timer;
    private GameArea              gameArea;
    private JLabel                score, lStatus;
    private GameStatus            eStatus;

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
        timer = new Timer(1000, this.new MoveTask());
        eStatus = GameStatus.LOAD_GAME;
    }

    public void initGameField(int rows, int cols) {
        gameArea = new GameArea(rows, cols);
        frame.add(gameArea, BorderLayout.CENTER);

        score = new JLabel("Score " + gameController.getScore());
        frame.add(score, BorderLayout.SOUTH);

        lStatus = new JLabel(eStatus.toString());
        frame.add(lStatus, BorderLayout.NORTH);

        frame.pack();
    }

    public void loadGame() {
        frame = new JFrame("Snake Game Loader");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel pLoad = new JPanel();
        pLoad.setLayout(new BoxLayout(pLoad, BoxLayout.Y_AXIS));

        ButtonGroup group = new ButtonGroup();
        JRadioButton rbSingle = new JRadioButton("Single Snake classic");
        rbSingle.setSelected(true);
        JRadioButton rbMulti = new JRadioButton("Two Snakes");
        group.add(rbSingle);
        group.add(rbMulti);
        Dimension btnSize = new Dimension(200, 25);
        JButton btnDefault = new JButton("Default Game");
        JButton btnGame = new JButton("Game Constructor");
        JButton btnStart = new JButton("Start Game");
        JButton btnPause = new JButton("Pause Game");
        JButton btnQuit = new JButton("Quit");
        btnDefault.setMaximumSize(btnSize);
        btnGame.setMaximumSize(btnSize);
        btnStart.setMaximumSize(btnSize);
        btnPause.setMaximumSize(btnSize);
        btnQuit.setMaximumSize(btnSize);

        rbSingle.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameType = GameType.SINGLE;
                gameController.setType(gameType);
            }
        });
        rbMulti.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameType = GameType.MULTI;
                gameController.setType(gameType);
            }
        });
        btnDefault.addActionListener(this.new DefaultGame());
        btnGame.addActionListener(this.new LoadGame());
        btnStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (eStatus == GameStatus.READY_TO_PLAY || eStatus == GameStatus.PAUSED) {
                    gameArea.setText(gameController.toString());
                    eStatus = GameStatus.PLAYING;
                    gameArea.requestFocusInWindow();
                    lStatus.setText(eStatus.toString());
                    timer.start();
                }
            }
        });

        btnPause.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (eStatus == GameStatus.PLAYING) {
                    eStatus = GameStatus.PAUSED;
                    timer.stop();
                    lStatus.setText(eStatus.toString());
                }
            }
        });

        btnQuit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });

        pLoad.add(rbSingle);
        pLoad.add(rbMulti);
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
                if (gameType == GameType.SINGLE) {
                    fieldSize = gameController.initFromConfig(defaultSingle);
                } else {
                    fieldSize = gameController.initFromConfig(defaultMulti);
                }
                initGameField(fieldSize.row, fieldSize.col);
            } catch (IOException | OutOfFieldException | SnakeOnWallException
                    | TeleportInitException | FieldInitException | SnakeCollisionException e1) {
                eStatus = GameStatus.LOAD_ERROR;
                lStatus.setText(eStatus + ": " + e1.getMessage());
                System.err.println(e1.getMessage());
            }
            if (eStatus != GameStatus.LOAD_ERROR) {
                eStatus = GameStatus.READY_TO_PLAY;
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
                    new PortParams();
                    gameArea.setText(gameController.toString());
                    new SnakeParams();
                    gameArea.setText(gameController.toString());
                    new StarsParams();
                    gameArea.setText(gameController.toString());
                } catch (IOException | FieldInitException e1) {
                    eStatus = GameStatus.LOAD_ERROR;
                    lStatus.setText(eStatus + ": " + e1.getMessage());
                    System.err.println(e1.getMessage());
                }
                if (eStatus != GameStatus.LOAD_ERROR) {
                    eStatus = GameStatus.READY_TO_PLAY;
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
                    eStatus = GameStatus.CONTRATULATIONS;
                    lStatus.setText(eStatus.toString());
                    System.out.println("Congratulations");
                    timer.stop();
                }
                gameArea.setText(gameController.toString());
            } catch (SnakeOnWallException | OutOfFieldException | SnakeAddException
                    | SnakeCollisionException e1) {
                eStatus = GameStatus.GAME_OVER;
                lStatus.setText(eStatus + ": " + e1.getMessage());
                System.err.println(e1.getMessage());
                timer.stop();
            }
            score.setText("Score " + gameController.getScore());
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
            tmp.setAlignmentX(LEFT_ALIGNMENT);
            add(tmp);

            add(new JLabel("Select Snake size"));
            int maxSize = 3 + gameController.field.getEffectiveSize() / 30;
            final ParamSlider sliderSize = new ParamSlider(1, maxSize, 3);
            sliderSize.setAlignmentX(LEFT_ALIGNMENT);
            add(sliderSize);

            add(new JLabel("Select Snake direction"));
            final ParamDirection list = new ParamDirection();
            list.setAlignmentX(LEFT_ALIGNMENT);
            add(list);

            JButton btnSave = new JButton("Save");
            btnSave.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        caret = tmp.getCaretPosition();
                        int row = caret / (fieldSize.col + 1);
                        int col = caret % (fieldSize.col + 1);

                        gameController.initSnake(1, new TextPoint(row, col),
                                list.getSelectedValue(), sliderSize.getValue());
                        eStatus = GameStatus.LOAD_GAME;
                        lStatus.setText(eStatus + ": Sanke Init Done");
                        dispose();
                    } catch (OutOfFieldException | SnakeOnWallException | SnakeCollisionException e1) {
                        eStatus = GameStatus.LOAD_ERROR;
                        lStatus.setText(eStatus + ": " + e1.getMessage());
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

            add(new JLabel("Set number of Stars"));
            int maxSize = 5 + gameController.field.getEffectiveSize() / 20;
            final ParamSlider slider = new ParamSlider(1, maxSize, 5);
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

    class PortParams extends JDialog {
        int                           caret            = 0;
        int                           count            = 0;
        HashMap<TextPoint, Direction> ports            = new HashMap<TextPoint, Direction>();
        /**
         * 
         */
        private static final long     serialVersionUID = 3538062280708782118L;

        public PortParams() {
            super();
            setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

            add(new JLabel("Set Port position"));
            final GameArea tmp = new GameArea(fieldSize.row, fieldSize.col);
            tmp.setText(gameController.toString());
            tmp.setAlignmentX(LEFT_ALIGNMENT);
            add(tmp);

            add(new JLabel("Select Port out direction"));
            final ParamDirection list = new ParamDirection();
            list.setAlignmentX(LEFT_ALIGNMENT);
            add(list);

            JButton btnPort = new JButton("Add Port");
            add(btnPort);

            final DefaultListModel<String> listModel = new DefaultListModel<String>();
            final JList<String> portList = new JList<String>(listModel);
            portList.setVisibleRowCount(5);
            JScrollPane scrollPane = new JScrollPane(portList);
            scrollPane.setAlignmentX(LEFT_ALIGNMENT);
            add(scrollPane);

            final JButton btnInit = new JButton("Init Teleport " + count);
            add(btnInit);

            JButton btnClose = new JButton("Close/Skip");
            add(btnClose);

            btnClose.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    dispose();
                }
            });

            btnPort.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    caret = tmp.getCaretPosition();
                    int row = caret / (fieldSize.col + 1);
                    int col = caret % (fieldSize.col + 1);
                    ports.put(new TextPoint(row, col), list.getSelectedValue());
                    listModel.addElement(new TextPoint(row, col) + ": " + list.getSelectedValue());
                }
            });

            btnInit.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        gameController.initPorts(ports);
                        ports.clear();
                        listModel.clear();
                        count++;
                        btnInit.setText("Init Teleport " + count);
                        tmp.setText(gameController.toString());
                        eStatus = GameStatus.LOAD_GAME;
                        lStatus.setText(eStatus + ": Ports Init Done");
                    } catch (TeleportInitException | OutOfFieldException e1) {
                        eStatus = GameStatus.LOAD_ERROR;
                        lStatus.setText(eStatus + ": " + e1.getMessage());
                        System.err.println(e1.getMessage());
                        ports.clear();
                        listModel.clear();
                    }
                }
            });

            pack();

            setModalityType(ModalityType.APPLICATION_MODAL);
            setTitle("Select Snake params");
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

enum GameStatus {
    PLAYING, PAUSED, LOAD_GAME, READY_TO_PLAY, CONTRATULATIONS, GAME_OVER, LOAD_ERROR
}
