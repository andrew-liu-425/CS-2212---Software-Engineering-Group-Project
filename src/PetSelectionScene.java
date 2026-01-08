import javafx.scene.Scene;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.io.IOException;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;

/** Represents the class where the player chooses the pet type. */
public class PetSelectionScene extends GameScene {
    
    /** The scene to hold UI elements. */
    private Scene scene;
    /** The image view for the background. */
    private ImageView backgroundImage;
    /** The text to display the title. */
    private Text title;
    /** The vbox to hold the UI elements. */
    private VBox box;

    /**
     * Constructs the pet selection scene.
     * <p>
     * In this scene, the player can select a pet type (Dog, Cat, Duck, or Dragon).
     * @param screenManager The screen manager to manage scene transitions.
     */
    public PetSelectionScene(ScreenManager screenManager) throws IOException {

        // Create the root pane
        StackPane root = new StackPane();
        root.setPrefSize(Main.WIDTH, Main.HEIGHT);

        // Background image
        backgroundImage = new ImageView();
        ImageManager.loadImage(backgroundImage, "../assets/backgrounds/MainMenuBackground.png");
        ImageManager.scaleBackground(backgroundImage, Main.WIDTH, Main.HEIGHT);
        ImageManager.registerImage(backgroundImage);
        root.getChildren().add(backgroundImage);

        // Title text
        Font customFont = TextManager.loadCustomFont("../assets/fonts/impact/impact.ttf", 80);
        title = new Text("SELECT YOUR PET");
        title.setFont(customFont != null ? customFont : Font.font(80));
        TextManager.registerText(title, "title");
        StackPane.setAlignment(title, Pos.TOP_CENTER);
        root.getChildren().add(title);

        int buttonSize = 200;

        // Create buttons for each pet type
        Button dogButton = createPetButton("Dog", "../assets/sprites/teenDogSprite.png", 0, 0, buttonSize);
        Button catButton = createPetButton("Cat", "../assets/sprites/teenCatSprite.png", 0, 0, buttonSize);
        Button duckButton = createPetButton("Duck", "../assets/sprites/teenDuckSprite.png", 0, 0, buttonSize);
        Button dinoButton = createPetButton("Dinosaur", "../assets/sprites/teenDragonSprite.png", 0, 0, buttonSize);

        // Back button
        Button backButton = new Button("Back");
        backButton.setFont(Font.font("Arial", 16));
        backButton.setStyle("-fx-background-color: lightgray; -fx-text-fill: black;");

        dogButton.setOnAction(e -> {
            TextManager.clearTextElements();
            ButtonManager.clearButtonElements();
            screenManager.getGameScene("PetName").setPetType_ForPetNameSceneOnly("Dog");
            screenManager.switchTo("PetName");
        });

        catButton.setOnAction(e -> {
            TextManager.clearTextElements();
            ButtonManager.clearButtonElements();
            screenManager.getGameScene("PetName").setPetType_ForPetNameSceneOnly("Cat");
            screenManager.switchTo("PetName");
        });

        duckButton.setOnAction(e -> {
            TextManager.clearTextElements();
            ButtonManager.clearButtonElements();
            screenManager.getGameScene("PetName").setPetType_ForPetNameSceneOnly("Duck");
            screenManager.switchTo("PetName");
        });

        dinoButton.setOnAction(e -> {
            TextManager.clearTextElements();
            ButtonManager.clearButtonElements();
            screenManager.getGameScene("PetName").setPetType_ForPetNameSceneOnly("Dinosaur");
            screenManager.switchTo("PetName");
        });

        backButton.setOnAction(e -> {
            TextManager.clearTextElements();
            ButtonManager.clearButtonElements();  
            screenManager.switchTo("NewAndLoad");
        });

        GridPane buttonGrid = new GridPane();
        buttonGrid.setAlignment(Pos.CENTER);
        buttonGrid.setHgap(20); // Horizontal gap between buttons
        buttonGrid.setVgap(20); // Vertical gap between buttons
        
        // Add buttons to the grid
        buttonGrid.add(dogButton, 0, 0);
        buttonGrid.add(catButton, 1, 0);
        buttonGrid.add(duckButton, 0, 1);
        buttonGrid.add(dinoButton, 1, 1);

        box = new VBox(20);
        box.setPadding(new Insets(20));
        box.setAlignment(Pos.CENTER);
        box.setTranslateY(Main.HEIGHT * 0.1);
        box.getChildren().addAll(buttonGrid, backButton);

        root.getChildren().add(box);

        // ❓ Circular Info Button
        Button infoButton = new Button("?");
        infoButton.setStyle(
            "-fx-background-radius: 50%; " +
            "-fx-background-color: white; " +
            "-fx-text-fill: red; " +
            "-fx-font-weight: bold; " +
            "-fx-font-size: 20px; " +
            "-fx-border-color: lightgray; " +
            "-fx-border-radius: 50%;"
        );
        infoButton.setPrefSize(36, 36);
        infoButton.setMaxSize(36, 36);
        StackPane.setAlignment(infoButton, Pos.TOP_RIGHT);
        StackPane.setMargin(infoButton, new Insets(12));

        // Info button for pets
        infoButton.setOnAction(e -> {
            try {
                PetInfoScene infoScene = new PetInfoScene(screenManager);
                screenManager.addScreen("PetInfo", infoScene.getScene(), infoScene);
            }
            catch (IOException ex) {
                ex.printStackTrace();
            }
            screenManager.switchTo("PetInfo");
        });

        root.getChildren().add(infoButton);

        scene = new Scene(root, Main.WIDTH, Main.HEIGHT);

        scene.widthProperty().addListener((obs, oldVal, newVal) -> updateScaling());
        scene.heightProperty().addListener((obs, oldVal, newVal) -> updateScaling());
    }

