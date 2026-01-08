import javafx.animation.PauseTransition;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

/** Represents the class that holds information on all the screens.
 * <p>
 * This class is responsible for global actions for the screens, such as timers and screen transitions.
 */
public class ScreenManager {

    /** The stage of the entire JavaFX application. */
    private final Stage stage;
    /** The list of all the screens in the game. */
    private ArrayList<Triple> screenMap = new ArrayList<>();
    /** The order of the gameplay screens (e.g. kitchen, bedroom) in the game. */
    private final ArrayList<String> sceneOrder = new ArrayList<>();
    /** The timer that is used to update the pet's stats. */
    private GameTimer statTimer;
    /** The timer that is used to update the pet's sleep. */
    private GameTimer sleepTimer;
    /** The timer that is used to update the pet's anger. */
    private GameTimer angryTimer;
    /** The timer that is used to update the pet's play cooldown. */
    private GameTimer playCooldownTimer;
    /** The timer that is used to update the pet's vet cooldown. */
    private GameTimer vetCooldownTimer;
    /** The current game scene that is being displayed. */
    private GameScene currentGameScene;
    /** The index of the current scene in the scene order. */
    private int currentSceneIndex = 0;

    /** The timer that represents total time elapsed. */
    private GameTimer currentTimer;
    /** The amount of time that has been elapsed in all sessions of the game. */
    private int totalPlayTime;
    /** The time information object. */
    private TimeInformation timeInfo;

    /** The delay between screen transitions, allowing time to set up UI. */
    private PauseTransition transitionDelay;

    /** The list of valid gameplay scenes that can be transitioned to. */
    private final ArrayList<String> validScenes = new ArrayList<>(List.of("Bedroom", "Kitchen", "Playground", "Vet"));

    /**
     * Creates a new ScreenManager object.
     * <p>
     * This is responsible for managing the screens in the game and their transitions.
     * It also handles the timers for the pet's stats and stores global actions for the screens.
     * @param stage The stage of the entire JavaFX application.
     */
    public ScreenManager(Stage stage, TimeInformation timeInfo) {
        this.stage = stage;
        this.timeInfo = timeInfo;
        totalPlayTime = timeInfo.getTotalPlayTime();
        this.currentTimer = new GameTimer(1, () -> this.handlePlayTimerTick());
        this.currentTimer.start();
        this.statTimer = new GameTimer(3, () -> this.handleTimerTick());
        this.sleepTimer = new GameTimer(1, () -> this.handleSleepTick());
        this.angryTimer = new GameTimer(1, () -> this.handleAngryTick());
        this.playCooldownTimer = new GameTimer(1, () -> this.handlePlayCooldownTick());
        this.vetCooldownTimer = new GameTimer(1, () -> this.handleVetCooldownTick());
        this.transitionDelay = new PauseTransition(Duration.millis(250));
    }

    /**
     * Adds a new screen to the screen manager.
     * <p>
     * This method removes any existing screen with the same name before adding the new one.
     * @param name The name of the screen.
     * @param scene The JavaFX Scene object representing the screen.
     * @param gameScene The GameScene object representing the game scene associated with this screen.
     */
    public void addScreen(String name, Scene scene, GameScene gameScene) {
        removeScreen(name);
        screenMap.add(new Triple(name, scene, gameScene));

        if (!sceneOrder.contains(name)) {
            sceneOrder.add(name);
        }
    }

    /**
     * Switches to a specific screen. Should only be called for non-gameplay screens.
     * @param name The string name of the screen to switch to.
     */
    public void switchTo(String name) {
        statTimer.pause();
        Scene scene = getScene(name);
        currentGameScene = getGameScene(name);
        stage.setScene(scene);
        currentGameScene.onEnter();
        stage.show();

        currentSceneIndex = sceneOrder.indexOf(name);
    }

    /**
     * Switches to the next screen, intended for arrow buttons. Should only be called for gameplay screens.
     */
    public void switchToNext() {
        if (isValidSceneIndex(currentSceneIndex + 1)) {
            String nextScene = sceneOrder.get(currentSceneIndex + 1);
            if (validScenes.contains(nextScene)) {
                transition(nextScene, false);
            }
        }
    }

    /**
     * Switches to the previous screen, intended for arrow buttons. Should only be called for gameplay screens.
     */
    public void switchToPrevious() {
        if (isValidSceneIndex(currentSceneIndex - 1)) {
            String prevScene = sceneOrder.get(currentSceneIndex - 1);
            if (validScenes.contains(prevScene)) {
                transition(prevScene, false);
            }
        }
    }

    /** Checks to see if a screen is a valid gameplay screen.
     * 
     * @param index The index of the screen in the scene order.
     * @return True if the index is valid, false otherwise.
     */
    private boolean isValidSceneIndex(int index) {
        return index >= 0 && index < sceneOrder.size();
    }

    /** Returns the stage.
     * 
     * @return The stage of the entire JavaFX application.
     */
    public Stage getStage() {
        return stage;
    }

