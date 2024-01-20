package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class InfoCenter {
    private StackPane pane;
    private Label message;
    private Button startGame;
    private Button history;

    public InfoCenter() {
        pane = new StackPane();
        pane.setMinSize(UIConst.APP_WIDTH, UIConst.APP_HEIGHT);
        pane.setTranslateX(UIConst.APP_WIDTH / 2);
        pane.setTranslateY(UIConst.INFO_CENTER_HEIGHT / 2);

        message = new Label("Tic Tac Toe Game");
        message.setMinSize(UIConst.APP_WIDTH, UIConst.APP_HEIGHT);
        message.setFont(Font.font(24));
        message.setAlignment(Pos.CENTER);
        message.setTranslateY(-60);

        startGame = new Button("Start New Game");
        startGame.setId("previousGameButton");
        startGame.setMinSize(135, 40);
        startGame.setTranslateY(20);

        history = new Button("Previous Games");
        history.setId("previousGameButton");
        history.setOnAction(event -> loadPreviousGame());
        history.setTranslateY(30);
        
       

        VBox buttonContainer = new VBox(10, startGame, history); // Adjust spacing as needed
        buttonContainer.setAlignment(Pos.CENTER);

        pane.getChildren().addAll(message, buttonContainer);
        
        pane.setId("infoCenter");

    }
    public void setPreviousGameBtnAction(EventHandler<ActionEvent> onAction) {
        this.previousGameBtnAction = onAction;
    }
    private EventHandler<ActionEvent> previousGameBtnAction;
    private void loadPreviousGame() {
        try {
            // Replace these values with your actual database information
            String jdbcUrl = MainController.getDatabaseUrl();
            String username = "sa";
            String password = "pass";

            // Establish a database connection
            try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {
                // Query to retrieve game information from the "games" table
                String query = "SELECT RESULT FROM GAMES";

                try (PreparedStatement preparedStatement = connection.prepareStatement(query);
                     ResultSet resultSet = preparedStatement.executeQuery()) {

                    // Create a StringBuilder to store game results
                    StringBuilder gameResults = new StringBuilder();

                    // Iterate through the result set and append the game results to the StringBuilder
                    int cnt=1;
                    while (resultSet.next()) {
                        String result = resultSet.getString("result");
                        gameResults.append("Game Result ").append(cnt).append(": ").append(result).append("\n");
                        cnt++;
                    }

                    // Display the game results in a new window
                    showPreviousGames(gameResults.toString());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void showPreviousGames(String gameResults) {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Previous Games");

        TextArea textArea = new TextArea();
        textArea.setEditable(false);
        textArea.setText(gameResults);

        StackPane layout = new StackPane(textArea);
        layout.setPadding(new Insets(10));

        Scene scene = new Scene(layout, 300, 200);
        stage.setScene(scene);

        stage.showAndWait();
    }

    @SuppressWarnings("exports")
	public StackPane getStackPane() {
        return pane;
    }

    public void updateMessage(String message) {
        this.message.setText(message);
    }

    public void showStartBtn() {
        startGame.setVisible(true);
        history.setVisible(true);
    }

    public void hideStartBtn() {
        startGame.setVisible(false);
        history.setVisible(false);
    }

    public void setStartBtnonAction(EventHandler<ActionEvent> onAction) {
        startGame.setOnAction(onAction);
    }
    
}
