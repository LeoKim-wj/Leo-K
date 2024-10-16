/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package memorygame;

/**
 *
 * @author kmkm3
 */
public class Card {
    private String image;
    private boolean revealed;
    private boolean matched;

    public Card(String image) {
        this.image = image;
        this.revealed = false;
        this.matched = false;
    }

    public String getImage() {
        return image;
    }

    public boolean isRevealed() {
        return revealed;
    }

    public void reveal() {
        this.revealed = true;
    }

    public void hide() {
        this.revealed = false;
    }

    public boolean isMatched() {
        return matched;
    }

    public void setMatched(boolean matched) {
        this.matched = matched;
    }
}
