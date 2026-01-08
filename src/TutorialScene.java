import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;

/** Represents the screen where text describing how to play the game is stored and displayed. */
public class TutorialScene extends GameScene {
    /** The scene object to hold UI elements. */
    private Scene scene;
    /** The text container. */
    private VBox textContainer;
    /** The screen manager to handle screen transitions. */
    private ScreenManager screenManager;
    /** The background image. */
    private ImageView backgroundImage;

    /**
     * Constructs a new tutorial scene.
     * <p>
     * This screen gives a short tutorial on how to play the game.
     * @param screenManager the screen manager for screen transitions.
     */
    public TutorialScene(ScreenManager screenManager) throws IOException {
        this.screenManager = screenManager;
        initializeScene();
    }

    /** Initializes the tutorial text and back button. */
    private void initializeScene() throws IOException{
        StackPane root = new StackPane();
        root.setPrefSize(Main.WIDTH, Main.HEIGHT);

        textContainer = new VBox(20);
        textContainer.setAlignment(Pos.CENTER);

        backgroundImage = new ImageView();
        ImageManager.loadImage(backgroundImage, "../assets/backgrounds/MainMenuBackground.png");
        ImageManager.scaleBackground(backgroundImage, Main.WIDTH, Main.HEIGHT);
        ImageManager.registerImage(backgroundImage);
        root.getChildren().add(backgroundImage);

        // Read the contents of Instructions.txt
        String filePath = "../assets/Instructions.txt";
        TextFlow tutorialTextFlow = new TextFlow();
        tutorialTextFlow.setTextAlignment(TextAlignment.CENTER);

        // writes tutorial text
        try {
            List<String> lines = Files.readAllLines(Paths.get(filePath)); // Read file as lines
            for (int i = 0; i < lines.size(); i++) {
                Text lineText = new Text(lines.get(i) + "\n");
                lineText.setFont(Font.font("Arial", 20)); // Slightly larger font size
                
                // Bold the first line
                if (i == 0) {
                    lineText.setFont(Font.font("Arial", FontWeight.BOLD, 22)); // Bigger & bold
                }
                
                tutorialTextFlow.getChildren().add(lineText);
            }
        } catch (IOException e) {
            e.printStackTrace();
            tutorialTextFlow.getChildren().add(new Text("Error loading tutorial."));
        }

        // creates back button
        Button menuButton = new Button("Main Menu");
        menuButton.setFont(Font.font("Arial", 16));
        menuButton.setStyle("-fx-background-color: lightgray; -fx-text-fill: black;");
        menuButton.setOnAction(e -> screenManager.switchTo("MainMenu"));

        textContainer.getChildren().addAll(tutorialTextFlow, menuButton);
        root.getChildren().add(textContainer);

        scene = new Scene(root, Main.WIDTH, Main.HEIGHT);

        scene.widthProperty().addListener((obs, oldVal, newVal) -> updateScaling());
        scene.heightProperty().addListener((obs, oldVal, newVal) -> updateScaling());
    }

    /** Returns the scene that holds UI elements 
     * @return the scene
    */
    public Scene getScene() {
        return scene;
    }

    /** Helper method to update the scaling of UI elements. */
    private void updateScaling() {
        double currentWidth = scene.getWidth();
        double currentHeight = scene.getHeight();
        ImageManager.scaleBackground(backgroundImage, currentWidth, currentHeight);
    }
}
