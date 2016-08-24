/**
 * Collidable.java
 * Interface for GameObjs that perform some action upon collision with
 * another GameObj.
 * @author Jack Harkins
 *
 */
public interface Collidable {
    void touchAction(GameObj other, CollisionDirection direction);
    void collisionAction(GameObj other, CollisionDirection direction);
}
