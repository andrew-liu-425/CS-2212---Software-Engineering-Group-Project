import javafx.scene.layout.StackPane;

/** Represents the class that contains the pet UI, including the pet's states. */
public class PetOverlay {

    /** The StackPane to hold UI elements */
    private StackPane root;
    /** The sprite manager to create pet sprites */
    private SpriteManager petSprite;

    /** The path to the sprite image */
    private String spritePath;
    /** The width of the sprite */
    private int spriteWidth;
    /** The height of the sprite */
    private int spriteHeight;
    /** The number of rows in the sprite sheet */
    private int rows;
    /** The number of columns in the sprite sheet */
    private int columns;

    /**
     * Creates a new PetOverlay.
     * <p>
     * This overlay displays the pet's sprite and manages its animation.
     */
    public PetOverlay() {
        spriteWidth = 2680; 
        spriteHeight = 5360; 
        rows = 4;
        columns = 8;
        root = new StackPane();
        
    }

    /** Constructs the pet's sprites using the sprite manager class.
     * Different sprites are used for different pet types.
     * @param pet The pet object to be displayed in the overlay.
     */
    public void buildPet(Pet pet) {

        switch (pet.getType()) {
            case ("Dog"):
                spritePath = "../assets/sprites/babyDogSprite.png";
                petSprite = new SpriteManager(spritePath, spriteWidth, spriteHeight, rows, columns, pet);
                petSprite.extractFrames("../assets/sprites/teenDogSprite.png");
                petSprite.extractFrames("../assets/sprites/adultDogSprite.png");
                break;
            case ("Cat"):
                spritePath = "../assets/sprites/babyCatSprite.png";
                petSprite = new SpriteManager(spritePath, spriteWidth, spriteHeight, rows, columns, pet);
                petSprite.extractFrames("../assets/sprites/teenCatSprite.png");
                petSprite.extractFrames("../assets/sprites/adultCatSprite.png");
                break;
            case ("Duck"):
                spritePath = "../assets/sprites/babyDuckSprite.png";
                petSprite = new SpriteManager(spritePath, spriteWidth, spriteHeight, rows, columns, pet);
                petSprite.extractFrames("../assets/sprites/teenDuckSprite.png");
                petSprite.extractFrames("../assets/sprites/adultDuckSprite.png");
                break;
            case ("Dinosaur"):
                spritePath = "../assets/sprites/babyDragonSprite.png";
                petSprite = new SpriteManager(spritePath, spriteWidth, spriteHeight, rows, columns, pet);
                petSprite.extractFrames("../assets/sprites/teenDragonSprite.png");
                petSprite.extractFrames("../assets/sprites/adultDragonSprite.png");
                break;
            default:
                throw new IllegalArgumentException("You entered the wrong name.");
        }

        // Set the initial frame
        petSprite.setCurrentFrame(0);

        // Add the sprite to the overlay
        root.getChildren().add(petSprite.getSpriteView());

        petSprite.startAnimation();

        // Resize sprite to a specific size when the PetOverlay is created
        petSprite.scaleSprite(100, 200);
    }
    
    /** Adds the overlay to the gameplay screens.
     * 
     * @param parentRoot The parent root to which the overlay will be added.
     */
    public void addToRoot(StackPane parentRoot) {
        parentRoot.getChildren().add(root);
    }

    /** Returns the root of this overlay.
     * 
     * @return The root of this overlay.
     */
    public StackPane getRoot() {
        return root;
    }

    /**
     * Updates the scaling of the sprite based on the screen size.
     * @param width the width of the screen
     * @param height the height of the screen
     */
    public void updateScaling(double width, double height) {
        // Scale the sprite dynamically
        petSprite.scaleSprite(width * 0.5, height * 1); 

        // Translate the sprite based on screen size 
        petSprite.translateSprite(0.125, 0.6, width, height);
    }

    /** Starts the sprite's animation. Used when the gameplay screens are active. */
    public void startAnimation() {
        petSprite.startAnimation();
    }

    /** Stops the sprite's animation. */
    public void stopAnimation() {
        petSprite.stopAnimation();
    }

    /** Sets the sprite's current frame. */
    public void setSpriteFrame(int frameIndex) {
        petSprite.setCurrentFrame(frameIndex);
    }

    /** Returns the sprite manager for this overlay.
     * 
     * @return The sprite manager for this overlay.
     */
    public SpriteManager getSpriteManager() {
        return petSprite;
    }
}
