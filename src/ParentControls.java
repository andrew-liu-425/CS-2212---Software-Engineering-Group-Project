import java.util.List;
import java.util.ArrayList;
import java.io.Serializable;
import java.time.LocalTime;

/**
 * The Parent class provides parental control features such as time restrictions,
 * playtime limits, and pet revival. It includes password protection to prevent
 * unauthorized access.
 */
public class ParentControls implements Serializable {
    
    /**
     * Indicates whether parental controls are enabled. 
     */
    private boolean enabled;
    
    /**
     * A list of allowed playtime hours.
     */
    private List<LocalTime> allowedHours;
    
    /**
     * The maximum playtime allowed in minutes.
     */
    private int maxPlaytimeMinutes;
    
    /**
     * The password required to access parental controls.
     */
    private String password;
    
    /**
     * Constructs a Parent object with a specified password.
     * @param password The password required to access parental controls.
     */
    public ParentControls(String password) {
        this.enabled = false;
        this.allowedHours = new ArrayList<>();
        this.maxPlaytimeMinutes = 0;
        this.password = password;
    }
    
    /**
     * Checks if parental controls are enabled.
     * @return true if enabled, false otherwise.
     */
    public boolean isEnabled() {
        return enabled;
    }
    
    /**
     * Enables or disables parental controls.
     * @param enabled true to enable, false to disable.
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    
    /**
     * Resets the allowed playtime and clears any set restrictions.
     */
    public void resetTime() {
        this.allowedHours.clear();
        this.maxPlaytimeMinutes = 0;
    }
    
    /**
     * Sets restrictions by defining allowed hours of gameplay.
     * @param allowedHours A list of allowed playtime hours.
     */
    public void setRestrictions(List<LocalTime> allowedHours) {
        this.allowedHours = new ArrayList<>(allowedHours);
    }

    /**
     * Revives the pet, restoring its stats to the maximum value.
     * @param pet the pet
     * @return if pet is revived
     */
    public boolean revivePet(Pet pet) {
        if(pet.isDead()){
            pet.incrementHealth(1000);
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * Sets the allowed playtime hours.
     * @param hours A list of LocalTime objects representing allowed play hours.
     */
    public void setAllowedHours(List<LocalTime> hours) {
        this.allowedHours = new ArrayList<>(hours);
    }
    
    /**
     * Sets the maximum playtime allowed in minutes.
     * @param minutes The maximum number of minutes allowed for play.
     */
    public void setMaxPlaytime(int minutes) {
        this.maxPlaytimeMinutes = minutes;
    }
    
    /**
     * Gets the maximum playtime allowed.
     * @return The maximum allowed playtime in minutes.
     */
    public int getMaxPlaytime() {
        return maxPlaytimeMinutes;
    }
    
    /**
     * Retrieves the allowed play hours.
     * @return A list of allowed play hours.
     */
    public List<LocalTime> getAllowedHours() {
        return new ArrayList<>(allowedHours);
    }
    
    /**
     * Verifies if the given password matches the stored parental control password.
     * @param inputPassword The password entered by the user.
     * @return true if the password matches, false otherwise.
     */
    public boolean verifyPassword(String inputPassword) {
        return this.password.equals(inputPassword);
    }
}

