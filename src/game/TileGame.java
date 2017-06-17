package game;


import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.scene.*;
/**
 * 
 * @author Max
 *
 */
public class TileGame extends Application {
	private static final int GRIDWIDTH = 3;
	private static final int GRIDLENGTH = 3;
	private static final int TILESIZE = 40; 

	public static void main(String[] args) {

		launch(args);

	}

	@Override
	public void start(Stage stg) throws Exception {
		Board myBoard =new Board();
		//myBoard.print();
//		
//		System.out.println("WIN CONDITION: " + myBoard.winCondition());
//		
//		GridPane grid = new GridPane();
//		initGrid(grid);
//		Scene scene = new Scene(grid, 800, 800, Color.BLACK);
//		
//		stg.setScene(scene);
//		stg.show();
//
//		grid.getChildren().get(1).setRotate(3.4);
		myBoard.test();
	}

	private void initGrid(GridPane grid) {
		grid.setAlignment(Pos.CENTER);
		grid.setPadding(new Insets(10, 10, 10, 10));
		grid.setVgap(5);
		grid.setHgap(5);
		
		
		
		/**
		 * REPLACE THIS NEXT PART WITH READING IN DATA
		 */
		for (int i = 0; i < GRIDWIDTH; i++) {
			for (int q = 0; q < GRIDLENGTH; q++) {
				Rectangle next = new Rectangle(GRIDWIDTH * TILESIZE, GRIDLENGTH * TILESIZE);
				next.fillProperty().set(Color.ALICEBLUE);
				grid.add(next, i, q);
			}

		}
		 Color myColor = javafx.scene.paint.Color.DARKCYAN;
		 
		 grid.setBorder(new Border(new BorderStroke(Color.GRAY, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(1))));	
		//myGrid.setBorder();
	}
}
