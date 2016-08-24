import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

/**
 * Enemy.java
 * Represents enemies that can hurt the player. Enemies are hard-coded to
 * walk before encountering a wall or another obstacle. They may be killed
 * by the player when he jumps on them.
 * @author Jack Harkins
 *
 */
public class Enemy extends GameObj implements Collidable {
    
    enum EnemyType{
        
        DIDDY("diddy.png");
        
        private String filename;
        
        EnemyType(String filename) {
            this.filename = filename;
        }
        
        String getFilename() {
            return filename;
        }
    }
    
    private boolean isAlive;
    private BufferedImage image;
    
    private static final int ENEMY_VELOCITY = 3;
    
    public Enemy(int px, int py, EnemyType type) {
        super(px, py, ENEMY_VELOCITY, 0, 60, 60);
        try {
            image = ImageIO.read(new File(type.getFilename()));
        } catch (java.io.IOException e) {
            JOptionPane.showMessageDialog(null, "Enemy image file not found!");
            System.exit(1);
        }
        isAlive = true;
    }
    
    /**
     * 
     * @return is the enemy alive?
     */
    public boolean isAlive() {
        return isAlive;
    }
    
    /**
     * Action to be performed when an enemy collides with another GameObj
     */
    @Override
    public void collisionAction(GameObj other, CollisionDirection direction) {
        if (other instanceof TerrainBlock) {
            if (direction == CollisionDirection.DOWN) {
                setVy(0);
                clipUp(other);
                setMovableDown(false);
            } else if (direction == CollisionDirection.LEFT) {
                setVx(ENEMY_VELOCITY); // reverse velocity
                clipRight(other);
                setMovableLeft(false);
            } else if (direction == CollisionDirection.RIGHT) {
                setVx(-ENEMY_VELOCITY); // reverse velocity
                clipLeft(other);
                setMovableRight(false);
            } else if (direction == CollisionDirection.UP) {
                setVy(0);
                clipDown(other);
                setMovableUp(false);
            }
        }
    }
    
    /**
     * Action to be performed when an enemy touches another GameObj
     */
    public void touchAction(GameObj other, CollisionDirection direction) {
        if (other instanceof TerrainBlock) {
            if (direction == CollisionDirection.DOWN) {
                setMovableDown(false);
            } else if (direction == CollisionDirection.UP) {
                setMovableUp(false);
            } else if (direction == CollisionDirection.LEFT) {
                setMovableLeft(false);
            } else if (direction == CollisionDirection.RIGHT) {
                setMovableRight(false);
            }
        }
    }
    
    /**
     * Kills the enemy
     */
    public void kill() {
        isAlive = false;
    }

    /**
     * Draws the enemy's sprite on the canvas, according to the enemy's position.
     */
    @Override
    public void draw(Graphics2D g, int cameraOffsetX) {
        int x = GameCourt.getCartesianX(getPx(), getWidth()) + cameraOffsetX;
        int y = GameCourt.getCartesianY(getPy(), getHeight());
        g.setColor(Color.BLACK);
        if (getVx() < 0) {
            g.drawImage(image, x + getWidth(), y, -getWidth(), getHeight(), null);
        } else {
            g.drawImage(image, x, y, getWidth(), getHeight(), null);
        }
    }
    
}
