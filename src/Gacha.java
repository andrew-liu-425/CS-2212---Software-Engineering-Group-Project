import java.io.Serializable;
import java.util.ArrayList;

/**
 * A gacha is a system that randomly gives items to the player.
 * This system is used to randomly give items to the player.
 */
public class Gacha implements Serializable {

    private static final long serialVersionUID = 1L; 
    
    /** The list of all possible items in the gacha.*/
    private ArrayList<Item> gachaItems;
    /** The sum of all gachaChance values of all items. */
    private int totalGachaChance;
    /** The inventory to insert the item into. */
    private Inventory inventory;

    /**
     * Creates a new gacha system. A gacha is a system that randomly gives items to the player.
     * <p>
     * To add items to this gacha, insert an item object into the gacha using the addItem method.
     * This will create the item with the initial gacha chance of 0.
     * <p>
     * To roll the gacha, use the rollGacha method. This method will return a random item from the gacha.
     * The item will be inserted into the inventory specified in the rollGacha method.
     * 
     * @param inventory the inventory to insert the item into
     */
    public Gacha(Inventory inventory) {
        gachaItems = new ArrayList<Item>();
        totalGachaChance = 0;
        this.inventory = inventory;
    }

    /**
     * Adds an item to the gacha with a specified gacha chance.
     * If an item with the same name already exists, it will replace the original item.
     * @param i the item to add
     * @param gachaChance the gacha chance of the item
     */
    public void addNewItem(Item i, int gachaChance) {
        for (int index = 0; index < gachaItems.size(); index++) {
            if (gachaItems.get(index).getItemName().equalsIgnoreCase(i.getItemName())) {
                totalGachaChance -= gachaItems.get(index).getGachaChance();
                gachaItems.set(index, i);
                i.setGachaChance(gachaChance);
                totalGachaChance += gachaChance;
                return;
            }
        }
        i.setGachaChance(gachaChance);
        gachaItems.add(i);
        totalGachaChance += gachaChance;
    }

    /**
     * Deletes an item from the gacha by its name.
     * @param itemName the name of the item to delete
     * @throws IllegalArgumentException if the item does not exist in the gacha
     */
    public void deleteItem(String itemName) {
        for (Item i : gachaItems) {
            if (i.getItemName().equalsIgnoreCase(itemName)) {
                gachaItems.remove(i);
                totalGachaChance -= i.getGachaChance();
                return;
            }
        }
        throw new IllegalArgumentException("Item does not exist in the gacha.");
    }

    /**
     * Updates the gacha chance of an item by its name.
     * @param itemName the name of the item to update
     * @param gachaChance the new gacha chance of the item
     * @throws IllegalArgumentException if the item does not exist in the gacha
     */
    public void updateItemGachaChance(String itemName, int gachaChance) {
        for (Item i : gachaItems) {
            if (i.getItemName().equalsIgnoreCase(itemName)) {
                totalGachaChance -= i.getGachaChance();
                i.setGachaChance(gachaChance);
                totalGachaChance += i.getGachaChance();
                return;
            }
        }
        throw new IllegalArgumentException("Item does not exist in the gacha.");
    }

    /**
     * Rolls the gacha and returns a random item.
     * The item is selected based on the gacha chances of all items.
     * The item is inserted into the inventory.
     * <p>
     * You should probably use this method with a condition (e.g. if coin > 0)
     * <p>
     * @return the item object obtained from the gacha roll
     */
    public Item rollGacha() {
        int random = (int) (Math.random() * totalGachaChance);
        int sum = 0;
        for (Item i : gachaItems) {
            sum += i.getGachaChance();
            if (random < sum) {
                inventory.updateItemQuantity(i.getItemName(), 1);;
                return i;
            }
        }
        return null;
    }

    /**
     * Gets the list of all items in the gacha.
     * @return the list of all items in the gacha
     */
    public ArrayList<Item> getGachaItems() {
        return gachaItems;
    }

    /**
     * Finds an item by its name and returns its gacha chance.
     * @param itemName the name of the item to search for
     * @return the gacha chance of the item if found, or -1 if the item is not found
     */
    public int getGachaChance(String itemName) {
        for (Item i : gachaItems) {
            if (i.getItemName().equalsIgnoreCase(itemName)) {
                return i.getGachaChance();
            }
        }
        return -1;
    }

    /**
     * Gets the total gacha chance of all items.
     * @return the total gacha chance of all items
     */
    public int getTotalGachaChance() {
        return totalGachaChance;
    }

}
