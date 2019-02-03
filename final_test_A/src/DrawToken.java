import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;

class DrawToken {
	static void drawToken(char token, Cell cell) {
		cell.setToken(token);
		if (token == 'X') {
			Line line1 = new Line(10, 10, cell.getWidth() - 10, cell.getHeight() - 10);
			line1.endXProperty().bind(cell.widthProperty().subtract(10));
			line1.endYProperty().bind(cell.heightProperty().subtract(10));
			Line line2 = new Line(10, cell.getHeight() - 10, cell.getWidth() - 10, 10);
			line2.startYProperty().bind(cell.heightProperty().subtract(10));
			line2.endXProperty().bind(cell.widthProperty().subtract(10));
			line1.setStroke(Color.RED);
			line2.setStroke(Color.RED);
			line1.setStrokeWidth(10.0f);
			line2.setStrokeWidth(10.0f);
			cell.getChildren().addAll(line1, line2);
		} else if (token == 'O') {
			Ellipse ellipse = new Ellipse(cell.getWidth() / 2, cell.getHeight() / 2, cell.getWidth() / 2 - 10,
					cell.getHeight() / 2 - 10);
			ellipse.centerXProperty().bind(cell.widthProperty().divide(2));
			ellipse.centerYProperty().bind(cell.heightProperty().divide(2));
			ellipse.radiusXProperty().bind(cell.widthProperty().divide(2).subtract(10));
			ellipse.radiusYProperty().bind(cell.heightProperty().divide(2).subtract(10));
			ellipse.setStroke(Color.BLACK);
			ellipse.setFill(Color.BLUE);
			cell.getChildren().add(ellipse);
		} else if (token == ' ')
			cell.getChildren().clear();
	}

	static void clearToken(Cell cell) {
		cell.setToken(' ');
		cell.getChildren().clear();
	}

}
