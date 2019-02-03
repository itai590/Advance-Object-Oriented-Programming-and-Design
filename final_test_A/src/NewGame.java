class NewGame {
	static void newGame(MementoTicTacToe mementoTicTacToe, CareTaker careTaker) {
		
		while(Undo.undo(mementoTicTacToe, careTaker));
	}
}
