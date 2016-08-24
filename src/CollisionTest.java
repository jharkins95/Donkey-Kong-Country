import static org.junit.Assert.*;
import org.junit.Test;

public class CollisionTest {

    // basic collision detection
    
    @Test
    public void testCollideTerrainSingleCenter() {
        Player player = new Player(0, 0, 5, "dk.png");
        TerrainBlock block = new TerrainBlock(0, 0, 10, 10);
        assertTrue("player colliding with block", player.isColliding(block));
        assertFalse("player not touching block", player.isTouching(block));
        assertTrue("block colliding with player", block.isColliding(player));
        assertFalse("block not touching player", block.isTouching(player));
    }
    
    @Test
    public void testNotCollidingTerrain() {
        Player player = new Player(0, 0, 5, "dk.png");
        TerrainBlock block = new TerrainBlock(80, 0, 10, 10);
        assertFalse(player.isColliding(block));
        assertFalse(player.isTouching(block));
        assertFalse(block.isColliding(player));
        assertFalse(block.isTouching(player));
    }
    
    @Test
    public void testTouchingNotCollidingTerrain() {
        Player player = new Player(0, 0, 5, "dk.png");
        TerrainBlock block = new TerrainBlock(60, 0, 10, 10);
        assertFalse(player.isColliding(block));
        assertTrue(player.isTouching(block));
        assertFalse(block.isColliding(player));
        assertTrue(block.isTouching(player));
        
        assertEquals(CollisionDirection.RIGHT, player.getCollisionDirection(block));
    }
    
    @Test
    public void testNotCollidingMultiple() {
        Player player = new Player(0, 0, 5, "dk.png");
        TerrainBlock b1 = new TerrainBlock(100, 0, 10, 10);
        TerrainBlock b2 = new TerrainBlock(0, 100, 10, 10);
        TerrainBlock b3 = new TerrainBlock(-100, 0, 10, 10);
        TerrainBlock b4 = new TerrainBlock(0, -100, 10, 10);
        
        assertFalse(player.isColliding(b1));
        assertFalse(player.isColliding(b2));
        assertFalse(player.isColliding(b3));
        assertFalse(player.isColliding(b4));
        
        assertFalse(player.isTouching(b1));
        assertFalse(player.isTouching(b2));
        assertFalse(player.isTouching(b3));
        assertFalse(player.isTouching(b4));
        
    }
    
    @Test
    public void testTouchingMultiple() {
        Player player = new Player(0, 0, 5, "dk.png");
        TerrainBlock b1 = new TerrainBlock(60, 0, 60, 60);
        TerrainBlock b2 = new TerrainBlock(0, 60, 60, 60);
        TerrainBlock b3 = new TerrainBlock(-60, 0, 60, 60);
        TerrainBlock b4 = new TerrainBlock(0, -60, 60, 60);
        
        assertTrue(player.isTouching(b1));
        assertTrue(player.isTouching(b2));
        assertTrue(player.isTouching(b3));
        assertTrue(player.isTouching(b4));
        
        assertFalse(player.isColliding(b1));
        assertFalse(player.isColliding(b2));
        assertFalse(player.isColliding(b3));
        assertFalse(player.isColliding(b4));
        
        assertEquals(CollisionDirection.RIGHT, player.getCollisionDirection(b1));
        assertEquals(CollisionDirection.UP, player.getCollisionDirection(b2));
        assertEquals(CollisionDirection.LEFT, player.getCollisionDirection(b3));
        assertEquals(CollisionDirection.DOWN, player.getCollisionDirection(b4));
    }
    
    @Test
    public void testCollidingMultiple() {
        Player player = new Player(0, 0, 5, "dk.png");
        TerrainBlock b1 = new TerrainBlock(50, 0, 60, 60);
        TerrainBlock b2 = new TerrainBlock(0, 50, 60, 60);
        TerrainBlock b3 = new TerrainBlock(-5, 0, 60, 60);
        TerrainBlock b4 = new TerrainBlock(0, -5, 60, 60);
        
        assertTrue(player.isColliding(b1));
        assertTrue(player.isColliding(b2));
        assertTrue(player.isColliding(b3));
        assertTrue(player.isColliding(b4));
        
        assertFalse(player.isTouching(b1));
        assertFalse(player.isTouching(b2));
        assertFalse(player.isTouching(b3));
        assertFalse(player.isTouching(b4));
        
        assertEquals(CollisionDirection.RIGHT, player.getCollisionDirection(b1));
        assertEquals(CollisionDirection.UP, player.getCollisionDirection(b2));
        assertEquals(CollisionDirection.LEFT, player.getCollisionDirection(b3));
        assertEquals(CollisionDirection.DOWN, player.getCollisionDirection(b4));
        
    }
    
