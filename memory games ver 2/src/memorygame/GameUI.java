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

    private GameLogic gameLogic;  // 게임 로직 인스턴스

    public GameUI(GameLogic gameLogic) {
        this.gameLogic = gameLogic;  // 외부에서 로직 객체 주입
        this.setLayout(new BorderLayout());
        this.setSize(400, 500);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initUI();  // UI 초기화
        gameLogic.mixCard();  // 카드 섞기

        this.pack();
        this.setVisible(true);
    }

    private void initUI() {
        // 상단 패널 초기화
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
        restartButton.addActionListener(this);  // 액션 리스너 등록
        panelNorth.add(restartButton);

        this.add("North", panelNorth);

        // 게임 버튼이 있는 중앙 패널 초기화
        panelCenter = new JPanel();
        panelCenter.setLayout(new GridLayout(4, 4));
        panelCenter.setPreferredSize(new Dimension(400, 400));

        for (int i = 0; i < 16; i++) {
            buttons[i] = new JButton();
            buttons[i].setPreferredSize(new Dimension(100, 100));
            buttons[i].setIcon(new ImageIcon("./card.png"));
            buttons[i].addActionListener(this);  // 각 버튼에 리스너 등록
            panelCenter.add(buttons[i]);
        }

        this.add("Center", panelCenter);
    }

    @Override
public void actionPerformed(ActionEvent e) {
    if (e.getSource() == restartButton) {
        gameLogic.restartGame(buttons, labelMessage);  // 게임 재시작
    } else if (e.getActionCommand().equals("Save")) {
        gameLogic.saveGame();  // 게임 저장
    } else if (e.getActionCommand().equals("Load")) {
        gameLogic.loadGame(buttons, labelMessage);  // 게임 불러오기
    } else {
        JButton btn = (JButton) e.getSource();
        gameLogic.handleCardClick(btn, buttons, labelMessage);  // 카드 클릭 처리
    }
}
}