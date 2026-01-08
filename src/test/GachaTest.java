import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GachaTest {

    private Gacha gacha;
    private Inventory inventory;

    @BeforeEach
    public void setup() {
        inventory = new Inventory();
        gacha = new Gacha(inventory);
    }

    @Test
    public void testAddNewItem() {
        Item item = new Item("Pizza", "food", 30);
        gacha.addNewItem(item, 10);

        assertEquals(1, gacha.getGachaItems().size());
        assertEquals(10, gacha.getTotalGachaChance());
        assertEquals(10, gacha.getGachaChance("Pizza"));
    }

    @Test
    public void testDeleteItem() {
        Item item = new Item("Burger", "food", 25);
        gacha.addNewItem(item, 5);

        gacha.deleteItem("Burger");

        assertEquals(0, gacha.getGachaItems().size());
        assertEquals(0, gacha.getTotalGachaChance());
    }

    @Test
    public void testUpdateItemChance() {
        Item item = new Item("Toy Car", "gift", 15);
        gacha.addNewItem(item, 8);

        gacha.updateItemGachaChance("Toy Car", 20);

        assertEquals(20, gacha.getGachaChance("Toy Car"));
        assertEquals(20, gacha.getTotalGachaChance());
    }

    @Test
    public void testRollGachaReturnsItem() {
        Item item1 = new Item("Rubix Cube", "gift", 15);
        Item item2 = new Item("Burger", "food", 25);

        gacha.addNewItem(item1, 70);
        gacha.addNewItem(item2, 30);

        Item rolledItem = gacha.rollGacha();

        assertNotNull(rolledItem);
        assertTrue(inventory.getItemQuantity(rolledItem.getItemName()) > 0);
    }

    @Test
    public void testRollGachaHandlesEmptyGacha() {
        Item result = gacha.rollGacha();
        assertNull(result);
    }
}