    @Test
    public void testPlayerTouchingRectangularTerrainBlock() {
        TerrainBlock floor = new TerrainBlock(0, 0, 1000, 200);
        TerrainBlock leftWall = new TerrainBlock(-10, 200, 10, 60);
        TerrainBlock rightWall = new TerrainBlock(1000, 200, 10, 60);
        Player p1 = new Player(0, 200, 5, "dk.png");
        Player p2 = new Player(500, 200, 5, "dk.png");
        Player p3 = new Player(940, 200, 5, "dk.png");
        
        assertTrue(p1.isTouching(floor));
        assertTrue(p2.isTouching(floor));
        assertTrue(p3.isTouching(floor));
        assertEquals(CollisionDirection.DOWN, p1.getCollisionDirection(floor));
        assertEquals(CollisionDirection.DOWN, p2.getCollisionDirection(floor));
        assertEquals(CollisionDirection.DOWN, p3.getCollisionDirection(floor));
        
        assertTrue(p1.isTouching(leftWall));
        assertFalse(p2.isTouching(leftWall));
        assertFalse(p3.isTouching(leftWall));
        assertEquals(CollisionDirection.LEFT, p1.getCollisionDirection(leftWall));
        
        assertFalse(p1.isTouching(rightWall));
        assertFalse(p2.isTouching(rightWall));
        assertTrue(p3.isTouching(rightWall));
        assertEquals(CollisionDirection.RIGHT, p3.getCollisionDirection(rightWall));
    }
    
    @Test
    public void testNotTouchingAtCorner() {
        Player player = new Player(0, 0, 5, "dk.png");
        TerrainBlock b1 = new TerrainBlock(60, 60, 60, 60);
        TerrainBlock b2 = new TerrainBlock(-60, 60, 60, 60);
        TerrainBlock b3 = new TerrainBlock(-60, -60, 60, 60);
        TerrainBlock b4 = new TerrainBlock(60, -60, 60, 60);
        
        assertFalse(player.isTouching(b1));
        assertFalse(player.isTouching(b2));
        assertFalse(player.isTouching(b3));
        assertFalse(player.isTouching(b4));
        
        assertFalse(player.isColliding(b1));
        assertFalse(player.isColliding(b2));
        assertFalse(player.isColliding(b3));
        assertFalse(player.isColliding(b4));
    }

    // actions upon collision
    
    @Test
    public void testPlayerCollideWithEnemy() {
        Player p1 = new Player(0, 59, 5, "dk.png");
        Player p2 = new Player(59, 0, 5, "dk.png");
        Player p3 = new Player(118, 59, 5, "dk.png");
        Player p4 = new Player(59, 118, 5, "dk.png");
        Enemy enemy = new Enemy(59, 59, Enemy.EnemyType.DIDDY);
        
        assertTrue("p1 colliding", p1.isColliding(enemy));
        assertTrue("p2 colliding", p2.isColliding(enemy));
        assertTrue("p3 colliding", p3.isColliding(enemy));
        assertTrue("p4 colliding", p4.isColliding(enemy));
        
        p1.collisionAction(enemy, p1.getCollisionDirection(enemy));
        assertFalse(p1.isAlive());
        assertTrue(enemy.isAlive());

        p2.collisionAction(enemy, p2.getCollisionDirection(enemy));
        assertFalse(p2.isAlive());
        assertTrue(enemy.isAlive());
        
        p3.collisionAction(enemy, p3.getCollisionDirection(enemy));
        assertFalse(p3.isAlive());
        assertTrue(enemy.isAlive());
        
        p4.collisionAction(enemy, p4.getCollisionDirection(enemy));
        assertTrue(p4.isAlive());
        assertFalse(enemy.isAlive());
    }
    
