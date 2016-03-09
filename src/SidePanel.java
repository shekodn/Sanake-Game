
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JPanel;

/**
 * The {@code SidePanel} class is responsible for displaying statistics and
 * controls to the player.
 *
 * @author Sergio Diaz A01192313
 * @author Ana Karen Beltran A01192508
 *
 */
public class SidePanel extends JPanel {

    /**
     * Serial Version UID.
     */
    private static final long serialVersionUID = -40557434900946408L;

    /**
     * The large font to draw with.
     */
    private static final Font LARGE_FONT = new Font("Tahoma", Font.BOLD, 20);

    /**
     * The medium font to draw with.
     */
    private static final Font MEDIUM_FONT = new Font("Tahoma", Font.BOLD, 16);

    /**
     * The small font to draw with.
     */
    private static final Font SMALL_FONT = new Font("Tahoma", Font.BOLD, 12);

    /**
     * The SnakeGame instance.
     */
    private SnakeGame game;

    /**
     * Creates a new SidePanel instance.
     *
     * @param game The SnakeGame instance.
     */
    public SidePanel(SnakeGame game) {
        this.game = game;

        setPreferredSize(new Dimension(300, BoardPanel.ROW_COUNT * BoardPanel.TILE_SIZE));
        setBackground(Color.WHITE);
    }

    private static final int STATISTICS_OFFSET = 60;

    private static final int CONTROLS_OFFSET = 290;

    private static final int MESSAGE_STRIDE = 25;

    private static final int SMALL_OFFSET = 30;

    private static final int LARGE_OFFSET = 50;

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        /**
         * Set the color to draw the font in to dark gray.
         */
        g.setColor(new Color(194,71,71).darker());
        
        /**
         * Draw the game name onto the window.
         */
        g.setFont(LARGE_FONT);
        g.drawString("Snake Game", getWidth() / 2 - g.getFontMetrics().stringWidth("Snake Game") / 2, 30);

        /**
         * Draw the categories onto the window.
         */
        g.setFont(MEDIUM_FONT);
        //g.setColor(new Color(103,20,255));
        //g.setColor(new Color(194,71,71));
        g.setColor(Color.DARK_GRAY);
        g.drawString("Statistics", SMALL_OFFSET, STATISTICS_OFFSET);
        g.drawString("Controls", SMALL_OFFSET, CONTROLS_OFFSET);
        g.drawString("Fruit Types", SMALL_OFFSET, 170);

        /**
         * Draw the category content onto the window.
         */
        g.setFont(SMALL_FONT);
        
        //g.setColor(Color.DARK_GRAY);
         g.setColor(new Color(153,51,51));
        //Draw the content for the statistics category.
        int drawY = STATISTICS_OFFSET;
        g.drawString("Total Score: " + game.getScore(), LARGE_OFFSET, drawY += MESSAGE_STRIDE);
        g.drawString("Fruit Eaten: " + game.getFruitsEaten(), LARGE_OFFSET, drawY += MESSAGE_STRIDE);
        g.drawString("Fruit Score: " + game.getNextFruitScore(), LARGE_OFFSET, drawY += MESSAGE_STRIDE);

        //g.drawString("Controls", SMALL_OFFSET, drawY += MESSAGE_STRIDE);
        //g.drawString("drawY"+drawY, SMALL_OFFSET, drawY += MESSAGE_STRIDE);
        drawY = 170;
        
        g.setColor(new Color(255, 51, 51));//alarga 1
        g.fillOval(LARGE_OFFSET, drawY += 10 , 12, 12);
        g.setColor(new Color(255, 51, 51).darker());
        g.drawString("Adds 1.",LARGE_OFFSET+20, drawY +10);
        
        g.setColor(new Color(153, 255, 51));//2
        g.fillOval(LARGE_OFFSET, drawY += MESSAGE_STRIDE-10, 12, 12);
        g.setColor(new Color(153, 255, 51).darker());
        g.drawString("Adds 2. Double points.",LARGE_OFFSET+20, drawY +10);
        
        g.setColor(new Color(51, 153, 255));//3
        g.fillOval(LARGE_OFFSET, drawY += MESSAGE_STRIDE - 10, 12, 12);
        g.setColor(new Color(51, 153, 255).darker());
        g.drawString("Alarga 3. Triple points.",LARGE_OFFSET+20, drawY +10);
        
        g.setColor(Color.CYAN);//end game
        g.fillOval(LARGE_OFFSET, drawY += MESSAGE_STRIDE - 10, 12, 12);
        g.setColor(Color.CYAN.darker());
        g.drawString("Ends game inmediately.",LARGE_OFFSET+20, drawY +10);
        
        g.setColor(new Color(255, 153, 51));//rest to zero
        g.fillOval(LARGE_OFFSET, drawY += MESSAGE_STRIDE - 10, 12, 12);
        g.setColor(new Color(255, 153, 51).darker());
        g.drawString("Reset socre to 0.",LARGE_OFFSET+20, drawY +10);

        //g.setColor(Color.DARK_GRAY);
         g.setColor(new Color(153,51,51));
        //Draw the content for the controls category.
        drawY = CONTROLS_OFFSET;
        g.drawString("Move Up: W / Up Arrowkey", LARGE_OFFSET, drawY += MESSAGE_STRIDE);
        g.drawString("Move Down: S / Down Arrowkey", LARGE_OFFSET, drawY += MESSAGE_STRIDE);
        g.drawString("Move Left: A / Left Arrowkey", LARGE_OFFSET, drawY += MESSAGE_STRIDE);
        g.drawString("Move Right: D / Right Arrowkey", LARGE_OFFSET, drawY += MESSAGE_STRIDE);
        g.drawString("Pause Game: P", LARGE_OFFSET, drawY += MESSAGE_STRIDE);
        g.drawString("Load Game: C", LARGE_OFFSET, drawY += MESSAGE_STRIDE);
        g.drawString("Save Game: G", LARGE_OFFSET, drawY += MESSAGE_STRIDE);
    }

}
