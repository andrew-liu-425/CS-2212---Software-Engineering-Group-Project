import java.io.Serializable;

/**
 * Represents an item in the inventory. Items can be food or gifts.
 * Insert these objects into the inventory class.
 */

public class Item implements Serializable {

    private static final long serialVersionUID = 1L;
    
    /** The name of the item (e.g. burger)*/ 
    private String itemName;
    /** The item type. Should be "food" or "gift". */
    private String itemType;
    /** How much of a stat this item gives. */
    private int scoreIncrease;
    /** The quantity of each item. */
    private int quantity;
    /** The probability that this item is pulled from gacha. */
    private int gachaChance;


    /**
     * Creates a new, unique item. Should be a food or gift.
     * Do not use this class if the item type already exists. For example, if there already exists a burger, don't create another "burger" item object.
     * The quantity of this item is set to 0 by default.
     * The gacha chance is set to 0 by default. To set a gacha chance, use the Gacha class.
     * <p>
     * To use this class, create a new item object and insert it into the inventory class, using the inventory's addItem method. You can use the inventory method to change the various values of this item.
     * You probably don't have to use the public methods in this class directly.
     * <p>
     * To add this item to gacha, use the Gacha class. You can set the gacha chance of this item using the Gacha class.
     * 
     * @param itemName - the unique name of the item (e.g. burger).
     * @param itemType - the type of the item. Must be "food" or "gift".
     * @param scoreIncrease - how much of a stat bonus this item gives. Must be a positive integer between 0 and 100.
     * @throws IllegalArgumentException if the score increase is not between 0 and 100, or if the quantity is not a positive integer. 
     * @throws IllegalArgumentException if the type does not equal "food" or "gift"
     */
    public Item(String itemName, String itemType, int scoreIncrease) throws IllegalArgumentException {
        if (scoreIncrease < 0 || scoreIncrease > 100) {
            throw new IllegalArgumentException("Score increase must be between 0 and 100.");
        }
        if (!itemType.equalsIgnoreCase("food") && !itemType.equalsIgnoreCase("gift")) {
            throw new IllegalArgumentException("Type must be 'food' or 'gift'.");
        }
        this.itemName = itemName;
        this.itemType = itemType;
        this.scoreIncrease = scoreIncrease;
        quantity = 0;
        gachaChance = 0;
    }

    /**
     * Returns the item's name (e.g. burger).
     * @return the name of the item
     */
    public String getItemName() {
        return itemName;
    }

    /**
     * Returns the item's type (e.g. "food" or "gift").
     * @return the type of the item
     */
    public String getItemType() {
        return itemType;
    }

    /**
     * Returns how much of a stat this item gives.
     * @return the score increase value of the item
     */
    public int getScoreIncrease() {
        return scoreIncrease;
    }

    /**
     * Returns the quantity of this item.
     * @return the quantity of the item
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Updates the quantity of this item by adding the specified value.
     * If the resulting quantity is less than 0, it is set to 0.
     * @param value the value to add to the current quantity. Can be positive or negative.
     */
    public void updateQuantity(int value) {
        quantity = quantity + value;
        if (quantity < 0) {
            quantity = 0;
        }
    }

    /**
     * Returns true if the item has a quantity greater than 0.
     * @param itemName the name of the item to check
     * @return true if the item has a quantity greater than 0, false otherwise
     */
    public boolean hasItem(String itemName) {
        return (quantity > 0);
    }

    /**
     * Sets the gacha chance for the item.
     * @param gachaChance the gacha chance to set
     */
    public void setGachaChance(int gachaChance) {
        this.gachaChance = gachaChance;
    }

    /**
     * Gets the gacha chance for the item.
     * @return the gacha chance
     */
    public int getGachaChance() {
        return gachaChance;
    }
}
