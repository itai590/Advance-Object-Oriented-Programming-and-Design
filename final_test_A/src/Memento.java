class Memento {

	private char whoseTurn;
	private Cell cell;

	Memento(char whoseTurn, Cell cell) {
		this.whoseTurn = whoseTurn;
		this.cell = cell;
	}

	char getWhoseTurn() {
		return whoseTurn;
	}

	Cell getCell() {
		return cell;
	}	
}