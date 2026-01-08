/** Represents a utility class that checks for stat updates for the pet and stat bars, and calls the necessary sprite managers. */
public class UIUpdate {

    /** Constructor that doesn't do anything.
     * @throws NullPointerException if this constructor is called.
     */
    public UIUpdate() {
        throw new NullPointerException("DON'T CREATE A UIUPDATE OBJECT!!!");
    }
    
    /**
     * Updates the UI based on the current stats of the pet.
     * <p>
     * Additionally, checks to see if the evolution requirements have been met.
     * @param pet the pet object
     * @param petSpriteManager the sprite manager that manages the pet sprites
     * @param statSpriteManagers the sprite manages that manages the stat bar sprites
     * @param happy whether the pet just received something that increases happiness
     */
    public static void updateUI(Pet pet, SpriteManager petSpriteManager, SpriteManager[] statSpriteManagers, boolean happy) {

        if (pet.getScore() >= 500 && pet.getEvoLevel() == 1) {
            pet.evolve();
        }
        else if (pet.getScore() >= 1000 && pet.getEvoLevel() == 2) {
            pet.evolve();
        }
        if (pet.isDead()) {
            petSpriteManager.setEmotion(SpriteManager.DEAD);
        }
        else if (pet.isStillSleeping()) {
            petSpriteManager.setEmotion(SpriteManager.SLEEPING);
        }
        else if (pet.isSick()) {
            petSpriteManager.setEmotion(SpriteManager.SICK);
        }
        else if (pet.isStillAngry()) {
            petSpriteManager.setEmotion(SpriteManager.ANGRY);
        }
        else if (pet.isHungry()) {
            petSpriteManager.setEmotion(SpriteManager.HUNGRY);
        }
        else if (pet.isSleepy()) {
            petSpriteManager.setEmotion(SpriteManager.SLEEPY);
        }
        else if (happy) {
            petSpriteManager.setEmotion(SpriteManager.HAPPY);
        }
        else if (pet.isNormal()) {
            petSpriteManager.setEmotion(SpriteManager.NORMAL);
        }

        statSpriteManagers[0].setStats(pet.getHealth(), pet.getMaxHealth());
        statSpriteManagers[1].setStats(pet.getFullness(), pet.getMaxFullness());
        statSpriteManagers[2].setStats(pet.getHappiness(), pet.getMaxHappiness());
        statSpriteManagers[3].setStats(pet.getEnergy(), pet.getMaxEnergy());

    }

}
