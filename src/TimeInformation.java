import java.io.Serializable;
import java.time.LocalTime;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

/** A custom class that stores time information for parental controls, to be kept between launches. */
public class TimeInformation implements Serializable{

    private static final long serialVersionUID = 1L;
    
    /** The total playtime elapsed for all sessions. */
    private int totalPlayTime;
    /** The average playtime per session. */
    private int averagePlayTime;
    /** The number of sessions. */
    private int numOfPlays;
    /** The start time for parental controls. */
    private LocalTime startTime;
    /** The end time for parental controls. */
    private LocalTime endTime;
    /** Whether the time control for parental controls is active. */
    private transient BooleanProperty checkTimes;
    /** Whether the time control for parental controls is active (Serializable version). */
    private boolean checkTimesS;

    /** Creates a new time information object, only called once per computer. */
    public TimeInformation() {
        totalPlayTime = 0;
        averagePlayTime = 0;
        numOfPlays = 0;
        startTime = LocalTime.of(0, 0);
        endTime = LocalTime.of(0, 0);
        checkTimes = new SimpleBooleanProperty(false);
    }

    /** Sets the total playtime. */
    public void setTotalPlayTime(int totalPlayTime) {
        this.totalPlayTime = totalPlayTime;
    }

    /** Updates the average playtime. 
     * @param newTime the time for the most recent session.
    */
    public void updateAveragePlayTime(int newTime) {
        this.numOfPlays++;
        averagePlayTime = (averagePlayTime*(numOfPlays-1) + newTime)/numOfPlays;
    }

    /** Increments the number of session. */
    public void incrementNumPlays() {
        this.numOfPlays++;
    }

    /** Sets the start time for parental controls. */
    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    /** Sets the end time for parental controls. */
    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    /** Creates a new check time variable. */
    public void createCheckTimes() {
        checkTimes = new SimpleBooleanProperty(checkTimesS);
    }

    /** Matches the two check time values for serializability. */
    public void setCheckTimes() {
        checkTimes.set(checkTimesS);
    }

    /** Matches the two check time values for serializability. */
    public void setCheckTimesS() {
        checkTimesS = checkTimes.get();
    }

    /** Returns the total play time. */
    public int getTotalPlayTime() {
        return totalPlayTime;
    }

    /** Returns the average play time. */
    public int getAveragePlayTime() {
        return averagePlayTime;
    }

    /** Returns the number of plays. */
    public int getNumOfPlays() {
        return numOfPlays;
    }

    /** Returns the start time for parental controls. */
    public LocalTime getStartTime() {
        return startTime;
    }

    /** Returns the end time for parental controls. */
    public LocalTime getEndTime() {
        return endTime;
    }

    /** Returns if the time control for parental controls is active. */
    public BooleanProperty getCheckTimes() {
        return checkTimes;
    }

}
