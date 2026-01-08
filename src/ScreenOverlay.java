import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.io.IOException;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.text.Text;
import javafx.scene.text.Font;

/** Represents the UI elements of the gameplay screens besides the pet. */
public class ScreenOverlay {

    /** Holds UI elements. */
    private StackPane root;
    /** Container for buttons. */
    private VBox buttonContainer;
    /** The list of sprite bars. */
    private SpriteManager[] statBars;
    /** The list of stat icons. */
    private ImageView[] statIcons;
    /** The text that displays the score and number of coins. */
    private Text scoreText, coinsText;
    /** The button that navigates left. */
    private Button leftArrowButton;
    /** The button that navigates right. */
    private Button rightArrowButton;
    /** The button that leads to the inventory. */
    private Button inventoryButton;
    /** The button that leads to the settings. */
    private Button settingsButton;

    /** The sprite sheet for the stat bars. */
    private final String spriteSheetPath = "../assets/sprites/statsBarSprite.png"; 

    /** The sprites for the icons. */
    private final String[] iconPaths = {
        "../assets/sprites/health.png",
        "../assets/sprites/hunger.png",
        "../assets/sprites/happy.png",
        "../assets/sprites/sleep.png"
    };

    /**
     * Constructs a screen overlay.
     * <p>
     * This class holds all UI elements of the gameplay screens besides the pet.
     * This includes buttons and stat bars.
     * @param screenManager the screen manager to handle transitions.
     * @param pet the pet object.
     */
    public ScreenOverlay(ScreenManager screenManager, Pet pet) throws IOException {
        root = new StackPane();

        // container for buttons 
        buttonContainer = new VBox();
        buttonContainer.setAlignment(Pos.TOP_RIGHT);
        root.getChildren().add(buttonContainer);

        // setting button
        settingsButton = new Button("⚙︎");
        settingsButton.setOnAction(e -> {
            TextManager.clearTextElements();
            ButtonManager.clearButtonElements();
            screenManager.switchTo("Settings");
        });
        buttonContainer.getChildren().add(settingsButton);

        // left arrow button
        leftArrowButton = new Button("←");
        leftArrowButton.setStyle("-fx-font-size: 30px;");
        leftArrowButton.setOnAction(e -> {
            TextManager.clearTextElements();
            ButtonManager.clearButtonElements();
            screenManager.switchToPrevious();
        });
        
        // right arrow button
        rightArrowButton = new Button("→");
        rightArrowButton.setStyle("-fx-font-size: 30px;");
        rightArrowButton.setOnAction(e -> {
            TextManager.clearTextElements();
            ButtonManager.clearButtonElements();
            screenManager.switchToNext();
        });
        
        root.getChildren().add(leftArrowButton);
        root.getChildren().add(rightArrowButton);

        // Create a button for the inventory
        inventoryButton = new Button("Inventory");
        inventoryButton.setStyle("-fx-font-size: 20px;");

        // Set button's action
        inventoryButton.setOnAction(e -> {
            TextManager.clearTextElements();
            ButtonManager.clearButtonElements(); 
            
            try {
                String currentSceneName = screenManager.getCurrentSceneName();
                InventoryScene inventoryScene = new InventoryScene(screenManager, pet, currentSceneName);
                screenManager.addScreen("Inventory", inventoryScene.getScene(), inventoryScene);
            }
            catch (IOException ex) {
                ex.printStackTrace();
            }
            
            screenManager.switchTo("Inventory");

        });

        // Add to root
        root.getChildren().add(inventoryButton);

        // Position the button at the bottom left of the screen
        StackPane.setAlignment(inventoryButton, Pos.BOTTOM_LEFT);
        inventoryButton.setTranslateX(20);  // Adjust X position slightly
        inventoryButton.setTranslateY(-20); // Adjust Y position slightly

        statBars = new SpriteManager[4];
        statIcons = new ImageView[iconPaths.length];

        // adds stat bars
        for (int i = 0; i < statBars.length; i++) {
            statBars[i] = new SpriteManager(spriteSheetPath, 10000, 135, 18, 1, pet);
            statBars[i].setCurrentFrame(4);
            root.getChildren().add(statBars[i].getSpriteView());
            statBars[i].startAnimation();
            statBars[i].scaleSprite(300, 60);
        }

        // adds stat icons
        for (int i = 0; i < iconPaths.length; i++) {
            statIcons[i] = new ImageView();
            ImageManager.loadImage(statIcons[i], iconPaths[i]);
            ImageManager.scaleImage(statIcons[i], 50, 50);
            root.getChildren().add(statIcons[i]);
        }

        // score text
        scoreText = new Text("SCORE: 0");
        Font customFont = TextManager.loadCustomFont("../assets/fonts/impact/impact.ttf", 80);
        scoreText.setFont(customFont != null ? customFont : Font.font(175));
        scoreText.setStyle("-fx-fill: black;");
        TextManager.registerText(scoreText, "title");

        // coins text
        coinsText = new Text("COINS: 0");
        coinsText.setFont(customFont != null ? customFont : Font.font(175));
        coinsText.setStyle("-fx-fill: black;");
        TextManager.registerText(coinsText, "title");

        root.getChildren().addAll(scoreText, coinsText);
    }

