import java.io.IOException;

import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.control.Button;

/** Represents the screen where the pet can be healed, increasing health.
 * <p>
 * There is a heal button which restores health. However, there is a cooldown before it can be used again.
 */
public class VetScene extends GameScene {

    /** The scene to hold UI elements. */
    private Scene scene;
    /** The background image. */
    private ImageView backgroundImageView;
    /** The screen manager to manage screen transitions. */
    private ScreenManager screenManager;
    /** The screen overlay that holds UI elements besides the pet. */
    private ScreenOverlay screenOverlay;
    /** The pet overlay that holds UI elements for the pet. */
    private PetOverlay petOverlay;
    /** The pet object. */
    private Pet pet;
    /** The button that heals the pet. */
    private Button commandButton;
    /** The cooldown for the heal button. */
    private int vetCooldown;

    /**
     * Constructs a new vet scene.
     * <p>
     * In this screen, the pet can be healed, which will increase health. However, the heal button has a cooldown.
     * @param screenManager the screen manager to handle transitions.
     * @param pet the pet object.
     */
    public VetScene(ScreenManager screenManager, Pet pet) throws IOException {
        this.screenManager = screenManager;
        this.pet = pet;
        this.vetCooldown = 0;

        StackPane root = new StackPane();
        root.setPrefSize(Main.WIDTH, Main.HEIGHT);

        // Load background image
        backgroundImageView = new ImageView();
        ImageManager.loadImage(backgroundImageView, "../assets/backgrounds/VetBackground.png");
        ImageManager.scaleBackground(backgroundImageView, Main.WIDTH, Main.HEIGHT);

        // Initialize screen overlay and pass screenManager
        screenOverlay = new ScreenOverlay(screenManager, pet);
        petOverlay = new PetOverlay();
        petOverlay.buildPet(pet);
        
        commandButton = new Button("Heal");
        ButtonManager.registerButton(commandButton, 300, 60, 50);

        // heal button logic
        commandButton.setOnAction(e -> {
            commandButton.setDisable(true); 
            vetCooldown = 15;
            pet.incrementHealth(100);
            pet.incrementScore(100);
            screenOverlay.setScoreText("SCORE: " + String.valueOf(pet.getScore()));
            screenManager.getVetCooldownTimer().start();
            updatePetState();
            UIUpdate.updateUI(pet, petOverlay.getSpriteManager(), screenOverlay.getStatBars(), true);
            System.out.println("Healing");
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
     * Returns the scene for UI elements.
     * @return the scene.
     */
    public Scene getScene() {
        return scene;
    }

    /** Helper method to dynamically resize UI elements.
     * @see #updateScaling()
     */
    private void addResizeListeners() {
        scene.widthProperty().addListener((obs, oldVal, newVal) -> updateScaling());
        scene.heightProperty().addListener((obs, oldVal, newVal) -> updateScaling());
    }

    /** Helper method to dynamically resize UI elements. */
    private void updateScaling() {
        double currentWidth = scene.getWidth();
        double currentHeight = scene.getHeight();

        ImageManager.scaleBackground(backgroundImageView, currentWidth, currentHeight);
        screenOverlay.updateScaling(currentWidth, currentHeight); 
        petOverlay.updateScaling(currentWidth, currentHeight);
        ButtonManager.translateButton(commandButton, 0.35, 0.4);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPet(Pet pet) {
        this.pet = pet;
        petOverlay.buildPet(pet);
        addResizeListeners();  
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
            if (!pet.isStillSleeping() && vetCooldown == 0) {
                commandButton.setDisable(false);
            }
        }
        screenManager.getStatTimer().start();
        screenManager.getVetCooldownTimer().start();
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
    public void onVetCooldownTick() {
        if (vetCooldown > 0) {
            commandButton.setText(String.valueOf(vetCooldown));
            vetCooldown--;
            commandButton.setDisable(true);
        }
        else {
            commandButton.setText("Heal");
            if (!pet.isStillSleeping() && !pet.isStillAngry()) {
                commandButton.setDisable(false);
            }
            screenManager.getPlayCooldownTimer().pause();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onSleepTick() {
        if (pet.isStillSleeping()) {
            pet.incrementEnergy(10);
            UIUpdate.updateUI(pet, petOverlay.getSpriteManager(), screenOverlay.getStatBars(), false);
        }
        if (!pet.isStillSleeping()) {
            screenManager.getSleepTimer().pause();
            screenOverlay.getLeftArrowButton().setDisable(false);
            screenOverlay.getRightArrowButton().setDisable(false);
            screenOverlay.getInventoryButton().setDisable(false);
            screenOverlay.getSettingsButton().setDisable(false);
            if (!pet.isStillAngry() && vetCooldown == 0) {
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
            if (!pet.isStillSleeping() && vetCooldown == 0) {
                commandButton.setDisable(false);
            }
        }
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
            screenManager.getVetCooldownTimer().stop();
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
            screenManager.getVetCooldownTimer().stop();
            pet.decrementAllStats(1000);
            return;
        }
        

    }
}
