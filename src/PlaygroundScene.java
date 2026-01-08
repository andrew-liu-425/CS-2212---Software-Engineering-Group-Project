import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

import java.io.IOException;

import javafx.geometry.Pos;
import javafx.scene.text.Font;

/** Represents the playground screen.
 * <p>
 * In this screen, the player can play with their pet and exercise it.
 * Playing with the pet will increase happiness, while exercising will increase health at the cost of fullness and energy.
 */
public class PlaygroundScene extends GameScene {

    /** The scene to hold UI elements. */
    private Scene scene;
    /** The image view for the background. */
    private ImageView backgroundImageView;
    /** The screen manager to manage scene transitions. */
    private ScreenManager screenManager;
    /** The screen overlay to display UI elements besides the pet. */
    private ScreenOverlay screenOverlay;
    /** The pet overlay to display the pet. */
    private PetOverlay petOverlay;
    /** The pet object representing the player's pet. */
    private Pet pet;
    /** The container for the buttons. */
    private HBox playCommands;
    /** The button to exercise the pet. */
    private Button exerciseButton;
    /** The button to play with the pet. */
    private Button playButton;

    /** The cooldown timer for the play button. */
    private int playCooldown;

    /**
     * Constructs the playground scene.
     * <p>
     * In this scene, the player can play with their pet and exercise it.
     * Playing with the pet will increase happiness, while exercising will increase health at the cost of fullness and energy.
     * @param screenManager The screen manager to manage scene transitions.
     * @param pet The pet object representing the player's pet.
     */
    public PlaygroundScene(ScreenManager screenManager, Pet pet) throws IOException {
        this.screenManager = screenManager;
        this.playCooldown = 0;
        this.pet = pet;

        StackPane root = new StackPane();
        root.setPrefSize(Main.WIDTH, Main.HEIGHT);

        playCommands = new HBox();
        playCommands.setAlignment(Pos.BOTTOM_RIGHT);
        root.getChildren().add(playCommands);

        // Load background image
        backgroundImageView = new ImageView();
        ImageManager.loadImage(backgroundImageView, "../assets/backgrounds/PlaygroundBackground.png");
        ImageManager.scaleBackground(backgroundImageView, Main.WIDTH, Main.HEIGHT);

        // Play button
        playButton = new Button("Play");
        playButton.setOnAction(e -> {
            playButton.setDisable(true);
            TextManager.clearTextElements();
            ButtonManager.clearButtonElements();
            pet.incrementHappiness(80);
            pet.incrementScore(80);
            screenOverlay.setScoreText("SCORE: " + String.valueOf(pet.getScore()));
            playCooldown = 10;
            screenManager.getPlayCooldownTimer().start();
            updatePetState();
            UIUpdate.updateUI(pet, petOverlay.getSpriteManager(), screenOverlay.getStatBars(), true);
        });
        
        // Exercise button
        exerciseButton = new Button("Exercise");
        exerciseButton.setOnAction(e -> {
            pet.incrementHealth(40);
            pet.incrementEnergy(-20);
            pet.incrementFullness(-20);
            pet.incrementScore(40);
            screenOverlay.setScoreText("SCORE: " + String.valueOf(pet.getScore()));
            updatePetState();
            UIUpdate.updateUI(pet, petOverlay.getSpriteManager(), screenOverlay.getStatBars(), false);
        });

        playButton.setMinSize(75, 50);
        exerciseButton.setMinSize(75, 50);
        playButton.setFont(new Font(40));
        exerciseButton.setFont(new Font(40));

        playCommands = new HBox(10, playButton, exerciseButton);

        // Initialize screen overlay and pass screenManager
        screenOverlay = new ScreenOverlay(screenManager, pet);
        petOverlay = new PetOverlay();
        petOverlay.buildPet(pet);

        // Add elements to root layout
        root.getChildren().addAll(backgroundImageView);
        petOverlay.addToRoot(root);
        screenOverlay.addToRoot(root); 
        root.getChildren().addAll(playCommands);

        // Create the scene
        scene = new Scene(root, Main.WIDTH, Main.HEIGHT);

        // Add resize listeners
        addResizeListeners();

        // Enforce 16:9 aspect ratio
        Main.enforceAspectRatio(screenManager.getStage());
    }

