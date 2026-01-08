import javafx.scene.Scene;

/** Represents a custom data structure to be used in the screen manager class.
 * <p>
 * Stores scene information, including string name, scene, and class name.
 */
public class Triple {
    
    /** The string screen name of the scene. */
    private String screenName;
    /** The scene object of the screen. */
    private Scene scene;
    /** The class name of the screen. */
    private GameScene gameScene;

    /**
     * Constructs a new triple data structure containing the info in the parameters.
     * @param screenName the string screen name
     * @param scene the scene object for the screen
     * @param gameScene the class object for the screen
     */
    public Triple(String screenName, Scene scene, GameScene gameScene) {
        this.screenName = screenName;
        this.scene = scene;
        this.gameScene = gameScene;
    }

    /**
     * Returns the string screen name
     * @return the screen name
     */
    public String getScreenName() {
        return screenName;
    }

    /**
     * Returns the scene object
     * @return the scene object
     */
    public Scene getScene() {
        return scene;
    }

    /**
     * Returns the class object
     * @return the class object
     */
    public GameScene getGameScene() {
        return gameScene;
    }

}
