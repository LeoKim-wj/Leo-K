/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package memorygame;
import java.awt.Image;
import javax.swing.*;
/**
 *
 * @author kmkm3
 */
public class Buttons {
    private JButton[] buttons;

    public Buttons(JButton[] buttons) {
        this.buttons = buttons;
    }

    public void updateButton(int index, String image) {
        buttons[index].setIcon(changeImage(image));
    }

    private ImageIcon changeImage(String filename) {
        ImageIcon icon = new ImageIcon("./" + filename);
        Image originImage = icon.getImage();
        Image changedImage = originImage.getScaledInstance(95, 95, Image.SCALE_SMOOTH);
        return new ImageIcon(changedImage);
    }
}