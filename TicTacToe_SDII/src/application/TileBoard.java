package application;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

public class TileBoard {
    private StackPane pane;
    private InfoCenter infoCenter;
    private Tile[][] tiles = new Tile[3][3];
    private MainController mainController;

    private char playerTurn = 'X';
    private boolean isEnd = false;
    public TileBoard(MainController mainController, InfoCenter infoCenter) {
        this.mainController = mainController;
        this.infoCenter = infoCenter;

        pane = new StackPane();
        pane.setMinSize(UIConst.APP_WIDTH, UIConst.TILE_BOARD_HEIGHT);
        pane.setTranslateX(UIConst.APP_WIDTH / 2);
        pane.setTranslateY((UIConst.TILE_BOARD_HEIGHT / 2) + UIConst.INFO_CENTER_HEIGHT);

        pane.setId("tileBoard");

        addAllTiles();
    }

    private void addAllTiles() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                Tile tile = new Tile();
                tile.getStackPane().setTranslateX((j * 100) - 100);
                tile.getStackPane().setTranslateY((i * 100) - 100);
                pane.getChildren().add(tile.getStackPane());
                tiles[i][j] = tile;
            }
        }
    }

    public void startNew() {
        isEnd = false;
        playerTurn = 'X';
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                tiles[i][j].setVal("");
            }
        }
    }

    public void changePlayer() {
        if (playerTurn == 'X') {
            playerTurn = 'O';
        } else {
            playerTurn = 'X';
        }
        infoCenter.updateMessage("Player " + playerTurn + "'s turn");
    }

    public String getPlayerTurn() {
        return String.valueOf(playerTurn);
    }

    public StackPane getStackPane() {
        return pane;
    }

    public void checkWinner() {
        checkRows();
        checkCols();
        checkTopLtoBottomR();
        checkTopRtoBottom();
        checkTie();
    }

    private void checkRows() {
        for (int i = 0; i < 3; i++) {
            if (tiles[i][0].getVal().equals(tiles[i][1].getVal()) &&
                    tiles[i][0].getVal().equals(tiles[i][2].getVal()) &&
                    !tiles[i][0].getVal().isEmpty()) {
                String winner = tiles[i][0].getVal();
                endGame(winner);
                return;
            }
        }
    }

    private void endGame(String winner) {
        isEnd = true;
        infoCenter.updateMessage("Player " + winner + " Wins");
        infoCenter.showStartBtn();

        // Save the game result to the database
        mainController.saveGameResult("The Winner is Player "+winner);
    }

    private void checkCols() {
        if (!isEnd) {
            for (int j = 0; j < 3; j++) {
                if (tiles[0][j].getVal().equals(tiles[1][j].getVal()) &&
                        tiles[0][j].getVal().equals(tiles[2][j].getVal()) &&
                        !tiles[0][j].getVal().isEmpty()) {
                    String winner = tiles[0][j].getVal();
                    endGame(winner);
                    return;
                }
            }
        }
    }

    private void checkTopLtoBottomR() {
        if (!isEnd) {
            if (tiles[0][0].getVal().equals(tiles[1][1].getVal()) && tiles[0][0].getVal().equals(tiles[2][2].getVal()) && !tiles[0][0].getVal().isEmpty()) {
                String winner = tiles[0][0].getVal();
                endGame(winner);
            }
        }
    }

    private void checkTopRtoBottom() {
        if (!isEnd) {
            if (tiles[0][2].getVal().equals(tiles[1][1].getVal()) && tiles[0][2].getVal().equals(tiles[2][0].getVal()) && !tiles[0][2].getVal().isEmpty()) {
                String winner = tiles[0][2].getVal();
                endGame(winner);
            }
        }
    }

    private void checkTie() {
        if (!isEnd) {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (tiles[i][j].getVal().isEmpty()) {
                        return;
                    }
                }
            }
            isEnd = true;
            infoCenter.updateMessage("Tie");
            infoCenter.showStartBtn();
            mainController.saveGameResult("The Game was a Tie");
        }
    }
    private class Tile {
        private StackPane pane;
        private Label label;

        public Tile() {
            pane = new StackPane();
            pane.setMinSize(100, 100);

            Rectangle border = new Rectangle();
            border.setWidth(100);
            border.setHeight(100);
            border.setFill(Color.TRANSPARENT);
            border.setStroke(Color.BLACK);
            border.setStrokeWidth(2);
            pane.getChildren().add(border);

            label = new Label("");
            label.setAlignment(Pos.CENTER);
            label.setFont(Font.font(24));
            pane.getChildren().add(label);

            pane.setOnMouseClicked(event -> {
                if (label.getText().isEmpty() && !isEnd) {
                    label.setText(getPlayerTurn());
                    changePlayer();
                    checkWinner();
                }
            });
        }

        public StackPane getStackPane() {
            return pane;
        }

        public String getVal() {
            return this.label.getText();
        }

        public void setVal(String value) {
            this.label.setText(value);
        }
    }
}