    /**
     * Returns the scene of the playground screen.
     * @return The scene of the playground screen.
     */
    public Scene getScene() {
        return scene;
    }

    /**
     * Helper method that adds listeners to the scene to handle resizing.
     */
    private void addResizeListeners() {
        scene.widthProperty().addListener((obs, oldVal, newVal) -> updateScaling());
        scene.heightProperty().addListener((obs, oldVal, newVal) -> updateScaling());
    }

    /**
     * Updates the scaling of the UI elements based on the scene size.
     * <p>
     * This method is called whenever the scene is resized.
     */
    private void updateScaling() {
        double width = scene.getWidth();
        double height = scene.getHeight();

        ImageManager.scaleBackground(backgroundImageView, width, height);
        screenOverlay.updateScaling(width, height); 
        petOverlay.updateScaling(width, height);
        playCommands.setTranslateY(height* 0.825);
        playCommands.setTranslateX(width * 0.65);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onEnter() {
        if (pet.isDead()) {
            exerciseButton.setDisable(true);
            playButton.setDisable(true);
            screenOverlay.getLeftArrowButton().setDisable(true);
            screenOverlay.getRightArrowButton().setDisable(true);
            screenOverlay.getInventoryButton().setDisable(true);
            return;
        }
        if (pet.isStillAngry()) {
            exerciseButton.setDisable(true);
            screenManager.getAngryTimer().start();
        }
        if (!pet.isStillAngry()) {
            screenManager.getAngryTimer().pause();
            if (!pet.isStillSleeping()) {
                exerciseButton.setDisable(false);
            }     
        }
        screenManager.getStatTimer().start();
        screenManager.getPlayCooldownTimer().start();
        
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
    public void onPlayCooldownTick() {
        if (playCooldown > 0) {
            playButton.setText(String.valueOf(playCooldown));
            playCooldown--;
            playButton.setDisable(true);
        }
        else {
            playButton.setText("Play");
            if (!pet.isStillSleeping()) {
                playButton.setDisable(false);
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
            if (!pet.isStillAngry()) {
                playButton.setDisable(false);
                exerciseButton.setDisable(false);
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
                exerciseButton.setDisable(false);
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
            playButton.setDisable(true);
            exerciseButton.setDisable(true);
            screenOverlay.getLeftArrowButton().setDisable(true);
            screenOverlay.getRightArrowButton().setDisable(true);
            screenOverlay.getInventoryButton().setDisable(true);
            screenOverlay.getSettingsButton().setDisable(false);
            screenManager.getStatTimer().stop();
            screenManager.getSleepTimer().stop();
            screenManager.getAngryTimer().stop();
            screenManager.getPlayCooldownTimer().stop();
            pet.decrementAllStats(1000);
            return;
        }
        if (pet.isSleeping()) {
            pet.incrementHealth(-40);
            playButton.setDisable(true);
            exerciseButton.setDisable(true);
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
            exerciseButton.setDisable(true);
            screenManager.getAngryTimer().start();
        }  
        if (pet.isDead()) {
            playButton.setDisable(true);
            exerciseButton.setDisable(true);
            screenOverlay.getLeftArrowButton().setDisable(true);
            screenOverlay.getRightArrowButton().setDisable(true);
            screenOverlay.getInventoryButton().setDisable(true);
            screenOverlay.getSettingsButton().setDisable(false);
            screenManager.getStatTimer().stop();
            screenManager.getSleepTimer().stop();
            screenManager.getAngryTimer().stop();
            screenManager.getPlayCooldownTimer().stop();
            pet.decrementAllStats(1000);
            return;
        }
        

    }
}
