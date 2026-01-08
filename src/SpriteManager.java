import javafx.animation.Animation;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.util.Duration;
import java.util.ArrayList;
import java.util.List;

/** Represents the class that builds sprites from spritesheet paths.
 * <p>
 * Also responsible for handling animations, and changing sprites when appropriate.
 */
public class SpriteManager {
    /** The current sprite displayed. */
    private ImageView spriteView;
    /** The list of all sprites to cycle through. */
    private List<Image> frames;
    /** The index of the current frame. */
    private int currentFrame = 0;
    /** The timeline object to handle animations. */
    private Timeline animation;
    /** The width of the sprite sheet. */
    private double spriteSheetWidth;
    /** The height of the sprite sheet. */
    private double spriteSheetHeight;
    /** The number of columns in the sprite sheet. */
    private int columns;
    /** The number of rows in the sprite sheet. */
    private int rows;

    public static final int NORMAL = 0;
    public static final int ANGRY = 4;
    public static final int HAPPY = 8;
    public static final int SLEEPY = 12;
    public static final int SLEEPING = 16;
    public static final int HUNGRY = 20;
    public static final int DEAD = 24;
    public static final int SICK = 28;

    /** The value of the current stat percentage. */
    private double statPercent; 

    /** The pet object. */
    private Pet pet;

