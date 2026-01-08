import java.io.Serializable;

/** A custom data structure storing information that should be kept between save files. */
public class GameState implements Serializable {
    
    private static final long serialVersionUID = 1L;

    /** The pet that should be restored. */
    private Pet pet;

    /** 
     * Constructs a new save data structure that will persist through resets.
     * <p>
     * This should be used in the setting scene class, when the user chooses to save.
     * It will be retrieved in the load game class.
     * @param pet The pet that should be restored.
     *
     */
    public GameState(Pet pet) {

        this.pet = pet;

    }

    /** 
     * Returns the pet that should be restored.
     * @return The pet that should be restored.
     */
    public Pet getPet() {
        return pet;
    }

}

