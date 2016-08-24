import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;


/**
 * PowerUp.java
 * The PowerUp class contains information specific to items that can help the player. Most of
 * the functionality of a PowerUp is stored as an enumerated type which can be accessed and
 * queried upon the player colliding with a power up to determine the appropriate action.
 * @author jthem
 *
 */
public class PowerUp extends GameObj {
    
    enum PowerUpType {
        BANANA("banana.png", 30, 30),
        BANANA_BUNCH("bananaBunch.png", 30, 30),
        RED_BALLOON("redBalloon.png", 50, 90),
        GREEN_BALLOON("greenBalloon.png", 50, 90),
        BLUE_BALLOON("blueBalloon.png", 50, 90),
        KONG_LETTER_K("kongLetterK.png", 30, 30),
        KONG_LETTER_O("kongLetterO.png", 30, 30),
        KONG_LETTER_N("kongLetterN.png", 30, 30),
        KONG_LETTER_G("kongLetterG.png", 30, 30);
        
        private final String filename;
        private final int width;
        private final int height;
        
        PowerUpType(String filename, int width, int height) {
            this.filename = filename;
            this.width = width;
            this.height = height;
        }
        
        int getWidth() {
            return width;
        }
        
        int getHeight() {
            return height;
        }
        
        String getFilename() {
            return filename;
        }
    }
    
    private BufferedImage image;
    private final PowerUpType type;
    private boolean isClaimed;
    
    public PowerUp(int px, int py, PowerUpType type) {
        super(px, py, 0, 0, type.getWidth(), type.getHeight());
        try {
            image = ImageIO.read(new File(type.getFilename()));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Powerup image file not found!");
            System.exit(1);
        }
        this.type = type;
    }
    
    /**
     * 
     * @return has this power up been used already?
     */
    public boolean hasBeenClaimed() {
        return isClaimed;
    }
    
    /**
     * Prevents this power up from being used multiple times
     */
    public void claim() {
        isClaimed = true;
    }
    
    /**
     * 
     * @return the enumerated type of this power up
     */
    public PowerUpType getType() {
        return type;
    }

    @Override
    public void draw(Graphics2D g, int cameraOffsetX) {
        int x = GameCourt.getCartesianX(getPx(), getWidth()) + cameraOffsetX;
        int y = GameCourt.getCartesianY(getPy(), getHeight());
        g.drawImage(image, x, y, getWidth(), getHeight(), null);
        
    }
}
