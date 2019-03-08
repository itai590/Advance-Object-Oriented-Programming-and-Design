/**
 * @author Itai
 * @HW 2
 */

import java.io.EOFException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Stack;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

public class HW_2_Itai_Cohen extends Application {
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {

		int violation = 1; // violation message check

		for (int i = 0; i < AddressBookPane.MAX_PANES + violation; i++) {
			AddressBookPane addressBookPane = AddressBookPane.getInstance();
			if (addressBookPane != null) {
				Pane pane = addressBookPane.getPane();
				Scene scene = new Scene(pane);
				scene.getStylesheets().add("styles.css");
				primaryStage.setTitle("HW_2_Itai_Cohen window:"+(i+1));
				primaryStage.setScene(scene);
				primaryStage.show();
				primaryStage.setAlwaysOnTop(true);
				primaryStage = new Stage();

			} else {
				System.out.println(
						"Singelton Vioaltion. Only " + AddressBookPane.getPanesCounter() + " panes were created");
			}
		}
	}
}

class AddressBookPane extends GridPane {
	private static int panesCounter = 0;
	// assume: MAX_PANES >= MAX_MAIN_PANES
	final static int MAX_MAIN_PANES = 1;
	final static int MAX_PANES = 3;

	private RandomAccessFile raf;

	// Text fields
	private TextField jtfName = new TextField();
	private TextField jtfStreet = new TextField();
	private TextField jtfCity = new TextField();
	private TextField jtfState = new TextField();
	private TextField jtfZip = new TextField();
	// Buttons
	private AddButton jbtAdd;
	private FirstButton jbtFirst;
	private NextButton jbtNext;
	private PreviousButton jbtPrevious;
	private LastButton jbtLast;
	private RedoButton jbtRedo;
	private UndoButton jbtUndo;

	// Buttons pane
	private FlowPane jpButton = new FlowPane();

	private ArrayList<CommandButton> commandBtArray = new ArrayList<CommandButton>();

	private Stack<CommandButton.Memento> stack = new Stack<>();

	// my Button
	// private PrintToConsoleButton jbtPrintToConsoleButton;

	public EventHandler<ActionEvent> ae = new EventHandler<ActionEvent>() {
		public void handle(ActionEvent arg0) {
			((Command) arg0.getSource()).Execute();
		}
	};

	public static AddressBookPane getInstance() {
		AddressBookPane addressPane = new AddressBookPane();

		if (panesCounter < MAX_MAIN_PANES) {
			Decorator.decorator(addressPane.jpButton, true, addressPane.commandBtArray);
			panesCounter++;
			return addressPane;

		}

		else if (panesCounter < MAX_PANES) {
			Decorator.decorator(addressPane.jpButton, false, addressPane.commandBtArray);
			panesCounter++;
			return addressPane;
		} else
			return null;
	}

