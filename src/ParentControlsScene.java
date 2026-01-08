import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import javafx.geometry.Insets;

/** 
 * Represents the parental controls screen where parents can:
 * - Set allowed play time ranges
 * - View playtime statistics
 * - Revive the virtual pet
 */
public class ParentControlsScene extends GameScene {
    /** The scene containing all UI components */
    private Scene scene;
    /** Background image for the scene */
    private ImageView backgroundImage;
    /** Screen manager for handling global variables. */
    private ScreenManager screenManager;
    /** Main title text */
    private Text title;
    /** Primary container for UI elements */
    private VBox mainContainer;
    /** Title for statistics section */
    private Text statTitle;
    /** Text displaying total playtime */
    private Text totalPlaytimetext;
    /** Text displaying average playtime */
    private Text averagePlaytimetext;
    /** Instruction text for time input */
    private Text timeOfDayMessage;
    /** Input field for start time */
    private TextField startTimeField;
    /** Input field for end time */
    private TextField endTimeField;
    /** Button to submit time range */
    private Button submitButton;
    /** Button to revive pet */
    private Button revivePetButton;

    /**
     * Constructs the Parental Controls scene
     * @param screenManager The manager for handling scene transitions
     */
    public ParentControlsScene(ScreenManager screenManager) throws IOException {
        this.screenManager = screenManager;

        // Root container setup
        StackPane root = new StackPane();
        root.setPrefSize(Main.WIDTH, Main.HEIGHT);

        // Background image configuration
        backgroundImage = new ImageView();
        ImageManager.loadImage(backgroundImage, "../assets/backgrounds/MainMenuBackground.png");
        ImageManager.scaleBackground(backgroundImage, Main.WIDTH, Main.HEIGHT);
        root.getChildren().add(backgroundImage);

        // Main container holds all UI elements vertically
        mainContainer = new VBox(30);
        mainContainer.setAlignment(Pos.CENTER);
        mainContainer.setPadding(new Insets(20));

        // Scene title with custom font
        title = new Text("PARENT CONTROLS");
        Font impactFont = TextManager.loadCustomFont("../assets/fonts/impact/impact.ttf", 90);
        title.setFont(impactFont != null ? impactFont : Font.font("Impact", 90));
        title.setStyle("-fx-fill: black;");
        TextManager.registerText(title, "title");

        // Statistics display section
        statTitle = new Text("Parental Statistics");
        Font impactFont2 = TextManager.loadCustomFont("../assets/fonts/impact/impact.ttf", 20);
        statTitle.setFont(impactFont2 != null ? impactFont2 : Font.font("Impact", 20));
        statTitle.setStyle("-fx-fill: black;");
        TextManager.registerText(statTitle, "title");

        // Playtime counter
        totalPlaytimetext = new Text("Total playtime: ");
        totalPlaytimetext.setFont(impactFont2 != null ? impactFont2 : Font.font("Impact", 20));
        totalPlaytimetext.setStyle("-fx-fill: black;");
        TextManager.registerText(totalPlaytimetext, "title");

        // Average playtime counter
        int averagePlayTime = screenManager.getTimeInformation().getAveragePlayTime();
        int minutes = averagePlayTime/60;
        int seconds = averagePlayTime%60;
        String time = "Average Playtime: " + String.format("%d:%02d", minutes, seconds);
        averagePlaytimetext = new Text(time);
        averagePlaytimetext.setFont(impactFont2 != null ? impactFont2 : Font.font("Impact", 20));
        averagePlaytimetext.setStyle("-fx-fill: black;");
        TextManager.registerText(averagePlaytimetext, "title");

        // Checkbox for enabling time range feature
        CheckBox checkbox = new CheckBox("Enable Time Control");
        checkbox.selectedProperty().bindBidirectional(screenManager.getTimeInformation().getCheckTimes());

        // Time range input section
        timeOfDayMessage = new Text("Please Enter Time Range (HH:MM)");
        timeOfDayMessage.setFont(Font.font("Arial", 20));
        TextManager.registerText(timeOfDayMessage, "timeMessage");

        // Time input fields
        startTimeField = new TextField();
        startTimeField.setPromptText("Start Time (HH:MM)");
        startTimeField.setMaxWidth(200);
        startTimeField.setFont(Font.font("Arial", 16));

        endTimeField = new TextField();
        endTimeField.setPromptText("End Time (HH:MM)");
        endTimeField.setMaxWidth(200);
        endTimeField.setFont(Font.font("Arial", 16));

        // Action buttons
        revivePetButton = createStyledButton("Revive Pet", "#ffffff", "#dcdcdc", false);
        submitButton = createStyledButton("Submit Time Range", "#4a8cff", "#1a5fb4", true);
        Button backButton = createStyledButton("Back to Menu", "#ffffff", "#dcdcdc", false);

        // Button action handlers
        submitButton.setOnAction(e -> handleTimeSubmission());
        revivePetButton.setOnAction(e -> revivePet());
        backButton.setOnAction(e -> {
            TextManager.clearTextElements();
            ButtonManager.clearButtonElements();
            timeOfDayMessage.setVisible(false);
            timeOfDayMessage.setText("Please Enter Time Range (HH:MM)");
            screenManager.switchTo("MainMenu");
        });

        // Organize time controls
        VBox timeRangeContainer = new VBox(10);
        timeRangeContainer.setAlignment(Pos.CENTER);
        timeRangeContainer.getChildren().addAll(
            timeOfDayMessage,
            startTimeField,
            endTimeField,
            submitButton
        );

        // Assemble main container
        mainContainer.getChildren().addAll(
            title,
            statTitle,
            totalPlaytimetext,
            averagePlaytimetext,
            revivePetButton,
            checkbox,
            timeRangeContainer,
            backButton
        );
        
        root.getChildren().add(mainContainer);
        scene = new Scene(root, Main.WIDTH, Main.HEIGHT);

        // Responsive scaling handlers
        scene.widthProperty().addListener((obs, oldVal, newVal) -> updateScaling());
        scene.heightProperty().addListener((obs, oldVal, newVal) -> updateScaling());
    }

