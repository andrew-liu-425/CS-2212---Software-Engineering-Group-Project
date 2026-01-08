import javafx.scene.control.Button;
import javafx.scene.text.Text;

/**
 * The parent class for all scenes in the game.
 * <p>
 * This class is mostly used for the screen manager class.
 * It provides the empty methods for many methods on gameplay screens that must be accessed through the screen manager.
 * There are no functional methods in this class.
 */
public abstract class GameScene {
    
    /**
     * Represents the events that occur after every few seconds.
     * <p>
     * Every few seconds, the pet's stats besides health will decrease.
     * Additionally, the pet will gain coins.
     * The pet's states and UI will be updated accordingly.
     */
    public void onTimerTick() {
        // Default nothing. Do something in children gameplay screens.
    }

    /**
     * Represents the events that occur when the pet is sleeping.
     * <p>
     * This timer is called when the pet starts sleeping, and is no longer called when the pet stops sleeping.
     * This timer will continuously check if the pet is still sleeping, and if so, will increase the pet's energy value.
     * If not, the gameplay buttons will be enabled again, assuming all other conditions are met.
     */
    public void onSleepTick() {
       // Default nothing. Do something in children gameplay screens.
    }

    /**
     * Represents the events that occur when the pet is angry.
     * <p>
     * This timer is called when the pet is angry, and is no longer called when the pet is no longer angry.
     * This timer will continuously check if the pet is still angry.
     * If not, the gameplay buttons will be enabled again, assuming all other conditions are met.
     */
    public void onAngryTick() {
        // Default nothing. Do something in children gameplay screens.
    }

    /**
     * Represents the cooldown for the play button in the playground class.
     * <p>
     * This cooldown is called when the play button is pressed, and is no longer called when the cooldown reaches 0.
     * Every second, the cooldown is decreased until it reaches 0. When that happens, the play button will be enabled.
     */
    public void onPlayCooldownTick() {
        // Default nothing. Do something in sleep screen.
    }

    /**
     * Represents the cooldown for the vet button in the vet class.
     * <p>
     * This cooldown is called when the vet button is pressed, and is no longer called when the cooldown reaches 0.
     * Every second, the cooldown is decreased until it reaches 0. When that happens, the vet button will be enabled.
     */
    public void onVetCooldownTick() {
        // Default nothing. Do something in sleep screen.
    }

    /**
     * Represents the events that occur when the scene is entered.
     * <p>
     * On scene entry, all UI elements are brought from the previous scene to maintain consistency.
     * Additionally, the stat decrease timer will start. If there are any buttons that require a condition to work, this method also checks for that condition.
     * It updates the buttons clickablility and the pet's states accordingly.
     */
    public void onEnter() {
        // Default nothing. Do something in children gameplay screens.
    }

    /**
     * Sets the pet type so it can be used to create a pet in the pet name scene class.
     * @param name The name of the pet type.
     */
    public void setPetType_ForPetNameSceneOnly(String name) {
        // Default nothing. Only useful for PetNameScene
    }
    
    /**
     * Returns the pet object for this game.
     * @return The pet object for this game.
     */
    public Pet getPet() {
        return null;
    }

    /**
     * Sets the pet object for a certain scene.
     * Used in gameScene.setPet(pet).
     * @param pet The pet object for this game.
     */
    public void setPet(Pet pet) {
        // Default nothing.
    }

    /**
     * Returns the screen overlay object for a certain scene.
     * @return The screen overlay object for a certain scene.
     * @see ScreenOverlay
     */
    public ScreenOverlay getScreenOverlay() {
        return null;
    }

    /**
     * Returns the pet overlay object for a certain scene.
     * @return The pet overlay object for a certain scene.
     * @see PetOverlay
     */
    public PetOverlay getPetOverlay() {
        return null;
    }

    /**
     * Updates the pet's states upon every stat increase or decrease.
     * <p>
     * If the pet is dead, all buttons besides settings are no longer functional.
     * If the pet is angry, all buttons that don't increase happiness are not functional.
     * If the pet is sleeping, all buttons besides settings are no longer functional, and the pet will receive a health penalty.
     * If the pet is hungry, they will lose health and happiness over time.
     */
    protected void updatePetState() {

    }

    /**
     * Sets the total playtime text for parental controls.
     * <p>
     * This text is displayed on the screen to indicate the total playtime of the game.
     * @param text The text representing the total playtime.
     */
    public void setTotalPlaytimetext(String text) {

    }

    /**
     * Retrieves the "New Game" button.
     * <p>
     * This button is used to start a new game and may be disabled based on parental control settings.
     * @return The button used to start a new game.
     */
    public Button getNewGameButton() {
        return null;
    }

    /**
     * Retrieves the "Load Game" button.
     * <p>
     * This button is used to load a previously saved game and may be disabled based on parental control settings.
     * @return The button used to load a saved game.
     */
    public Button getLoadGameButton() {
        return null;
    }

    /**
     * Retrieves the welcome text displayed on the new and load game screen.
     * <p>
     * This text provides a greeting or instructions to the player before they start or load a game.
     * @return The welcome text displayed on the screen.
     */
    public Text getWelcomeText() {
        return null;
    }
}
