package test;
import org.junit.jupiter.api.Test;

import Gacha;
import Inventory;
import Pet;

import static org.junit.jupiter.api.Assertions.*;

public class PetTest {

    @Test
    public void testCoinIncrementAndSpend() {
        Inventory inventory = new Inventory();
        GachaTest gacha = new GachaTest();
        Pet pet = new Pet("Buddy", "Dog", inventory, gacha);

        pet.incrementCoins(10);
        assertEquals(10, pet.getCoins(), "Coins should be 10 after increment");

        boolean success = pet.spendCoins(6);
        assertTrue(success, "Should be able to spend 6 coins");
        assertEquals(4, pet.getCoins(), "Coins should now be 4");

        boolean fail = pet.spendCoins(10);
        assertFalse(fail, "Should not be able to spend 10 coins when only 4 are left");
    }

    @Test
    public void testFullnessIncrease() {
        Pet pet = new Pet("Buddy", "Dog", new Inventory(), new GachaTest());

        int original = pet.getFullness();
        pet.incrementFullness(10);
        assertEquals(Math.min(100, original + 10), pet.getFullness(), "Fullness should increase correctly");
    }

    @Test
    public void testPetAngryLogic() {
        Pet pet = new Pet("Buddy", "Dog", new Inventory(), new GachaTest());

        pet.incrementHappiness(-pet.getHappiness());
        assertTrue(pet.isAngry(), "Pet should be angry at 0 happiness");

        pet.incrementHappiness(60);
        assertTrue(pet.isNotLongerAngry(), "Pet should no longer be angry at happiness >= 50");
    }
}
