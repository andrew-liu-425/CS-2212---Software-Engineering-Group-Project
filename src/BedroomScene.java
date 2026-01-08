import java.io.IOException;

import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.control.Button;

/**
 * Represents the UI and logic for the bedroom scene in EvoPets.
 * <p>
 * In this scene, the pet can sleep. During sleep, the pet will slowly increase their energy value over time.
 * All buttons are not functional when the pet is sleeping.
 */
public class BedroomScene extends GameScene {

    /** The UI scene for the bedroom. */
    private Scene scene;
    /** The image for the background picture. */
    private ImageView backgroundImageView;
    /** The screen manager class this class uses. */
    private ScreenManager screenManager;
    /** The UI elements besides the pet that are consistent over screens. */
    private ScreenOverlay screenOverlay;
    /** The UI elements for the pet, including sprites. */
    private PetOverlay petOverlay;
    /** The pet object that is being used in the scene. */
    private Pet pet;
    /** The button that allows the pet to go to sleep. */
    private Button commandButton;

    /** 
     * Constructs the bedroom scene and stores it inside the screen manager.
     * <p>
     * The bedroom scene is where the pet can sleep to regenerate energy over time.
     * This constructor will build all UI elements for the bedroom, including buttons.
     * To switch to this scene, use the screen manager class.
     * 
     * @param screenManager The screen manager that stores this scene.
     * @param pet The pet object that will be used in the scene.
     * 
     */
    public BedroomScene(ScreenManager screenManager, Pet pet) throws IOException {
        this.screenManager = screenManager;
        this.pet = pet;

        // Create the root layout
        StackPane root = new StackPane();
        root.setPrefSize(Main.WIDTH, Main.HEIGHT);

        // Load background image
        backgroundImageView = new ImageView();
        ImageManager.loadImage(backgroundImageView, "../assets/backgrounds/BedroomBackground.png");
        ImageManager.scaleBackground(backgroundImageView, Main.WIDTH, Main.HEIGHT);

        // Initialize screen overlay and pass screenManager
        screenOverlay = new ScreenOverlay(screenManager, pet);
        petOverlay = new PetOverlay();
        petOverlay.buildPet(pet);

        // Create sleep button
        commandButton = new Button("Sleep");
        ButtonManager.registerButton(commandButton, 300, 60, 50);

        // Set sleep button action
        commandButton.setOnAction(e -> {
            commandButton.setDisable(true);
            screenOverlay.getLeftArrowButton().setDisable(true);
            screenOverlay.getRightArrowButton().setDisable(true);
            screenOverlay.getInventoryButton().setDisable(true);
            screenOverlay.getSettingsButton().setDisable(true);
            screenManager.getSleepTimer().start();
            pet.setSleepingState(true);
            UIUpdate.updateUI(pet, petOverlay.getSpriteManager(), screenOverlay.getStatBars(), false);
            System.out.println("Sleeping");
        });

        // Add elements to root layout
        root.getChildren().addAll(backgroundImageView);
        petOverlay.addToRoot(root);
        screenOverlay.addToRoot(root); 
        root.getChildren().addAll(commandButton);

        // Create the scene
        scene = new Scene(root, Main.WIDTH, Main.HEIGHT);

        // Add resize listeners
        addResizeListeners();

        // Enforce 16:9 aspect ratio
        Main.enforceAspectRatio(screenManager.getStage());
    }

    /**
     * Returns the scene for the bedroom.
     * @return The scene for the bedroom.
     */
    public Scene getScene() {
        return scene;
    }

    /**
     * This helper method is used to scale the background image and UI elements.
     * <p>
     * @see BedroomScene#updateScaling()
     */
    private void addResizeListeners() {
        scene.widthProperty().addListener((obs, oldVal, newVal) -> updateScaling());
        scene.heightProperty().addListener((obs, oldVal, newVal) -> updateScaling());
    }

