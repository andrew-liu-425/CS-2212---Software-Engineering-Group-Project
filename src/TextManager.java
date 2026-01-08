import javafx.scene.text.Text;
import javafx.scene.text.Font;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/** A utility class to scale and resize text elements. */
public class TextManager {
    /** The list of title elements to scale. */
    private static final List<Text> titleElements = new ArrayList<>();
    /** The list of paragraph elements to scale. */
    private static final List<Text> paragraphElements = new ArrayList<>();

    /** Registers a certain text that needs resizing and scaling.
     * 
     * @param text the text object that needs scaling.
     * @param type the type of the text object. Should be "title" or "paragraph"
     */
    public static void registerText(Text text, String type) {
        if (type.equals("title")) {
            titleElements.add(text);
        } else if (type.equals("paragraph")) {
            paragraphElements.add(text);
        }
    }

    /** Clear all registered text elements. */
    public static void clearTextElements() {
        titleElements.clear();
        paragraphElements.clear();
    }

    /** Method to update individual text scaling. */
    public static void scaleText(Text text, double size, double width, double height, double baseWidth, double baseHeight) {
        double scaleFactor = Math.min(width / baseWidth, height / baseHeight);
        text.setStyle("-fx-font-size: " + (size * scaleFactor) + "px; -fx-fill: black;");
    }
    
    /** Translate a specific text element (either title or paragraph) to a certain location.
     * 
     * @param text the text to translate
     * @param width the width of the text
     * @param height the height of the text
     * @param textX the x location of the text
     * @param textY the y location of the text
     */
    public static void translateText(Text text, double width, double height, double textX, double textY) {
        // Apply translation based on the given x and y offset
        text.setTranslateX(width * textX);
        text.setTranslateY(height * textY);
    }

    /** Loads a custom font from a certain file path to apply to the text.
     * 
     * @param path the file path of the font
     * @param size the font size
     * @return the font
     */
    public static Font loadCustomFont(String path, double size) {
        try (InputStream fontStream = Files.newInputStream(Paths.get(path))) {
            return Font.loadFont(fontStream, size); // Load the font with specified size
        } catch (IOException e) {
            e.printStackTrace(); // Handle I/O exceptions
            return null;
        }
    }
}