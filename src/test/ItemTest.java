import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ItemTest {

    @Test
    public void testItemConstructorValid() {
        Item item = new Item("Burger", "food", 25);
        assertEquals("Burger", item.getItemName());
        assertEquals("food", item.getItemType());
        assertEquals(25, item.getScoreIncrease());
        assertEquals(0, item.getQuantity());
        assertEquals(0, item.getGachaChance());
    }

    @Test
    public void testItemConstructorInvalidScoreLow() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Item("Burger", "food", -5);
        });
        assertTrue(exception.getMessage().contains("Score increase must be between 0 and 100"));
    }

    @Test
    public void testItemConstructorInvalidScoreHigh() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Item("Burger", "food", 120);
        });
        assertTrue(exception.getMessage().contains("Score increase must be between 0 and 100"));
    }

    @Test
    public void testItemConstructorInvalidType() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Item("Burger", "drink", 25);
        });
        assertTrue(exception.getMessage().contains("Type must be 'food' or 'gift'"));
    }

    @Test
    public void testUpdateQuantityPositiveAndNegative() {
        Item item = new Item("Burger", "food", 25);
        item.updateQuantity(5);
        assertEquals(5, item.getQuantity());
        item.updateQuantity(-3);
        assertEquals(2, item.getQuantity());
    }

    @Test
    public void testUpdateQuantityNegativeClampToZero() {
        Item item = new Item("Burger", "food", 25);
        item.updateQuantity(-1);
        assertEquals(0, item.getQuantity());
    }

    @Test
    public void testGachaChanceSetGet() {
        Item item = new Item("Gift", "gift", 15);
        item.setGachaChance(40);
        assertEquals(40, item.getGachaChance());
    }

    @Test
    public void testHasItemTrue() {
        Item item = new Item("Burger", "food", 25);
        item.updateQuantity(2);
        assertTrue(item.hasItem("Burger"));
    }

    @Test
    public void testHasItemFalse() {
        Item item = new Item("Burger", "food", 25);
        assertFalse(item.hasItem("Burger"));
    }
}

