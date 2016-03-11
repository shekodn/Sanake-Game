
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.*;
import java.util.*;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * The {@code SnakeGame} class is responsible for handling much of the game's
 * logic.
 *
 * @author Sergio Diaz A01192313
 * @author Ana Karen Beltran A01192508
 *
 */
public class SnakeGame extends JFrame implements KeyListener {

    /**
     * The Serial Version UID.
     */
    private static final long serialVersionUID = 6678292058307426314L;

    /**
     * The number of milliseconds that should pass between each frame.
     */
    private static final long FRAME_TIME = 1000L / 50L;

    /**
     * The minimum length of the snake. This allows the snake to grow right when
     * the game starts, so that we're not just a head moving around on the
     * board.
     */
    private static final int MIN_SNAKE_LENGTH = 5;

    /**
     * The maximum number of directions that we can have polled in the direction
     * list.
     */
    private static final int MAX_DIRECTIONS = 3;

    /**
     * The BoardPanel instance.
     */
    private BoardPanel board;

    /**
     * The SidePanel instance.
     */
    private SidePanel side;

    /**
     * The random number generator (used for spawning fruits).
     */
    private Random random;

    /**
     * The Clock instance for handling the game logic.
     */
    private Clock logicTimer;

    /**
     * Whether or not we're running a new game.
     */
    private boolean isNewGame;

    /**
     * Whether or not the game is over.
     */
    private boolean isGameOver;

    /**
     * Whether or not the game is paused.
     */
    private boolean isPaused;

    /**
     * The list that contains the points for the snake.
     */
    private LinkedList<Point> snake;

    /**
     * The list that contains the queued directions.
     */
    private LinkedList<Direction> directions;

    /**
     * The current file for loading a game.
     */
    private String sNombreArchivo;

    /**
     * The current score.
     */
    private int score;

    /**
     * The number of fruits that we've eaten.
     */
    private int fruitsEaten;

    /**
     * The number of points that the next fruit will award us.
     */
    private int nextFruitScore;

    /**
     * Variable used to control the for depending of the eaten piece.
     */
    private int iCounter;

    /**
     * Variable used to control the color of the snake.
     */
    public int iSnakeColorCounter;

    /**
     * Variable used to control the color of the snake.
     */
    private int iSnakeTimer;
    /*
     * Audios
     */
    private SoundClip souBackgroundMusic;
    private SoundClip souEatGood;
    private SoundClip souEatBad;

