import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.scene.Node;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import java.util.ArrayList;
import java.util.List;

/** 
 * A helper class that scales and resizes buttons.
 *
 */
public class ButtonManager {
    /** The list of all buttons that need scaling and resizing. */
    private static final List<Button> buttonElements = new ArrayList<>();
    /** The list of all button containers that stores the buttons. */
    private static final List<VBox> buttonContainers = new ArrayList<>();
    
    // Default button size values
    private static final double DEFAULT_BUTTON_WIDTH = 400;
    private static final double DEFAULT_BUTTON_HEIGHT = 40;
    private static final double DEFAULT_FONT_SIZE = 30;

    /** Inserts a button into the list for scaling.
     * @param button The button to be registered.
     */
    public static void registerButton(Button button) {
        // Register button with default size
        registerButton(button, DEFAULT_BUTTON_WIDTH, DEFAULT_BUTTON_HEIGHT, DEFAULT_FONT_SIZE);
    }

    /** Inserts a button into the list for scaling.
     * @param button The button to be registered.
     * @param width The intended width of the button.
     * @param height The intended height of the button.
     * @param fontSize The intended font size of the button.
     */
    public static void registerButton(Button button, double width, double height, double fontSize) {
        buttonElements.add(button);
        setButtonSize(button, width, height, fontSize);
    }

    /** Inserts a button container into the list for scaling.
     * @param container The VBox container to be registered.
     */
    public static void registerButtonContainer(VBox container) {
        buttonContainers.add(container);
    }

    /** Returns the list of buttons that need scaling.
     * @return The list of buttons.
     */
    public static List<Button> getButtonElements() {
        return buttonElements;
    }

    /** Returns the list of button containers storing the buttons.
     * @return The list of button containers.
     */
    public static List<VBox> getButtonContainers() {
        return buttonContainers;
    }

    /** Removes all the button and container elements in this class. */
    public static void clearButtonElements() {
        buttonElements.clear();
        buttonContainers.clear();
    }

    /** Sets the custom button sprite.
     * @param button The button to set the sprite for.
     * @param imagePath The path to the image file.
     */
    public static void setCustomButtonSprite(Button button, String imagePath) {
        Image buttonImage = new Image("file:" + imagePath);
        ImageView imageView = new ImageView(buttonImage);
        imageView.setFitWidth(DEFAULT_BUTTON_WIDTH);
        imageView.setFitHeight(DEFAULT_BUTTON_HEIGHT);
        button.setGraphic(imageView);
    }

    /** Scales a button container.
     * @param container The VBox container to be scaled.
     * @param width The intended width of the container.
     * @param height The intended height of the container.
     * @param baseWidth The base width for scaling.
     * @param baseHeight The base height for scaling.
     */
    public static void scaleButtonContainer(VBox container, double width, double height, double baseWidth, double baseHeight) {
        double scaleFactor = Math.min(width / baseWidth, height / baseHeight);
        container.setPrefWidth(width);
        container.setPrefHeight(height);
        container.setAlignment(Pos.CENTER);

        // Loop through the buttons inside the VBox and set them to auto-scale with the container
        for (Node node : container.getChildren()) {
            if (node instanceof Button) {
                Button button = (Button) node;
                // Scale button width based on the container size
                double scaledWidth = DEFAULT_BUTTON_WIDTH * scaleFactor;  // Default width scaled based on ratio
                button.setPrefWidth(scaledWidth); // Apply the calculated width
                button.setMaxWidth(scaledWidth); // Ensure button expands to the calculated width

                // Maintain the button's height and font size based on scaling factor
                double scaledHeight = DEFAULT_BUTTON_HEIGHT * scaleFactor;
                button.setPrefHeight(scaledHeight);
                button.setStyle("-fx-font-size: " + (DEFAULT_FONT_SIZE * scaleFactor) + "px;");
            }
        }
    }

    /** Translates the container based on a certain percentage.
     * @param container The VBox container to be translated.
     * @param xPosPercentage The percentage of the width to translate the container.
     * @param yPosPercentage The percentage of the height to translate the container.
     */
    public static void translateContainer(VBox container, double xPosPercentage, double yPosPercentage) {
        Scene scene = container.getScene();
        if (scene != null) {
            double xTranslation = scene.getWidth() * xPosPercentage;
            double yTranslation = scene.getHeight() * yPosPercentage;
            container.setTranslateX(xTranslation);
            container.setTranslateY(yTranslation);
        }
    }

    /** 
     * Sets the size of a button.
     * @param button The button to set the size for.
     * @param width The intended width of the button.
     * @param height The intended height of the button.
     * @param fontSize The intended font size of the button.
     */
    private static void setButtonSize(Button button, double width, double height, double fontSize) {
        button.setPrefWidth(width);
        button.setPrefHeight(height);
        button.setStyle("-fx-font-size: " + fontSize + "px;");
    }

    /** 
     * Translates a button based on a certain percentage.
     * @param button The button to be translated.
     * @param xPosPercentage The percentage of the width to translate the button.
     * @param yPosPercentage The percentage of the height to translate the button.
     */
    public static void translateButton(Button button, double xPosPercentage, double yPosPercentage) {
        // Translate button based on percentage of width/height
        double xTranslation = button.getScene().getWidth() * xPosPercentage;
        double yTranslation = button.getScene().getHeight() * yPosPercentage;
        button.setTranslateX(xTranslation);
        button.setTranslateY(yTranslation);
    }

    /**
     * Binds a specific key to trigger the button's action.
     * 
     * @param button The button to bind the key to.
     * @param key The keyboard key to bind.
     */
    public static void bind(Button button, KeyCode key) {
        
    }
}
