import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class InventoryTest {

    private Inventory inventory;

    @BeforeEach
    public void setup() {
        inventory = new Inventory();
    }

    @Test
    public void testAddNewItem() {
        Item item = new Item("Burger", "food", 25);
        inventory.addNewItem(item);
        assertTrue(inventory.hasItem("Burger"));
    }

    @Test
    public void testAddNewItemDuplicateThrowsException() {
        Item item1 = new Item("Burger", "food", 25);
        Item item2 = new Item("Burger", "food", 30);
        inventory.addNewItem(item1);
        assertThrows(IllegalArgumentException.class, () -> inventory.addNewItem(item2));
    }

    @Test
    public void testDeleteItem() {
        Item item = new Item("Burger", "food", 25);
        inventory.addNewItem(item);
        inventory.deleteItem("Burger");
        assertFalse(inventory.hasItem("Burger"));
    }

    @Test
    public void testDeleteItemNotFound() {
        assertThrows(IllegalArgumentException.class, () -> inventory.deleteItem("Unknown"));
    }

    @Test
    public void testUpdateItemQuantity() {
        Item item = new Item("Burger", "food", 25);
        inventory.addNewItem(item);
        inventory.updateItemQuantity("Burger", 5);
        assertEquals(5, inventory.getItemQuantity("Burger"));
    }

    @Test
    public void testUseItemDecreasesQuantity() {
        Item item = new Item("Burger", "food", 25);
        inventory.addNewItem(item);
        inventory.updateItemQuantity("Burger", 3);
        Item usedItem = inventory.useItem("Burger");
        assertNotNull(usedItem);
        assertEquals(2, inventory.getItemQuantity("Burger"));
    }

    @Test
    public void testUseItemWithZeroQuantityReturnsNull() {
        Item item = new Item("Burger", "food", 25);
        inventory.addNewItem(item);
        assertNull(inventory.useItem("Burger"));
    }

    @Test
    public void testUseItemNotFoundThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> inventory.useItem("NonExistent"));
    }

    @Test
    public void testGetItemType() {
        Item item = new Item("Cake", "gift", 15);
        inventory.addNewItem(item);
        assertEquals("gift", inventory.getItemType("Cake"));
    }

    @Test
    public void testGetItemScoreIncrease() {
        Item item = new Item("Burger", "food", 40);
        inventory.addNewItem(item);
        assertEquals(40, inventory.getItemScoreIncrease("Burger"));
    }
}
