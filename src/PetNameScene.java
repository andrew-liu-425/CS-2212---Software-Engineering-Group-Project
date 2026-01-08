import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.Font;

import java.io.IOException;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

/** Represents the screen where the player can name their new pet. It is the final screen before the actual game starts. */
public class PetNameScene extends GameScene {
    
    /** The type of pet the player has selected. */
    private String petType;
    /** The scene for UI elements. */
    private Scene scene;
    /** The ScreenManager to manage scene transitions. */
    private ScreenManager screenManager;
    /** The background image for the scene. */
    private ImageView backgroundImage;
    /** The container for the buttons. */
    private VBox buttonContainer;

    /** The default text in the textfield. */
    private Text instructions;
    /** The textfield for the player to input their pet's name. */
    private TextField inputField;
    /** The button to go back to the previous screen and the button to start the game. */
    private Button backButton, playButton;

    /**
     * Creates a new PetNameScene.
     * <p>
     * This scene allows the player to enter their pet's name before starting the game.
     * @param screenManager The ScreenManager to manage scene transitions.
     */
    public PetNameScene(ScreenManager screenManager) throws IOException {
        this.screenManager = screenManager;
        petType = "Dog"; // default; this field doesn't really matter

        // UI container
        StackPane root = new StackPane();
        root.setPrefSize(Main.WIDTH, Main.HEIGHT);

        // Background image
        backgroundImage = new ImageView();
        ImageManager.loadImage(backgroundImage, "../assets/backgrounds/MainMenuBackground.png");
        ImageManager.scaleBackground(backgroundImage, Main.WIDTH, Main.HEIGHT);
        ImageManager.registerImage(backgroundImage);
        root.getChildren().add(backgroundImage);

        // Default text
        instructions = new Text("Please Enter Your Pet's Name: ");
        Font customFont = TextManager.loadCustomFont("../assets/fonts/impact/impact.ttf", 36);
        instructions.setFont(customFont != null ? customFont : Font.font(70));
        instructions.setStyle("-fx-fill: black;");
        TextManager.registerText(instructions, "paragraph");

        // Text field for input
        inputField = new TextField();
        inputField.setPromptText("Your Pet's Name Here");
        inputField.setMaxWidth(500);
        inputField.setPrefHeight(50);
        inputField.setStyle("-fx-font-size: 20px;");

        // Buttons
        backButton = new Button("Back");
        playButton = new Button("Play");

        // back button
        backButton.setOnAction(e -> {
            ButtonManager.clearButtonElements();
            TextManager.clearTextElements();
            screenManager.switchTo("PetSelection");
        });

        // play button
        playButton.setOnAction(e -> {
            try {
                ButtonManager.clearButtonElements();
                TextManager.clearTextElements();
                createNewPet(inputField.getText());
            }
            catch (IOException ex) {
                ex.printStackTrace();
            }
        
            screenManager.switchTo("Playground");
        });

        // Scales buttons
        ButtonManager.registerButton(backButton, 200, 30, 20);
        ButtonManager.registerButton(playButton, 200, 30, 20);

        // Default not clickable (until name is inputted)
        playButton.setDisable(true);
        playButton.setOpacity(0.5);

        // Text field listener to enable play button when text is entered
        inputField.textProperty().addListener((observable, oldValue, newValue) -> {
            boolean isEmpty = newValue.trim().isEmpty();
            playButton.setDisable(isEmpty);
            playButton.setOpacity(isEmpty ? 0.5 : 1.0);
        });

        buttonContainer = new VBox(15, backButton, playButton);
        buttonContainer.setAlignment(Pos.CENTER);

        root.getChildren().addAll(instructions, inputField, buttonContainer);

        scene = new Scene(root, Main.WIDTH, Main.HEIGHT);

        scene.widthProperty().addListener((obs, oldVal, newVal) -> updateScaling());
        scene.heightProperty().addListener((obs, oldVal, newVal) -> updateScaling());
    }

    /**
     * Sets the pet type for the scene. This is used when the player selects a pet type in the previous screen.
     * @param petType The type of pet selected by the player.
     */
    @Override
    public void setPetType_ForPetNameSceneOnly(String petType) {
        this.petType = petType;

        inputField.clear(); 
        playButton.setDisable(true); 
        playButton.setOpacity(0.5); 
    
        // Set focus to input field
        inputField.requestFocus();
    }

    /**
     * Returns the scene for this PetNameScene.
     * @return The scene for this PetNameScene.
     */
    public Scene getScene() {
        return scene;
    }

    /**
     * Updates the scaling of UI elements based on the current scene dimensions.
     * This method is called whenever the scene is resized.
     */
    private void updateScaling() {
        double width = scene.getWidth();
        double height = scene.getHeight();
        ImageManager.scaleBackground(backgroundImage, width, height);
        TextManager.scaleText(instructions, 60, width, height, Main.WIDTH, Main.HEIGHT); 
        TextManager.translateText(instructions, width, height, 0.0, -0.15);

        ButtonManager.scaleButtonContainer(buttonContainer, width, height, Main.WIDTH, Main.HEIGHT);
        ButtonManager.translateContainer(buttonContainer, 0, 0.15);
        
    }

    private void createNewPet(String inputField) throws IOException {
        Item burger = new Item("Burger", "Food", 20);
        Item pizza = new Item("Pizza", "Food", 50);
        Item salad = new Item("Salad", "Food", 100);
        Item soccer = new Item("Soccer Ball", "Gift", 10);
        Item toycar = new Item("Toy Car", "Gift", 40);
        Item rubix = new Item("Rubix Cube", "Gift", 70);

        Inventory inventory = new Inventory();
        inventory.addNewItem(burger);
        inventory.addNewItem(pizza);
        inventory.addNewItem(salad);
        inventory.addNewItem(soccer);
        inventory.addNewItem(toycar);
        inventory.addNewItem(rubix);
        Gacha gacha = new Gacha(inventory);
        gacha.addNewItem(soccer, 60);
        gacha.addNewItem(toycar, 30);
        gacha.addNewItem(rubix, 10);
        
        Pet pet = new Pet(inputField, petType, inventory, gacha);

        PlaygroundScene playgroundScene = new PlaygroundScene(screenManager, pet);
        BedroomScene bedroomScene = new BedroomScene(screenManager, pet);
        KitchenScene kitchenScene = new KitchenScene(screenManager, pet);
        VetScene vetScene = new VetScene(screenManager, pet);

        screenManager.addScreen("Bedroom", bedroomScene.getScene(), bedroomScene);
        screenManager.addScreen("Playground", playgroundScene.getScene(), playgroundScene);
        screenManager.addScreen("Vet", vetScene.getScene(), vetScene);
        screenManager.addScreen("Kitchen", kitchenScene.getScene(), kitchenScene);

        screenManager.getGameScene("Settings").setPet(pet);
        
    }
}