    /**
     * Creates a consistently styled button
     * @param text Button label
     * @param topColor Top gradient color
     * @param bottomColor Bottom gradient color
     * @param isPrimary Whether this is a primary action button
     * @return Configured Button instance
     */
    private Button createStyledButton(String text, String topColor, String bottomColor, boolean isPrimary) {
        Button button = new Button(text);
        button.setFont(Font.font("Arial", isPrimary ? 20 : 24));
        button.setStyle(String.format(
            "-fx-background-color: linear-gradient(to bottom, %s, %s);" +
            "-fx-text-fill: %s;" +
            "-fx-padding: %s;" +
            "-fx-border-radius: 5;" +
            "-fx-background-radius: 5;" +
            "-fx-min-width: 250px;",
            topColor, bottomColor,
            isPrimary ? "white" : "black",
            isPrimary ? "10 30" : "15 40"
        ));
        ButtonManager.registerButton(button);
        return button;
    }

    /**
     * Handles validation and processing of time range submission
     */
    private void handleTimeSubmission() {
        String startTime = startTimeField.getText();
        String endTime = endTimeField.getText();
        
        if (validateTimeFormat(startTime) && validateTimeFormat(endTime)) {
            timeOfDayMessage.setText("Times successfully set!");
            timeOfDayMessage.setVisible(true);
            DateTimeFormatter format = DateTimeFormatter.ofPattern("HH:mm");
            screenManager.getTimeInformation().setStartTime(LocalTime.parse(startTime, format));
            screenManager.getTimeInformation().setEndTime(LocalTime.parse(endTime, format));
        } else {
            timeOfDayMessage.setText("Invalid time format. Please use HH:MM. Include the zero for single digit hours.");
            timeOfDayMessage.setVisible(true);
        }
    }

    /**
     * Validates time string format (HH:MM)
     * @param time Time string to validate
     * @return True if valid format, false otherwise
     */
    private boolean validateTimeFormat(String time) {
        if (time == null || time.length() != 5) return false;
        try {
            int hours = Integer.parseInt(time.substring(0, 2));
            int minutes = Integer.parseInt(time.substring(3));
            return hours >= 0 && hours < 24 && 
                   minutes >= 0 && minutes < 60 && 
                   time.charAt(2) == ':';
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Adjusts UI elements when window is resized
     */
    private void updateScaling() {
        if (scene == null) return;
        
        double width = scene.getWidth();
        double height = scene.getHeight();
        
        // Scale background to window
        backgroundImage.setFitWidth(width);
        backgroundImage.setFitHeight(height);

        // Scale title proportionally
        double scaleFactor = Math.min(width/Main.WIDTH, height/Main.HEIGHT);
        title.setFont(Font.font(title.getFont().getFamily(), 40 * scaleFactor));

        // Adjust input field widths
        startTimeField.setPrefWidth(width * 0.2);
        endTimeField.setPrefWidth(width * 0.2);
    }

    /**
     * Provides access to the scene
     * @return The configured Scene instance
     */
    public Scene getScene() {
        return scene;
    }

    /** Changes the current play time text to reflect how much time has elapsed.
     * @param text the string to change the text to.
     */
    @Override
    public void setTotalPlaytimetext(String text) {
        totalPlaytimetext.setText(text);
    }

    /** Revives all pets that are currently stored as save files. */
    private void revivePet() {

        // Finds all pet save files
        String folderPath = System.getProperty("user.dir");
        boolean hasRevived = false;

        try (var stream = Files.list(Paths.get(folderPath))) {
            int count = 0;
            for (Path path : stream.toArray(Path[]::new)) {
                String saveFile = path.toString();
                if (saveFile.endsWith(".ser") && !saveFile.endsWith("TimeInformation.ser") && count < 3) {
                    Pet pet = SaveLoadManager.loadGame(saveFile).getPet();
                    if (pet.isDead()) {
                        pet.incrementHealth(1000);
                        pet.incrementEnergy(1000);
                        pet.incrementHappiness(1000);
                        pet.incrementFullness(1000);
                        revivePetButton.setText("Pet revived!");
                        hasRevived = true;
                        GameState gameState = new GameState(pet);
                        SaveLoadManager.saveGame(pet.getName(), gameState);
                    }
                    count++;
                }
                if (count >= 3) {
                    if (!hasRevived) {
                        revivePetButton.setText("No pets to revive.");
                    }
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}