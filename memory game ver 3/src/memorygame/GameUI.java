/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package memorygame;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
/**
 *
 * @author kmkm3
 */
public class GameUI extends JFrame implements ActionListener {
    private JPanel panelNorth;
    private JPanel panelCenter;
    private JLabel labelMessage;
    private JButton[] buttons = new JButton[16];
    private JButton restartButton;

    private GameLogic gameLogic; 

    public GameUI(GameLogic gameLogic) {
        this.gameLogic = gameLogic; 
        this.setLayout(new BorderLayout());
        this.setSize(400, 500);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initUI(); 
        gameLogic.mixCard();  

        this.pack();
        this.setVisible(true);
    }

    private void initUI() {
        
        panelNorth = new JPanel();
        panelNorth.setPreferredSize(new Dimension(600, 100));
        panelNorth.setBackground(Color.LIGHT_GRAY);

        labelMessage = new JLabel("Find Same Cards! Try 0");
        labelMessage.setPreferredSize(new Dimension(400, 100));
        labelMessage.setForeground(Color.WHITE);
        labelMessage.setFont(new Font("arial", Font.BOLD, 20));
        labelMessage.setHorizontalAlignment(JLabel.CENTER);
        panelNorth.add(labelMessage);

        restartButton = new JButton("Restart");
        restartButton.setPreferredSize(new Dimension(100, 50));
        restartButton.addActionListener(this);
        panelNorth.add(restartButton);

        this.add("North", panelNorth);

        panelCenter = new JPanel();
        panelCenter.setLayout(new GridLayout(4, 4));
        panelCenter.setPreferredSize(new Dimension(400, 400));

        for (int i = 0; i < 16; i++) {
            buttons[i] = new JButton();
            buttons[i].setPreferredSize(new Dimension(100, 100));
            buttons[i].setIcon(new ImageIcon("./card.png"));
            buttons[i].addActionListener(this); 
            panelCenter.add(buttons[i]);
        }

        this.add("Center", panelCenter);
    }

    @Override
public void actionPerformed(ActionEvent e) {
    if (e.getSource() == restartButton) {
        gameLogic.restartGame(buttons, labelMessage);  
    } else if (e.getActionCommand().equals("Save")) {
        gameLogic.saveGame();  
    } else if (e.getActionCommand().equals("Load")) {
        gameLogic.loadGame(buttons, labelMessage);  
    } else {
        JButton btn = (JButton) e.getSource();
        gameLogic.handleCardClick(btn, buttons, labelMessage); 
    }
}
}