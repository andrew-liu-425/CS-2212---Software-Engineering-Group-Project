import javafx.animation.*;
import javafx.util.Duration;

/** Represents timers that will continuously run every few seconds.
 * <p>
 * Useful for background tasks that automatically update the UI and gamestate.
 */
public class GameTimer {
    
    /** The timeline object. */
    private Timeline timeline;
    /** The runnable object. */
    private Runnable onTick;
    /** The frequency of the timer ticks. */
    private double intervalSeconds;

    /** Constructs a new game timer.
     * <p>
     * The timer will run every intervalSeconds seconds.
     * Used for background tasks that happen automatically.
     * @param intervalSeconds The interval in seconds between ticks.
     * @param onTick The runnable to run on each tick.
     */
    public GameTimer(double intervalSeconds, Runnable onTick) {
        this.intervalSeconds = intervalSeconds;
        this.onTick = onTick;
        createTimeline();
    }

    /** A helper method for the constructor.
     * <p>
     * Creates a new timeline object and sets the runnable to run on each tick.
     */
    private void createTimeline() {
        timeline = new Timeline(
            new KeyFrame(Duration.seconds(intervalSeconds), 
            e -> onTick.run())
        );
        timeline.setCycleCount(Animation.INDEFINITE);
    }

    /** Starts the timeline. */
    public void start() { 
        timeline.play(); 
    }

    /** Pauses the timeline. */
    public void pause() {
        timeline.pause(); 
    }

    /** Stops the timeline. */
    public void stop() { 
        timeline.stop(); 
    }
    
    /** Sets the frequency of each timer tick.
     * 
     * @param seconds The interval in seconds between ticks.
     */
    public void setInterval(double seconds) {
        intervalSeconds = seconds;
        createTimeline();
    }

}