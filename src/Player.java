import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.swing.Timer;

import javax.imageio.ImageIO;

/**
 * Player.java
 * The Player class handles state that is specific to a human player, including the number
 * of lives remaining and bananas collected, the KONG letters acquired, and whether or not
 * the player is allowed to jump or move left/right.
 * @author Jack Harkins
 *
 */
public class Player extends GameObj implements Collidable {
    
    private static int JUMP_VELOCITY = 15;
    
    private int numBananas;
    private int numLives;
    private boolean canJump;
    private BufferedImage image;
    private Timer deathTimer;
    private boolean isAlive;
    private boolean hasFinishedDying;
    private boolean acquiredLetterK;
    private boolean acquiredLetterO;
    private boolean acquiredLetterN;
    private boolean acquiredLetterG;
    
    // used for keyboard movement controls
    // updating the camera won't work without these
    private boolean isMovingLeft;
    private boolean isMovingRight;
    
    private static final int DEATH_DELAY = 1000;
    
    public Player(int px, int py, int numLives, String imageName) {
        super(px, py, 0, 0, 60, 60);
        this.numLives = numLives;
        try {
            image = ImageIO.read(new File(imageName));
        } catch (IOException e) {
            throw new IllegalArgumentException("image file not found");
        }
        canJump = true;
        isAlive = true;
        deathTimer = new Timer(DEATH_DELAY, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hasFinishedDying = true;
            }
        });
    }
    
    /**
     * @param isMovingLeft whether the player is moving left, according to the
     * key controls
     */
    public void setIsMovingLeft(boolean isMovingLeft) {
        this.isMovingLeft = isMovingLeft;
    }
    
    /**
     * @param isMovingRight whether the player is moving right, according to the
     * key controls
     */
    public void setIsMovingRight(boolean isMovingRight) {
        this.isMovingRight = isMovingRight;
    }
    
    /**
     * 
     * @return is the player moving left, according to the keyboard?
     */
    public boolean isMovingLeft() {
        return isMovingLeft;
    }
    
    /**
     * 
     * @return is the player moving right, according to the keyboard?
     */
    public boolean isMovingRight() {
        return isMovingRight;
    }
    
    /**
     * 
     * @return how many bananas the player currently has
     */
    public int getNumBananas() {
        return numBananas;
    }
    
    private void incNumBananas() {
        numBananas++;
        if (numBananas == 100) {
            numBananas = 0;
            numLives++;
        }
    }
    
    /**
     * @return has the player's death animation timer expired?
     */
    public boolean hasFinishedDying() {
        return hasFinishedDying;
    }
    
    
    /**
     * 
     * @return the player's current count of lives left
     */
    public int getNumLives() {
        return numLives;
    }
    
    /**
     * 
     * @return is the player alive?
     */
    public boolean isAlive() {
        return isAlive;
    }
    
    /**
     * Decrements the player's number of lives and starts the death timer.
     */
    public void kill() {
        numLives--;
        isAlive = false;
        setVx(0);
        setVy(0);
        deathTimer.start();
    }
    
    /**
     * 
     * @return is the player allowed to jump?
     */
    public boolean canJump() {
        return canJump;
    }
    
    /**
     * 
     * @param canJump whether or not the player can jump
     */
    public void setJumpable(boolean canJump) {
        this.canJump = canJump;
    }
    
    /**
     * Sets the player's velocity in the upward direction to 15
     */
    public void jump() {
        setVy(JUMP_VELOCITY);
    }
    
    private boolean hasAcquiredKong() {
        return acquiredLetterK && acquiredLetterO && acquiredLetterN && acquiredLetterG;
    }
    
    private void resetKongAcquisitions() {
        acquiredLetterK = false;
        acquiredLetterO = false;
        acquiredLetterN = false;
        acquiredLetterG = false;
    }
    
    /**
     * Action to be performed when the player touches another GameObj
     */
    @Override
    public void touchAction(GameObj other, CollisionDirection direction) {
        if (other instanceof TerrainBlock) {
            if (direction == CollisionDirection.DOWN) {
                setMovableDown(false);
                setJumpable(true);
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
     * Action to be performed when the player collides with another GameObj
     * 
     * When the player collides in a downward direction with an enemy (i.e. the player is
     * falling onto the enemy), the enemy is killed. The player is killed if the collision occurs
     * in any other direction.
     * 
     * Colliding with terrain results in the player's position being clipped outside the
     * boundaries of the terrain. If the player collides in a downward direction with terrain,
     * the player is allowed to jump.
     */
    @Override
    public void collisionAction(GameObj other, CollisionDirection direction) {
        if (other instanceof Enemy) {
            Enemy enemy = (Enemy) other;
            if (direction == CollisionDirection.DOWN) {
                enemy.kill();
                jump(); // bounce the player upward
            } else {
                kill();
            }
        } else if (other instanceof TerrainBlock) {
            if (direction == CollisionDirection.DOWN) {
                setVy(0);
                clipUp(other);
                setMovableDown(false);
                setJumpable(true);
            } else if (direction == CollisionDirection.LEFT) {
                setVx(0);
                clipRight(other);
                setMovableLeft(false);
            } else if (direction == CollisionDirection.RIGHT) {
                setVx(0);
                clipLeft(other);
                setMovableRight(false);
            } else if (direction == CollisionDirection.UP) {
                setVy(0);
                clipDown(other);
                setMovableUp(false);
            }
        } else if (other instanceof PowerUp) {
            PowerUp powerUp = (PowerUp) other;
            powerUp.claim();
            switch (powerUp.getType()) {
            case BANANA:
                incNumBananas();
                break;
            case BANANA_BUNCH:
                for (int i = 0; i < 5; i++) {
                    incNumBananas();
                }
                break;
            case RED_BALLOON:
                numLives++;
                break;
            case GREEN_BALLOON:
                numLives += 2;
                break;
            case BLUE_BALLOON:
                numLives += 3;
                break;
            case KONG_LETTER_K:
                acquiredLetterK = true;
                break;
            case KONG_LETTER_O:
                acquiredLetterO = true;
                break;
            case KONG_LETTER_N:
                acquiredLetterN = true;
                break;
            case KONG_LETTER_G:
                acquiredLetterG = true;
                break;
            }
            if (hasAcquiredKong()) {
                numLives++;
                resetKongAcquisitions();
            }
        }
    }
    
    /**
     * Draws the player's sprite on the canvas, according to the player's movement direction
     */
    @Override
    public void draw(Graphics2D g, int cameraOffsetX) {
        int x = GameCourt.getCartesianX(getPx(), getWidth()) + cameraOffsetX;
        int y = GameCourt.getCartesianY(getPy(), getHeight());
        g.setColor(Color.BLACK);
        if (isMovingLeft()) {
            g.drawImage(image, x + getWidth(), y, -getWidth(), getHeight(), null);
        } else {
            g.drawImage(image, x, y, getWidth(), getHeight(), null);
        }
    }
}
