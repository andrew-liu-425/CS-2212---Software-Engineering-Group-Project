import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.HBox;
import javafx.scene.control.Button;

import java.io.IOException;

import javafx.geometry.Pos;
import javafx.scene.text.Font;

/** Represents the kitchen scene.
 * <p>
 * In this scene, the pet can purchase food using coins. The food will be brought into the inventory, where the pet can eat the food.
 * The pet's stats will be updated based on the food purchased and eaten.
 */
public class KitchenScene extends GameScene {

    /** The scene that holds the UI. */
    private Scene scene;
    /** The background image. */
    private ImageView backgroundImageView;
    /** The screen manager that manages screen transitions. */
    private ScreenManager screenManager;
    /** The UI elements besides the pet. */
    private ScreenOverlay screenOverlay;
    /** The UI elements of the pet. */
    private PetOverlay petOverlay;
    /** The pet object. */
    private Pet pet;
    /** The UI container tha holds the food purchase buttons. */
    private HBox foodContainer;
    /** The button to purchase a burger. */
    private Button burgerButton;
    /** The button to purchase a pizza. */
    private Button pizzaButton;
    /** The button to purchase a salad. */
    private Button saladButton;

    /** Creates a kitchen scene.
     * <p>
     * In this scene, the pet can purchase food using coins. The food will be brought into the inventory, where the pet can eat the food.
     * The pet's stats will be updated based on the food purchased and eaten.
     * @param screenManager the screen manager that manages screen transitions
     * @param pet the pet object
     */
    public KitchenScene(ScreenManager screenManager, Pet pet) throws IOException{
        this.screenManager = screenManager;
        this.pet = pet;

        // holds UI elements
        StackPane root = new StackPane();
        root.setPrefSize(Main.WIDTH, Main.HEIGHT);

        // Create the food container
        foodContainer = new HBox();
        foodContainer.setAlignment(Pos.BOTTOM_RIGHT);
        root.getChildren().add(foodContainer);

        // Load background image
        backgroundImageView = new ImageView();
        ImageManager.loadImage(backgroundImageView, "../assets/backgrounds/KitchenBackground.png");
        ImageManager.scaleBackground(backgroundImageView, Main.WIDTH, Main.HEIGHT);

        // burger button
        burgerButton = new Button("Buy Burger (2 coins)");
        burgerButton.setOnAction(e -> {
            if (pet.getCoins() >= 2) {
                pet.getInventory().updateItemQuantity("Burger", 1);
                pet.incrementCoins(-2);
                if (pet.getCoins() < 7) {
                    saladButton.setDisable(true);
                }        
                if (pet.getCoins() < 5) {
                    pizzaButton.setDisable(true);
                }
                if (pet.getCoins() < 2) {
                    burgerButton.setDisable(true);
                }
            }
            screenOverlay.setCoinsText("COINS: " + String.valueOf(pet.getCoins()));
        });
        
        // pizza button
        pizzaButton = new Button("Buy Pizza (5 coins)");
        pizzaButton.setOnAction(e -> {
            if (pet.getCoins() >= 5) {
                pet.incrementCoins(-5);
                pet.getInventory().updateItemQuantity("Pizza", 1);
                if (pet.getCoins() < 7) {
                    saladButton.setDisable(true);
                }        
                if (pet.getCoins() < 5) {
                    pizzaButton.setDisable(true);
                }
                if (pet.getCoins() < 2) {
                    burgerButton.setDisable(true);
                }
            }
            screenOverlay.setCoinsText("COINS: " + String.valueOf(pet.getCoins()));
        });

        // salad button
        saladButton = new Button("Buy Salad (7 coins)");
        saladButton.setOnAction(e -> {
            if (pet.getCoins() >= 7) {
                pet.incrementCoins(-7);
                pet.getInventory().updateItemQuantity("Salad", 1);
                if (pet.getCoins() < 7) {
                    saladButton.setDisable(true);
                }
                if (pet.getCoins() < 5) {
                    pizzaButton.setDisable(true);
                }
                if (pet.getCoins() < 2) {
                    burgerButton.setDisable(true);
                }
            }
            screenOverlay.setCoinsText("COINS: " + String.valueOf(pet.getCoins()));
        });

        // Set button properties
        burgerButton.setMinSize(75, 50);
        pizzaButton.setMinSize(75, 50);
        saladButton.setMinSize(75, 50);
        burgerButton.setFont(new Font(20));
        pizzaButton.setFont(new Font(20));
        saladButton.setFont(new Font(20));

        foodContainer = new HBox(10, burgerButton, pizzaButton, saladButton);

        // Initialize screen overlay and pass screenManager
        screenOverlay = new ScreenOverlay(screenManager, pet);

        // builds pet UI
        petOverlay = new PetOverlay();
        petOverlay.buildPet(pet);

        // Add elements to root layout
        root.getChildren().add(backgroundImageView);
        petOverlay.addToRoot(root);
        screenOverlay.addToRoot(root); 
        root.getChildren().add(foodContainer);

        // Create the scene
        scene = new Scene(root, Main.WIDTH, Main.HEIGHT);

        // Add resize listeners
        addResizeListeners();

        // Enforce 16:9 aspect ratio
        Main.enforceAspectRatio(screenManager.getStage());
    }

