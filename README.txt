=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=
CIS 120 Game Project README
PennKey: harkj
=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=

===================
=: Core Concepts :=
===================

- List the four core concepts, the features they implement, and why each feature
  is an approprate use of the concept. You may copy and paste from your proposal
  document if you did not change the features you are implementing.

  1. Collections

- What specific feature of your game will be implemented using this concept?

  A TreeSet is used to keep track of the various game objects currently present in the game,
  such as the player, enemies, powerups, and terrain blocks. The set is accessed to draw the 
  players on the screen. move objects at every timestep, and perform checks for collisions.

- Why does it make sense to implement this feature with this concept? Justify
  why this is a non-trivial application of the concept in question.
  
  It wouldn't be feasible to keep track of potentially many game objects in any other way.
  Using an array wouldn't be very efficient since it would have to be copied each time a 
  game object is added or deleted. Multiple collections of various types of game objects were used
  to avoid syntactic clutter and overuse of the "instanceOf" keyword to discern different
  types of objects.

  2. File IO

- What specific feature of your game will be implemented using this concept?
    I will use file IO to allow custom level files to be made and used in game. The level
    files will specify the locations of the game objects in a comma-separated value format,
    with the values depending on the type of game object currently being read in. The levels
    are able to be loaded on-demand using a load button in the GUI.


- Why does it make sense to implement this feature with this concept? Justify
  why this is a non-trivial application of the concept in question.
  
  It would be a pain to hard-code the levels into my program's source code. Doing this would
  also prevent others from creating their own levels easily if they did not have access to
  the source code of my game. Separating the game core from "optional" parts of the game
  (i.e. extra levels) also makes the code cleaner and more generalized. I feel my file format
  is sufficiently complex because there are several types of objects encoded in the format
  that take different parameters when instantiating them in my game.

  3. JUnit Testing

