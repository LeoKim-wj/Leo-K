/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package memorygame;

import java.awt.Image;
import javax.swing.*;
import java.awt.event.*;
import java.io.*;
/**
 *
 * @author kmkm3
 */
public class GameLogic {
    private Card[] cards;
    private int openCount = 0;
    private int buttonIndexSave1 = 0;
    private int buttonIndexSave2 = 0;
    private int tryCount = 0;
    private int successCount = 0;
    private Timer timer;

    public GameLogic() {
        cards = new Card[16];
        // Initialize cards with images
        String[] images = {
            "card (2).png", "card (3).png", "card (4).png", "card (5).png",
            "card (6).png", "card (7).png", "card (8).png", "card (9).png",
            "card (2).png", "card (3).png", "card (4).png", "card (5).png",
            "card (6).png", "card (7).png", "card (8).png", "card (9).png",
        };
        for (int i = 0; i < cards.length; i++) {
            cards[i] = new Card(images[i]);
        }
    }

    public void handleAction(ActionEvent e, JButton[] buttons, JLabel labelMessage) {
        // Handle game logic when buttons are clicked
        JButton btn = (JButton) e.getSource();
        int index = getButtonIndex(btn, buttons);

        if (openCount == 2) {
            return;
        }

        btn.setIcon(changeImage(cards[index].getImage()));
        openCount++;

        if (openCount == 1) {
            buttonIndexSave1 = index;
        } else if (openCount == 2) {
            buttonIndexSave2 = index;
            tryCount++;
            labelMessage.setText("Find Same Card! Try " + tryCount);

            if (cards[buttonIndexSave1].getImage().equals(cards[buttonIndexSave2].getImage())) {
                successCount++;
                if (successCount == 8) {
                    labelMessage.setText("Game Over! Tries: " + tryCount);
                }
            } else {
                backToCard(buttons);
            }
        }
    }

    private void backToCard(JButton[] buttons) {
        timer = new Timer(500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buttons[buttonIndexSave1].setIcon(changeImage("card.png"));
                buttons[buttonIndexSave2].setIcon(changeImage("card.png"));
                openCount = 0;
                timer.stop();
            }
        });
        timer.start();
    }

    private int getButtonIndex(JButton btn, JButton[] buttons) {
        for (int i = 0; i < buttons.length; i++) {
            if (buttons[i] == btn) {
                return i;
            }
        }
        return -1;
    }

   static ImageIcon changeImage(String filename) {
        ImageIcon icon = new ImageIcon("./" + filename);
        Image originImage = icon.getImage();
        Image changedImage = originImage.getScaledInstance(95, 95, Image.SCALE_SMOOTH);
        return new ImageIcon(changedImage);
    }
}