    /**
     * Creates a button for the pet selection scene.
     * @param petName The name of the pet type.
     * @param spriteSheetPath The path to the sprite sheet image.
     * @param col The column index of the sprite in the sprite sheet.
     * @param row The row index of the sprite in the sprite sheet.
     * @param buttonSize The size of the button.
     * @return The created button.
     */
    public Button createPetButton(String petName, String spriteSheetPath, int col, int row, double buttonSize) {
        Button button = new Button();

        Image spriteSheet = new Image("file:" + spriteSheetPath);
        ImageView imageView = new ImageView(spriteSheet);

        int spriteWidth = 2640 / 4; 
        int spriteHeight = 5280 / 8; 

        imageView.setViewport(new Rectangle2D(col * spriteWidth, row * spriteHeight, spriteWidth, spriteHeight));
        imageView.setFitWidth(buttonSize);
        imageView.setFitHeight(buttonSize * (spriteHeight / (double) spriteWidth)); 

        button.setGraphic(imageView);
        button.setStyle("-fx-background-color: transparent;"); 

        return button;
    }

    /** Returns the scene.
     * 
     * @return The scene.
     */
    public Scene getScene() {
        return scene;
    }

    /** 
     * Updates the scaling of the UI elements based on the scene size.
     */
    private void updateScaling() {
        double width = scene.getWidth();
        double height = scene.getHeight();
    
        TextManager.scaleText(title, 100, width, height, Main.WIDTH, Main.HEIGHT); 
        TextManager.translateText(title, width, height, 0.0, 0.1); 
        
        box.setTranslateY(height * 0.05);
        ButtonManager.scaleButtonContainer(box, width, height, Main.WIDTH, Main.HEIGHT);
        double scaleFactor = Math.min(width / Main.WIDTH, height / Main.HEIGHT);
        box.setScaleX(scaleFactor);  
        box.setScaleY(scaleFactor); 
        
        ImageManager.scaleBackground(backgroundImage, width, height);
    }
}
