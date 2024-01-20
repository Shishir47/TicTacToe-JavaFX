module TicTacToe_SDII {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
	requires javafx.graphics;

    opens application to javafx.graphics;
    exports application;
}
