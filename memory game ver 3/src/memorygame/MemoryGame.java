package memorygame;

public class MemoryGame {
    public static void main(String[] args) {
        GameLogic gameLogic = new GameLogic();  
        new GameUI(gameLogic);  
    }
}