    /**
     * Constructs a new sprite manager.
     * <p>
     * Responsible for building sprites and their animations. There should be one of these per stat bar and pet.
     * @param spriteSheetPath the sprite sheet path
     * @param spriteSheetWidth the sprite sheet width
     * @param spriteSheetHeight the sprite sheet height
     * @param columns the number of columns in the sprite sheet
     * @param rows the number of rows in the sprite sheet
     * @param pet the pet object
     */
    public SpriteManager(String spriteSheetPath, double spriteSheetWidth, double spriteSheetHeight, int columns, int rows, Pet pet) {
        this.spriteSheetWidth = spriteSheetWidth;
        this.spriteSheetHeight = spriteSheetHeight;
        this.columns = columns;
        this.rows = rows;

        this.statPercent = 75;

        this.pet = pet;

        frames = new ArrayList<>();
        
        spriteView = new ImageView(new Image("file:" + spriteSheetPath));
        extractFrames(spriteSheetPath);
        setupAnimation();
    }

    
    /**
     * Extracts frames from a given sprite sheet and places them into the frames list.
     * @param spriteSheetPath the sprite sheet path
     */
    public void extractFrames(String spriteSheetPath) {
        
        Image spriteSheet = new Image("file:" + spriteSheetPath);
        PixelReader pixelReader = spriteSheet.getPixelReader();

        double frameWidth = spriteSheetWidth / columns;
        double frameHeight = spriteSheetHeight / rows;

        try {
            for (int row = 0; row < rows; row++) {
                for (int col = 0; col < columns; col++) {
                    Image frame = new WritableImage(pixelReader,
                            (int) (col * frameWidth), (int) (row * frameHeight),
                            (int) frameWidth, (int) frameHeight);
                    frames.add(frame);
                }
            }
        }
        catch (NullPointerException e) {
            throw new NullPointerException("Sprite file could not be found!");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
    }

    /**
     * Extracts a single frame image and returns it.
     * @param spriteSheet the sprite sheet containing the image
     * @param row the row of the frame
     * @param column the column of the fram
     * @param totalColumns the total number of columns in the sprite sheet
     * @param totalRows the total number of rows in the sprite sheet
     * @return the image containing the sprite
     */
    public static Image extractFrame(Image spriteSheet, int row, int column, int totalColumns, int totalRows) {
        // Determine the width and height of each frame
        double frameWidth = spriteSheet.getWidth() / totalColumns;
        double frameHeight = spriteSheet.getHeight() / totalRows;

        double x = column * frameWidth;
        double y = row * frameHeight;

        // Return a WritableImage containing the frame
        WritableImage frameImage = new WritableImage(spriteSheet.getPixelReader(), (int) x, (int) y, (int) frameWidth, (int) frameHeight);
        return frameImage;
    }
    
    /** Helper method to set up the animation of the sprites. */
    private void setupAnimation() {
        animation = new Timeline(new KeyFrame(Duration.seconds(0.25), e -> {
        nextFrame();
        updateStatBars();
        }));
        animation.setCycleCount(Animation.INDEFINITE);
    }

    /** For pets only. It advances to the next frame of the same emotion.
     */
    private void nextFrame() {
        if (rows != 1 && !frames.isEmpty()) {
            // Loop through only the first 4 frames
                spriteView.setImage(frames.get(currentFrame + (pet.getEvoLevel()-1)*32));
                if (currentFrame % 4 == 3) {
                    currentFrame = currentFrame - 3;
                }
                else {
                    currentFrame = currentFrame + 1;
                }
            
        }
    }

    /** For stat bars, check to see if the current stat bar needs changing, and does so accordingly. */
    private void updateStatBars() {
        if (rows == 1 && !frames.isEmpty()) {
                if (statPercent < 1) {
                    currentFrame = 16;
                    spriteView.setImage(frames.get(currentFrame));
                }
                else {
                    currentFrame = (int) (16 - Math.ceil(statPercent/6.25));
                    spriteView.setImage(frames.get(currentFrame));
                }
            
        }
    }

    /** Manually sets the current frame if necessary. */
    public void setCurrentFrame(int frameIndex) {
        if (frameIndex >= 0 && frameIndex < frames.size()) {
            currentFrame = frameIndex;
            spriteView.setImage(frames.get(currentFrame));
        }
    }

    /** Start the animation of the sprites. */
    public void startAnimation() {
        animation.play();
    }

    /** Stopos the animation of the sprites. */
    public void stopAnimation() {
        animation.stop();
    }

    /**
     * Dynamically translates sprite based on screen size
     * @param xPercent the x percent to translate the sprite
     * @param yPercent the y percent to translate the sprite
     * @param screenWidth the screen width
     * @param screenHeight the screen height
     */
    public void translateSprite(double xPercent, double yPercent, double screenWidth, double screenHeight) {
        // Apply scaling factor to the position based on the sprite size
        double scaledWidth = spriteView.getFitWidth();
        double scaledHeight = spriteView.getFitHeight();
    
        // Translate based on the screen size and sprite scale
        double translatedX = screenWidth * xPercent - scaledWidth / 2; 
        double translatedY = screenHeight * yPercent - scaledHeight / 2; 
    
        spriteView.setTranslateX(translatedX);
        spriteView.setTranslateY(translatedY);
    }

    /** 
     * Scales sprite based on passed width and height, preserving aspect ratio
     * @param targetWidth the intended width of the sprite
     * @param targetHeight the intended height of the sprite
     */
    public void scaleSprite(double targetWidth, double targetHeight) {
        double aspectRatio = spriteSheetWidth / spriteSheetHeight;
        
        // Calculate scaled width and height based on the target width
        double scaledWidth = targetWidth;
        double scaledHeight = scaledWidth / aspectRatio;
    
        // If the scaled height exceeds the target height, adjust the height and recompute the width
        if (scaledHeight > targetHeight) {
            scaledHeight = targetHeight;
            scaledWidth = scaledHeight * aspectRatio;
        }
    
        // Ensure the sprite maintains the calculated width and height
        spriteView.setFitWidth(scaledWidth);
        spriteView.setFitHeight(scaledHeight);
        
        // Set the scaling mode to preserve aspect ratio
        spriteView.setPreserveRatio(true);
    }

    /**
     * Returns the current sprite image
     * @return the current sprite image.
     */
    public ImageView getSpriteView() {
        return spriteView;
    }

    /**
     * Sets the pet's current emotion. Used to change the sprite of the pet.
     * @param emotion the current emotion of the pet, represented as a final int from this class.
     */
    public void setEmotion(int emotion) {
        currentFrame = emotion;     
    }

    /**
     * Sets the stat for stat bars. Used to update the current stat bars.
     * @param stat the new stat
     * @param maxStat the pet's max stat 
     */
    public void setStats(double stat, double maxStat) {
        this.statPercent = (stat*100/maxStat);
    }
}