    /**
     * Creates a new SnakeGame instance. Creates a new window, and sets up the
     * controller input.
     */
    private SnakeGame() {
        super("Snake Remake");
        setLayout(new BorderLayout());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        //Initizlizes loop for Background Music
        souBackgroundMusic = new SoundClip("audio/BackgroundB.wav");
        souBackgroundMusic.setLooping(true);

        //Initizlizes sounds when the snake eats an object
        souEatGood = new SoundClip("audio/beep1.wav");
        souEatBad = new SoundClip("audio/gunshot3.wav");

        /**
         * Initialize the game's panels and add them to the window.
         */
        this.board = new BoardPanel(this);
        this.side = new SidePanel(this);

        add(board, BorderLayout.CENTER);
        add(side, BorderLayout.EAST);
        iSnakeColorCounter = 1;
        iSnakeTimer = 0;

        addKeyListener(this);

        /**
         * Resize the window to the appropriate size, center it on the screen
         * and display it.
         */
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * Starts the game running.
     */
    private void startGame() {

        /**
         * Initialize everything we're going to be using.
         */
        this.random = new Random();
        this.snake = new LinkedList<>();
        this.directions = new LinkedList<>();
        this.logicTimer = new Clock(9.0f);
        this.isNewGame = true;
        sNombreArchivo = "load.dat";

        //Set the timer to paused initially.
        logicTimer.setPaused(true);

        /**
         * This is the game loop. It will update and render the game and will
         * continue to run until the game window is closed.
         */
        while (true) {
            //Get the current frame's start time.
            long start = System.nanoTime();

            //Update the logic timer.
            logicTimer.update();

            /**
             *
             * If a cycle has elapsed on the logic timer, then update the game.
             */
            if (logicTimer.hasElapsedCycle()) {
                updateGame();
            }

            snakeTimer();

            //Repaint the board and side panel with the new content.
            board.repaint();
            side.repaint();

            /**
             * Calculate the delta time between since the start of the frame and
             * sleep for the excess time to cap the frame rate. While not
             * incredibly accurate, it is sufficient for our purposes.
             */
            long delta = (System.nanoTime() - start) / 1000000L;
            if (delta < FRAME_TIME) {
                try {
                    Thread.sleep(FRAME_TIME - delta);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Updates the game's logic.
     */
    private void updateGame() {

        /**
         * Gets the type of tile that the head of the snake collided with. If
         * the snake hit a wall, SnakeBody will be returned, as both conditions
         * are handled identically.
         *
         */
        TileType collision = updateSnake();

        /**
         * Here we handle the different possible collisions.
         *
         * Fruit: If we collided with a fruit, we increment the number of fruits
         * that we've eaten, update the score, and spawn a new fruit.
         *
         * Fruit 2: If we collided with a fruit2, we increment the number of
         * fruits that we've eaten by 2
         *
         * Fruit 3: If we collided with a fruit3, we increment the number of
         * fruits that we've eaten by 3
         *
         * badFruit:If we collided with a badFruit, it's game over
         *
         * SnakeBody: If we collided with our tail (or a wall), we flag that the
         * game is over and pause the game. If no collision occurred, we simply
         * decrement the number of points that the next fruit will give us if
         * it's high enough. This adds a bit of skill to the game as collecting
         * fruits more quickly will yield a higher score.
         */
        if (collision == TileType.Fruit) {
            fruitsEaten++;
            score += nextFruitScore;
            spawnFruit();
            souEatGood.play();

        } else if (collision == TileType.Fruit2) {
            fruitsEaten += 2;
            score += nextFruitScore;
            spawnFruit2();
            souEatGood.play();

        } else if (collision == TileType.Fruit3) {
            fruitsEaten += 3;
            score += nextFruitScore;
            spawnFruit3();
            souEatGood.play();

        } else if (collision == TileType.FruitZero) {
            score = 0;
            spawnFruitZero();
            souEatBad.play();

        } else if (collision == TileType.SnakeBody || collision == 
                TileType.badFruit) {
            isGameOver = true;
            logicTimer.setPaused(true);
            souEatBad.play();
            souBackgroundMusic.stop();

        } else if (nextFruitScore > 10) {
            nextFruitScore--;
        }
    }

    /**
     * Updates the snake's position and size.
     *
     * @return Tile tile that the head moved into.
     */
    private TileType updateSnake() {

        /**
         * Here we peek at the next direction rather than polling it. While not
         * game breaking, polling the direction here causes a small bug where
         * the snake's direction will change after a game over (though it will
         * not move).
         */
        Direction direction = directions.peekFirst();

        /**
         * Here we calculate the new point that the snake's head will be at
         * after the update.
         */
        Point head = new Point(snake.peekFirst());
        switch (direction) {
            case North:
                head.y--;
                break;

            case South:
                head.y++;
                break;

            case West:
                head.x--;
                break;

            case East:
                head.x++;
                break;
        }

        /**
         * If the snake has moved out of bounds ('hit' a wall), we can just
         * return that it's collided with itself, as both cases are handled
         * identically.
         */
        if (head.x < 0 || head.x >= BoardPanel.COL_COUNT || head.y < 0
                || head.y >= BoardPanel.ROW_COUNT) {
            return TileType.SnakeBody; //Pretend we collided with our body.
        }

        /**
         * Here we get the tile that was located at the new head position and
         * remove the tail from of the snake and the board if the snake is long
         * enough, and the tile it moved onto is not a fruit.
         *
         * If the tail was removed, we need to retrieve the old tile again
         * incase the tile we hit was the tail piece that was just removed to
         * prevent a false game over.
         */
        TileType old = board.getTile(head.x, head.y);

        //Set counter to to know how many peices we have eaten. 
        setCounter(old);

        old = moveSnake(old, head);

        return old;
    }

    /**
     * This counter determines how many tiles should be created. If the player
     * eats a red piece the tail will increase by 1. If the player eats a green
     * piece the tail will increase by 2. If the player eats a blue piece the
     * tail will increase by 3.
     */
    public void setCounter(TileType old) {
        iCounter = 0;

        if (old == TileType.Fruit) {

            iCounter = 0;
        }

        if (old == TileType.Fruit2) {

            iCounter = 1;
        }

        if (old == TileType.Fruit3) {

            iCounter = 2;
        }

        if (old == TileType.FruitZero) {

            iCounter = 0;
        }
    }
    
    public TileType moveSnake(TileType old, Point head){
        for (int iI = 0; iI <= iCounter; iI++) {

            if (old != TileType.Fruit && old != TileType.Fruit2 && old
                    != TileType.Fruit3 && snake.size() > MIN_SNAKE_LENGTH) {
                Point tail = snake.removeLast();
                board.setTile(tail, null);

                old = board.getTile(head.x, head.y);
            }

            /**
             * Update the snake's position on the board if we didn't collide
             * with our tail:
             *
             * 1. Set the old head position to a body tile. 2. Add the new head
             * to the snake. 3. Set the new head position to a head tile.
             *
             * If more than one direction is in the queue, poll it to read new
             * input.
             */
            if (old != TileType.SnakeBody) {
                board.setTile(snake.peekFirst(), TileType.SnakeBody);

                snake.push(head);
                board.setTile(head, TileType.SnakeHead);

                if (directions.size() > 1) {
                    directions.poll();
                }
            }

        }
        return old;
    }
    
    /**
     * Resets the game's variables to their default states and starts a new
     * game.
     */
    private void resetGame() {
        /**
         * Reset the score statistics. (Note that nextFruitPoints is reset in
         * the spawnFruit function later on).
         */
        this.score = 0;
        this.fruitsEaten = 0;
        this.iSnakeColorCounter = 1;
        /**
         * Reset both the new game and game over flags.
         */
        this.isNewGame = false;
        this.isGameOver = false;

        /**
         * Create the head at the center of the board.
         */
        Point head = new Point(BoardPanel.COL_COUNT / 2, BoardPanel.ROW_COUNT
                / 2);

        /**
         * Clear the snake list and add the head.
         */
        snake.clear();
        snake.add(head);

        /**
         * Clear the board and add the head.
         */
        board.clearBoard();
        board.setTile(head, TileType.SnakeHead);

        /**
         * Clear the directions and add north as the default direction.
         */
        directions.clear();
        directions.add(Direction.North);

        /**
         * Reset the logic timer.
         */
        logicTimer.reset();

        /**
         * Spawn a type of fruit.
         */
        spawnFruit();
        spawnFruit2();
        spawnFruit3();
        spawnBadFruit();
        spawnFruitZero();
    }

    /**
     * Gets the flag that indicates whether or not we're playing a new game.
     *
     * @return The new game flag.
     */
    public boolean isNewGame() {
        return isNewGame;
    }

    /**
     * Gets the flag that indicates whether or not the game is over.
     *
     * @return The game over flag.
     */
    public boolean isGameOver() {
        return isGameOver;
    }

    /**
     * Gets the flag that indicates whether or not the game is paused.
     *
     * @return The paused flag.
     */
    public boolean isPaused() {
        return isPaused;
    }
    
    /**
     * Handles timer that controls the change of color of the snake
     */
    public void snakeTimer() {
        iSnakeTimer++;

        if (iSnakeTimer >= 30) {
            iSnakeColorCounter++;
            if (iSnakeColorCounter > 3) {
                iSnakeColorCounter = 1;
            }
            iSnakeTimer = 0;
        }
    }

    /**
     * Spawns a new fruit onto the board.
     */
    private void spawnFruit() {
        //Reset the score for this fruit to 100.
        this.nextFruitScore = 100;

        /**
         * Get a random index based on the number of free spaces left on the
         * board.
         */
        int index = random.nextInt(BoardPanel.COL_COUNT * BoardPanel.ROW_COUNT
                - snake.size());

        /**
         * While we could just as easily choose a random index on the board and
         * check it if it's free until we find an empty one, that method tends
         * to hang if the snake becomes very large.
         *
         * This method simply loops through until it finds the nth free index
         * and selects uses that. This means that the game will be able to
         * locate an index at a relatively constant rate regardless of the size
         * of the snake.
         */
        int freeFound = -1;
        for (int x = 0; x < BoardPanel.COL_COUNT; x++) {
            for (int y = 0; y < BoardPanel.ROW_COUNT; y++) {
                TileType type = board.getTile(x, y);
                if (type == null || type == TileType.Fruit) {
                    if (++freeFound == index) {
                        board.setTile(x, y, TileType.Fruit);
                        break;
                    }
                }
            }
        }
    }

    /**
     * Spawns a new fruit onto the board.
     */
    private void spawnFruit2() {
        //Reset the score for this fruit to 100.
        this.nextFruitScore = 100;

        /**
         *
         * Get a random index based on the number of free spaces left on the
         * board.
         */
        int index = random.nextInt(BoardPanel.COL_COUNT * BoardPanel.ROW_COUNT
                - snake.size());

        /**
         * While we could just as easily choose a random index on the board and
         * check it if it's free until we find an empty one, that method tends
         * to hang if the snake becomes very large.
         *
         * This method simply loops through until it finds the nth free index
         * and selects uses that. This means that the game will be able to
         * locate an index at a relatively constant rate regardless of the size
         * of the snake.
         */
        int freeFound = -1;
        for (int x = 0; x < BoardPanel.COL_COUNT; x++) {
            for (int y = 0; y < BoardPanel.ROW_COUNT; y++) {
                TileType type = board.getTile(x, y);
                if (type == null || type == TileType.Fruit2) {
                    if (++freeFound == index) {
                        board.setTile(x, y, TileType.Fruit2);

                        break;
                    }
                }
            }
        }
    }

    /**
     * Spawns a new fruit type 3 onto the board.
     */
    private void spawnFruit3() {
        //Reset the score for this fruit to 100.
        this.nextFruitScore = 100;
        /**
         * Get a random index based on the number of free spaces left on the
         * board.
         */
        int index = random.nextInt(BoardPanel.COL_COUNT * BoardPanel.ROW_COUNT
                - snake.size());

        /**
         * While we could just as easily choose a random index on the board and
         * check it if it's free until we find an empty one, that method tends
         * to hang if the snake becomes very large.
         *
         * This method simply loops through until it finds the nth free index
         * and selects uses that. This means that the game will be able to
         * locate an index at a relatively constant rate regardless of the size
         * of the snake.
         */
        int freeFound = -1;
        for (int x = 0; x < BoardPanel.COL_COUNT; x++) {
            for (int y = 0; y < BoardPanel.ROW_COUNT; y++) {
                TileType type = board.getTile(x, y);
                if (type == null || type == TileType.Fruit3) {
                    if (++freeFound == index) {
                        board.setTile(x, y, TileType.Fruit3);

                        break;
                    }
                }
            }
        }
    }

    /**
     * Spawns a new fruit onto the board.
     */
    private void spawnBadFruit() {
        //Reset the score for this fruit to 100.
        this.nextFruitScore = 100;

        /**
         * Get a random index based on the number of free spaces left on the
         * board.
         */
        int index = random.nextInt(BoardPanel.COL_COUNT * BoardPanel.ROW_COUNT
                - snake.size());

        /**
         * While we could just as easily choose a random index on the board and
         * check it if it's free until we find an empty one, that method tends
         * to hang if the snake becomes very large.
         *
         * This method simply loops through until it finds the nth free index
         * and selects uses that. This means that the game will be able to
         * locate an index at a relatively constant rate regardless of the size
         * of the snake.
         */
        int freeFound = -1;
        for (int x = 0; x < BoardPanel.COL_COUNT; x++) {
            for (int y = 0; y < BoardPanel.ROW_COUNT; y++) {
                TileType type = board.getTile(x, y);
                if (type == null || type == TileType.badFruit) {
                    if (++freeFound == index) {
                        board.setTile(x, y, TileType.badFruit);
                        break;
                    }
                }
            }
        }
    }

    /**
     * Spawns a new fruit onto the board.
     */
    private void spawnFruitZero() {
        //Reset the score for this fruit to 100.
        this.nextFruitScore = 100;

        /**
         * Get a random index based on the number of free spaces left on the
         * board.
         */
        int index = random.nextInt(BoardPanel.COL_COUNT * BoardPanel.ROW_COUNT
                - snake.size());

        /**
         * While we could just as easily choose a random index on the board and
         * check it if it's free until we find an empty one, that method tends
         * to hang if the snake becomes very large.
         *
         * This method simply loops through until it finds the nth free index
         * and selects uses that. This means that the game will be able to
         * locate an index at a relatively constant rate regardless of the size
         * of the snake.
         */
        int freeFound = -1;
        for (int x = 0; x < BoardPanel.COL_COUNT; x++) {
            for (int y = 0; y < BoardPanel.ROW_COUNT; y++) {
                TileType type = board.getTile(x, y);
                if (type == null || type == TileType.FruitZero) {
                    if (++freeFound == index) {
                        board.setTile(x, y, TileType.FruitZero);
                        break;
                    }
                }
            }
        }
    }

    /**
     * Gets the current score.
     *
     * @return The score.
     */
    public int getScore() {
        return score;
    }

    /**
     * Gets the number of fruits eaten.
     *
     * @return The fruits eaten.
     */
    public int getFruitsEaten() {
        return fruitsEaten;
    }

    /**
     * Gets the next fruit score.
     *
     * @return The next fruit score.
     */
    public int getNextFruitScore() {
        return nextFruitScore;
    }

    /**
     * Gets the current direction of the snake.
     *
     * @return The current direction.
     */
    public Direction getDirection() {
        return directions.peek();
    }

    /**
     * Sets the current direction of the snake.
     *
     */
    public void setDirection(Direction directionsP) {
        directions.clear();
        this.directions.push(directionsP);
    }

    /**
     * Gets the current list of the snake.
     *
     * @return The current lkl snake.
     */
    public LinkedList<Point> getSnake() {
        return snake;
    }

    /**
     * Sets the new list of the snake.
     *
     */
    public void setSnake(LinkedList<Point> snake) {
        this.snake = snake;
    }

    /**
     * Metodo que agrega la informacion del vector al archivo.
     *
     * @throws IOException
     */
    public void grabaArchivo() throws IOException {
        ObjectOutputStream fpwArchivo = new ObjectOutputStream(new 
            BufferedOutputStream(new FileOutputStream(sNombreArchivo)));

        fpwArchivo.writeInt(getScore());
        fpwArchivo.writeInt(getNextFruitScore());
        fpwArchivo.writeInt(getFruitsEaten());
        fpwArchivo.writeBoolean(isGameOver);
        fpwArchivo.writeBoolean(isNewGame);

        //linkedlist direccion
        fpwArchivo.writeObject(this.getDirection());
        //linkedlist snake
        fpwArchivo.writeObject(getSnake());
        //guarda length array
        int matStatus[] = board.getMatrix();
        //guarda tiles
        fpwArchivo.writeInt(matStatus.length);
        for (int iR = 0; iR < matStatus.length; iR++) {
            fpwArchivo.writeInt(matStatus[iR]);
        }
        fpwArchivo.close();
    }

    /**
     * Metodo que agrega la informacion del vector al archivo.
     *
     * @throws IOException
     */
    public void leeArchivo() throws IOException, ClassNotFoundException {
        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter stdOut = new PrintWriter(System.out, true);

        try {
            // asking user the name of the user
            //System.out.print("Ingresa el nombre del usuario:");
            String sNombreArchivo = JOptionPane.showInputDialog("Cual es tu nombre?");
//                JOptionPane.showMessageDialog(null, 
//                              "El puntaje de " + nombre + " es: " + score, "PUNTAJE", 
//                              JOptionPane.PLAIN_MESSAGE);
            System.out.print(sNombreArchivo);
            // opening file
            ObjectInputStream finArchivo = new ObjectInputStream(new 
                BufferedInputStream(new FileInputStream(sNombreArchivo
                    + ".dat")));
            boolean flag = true;
            try {
                // reading data from file & transfering it to variables
                this.score = finArchivo.readInt();
                this.nextFruitScore = finArchivo.readInt();
                this.fruitsEaten = finArchivo.readInt();
                this.isGameOver = finArchivo.readBoolean();
                this.isNewGame = finArchivo.readBoolean();

                //snake.clear();
                //linkedlist direccion
                this.setDirection((Direction) finArchivo.readObject());

                //linkedlist snake
                this.setSnake((LinkedList) finArchivo.readObject());

                int i = finArchivo.readInt();
                int matBoard[] = new int[i];

                //lee tiles
                for (int iR = 0; iR < i; iR++) {
                    matBoard[iR] = finArchivo.readInt();
                }

                //snake.clear();
                board.clearBoard();
                board.setMatrix(matBoard);
                finArchivo.close();// closing file
            } catch (EOFException e) {
            } catch (ClassNotFoundException ex) {
                stdOut.println(ex.getMessage());
            }
            //inFile.close(); // closing file
        } catch (Exception e) {
            stdOut.println(e.getMessage());
            // file not found exception
            System.out.println("That user does not exist.");
            // user must create a new file
        }
        System.out.println(" ");
    }

    /**
     * Entry point of the program.
     *
     * @param args Unused.
     */
    public static void main(String[] args) {
        SnakeGame snake = new SnakeGame();
        snake.startGame();
    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            /**
             * If the game is not paused, and the game is not over... move up
             */
            case KeyEvent.VK_W:
            case KeyEvent.VK_UP:
                if (!isPaused && !isGameOver) {
                    moveUp();
                }
                break;
            /**
             * If the game is not paused, and the game is not over... move down
             */
            case KeyEvent.VK_S:
            case KeyEvent.VK_DOWN:
                if (!isPaused && !isGameOver) {
                    moveDown();
                }
                break;
            /**
             * If the game is not paused, and the game is not over... move left
             */
            case KeyEvent.VK_A:
            case KeyEvent.VK_LEFT:
                if (!isPaused && !isGameOver) {
                    moveLeft();
                }
                break;
            /**
             * If the game is not paused, and the game is not over... move right
             */
            case KeyEvent.VK_D:
            case KeyEvent.VK_RIGHT:
                if (!isPaused && !isGameOver) {
                    moveRight();
                }
                break;
            /**
             * If the game is not over, pause.
             */
            case KeyEvent.VK_P:
                if (!isGameOver) {
                    pauseGame();
                }
                break;
            /**
             * Reset the game if one is not currently in progress.
             */
            case KeyEvent.VK_ENTER:
                if (isNewGame || isGameOver) {
                    souBackgroundMusic.play();
                    resetGame();
                }
                break;
            /**
             * Saves game into a .dat file
             */
            case KeyEvent.VK_G: {
                if (!isGameOver && !isPaused) {
                    try {
                        grabaArchivo();
                    } catch (IOException ex) {
                        System.out.println(ex);
                    }
                }
            }
            break;
            /**
             * Reads game from a .dat file
             */
            case KeyEvent.VK_C: {
                if (!isGameOver && !isPaused) {
                    try {
                        leeArchivo();
                    } catch (IOException ex) {
                        System.out.println(ex);
                    } catch (ClassNotFoundException ex) {
                        System.out.println(ex);
                    }
                }
            }
            break;
        }
    }

    public void keyReleased(KeyEvent e) {
    }

    /**
     * Ensure that the direction list is not full, and that the most recent
     * direction is adjacent to North before adding the direction to the list.
     */
    public void moveUp() {
        if (directions.size() < MAX_DIRECTIONS) {
            Direction last = directions.peekLast();
            if (last != Direction.South && last != Direction.North) {
                directions.addLast(Direction.North);
            }
        }
    }

    /**
     * Ensure that the direction list is not full, and that the most recent
     * direction is adjacent to South before adding the direction to the list.
     */
    public void moveDown() {
        if (directions.size() < MAX_DIRECTIONS) {
            Direction last = directions.peekLast();
            if (last != Direction.North && last != Direction.South) {
                directions.addLast(Direction.South);
            }
        }
    }

    /**
     * Ensure that the direction list is not full, and that the most recent
     * direction is adjacent to West before adding the direction to the list.
     */
    public void moveLeft() {
        if (directions.size() < MAX_DIRECTIONS) {
            Direction last = directions.peekLast();
            if (last != Direction.East && last != Direction.West) {
                directions.addLast(Direction.West);
            }
        }
    }

    /**
     * Ensure that the direction list is not full, and that the most recent
     * direction is adjacent to East before adding the direction to the list.
     */
    public void moveRight() {
        if (directions.size() < MAX_DIRECTIONS) {
            Direction last = directions.peekLast();
            if (last != Direction.West && last != Direction.East) {
                directions.addLast(Direction.East);
            }
        }
    }

    /**
     * toggle the paused flag and update the logicTimer's pause flag accordingly
     */
    public void pauseGame() {
        isPaused = !isPaused;
        logicTimer.setPaused(isPaused);

        if (isPaused) {
            souBackgroundMusic.stop();
        } else {
            souBackgroundMusic.play();
        }
    }
}
