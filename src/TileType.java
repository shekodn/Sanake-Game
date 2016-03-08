
import java.awt.Color;


/**
 * The {@code TileType} class represents the different
 * types of tiles that can be displayed on the screen.
 * 
 * @author Sergio Diaz A01192313
 * @author Ana Karen Beltran A01192508
 *
 */
public enum TileType {

    Fruit(new Color(255,51,51), 0),//red alarga 1
    Fruit2(new Color(153,255,51), 1),//green alarga 2
    Fruit3(new Color(51,153,255), 2),//blue alarga 3
    badFruit(Color.CYAN, 3),//game ends inmediately 
    SnakeHead(new Color(204,204,0), 4),
    SnakeBody(new Color(204,204,0), 5),
    FruitZero(new Color(255,153,51),6);//score resets to zero
    
    private int iType;
    public Color cTypeColor;

    private TileType(Color color, int iType) {
		this.cTypeColor = color;
//		this.lightColor = color.brighter();
//		this.darkColor = color.darker();
//		this.iDimension = dimension;
//		this.bMatTiles = tiles;
//		this.iCols = cols;
//		this.iRows = rows;

		this.iType = iType;
//		this.iSpawnCol = 5 - (dimension >> 1);
//		this.iSpawnRow = getTopInset(0);
    }

    /**
     * Gets the color of this type.
     *
     * @return The color.
     */
    public Color getBaseColor() {
        return cTypeColor;
    }
    //case
    
    
    public Color getSnakeColor(int iCounter){

        switch (iCounter) {
            case 1:
                cTypeColor = new Color(204, 204, 0);//original
                break;
            case 2:
                cTypeColor = new Color(204, 204, 0).brighter();//brighter
                break;
                
            case 3:
                cTypeColor = new Color(204, 204, 0).darker();//darker
                break;
        }

        return cTypeColor;
    }

    /**
     * Gets the type of a tile.
     *
     * @return The int of the type.
     */
    public int getType() {
        return iType;
    }
}
