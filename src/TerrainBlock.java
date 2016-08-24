import java.awt.*;

/**
 * TerrainBlock.java
 * Represents terrain that players and enemies can walk on, as well as walls and ceilings.
 * @author Jack Harkins
 *
 */
public class TerrainBlock extends GameObj {

    public TerrainBlock(int px, int py, int width, int height) {
        super(px, py, 0, 0, width, height);
    }
    
    @Override
    public void draw(Graphics2D g, int cameraOffsetX) {
        int x = GameCourt.getCartesianX(getPx(), getWidth()) + cameraOffsetX;
        int y = GameCourt.getCartesianY(getPy(), getHeight());
        g.setColor(Color.GREEN);
        g.fillRect(x, y, getWidth(), getHeight());

    }
}