    /**
     * Adds the UI elements to a gameplay screen.
     * @param parentRoot the root of the gameplay screen.
     */
    public void addToRoot(StackPane parentRoot) {
        parentRoot.getChildren().add(root);
    }

    /**
     * Returns the root of this screen overlay
     * @return the root
     */
    public StackPane getRoot() {
        return root;
    }

    /**
     * Adds a button to the gameplay screen.
     * @param button the button to be added
     */
    public void addButton(Node button) {
        buttonContainer.getChildren().add(button);
    }

    /** Used for updating the scaling of the UI elements.
     * 
     * @param width the width of the screen
     * @param height the height of the screen
     */
    public void updateScaling(double width, double height) {
        double buttonSize = Math.min(width, height) * 0.1;
        Button settingsButton = (Button) buttonContainer.getChildren().get(0);
    
        settingsButton.setMaxWidth(buttonSize);
        settingsButton.setMaxHeight(buttonSize);
    
        settingsButton.setStyle("-fx-font-size: " + (buttonSize * 0.4) + "px;");
    
        settingsButton.setTranslateX(width * -0.015);
        settingsButton.setTranslateY(height * 0.025);
    
        double barMaxWidth = width * 2.5; 
        double barMaxHeight = height * 0.7; 
        double iconMaxSize = height * 0.065; 
    
        double xOffset = -0.37;
        double yOffset = -0.425;
        double xSpacing = 0.195;
        double ySpacing = 0.07;
        double iconOffsetX = -0.097;
        double iconOffsetY = 0.0;
    
        for (int i = 0; i < statBars.length; i++) {
            int row = i / 2;
            int col = i % 2;
    
            double xPos = width * (xOffset + col * xSpacing);
            double yPos = height * (yOffset + row * ySpacing);
    
            ImageManager.translateImage(statBars[i].getSpriteView(), xPos, yPos);
            statBars[i].scaleSprite(barMaxWidth, barMaxHeight);
    
            ImageManager.translateImage(statIcons[i], xPos + (width * iconOffsetX), yPos + (height * iconOffsetY));
            ImageManager.scaleImage(statIcons[i], iconMaxSize, iconMaxSize);
        }
    
        Button leftArrowButton = (Button) root.getChildren().get(1);
        Button rightArrowButton = (Button) root.getChildren().get(2);
    
        StackPane.setAlignment(leftArrowButton, Pos.CENTER_LEFT);
        StackPane.setAlignment(rightArrowButton, Pos.CENTER_RIGHT);
    
        leftArrowButton.setTranslateX(width * 0.025);
        rightArrowButton.setTranslateX(width * -0.025);
    
        double baseWidth = 1920;
        double baseHeight = 1080;
    
        // Scale and position scoreText
        // Scale and position scoreText
        TextManager.scaleText(scoreText, 80, width, height, baseWidth, baseHeight);
        TextManager.translateText(scoreText, width, height, 0.1, -0.435);

        TextManager.scaleText(coinsText, 80, width, height, baseWidth, baseHeight);
        TextManager.translateText(coinsText, width, height, 0.325, -0.435);
    } 

    /**
     * Returns the stat bars of this screen overlay.
     * @return the stat bars
     */
    public SpriteManager[] getStatBars() {
        return statBars;
    }

    /**
     * Returns the left arrow button of this screen overlay.
     * @return the left arrow button
     */
    public Button getLeftArrowButton() {
        return leftArrowButton;
    }

    /**
     * Returns the right arrow button of this screen overlay.
     * @return the right arrow button
     */
    public Button getRightArrowButton() {
        return rightArrowButton;
    }

    /**
     * Returns the inventory button of this screen overlay.
     * @return the inventory button
     */
    public Button getInventoryButton() {
        return inventoryButton;
    }

    /**
     * Returns the settings button of this screen overlay.
     * @return the settings button
     */
    public Button getSettingsButton() {
        return settingsButton;
    }

    /**
     * Sets the coin text of this screen overlay.
     * @param text the coins text
     */
    public void setCoinsText(String text) {
        coinsText.setText(text);
    }

    /**
     * Sets the score text of this screen overlay.
     * @param text the score text
     */
    public void setScoreText(String text) {
        scoreText.setText(text);
    }
}
