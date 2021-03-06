
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.net.URL;

import javax.swing.JPanel;

/**
 * The {@code BoardPanel} class is responsible for managing and displaying the
 * contents of the game board.
 *
 * @author Sergio Diaz A01192313
 * @author Ana Karen Beltran A01192508
 * Version: 1.0 entregada
 *
 */
public class BoardPanel extends JPanel {

    /**
     * Serial Version UID.
     */
    private static final long serialVersionUID = -1102632585936750607L;

    /**
     * The number of columns on the board. (Should be odd so we can start in the
     * center).
     */
    public static final int COL_COUNT = 25;

    /**
     * The number of rows on the board. (Should be odd so we can start in the
     * center).
     */
    public static final int ROW_COUNT = 25;

    /**
     * The size of each tile in pixels.
     */
    public static final int TILE_SIZE = 20;

    /**
     * The number of pixels to offset the eyes from the sides.
     */
    private static final int EYE_LARGE_INSET = TILE_SIZE / 3;

    /**
     * The number of pixels to offset the eyes from the front.
     */
    private static final int EYE_SMALL_INSET = TILE_SIZE / 6;

    /**
     * The length of the eyes from the base (small inset).
     */
    private static final int EYE_LENGTH = TILE_SIZE / 5;

    /**
     * The font to draw the text with.
     */
    private static final Font FONT = new Font("Tahoma", Font.BOLD, 25);

    /**
     * The SnakeGame instance.
     */
    private SnakeGame game;

    /**
     * The array of tiles that make up this board.
     */
    private TileType[] tiles;

    /**
     * Images of fruits and bad fruits
     */
    public Image imaApple;
    public Image imaCucumber;
    public Image imaKiwi;
    public Image imaBlueBerry;
    public Image imaOrange;

    /**
     * Creates a new BoardPanel instance.
     *
     * @param game The SnakeGame instance.
     */
    public BoardPanel(SnakeGame game) {
        this.game = game;
        this.tiles = new TileType[ROW_COUNT * COL_COUNT];

        setPreferredSize(new Dimension(COL_COUNT * TILE_SIZE, ROW_COUNT
                * TILE_SIZE));
        setBackground(Color.WHITE);
    }

    /**
     * Clears all of the tiles on the board and sets their values to null.
     */
    public void clearBoard() {
        for (int i = 0; i < tiles.length; i++) {
            tiles[i] = null;
        }
    }

    /**
     * Sets the tile at the desired coordinate.
     *
     * @param point The coordinate of the tile.
     * @param type The type to set the tile to.
     */
    public void setTile(Point point, TileType type) {
        setTile(point.x, point.y, type);
    }

    /**
     * Sets the tile at the desired coordinate.
     *
     * @param x The x coordinate of the tile.
     * @param y The y coordinate of the tile.
     * @param type The type to set the tile to.
     */
    public void setTile(int x, int y, TileType type) {
        tiles[y * ROW_COUNT + x] = type;
    }

    /**
     * Gets the tile at the desired coordinate.
     *
     * @param x The x coordinate of the tile.
     * @param y The y coordinate of the tile.
     * @return
     */
    public TileType getTile(int x, int y) {
        return tiles[y * ROW_COUNT + x];
    }

    /**
     * Loop through each tile on the board and draw it if it is not null.
     */
    public void drawTile(Graphics g) {

        for (int x = 0; x < COL_COUNT; x++) {
            for (int y = 0; y < ROW_COUNT; y++) {
                TileType type = getTile(x, y);
                if (type != null) {
                    drawTile(x * TILE_SIZE, y * TILE_SIZE, type, g);
                }
            }
        }
    }

    /**
     * Draw the grid on the board. This makes it easier to see exactly where we
     * in relation to the fruit.
     *
     * The panel is one pixel too small to draw the bottom and right outlines,
     * so we outline the board with a rectangle separately.
     */
    public void drawGrid(Graphics g) {

        g.setColor(Color.DARK_GRAY);
        g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
        for (int x = 0; x < COL_COUNT; x++) {
            for (int y = 0; y < ROW_COUNT; y++) {
                g.drawLine(x * TILE_SIZE, 0, x * TILE_SIZE, getHeight());
                g.drawLine(0, y * TILE_SIZE, getWidth(), y * TILE_SIZE);
            }
        }
    }

