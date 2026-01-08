import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.scene.text.Font;

import java.io.IOException;

import javafx.geometry.Pos;
import javafx.scene.control.Button;

/** Represents the info screen on the new game screen that the player can read to learn about the stat differences on the pets. */
public class PetInfoScene extends GameScene {

    /** The scene for UI elements. */
    private Scene scene;
    /** The background image for the scene. */
    private ImageView backgroundImage;

    /** Sprite images */
    private ImageView sprite1, sprite2, sprite3, sprite4;
    /** Sprite sheets for pets */
    private Image catSheet, dogSheet, duckSheet, dragonSheet;

    /** Number of rows and columns in sprite sheets */
    private int rows = 8;
    private int columns = 4;    

    /**
     * Creates a new PetInfoScene.
     * <p>
     * This scene displays information about the pets available in the game.
     * @param screenManager The ScreenManager to manage scene transitions.
     */
    public PetInfoScene(ScreenManager screenManager) throws IOException {
        // UI container
        StackPane root = new StackPane();
        root.setPrefSize(Main.WIDTH, Main.HEIGHT);

        // Background
        backgroundImage = new ImageView();
        ImageManager.loadImage(backgroundImage, "../assets/backgrounds/MainMenuBackground.png");
        ImageManager.scaleBackground(backgroundImage, Main.WIDTH, Main.HEIGHT);
        root.getChildren().add(backgroundImage);

        VBox content = new VBox(30);
        content.setAlignment(Pos.CENTER);

        // Load sprite sheets
        catSheet = new Image("file:../assets/sprites/teenCatSprite.png");
        dogSheet = new Image("file:../assets/sprites/teenDogSprite.png");
        duckSheet = new Image("file:../assets/sprites/duckSprite.png"); 
        dragonSheet = new Image("file:../assets/sprites/teenDragonSprite.png");

        // Initialize ImageViews for sprites
        sprite1 = new ImageView();
        sprite2 = new ImageView();
        sprite3 = new ImageView();
        sprite4 = new ImageView();

        // Extract a frame for each sprite
        sprite1.setImage(SpriteManager.extractFrame(catSheet, 2, 2, columns, rows)); 
        sprite2.setImage(SpriteManager.extractFrame(dogSheet, 2, 2, columns, rows)); 
        sprite3.setImage(SpriteManager.extractFrame(duckSheet, 2, 2, columns, rows)); 
        sprite4.setImage(SpriteManager.extractFrame(dragonSheet, 2, 2, columns, rows)); 

        // Scale images
        ImageManager.registerImage(sprite1);
        ImageManager.registerImage(sprite2);
        ImageManager.registerImage(sprite3);
        ImageManager.registerImage(sprite4);

        ImageManager.scaleImage(sprite1, 67, 67);
        ImageManager.scaleImage(sprite2, 67, 67);
        ImageManager.scaleImage(sprite3, 67, 67);
        ImageManager.scaleImage(sprite4, 67, 67);
        
        // PET ROWS
        content.getChildren().add(createRow("Cat", sprite1, "Health: 80, Sleep: 120, Fullness: 100, Happiness: 80"));
        content.getChildren().add(createRow("Dog", sprite2, "Health: 100, Sleep: 100, Fullness: 100, Happiness: 100"));
        content.getChildren().add(createRow("Duck", sprite3, "Health: 100, Sleep: 100, Fullness: 75, Happiness: 125"));
        content.getChildren().add(createRow("Dragon", sprite4, "Health: 120, Sleep: 80, Fullness: 80, Happiness: 80"));

        // Back button
        Button backButton = new Button("Back");
        ButtonManager.registerButton(backButton);
        backButton.setOnAction(e -> screenManager.switchTo("PetSelection"));

        VBox layout = new VBox(40);
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(content, backButton);

        root.getChildren().add(layout);
        scene = new Scene(root, Main.WIDTH, Main.HEIGHT);

        addResizeListeners();
        Main.enforceAspectRatio(screenManager.getStage());
    }

    /** Helper method to create a row of text and images. */
    private HBox createRow(String name, ImageView sprite, String stats) {
        HBox row = new HBox(30);
        row.setAlignment(Pos.CENTER);

        VBox infoBox = new VBox(5);
        infoBox.setAlignment(Pos.CENTER_LEFT);

        Text nameText = new Text(name);
        nameText.setFont(Font.font("Arial", 20));
        Text statText = new Text(stats);
        statText.setFont(Font.font("Arial", 16));

        infoBox.getChildren().addAll(nameText, statText);
        row.getChildren().addAll(sprite, infoBox);

        return row;
    }

    /**
     * Returns the scene for this PetInfoScene.
     * @return The scene for this PetInfoScene.
     */
    public Scene getScene() {
        return scene;
    }

    /**
     * Adds resize listeners to adjust scaling dynamically.
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

        // Scale the background image
        ImageManager.scaleBackground(backgroundImage, width, height);
    }
}
