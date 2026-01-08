import java.io.IOException;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.geometry.Pos;

/** Represents the setting screen.
 * <p>
 * This screen contains buttons to save the game as well as navigate between certain screens.
 */
public class SettingsScene extends GameScene {
    /** Scene to hold UI elements. */
    private Scene scene;
    /** The background image. */
    private ImageView backgroundImageView;
    /** The text for "SETTINGS" */
    private Text title;
    /** The text that appears when you save. */
    private Text saveText;
    /** The containers for buttons. */
    private VBox buttonContainer; 
    /** The pet object. */
    private Pet pet;

    /**
     * Constructs the settings screen.
     * <p>
     * This screen contains buttons to save the game as well as navigate between certain screens.
     * @param screenManager the screen manager to handle transitions.
     */
    public SettingsScene(ScreenManager screenManager) throws IOException {

        StackPane root = new StackPane();
        root.setPrefSize(Main.WIDTH, Main.HEIGHT);

        // Load background image
        backgroundImageView = new ImageView();
        ImageManager.loadImage(backgroundImageView, "../assets/backgrounds/MainMenuBackground.png");
        ImageManager.scaleBackground(backgroundImageView, Main.WIDTH, Main.HEIGHT);

        // Title text
        title = new Text("SETTINGS");
        title.setFont(TextManager.loadCustomFont("../assets/fonts/impact/impact.ttf", 175));
        title.setStyle("-fx-fill: black;");
        StackPane.setAlignment(title, Pos.TOP_CENTER);
        TextManager.registerText(title, "title");

        saveText = new Text("Game successfully saved!");
        saveText.setVisible(false);
        saveText.setFont(TextManager.loadCustomFont("../assets/fonts/impact/impact.ttf", 175));
        saveText.setStyle("-fx-fill: black;");
        TextManager.registerText(saveText, "paragraph");

        VBox textBox = new VBox(40);
        textBox.setAlignment(Pos.TOP_CENTER);
        textBox.getChildren().addAll(title, saveText);

        // VBox for buttons
        buttonContainer = new VBox(10);
        buttonContainer.setAlignment(Pos.CENTER);

        // Create buttons
        Button backToPlaygroundButton = new Button("Back");
        Button saveButton = new Button("Save");
        Button exitButton = new Button("Exit");

        // Button event handlers
        backToPlaygroundButton.setOnAction(e -> {
            if (!pet.isDead()) {
                saveText.setVisible(false);
                TextManager.clearTextElements();
                ButtonManager.clearButtonElements();  
                screenManager.transition("Playground", false);
            }
            else {
                backToPlaygroundButton.setDisable(true);
            }
        });
        saveButton.setOnAction(e -> {
            saveText.setVisible(true);
            GameState gameState = new GameState(pet);
            SaveLoadManager.saveGame(pet.getName(), gameState);
        }
        );
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
        ButtonManager.registerButton(backToPlaygroundButton);
        ButtonManager.registerButton(saveButton);
        ButtonManager.registerButton(exitButton);

        // Add buttons to container
        buttonContainer.getChildren().addAll(backToPlaygroundButton, saveButton, exitButton);

        // Add components to root
        root.getChildren().addAll(backgroundImageView, textBox, buttonContainer);

        // Create scene
        scene = new Scene(root, Main.WIDTH, Main.HEIGHT);
        addResizeListeners();
        
        // Enforce 16:9 aspect ratio
        Main.enforceAspectRatio(screenManager.getStage());
    }

    /**
     * Returns the scene for UI elements.
     * @return the scene
     */
    public Scene getScene() {
        return scene;
    }

    /**
     * Helper method to dynamically rescale UI elements.
     * @see #updateScaling()
     */
    private void addResizeListeners() {
        scene.widthProperty().addListener((obs, oldVal, newVal) -> updateScaling());
        scene.heightProperty().addListener((obs, oldVal, newVal) -> updateScaling());
    }

    /**
     * Helper method to dynamically rescale UI elements.
     */
    private void updateScaling() {
        double width = scene.getWidth();
        double height = scene.getHeight();
    
        // Adjust title position
        TextManager.translateText(title, width, height, 0, 0.1);
        buttonContainer.setTranslateY(height * 0.05);
    
        // Scale title
        TextManager.scaleText(title, 150, width, height, Main.WIDTH, Main.HEIGHT);
        TextManager.scaleText(saveText, 50, width, height, Main.WIDTH, Main.HEIGHT);

        ButtonManager.scaleButtonContainer(buttonContainer, width, height, Main.WIDTH, Main.HEIGHT);
        ButtonManager.translateContainer(buttonContainer, 0.0, 0.125);

        // Scale background image
        ImageManager.scaleBackground(backgroundImageView, width, height);
    }

    /**
     * {@inheritDoc}
     * 
     * Sets the pet object for this game scene.
     *
     * @param pet The pet to assign to this scene.
     */
    @Override
    public void setPet(Pet pet) {
        this.pet = pet;
    }
}