    public void messageAllocator(Graphics g) {
        /**
         * Get the center coordinates of the board.
         */
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;

        String largeMessage = null;
        String smallMessage = null;
        g.setColor(new Color(194, 71, 71).darker());
        if (game.isNewGame()) {
            largeMessage = "Snake Game!";
            smallMessage = "Press Enter to Start";
        } else if (game.isGameOver()) {
            largeMessage = "Game Over!";
            smallMessage = "Press Enter to Restart";
        } else if (game.isPaused()) {
            largeMessage = "Paused";
            smallMessage = "Press P to Resume";
        }

        /**
         * Set the message font and draw the messages in the center of the
         * board.
         */
        g.setFont(FONT);
        g.drawString(largeMessage, centerX - g.getFontMetrics().
                stringWidth(largeMessage) / 2, centerY - 50);
        g.drawString(smallMessage, centerX - g.getFontMetrics().
                stringWidth(smallMessage) / 2, centerY + 50);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        /**
         * Loop through each tile on the board and draw it if it is not null.
         */
        drawTile(g);

        /**
         * Draw the grid on the board. This makes it easier to see exactly where
         * we in relation to the fruit.
         *
         * The panel is one pixel too small to draw the bottom and right
         * outlines, so we outline the board with a rectangle separately.
         */
        drawGrid(g);

        /**
         * Show a message on the screen based on the current game state.
         */
        if (game.isGameOver() || game.isNewGame() || game.isPaused()) {
            g.setColor(Color.BLACK);

            /**
             * Allocate the messages for and set their values based on the game
             * state.
             */
            messageAllocator(g);
        }
    }

    /**
     * Draws tiles onto the board.
     *
     * @param x The x coordinate of the tile (in pixels).
     * @param y The y coordinate of the tile (in pixels).
     * @param type The type of tile to draw.
     * @param g The graphics object to draw to.
     */
    private void drawTile(int x, int y, TileType type, Graphics g) {

        imageInit();

        /**
         * Because each type of tile is drawn differently, it's easiest to just
         * run through a switch statement rather than come up with some overly
         * complex code to handle everything.
         */
        switch (type) {

            /**
             * A fruit is depicted as a small red circle that with a bit of
             * padding on each side.
             */
            case Fruit:

                g.drawImage(imaApple, x + 2, y + 2, null);
                break;

            /**
             * A fruit is depicted as a small green circle that with a bit of
             * padding on each side.
             */
            case Fruit2:
                g.drawImage(imaKiwi, x + 2, y + 2, null);
                break;

            /**
             * A fruit is depicted as a small blue circle that with a bit of
             * padding on each side.
             */
            case Fruit3:
                g.drawImage(imaBlueBerry, x + 2, y + 2, null);
                break;

            /**
             * A fruit is depicted as a small cyan circle that with a bit of
             * padding on each side.
             */
            case badFruit:
                g.drawImage(imaCucumber, x + 2, y + 2, null);
                break;

            /**
             * A fruit is depicted as a small Yellow circle that with a bit of
             * padding on each side.
             */
            case FruitZero:
                g.drawImage(imaOrange, x + 2, y + 2, null);
                break;

            /**
             * The snake body is depicted as a green square that takes up the
             * entire tile.
             */
            case SnakeBody:
                snakeBody(x, y, type, g);
                break;

            /**
             * The snake head is depicted similarly to the body, but with two
             * lines (representing eyes) that indicate it's direction.
             */
            case SnakeHead:

                snakeHead(x, y, type, g);

                /**
                 * The eyes will always 'face' the direction that the snake is
                 * moving.
                 *
                 * Vertical lines indicate that it's facing North or South, and
                 * Horizontal lines indicate that it's facing East or West.
                 *
                 * Additionally, the eyes will be closer to whichever edge it's
                 * facing.
                 *
                 */
                gameDirection(x, y, type, g);
                break;
        }
    }

    /**
     * @getMatrix
     *
     * Scans the board to see which tiles are empty and which ones are occupied.
     * We use this method so we can save and load the game successfully.
     *
     * @Parameter: none
     * @retrun: State
     */
    public int[] getMatrix() {
        int State[] = new int[tiles.length];

        for (int iI = 0; iI < tiles.length; iI++) {
            if (tiles[iI] != null) {
                State[iI] = tiles[iI].getType();
            } else {
                State[iI] = -1;
            }
        }
        return State;
    }

    /**
     * @setMatrix
     *
     * We use this method so we can save and load the game successfully. This
     * setter is for the loading part specifically.
     *
     * @Parameter: none
     * @retrun: State
     */
    public void setMatrix(int[] State) {

        tiles = new TileType[State.length];

        for (int iC = 0; iC < State.length; iC++) {
            if (State[iC] != -1) {
                tiles[iC] = TileType.values()[State[iC]];
            } else {
                tiles[iC] = null;
            }
        }

    }