	private AddressBookPane() { // Open or create a random access file
		try {
			raf = new RandomAccessFile("address.dat", "rw");
		} catch (IOException ex) {
			System.out.print("Error: " + ex);
			System.exit(0);
		}
		jtfState.setAlignment(Pos.CENTER_LEFT);
		jtfState.setPrefWidth(120);// old 25
		jtfZip.setPrefWidth(120);

		// add buttons to array
		commandBtArray.add(jbtAdd = new AddButton(this, raf, true));
		commandBtArray.add(jbtFirst = new FirstButton(this, raf, false));
		commandBtArray.add(jbtNext = new NextButton(this, raf, false));
		commandBtArray.add(jbtPrevious = new PreviousButton(this, raf, false));
		commandBtArray.add(jbtLast = new LastButton(this, raf, false));
		commandBtArray.add(jbtRedo = new RedoButton(this, raf, true, stack));
		commandBtArray.add(jbtUndo = new UndoButton(this, raf, true, stack));

		Label state = new Label("State");
		Label zp = new Label("Zip");
		Label name = new Label("Name");
		Label street = new Label("Street");
		Label city = new Label("City");
		// Panel p1 for holding labels Name, Street, and City
		GridPane p1 = new GridPane();
		p1.add(name, 0, 0);
		p1.add(street, 0, 1);
		p1.add(city, 0, 2);
		p1.setAlignment(Pos.CENTER_LEFT);
		p1.setVgap(8);
		p1.setPadding(new Insets(0, 2, 0, 2));
		GridPane.setVgrow(name, Priority.ALWAYS);
		GridPane.setVgrow(street, Priority.ALWAYS);
		GridPane.setVgrow(city, Priority.ALWAYS);
		// City Row
		GridPane adP = new GridPane();
		adP.add(jtfCity, 0, 0);
		adP.add(state, 1, 0);
		adP.add(jtfState, 2, 0);
		adP.add(zp, 3, 0);
		adP.add(jtfZip, 4, 0);
		adP.setAlignment(Pos.CENTER_LEFT);
		GridPane.setHgrow(jtfCity, Priority.ALWAYS);
		GridPane.setVgrow(jtfCity, Priority.ALWAYS);
		GridPane.setVgrow(jtfState, Priority.ALWAYS);
		GridPane.setVgrow(jtfZip, Priority.ALWAYS);
		GridPane.setVgrow(state, Priority.ALWAYS);
		GridPane.setVgrow(zp, Priority.ALWAYS);
		// Panel p4 for holding jtfName, jtfStreet, and p3
		GridPane p4 = new GridPane();
		p4.add(jtfName, 0, 0);
		p4.add(jtfStreet, 0, 1);
		p4.add(adP, 0, 2);
		p4.setVgap(1);
		GridPane.setHgrow(jtfName, Priority.ALWAYS);
		GridPane.setHgrow(jtfStreet, Priority.ALWAYS);
		GridPane.setHgrow(adP, Priority.ALWAYS);
		GridPane.setVgrow(jtfName, Priority.ALWAYS);
		GridPane.setVgrow(jtfStreet, Priority.ALWAYS);
		GridPane.setVgrow(adP, Priority.ALWAYS);
		// Place p1 and p4 into jpAddress
		GridPane jpAddress = new GridPane();
		jpAddress.add(p1, 0, 0);
		jpAddress.add(p4, 1, 0);
		GridPane.setHgrow(p1, Priority.NEVER);
		GridPane.setHgrow(p4, Priority.ALWAYS);
		GridPane.setVgrow(p1, Priority.ALWAYS);
		GridPane.setVgrow(p4, Priority.ALWAYS);
		// Set the panel with line border
		jpAddress.setStyle("-fx-border-color: grey;" + " -fx-border-width: 1;" + " -fx-border-style: solid outside ;");

		jpButton.setHgap(5);

		// jpButton.getChildren().addAll(jbtAdd, jbtFirst, jbtNext, jbtPrevious,
		// jbtLast, jbtPrintToConsoleButton);

		jpButton.setAlignment(Pos.CENTER);
		GridPane.setVgrow(jpButton, Priority.NEVER);
		GridPane.setVgrow(jpAddress, Priority.ALWAYS);
		GridPane.setHgrow(jpButton, Priority.ALWAYS);
		GridPane.setHgrow(jpAddress, Priority.ALWAYS);

		// Add jpAddress and jpButton to the stage
		this.setVgap(5);
		this.add(jpAddress, 0, 0);
		this.add(jpButton, 0, 1);

		jbtAdd.setOnAction(ae);
		jbtFirst.setOnAction(ae);
		jbtNext.setOnAction(ae);
		jbtPrevious.setOnAction(ae);
		jbtLast.setOnAction(ae);
		jbtRedo.setOnAction(ae);
		jbtUndo.setOnAction(ae);
		// jbtPrintToConsoleButton.setOnAction(ae);

		jbtFirst.Execute();
	}

	public void actionHandled(ActionEvent e) {
		((Command) e.getSource()).Execute();
	}

	public void SetName(String text) {
		jtfName.setText(text);
	}

	public void SetStreet(String text) {
		jtfStreet.setText(text);
	}

	public void SetCity(String text) {
		jtfCity.setText(text);
	}

	public void SetState(String text) {
		jtfState.setText(text);
	}

	public void SetZip(String text) {
		jtfZip.setText(text);
	}

	public void ClearTextFields() {
		jtfName.setText("");
		jtfStreet.setText("");
		jtfCity.setText("");
		jtfState.setText("");
		jtfZip.setText("");
	}

	public String GetName() {
		return jtfName.getText();
	}

	public String GetStreet() {
		return jtfStreet.getText();
	}

	public String GetCity() {
		return jtfCity.getText();
	}

	public String GetState() {
		return jtfState.getText();
	}

	public String GetZip() {
		return jtfZip.getText();
	}

	public Pane getPane() {
		return this;
	}

	public static int getPanesCounter() {
		return panesCounter;
	}
}

interface Command {
	public void Execute();
}

