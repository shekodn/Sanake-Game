
import java.awt.Color;


/**
 * The {@code TileType} class represents the different
 * types of tiles that can be displayed on the screen.
 * @author Brendan Jones
 *
 */
public enum TileType {

    Fruit(Color.RED, 0),
    Fruit2(Color.GREEN, 1),
    Fruit3(Color.BLUE, 2),
    badFruit(Color.CYAN, 3),
    SnakeHead(Color.GREEN, 4),
    SnakeBody(Color.GREEN, 5);
    
    private int iType;

    private TileType(Color color, int iType) {
//		this.baseColor = color;
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
    
    public int getType (){
        return iType;
    }
}
