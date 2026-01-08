// Programmer Name: 84

import java.io.IOException;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/** Represents the scene that stores the inventory, allowing the player to use items.
 * <p>
 * Also has a gacha button.
 */
public class InventoryScene extends GameScene {

    /** The scene that displays the UI. */
    private Scene scene;
    /** The list of all buttons on this scene. */
    private Button[] buttons;
    /** The pet object that this scene references. */
    private Pet pet;
    /** The button that allows the player to gacha. */
    private Button gachaButton;

    /**
     * Creates a new inventory scene.
     * <p>
     * This scene displays the inventory of the pet, allowing the player to use items.
     * It includes buttons to use the items and to roll the gacha. It also has a button to display information about the items.
     * @param screenManager the screen manager that manages the scenes
     * @param pet the pet object that this scene references
     * @param previousSceneName the name of the previous scene to return to when the back button is pressed
     */
    public InventoryScene(ScreenManager screenManager, Pet pet, String previousSceneName) throws IOException {
        this.pet = pet;
        buttons = new Button[6];

        // adds containers
        StackPane root = new StackPane();
        VBox rootLayout = new VBox(20);
        rootLayout.setPadding(new Insets(30));
        rootLayout.setAlignment(Pos.TOP_CENTER);
        rootLayout.setBackground(new Background(new BackgroundFill(Color.BEIGE, CornerRadii.EMPTY, Insets.EMPTY)));

        // adds title text
        Text title = new Text("Inventory");
        title.setFont(Font.font("Arial", 48));
        title.setFill(Color.DARKSLATEGRAY);
        rootLayout.getChildren().add(title);

        // adds item containers
        GridPane itemsGrid = new GridPane();
        itemsGrid.setHgap(30);
        itemsGrid.setVgap(30);
        itemsGrid.setAlignment(Pos.CENTER);

        int col = 0;
        int row = 0;

        // builds multiple item boxes
        if (pet != null) {
            int count = 0;
            for (Item item : pet.getInventory().getItems()) {
                String itemName = item.getItemName();
                int quantity = item.getQuantity();

                // containers
                VBox itemBox = new VBox(10);
                itemBox.setAlignment(Pos.CENTER);
                itemBox.setPrefSize(160, 160);
                itemBox.setStyle("-fx-background-color: white; -fx-border-color: black; -fx-border-width: 3;");

                // images
                ImageView imageView = new ImageView();
                String imagePath = "../assets/Images/" + getImageFileName(itemName);
                ImageManager.loadImage(imageView, imagePath);
                ImageManager.scaleImage(imageView, 80, 80);

                // item text
                Text itemText = new Text(getDisplayName(itemName) + " x" + quantity);
                itemText.setFont(Font.font("Arial", 16));

                // buttons to use items
                Button useButton = new Button("Use");
                buttons[count] = useButton;

                // disables button if not enough quantity
                if (quantity <= 0) {
                    imageView.setOpacity(0.4);
                    itemText.setOpacity(0.4);
                    useButton.setDisable(true);
                    useButton.setStyle("-fx-background-color: #888; -fx-text-fill: white;");
                }

                // sets the button's action
                useButton.setOnAction(e -> {
                    pet.giveItem(itemName);
                    pet.incrementScore(pet.getInventory().getItemScoreIncrease(itemName));
                    screenManager.transition(previousSceneName, true);
                });

                // adds elements to the item box
                itemBox.getChildren().addAll(imageView, itemText, useButton);
                itemsGrid.add(itemBox, col, row);

                col++;
                if (col >= 3) {
                    col = 0;
                    row++;
                }

                count++;
                if (count >= 6) break;
            }
        }

        rootLayout.getChildren().add(itemsGrid);

        HBox buttonRow = new HBox(20);
        buttonRow.setAlignment(Pos.CENTER);

        // back button logic
        Button backButton = new Button("Back");
        ButtonManager.registerButton(backButton);
        backButton.setOnAction(e -> {
            TextManager.clearTextElements();
            ButtonManager.clearButtonElements();
            screenManager.switchTo(previousSceneName);
        });

        // gacha button logic
        gachaButton = new Button("Gacha (2 coins)");
        ButtonManager.registerButton(gachaButton);
        gachaButton.setOnAction(e -> {
            pet.incrementCoins(-2);
            Item wonItem = pet.getGacha().rollGacha();
            System.out.println("🎁 You received: " + wonItem.getItemName());
            screenManager.transition(previousSceneName, false);
        });

        buttonRow.getChildren().addAll(backButton, gachaButton);
        rootLayout.getChildren().add(buttonRow);

        // Circular ❓ info button
        Button infoButton = new Button("?");
        infoButton.setStyle("-fx-background-radius: 30; -fx-font-size: 20px; -fx-text-fill: red;");
        infoButton.setOnAction(e -> {
            try {
                InfoScene infoScene = new InfoScene(screenManager);
                screenManager.addScreen("InventoryInfo", infoScene.getScene(), infoScene);
            }
            catch (IOException ex) {
                System.out.println("⚠️ Error loading info scene: " + ex.getMessage());
            }
            screenManager.switchTo("InventoryInfo");
        });
        StackPane.setAlignment(infoButton, Pos.TOP_RIGHT);
        infoButton.setTranslateX(-20);
        infoButton.setTranslateY(20);

        root.getChildren().addAll(rootLayout, infoButton);
        this.scene = new Scene(root, Main.WIDTH, Main.HEIGHT);
    }

    /**
     *  Returns the scene that displays the inventory.
     *  @return the scene that displays the inventory
     */
    public Scene getScene() {
        return scene;
    }

    /** Helper method that gets the images file of the objects.
     * 
     * @param itemName the name of the item
     * @return the file name of the image
     */
    private String getImageFileName(String itemName) {
        switch (itemName.toLowerCase()) {
            case "burger": return "burger.png";
            case "pizza": return "pizza.png";
            case "salad": return "salad.png";
            case "ball":
            case "soccer ball": return "ball.png";
            case "toy":
            case "toy car": return "toy.png";
            case "rubix":
            case "rubix cube": return "rubix.png";
            case "coin": return "coin.png";
            default:
                System.out.println("⚠️ No image match for: " + itemName);
                return "default.png";
        }
    }

    /** Helper method to helo display the item name on the UI.
     * 
     * @param itemName the name of the item
     * @return the display name of the item
     */
    private String getDisplayName(String itemName) {
        switch (itemName.toLowerCase()) {
            case "burger": return "Burger";
            case "pizza": return "Pizza";
            case "salad": return "Salad";
            case "ball":
            case "soccer ball": return "Soccer Ball ✯";
            case "toy":
            case "toy car": return "Toy Car ✯✯";
            case "rubix":
            case "rubix cube": return "Toy Cube ✯✯✯";
            case "coin": return "Coin";
            default:
                return itemName.substring(0, 1).toUpperCase() + itemName.substring(1).replace("_", " ");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onEnter() {
        int count = 0;
        if (pet.getCoins() < 2) gachaButton.setDisable(true);
        else gachaButton.setDisable(false);
        for (Item item : pet.getInventory().getItems()) {
            if (pet.isStillAngry() && count <= 2) {
                buttons[count].setDisable(true);
            }
            else if (item.getQuantity() == 0) {
                buttons[count].setDisable(true);
            } else {
                buttons[count].setDisable(false);
            }
            count++;
        }
    }
}
