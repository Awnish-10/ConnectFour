package com.internshala.connect4;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Optional;

public class Main extends Application {

	private Controller controller;

	@Override
	public void start(Stage primaryStage) throws Exception {

		FXMLLoader loader = new FXMLLoader(getClass().getResource("game.fxml"));
		GridPane rootGP = loader.load();

		controller = loader.getController();
		controller.createPlayground();

		MenuBar menuBar = createMenu();
		menuBar.prefWidthProperty().bind(primaryStage.widthProperty());

		Pane menuPane = (Pane) rootGP.getChildren().get(0);
		menuPane.getChildren().add(menuBar);


		Scene scene = new Scene(rootGP);

		primaryStage.setScene(scene);
		primaryStage.setTitle("Connect Four");
		primaryStage.setResizable(false);
		primaryStage.show();
	}

	private MenuBar createMenu() {

		Menu fileMenu = new Menu("File");//file menu

		MenuItem newGame = new MenuItem("New Game");
		newGame.setOnAction(event -> controller.resetGame());


		MenuItem resetGame = new MenuItem("Reset Game");
		resetGame.setOnAction(event -> controller.resetGame());

		SeparatorMenuItem smi = new SeparatorMenuItem();

		MenuItem exitGame = new MenuItem("Exit Game");
		exitGame.setOnAction(event -> {

			Platform.exit();
			System.exit(0);

		});
		fileMenu.getItems().addAll(newGame, resetGame, smi, exitGame);

		Menu helpMenu = new Menu("Help");//help
		MenuItem aboutGame = new MenuItem("About Connect4");
		aboutGame.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				aboutGame();


			}
		});
		SeparatorMenuItem smi2 = new SeparatorMenuItem();

		MenuItem aboutMe = new MenuItem("About Me");
		aboutMe.setOnAction(event -> {
			aboutMe();
		});

		helpMenu.getItems().addAll(aboutGame, smi2, aboutMe);

		MenuBar menuBar = new MenuBar();
		menuBar.getMenus().addAll(fileMenu, helpMenu);

		return menuBar;


	}

	private void aboutMe() {
		Alert alertDialog = new Alert(Alert.AlertType.INFORMATION);
		alertDialog.setTitle("About The Developer");
		alertDialog.setHeaderText("Awnish Negi");
		alertDialog.setContentText("I love to play around with codes." +
				"This is my first Java Game");
		alertDialog.show();
	}



	private void aboutGame() {

		Alert alertDialog = new Alert(Alert.AlertType.INFORMATION);
		alertDialog.setTitle("About Connect Four");
		alertDialog.setHeaderText("How To Play");
		alertDialog.setContentText("Connect Four is a two-player connection game in which the players first choose a color and" +
				" then take turns dropping colored discs from the top into a seven-column, six-row vertically suspended grid. " +
				"The pieces fall straight down, occupying the next available space within the column. The objective of the game is " +
				"to be the first to form a horizontal, vertical, or diagonal line of four of one's own discs. Connect Four is a " +
				"solved game. The first player can always win by playing the right moves.\n" +
				"\n");
		alertDialog.show();
	}


}
