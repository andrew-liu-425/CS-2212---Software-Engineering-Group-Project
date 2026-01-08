import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputControl;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/**
 * Keyboard class to handle keyboard navigation and button firing in JavaFX applications.
 * Created by DeepSeek v3 (not created by us).
 */
public class Keyboard {
    
    /** 
     * Attaches buttons to keyboard events for navigation and firing.
     * @param container The parent container (VBox or GridPane) containing the buttons and text fields.
     */
    public static void attach(Pane container) {
        // Enable focus for buttons/text fields
        container.getChildren().forEach(node -> {
            if (node instanceof Button || node instanceof TextInputControl) {
                node.setFocusTraversable(true);
            }
        });
    
        // Handle key events
        container.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            Node focusedNode = container.getScene().getFocusOwner();
                
            if (!container.getChildren().contains(focusedNode)) return;
    
            switch (event.getCode()) {
                case DOWN:
                    moveFocus(container, focusedNode, 0, 1);
                    event.consume();
                    break;
                case UP:
                    moveFocus(container, focusedNode, 0, -1);
                    event.consume();
                    break;
                case RIGHT:
                    if (!(focusedNode instanceof TextInputControl)) {
                        moveFocus(container, focusedNode, 1, 0);
                        event.consume();
                    }
                    break;
                case LEFT:
                    if (!(focusedNode instanceof TextInputControl)) {
                        moveFocus(container, focusedNode, -1, 0);
                        event.consume();
                    }
                    break;
                case SPACE:
                case ENTER:
                    if (focusedNode instanceof Button) {
                        ((Button) focusedNode).fire(); // Click the button
                        event.consume();
                    }
                    break;
                default:
                    break;
            }
        });
    }

    private static void moveFocus(Pane container, Node currentNode, int colDelta, int rowDelta) {
        if (container instanceof VBox) {
            moveFocusVBox((VBox) container, currentNode, rowDelta);
        } else if (container instanceof GridPane) {
            moveFocusGridPane((GridPane) container, currentNode, colDelta, rowDelta);
        }
    }

    private static void moveFocusVBox(VBox vbox, Node currentNode, int delta) {
        int currentIndex = vbox.getChildren().indexOf(currentNode);
        int nextIndex = currentIndex + delta;
        
        if (nextIndex >= 0 && nextIndex < vbox.getChildren().size()) {
            Node nextNode = vbox.getChildren().get(nextIndex);
            if (nextNode.isFocusTraversable()) {
                nextNode.requestFocus();
            }
        }
    }

    private static void moveFocusGridPane(GridPane grid, Node currentNode, int colDelta, int rowDelta) {
        // Use final variables for lambda compatibility
        final int currentRow = GridPane.getRowIndex(currentNode) == null ? 0 : GridPane.getRowIndex(currentNode);
        final int currentCol = GridPane.getColumnIndex(currentNode) == null ? 0 : GridPane.getColumnIndex(currentNode);
    
        grid.getChildren().stream()
            .filter(node -> {
                Integer row = GridPane.getRowIndex(node);
                Integer col = GridPane.getColumnIndex(node);
                return (row == null ? 0 : row) == currentRow + rowDelta && 
                       (col == null ? 0 : col) == currentCol + colDelta && 
                       node.isFocusTraversable();
            })
            .findFirst()
            .ifPresent(Node::requestFocus);
    }

    public static void bind(Button button, KeyCode key) {
        button.setOnKeyPressed(e -> {
            if (e.getCode() == key) {
                button.fire();
            }
        });
    }
}