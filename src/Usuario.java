/**
 * @author Sergio Diaz A01192313
 * @author Ana Karen Beltran A01192508
 */
public class Usuario {

    private String userName;
    private int score;
    private int nextFruitScore;
    private int fruitsEaten;
    private boolean isGameOver;
    private boolean isNewGame;

    public Usuario(String userName, int score, int nextFruitScore, int fruitsEaten,
            boolean isGameOver, boolean isNewGame) {
        this.userName = userName; 
        this.score = score; 
        this.nextFruitScore = nextFruitScore;
        this.fruitsEaten = fruitsEaten;
        this.isGameOver = isGameOver;
        this.isNewGame = isNewGame;
    }
}

