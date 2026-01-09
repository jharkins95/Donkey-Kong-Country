import java.awt.*;

/**
 * GameObj.java
 * An abstract class to represent the different types of game objects in Donkey
 * Kong Country, each with their own behavior. Such types include players, enemies,
 * and power ups.
 * @author Jack Harkins
 *
 */
public abstract class GameObj {
    
    // px, py, vx, and vy are in Cartesian coordinates
    private int px;
    private int py; 
    private int vx;
    private int vy;
    
    private final int width;
    private final int height;
    private boolean canMoveUp;
    private boolean canMoveDown;
    private boolean canMoveLeft;
    private boolean canMoveRight;
    
    // Used to match falling behavior of DKC and similar platform games
    private static final int MAX_VELOCITY = 20;
    
    public GameObj(int px, int py, int vx, int vy, int width, int height) {
        this.px = px;
        this.py = py;
        this.vx = vx;
        this.vy = vy;
        this.width = width;
        this.height = height;
        canMoveUp = true;
        canMoveDown = true;
        canMoveLeft = true;
        canMoveRight = true;
    }
     
    /**
     * 
     * @return the player's current x coordinate, in Cartesian coordinates
     */
    public int getPx() {
        return px;
    }
    
    /**
     * 
     * @return the player's current y coordinate, in Cartesian coordinates
     */
    public int getPy() {
        return py;
    }
    
    /**
     * 
     * @return the player's current x velocity
     */
    public int getVx() {
        return vx;
    }
    
    /**
     * 
     * @return the player's current y velocity
     */
    public int getVy() {
        return vy;
    }
    
    /**
     * 
     * @param vx the new x velocity
     */
    public void setVx(int vx) {
        this.vx = vx;
    }
    
    /**
     * 
     * @param vx the new y velocity
     */
    public void setVy(int vy) {
        this.vy = vy;
    }
    
    /**
     * Updates the player's position according to their current velocity
     */
    public void move() {
        px += vx;
        py += vy;
    }
    
    /**
     * Sets the player's velocity to zero
     */
    public void stop() {
        vx = 0;
        vy = 0;
    }
    
    /**
     * Updates an object's y-velocity
     * @param yAccel the acceleration due to gravity
     */
    public void fall(int yAccel) {
        if (vy - yAccel < -MAX_VELOCITY) {
            vy = -MAX_VELOCITY;
        } else if (vy + yAccel > MAX_VELOCITY) {
            vy = MAX_VELOCITY;
        } else {
            vy += yAccel;
        }
    }
    
    /**
     * 
     * @return width of player's sprite
     */
    public int getWidth() {
        return width;
    }
    
    /**
     * 
     * @return height of player's sprite
     */
    public int getHeight() {
        return height;
    }
    
    /**
     * 
     * @return is the player able to move up?
     */
    public boolean canMoveUp() {
        return canMoveUp;
    }
    
    /**
     * 
     * @return is the player able to move down?
     */
    public boolean canMoveDown() {
        return canMoveDown;
    }
    
    /**
     * 
     * @return is the player able to move left?
     */
    public boolean canMoveLeft() {
        return canMoveLeft;
    }
    
    /**
     * 
     * @return is the player able to move right?
     */
    public boolean canMoveRight() {
        return canMoveRight;
    }
    
    /**
     * Allows the player to move in all directions
     */
    public void makeMovable() {
        canMoveUp = true;
        canMoveDown = true;
        canMoveLeft = true;
        canMoveRight = true;
    }
    
    /**
     * Prevents the player from moving along the x axis.
     * Used after the player dies to prevent keyboard controls
     * from moving the player.
     */
    public void makeImmovableHoriz() {
        canMoveLeft = false;
        canMoveRight = false;
    }
    
    protected void setMovableUp(boolean canMoveUp) {
        this.canMoveUp = canMoveUp;
    }
    
    protected void setMovableDown(boolean canMoveDown) {
        this.canMoveDown = canMoveDown;
    }
    
    protected void setMovableLeft(boolean canMoveLeft) {
        this.canMoveLeft = canMoveLeft;
    }
    
    protected void setMovableRight(boolean canMoveRight) {
        this.canMoveRight = canMoveRight;
    }
    
    // Collision methods
    // All GameObjs have these methods by default to avoid having to reimplement them
    // in every type of Collidable. (Collidable merely uses these to determine what
    // action to perform.)
    
    private boolean isCollidingLeft(GameObj other) {
        return getPx() < other.getPx() + other.getWidth();
    }
    
    private boolean isCollidingRight(GameObj other) {
        return getPx() + getWidth() > other.getPx();
    }
    
    private boolean isCollidingUp(GameObj other) {
        return getPy() + getHeight() > other.getPy();
    }
    
    private boolean isCollidingDown(GameObj other) {
        return getPy() < other.getPy() + other.getHeight();
    }
    
    /**
     * 
     * @param other a GameObj the player might be colliding with
     * @return is the player's sprite intersecting the other sprite?
     */
    public boolean isColliding(GameObj other) {
        return isCollidingUp(other) && isCollidingDown(other) &&
                isCollidingLeft(other) && isCollidingRight(other);
    }    
    
    private boolean isTouchingUp(GameObj other) {
        return getPy() + getHeight() == other.getPy() && isCollidingLeft(other) &&
                isCollidingRight(other);
    }
    
    private boolean isTouchingDown(GameObj other) {
        return getPy() == other.getPy() + other.getHeight() && isCollidingLeft(other) &&
                isCollidingRight(other) && isCollidingUp(other);
    }
    
    private boolean isTouchingLeft(GameObj other) {
        return getPx() == other.getPx() + other.getWidth() && isCollidingUp(other) &&
                isCollidingDown(other) && isCollidingRight(other);
    }
    
    private boolean isTouchingRight(GameObj other) {
        return getPx() + getWidth() == other.getPx() && isCollidingUp(other) &&
                isCollidingDown(other) && isCollidingLeft(other);
    }
    
    /**
     * 
     * @param other a GameObj the player might be touching
     * @return is the player's sprite immediately adjacent to (but not colliding with)
     * the other?
     */
    public boolean isTouching(GameObj other) {
        return isTouchingLeft(other) || isTouchingRight(other) ||
                isTouchingUp(other) || isTouchingDown(other);
    }

    public void clipLeft(GameObj other) {
        px = other.getPx() - getWidth();
    }
    
    public void clipRight(GameObj other) {
        px = other.getPx() + other.getWidth();
    }
    
    public void clipUp(GameObj other) {
        py = other.getPy() + other.getHeight();
    }
    
    public void clipDown(GameObj other) {
        py = other.getPy() - getHeight();
    }
    
    // This is not my original code.
    // Credit: CIS 120 sample game code
    public CollisionDirection getCollisionDirection(GameObj other) {  
        double dx = other.getPx() + other.getWidth() / 2 - (getPx() + getWidth() / 2);
        double dy = other.getPy() + other.getHeight() / 2 - (getPy() + getHeight() / 2);

        double theta = Math.acos(dx / (Math.sqrt(dx * dx + dy * dy)));
        double diagTheta = Math.atan2(other.getHeight() / 2, other.getWidth() / 2);

        if (theta <= diagTheta ) {
            return CollisionDirection.RIGHT;
        } else if ( theta > diagTheta && theta <= Math.PI - diagTheta ) {
            if ( dy > 0 ) {
                return CollisionDirection.UP;
            } else {
                return CollisionDirection.DOWN;
            }
        } else {
            return CollisionDirection.LEFT;
        }
    }
    
    // Abstract since every object is drawn differently
    public abstract void draw(Graphics2D g, int cameraOffsetX);
}
