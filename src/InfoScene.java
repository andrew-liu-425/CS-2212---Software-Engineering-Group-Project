import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.control.Button;

import java.io.IOException;

import javafx.geometry.Pos;
import javafx.scene.image.ImageView;

/** Represents a scene that stores a list of information for the player to read. */
public class InfoScene extends GameScene {

    /** The scene that displays the information. */
    private Scene scene;

    /** Creates a new InfoScene scene. 
     * <p>
     * This scene contains inventory information about the items in the game, including their names, images, and descriptions.
     * Used purely for informational purposes.
     * @param screenManager The ScreenManager that manages the switching of scenes.
    */
    public InfoScene(ScreenManager screenManager) throws IOException {
        // sets layouts
        VBox layout = new VBox(30);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: beige;");
        layout.setPadding(new javafx.geometry.Insets(50, 0, 30, 0));

        // adds some images and text to the layout
        layout.getChildren().add(createItemInfo("Burger", "burger.png", "Restores low fullness (+20): Costs 2 coins"));
        layout.getChildren().add(createItemInfo("Pizza", "pizza.png", "Restores medium fullness (+50): Costs 5 coins"));
        layout.getChildren().add(createItemInfo("Salad", "salad.png", "Restores max fullness (+100): Costs 7 coins"));
        layout.getChildren().add(createItemInfo("Soccer Ball", "ball.png", "Increases happiness (+10): 60% drop chance"));
        layout.getChildren().add(createItemInfo("Toy Car", "toy.png", "Increases happiness more (+40): 30% drop chance"));
        layout.getChildren().add(createItemInfo("Rubik's Cube", "rubix.png", "Increases happiness a ton (+70): 10% drop chance"));

        // creates a back button
        Button backButton = new Button("Back");
        ButtonManager.registerButton(backButton);
        backButton.setOnAction(e -> screenManager.switchTo("Inventory"));

        // adds stuff to the scene
        layout.getChildren().add(backButton);
        scene = new Scene(layout, Main.WIDTH, Main.HEIGHT);
    }

    /** Helper method that creates images and their descriptions on the scene. 
     * 
     * @param name The name of the item.
     * @param imgFile The name of the image file for the item.
     * @param desc The description of the item.
    */
    private HBox createItemInfo(String name, String imgFile, String desc) throws IOException {
        HBox box = new HBox(20);
        box.setAlignment(Pos.CENTER_LEFT);
        box.setMaxWidth(600);

        ImageView img = new ImageView();
        ImageManager.loadImage(img, "../assets/Images/" + imgFile);
        ImageManager.scaleImage(img, 60, 60);

        VBox textBox = new VBox(4);
        textBox.setAlignment(Pos.CENTER_LEFT);

        Text itemName = new Text(name);
        itemName.setFont(Font.font("Arial", 20));

        Text itemDesc = new Text(desc);
        itemDesc.setFont(Font.font("Arial", 16));

        textBox.getChildren().addAll(itemName, itemDesc);
        box.getChildren().addAll(img, textBox);
        return box;
    }

    /** Returns the scene that displays the information.
     * @return The scene that displays the information.
    */
    public Scene getScene() {
        return scene;
    }
}
