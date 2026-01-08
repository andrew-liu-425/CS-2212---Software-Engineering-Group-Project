import java.io.*;


/**
 * The SaveLoadManager class handles saving and loading the game state
 * to and from a file using Java serialization.
 */
public class SaveLoadManager {

    /**
     * Saves the game state to a file.
     * 
     * @param fileName  The name of the file to save the game state to.
     * @param gameState The GameState object representing the current game state.
     */
    public static void saveGame(String fileName, GameState gameState) {
        fileName = fileName + ".ser";
        try (FileOutputStream fileOut = new FileOutputStream(fileName);
             ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
            out.writeObject(gameState);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveTime(TimeInformation time) {
        String fileName = "TimeInformation.ser";
        try (FileOutputStream fileOut = new FileOutputStream(fileName);
             ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
            out.writeObject(time);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads the game state from a file.
     * 
     * @param fileName The name of the file to load the game state from.
     * @return The loaded GameState object, or null if an error occurs.
     */
    public static GameState loadGame(String fileName) {
        try (FileInputStream fileIn = new FileInputStream(fileName);
            ObjectInputStream in = new ObjectInputStream(fileIn)) {
            return (GameState) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static TimeInformation loadTime() {
        try (FileInputStream fileIn = new FileInputStream("TimeInformation.ser");
            ObjectInputStream in = new ObjectInputStream(fileIn)) {
            return (TimeInformation) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return null;
        }
    }

}
