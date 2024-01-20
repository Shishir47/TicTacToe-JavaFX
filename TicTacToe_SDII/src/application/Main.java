package application;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Main extends Application {
    private InfoCenter infoCenter;
    private TileBoard tileBoard;
    private MainController mainController;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            BorderPane root = new BorderPane();
            root.setId("root");
            Scene scene = new Scene(root, UIConst.APP_WIDTH, UIConst.APP_HEIGHT);
            scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
            initLayout(root);
            initDatabase();

            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initLayout(BorderPane root) {
        mainController = new MainController();
        infoCenter = new InfoCenter();
        infoCenter.setStartBtnonAction(startNewGame());

        tileBoard = new TileBoard(mainController, infoCenter);
        root.getChildren().addAll(infoCenter.getStackPane(), tileBoard.getStackPane());
    }

    private EventHandler<ActionEvent> startNewGame() {
        return new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                infoCenter.hideStartBtn();
                infoCenter.updateMessage("Player X's Turn");
                tileBoard.startNew();
            }
        };
    }

    private void initDatabase() {
        // Perform any additional database initialization if needed
    }
}
