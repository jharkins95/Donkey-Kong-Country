/**
 * FileInputMode.java
 * Used by GameCourt to determine which type of object is currently 
 * being read in from a level file.
 * @author Jack Harkins
 *
 */
public enum FileInputMode {
    TERRAIN_MODE,
    ENEMY_MODE,
    BARREL_MODE,
    PLAYER_MODE,
    NONE;
}
