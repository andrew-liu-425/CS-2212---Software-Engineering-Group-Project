import java.io.Serializable;
import java.util.ArrayList;

/**
 * An inventory is a list of items that the player has.
 * To add items, use the addItem method, and insert an item object.
 * Items remain in the inventory until they are deleted, even if their quantity is zero.
 */
public class Inventory implements Serializable {

    private static final long serialVersionUID = 1L;

    /** A list that stores all items inside this inventory, including ones that have no quantity. */ 
    private ArrayList<Item> items;

    /**
     * Creates a new inventory. An inventory is a list of items that the player has. 
     * <p>
     * To use this inventory, create a unique item object and insert it into the inventory using the addItem method.
     * This will create the item with the initial quantity of 0. Change the quantity of the item using the updateItemQuantity method.
     * <p>
     * You can use the inventory methods to change the various values of the item.
     * Items remain in the inventory until they are deleted, even if their quantity is zero.
     * <p>
     * To use an item, use the useItem method. This method will decrease the quantity of the item by 1.
     */
    public Inventory() {
        items = new ArrayList<Item>();
    }

    /**
     * Adds an item to the inventory. If an item with the same name already exists, an exception is thrown.
     * @param i the item to add
     * @throws IllegalArgumentException if an item with the same name already exists in the inventory
     */
    public void addNewItem(Item i) {
        for (Item existingItem : items) {
            if (existingItem.getItemName().equalsIgnoreCase(i.getItemName())) {
                throw new IllegalArgumentException("Item with the same name already exists in the inventory.");
            }
        }
        items.add(i);
    }

    /**
     * Completely deletes an item from the inventory by its name.
     * @param itemName the name of the item to remove
     * @throws IllegalArgumentException if the item does not exist in the inventory
     */
    public void deleteItem(String itemName) {
        for (Item i : items) {
            if (i.getItemName().equalsIgnoreCase(itemName)) {
                items.remove(i);
                return;
            }
        }
        throw new IllegalArgumentException("Item does not exist in the inventory.");
    }

    /**
     * Retrieves all items in the inventory.
     * @return a list of all items in the inventory
     */
    public ArrayList<Item> getItems() {
        return items;
    }

    /**
     * Finds an item by its name and returns its type (food or gift).
     * @param itemName the name of the item to search for
     * @return the type of the item if found, or null if the item is not found
     */
    public String getItemType(String itemName) {
        for (Item i : items) {
            if (i.getItemName().equalsIgnoreCase(itemName)) {
                return i.getItemType();
            }
        }
        return null; // Return null if the item is not found
    }

    /**
     * Finds an item by its name and returns its quantity.
     * @param itemName the name of the item to search for
     * @return the quantity of the item if found, or -1 if the item is not found
     */
    public int getItemQuantity(String itemName) {
        for (Item i : items) {
            if (i.getItemName().equalsIgnoreCase(itemName)) {
                return i.getQuantity();
            }
        }
        return -1; // Return -1 if the item is not found
    }

    /**
     * Updates the quantity of an item by adding the specified value.
     * @param itemName the name of the item to update
     * @param value the value to add to the current quantity. Can be positive or negative.
     */
    public void updateItemQuantity(String itemName, int value) {
        for (Item i : items) {
            if (i.getItemName().equalsIgnoreCase(itemName)) {
                i.updateQuantity(value);
            }
        }
    }

    /**
     * Finds an item by its name and returns its score increase.
     * @param itemName the name of the item to search for
     * @return the score increase of the item if found, or -1 if the item is not found
     */
    public int getItemScoreIncrease(String itemName) {
        for (Item i : items) {
            if (i.getItemName().equalsIgnoreCase(itemName)) {
                return i.getScoreIncrease();
            }
        }
        return -1; // Return -1 if the item is not found

    }        

    /**
     * Checks if the inventory has an item.
     * @param itemName the name of the item to search for
     * @return true if the inventory has the item, false otherwise
     */
    public boolean hasItem(String itemName) {
        for (Item i : items) {
            if (i.getItemName().equalsIgnoreCase(itemName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Uses an item from the inventory by decreasing its quantity by 1.
     * @param itemName the name of the item to use
     * @return the item if its quantity is greater than 0, or null if the item's quantity is 0
     * @throws IllegalArgumentException if the item does not exist in the inventory
     */
    public Item useItem(String itemName) {
        for (Item i : items) {
            if (i.getItemName().equalsIgnoreCase(itemName)) {
                if (i.getQuantity() > 0) {
                    i.updateQuantity(-1);
                    return i;
                }
                return null; // Return null if the item's quantity is 0
            }
        }
        throw new IllegalArgumentException("Item does not exist in the inventory.");
    }

}
