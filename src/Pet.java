import java.io.Serializable;

/**
 * The main class that stores the attributes of a single pet.
 * There will probably be one of these per save file.
 */
public class Pet implements Serializable {

    private static final long serialVersionUID = 1L;

    /** The name of the pet (e.g. Fido) */ 
    private String name;
    /** The type of the pet (e.g. Dog) */
    private String type;

    /** The health of the pet (0-100) */
    private int health;
    /** The energy of the pet (0-100) */
    private int energy;
    /** The fullness of the pet (0-100) */
    private int fullness;
    /** The happiness of the pet (0-100) */
    private int happiness;

    /** The max amount of health the pet can have */
    private int maxHealth;
    /** The max amount of energy the pet can have */
    private int maxEnergy;
    /** The max amount of fullness the pet can have */
    private int maxFullness;
    /** The max amount of happiness the pet can have */
    private int maxHappiness;

    /** If the pet is currently in the angry state */
    private boolean isInAngryState;
    private boolean isInSleepingState;

    /** The inventory that this pet uses */
    private Inventory inventory;
    /** The gacha system this pet uses */
    private Gacha gacha;

    /** The score of the pet, as per the functional requirements */
    private int score;
    /** The number of coins the pet has */
    private int coins;
    /** The evolution level of the pet */
    private int evoLevel;

    /**
     * Creates a new pet, probably called once per save file.
     * <p>
     * This pet starts with a default value of 75/100 points for all stats (feel free to change).
     * The pet's stats are: health, energy, fullness, happiness, hydration, intelligence.
     * There is also a stat for score (as per the functional requirements) and evoLevel.
     * <p>
     * You should call methods in this class depending on player interaction.
     * <p>
     * The default max values for all stats are 100. You can change this using the setMax methods.
     * <p>
     * Please read the method descriptions for more information.
     * </p>
     * @param name - The name given to the pet (e.g. Fido)
     * @param type - The pet type (e.g. Dog)
     * @param inventory - The inventory that this pet uses. Create a new inventory class and pass it here.
     * @param gacha - The gacha system that this pet uses. Create a new gacha class and pass it here.
     */
    public Pet(String name, String type, Inventory inventory, Gacha gacha) {
        this.name = name;
        this.type = type;
        this.inventory = inventory;
        this.gacha = gacha;

        this.health = 75;
        this.energy = 75;
        this.fullness = 75;
        this.happiness = 75;

        this.isInAngryState = false;
        this.isInSleepingState = false;

        this.maxHealth = 100;
        this.maxEnergy = 100;
        this.maxFullness = 100;
        this.maxHappiness = 100;

        this.coins = 0;
        this.score = 0;
        this.evoLevel = 1;
    }

    /** Increases the pet's health by the specified amount.
     * Can be negative to decrease health.
     * The pet's stat will remain between 0 and 100.
     * @param amount - The amount to increase the health by
     */
    public void incrementHealth(int amount) {
        this.health = Math.max(0, Math.min(maxHealth, this.health + amount));
    }

    /** Increases the pet's energy by the specified amount.
     * Can be negative to decrease energy.
     * The pet's stat will remain between 0 and 100.
     * @param amount - The amount to increase the energy by
     */
    public void incrementEnergy(int amount) {
        this.energy = Math.max(0, Math.min(maxEnergy, this.energy + amount));
    }

    /** Increases the pet's fullness by the specified amount.
     * Can be negative to decrease fullness.
     * The pet's stat will remain between 0 and 100.
     * @param amount - The amount to increase the fullness by
     */
    public void incrementFullness(int amount) {
        this.fullness = Math.max(0, Math.min(maxFullness, this.fullness + amount));
    }

    /** Increases the pet's happiness by the specified amount.
     * Can be negative to decrease happiness.
     * The pet's stat will remain between 0 and 100.
     * @param amount - The amount to increase the happiness by
     */
    public void incrementHappiness(int amount) {
        this.happiness = Math.max(0, Math.min(maxHappiness, this.happiness + amount));
    }

