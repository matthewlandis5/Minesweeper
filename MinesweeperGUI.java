import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseButton;
import javafx.event.ActionEvent;
import javafx.stage.Stage;


/**
 * Minesweeper game. Now with interactive GUI.
 * @author Matthew Landis
 * @version 1.0
 */
public class MinesweeperGUI extends Application {

    private Button[][] buttons;
    private int[][] mapHidden;
    private int boardSize;
    private int numberOfBombs;
    private boolean firstMove;
    private Stage mainStage;

    @Override
    public void start(Stage primaryStage) {
        Scene scene = new Scene(getInputs(), 500, 200);

        primaryStage.setTitle("Java Minesweeper");
        primaryStage.setScene(scene);
        primaryStage.show();
        mainStage = primaryStage;
    }

    /**
     * Builds the surface for the GUI and calls methods to populate it.
     * @return BorderPane object with the completed surface
     */
    public BorderPane buildSurface() {
        firstMove = true;

        Text text = new Text(10, 10, "Minesweeper");
        text.setTranslateX(20);
        text.setFont(Font.font("Courier", 40));

        GridPane grid = buildGrid();
        populateGrid();

        BorderPane bPane = new BorderPane();
        bPane.setTop(text);
        bPane.setCenter(grid);
        return bPane;
    }

    /**
     * Makes the grid and fills with the mineable locations.
     * @return GridPane with the interactive buttons
     */
    public GridPane buildGrid() {
        GridPane gridpane = new GridPane();

        buttons = new Button[boardSize][boardSize];
        for (int i = 0; i < buttons.length; i++) {
            for (int j = 0; j < buttons[i].length; j++) {
                buttons[i][j] = new Button(" ");
                buttons[i][j].setMinHeight(28);
                buttons[i][j].setMinWidth(28);
                int x = i;
                int y = j;
                buttons[i][j].setOnMouseClicked(e -> {
                    if (e.getButton() == MouseButton.PRIMARY) {
                        selected(x, y);
                    } else {
                        antiselected(x, y);
                    }
                });
                gridpane.add(buttons[i][j], i, j);
            }
        }
        gridpane.setHgap(1.5);
        gridpane.setVgap(1.5);
        gridpane.setTranslateX(20);
        return gridpane;
    }

