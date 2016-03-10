
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.net.URL;

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

    private static final int CONTROLS_OFFSET = 300;

    private static final int MESSAGE_STRIDE = 25;

    private static final int SMALL_OFFSET = 30;

    private static final int LARGE_OFFSET = 50;

    /**
     * Images of fruits and bad fruits
     */
    public Image imaApple;
    public Image imaCucumber;
    public Image imaKiwi;
    public Image imaBlueBerry;
    public Image imaOrange;
    
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        imageInit();
        
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
        g.drawString("Fruit Types", SMALL_OFFSET, 165);

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
        drawY = 160;
        
        g.setColor(new Color(255, 51, 51).darker());
        g.drawImage(imaApple,LARGE_OFFSET, drawY += 20, null);
      
        
        g.setColor(new Color(153,51,51));
        g.drawString("Apple: Adds 1 ",LARGE_OFFSET+20, drawY +10);
        
        g.drawImage(imaKiwi,LARGE_OFFSET, drawY += 20, null);
        g.drawString("Kiwi: Adds 2 ",LARGE_OFFSET+20, drawY +10);
       
        g.drawImage(imaBlueBerry, LARGE_OFFSET, drawY += 20, null);
        g.drawString("Blueberry: Add 3 ",LARGE_OFFSET+20, drawY +10);
        
        g.drawImage(imaCucumber, LARGE_OFFSET, drawY += 20, null);
        g.drawString("Cucumber: Ends game inmediately",LARGE_OFFSET+20, drawY +10);
        
        g.drawImage(imaOrange, LARGE_OFFSET, drawY += 20, null);
        g.drawString("Orange: Reset socre to 0",LARGE_OFFSET+20, drawY +10);
        

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
    
    
    /**
         * Initializes the images. 
         * Fruit 1 = apple
         * Fruit 2 = kiwi
         * Fruit 3 = blueberry
         * Bad Fruit = cucumber
         * Zero Fruit = orange
         *
         */
        public void imageInit() {

            URL urlImagenCucumber = this.getClass()
                    .getResource("images/cucumber.gif");
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
            
            URL urlImagenBlueBerry = this.getClass()
                    .getResource("images/blueberry.gif");
            imaBlueBerry = Toolkit.getDefaultToolkit().getImage(urlImagenBlueBerry);
           
        }

}