- What specific feature of your game will be implemented using this concept?

    I use JUnit testing to verify the correctness of my collision detection algorithms,
    especially for nontrivial scenarios and corner cases (example: multiple blocks colliding
    with the player at the same time.. I will also use JUnit to test
    that players are killed when enemies hit them, as well as the functionality of the various
    power ups.


- Why does it make sense to implement this feature with this concept? Justify
  why this is a non-trivial application of the concept in question.
  
  It's often hard to tell if a computer program works well just by looking at its output. This is
  especially true for collision detection, since the user may believe that a collision was
  properly detected (the player stops moving) when it actually wasn't. Additionally, detecting
  when a collision has occurred isn't trivial - collisions can be detected in more than one
  direction, and one object might be colliding with or touching more than one object at once.
  This means that there are many scenarios that need to be rigorously tested.
  

  4. Inheritance and Subtyping

- What specific feature of your game will be implemented using this concept?

    I use subtyping and inheritance to implement the various kinds of objects that can
    be placed in a platform game, each with their own behavior. For example, I created
    a Player class that has the ability to respond to user inputs (jump, move left and right) and
    to be killed by enemies. I will also create a power-ups class whose members cannot be killed.
    but grant a bonus to the player upon picking them up.

- Why does it make sense to implement this feature with this concept? Justify
  why this is a non-trivial application of the concept in question.
  
   Java's subtyping system will be used because although all game objects share certain
   properties (position, velocity, color, etc.), there are certain behaviors that are best
   delegated only to objects of certain types. It doesn't make sense to kill a power-up,
   for example, just as it doesn't make sense to give a player some health bonus when he or she
   collides with an enemy. Subtyping will make the distinctions between different object types
   much clearer than if I were to use conditional statements to check the type of only one generic
   game object.
  


=========================
=: Your Implementation :=
=========================

- Provide an overview of each of the classes in your code, and what their
  function is in the overall game.
  
  Game: the main entry point of the game. Used to set up the drawing canvas.
  GameCourt: stores most of the core game state, including the various game objects,
    the background images and music, the game status labels, and the actions to be
    performed at every interval the game is updated (moving enemies, checking for collisions).
    The main function is to provide a central interface through which all of the other
    classes can interact.
  GameObj: an abstract class representing an object in the game that has position, velocity, and
    the ability to have its position queried for the purpose of collision detection. All
    of the game objects that can be displayed on screen and interacted with in the canvas inherit
    from this class, including Player, Enemy, PowerUp, and TerrainBlock.
  Player: stores state that is specific to the human player, including the number of lives,
    number of bananas currently held, and whether or not the player is moving to the left
    or right (used to properly handle keyboard controls). Player objects also implement Collidable,
    which is used to kill the player upon contact with an enemy or stop the player upon hitting
    a wall.
  Enemy: used to represent a game object that can harm the player upon collision. Enemies are
    programmed to move until they hit a wall, upon which they reverse direction (therefore,
    they implement Collidable).
  Collidable: interface to represent an action taken by a game object upon collision with
    another game object. Provisions are also made for actions upon objects touching, but not
    colliding (to prevent constant clipping of position). An object being collided with does
    not have to implement Collidable. (example: player collides with wall; wall does nothing
    while player stops)
  TerrainBlock: used to represent an object that can be stood upon and that prevents
    movement upon colliding with it. Terrain blocks are otherwise not very interesting,
    but they still have position and can be checked for collisions, which is why they
    are a subtype of GameObj.
  PowerUp: represents something that is beneficial to the player when he picks one up. For
    example, picking up a balloon will grant the player an extra life. PowerUps are parameterized
    by an inner enum PowerUpType, which contains image data and effectively represents what the
    power up does upon picking it up.
  FileInputMode: an enumeration representing the current state of level file parsing. Used in
    GameCourt.reset() to determine which type of game object is currently being read into the
    game.
  LevelParser: a class that reads a level file line by line and tokenizes the line into
    data that can be read when resetting the game. For example, if the game is currently
    reading in enemy data, LevelParser will return a list of strings about the enemy's data in
    the input file, including the position and type. Used in GameCourt.reset().
  CollisionDirection: an enumeration representing the direction in which two GameObjs collide.
    This class was adapted from the CIS 120 example game.
  
  
- Revisit your proposal document. What components of your plan did you end up
  keeping? What did you have to change? Why?
  I originally stated that I would be using ArrayLists to keep track of game objects. This
  was made obsolete by moving the game objects into distinct lists that can be drawn independently
  of one another. Previously, I had no way of guaranteeing that the player would be drawn over
  everything else unless I placed the player last in the list.

- Were there any significant stumbling blocks while you were implementing your
  game (related to your design, or otherwise)?
  The main stumbling block that I encountered was implementing collision detection properly. I
  found it to be a challenge to think through all of the different corner cases, and I often
  found initially that objects very far away from each other were registering collisions because
  of the simplest mistakes in my logic (using and versus or, for example). 

- Evaluate your design. Is there a good separation of functionality? How well is
  private state encapsulated? What would you refactor, if given the chance?
  I am, overall, pleased with my design. Given the limited time that I had to write my
  code, I would say that I did a generally good job adhering to good design principles. If there
  is anything I would change, I would definitely try to reduce the size of GameCourt because I
  feel that it's responsible for too much functionality. If I end up working on my game at another
  time, I will probably split non-gameplay-related things (such as level processing) into different 
  classes entirely.


========================
=: External Resources :=
========================

- Cite any external resources (libraries, images, tutorials, etc.) that you may
  have used while implementing your game.
  
  Playing audio: http://stackoverflow.com/questions/26305/how-can-i-play-sound-in-java
  Swing dialog boxes: http://docs.oracle.com/javase/7/docs/api/javax/swing/JOptionPane.html
  File IO: https://docs.oracle.com/javase/7/docs/api/java/util/Scanner.html 
  Backgrounds/sprites: http://www.spriters-resource.com/snes/dkc/
  Music:
    Title theme: https://www.youtube.com/watch?v=KTnMpJIaMyk
    Level theme: https://www.youtube.com/watch?v=uPTm-8YqJfw
    Gane over theme: https://www.youtube.com/watch?v=i9ZhA0leIcQ
  
  Image files were edited in GIMP for transparency support.
  Sound files were converted to WAV format using Audacity.