    /**
     * Places the bombs onto the hidden board.
     */
    public void populateGrid() {
        mapHidden = new int[boardSize][boardSize];
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                mapHidden[i][j] = 0;
            }
        }
        int x;
        int y;
        for (int i = 0; i < numberOfBombs; i++) {
            x = (int) (Math.random() * boardSize);
            y = (int) (Math.random() * boardSize);
            if (mapHidden[x][y] != 0) {
                i -= 1;
            } else {
                mapHidden[x][y] = 9;
            }
        }
        markNeighbors();
    }

    /**
     * Handles all actions that occur when a mineable grid position is clicked.
     * @param i x position
     * @param j y position
     */
    public void selected(int i, int j) {
        if (firstMove) {
            //this makes it so that you always start by selecting a zero

            //instead, go in and move the bomb if it is selected at a place with a bomb
            //make another populate grid method where you account for this
            if (mapHidden[i][j] != 0) {
                populateGrid();
                selected(i, j);
            } else {
                firstMove = false;
            }
            checkIfWon();
        }
        if (mapHidden[i][j] == 0) {
            zeroLoop(i, j);
            checkIfWon();
        } else if (mapHidden[i][j] == 9) {
            hitBomb();
        } else {
            buttons[i][j].setText("" + mapHidden[i][j]);
            buttons[i][j].setStyle("-fx-background-color: White");
            checkIfWon();
        }
    }

    /**
     * Implements the right click functionality. Places a W on the button as a warning.
     * @param i x position
     * @param j y position
     */
    public void antiselected(int i, int j) {
        if (buttons[i][j].getText().equals("W")) {
            buttons[i][j].setText(" ");
        } else {
            buttons[i][j].setText("W");
        }
    }

    /**
     * Loops through the positions besides a selected zero button.
     * This allows for all of them to be revealed when a bordered zero is clicked.
     * @param i x position
     * @param j y position
     */
    public void zeroLoop(int i, int j) {
        if (mapHidden[i][j] == 0) {
            buttons[i][j].setText("0");
            buttons[i][j].setStyle("-fx-background-color: White");
            try {
                if (buttons[i + 1][j].getText().equals(" ")) {
                    zeroLoop(i + 1, j);
                }
            } catch (Exception e) { }
            try {
                if (buttons[i][j + 1].getText().equals(" ")) {
                    zeroLoop(i, j + 1);
                }
            } catch (Exception e) { }
            try {
                if (buttons[i - 1][j].getText().equals(" ")) {
                    zeroLoop(i - 1, j);
                }
            } catch (Exception e) { }
            try {
                if (buttons[i][j - 1].getText().equals(" ")) {
                    zeroLoop(i, j - 1);
                }
            } catch (Exception e) { }
            try {
                if (buttons[i + 1][j + 1].getText().equals(" ")) {
                    zeroLoop(i + 1, j + 1);
                }
            } catch (Exception e) { }
            try {
                if (buttons[i + 1][j - 1].getText().equals(" ")) {
                    zeroLoop(i + 1, j - 1);
                }
            } catch (Exception e) { }
            try {
                if (buttons[i - 1][j + 1].getText().equals(" ")) {
                    zeroLoop(i - 1, j + 1);
                }
            } catch (Exception e) { }
            try {
                if (buttons[i - 1][j - 1].getText().equals(" ")) {
                    zeroLoop(i - 1, j - 1);
                }
            } catch (Exception e) { }
        } else if (mapHidden[i][j] != 9) {
            buttons[i][j].setText("" + mapHidden[i][j]);
            buttons[i][j].setStyle("-fx-background-color: White");
        }
    }

    /**
     * Builds the start menu and handles the selection of the size of field and number of bombs.
     * @return VBox holding the start menu GUI
     */
    public VBox getInputs() {
        Spinner spinSize =  new Spinner(4, 20, 9, 1);
        Spinner spinBombs =  new Spinner(2, 100, 9, 1);
        Button newGame = new Button("NEW GAME");
        newGame.setOnAction((ActionEvent e) -> {
            boardSize = (int) spinSize.getValue();
            numberOfBombs = (int) spinBombs.getValue();
            if (boardSize * boardSize > numberOfBombs + 8) {
                newGame();
            } else {
                Alert alert = new Alert(AlertType.WARNING);
                alert.setTitle("INVALID INPUTS");
                alert.setHeaderText("You selected too many bombs for the size of field.");
                alert.setContentText("Click \"OK\" to return to the start menu.");
                alert.showAndWait();
            }
        });
        HBox hboxInput =  new HBox();
        hboxInput.getChildren().addAll(spinSize, spinBombs, newGame);
        hboxInput.setTranslateY(30);
        hboxInput.setTranslateX(10);

        Text textSize = new Text(10, 10, "Size of Field");
        textSize.setTranslateX(10);
        Text textBombs = new Text(10, 10, "Number of Bombs");
        textBombs.setTranslateX(95);
        HBox hboxLabels =  new HBox();
        hboxLabels.getChildren().addAll(textSize, textBombs);
        hboxLabels.setTranslateY(30);

        Text textTitle = new Text(10, 10, "Minesweeper");
        textTitle.setFont(Font.font("Courier", 40));
        textTitle.setTranslateX(20);

        Text textDescription = new Text(10, 10, "Welcome to Minesweeper. "
                + "To begin, please enter the desired field size and bomb count.");
        textDescription.setFont(Font.font("Courier", 12));
        textDescription.setTranslateY(5);
        textDescription.setTranslateX(15);

        VBox vbox = new VBox();
        vbox.getChildren().addAll(textTitle, textDescription, hboxLabels, hboxInput);
        return vbox;
    }

    /**
     * Completes the field generation by marking the positions neighboring a bomb with the proper number.
     * This code is taken from a past version and is likely poorly optimized.
     */
    public void markNeighbors() {
        int temp;
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (mapHidden[i][j] == 9) {
                    if (i > 0 && j > 0) {
                        temp = mapHidden[i - 1][j - 1];
                        if (temp != 9) {
                            mapHidden[i - 1][j - 1] = temp + 1;
                        }
                    }
                    if (i > 0) {
                        temp = mapHidden[i - 1][j];
                        if (temp != 9) {
                            mapHidden[i - 1][j] = temp + 1;
                        }
                    }
                    if (j > 0) {
                        temp = mapHidden[i][j - 1];
                        if (temp != 9) {
                            mapHidden[i][j - 1] = temp + 1;
                        }
                    }
                    if (i < (boardSize - 1) && j > 0) {
                        temp = mapHidden[i + 1][j - 1];
                        if (temp != 9) {
                            mapHidden[i + 1][j - 1] = temp + 1;
                        }
                    }
                    if (i > 0 && j < (boardSize - 1)) {
                        temp = mapHidden[i - 1][j + 1];
                        if (temp != 9) {
                            mapHidden[i - 1][j + 1] = temp + 1;
                        }
                    }
                    if (i < (boardSize - 1) && j < (boardSize - 1)) {
                        temp = mapHidden[i + 1][j + 1];
                        if (temp != 9) {
                            mapHidden[i + 1][j + 1] = temp + 1;
                        }
                    }
                    if (i < (boardSize - 1)) {
                        temp = mapHidden[i + 1][j];
                        if (temp != 9) {
                            mapHidden[i + 1][j] = temp + 1;
                        }
                    }
                    if (j < (boardSize - 1)) {
                        temp = mapHidden[i][j + 1];
                        if (temp != 9) {
                            mapHidden[i][j + 1] = temp + 1;
                        }
                    }
                }
            }
        }
    }

    /**
     * Handles the loss scenario by alerting the user of loss and showing the complete map.
     */
    public void hitBomb() {
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (mapHidden[i][j] == 9) {
                    buttons[i][j].setText("B");
                    buttons[i][j].setStyle("-fx-background-color: Red");
                } else {
                    buttons[i][j].setText("" + mapHidden[i][j]);
                }
            }
        }
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle("GAME OVER");
        alert.setHeaderText("You stepped on a mine and died.");
        alert.setContentText("Click \"OK\" to return to the start menu.");
        alert.showAndWait();
        restart();
    }

    /**
     * Restarts the game by calling the input constructor and showing the user.
     */
    public void restart() {
        Scene scene = new Scene(getInputs(), 500, 200);

        mainStage.setScene(scene);
        mainStage.show();
    }

    /**
     * Starts the game by calling the map constuctor using the size and bomb count from the input.
     */
    public void newGame() {
        BorderPane surface = buildSurface();
        Scene scene = new Scene(surface, 29.5 * boardSize + 40, 29.5 * boardSize + 80);

        mainStage.setScene(scene);
        mainStage.show();
    }

    /**
     * Handles the win condition by checking if only the bombs are left hidden in the map.
     */
    public void checkIfWon() {
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (mapHidden[i][j] != 9) {
                    if (buttons[i][j].getText().equals(" ")) {
                        return;
                    }
                }
            }
        }
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("YOU WIN!");
        alert.setHeaderText("You cleared the field without injury.");
        alert.setContentText("Click \"OK\" to return to the start menu.");
        alert.showAndWait();

        Scene scene = new Scene(getInputs(), 500, 200);

        mainStage.setScene(scene);
        mainStage.show();
    }
}
