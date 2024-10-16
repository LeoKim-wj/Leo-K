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
    static JPanel panelNorth;
    static JPanel panelCenter;
    static JLabel labelMessage;
    static JButton[] buttons;
    static JButton restartButton, saveButton, loadButton;
    
    private GameLogic gameLogic;

    public GameUI(String title) {
        super(title);
        this.setLayout(new BorderLayout());
        this.setSize(400, 500);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        gameLogic = new GameLogic(); // Initialize game logic
        
        initUI(); 
        this.pack();
        this.setVisible(true);
    }

    private void initUI() {
        // Initialize North Panel with buttons
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
        saveButton = new JButton("Save");
        loadButton = new JButton("Load");
        restartButton.addActionListener(this);
        saveButton.addActionListener(this);
        loadButton.addActionListener(this);

        panelNorth.add(restartButton);
        panelNorth.add(saveButton);
        panelNorth.add(loadButton);

        this.add("North", panelNorth);

        // Initialize Center Panel with game buttons
        panelCenter = new JPanel();
        panelCenter.setLayout(new GridLayout(4, 4));
        buttons = new JButton[16];
        for (int i = 0; i < 16; i++) {
            buttons[i] = new JButton();
            buttons[i].setPreferredSize(new Dimension(100, 100));
            buttons[i].setIcon(changeImage("card.png"));
            buttons[i].addActionListener(this);
            panelCenter.add(buttons[i]);
        }

        this.add("Center", panelCenter);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Handle button clicks here
        // Delegate logic to `GameLogic`
        gameLogic.handleAction(e, buttons, labelMessage);
    }

    private ImageIcon changeImage(String filename) {
        ImageIcon icon = new ImageIcon("./" + filename);
        Image originImage = icon.getImage();
        Image changedImage = originImage.getScaledInstance(95, 95, Image.SCALE_SMOOTH);
        return new ImageIcon(changedImage);
    }

    public static void main(String[] args) {
        new GameUI("Memory Game");
    }
}