    /** Returns the scene containing UI elements.
     * @return the scene
     */
    public Scene getScene() {
        return scene;
    }

    /**
     * Helper method used for dynamic UI scaling.
     * @see #updateScaling()
     */
    private void addResizeListeners() {
        scene.widthProperty().addListener((obs, oldVal, newVal) -> updateScaling());
        scene.heightProperty().addListener((obs, oldVal, newVal) -> updateScaling());
    }

    /** Helper method used to scale the UI elements, including background and others */
    private void updateScaling() {
        double width = scene.getWidth();
        double height = scene.getHeight();

        ImageManager.scaleBackground(backgroundImageView, width, height);
        screenOverlay.updateScaling(width, height); 
        petOverlay.updateScaling(width, height);
        foodContainer.setTranslateY(height* 0.825);
        foodContainer.setTranslateX(width * 0.5);
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
            burgerButton.setDisable(true);
            pizzaButton.setDisable(true);
            saladButton.setDisable(true);
            screenManager.getAngryTimer().start();
        }
        else {
            if (pet.getCoins() < 7) {
                saladButton.setDisable(true);
            }
            else {
                saladButton.setDisable(false);
            }
            if (pet.getCoins() < 5) {
                pizzaButton.setDisable(true);
            }
            else {
                pizzaButton.setDisable(false);
            }
            if (pet.getCoins() < 2) {
                burgerButton.setDisable(true);
            }
            else {
               burgerButton.setDisable(false);
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
        if (!pet.isStillAngry() && !pet.isStillSleeping()) {
            if (pet.getCoins() >= 2) {
                burgerButton.setDisable(false);
            }
            if (pet.getCoins() >= 5) {
                pizzaButton.setDisable(false);
            }
            if (pet.getCoins() >= 7) {
                saladButton.setDisable(false);
            }
        }
        updatePetState();
        UIUpdate.updateUI(pet, petOverlay.getSpriteManager(), screenOverlay.getStatBars(), false);
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
            if (!pet.isStillAngry() && pet.getCoins() >= 2) {
                burgerButton.setDisable(false);
                if (pet.getCoins() >= 5) {
                    pizzaButton.setDisable(false);
                }
                if (pet.getCoins() >= 7) {
                    saladButton.setDisable(false);
                }
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
            if (!pet.isStillSleeping() && pet.getCoins() >= 2) {
                burgerButton.setDisable(false);
                if (pet.getCoins() >= 5) {
                    pizzaButton.setDisable(false);
                }
                if (pet.getCoins() >= 7) {
                    saladButton.setDisable(false);
                }
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
            burgerButton.setDisable(true);
            pizzaButton.setDisable(true);
            saladButton.setDisable(true);
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
            burgerButton.setDisable(true);
            pizzaButton.setDisable(true);
            saladButton.setDisable(true);
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
            burgerButton.setDisable(true);
            pizzaButton.setDisable(true);
            saladButton.setDisable(true);
            screenManager.getAngryTimer().start();
        }  
        if (pet.isDead()) {
            burgerButton.setDisable(true);
            pizzaButton.setDisable(true);
            saladButton.setDisable(true);
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
