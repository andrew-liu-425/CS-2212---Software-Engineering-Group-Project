import java.io.IOException;
import java.time.LocalTime;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.geometry.Pos;
import javafx.scene.control.Button;

/** Represents the main menu scene.
 * <p>
 * This screen containss buttons for play, tutorial, parental controls, credits, and exit.
 */
public class MainMenuScene extends GameScene {

    /** Scene to hold UI elements */
    private Scene scene;
    /** Screen manager to hold global variables. */
    private ScreenManager screenManager;
    /** UI elements */
    private VBox buttonContainer;
    /** UI elements */
    private HBox spriteContainer;
    /** Title text */
    private Text title;
    /** Sprite images */
    private ImageView sprite1, sprite2;
    /** Sprite sheets for cat and dog */
    private Image catSheet, dogSheet;

    private int rows = 8;
    private int columns = 4;


    /** The background image. */
    private ImageView backgroundImageView;

    /**
     * Creates the main menu scene.
     * <p>
     * This scene contains buttons for play, tutorial, parental controls, credits, and exit.
     * @param screenManager The screen manager to handle scene transitions.
     */
    public MainMenuScene(ScreenManager screenManager) throws IOException {

        this.screenManager = screenManager;

        // UI elements
        StackPane root = new StackPane();
        root.setPrefSize(Main.WIDTH, Main.HEIGHT);

        // Load background image
        backgroundImageView = new ImageView();
        ImageManager.loadImage(backgroundImageView, "../assets/backgrounds/MainMenuBackground.png");
        ImageManager.scaleBackground(backgroundImageView, Main.WIDTH, Main.HEIGHT);

        // Title text
        Font customFont = TextManager.loadCustomFont("../assets/fonts/impact/impact.ttf", 80);
        title = new Text("EVOPETS");
        title.setFont(customFont != null ? customFont : Font.font(175));
        TextManager.registerText(title, "title");
        StackPane.setAlignment(title, Pos.TOP_CENTER);

        spriteContainer = new HBox(500);
        
        spriteContainer.setAlignment(Pos.CENTER);

        // Load sprite sheets
        catSheet = new Image("file:../assets/sprites/teenCatSprite.png");
        dogSheet = new Image("file:../assets/sprites/teenDogSprite.png");

        // Initialize ImageViews for sprites
        sprite1 = new ImageView();
        sprite2 = new ImageView();

        // Extract a frame for each sprite (row and column are indices)
        Image catSprite = SpriteManager.extractFrame(catSheet, 0, 0, columns, rows); 
        Image dogSprite = SpriteManager.extractFrame(dogSheet, 2, 2, columns, rows); 

        // Set the extracted images to the ImageViews
        sprite1.setImage(catSprite);
        sprite2.setImage(dogSprite);

        // Scales images
        ImageManager.registerImage(sprite1);
        ImageManager.registerImage(sprite2);

        ImageManager.scaleImage(sprite1, 67, 67);
        ImageManager.scaleImage(sprite2, 67, 67);
        
        spriteContainer.getChildren().addAll(sprite1, sprite2);

        // VBox for buttons
        buttonContainer = new VBox(10);
        buttonContainer.setAlignment(Pos.CENTER);

        // Create buttons
        Button playButton = new Button("Play");
        Button tutorialButton = new Button("Instructions");
        Button parentalControlsButton = new Button("Parental Controls");
        Button creditsButton = new Button("Credits");
        Button exitButton = new Button("Exit");

        // Button event handlers
        playButton.setOnAction(e -> {
            TextManager.clearTextElements();
            ButtonManager.clearButtonElements();

            if (checkIfValidTime() || !screenManager.getTimeInformation().getCheckTimes().get()) {
                screenManager.getGameScene("NewAndLoad").getNewGameButton().setDisable(false);
                screenManager.getGameScene("NewAndLoad").getLoadGameButton().setDisable(false);
                screenManager.getGameScene("NewAndLoad").getWelcomeText().setText("Welcome to the game!\nTo commence your journey click the buttons below:");
            }   
            else {
                screenManager.getGameScene("NewAndLoad").getNewGameButton().setDisable(true);
                screenManager.getGameScene("NewAndLoad").getLoadGameButton().setDisable(true);
                screenManager.getGameScene("NewAndLoad").getWelcomeText().setText("You are not allowed to play EvoPets at this time.");
            }

            screenManager.switchTo("NewAndLoad"); 
        });
        tutorialButton.setOnAction(e -> {
            TextManager.clearTextElements();
            ButtonManager.clearButtonElements();
            screenManager.switchTo("Tutorial");
        });
        parentalControlsButton.setOnAction(e ->{
            TextManager.clearTextElements();
            ButtonManager.clearButtonElements(); 
            screenManager.switchTo("ParentControlPassword"); });
        creditsButton.setOnAction(e -> {
            TextManager.clearTextElements();
            ButtonManager.clearButtonElements(); 
            screenManager.switchTo("Credits"); 
        });
        exitButton.setOnAction(e -> {
            TimeInformation timeInfo = screenManager.getTimeInformation();
            timeInfo.setCheckTimesS();
            int currentTime = screenManager.getTotalPlayTime() - timeInfo.getTotalPlayTime();
            timeInfo.setTotalPlayTime(screenManager.getTotalPlayTime());
            timeInfo.updateAveragePlayTime(currentTime);
            SaveLoadManager.saveTime(timeInfo);
            Platform.exit();
        });

        // Register buttons for scaling
        ButtonManager.registerButton(playButton);
        ButtonManager.registerButton(tutorialButton);
        ButtonManager.registerButton(parentalControlsButton);
        ButtonManager.registerButton(creditsButton);
        ButtonManager.registerButton(exitButton);

        // Add buttons to container
        buttonContainer.getChildren().addAll(playButton, tutorialButton, parentalControlsButton, creditsButton, exitButton);

        // Register the button container to reset translation
        ButtonManager.registerButtonContainer(buttonContainer);

        // Add components to root
        root.getChildren().addAll(backgroundImageView, title, spriteContainer, buttonContainer);

        // Create scene
        scene = new Scene(root, Main.WIDTH, Main.HEIGHT);
        addResizeListeners();

        // Enforce 16:9 aspect ratio
        Main.enforceAspectRatio(screenManager.getStage());

        Keyboard.attach(buttonContainer);
    }