    /**
     * Initializes the images. Fruit 1 = apple Fruit 2 = kiwi Fruit 3 =
     * blueberry Bad Fruit = cucumber Zero Fruit = orange
     *
     */
    public void imageInit() {

        URL urlImagenCucumber = this.getClass().
                getResource("images/cucumber.gif");
        imaCucumber = Toolkit.getDefaultToolkit().getImage(urlImagenCucumber);

        URL urlImagenKiwi = this.getClass()
                .getResource("images/kiwi.gif");
        imaKiwi = Toolkit.getDefaultToolkit().getImage(urlImagenKiwi);

        URL urlImagenApple = this.getClass()
                .getResource("images/apple.png");
        imaApple = Toolkit.getDefaultToolkit().getImage(urlImagenApple);

        URL urlImagenOrange = this.getClass()
                .getResource("images/orange.gif");
        imaOrange = Toolkit.getDefaultToolkit().getImage(urlImagenOrange);

        URL urlImagenBerry = this.getClass()
                .getResource("images/blueberry.gif");
        imaBlueBerry = Toolkit.getDefaultToolkit().getImage(urlImagenBerry);

    }

    /**
     * The eyes will always 'face' the direction that the snake is moving.
     *
     * Vertical lines indicate that it's facing North or South, and Horizontal
     * lines indicate that it's facing East or West.
     *
     * Additionally, the eyes will be closer to whichever edge it's facing.
     *
     * Drawing the eyes is fairly simple, but is a bit difficult to explain. The
     * basic process is this:
     *
     * First, we add (or subtract) EYE_SMALL_INSET to or from the side of the
     * tile representing the direction we're facing. This will be constant for
     * both eyes, and is represented by the variable 'baseX' or 'baseY'
     * (depending on orientation).
     *
     * Next, we add (or subtract) EYE_LARGE_INSET to and from the two
     * neighboring directions (Example; East and West if we're facing north).
     *
     * Finally, we draw a line from the base offset that is EYE_LENGTH pixels in
     * length at whatever the offset is from the neighboring directions.
     *
     */
    public void gameDirection(int x, int y, TileType type, Graphics g) {

        switch (game.getDirection()) {

            case North: {
                gameDirectionNorth(x, y, type, g);
                break;
            }

            case South: {
                gameDirectionSouth(x, y, type, g);
                break;
            }

            case West: {
                gameDirectionWest(x, y, type, g);
                break;
            }

            case East: {
                gameDirectionEast(x, y, type, g);
                break;
            }
        }
    }

    public void gameDirectionNorth(int x, int y, TileType type, Graphics g) {

        int baseY = y + EYE_SMALL_INSET;
        g.drawLine(x + EYE_LARGE_INSET, baseY, x
                + EYE_LARGE_INSET, baseY + EYE_LENGTH);
        g.drawLine(x + TILE_SIZE - EYE_LARGE_INSET, baseY, x
                + TILE_SIZE - EYE_LARGE_INSET, baseY + EYE_LENGTH);

    }

    public void gameDirectionSouth(int x, int y, TileType type, Graphics g) {

        int baseY = y + TILE_SIZE - EYE_SMALL_INSET;
        g.drawLine(x + EYE_LARGE_INSET, baseY, x
                + EYE_LARGE_INSET, baseY - EYE_LENGTH);
        g.drawLine(x + TILE_SIZE - EYE_LARGE_INSET, baseY, x
                + TILE_SIZE - EYE_LARGE_INSET, baseY - EYE_LENGTH);

    }

    public void gameDirectionWest(int x, int y, TileType type, Graphics g) {

        int baseX = x + EYE_SMALL_INSET;
        g.drawLine(baseX, y + EYE_LARGE_INSET, baseX
                + EYE_LENGTH, y + EYE_LARGE_INSET);
        g.drawLine(baseX, y + TILE_SIZE - EYE_LARGE_INSET, baseX
                + EYE_LENGTH, y + TILE_SIZE - EYE_LARGE_INSET);

    }

    public void gameDirectionEast(int x, int y, TileType type, Graphics g) {

        int baseX = x + TILE_SIZE - EYE_SMALL_INSET;
        g.drawLine(baseX, y + EYE_LARGE_INSET, baseX
                - EYE_LENGTH, y + EYE_LARGE_INSET);
        g.drawLine(baseX, y + TILE_SIZE - EYE_LARGE_INSET, baseX
                - EYE_LENGTH, y + TILE_SIZE - EYE_LARGE_INSET);

    }

    public void snakeHead(int x, int y, TileType type, Graphics g) {

        //Fill the tile in with green.
        g.setColor(type.getSnakeColor(game.iSnakeColorCounter));
        g.fillRect(x, y, TILE_SIZE, TILE_SIZE);

        //Set the color to black so that we can start drawing the eyes.
        g.setColor(Color.BLACK);

    }

    public void snakeBody(int x, int y, TileType type, Graphics g) {
        g.setColor(type.getSnakeColor(game.iSnakeColorCounter));
        g.fillRect(x, y, TILE_SIZE, TILE_SIZE);

    }
}