    private void handlePlayTimerTick() {
        totalPlayTime = totalPlayTime + 1;
        int minutes = totalPlayTime/60;
        int seconds = totalPlayTime%60;
        getGameScene("ParentControls").setTotalPlaytimetext("Total Playtime: " + String.format("%d:%02d", minutes, seconds));
    }

    /** Calls the method of the timer in gameplay screens that occur once every few seconds. */
    private void handleTimerTick() {
        if (currentGameScene != null) {
            currentGameScene.onTimerTick();
        }
    }

    /** Calls the method of the timer in gameplay screens that occur when the pet is sleeping. */
    private void handleSleepTick() {
        if (currentGameScene != null) {
            currentGameScene.onSleepTick();
        }
    }

    /** Calls the method of the timer in gameplay screens that occur when the pet is angry. */
    private void handleAngryTick() {
        if (currentGameScene != null) {
            currentGameScene.onAngryTick();
        }
    }

    /** Calls the method of the timer in gameplay screens that occur when the pet's play is on cooldown. */
    private void handlePlayCooldownTick() {
        if (currentGameScene != null) {
            currentGameScene.onPlayCooldownTick();
        }
    } 

    /** Calls the method of the timer in gameplay screens that occur when the pet's vet is on cooldown. */
    private void handleVetCooldownTick() {
        if (currentGameScene != null) {
            currentGameScene.onVetCooldownTick();
        }
    } 

    /** Returns the stat timer.
     * 
     * @return The stat timer.
     */
    public GameTimer getStatTimer() {
        return statTimer;
    }

    /** Returns the sleep timer.
     * 
     * @return The sleep timer.
     */
    public GameTimer getSleepTimer() {
        return sleepTimer;
    }

    /** Returns the angry timer.
     * 
     * @return The angry timer.
     */
    public GameTimer getAngryTimer() {
        return angryTimer;
    }

    /** Returns the play cooldown timer.
     * 
     * @return The play cooldown timer.
     */
    public GameTimer getPlayCooldownTimer() {
        return playCooldownTimer;
    }

    /** Returns the vet cooldown timer.
     * 
     * @return The vet cooldown timer.
     */
    public GameTimer getVetCooldownTimer() {
        return vetCooldownTimer;
    }

    /** Removes a screen from the list of screens.
     * Used to free up memory.
     * @param name The name of the screen to remove.
     */
    private void removeScreen(String name) {
        for (int i = 0; i < screenMap.size(); i++) {
            if (screenMap.get(i).getScreenName().equals(name)) {
                screenMap.remove(i);
                break;
            }
        }
    }

    /** Switches to another screen with a delay to facilitate UI changes.
     * Intended for gameplay screens.
     * @param scene The name of the screen to switch to.
     * @param happy Whether the pet should enter the screen happy or not.
     */
    public void transition(String scene, boolean happy) {
        transitionDelay.stop();
        getGameScene(scene).getScreenOverlay().setScoreText("SCORE: " + String.valueOf(getGameScene(scene).getPet().getScore()));
        getGameScene(scene).getScreenOverlay().setCoinsText("COINS: " + String.valueOf(getGameScene(scene).getPet().getCoins()));
        UIUpdate.updateUI(getGameScene(scene).getPet(), getGameScene(scene).getPetOverlay().getSpriteManager(), getGameScene(scene).getScreenOverlay().getStatBars(), happy);
        transitionDelay.setOnFinished(e -> {
            switchTo(scene); 
        });
        transitionDelay.play();
    }

    /**
     * Returns the scene associated with a given name.
     * * @param name The string name of the screen.
     * * @return The Scene object associated with the screen name.
     */
    public Scene getScene(String name) {
        for (Triple entry : screenMap) {
            if (entry.getScreenName().equals(name)) {
                return entry.getScene();
            }
        }
        throw new IllegalArgumentException("You entered the wrong screen name.");
    }

    /**
     * Returns the GameScene associated with a given name.
     * @param name The string name of the screen.
     * @return The GameScene object associated with the screen name.
     */
    public GameScene getGameScene(String name) {
        for (Triple entry : screenMap) {
            if (entry.getScreenName().equals(name)) {
                return entry.getGameScene();
            }
        }
        throw new IllegalArgumentException("You entered the wrong screen name.");
    }

    /** Returns the current game scene.
     * 
     * @return The current game scene.
     */
    public GameScene getCurrentGameScene() {
        return currentGameScene;
    }

    /** Returns the current gameplay scene.
     * 
     * @return The current gameplay scene.
     */
    public String getCurrentSceneName() {
        return sceneOrder.get(currentSceneIndex);
    }

    /** Returns the current pet in the game scene.
     * 
     * @return The current pet in the game scene.
     */
    public Pet getCurrentPet() {
        return currentGameScene != null ? currentGameScene.getPet() : null;
    }

    /** Returns the total play time that has ever elapsed, including in past saves.
     * 
     * @return the total play time
     */
    public int getTotalPlayTime() {
        return totalPlayTime;
    }

    /**
     * Return the timeInformation object that contains time data.
     * @return the time info object.
     */
    public TimeInformation getTimeInformation() {
        return timeInfo;
    }

}