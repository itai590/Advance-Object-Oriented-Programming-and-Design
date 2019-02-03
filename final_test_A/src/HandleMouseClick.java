class HandleMouseClick {
	static void handleMouseClick(Cell cell) {
		if (cell.getToken() == ' ' && cell.getMementoTicTacToe().getWhoseTurn() != ' ') {
			DrawToken.drawToken(cell.getMementoTicTacToe().getWhoseTurn(), cell);
			
			if (GameResult.isWon(cell.getMementoTicTacToe().getCell(), cell.getMementoTicTacToe().getWhoseTurn())) {
				setMemento(cell);
				cell.getMementoTicTacToe().getLblStatus()
						.setText(cell.getMementoTicTacToe().getWhoseTurn() + " won! The game is over");
				cell.getMementoTicTacToe().setWhoseTurn(' ');
			} else if (GameResult.isFull(cell.getMementoTicTacToe().getCell())) {
				setMemento(cell);
				cell.getMementoTicTacToe().getLblStatus().setText("Draw! The game is over");
				cell.getMementoTicTacToe().setWhoseTurn(' ');
			} else {
				setMemento(cell);
				cell.getMementoTicTacToe().setWhoseTurn(changeWhoseTurn(cell));
				cell.getMementoTicTacToe().getLblStatus()
						.setText(cell.getMementoTicTacToe().getWhoseTurn() + "'s turn");
			}
		}
	}

	private static void setMemento(Cell cell) {
		Memento state = new Memento(cell.getMementoTicTacToe().getWhoseTurn(), cell);
		cell.getMementoTicTacToe().getCareTaker().add(state);
	}

	private static char changeWhoseTurn(Cell cell) {
		return (cell.getMementoTicTacToe().getWhoseTurn() == 'X') ? 'O' : 'X';
	}
}
