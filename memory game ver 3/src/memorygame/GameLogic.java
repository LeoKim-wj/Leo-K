/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package memorygame;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.sql.SQLException;
import java.util.Random;
/**
 *
 * @author kmkm3
 */
public class GameLogic {
    private GameDB gameDB = new GameDB();  // Derby DB 
    private String[] images = {
        "card (2).png", "card (3).png", "card (4).png", "card (5).png",
        "card (6).png", "card (7).png", "card (8).png", "card (9).png",
        "card (2).png", "card (3).png", "card (4).png", "card (5).png",
        "card (6).png", "card (7).png", "card (8).png", "card (9).png"
    };

    private int openCount = 0;
    private int buttonIndexSave1 = -1;
    private int buttonIndexSave2 = -1;
    private Timer timer;
    private int tryCount = 0;
    private int successCount = 0;
    private boolean[] cardStates = new boolean[16];  

    public GameLogic() {
        gameDB.createTable();  
    }

    public void mixCard() {
        Random rand = new Random();
        for (int i = 0; i < 1000; i++) {
            int random = rand.nextInt(15) + 1;
            String temp = images[0];
            images[0] = images[random];
            images[random] = temp;
        }
    }

    public boolean checkCard(int index1, int index2) {
        if (index1 == index2) {
            return false;  
        }
        return images[index1].equals(images[index2]);
    }

    public void handleCardClick(JButton btn, JButton[] buttons, JLabel labelMessage) {
        if (openCount == 2 || btn.getIcon() != null) {
            return;  
        }

        int index = getButtonIndex(btn, buttons);
        btn.setIcon(changeImage(images[index]));
        cardStates[index] = true;

        openCount++;
        if (openCount == 1) {
            buttonIndexSave1 = index;
        } else if (openCount == 2) {
            buttonIndexSave2 = index;
            tryCount++;
            labelMessage.setText("Find Same Card! Try " + tryCount);

            if (checkCard(buttonIndexSave1, buttonIndexSave2)) {
                openCount = 0;
                successCount++;
                if (successCount == 8) {
                    labelMessage.setText("Game Over. Try " + tryCount);
                }
            } else {
                backToCard(buttons);
            }
        }
    }

    public void restartGame(JButton[] buttons, JLabel labelMessage) {
        openCount = 0;
        tryCount = 0;
        successCount = 0;
        labelMessage.setText("Find Same Cards! Try 0");
        mixCard();
        for (int i = 0; i < 16; i++) {
            cardStates[i] = false;
            buttons[i].setIcon(changeImage("card.png"));
        }
    }

    private void backToCard(JButton[] buttons) {
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

    private int getButtonIndex(JButton btn, JButton[] buttons) {
        for (int i = 0; i < 16; i++) {
            if (buttons[i] == btn) {
                return i;
            }
        }
        return -1;
    }

    private ImageIcon changeImage(String filename) {
        ImageIcon icon = new ImageIcon(getClass().getResource("/images/" + filename));
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
                    buttons[i].setIcon(new ImageIcon(cardStates[i] ? 
                        getClass().getResource("/images/" + images[i]) : 
                        getClass().getResource("/images/card.png")));
                }
                labelMessage.setText("Find Same Cards! Try " + tryCount);
                JOptionPane.showMessageDialog(null, "Game loaded successfully!");
            } else {
                JOptionPane.showMessageDialog(null, "No saved game found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database error: " + e.getMessage(), 
                                          "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}