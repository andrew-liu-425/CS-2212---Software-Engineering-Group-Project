import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.Font;

import java.io.IOException;

import javafx.geometry.Pos;
import javafx.scene.control.Button;

/** Represents the scene that contains the new and load game button. */
public class LoadAndNewGameScene extends GameScene {

    /** The scene that holds the UI elements. */
    private Scene scene;
    /** The image view for the background. */
    private ImageView backgroundImage;
    /** The layout for the buttons. */
    private VBox layout;
    /** The text that welcomes the user. */
    private Text welcomeText;
    /** The new game button. */
    private Button newGameButton;
    /** The load game button. */
    private Button loadGameButton;

    /** 
     * Creates a new LoadAndNewGameScene.
     * <p>
     * This scene holds the buttons to create a new game or load an existing one.
     * @param screenManager The ScreenManager to handle scene transitions.
     */
    public LoadAndNewGameScene(ScreenManager screenManager) throws IOException {

        // holds UI elements
        StackPane root = new StackPane();
        root.setPrefSize(Main.WIDTH, Main.HEIGHT);

        // Load and scale the background image
        backgroundImage = new ImageView();
        ImageManager.loadImage(backgroundImage, "../assets/backgrounds/MainMenuBackground.png");
        ImageManager.scaleBackground(backgroundImage, Main.WIDTH, Main.HEIGHT);
        ImageManager.registerImage(backgroundImage);
        root.getChildren().add(backgroundImage);

        // Create a layout for the buttons
        layout = new VBox(30);
        layout.setAlignment(Pos.CENTER); 

        // Welcome text
        welcomeText = new Text("Welcome to the game!\nTo commence your journey click the buttons below:");
        Font customFont = TextManager.loadCustomFont("../assets/fonts/impact/impact.ttf", 36);
        welcomeText.setFont(customFont != null ? customFont : Font.font(36));
        welcomeText.setStyle("-fx-fill: black;");
        TextManager.registerText(welcomeText, "paragraph");

        // buttons for new game load game
        newGameButton = new Button("New Game");
        loadGameButton = new Button("Load Game");

        newGameButton.setFont(Font.font("Arial", 16));
        newGameButton.setStyle("-fx-background-color: lightgray; -fx-text-fill: black;");
        ButtonManager.registerButton(newGameButton);
        newGameButton.setOnAction(e -> {
            TextManager.clearTextElements();
            ButtonManager.clearButtonElements();
            screenManager.switchTo("PetSelection");
        });

        loadGameButton.setFont(Font.font("Arial", 16));
        loadGameButton.setStyle("-fx-background-color: lightgray; -fx-text-fill: black;");
        ButtonManager.registerButton(loadGameButton);
        loadGameButton.setOnAction(e -> {

            try {
                LoadGameScene loadGameScene = new LoadGameScene(screenManager);
            screenManager.addScreen("LoadGame", loadGameScene.getScene(), loadGameScene);
            }
            catch (IOException ex) {
                ex.printStackTrace();
            }
            
            TextManager.clearTextElements();
            ButtonManager.clearButtonElements();
            
            screenManager.switchTo("LoadGame");
        });

        // Add buttons to layout
        layout.getChildren().addAll(newGameButton, loadGameButton);

        // Back Button below centered
        Button backButton = new Button("Back");
        backButton.setFont(Font.font("Arial", 16));
        backButton.setStyle("-fx-background-color: lightgray; -fx-text-fill: black;");
        ButtonManager.registerButton(backButton);
        backButton.setOnAction(e -> {
            TextManager.clearTextElements();
            ButtonManager.clearButtonElements();
            screenManager.switchTo("MainMenu");
        });

        // Add backButton to layout
        layout.getChildren().add(backButton);

        // Add layout to root
        root.getChildren().addAll(layout, welcomeText);

        scene = new Scene(root, Main.WIDTH, Main.HEIGHT);

        // Listen to scene size changes to update scaling
        scene.widthProperty().addListener((obs, oldVal, newVal) -> updateScaling());
        scene.heightProperty().addListener((obs, oldVal, newVal) -> updateScaling());
    }

    /**
     * Returns the scene for this LoadAndNewGameScene.
     * @return The scene for this LoadAndNewGameScene.
     */
    public Scene getScene() {
        return scene;
    }

    /** 
     * Updates the scaling of UI elements based on the current scene size.
     */
    private void updateScaling() {
        double width = scene.getWidth();
        double height = scene.getHeight();

        // Update paragraph scaling 
        TextManager.scaleText(welcomeText, 36, width, height, Main.WIDTH, Main.HEIGHT); 
        TextManager.translateText(welcomeText, width, height, 0.0, -0.3);

        // Update button scaling
        ButtonManager.scaleButtonContainer(layout, width, height, Main.WIDTH, Main.HEIGHT);
        ButtonManager.translateContainer(layout, 0.0, 0.05);

        // Scale background image
        ImageManager.scaleBackground(backgroundImage, width, height);
    }

    /** Returns the new game button.
     * 
     * @return the new game button.
     */
    @Override
    public Button getNewGameButton() {
        return newGameButton;
    }

    /**
     * Returns the load game button.
     * @return the load game button.
     */
    @Override
    public Button getLoadGameButton() {
        return loadGameButton;
    }

    /**
     * Returns the welcome text.
     * @return the welcome text.
     */
    @Override
    public Text getWelcomeText() {
        return welcomeText;
    }
}
