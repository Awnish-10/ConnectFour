package com.internshala.connect4;

import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.util.Duration;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.lang.System.*;

public class Controller implements Initializable {
	private static final int COLUMNS = 7;
	private static final int ROWS = 6;
	private static final int CIRCLE_DIAMETER = 80;
	private static final String diskColor1 = "#24303E";
	private static final String diskColor2 = "#4CAA88";
	private boolean isAllowedToInsert = true;

	private static String Player1 ="Player1" ;
	private static String Player2 ="Player2" ;
	private boolean isFirstPlayer = true;

	private Disc [][] inserterDiscArray= new Disc[ROWS][COLUMNS];//structural changes


	@FXML
	public GridPane rootGP;
	@FXML
	public Pane insertedDisc;
	@FXML
	public Label playerNameLabel;
	@FXML
	public TextField textField1;
	@FXML
	public TextField textField2;
	@FXML
	public Button nameButton;

	public  void createPlayground(){

Shape rectangleWithHoles = createGameStructuralGrid();
settingPlayerNames();
		rootGP.add(rectangleWithHoles,0,1);
		List<Rectangle> rectangleList = createClickableColumn();
		for(Rectangle rectangle:rectangleList) {
			rootGP.add(rectangle, 0, 1);

		}
	}

	private void settingPlayerNames() {


		nameButton.setOnAction(event -> {

			Player1 = textField1.getText();
			Player2 = textField2.getText();
			playerNameLabel.setText(Player1);

		});
	}

	private  Shape createGameStructuralGrid(){

		Shape rectangleWithHoles = new Rectangle((COLUMNS+1)*CIRCLE_DIAMETER ,(ROWS+1)*CIRCLE_DIAMETER);
		for(int row=0 ; row< ROWS;row++){
			for (int col=0 ;col<COLUMNS ;col++) {
				Circle circle = new Circle();
				circle.setRadius(CIRCLE_DIAMETER / 2);
				circle.setCenterX(CIRCLE_DIAMETER / 2);
				circle.setCenterY(CIRCLE_DIAMETER / 2);
				circle.setSmooth(true);

				circle.setTranslateX(col* (CIRCLE_DIAMETER+5)+CIRCLE_DIAMETER/4);
				circle.setTranslateY(row* (CIRCLE_DIAMETER+5)+CIRCLE_DIAMETER/4);
				rectangleWithHoles = Shape.subtract(rectangleWithHoles, circle);
			}
		}

		rectangleWithHoles.setFill(Color.WHITE);
		return rectangleWithHoles;

	}
	private List<Rectangle> createClickableColumn() {
		List<Rectangle> rectangleList = new ArrayList<>();

		for (int col = 0; col < COLUMNS; col++) {
			Rectangle rectangle = new Rectangle(CIRCLE_DIAMETER, (ROWS + 1) * CIRCLE_DIAMETER);
			rectangle.setFill(Color.TRANSPARENT);
			rectangle.setTranslateX(col * (CIRCLE_DIAMETER + 5) + CIRCLE_DIAMETER / 4);
			rectangle.setOnMouseEntered(event->rectangle.setFill(Color.valueOf("#eeeeee26")));
			rectangle.setOnMouseExited(event->rectangle.setFill(Color.TRANSPARENT));
            final int column = col;
			rectangle.setOnMouseClicked(event ->{

                    if(isAllowedToInsert) {
                    	isAllowedToInsert = false;
	                    insertDisc(new Disc(isFirstPlayer), column);
                    }
		     });
			rectangleList.add(rectangle);
		}
			return rectangleList;

		}

