import java.io.IOException;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

/**
 * Main class for the EvoPets game.
 * This class initializes the JavaFX application and sets up the main game window.
 */
public class Main extends Application {
    /** Width of the game window */
    public static final double WIDTH = 1280;
    /** Height of the game window */
    public static final double HEIGHT = 720;
    
    /**
     * Starts the JavaFX application.
     * @param primaryStage The primary stage for this application.
     */
    @Override
    public void start(Stage primaryStage) throws IOException{

        TimeInformation time = SaveLoadManager.loadTime();
        if (time == null) {
            time = new TimeInformation();
        }
        else {
            time.createCheckTimes();
        }
        // Creates all scenes that don't require the pet object
        ScreenManager screenManager = new ScreenManager(primaryStage, time);
        MainMenuScene mainMenuScene = new MainMenuScene(screenManager);
        LoadAndNewGameScene loadAndNewgameScreen = new LoadAndNewGameScene(screenManager);
        CreditsScene creditsScene = new CreditsScene(screenManager);
        SettingsScene settingsScene = new SettingsScene(screenManager);
        PetSelectionScene petSelectionScene = new PetSelectionScene(screenManager);
        PetNameScene petNameScene = new PetNameScene(screenManager);
        TutorialScene tutorialScene = new TutorialScene(screenManager);
        ParentControlPasswordScene parentControlPasswordScene = new ParentControlPasswordScene(screenManager);
        ParentControlsScene parentControlsScene = new ParentControlsScene(screenManager);
        // Pushes all scenes to the screen manager
        screenManager.addScreen("MainMenu", mainMenuScene.getScene(), mainMenuScene);
        screenManager.addScreen("Credits", creditsScene.getScene(), creditsScene);
        screenManager.addScreen("Settings", settingsScene.getScene(), settingsScene);
        screenManager.addScreen("NewAndLoad", loadAndNewgameScreen.getScene(), loadAndNewgameScreen);
        screenManager.addScreen("PetSelection", petSelectionScene.getScene(), petSelectionScene);
        screenManager.addScreen("PetName", petNameScene.getScene(), petNameScene);
        screenManager.addScreen("Tutorial", tutorialScene.getScene(), tutorialScene);
        screenManager.addScreen("ParentControlPassword", parentControlPasswordScene.getScene(), parentControlPasswordScene);
        screenManager.addScreen("ParentControls", parentControlsScene.getScene(), parentControlsScene);
        // Sets the initial scene to the main menu
        screenManager.switchTo("MainMenu");
        primaryStage.setTitle("EvoPets");
        primaryStage.setWidth(WIDTH);
        primaryStage.setHeight(HEIGHT);
        primaryStage.setMinWidth(20);
        primaryStage.setMinHeight(11);
        primaryStage.setResizable(true);
        enforceAspectRatio(primaryStage);
        primaryStage.show();
    }

    /**
     * Enforces a 16:9 aspect ratio for the game window.
     * This method ensures that the width and height of the stage maintain a 16:9 ratio.
     * @param stage The stage to enforce the aspect ratio on.
     */
    public static void enforceAspectRatio(Stage stage) {
        stage.widthProperty().addListener((obs, oldVal, newVal) -> adjustAspectRatio(stage));
        stage.heightProperty().addListener((obs, oldVal, newVal) -> adjustAspectRatio(stage));
    }
    /**
     * Adjusts the width and height of the stage to maintain a 16:9 aspect ratio.
     * This method is called whenever the width or height of the stage changes.
     * @param stage The stage to adjust.
     */
    private static void adjustAspectRatio(Stage stage) {
        Platform.runLater(() -> {
            double width = stage.getWidth();
            double height = stage.getHeight();
            if (width / height > 16.0 / 9.0) {
                stage.setHeight(width * 9.0 / 16.0);
            } else {
                stage.setWidth(height * 16.0 / 9.0);
            }
        });
    }
    
    /**
     * The main method to launch the JavaFX application.
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        launch(args);
    }
}
