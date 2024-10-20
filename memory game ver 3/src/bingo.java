package memorygame;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;

public class bingo { 
    static JPanel panelNorth; //Tap View
    static JPanel panelCenter; //Game View
    static JLabel labelMessage;
    static JButton[] buttons = new JButton[16];
    static String[] images = {
        "card (2).png", "card (3).png", "card (4).png", "card (5).png", //all_undercase
        "card (6).png", "card (7).png", "card (8).png", "card (9).png",
        "card (2).png", "card (3).png", "card (4).png", "card (5).png", 
        "card (6).png", "card (7).png", "card (8).png", "card (9).png",
    };
    static int openCount = 0; //opened Card Counter
    static int buttonIndexSave1 = 0; //First Opened Card Index
    static int buttonIndexSave2 = 0; //Second Opened Card Index
    static Timer timer;
    static int tryCount = 0; //Try Count
    static int successCount = 0; //Success Count 
    static JButton restartButton; // Restart Button
    
    static class MyFrame extends JFrame implements ActionListener {
        public MyFrame(String title) {
            super(title);
            this.setLayout(new BorderLayout());
            this.setSize(400,500);
            this.setVisible(true);
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            
            initUI(this); //Screen UI Set.
            mixCard(); //Mix Card
            
            this.pack(); //Pack Empty Space
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == restartButton) {
                restartGame();
                return;
            }

            if(openCount == 2) {
                return;
            }
            
            JButton btn = (JButton)e.getSource();
            int index = getButtonIndex(btn);
            
            btn.setIcon(changeImage(images[index]));
            
            openCount++;
            if(openCount == 1) { //First Card?
                buttonIndexSave1 = index;
            } else if(openCount == 2) { //Second Card
                buttonIndexSave2 = index;
                tryCount++;
                labelMessage.setText("Find Same Card!" + " Try " + tryCount);
                
                //Judge Logic
                boolean isBingo = checkCard(buttonIndexSave1,buttonIndexSave2);
                if(isBingo) {
                    openCount = 0;
                    successCount++;
                    if(successCount == 8) {
                        labelMessage.setText("Game Over  " + "Try " +tryCount);
                    }
                } else {
                    backtoCard();
                }
            }
        }

        public void restartGame() { //  restart method
            openCount = 0;
            tryCount = 0;
            successCount = 0;
            labelMessage.setText("Find Same Cards!" + " Try 0");
            mixCard();
            for (int i = 0; i < 16; i++) {
                buttons[i].setIcon(changeImage("card.png"));
            }
        }

        public void backtoCard() {
            //Timer 1Second
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
        
        public boolean checkCard(int index1, int index2) {
            if(index1 == index2) {
                return false;
            }
            return images[index1].equals(images[index2]);
        }
        
        public int getButtonIndex(JButton btn) {
            for(int i = 0; i < 16; i++) {
                if(buttons[i] == btn) { //same instance?
                    return i;
                }
            }
            return -1;
        }
    }
    
    static void mixCard() {
        Random rand = new Random();
        for(int i=0; i<1000; i++) {
            int random = rand.nextInt(15) + 1;
            //swap
            String temp = images[0];
            images[0] = images[random];
            images[random] = temp;
        }
    }
    
    static void initUI(MyFrame myFrame){
        panelNorth = new JPanel();
        panelNorth.setPreferredSize(new Dimension(600,100));
        panelNorth.setBackground(Color.LIGHT_GRAY);
        labelMessage = new JLabel("Find Same Cards!" + " Try 0");
        labelMessage.setPreferredSize(new Dimension(400,100));
        labelMessage.setForeground(Color.WHITE);
        labelMessage.setFont(new Font("arial", Font.BOLD, 20));
        labelMessage.setHorizontalAlignment(JLabel.CENTER);
        panelNorth.add(labelMessage);
        
        restartButton = new JButton("Restart"); // restart buttons
        restartButton.setPreferredSize(new Dimension(100, 50)); 
        restartButton.addActionListener(myFrame); 
        panelNorth.add(restartButton); 
        
        myFrame.add("North", panelNorth);
        
        panelCenter = new JPanel();
        panelCenter.setLayout(new GridLayout(4,4));
        panelCenter.setPreferredSize(new Dimension(400,400));
        for(int i = 0; i<16; i++) {
            buttons[i] = new JButton();
            buttons[i].setPreferredSize(new Dimension(100,100));
            buttons[i].setIcon(changeImage("card.png"));
            buttons[i].addActionListener(myFrame);
            panelCenter.add(buttons[i]);
        }
        myFrame.add("Center", panelCenter);
    }
    
    static ImageIcon changeImage(String filename) {
        ImageIcon icon = new ImageIcon("./"+ filename);
        Image originImage = icon.getImage();
        Image changedImage = originImage.getScaledInstance(95, 95, Image.SCALE_SMOOTH);
        return new ImageIcon(changedImage);
    }
    
    public static void main(String[] args) {
        new MyFrame("Memory Game");
    }
}
