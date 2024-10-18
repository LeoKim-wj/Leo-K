/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package memorygame;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;
/**
 *
 * @author kmkm3
 */
public class GameLogic {
    private GameDB gameDB = new GameDB();  // DB 인스턴스
      public GameLogic() {
        gameDB.createTable(); 
    }
    private String[] images = {
        "card (2).png", "card (3).png", "card (4).png", "card (5).png",
        "card (6).png", "card (7).png", "card (8).png", "card (9).png",
        "card (2).png", "card (3).png", "card (4).png", "card (5).png",
        "card (6).png", "card (7).png", "card (8).png", "card (9).png"
    };

    private int openCount = 0;  // 열려 있는 카드 개수
    private int buttonIndexSave1 = 0;  // 첫 번째 열린 카드 인덱스
    private int buttonIndexSave2 = 0;  // 두 번째 열린 카드 인덱스
    private Timer timer;
    private int tryCount = 0;  // 시도 횟수
    private int successCount = 0;  // 성공한 짝 개수
    
    private boolean[] cardStates = new boolean[16];
    
    public void mixCard() {  // 카드 섞기
        Random rand = new Random();
        for (int i = 0; i < 1000; i++) {
            int random = rand.nextInt(15) + 1;
            String temp = images[0];
            images[0] = images[random];
            images[random] = temp;
        }
    }

    public boolean checkCard(int index1, int index2) {  // 두 카드가 같은지 확인
        if (index1 == index2) {
            return false;
        }
        return images[index1].equals(images[index2]);
    }

    public void handleCardClick(JButton btn, JButton[] buttons, JLabel labelMessage) {
        if (openCount == 2) {
            return;  // 이미 두 개의 카드가 열려 있으면 더 클릭 불가
        }

        int index = getButtonIndex(btn, buttons);
        btn.setIcon(changeImage(images[index]));  // 클릭한 카드 열기
        cardStates[index] = true;
        
        openCount++;
        if (openCount == 1) {
            buttonIndexSave1 = index;  // 첫 번째 카드 저장
        } else if (openCount == 2) {
            buttonIndexSave2 = index;  // 두 번째 카드 저장
            tryCount++;
            labelMessage.setText("Find Same Card! Try " + tryCount);

            if (checkCard(buttonIndexSave1, buttonIndexSave2)) {  // 짝 확인
                openCount = 0;
                successCount++;
                if (successCount == 8) {  // 모든 짝을 맞추면 게임 종료
                    labelMessage.setText("Game Over. Try " + tryCount);
                }
            } else {
                backToCard(buttons);
            }
        }
    }

    public void restartGame(JButton[] buttons, JLabel labelMessage) {  // 게임 재시작
        openCount = 0;
        tryCount = 0;
        successCount = 0;
        labelMessage.setText("Find Same Cards! Try 0");
        mixCard();
        for (int i = 0; i < 16; i++) {
            cardStates[i] = false;
            buttons[i].setIcon(changeImage("card.png"));  // 모든 카드를 닫음
        }
    }

    private void backToCard(JButton[] buttons) {  // 틀린 카드 다시 닫기
        timer = new Timer(250, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openCount = 0;
                buttons[buttonIndexSave1].setIcon(changeImage("card.png"));
                buttons[buttonIndexSave2].setIcon(changeImage("card.png"));
                timer.stop();
            }
        });
        timer.start();
    }

    private int getButtonIndex(JButton btn, JButton[] buttons) {  // 버튼 인덱스 찾기
        for (int i = 0; i < 16; i++) {
            if (buttons[i] == btn) {
                return i;
            }
        }
        return -1;  // 버튼을 찾지 못한 경우
    }

    private ImageIcon changeImage(String filename) {  // 이미지 변경
        ImageIcon icon = new ImageIcon("./" + filename);
        Image originImage = icon.getImage();
        Image changedImage = originImage.getScaledInstance(95, 95, Image.SCALE_SMOOTH);
        return new ImageIcon(changedImage);
    }
    
    public void saveGame() {
        StringBuilder stateBuilder = new StringBuilder();
        for (boolean state : cardStates) {
            stateBuilder.append(state ? '1' : '0');
        }
        gameDB.saveGame(stateBuilder.toString(), tryCount, successCount);
        JOptionPane.showMessageDialog(null, "Game saved successfully!");
    }

public void loadGame(JButton[] buttons, JLabel labelMessage) {
    try (var resultSet = gameDB.loadGame()) {
        if (resultSet != null && resultSet.next()) {
            String cardStateString = resultSet.getString("cardStates");
            tryCount = resultSet.getInt("tryCount");
            successCount = resultSet.getInt("successCount");

            for (int i = 0; i < 16; i++) {
                cardStates[i] = cardStateString.charAt(i) == '1';
                buttons[i].setIcon(new ImageIcon(cardStates[i] ? "./" + images[i] : "./card.png"));
            }
            labelMessage.setText("Find Same Cards! Try " + tryCount);
            JOptionPane.showMessageDialog(null, "Game loaded successfully!");
        } else {
            JOptionPane.showMessageDialog(null, "No saved game found.");
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
}
}