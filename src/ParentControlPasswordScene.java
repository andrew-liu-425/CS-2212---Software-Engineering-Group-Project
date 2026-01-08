import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.Font;

import java.io.IOException;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.text.TextAlignment;

/** 
 * This class represents the password entry scene for parental controls.
 * <p>
 * It provides a user interface for entering a password to access parental controls.
 */
public class ParentControlPasswordScene extends GameScene {
    
    /** The scene to hold UI elements. */
    private Scene scene;
    /** The background image for the scene. */
    private ImageView backgroundImage;
    /** The main container for the scene's UI elements. */
    private VBox mainContainer;

    /** The message prompting the user to enter a password. */
    private Text passwordMessage;
    /** The password input field. */
    private PasswordField passwordField;
    /** The buttons to go back to the main menu and to access parental controls. */
    private Button backButton, submitButton;
    /** The error message displayed when the password is incorrect. */
    private Text errorMessage;

    /**
     * Constructor for the ParentControlPasswordScene.
     * <p>
     * This initializes the scene with a password field to enter the password for parental controls.
     * @param screenManager The ScreenManager to handle scene transitions.
     */
    public ParentControlPasswordScene(ScreenManager screenManager) throws IOException {

        // holds UI elements
        StackPane root = new StackPane();
        root.setPrefSize(Main.WIDTH, Main.HEIGHT);

        // Initialize all components first to prevent null references
        backgroundImage = new ImageView();
        passwordMessage = new Text("PLEASE ENTER YOUR PASSWORD");
        passwordField = new PasswordField();
        backButton = new Button("Back");
        submitButton = new Button("Submit");
        errorMessage = new Text();

        // Background setup
        ImageManager.loadImage(backgroundImage, "../assets/backgrounds/MainMenuBackground.png");
        ImageManager.scaleBackground(backgroundImage, Main.WIDTH, Main.HEIGHT);
        ImageManager.registerImage(backgroundImage);
        root.getChildren().add(backgroundImage);

        // Title text setup
        Font impactFont = TextManager.loadCustomFont("../assets/fonts/impact/impact.ttf", 40);
        passwordMessage.setFont(impactFont != null ? impactFont : Font.font("Impact", 40));
        passwordMessage.setStyle("-fx-fill: black;");
        passwordMessage.setTextAlignment(TextAlignment.CENTER);
        passwordMessage.setWrappingWidth(600);
        TextManager.registerText(passwordMessage, "title");

        // Password field setup
        passwordField.setPromptText("Enter password");
        passwordField.setMaxWidth(300);
        passwordField.setPrefHeight(40);
        passwordField.setStyle("-fx-font-size: 20px;");

        // Error message setup
        errorMessage.setFont(Font.font(16));
        errorMessage.setStyle("-fx-fill: red;");
        errorMessage.setVisible(false);

        // Button setup
        backButton.setFont(Font.font("Arial", 24));
        backButton.setStyle("""
            -fx-background-color: linear-gradient(to bottom, #ffffff, #dcdcdc);
            -fx-text-fill: black;
            -fx-padding: 10 20;
            -fx-border-color: #b0b0b0;
            -fx-border-radius: 5;
            -fx-background-radius: 5;
        """);
        ButtonManager.registerButton(backButton);
        
        // back button logic
        backButton.setOnAction(e -> {
            TextManager.clearTextElements();
            ButtonManager.clearButtonElements();
            screenManager.switchTo("MainMenu");
        });

        // Submit button setup
        submitButton.setFont(Font.font("Arial", 24));
        submitButton.setStyle("""
            -fx-background-color: linear-gradient(to bottom, #ffffff, #dcdcdc);
            -fx-text-fill: black;
            -fx-padding: 10 20;
            -fx-border-color: #b0b0b0;
            -fx-border-radius: 5;
            -fx-background-radius: 5;
        """);
        ButtonManager.registerButton(submitButton);

        // submit button logic
        submitButton.setOnAction(e -> {
            if (passwordField.getText().equals("CS2212")) {
                passwordField.clear();
                ButtonManager.clearButtonElements();
                TextManager.clearTextElements();
                errorMessage.setVisible(false);
                screenManager.switchTo("ParentControls");
            } else {
                errorMessage.setText("Incorrect password. Try again.");
                errorMessage.setVisible(true);
                passwordField.clear();
            }
        });

        // Disable submit button until password is entered
        submitButton.setDisable(true);
        submitButton.setOpacity(0.5);

        passwordField.textProperty().addListener((observable, oldValue, newValue) -> {
            boolean isEmpty = newValue.trim().isEmpty();
            submitButton.setDisable(isEmpty);
            submitButton.setOpacity(isEmpty ? 0.5 : 1.0);
            errorMessage.setVisible(false);
        });

        // Create button container
        VBox buttonContainer = new VBox(20, backButton, submitButton);
        buttonContainer.setAlignment(Pos.CENTER);

        // Main container with all components
        mainContainer = new VBox(30, passwordMessage, passwordField, errorMessage, buttonContainer);
        mainContainer.setAlignment(Pos.CENTER);

        root.getChildren().add(mainContainer);

        scene = new Scene(root, Main.WIDTH, Main.HEIGHT);

        scene.widthProperty().addListener((obs, oldVal, newVal) -> updateScaling());
        scene.heightProperty().addListener((obs, oldVal, newVal) -> updateScaling());
    }

    /**
     * Returns the scene for this password entry screen.
     * @return The scene containing the password entry UI.
     */
    public Scene getScene() {
        return scene;
    }

    /**
     * Updates the scaling of UI elements based on the current scene dimensions.
     */
    private void updateScaling() {
        double width = scene.getWidth();
        double height = scene.getHeight();
        
        // Scale background
        ImageManager.scaleBackground(backgroundImage, width, height);

        // Scale and position title text
        TextManager.scaleText(passwordMessage, 40, width, height, Main.WIDTH, Main.HEIGHT);
        TextManager.translateText(passwordMessage, width, height, 0.0, -0.20);

        // Scale password field
        passwordField.setPrefWidth(width * 0.3);
        passwordField.setPrefHeight(height * 0.05);
        passwordField.setTranslateY(height * 0.10);

        // Scale and position main container
        ButtonManager.scaleButtonContainer(mainContainer, width, height, Main.WIDTH, Main.HEIGHT);
    }
}