    @Test
    public void testPlayerCollideWithBananas() {
        Player player = new Player(0, 0, 5, "dk.png");
        assertEquals(5, player.getNumLives());
        
        PowerUp b1 = new PowerUp(0, 0, PowerUp.PowerUpType.BANANA);
        assertTrue(player.isColliding(b1));
        player.collisionAction(b1, player.getCollisionDirection(b1));
        assertEquals(player.getNumBananas(), 1);
        
        PowerUp b2 = new PowerUp(0, 0, PowerUp.PowerUpType.BANANA);
        assertTrue(player.isColliding(b2));
        player.collisionAction(b2, player.getCollisionDirection(b2));
        assertEquals(player.getNumBananas(), 2);
        
        PowerUp b3 = new PowerUp(0, 0, PowerUp.PowerUpType.BANANA);
        assertTrue(player.isColliding(b3));
        player.collisionAction(b3, player.getCollisionDirection(b3));
        assertEquals(player.getNumBananas(), 3);
        
        PowerUp b4 = new PowerUp(0, 0, PowerUp.PowerUpType.BANANA);
        assertTrue(player.isColliding(b4));
        player.collisionAction(b4, player.getCollisionDirection(b4));
        assertEquals(player.getNumBananas(), 4);
        
        for (int i = 0; i < 95; i++) {
            // collision direction shouldn't matter...
            player.collisionAction(new PowerUp(0, 0, PowerUp.PowerUpType.BANANA), 
                    CollisionDirection.DOWN);
        }
        
        assertEquals(99, player.getNumBananas());
        
        // number of lives should increment
        player.collisionAction(new PowerUp(0, 0, PowerUp.PowerUpType.BANANA), 
                CollisionDirection.DOWN);
        assertEquals(0, player.getNumBananas());
        assertEquals(6, player.getNumLives());
        
        // number of lives should not change
        player.collisionAction(new PowerUp(0, 0, PowerUp.PowerUpType.BANANA), 
                CollisionDirection.DOWN);
        assertEquals(1, player.getNumBananas());
        assertEquals(6, player.getNumLives());
    }
    
    @Test
    public void testPlayerCollideWithKongLetters() {
        Player player = new Player(0, 0, 5, "dk.png");
        PowerUp k = new PowerUp(0, 0, PowerUp.PowerUpType.KONG_LETTER_K);
        PowerUp o = new PowerUp(0, 0, PowerUp.PowerUpType.KONG_LETTER_O);
        PowerUp n = new PowerUp(0, 0, PowerUp.PowerUpType.KONG_LETTER_N);
        PowerUp g = new PowerUp(0, 0, PowerUp.PowerUpType.KONG_LETTER_G);
        
        player.collisionAction(k, player.getCollisionDirection(k));
        player.collisionAction(o, player.getCollisionDirection(o));
        player.collisionAction(n, player.getCollisionDirection(n));
        assertEquals(5, player.getNumLives());
        
        player.collisionAction(g, player.getCollisionDirection(g));
        assertEquals(6, player.getNumLives());
    }
    
    @Test
    public void testPlayerCollideWithBananaBunch() {
        Player player = new Player(0, 0, 5, "dk.png");
        player.collisionAction(new PowerUp(0, 0, PowerUp.PowerUpType.BANANA_BUNCH), 
                CollisionDirection.DOWN);
        assertEquals(5, player.getNumBananas());
        assertEquals(5, player.getNumLives());
        
        for (int i = 0; i < 18; i++) {
            player.collisionAction(new PowerUp(0, 0, PowerUp.PowerUpType.BANANA_BUNCH), 
                    CollisionDirection.DOWN);
        }
        
        assertEquals(95, player.getNumBananas());
        assertEquals(5, player.getNumLives());
        
        player.collisionAction(new PowerUp(0, 0, PowerUp.PowerUpType.BANANA_BUNCH), 
                CollisionDirection.DOWN);
        assertEquals(0, player.getNumBananas());
        assertEquals(6, player.getNumLives());
        
        player.collisionAction(new PowerUp(0, 0, PowerUp.PowerUpType.BANANA_BUNCH), 
                CollisionDirection.DOWN);
        assertEquals(5, player.getNumBananas());
        assertEquals(6, player.getNumLives());
    }
    
    @Test
    public void testPlayerCollideWithBalloons() {
        Player player = new Player(0, 0, 5, "dk.png");
        player.collisionAction(new PowerUp(0, 0, PowerUp.PowerUpType.RED_BALLOON), 
                CollisionDirection.DOWN);
        assertEquals(6, player.getNumLives());
        
        player.collisionAction(new PowerUp(0, 0, PowerUp.PowerUpType.GREEN_BALLOON), 
                CollisionDirection.DOWN);
        assertEquals(8, player.getNumLives());
        
        player.collisionAction(new PowerUp(0, 0, PowerUp.PowerUpType.BLUE_BALLOON), 
                CollisionDirection.DOWN);
        assertEquals(11, player.getNumLives());
    }
}