	private void insertDisc(Disc disc ,int col ) {

		int row = ROWS - 1;

		while(row >=0)
		{
			if(getDiscIfPresent(row ,col)==null)
				break;
			 row--;

		}
		if(row<0)
			return;

      inserterDiscArray[row][col] =disc;
      insertedDisc.getChildren().add(disc);
      disc.setTranslateX(col * (CIRCLE_DIAMETER + 5) + CIRCLE_DIAMETER / 4);

		int currentRow = row;
		TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(0.5) ,disc);
         translateTransition.setToY(row* (CIRCLE_DIAMETER+5)+CIRCLE_DIAMETER/4);
         translateTransition.setOnFinished(event->{

         	isAllowedToInsert = true;
         	if(gameEnded(currentRow ,col))
         	{
         		gameOver();
         		return;
            }
         	isFirstPlayer = !isFirstPlayer;
         	playerNameLabel.setText(isFirstPlayer?Player1:Player2);
         });
         translateTransition.play();
	}

	private void gameOver() {

		String winner = isFirstPlayer ? Player1 : Player2;

        Alert alertDialog = new Alert(Alert.AlertType.INFORMATION);
        alertDialog.setTitle("Connect Four");
		alertDialog.setHeaderText("The winner is " + winner);
		alertDialog.setContentText("Want to play again");
		ButtonType yes = new ButtonType("Yes");
		ButtonType no = new ButtonType("No ,Exit");
		alertDialog.getButtonTypes().setAll(yes ,no);

		Platform.runLater(()->{
		Optional<ButtonType> btnClicked = alertDialog.showAndWait();
		if(btnClicked.isPresent() && btnClicked.get() == yes)
		{
			resetGame();
		}else
		{
			Platform.exit();
			System.exit(0);
		}
		});



	}

	public void resetGame() {

		insertedDisc.getChildren().clear();

		for (int i=0 ;i<inserterDiscArray.length ;i++)
			for (int j=0 ;j<inserterDiscArray[i].length ;j++)
				inserterDiscArray[i][j] = null;

			isFirstPlayer = true;
			Player1 = "Player1";
			Player2 = "Player2";


			playerNameLabel.setText(Player1);


			createPlayground();


	}

	private boolean gameEnded(int row, int column) {

		List<Point2D> verticalPoints = IntStream.rangeClosed(row - 3 ,row + 3)
				                       .mapToObj(r-> new Point2D(r,column))
				                       .collect(Collectors.toList());

		List<Point2D> horizontalPoints = IntStream.rangeClosed(column - 3 ,column + 3)
				                         .mapToObj(col-> new Point2D(row,col))
				                         .collect(Collectors.toList());

		Point2D startPoint1 = new Point2D(row - 3 ,column +3);
		List<Point2D> diagonal1Points = IntStream.rangeClosed(0 ,6)
				                         .mapToObj(i-> startPoint1.add(i ,-i))
				                         .collect(Collectors.toList());

		Point2D startPoint2 = new Point2D(row - 3 ,column -3);
		List<Point2D> diagonal2Points = IntStream.rangeClosed(0 ,6)
				                        .mapToObj(i-> startPoint2.add(i ,i))
				                        .collect(Collectors.toList());

		boolean isEnded = checkCombinations(verticalPoints) || checkCombinations(horizontalPoints)
				         || checkCombinations(diagonal1Points) ||checkCombinations(diagonal2Points);


		return isEnded;
	}

	private boolean checkCombinations(List<Point2D> Points) {
		int chain = 0;
		for (Point2D point: Points) {

			int rowIndexForArray = (int) point.getX();
			int columnIndexForArray = (int) point.getY();

			Disc disc = getDiscIfPresent(rowIndexForArray ,columnIndexForArray);
			if (disc != null && disc.isPlayerOneMove==isFirstPlayer) {
				chain++;
				if (chain == 4) {
					return true;
				}
			}else {
				chain = 0;
			}
		}
		return false;
	}
    private Disc getDiscIfPresent(int row ,int column)
    {
    	if (row>=ROWS || row<0 ||column>=COLUMNS ||column<0 )
    		return null;

    	return inserterDiscArray[row][column];
    }


	private static class Disc extends Circle{

	private final boolean isPlayerOneMove;

		public Disc(boolean isPlayerOneMove) {
			this.isPlayerOneMove = isPlayerOneMove;
			setRadius(CIRCLE_DIAMETER /2);
			setFill(isPlayerOneMove?Color.valueOf(diskColor1):Color.valueOf(diskColor2));
			setCenterX(CIRCLE_DIAMETER/2);
			setCenterY(CIRCLE_DIAMETER/2);
		}

	}


	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}
}
