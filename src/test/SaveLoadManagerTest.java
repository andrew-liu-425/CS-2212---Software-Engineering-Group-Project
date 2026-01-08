import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DummySpriteManager extends SpriteManager {
    public String emotion = "";
    
    public DummySpriteManager() {
        super("", 0, 0, 0, 1); // Dummy values
    }

    @Override
    public void setEmotion(int emotionCode) {
        switch (emotionCode) {
            case SpriteManager.DEAD -> emotion = "DEAD";
            case SpriteManager.HAPPY -> emotion = "HAPPY";
            case SpriteManager.SLEEPING -> emotion = "SLEEPING";
            default -> emotion = "OTHER";
        }
    }
}

public class UIUpdateTest {

    @Test
    public void testEvolveWhenScoreHighEnough() {
        Inventory inv = new Inventory();
        Gacha gacha = new Gacha(inv);
        Pet pet = new Pet("Zippy", "Duck", inv, gacha);
        pet.incrementScore(1100); // Triggers evolution
        DummySpriteManager petSprite = new DummySpriteManager();
        DummySpriteManager[] stats = {new DummySpriteManager(), new DummySpriteManager(), new DummySpriteManager(), new DummySpriteManager()};

        UIUpdate.updateUI(pet, petSprite, stats, true);

        assertEquals(2, pet.getEvoLevel()); // Should have evolved from 1 to 2
        assertEquals("HAPPY", petSprite.emotion);
    }
}
