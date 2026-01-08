import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/** Represents the screen where you can choose a past save file to load your game into. */
public class LoadGameScene extends GameScene {
    

    /** The scene that holds the UI elements. */
    private Scene scene;
    /** The background image. */
    private ImageView backgroundImage;
    /** The VBox that holds the screen elements. */
    private VBox vBox;
    /** The array of save files. */
    private GameState[] saveFiles;

    /**
     * Constructs the load game screen.
     * <p>
     * This screen allows the user to load a past save file. There are 3 save slots, each displaying information about the save.
     * @param screenManager The screen manager that manages the transitions between screens.
     */
    public LoadGameScene(ScreenManager screenManager) throws IOException {
        this.saveFiles = new GameState[3];

        // Use a StackPane as the root layout
        StackPane root = new StackPane();
        root.setPrefSize(Main.WIDTH, Main.HEIGHT);

        // Create a background image
        backgroundImage = new ImageView();
        ImageManager.loadImage(backgroundImage, "../assets/backgrounds/MainMenuBackground.png");
        ImageManager.scaleBackground(backgroundImage, Main.WIDTH, Main.HEIGHT);
        ImageManager.registerImage(backgroundImage);

        root.getChildren().add(backgroundImage);

        // Retrieve the save files
        getSaves();

        // Create a VBox to hold the buttons and other UI elements
        vBox = new VBox(20);
        vBox.setPadding(new Insets(20));
        vBox.setAlignment(Pos.CENTER);
        vBox.setMaxWidth(Main.WIDTH * 0.8);

        // Create buttons for each save file
        for (int i = 0; i < 3; i++) {
            if (saveFiles[i] == null) {
                Button button = createLoadButton(null, null);
                vBox.getChildren().add(button);
            } else {
                Pet pet = saveFiles[i].getPet();
                String details = "Pet Type: " + pet.getType() + "\nScore: " + pet.getScore() + "\nEvoLevel: " + pet.getEvoLevel();
                Button button = createLoadButton(saveFiles[i].getPet().getName(), details);

                // Loads the game when the button is clicked
                button.setOnAction(e -> {
                    ButtonManager.clearButtonElements();

                    try {
                        PlaygroundScene playgroundScene = new PlaygroundScene(screenManager, pet);
                        BedroomScene bedroomScene = new BedroomScene(screenManager, pet);
                        KitchenScene kitchenScene = new KitchenScene(screenManager, pet);
                        VetScene vetScene = new VetScene(screenManager, pet);

                        screenManager.addScreen("Bedroom", bedroomScene.getScene(), bedroomScene);
                        screenManager.addScreen("Playground", playgroundScene.getScene(), playgroundScene);
                        screenManager.addScreen("Kitchen", kitchenScene.getScene(), kitchenScene);
                        screenManager.addScreen("Vet", vetScene.getScene(), vetScene);

                        screenManager.getGameScene("Settings").setPet(pet);
                    }
                    catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    

                    screenManager.transition("Playground", false);
                });

                vBox.getChildren().add(button);
            }
        }

        // Back button
        Button backButton = new Button("Back");
        backButton.setFont(Font.font("Arial", 16));
        backButton.setStyle("-fx-background-color: lightgray; -fx-text-fill: black;");

        backButton.setOnAction(e -> {
            ButtonManager.clearButtonElements();
            screenManager.switchTo("NewAndLoad");
        });

        vBox.getChildren().add(backButton);

        // Add the VBox as the root layout
        root.getChildren().add(vBox);

        scene = new Scene(root, Main.WIDTH, Main.HEIGHT);

        // Update scaling of the VBox and its contents
        scene.widthProperty().addListener((obs, oldVal, newVal) -> updateScaling());
        scene.heightProperty().addListener((obs, oldVal, newVal) -> updateScaling());
    }

    /** Helper method to create each load button */
    private Button createLoadButton(String petName, String details) {
        Button button = new Button();
        button.setStyle("-fx-min-width: 400px; -fx-min-height: 100px; -fx-background-radius: 5px; -fx-border-radius: 5px;");

        // If not enough save files
        if (petName == null && details == null) {
            button.setText("No save file");
            button.setOpacity(0.5);
            button.setDisable(true);
            return button;
        }

        // save text
        Text saveTitle = new Text(petName);
        saveTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");

        // save description
        Text saveDesc = new Text(details);
        saveDesc.setStyle("-fx-font-size: 14px;");

        // vbox to hold the button elements
        VBox textBox = new VBox(5, saveTitle, saveDesc);
        textBox.setAlignment(Pos.CENTER_LEFT);

        VBox content = new VBox(15, textBox);
        content.setAlignment(Pos.CENTER_LEFT);
        content.setPadding(new Insets(10));

        button.setGraphic(content);
        ButtonManager.registerButton(button);

        return button;
    }

    /** Populates the saveFiles array with past game objects */
    private void getSaves() {
        String folderPath = System.getProperty("user.dir");

        try (var stream = Files.list(Paths.get(folderPath))) {
            int count = 0;
            for (Path path : stream.toArray(Path[]::new)) {
                String saveFile = path.toString();
                if (saveFile.endsWith(".ser") && !saveFile.endsWith("TimeInformation.ser") && count < 3) {
                    System.out.println(saveFile);
                    saveFiles[count] = SaveLoadManager.loadGame(saveFile);
                    count++;
                }
                if (count >= 3) break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the scene of the load game screen.
     * @return The scene of the load game screen.
     */
    public Scene getScene() {
        return scene;
    }

    /** Helper method to dynamically update the scaling of the buttons and text */
    public void updateScaling() {
        double width = scene.getWidth();
        double height = scene.getHeight();

        // Scaling for the VBox container
        ButtonManager.scaleButtonContainer(vBox, width, height, Main.WIDTH, Main.HEIGHT);
        ImageManager.scaleBackground(backgroundImage, width, height);
    }
}
