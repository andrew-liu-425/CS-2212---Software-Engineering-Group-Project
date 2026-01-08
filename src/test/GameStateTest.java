import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GameStateTest {

    @Test
    public void testGameStateStoresCorrectData() {
        Inventory inventory = new Inventory();
        Gacha gacha = new Gacha(inventory);
        Pet pet = new Pet("Fluffy", "Cat", inventory, gacha);
        ParentControls parent = new ParentControls("5678", false);
        String path = "sprites/cat.png";

        GameState state = new GameState(pet, parent, path);

        assertEquals(pet, state.getPet());
        assertEquals(parent, state.getParent());
        assertEquals(path, state.getPetSpriteFilePath());
    }
}
