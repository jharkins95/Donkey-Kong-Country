import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.net.URL;
import javax.sound.sampled.*;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.LinkedList;

/**
 * GameCourt.java
 * GameCourt handles the core state of the game, including the game objects currently
 * in play, whether or not the game is paused, the current keys being pressed, and the updating
 * of game state due to collisions and other game events.
 * @author Jack Harkins
 */

@SuppressWarnings("serial")
public class GameCourt extends JPanel {
    
    private static final int COURT_WIDTH = 800;
    private static final int COURT_HEIGHT = 600;
    private static final int TIMER_INTERVAL = 15;
    private static final int MOVE_VELOCITY = 6;
    private static final int LAND_GRAVITY = -1;
    private static final int DEFAULT_NUM_LIVES = 5;
    private static final int FALL_DEATH_THRESHOLD = -50;
    
    private int cumulativeNumLives; // save # of lives after death
    
    // Game objects 
    private Player activePlayer;  
    private List<Enemy> enemyList;
    private List<TerrainBlock> terrainBlockList;
    private List<PowerUp> powerUpList;
    
    // Game state labels
    private final JLabel status;
    private final JLabel lives;
    private final JLabel bananas;
    private final JLabel enemiesRemaining;
    private final Timer loopTimer;
    private String fileInputMode;
    
    // Audio
    private Clip titleMusic;
    private Clip levelMusic;
    private Clip gameOverMusic;
    
    // Drawing and backgrounds
    private int cameraOffsetX;
    private BufferedImage backgroundImage;
    private BufferedImage gameOverScreen;
    private BufferedImage titleScreen;
    
    private boolean atTitleScreen;
    private boolean atGameOverScreen;
    
