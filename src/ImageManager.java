import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/** Utility class used to scale and resize images. */
public class ImageManager {

    /** The list of images to be scaled and resized. */
    private static List<ImageView> registeredImages = new ArrayList<>();

    /**
     * Load an image and set it to an ImageView
     * @param imageView the ImageView to set the image to
     * @param path the path to the image file
     * @throws IOException if the image file cannot be read
     */
    public static void loadImage(ImageView imageView, String path) throws IOException {
        try (InputStream is = Files.newInputStream(Paths.get(path))) {
            Image img = new Image(is);
            imageView.setImage(img);
        } catch (IOException e) {
            e.printStackTrace(); // Handle I/O exceptions
        }
    }

    /**
     * Scale the background image to cover the screen completely
     * @param imageView the ImageView object to scale
     * @param width the width of the screen
     * @param height the height of the screen
     */
    public static void scaleBackground(ImageView imageView, double width, double height) {
        if (imageView.getImage() != null) {
            // Set the width and height to match the screen size
            imageView.setFitWidth(width);
            imageView.setFitHeight(height);
            imageView.setPreserveRatio(false);
        }
    }

    /**
     * Scale a normal image with dynamic size while preserving aspect ratio
     * @param imageView the ImageView object to scale
     * @param maxWidth the maximum width of the image
     * @param maxHeight the maximum height of the image
     */
    public static void scaleImage(ImageView imageView, double maxWidth, double maxHeight) {
        if (imageView.getImage() != null) {
            double originalWidth = imageView.getImage().getWidth();
            double originalHeight = imageView.getImage().getHeight();
            double aspectRatio = originalWidth / originalHeight;

            double newWidth = maxWidth;
            double newHeight = maxWidth / aspectRatio;

            if (newHeight > maxHeight) {
                newHeight = maxHeight;
                newWidth = maxHeight * aspectRatio;
            }

            imageView.setFitWidth(newWidth);
            imageView.setFitHeight(newHeight);
            imageView.setPreserveRatio(true);
        }
    }


    /**
     * Register an ImageView to be scaled and resized. Stores it in the list of images.
     * @param imageView the ImageView to register
     */
    public static void registerImage(ImageView imageView) {
        if (!registeredImages.contains(imageView)) {
            registeredImages.add(imageView);
        }
    }

    /**
     * Update scaling for all registered images
     * @param width of imageView
     * @param height of imageView
     */
    public static void updateScaling(double width, double height) {
        for (ImageView imageView : registeredImages) {
            scaleImage(imageView, width, height);
        }
    }

    /**
     * Translate the image to a specific position
     * @param imageView the ImageView to translate
     * @param width the x-coordinate to translate to
     * @param height the y-coordinate to translate to
     */
    public static void translateImage(ImageView imageView, double width, double height) {
        if (imageView != null) {
            imageView.setTranslateX(width);
            imageView.setTranslateY(height);
        }
    }
}