class CommandButton extends Button implements Command {
	public final static int NAME_SIZE = 32;
	public final static int STREET_SIZE = 32;
	public final static int CITY_SIZE = 20;
	public final static int STATE_SIZE = 10;
	public final static int ZIP_SIZE = 5;
	public final static int RECORD_SIZE = (NAME_SIZE + STREET_SIZE + CITY_SIZE + STATE_SIZE + ZIP_SIZE);// 99
	public final static int CHAR_SIZE = 2; // *r/w implemented with char
	protected AddressBookPane p;
	protected RandomAccessFile raf;
	protected boolean isInMainWindow;

	public CommandButton(AddressBookPane pane, RandomAccessFile r, boolean isInMainWindow) {
		super();
		p = pane;
		raf = r;
		this.isInMainWindow = isInMainWindow;
	}

	public void writeAddressMementoStack(long position, Memento memento) {
		writeAddress(memento.getAddress(), position);
	}

	public CommandButton() {
	}

	public void Execute() {
	}

	/** Write a record at the end of the file */
	public void writeAddress(Address address, long position) {
		try {
			raf.seek(position);
			FixedLengthStringIO.writeFixedLengthString(address.getName(), NAME_SIZE, raf);
			FixedLengthStringIO.writeFixedLengthString(address.getStreet(), STREET_SIZE, raf);
			FixedLengthStringIO.writeFixedLengthString(address.getCity(), CITY_SIZE, raf);
			FixedLengthStringIO.writeFixedLengthString(address.getState(), STATE_SIZE, raf);
			FixedLengthStringIO.writeFixedLengthString(String.valueOf(address.getZip()), ZIP_SIZE, raf);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	// Write a record at the end of the file */
	public void writeAddress() {
		try {
			raf.seek(raf.length());
			FixedLengthStringIO.writeFixedLengthString(p.GetName(), NAME_SIZE, raf);
			FixedLengthStringIO.writeFixedLengthString(p.GetStreet(), STREET_SIZE, raf);
			FixedLengthStringIO.writeFixedLengthString(p.GetCity(), CITY_SIZE, raf);
			FixedLengthStringIO.writeFixedLengthString(p.GetState(), STATE_SIZE, raf);
			FixedLengthStringIO.writeFixedLengthString(p.GetZip(), ZIP_SIZE, raf);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Read a record at the specified position
	 * 
	 * @return Address
	 */
	public Address readAddress(long position) throws IOException {
		raf.seek(position);
		String name = FixedLengthStringIO.readFixedLengthString(NAME_SIZE, raf);
		String street = FixedLengthStringIO.readFixedLengthString(STREET_SIZE, raf);
		String city = FixedLengthStringIO.readFixedLengthString(CITY_SIZE, raf);
		String state = FixedLengthStringIO.readFixedLengthString(STATE_SIZE, raf);
		String zip = FixedLengthStringIO.readFixedLengthString(ZIP_SIZE, raf);
		p.SetName(name);
		p.SetStreet(street);
		p.SetCity(city);
		p.SetState(state);
		p.SetZip(zip);

		return new Address(p.GetName(), p.GetStreet(), p.GetCity(), p.GetState(), p.GetZip());
	}

	/** sort the file by the specified comparator * @throws IOException */
	public void sort(Comparator<Address> comperator) throws IOException {

		for (int i = 0; i < raf.length() / RECORD_SIZE / CHAR_SIZE; i++) {
			for (int j = 0; j < raf.length() / RECORD_SIZE / CHAR_SIZE - 1; j++) {

				long pos = j * (RECORD_SIZE * CHAR_SIZE);
				Address a1 = readAddress(pos);
				Address a2 = readAddress(raf.getFilePointer());
				if (comperator.compare(a1, a2) > 0)
					swap(a1, a2, pos);
			}
		}
		raf.seek(0);
		readAddress(0);

	}

	/**
	 * swap two Address-es in the file using RandomAccessFile * @throws IOException
	 */
	public void swap(Address a1, Address a2, long i) throws IOException {
		writeAddress(a2, i);
		writeAddress(a1, i + RECORD_SIZE * CHAR_SIZE);
	}

	/**
	 * print file elements to console using RandomAccessFile * @throws IOException
	 * FileNotFoundException
	 */
	public void printFile() throws FileNotFoundException, IOException {
		String fileContentMsg = "";
		for (int i = 0; i < RECORD_SIZE; i++) {
			if (i == RECORD_SIZE / 2)
				fileContentMsg = fileContentMsg + "file content";
			else
				fileContentMsg = fileContentMsg + "-";
		}
		System.out.println(fileContentMsg);
		if (raf.length() > 0) {
			long pointer = raf.getFilePointer();
			try {
				raf.seek(0);
				while (true) {
					Address a1 = readAddress(raf.getFilePointer());
					System.out.println(a1);
				}
			} catch (EOFException ex) {
			} catch (IOException ex) {
				ex.printStackTrace();
			}
			readAddress(pointer - CHAR_SIZE * RECORD_SIZE);
		}
	}

	public void ClearMainWindow() {
		p.ClearTextFields();
	}

	public static class Memento {
		private Address address;

		public Memento(Address a) {
			address = a;
		}

		private Address getAddress() {
			return address;
		}
	}
}

class AddButton extends CommandButton {
	public AddButton(AddressBookPane pane, RandomAccessFile r, boolean isInMainWindow) {
		super(pane, r, isInMainWindow);
		this.setText("Add");
	}

	@Override
	public void Execute() {
		writeAddress();
	}
}

class NextButton extends CommandButton {
	public NextButton(AddressBookPane pane, RandomAccessFile r, boolean isInMainWindow) {
		super(pane, r, isInMainWindow);
		this.setText("Next");
	}

	@Override
	public void Execute() {
		try {
			if (raf.length() == 0)
				ClearMainWindow();
			else {
				long currentPosition = raf.getFilePointer();
				if (currentPosition < raf.length())
					readAddress(currentPosition);
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}

class PreviousButton extends CommandButton {
	public PreviousButton(AddressBookPane pane, RandomAccessFile r, boolean isInMainWindow) {
		super(pane, r, isInMainWindow);
		this.setText("Previous");
	}

	@Override
	public void Execute() {
		try {
			if (raf.length() == 0)
				ClearMainWindow();
			else {
				long currentPosition = raf.getFilePointer();
				if (currentPosition - 2 * 2 * RECORD_SIZE >= 0)
					readAddress(currentPosition - 2 * 2 * RECORD_SIZE);
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}

class LastButton extends CommandButton {
	public LastButton(AddressBookPane pane, RandomAccessFile r, boolean isInMainWindow) {
		super(pane, r, isInMainWindow);
		this.setText("Last");
	}

	@Override
	public void Execute() {
		try {
			long lastPosition = raf.length();
			if (lastPosition > 0)
				readAddress(lastPosition - 2 * RECORD_SIZE);
			else
				ClearMainWindow();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}

class FirstButton extends CommandButton {
	public FirstButton(AddressBookPane pane, RandomAccessFile r, boolean isInMainWindow) {
		super(pane, r, isInMainWindow);
		this.setText("First");
	}

	@Override
	public void Execute() {
		try {
			if (raf.length() > 0)
				readAddress(0);
			else
				ClearMainWindow();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}

class UndoButton extends CommandButton {
	private Stack<Memento> s;

	public UndoButton(AddressBookPane pane, RandomAccessFile r, boolean isInMainWindow, Stack<Memento> s) {
		super(pane, r, isInMainWindow);
		this.setText("Undo");
		this.s = s;
	}

	@Override
	public void Execute() {
		try {

			long lastPosition = raf.length();

			if (lastPosition > 0) {
				Address address = readAddress(lastPosition - 2 * RECORD_SIZE);
				s.push(new Memento(address));
				raf.setLength(raf.length() - 2 * RECORD_SIZE);

				if (raf.length() > 0) {
					raf.seek(0);
					readAddress(0);
				} else {
					ClearMainWindow();
				}
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}

class RedoButton extends CommandButton {
	private Stack<Memento> s;

	public RedoButton(AddressBookPane pane, RandomAccessFile r, boolean isInMainWindow, Stack<Memento> s) {
		super(pane, r, isInMainWindow);
		this.setText("Redo");
		this.s = s;
	}

	@Override
	public void Execute() {
		try {
			long currentPosition = raf.length();

			if (!s.isEmpty())
				writeAddressMementoStack(currentPosition, s.pop());
			if (raf.length() > 0)
				readAddress(currentPosition);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

class PrintToConsoleButton extends CommandButton {
	public PrintToConsoleButton(AddressBookPane pane, RandomAccessFile r, boolean isInMainWindow) {

		super(pane, r, isInMainWindow);
		this.setText("Console Print");
	}

	@Override
	public void Execute() {

		try {
			printFile();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
