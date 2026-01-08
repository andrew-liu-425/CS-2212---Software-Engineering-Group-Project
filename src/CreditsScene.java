import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.io.IOException;

import javafx.geometry.Pos;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

/**
 * This class represents the credits scene in the main menu and all its UI components.
 * <p>
 * It will display the creators of EvoPets and all other relevant info.
 */
public class CreditsScene extends GameScene {

    /** The UI scene for credits. */
    private Scene scene;

    /** The image for the background picture. */
    private ImageView backgroundImage;
    /** The text "SETTINGS" */
    private Text title;
    /** The text displaying the creators of EvoPets */
    private Text devs;  
    /** The button that leads back to the main menu. */
    private Button creditToMainMenuButton; 
    /** The container that stores the UI elements of this class. */
    private VBox creditsBox;

    /**
     * Constructs the credits scene and stores it in the screen manager.
     * <p>
     * The credits scene holds all the credits information.
     * To switch to this scene, use the screen manager class.
     * @param screenManager the screen manager that stores this scene.
     */
    public CreditsScene(ScreenManager screenManager) throws IOException {

        // The stackpane root
        StackPane root = new StackPane();
        root.setPrefSize(Main.WIDTH, Main.HEIGHT);

        // Background
        backgroundImage = new ImageView();
        ImageManager.loadImage(backgroundImage, "../assets/backgrounds/MainMenuBackground.png");
        ImageManager.scaleBackground(backgroundImage, Main.WIDTH, Main.HEIGHT);
        ImageManager.registerImage(backgroundImage);
        root.getChildren().add(backgroundImage);

        // The container for the UI elements
        creditsBox = new VBox(20);
        creditsBox.setAlignment(Pos.CENTER);

        // Title
        title = new Text("CREDITS");
        Font impactFont = TextManager.loadCustomFont("../assets/fonts/impact/impact.ttf", 20);
        title.setFont(impactFont != null ? impactFont : Font.font("Impact", 20));
        title.setStyle("-fx-fill: black;");
        TextManager.registerText(title, "title");

        // Body text - smaller base font
        devs = new Text(
            "Developers:\n" +
            "• Xavier\n" +
            "• Sherry\n" +
            "• Andrew\n" +
            "• Matt\n" +
            "• Arik\n\n" +
            "Team: Group 72\n" +
            "Term: Winter 2025\n" +
            "Created as part of CS2212 at Western University"
        );
        devs.setFont(Font.font("Arial", 20));
        devs.setStyle("-fx-fill: black;");
        devs.setTextAlignment(TextAlignment.CENTER);
        devs.setWrappingWidth(600);
        TextManager.registerText(devs, "paragraph");

        // Back button (styled same as Main Menu)
        creditToMainMenuButton = new Button("Back");
        creditToMainMenuButton.setFont(Font.font("Arial", 24));
        creditToMainMenuButton.setStyle("""
            -fx-background-color: linear-gradient(to bottom, #ffffff, #dcdcdc);
            -fx-text-fill: black;
            -fx-padding: 10 20;
            -fx-border-color: #b0b0b0;
            -fx-border-radius: 5;
            -fx-background-radius: 5;
        """);
        ButtonManager.registerButton(creditToMainMenuButton);

        // The back button's behaviour
        creditToMainMenuButton.setOnAction(e -> {
            TextManager.clearTextElements();
            ButtonManager.clearButtonElements(); 
            screenManager.switchTo("MainMenu");
        });

        // Adds credits UI to root
        creditsBox.getChildren().addAll(title, devs, creditToMainMenuButton);
        ButtonManager.registerButtonContainer(creditsBox);
        root.getChildren().add(creditsBox);

        scene = new Scene(root, Main.WIDTH, Main.HEIGHT);

        // For dynamic scaling
        scene.widthProperty().addListener((obs, oldVal, newVal) -> updateScaling());
        scene.heightProperty().addListener((obs, oldVal, newVal) -> updateScaling());

        // Initial scaling
        updateScaling();
    }


    /**
     * Returns the settings scene.
     * @return the settings scene.
     */
    public Scene getScene() {
        return scene;
    }

    /**
     * This helper method is used for dynamic scaling of the UI elements of the class.
     */
    private void updateScaling() {
        double width = scene.getWidth();
        double height = scene.getHeight();

        // Scale and translate title
        TextManager.scaleText(title, 125, width, height, Main.WIDTH, Main.HEIGHT);
        TextManager.translateText(title, width, height, 0, -0.05); 

        // Scale and translate body text 
        TextManager.scaleText(devs, 28, width, height, Main.WIDTH, Main.HEIGHT);
        TextManager.translateText(devs, width, height, 0, -0.055); 

        // Scale and translate button 
        ButtonManager.scaleButtonContainer(creditsBox, width, height, Main.WIDTH, Main.HEIGHT);
        ButtonManager.translateContainer(creditsBox, 0.0, -0.05);
        
        // Adjust Y position of the button
        creditToMainMenuButton.setTranslateY(height * -0.05); 
        
        ImageManager.scaleBackground(backgroundImage, width, height);
    }
}