    /**
     * This helper method is used to scale the background image and UI elements.
     * <p>
     * @see BedroomScene#addResizeListeners()
     */
    private void updateScaling() {
        double currentWidth = scene.getWidth();
        double currentHeight = scene.getHeight();

        ImageManager.scaleBackground(backgroundImageView, currentWidth, currentHeight);
        petOverlay.updateScaling(currentWidth, currentHeight);
        screenOverlay.updateScaling(currentWidth, currentHeight); 
        ButtonManager.translateButton(commandButton, 0.35, 0.4);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Pet getPet() {
        return this.pet;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onEnter() {
        if (pet.isStillAngry()) {
            commandButton.setDisable(true);
            screenManager.getAngryTimer().start();
        }
        if (!pet.isStillAngry()) {
            if (!pet.isStillSleeping()) {
                commandButton.setDisable(false);
            }     
        }
        screenManager.getStatTimer().start();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onTimerTick() {
        if (pet.isHungry()) pet.incrementHappiness(-20);
        pet.decrementAllStats(8);
        pet.incrementCoins(1);
        screenOverlay.setCoinsText("COINS: " + String.valueOf(pet.getCoins()));
        updatePetState();
        UIUpdate.updateUI(pet, petOverlay.getSpriteManager(), screenOverlay.getStatBars(), false);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onSleepTick() {
        if (pet.isStillSleeping()) {
            pet.incrementEnergy(15);
            pet.incrementScore(15);
            screenOverlay.setScoreText("SCORE: " + String.valueOf(pet.getScore()));
            UIUpdate.updateUI(pet, petOverlay.getSpriteManager(), screenOverlay.getStatBars(), false);
        }
        if (!pet.isStillSleeping()) {
            screenManager.getSleepTimer().pause();
            screenOverlay.getLeftArrowButton().setDisable(false);
            screenOverlay.getRightArrowButton().setDisable(false);
            screenOverlay.getInventoryButton().setDisable(false);
            screenOverlay.getSettingsButton().setDisable(false);
            if (!pet.isStillAngry()) {
                commandButton.setDisable(false);
            }       
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onAngryTick() {
        if (!pet.isStillAngry()) {
            screenManager.getAngryTimer().pause();
            if (!pet.isStillSleeping()) {
                commandButton.setDisable(false);
            }     
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ScreenOverlay getScreenOverlay() {
        return screenOverlay;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PetOverlay getPetOverlay() {
        return petOverlay;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void updatePetState() {
        if (pet.isDead()) {
            commandButton.setDisable(true);
            screenOverlay.getLeftArrowButton().setDisable(true);
            screenOverlay.getRightArrowButton().setDisable(true);
            screenOverlay.getInventoryButton().setDisable(true);
            screenOverlay.getSettingsButton().setDisable(false);
            screenManager.getStatTimer().stop();
            screenManager.getSleepTimer().stop();
            screenManager.getAngryTimer().stop();
            pet.decrementAllStats(1000);
            return;
        }
        if (pet.isSleeping()) {
            pet.incrementHealth(-40);
            commandButton.setDisable(true);
            screenOverlay.getLeftArrowButton().setDisable(true);
            screenOverlay.getRightArrowButton().setDisable(true);
            screenOverlay.getInventoryButton().setDisable(true);
            screenOverlay.getSettingsButton().setDisable(true);
            screenManager.getSleepTimer().start();
        }
        if (pet.isHungry()) {
            pet.incrementHealth(-20);
            pet.incrementHappiness(-20);
        }
        if (pet.isAngry()) {
            commandButton.setDisable(true);
            screenManager.getAngryTimer().start();
        }     
        if (pet.isDead()) {
            commandButton.setDisable(true);
            screenOverlay.getLeftArrowButton().setDisable(true);
            screenOverlay.getRightArrowButton().setDisable(true);
            screenOverlay.getInventoryButton().setDisable(true);
            screenOverlay.getSettingsButton().setDisable(false);
            screenManager.getStatTimer().stop();
            screenManager.getSleepTimer().stop();
            screenManager.getAngryTimer().stop();
            pet.decrementAllStats(1000);
            return;
        }
    } 
}