    private Timer makeTimer(int interval) {
        return new Timer(interval, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!atTitleScreen && !atGameOverScreen) {
                    tick();
                }
                repaint();
            }
        });
    }
    
    public GameCourt(JLabel status, JLabel lives, JLabel bananas, JLabel enemiesRemaining) {
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        loopTimer = makeTimer(TIMER_INTERVAL);
        atTitleScreen = true;
        
        setFocusable(true);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (!activePlayer.isAlive()) {
                    return;
                }
                int keyCode = e.getKeyCode();
                if (keyCode == KeyEvent.VK_LEFT && activePlayer.canMoveLeft()) {
                    activePlayer.setIsMovingLeft(true);
                    activePlayer.setIsMovingRight(false);
                    
                } else if (keyCode == KeyEvent.VK_RIGHT && activePlayer.canMoveRight()) {
                    activePlayer.setIsMovingLeft(false);
                    activePlayer.setIsMovingRight(true);
                } else if (keyCode == KeyEvent.VK_P) {
                    setPauseState(!isPaused());
                } else if (keyCode == KeyEvent.VK_J) {
                    if (activePlayer.canJump()) {
                        activePlayer.jump();
                    }
                }
                
                // debug: change the animation rate
                else if (keyCode == KeyEvent.VK_T) {
                    if (loopTimer.getDelay() != 200) {
                        loopTimer.setDelay(200);
                    } else {
                        loopTimer.setDelay(TIMER_INTERVAL);
                    }
                }
            }
            
            @Override
            public void keyReleased(KeyEvent e) {
                int keyCode = e.getKeyCode();
                if (keyCode == KeyEvent.VK_LEFT || keyCode == KeyEvent.VK_RIGHT) {
                    activePlayer.setIsMovingLeft(false);
                    activePlayer.setIsMovingRight(false);
                }
            }
        });
        
        this.status = status;
        this.lives = lives;
        this.bananas = bananas;
        this.enemiesRemaining = enemiesRemaining;
        
        try {
            backgroundImage = ImageIO.read(new File("jungleBackground.png"));
            gameOverScreen = ImageIO.read(new File("gameOverScreen.png"));
            titleScreen = ImageIO.read(new File("titleScreen.png"));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error loading background, game over screen, or"
                    + " title screen");
            System.exit(1);
        }
       
    }
    
    // Methods to update JLabels representing game state
    
    private void setPanelStatus(String message) {
        status.setText(message);
    }
    
    private void setNumLives(int numLives) {
        lives.setText("Lives: " + numLives);
    }
    
    private void setNumBananas(int numBananas) {
        bananas.setText("Bananas: " + numBananas);
    }
    
    private void setNumEnemies(int numEnemies) {
        enemiesRemaining.setText("Enemies remaining: " + enemyList.size());
    }
    
    public boolean isPaused() {
        return !loopTimer.isRunning();
    }
    
    /**
     * Sets whether the game is currently paused.
     * @param paused is the game paused?
     */
    public void setPauseState(boolean paused) {
        if (paused) {
            loopTimer.stop();
            setPanelStatus("Paused");
        } else {
            loopTimer.start();
            setPanelStatus("Running");
        }
    }
    
    private void showGameOverScreen(Graphics2D g) {
        g.drawImage(gameOverScreen, 0, 0, COURT_WIDTH, COURT_HEIGHT, null);
    }
    
    private void showTitleScreen(Graphics2D g) {
        g.drawImage(titleScreen, 0, 0, COURT_WIDTH, COURT_HEIGHT, null);
    }
    
    private void drawGameScreen(Graphics2D g) {
        // draw background
        g.setColor(Color.DARK_GRAY);
        g.fillRect(0, 0, COURT_WIDTH, COURT_HEIGHT);
        g.drawImage(backgroundImage, 0, 0, COURT_WIDTH, COURT_HEIGHT, null);
        
        // draw game objects
        for (TerrainBlock block : terrainBlockList) {
            block.draw(g, cameraOffsetX);
        }
        for (Enemy enemy : enemyList) {
            enemy.draw(g, cameraOffsetX);
        }
        for (PowerUp powerUp : powerUpList) {
            powerUp.draw(g, cameraOffsetX);
        }
        activePlayer.draw(g, cameraOffsetX);
    }
    
    private void addGameObjs(List<String> tokens) {
        if (tokens.isEmpty()) return;
        if (tokens.size() == 1) {
            fileInputMode = tokens.get(0);
            return;
        }
        int px = Integer.parseInt(tokens.get(0));
        int py = Integer.parseInt(tokens.get(1));
        
        switch (fileInputMode) {
        case "TerrainBlock":
            int width = Integer.parseInt(tokens.get(2));
            int height = Integer.parseInt(tokens.get(3));
            terrainBlockList.add(new TerrainBlock(px, py, width, height));
            break;
        case "Player":
            // player starts with 5 lives by default
            int numLives = (cumulativeNumLives == 0) ? DEFAULT_NUM_LIVES : cumulativeNumLives;
            String imageName = tokens.get(2);
            activePlayer = new Player(px, py, numLives, imageName);
            break;
        case "Enemy":
            String enemyType = tokens.get(2);
            if (enemyType.equals("diddy")) {
                enemyList.add(new Enemy(px, py, Enemy.EnemyType.DIDDY));
            }
            break;
        case "PowerUp":
            String powerUpType = tokens.get(2);
            switch (powerUpType) {
            case "banana":
                powerUpList.add(new PowerUp(px, py, PowerUp.PowerUpType.BANANA));
                break;
            case "bananaBunch":
                powerUpList.add(new PowerUp(px, py, PowerUp.PowerUpType.BANANA_BUNCH));
                break;
            case "redBalloon":
                powerUpList.add(new PowerUp(px, py, PowerUp.PowerUpType.RED_BALLOON));
                break;
            case "blueBalloon":
                powerUpList.add(new PowerUp(px, py, PowerUp.PowerUpType.BLUE_BALLOON));
                break;
            case "greenBalloon":
                powerUpList.add(new PowerUp(px, py, PowerUp.PowerUpType.GREEN_BALLOON));
                break;
            case "kongLetterK":
                powerUpList.add(new PowerUp(px, py, PowerUp.PowerUpType.KONG_LETTER_K));
                break;
            case "kongLetterO":
                powerUpList.add(new PowerUp(px, py, PowerUp.PowerUpType.KONG_LETTER_O));
                break;
            case "kongLetterN":
                powerUpList.add(new PowerUp(px, py, PowerUp.PowerUpType.KONG_LETTER_N));
                break;
            case "kongLetterG":
                powerUpList.add(new PowerUp(px, py, PowerUp.PowerUpType.KONG_LETTER_G));
                break;
            default:
            }
        default:
        }
    }
    
    /**
     * Resets the game to its initial state when starting the game or after the
     * player has died.
     */
    public void reset() {

        terrainBlockList = new LinkedList<>();
        enemyList = new LinkedList<>();
        powerUpList = new LinkedList<>();
        
        if (!isMusicInitialized()) {
            try {
                URL titleURL = new File("title.wav").toURI().toURL();
                AudioInputStream title = AudioSystem.getAudioInputStream(titleURL);
                titleMusic = AudioSystem.getClip();
                titleMusic.open(title);
                URL levelURL = new File("jungleGroove.wav").toURI().toURL();
                AudioInputStream level = AudioSystem.getAudioInputStream(levelURL);
                levelMusic = AudioSystem.getClip();
                levelMusic.open(level);
                URL gameOverURL = new File("gameOver.wav").toURI().toURL();
                AudioInputStream gameOver = AudioSystem.getAudioInputStream(gameOverURL);
                gameOverMusic = AudioSystem.getClip();
                gameOverMusic.open(gameOver);
             } catch (UnsupportedAudioFileException e) {
                e.printStackTrace();
             } catch (IOException e) {
                e.printStackTrace();
             } catch (LineUnavailableException e) {
                e.printStackTrace();
             }
        }
        String filename = JOptionPane.showInputDialog(null, "Enter a level", 
                "sampleDKCLevel.txt");
        LevelParser parser = new LevelParser(filename);
        while (parser.hasNext()) {
            List<String> tokens = parser.readNextTokens();
            addGameObjs(tokens);
        }
        parser.close();

        // Make sure that this component has the keyboard focus
        requestFocusInWindow();
        
        cameraOffsetX = -activePlayer.getPx() + COURT_WIDTH / 2;
        atTitleScreen = false;
        setNumEnemies(enemyList.size());
        loopTimer.start();
    }
    
    private boolean isMusicInitialized() {
        return titleMusic != null && levelMusic != null && gameOverMusic != null;
    }

    private void checkCollisions() {
        // can't jump unless touching terrain
        // can move by default unless dead, touching or colliding
        activePlayer.setJumpable(false);
        if (activePlayer.isAlive()) {
            activePlayer.makeMovable();
        } else {
            activePlayer.makeImmovableHoriz();
        }
        
        // did the player collide with a block?
        for (TerrainBlock block : terrainBlockList) {
            CollisionDirection direction = activePlayer.getCollisionDirection(block);
            if (activePlayer.isColliding(block)) {
                activePlayer.collisionAction(block, direction);
            } else if (activePlayer.isTouching(block)) {
                activePlayer.touchAction(block, direction);
            }
        }
        
        for (Enemy enemy : enemyList) {
            // did the player collide with an enemy?
            CollisionDirection playerCollisionDirection = activePlayer.getCollisionDirection(enemy);
            if (activePlayer.isAlive() && activePlayer.isColliding(enemy)) {
                activePlayer.collisionAction(enemy, playerCollisionDirection);
            }
            
            // did an enemy collide with a block?
            for (TerrainBlock block : terrainBlockList) {
                enemy.makeMovable();
                CollisionDirection enemyCollisionDirection = enemy.getCollisionDirection(block);
                if (enemy.isColliding(block)) {
                    enemy.collisionAction(block, enemyCollisionDirection);
                } else if (enemy.isTouching(block)) {
                    enemy.touchAction(block, enemyCollisionDirection);
                }
            }
        }
        
        for (PowerUp powerUp : powerUpList) {
            if (activePlayer.isColliding(powerUp)) {
                activePlayer.collisionAction(powerUp, null);
            }
        }
    }
    
    private void tick() {
        // Check if no enemies remain. If so, the player wins.
        if (enemyList.isEmpty()) {
            String message =  "No more enemies remain!\nYou win!\nPlay again?";
            int choice = JOptionPane.showOptionDialog(null, message,
                    "Winner!", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, 
                    new String[] {"Yes", "No"}, "Yes");
            if (choice == 0) {
                cumulativeNumLives = activePlayer.getNumLives();
                reset();
            } else {
                System.exit(0);
            }
        }
        
        // Check if player's death animation has finished
        if (activePlayer.hasFinishedDying()) {
            loopTimer.stop();
            if (activePlayer.getNumLives() == 0) {
                JOptionPane.showMessageDialog(null, "You are out of lives!", "Game over!", 
                        JOptionPane.WARNING_MESSAGE);
                atGameOverScreen = true;
                repaint();
                return;
            }
            String message = "You have been killed!\nYou have " + activePlayer.getNumLives() + 
                    " lives remaining.\nContinue?";
            int choice = JOptionPane.showOptionDialog(null, message,
                    "Killed!", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, 
                    new String[] {"Yes", "No"}, "Yes");
            if (choice == 0) {
                cumulativeNumLives = activePlayer.getNumLives();
                reset();
            } else {
                System.exit(0);
            }
        }
        
        // Check which enemies have died and remove them from play
        List<Enemy> dead = new LinkedList<>();
        for (Enemy enemy : enemyList) {
            if (!enemy.isAlive()) {
                dead.add(enemy);
            }
        }
        enemyList.removeAll(dead);
        
        // Similarly, check used powerups
        List<PowerUp> claimed = new LinkedList<>();
        for (PowerUp powerUp : powerUpList) {
            if (powerUp.hasBeenClaimed()) {
                claimed.add(powerUp);
            }
        }
        powerUpList.removeAll(claimed);
        
        // Update player and camera positions
        if (activePlayer.isMovingLeft() && activePlayer.canMoveLeft()) {
            activePlayer.setVx(-MOVE_VELOCITY);
            if (activePlayer.getPx() + cameraOffsetX <= 3 * COURT_WIDTH / 8) {
                cameraOffsetX += MOVE_VELOCITY;
            }
        } else if (activePlayer.isMovingRight() && activePlayer.canMoveRight()) {
            activePlayer.setVx(MOVE_VELOCITY);
            if (activePlayer.getPx() + cameraOffsetX >= 5  * COURT_WIDTH / 8) {
                cameraOffsetX -= MOVE_VELOCITY;
            }
        } else {
            activePlayer.setVx(0);
        }
        
        // Make objects fall and move
        for (Enemy enemy : enemyList) {
            if (enemy.canMoveDown()) {
                enemy.fall(LAND_GRAVITY);
            }
            enemy.move();
        }
        for (TerrainBlock block : terrainBlockList) {
            block.move();
        }
        if (activePlayer.canMoveDown()) {
            activePlayer.fall(LAND_GRAVITY);
        }
        activePlayer.move();
        
        checkCollisions();
        
        // Kill enemies/player if they've fallen off the map.
        for (Enemy enemy : enemyList) {
            if (enemy.isAlive() && enemy.getPy() < FALL_DEATH_THRESHOLD) {
                enemy.kill();
            }
        }
        if (activePlayer.isAlive() && activePlayer.getPy() < FALL_DEATH_THRESHOLD) {
            activePlayer.kill();
        }
        
        // Update status bar
        setNumLives(activePlayer.getNumLives());
        setNumBananas(activePlayer.getNumBananas());
        setNumEnemies(enemyList.size());
    }
    
    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2D = (Graphics2D) g;
        super.paintComponent(g);
        
        if (atTitleScreen) {
            showTitleScreen(g2D);
            if (!titleMusic.isActive()) {
                titleMusic.start();
            }
        } else if (atGameOverScreen) {
            showGameOverScreen(g2D);
            if (!gameOverMusic.isActive()) {
                levelMusic.stop();
                gameOverMusic.start();
            }
        } else {
            if (!levelMusic.isActive()) {
                titleMusic.stop();
                levelMusic.start();
            }
            drawGameScreen(g2D);
        }
        
    }
    
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(COURT_WIDTH, COURT_HEIGHT);
    }
    
    public static int getCartesianX(int x, int width) {
        return x;
    }
    
    public static int getCartesianY(int y, int height) {
        return COURT_HEIGHT - (y + height);
    }
}
