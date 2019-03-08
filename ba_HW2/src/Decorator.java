
/**
 * @author Itai
 * @HW 2
 */

import java.util.ArrayList;
import javafx.scene.layout.FlowPane;

public class Decorator {
	public static void decorator(FlowPane jpButton, boolean isMainWindow, ArrayList<CommandButton> commandBtArray) {
		if (isMainWindow) {
			for (int i = 0; i < commandBtArray.size(); i++) {
				jpButton.getChildren().add(commandBtArray.get(i));
			}
		} else {// secondary window
			for (int i = 0; i < commandBtArray.size(); i++) {
				if (!commandBtArray.get(i).isInMainWindow) {
					jpButton.getChildren().add(commandBtArray.get(i));
				}
			}
		}
	}
}