    /**
     * Returns the scene.
     * @return The scene.
     */
    public Scene getScene() {
        return scene;
    }

    /**
     * Helper method that adds listeners to the scene for resizing.
     * @see #updateScaling()
     */
    private void addResizeListeners() {
        scene.widthProperty().addListener((obs, oldVal, newVal) -> updateScaling());
        scene.heightProperty().addListener((obs, oldVal, newVal) -> updateScaling());
    }

    /**
     * Updates the scaling of UI elements based on the current scene size.
     */
    private void updateScaling() {
        double width = scene.getWidth();
        double height = scene.getHeight();

        // Scale and translate the title
        TextManager.scaleText(title, 175, width, height, Main.WIDTH, Main.HEIGHT);
        TextManager.translateText(title, width, height, 0, 0.06); 

        // Scale and translate buttons
        ButtonManager.scaleButtonContainer(buttonContainer, width, height, Main.WIDTH, Main.HEIGHT);
        ButtonManager.translateContainer(buttonContainer, 0.0, 0.125);
        
        // Scale the background image
        ImageManager.scaleBackground(backgroundImageView, width, height);
        ImageManager.updateScaling(width * 0.5, height *0.5);

    }

    /** Checks if the current time is within the valid range specified by the parental controls.
     * 
     * @return true if the time is valid, false otherwise.
     */
    private boolean checkIfValidTime() {
        LocalTime start = screenManager.getTimeInformation().getStartTime();
        LocalTime end = screenManager.getTimeInformation().getEndTime();
        LocalTime current = LocalTime.now();
        if (start.isBefore(end)) {
            return !current.isBefore(start) && !current.isAfter(end);
        }
        else {
            return !current.isBefore(start) || !current.isAfter(end);
        }
    }
}