    /** Increases the pet's score by the specified amount.
     * Can be negative to decrease score.
     * No need to call this method for stat increases (already done in this class).
     * The pet's score will remain at a minimum of 0.
     * @param amount - The amount to increase the score by
     */
    public void incrementScore(int amount) {
        this.score = Math.max(0, this.score + amount);
    }

    /** Increases the pet's coins by the specified amount.
     * Can be negative to decrease coins.
     * The pet's coins will remain at a minimum of 0.
     * @param amount - The amount to increase the coins by
     */
    public void incrementCoins(int amount) {
        this.coins = Math.max(0, this.coins + amount);
    }

    /**
     * Decrements all stats besides health by 1.
     * @param amount the value to decrease stats by. Should be positive.
     */
    public void decrementAllStats(int amount) {
        incrementHappiness(amount*(-1));
        incrementEnergy(amount*(-1));
        incrementFullness(amount*(-1));
        if (happiness < 0) happiness = 0;
        if (energy < 0) energy = 0;
        if (fullness < 0) fullness = 0;
    }

    /**
     * Sets the pet's health to the specified amount.
     * @param maxHealth - The new health value
     */
    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }

    /**
     * Sets the pet's energy to the specified amount.
     * @param maxEnergy - The new energy value
     */
    public void setMaxEnergy(int maxEnergy) {
        this.maxEnergy = maxEnergy;
    }

    /**
     * Sets the pet's fullness to the specified amount.
     * @param maxFullness - The new fullness value
     */
    public void setMaxFullness(int maxFullness) {
        this.maxFullness = maxFullness;
    }

    /**
     * Sets the pet's happiness to the specified amount.
     * @param maxHappiness - The new happiness value
     */
    public void setMaxHappiness(int maxHappiness) {
        this.maxHappiness = maxHappiness;
    }

    /** 
     * Returns the name of the pet.
     * @return the name of the pet
     */
    public String getName() {
        return name;
    }

    /** 
     * Returns the type of the pet.
     * @return the type of the pet
     */
    public String getType() {
        return type;
    }

    /** 
     * Returns the health of the pet.
     * @return the health of the pet
     */
    public int getHealth() {
        return health;
    }

    /** 
     * Returns the energy of the pet.
     * @return the energy of the pet
     */
    public int getEnergy() {
        return energy;
    }

    /** 
     * Returns the fullness of the pet.
     * @return the fullness of the pet
     */
    public int getFullness() {
        return fullness;
    }

    /** 
     * Returns the happiness of the pet.
     * @return the happiness of the pet
     */
    public int getHappiness() {
        return happiness;
    }

    /**
     * Checks to see if the pet is currently angry.
     * @return true if the pet is angry, false otherwise.
     */
    public boolean IsInAngryState() {
        return isInAngryState;
    }

    public boolean IsInSleepingState() {
        return isInSleepingState;
    }

    /**
     * Returns an inventory object for the pet.
     * @return the inventory object for the pet
     */
    public Inventory getInventory() {
        return inventory;
    }

    /**
     * Returns a gacha object for the pet.
     * Can be used to access gacha methods like rollGacha.
     * @return the gacha object for the pet
     */
    public Gacha getGacha() {
        return gacha;
    }

    /**
     * Returns the evolution level of the pet.
     * @return the evolution level of the pet
     */
    public int getEvoLevel() {
        return evoLevel;
    }

    /**
     * Returns the score of the pet.
     * @return the score of the pet
     */
    public int getScore() {
        return score;
    }

    public int getCoins() {
        return coins;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public int getMaxEnergy() {
        return maxEnergy;
    }

    public int getMaxFullness() {
        return maxFullness;
    }

    public int getMaxHappiness() {
        return maxHappiness;
    }

    /**
     * Returns if the pet is dead.
     * If the pet died, the game should be over.
     * The player should not be able to do anything in this save file anymore unless the pet is revived via parental controls.
     * @return true if the pet is dead, false otherwise
     */
    public boolean isDead() {
        return health == 0;
    }

    /**
     * Returns if the pet is sleeping.
     * If the pet is sleeping, there should be a health penalty.
     * No player interaction can be done until the pet wakes up, where the energy level will return to 100.
     * @return true if the pet is sleeping, false otherwise
     */
    public boolean isSleeping() {
        if (energy == 0) {
            isInSleepingState = true;
            return true;
        }
        return false;
    }

    public boolean isSleepy() {
        return ((energy*100)/(maxEnergy)) <= 25;
    }

    public boolean isSick() {
        return ((health*100)/(maxHealth)) <= 25;
    }

    /**
     * Returns if the pet is hungry.
     * If the pet is hungry, their happiness and health should decrease faster over time.
     * @return true if the pet is hungry, false otherwise
     */
    public boolean isHungry() {
        return fullness == 0;
    }

    /**
     * Returns if the pet is angry.
     * If the pet is angry, they should refuse all commands that don't increase happiness.
     * The pet remains in this state until their happiness reaches at least 50.
     * @return true if the pet is angry, false otherwise
     */
    public boolean isAngry() {
        if (happiness == 0) isInAngryState = true;
        return happiness == 0;
    }

    /** 
     * Should only be called if the pet is in the angry state.
     * If the pet is not longer angry, return true.
     * @return true if the pet has recovered from the angry state, false otherwise.
     */
    public boolean isStillAngry() {
        if (!isInAngryState || !(happiness < 0.5*maxHappiness)) {
            isInAngryState = false;
            return false;
        }
        return true;
    }

    public boolean isStillSleeping() {
        if (!isInSleepingState || energy == maxEnergy) {
            isInSleepingState = false;
            return false;
        }
        return true;
    }

    public void setSleepingState(boolean state) {
        isInSleepingState = state;
    }

    /**
     * Checks all negative states. If none of them are true, then the pet is normal.
     * @return true if the pet is normal, false otherwise
     */
    public boolean isNormal() {
        return !isDead() && !isSleeping() && !isHungry() && !isAngry();
    }

    /**
     * Adds an item to the pet's inventory.
     * This method takes an item object, and should only be called once per object type.
     * @see Inventory#addNewItem(Item)
     * @param item - The item to add to the inventory
     * @throws IllegalArgumentException if an item with the same name already exists in the inventory
     */
    public void addNewItemToInventory(Item item) {
        inventory.addNewItem(item);
    }

    /**
     * Increases or decreases the quantity of an item in the pet's inventory.
     * Used when the player gains or uses an item.
     * @see Inventory#updateItemQuantity(String, int)
     * @param itemName - The name of the item to update
     * @param value - The value to add to the current quantity. Can be positive or negative.
     */
    public void updateItemQuantity(String itemName, int value) {
        inventory.updateItemQuantity(itemName, value);
    }

    /**
     * Gives an item to the pet from the inventory.
     * If the item's quantity is zero, return null.
     * If the item does not exist, throws an exception.
     * <p>
     * When an item is used, the item's quantity should decrease by 1.
     * The corresponding stat will increase based on the value specified in the item class.
     * If it is a food, fullness will increase.
     * If it is a gift, happiness will increase.
     * @see Inventory#useItem(String)
     * @param itemName - The name of the item to give
     * @return the item if its quantity is greater than 0, or null if the item's quantity is 0
     */
    public Item giveItem(String itemName) {
        Item item = inventory.useItem(itemName);
        if (item == null) {
            return null;
        }
        if (item.getItemType().equalsIgnoreCase("food")) {
            incrementFullness(item.getScoreIncrease());
        }
        else if (item.getItemType().equalsIgnoreCase("gift")) {
            incrementHappiness(item.getScoreIncrease());
        }
        return item;
    }

    /**
     * Increases the evolution level by 1.
     * Should be used with a condition in the front-end (e.g. if score is greater than a certain amount).
     */
    public void evolve() {
        evoLevel++;
    }
    

}
