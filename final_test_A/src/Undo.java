class Undo {
	static boolean undo(MementoTicTacToe mementoTicTacToe, CareTaker careTaker) {

		if (careTaker.getIndex() > 0) {
			Memento prev = careTaker.getPrev();
			DrawToken.clearToken(prev.getCell());
			//DrawToken.drawToken(' ', prev.getCell());
			mementoTicTacToe.setWhoseTurn(prev.getWhoseTurn());
			mementoTicTacToe.getLblStatus().setText(mementoTicTacToe.getWhoseTurn() + "'s turn");
			return true;
		}
		return false;
	}
}
