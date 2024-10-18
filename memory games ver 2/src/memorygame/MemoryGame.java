package memorygame;

public class MemoryGame {
    public static void main(String[] args) {
        GameLogic gameLogic = new GameLogic();  // 게임 로직 생성
        new GameUI(gameLogic);  // UI 생성 및 로직 연결
    }
}