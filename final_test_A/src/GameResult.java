class GameResult {

	static boolean isFull(Cell[][] cell) {
		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 3; j++)
				if (cell[i][j].getToken() == ' ')
					return false;
		return true;
	}

	static boolean isWon(Cell[][] cell, char token) {
		for (int i = 0; i < 3; i++)
			if (cell[i][0].getToken() == token && cell[i][1].getToken() == token && cell[i][2].getToken() == token)
				return true;
		for (int j = 0; j < 3; j++)
			if (cell[0][j].getToken() == token && cell[1][j].getToken() == token && cell[2][j].getToken() == token)
				return true;
		if (cell[0][0].getToken() == token && cell[1][1].getToken() == token && cell[2][2].getToken() == token)
			return true;
		if (cell[0][2].getToken() == token && cell[1][1].getToken() == token && cell[2][0].getToken() == token)
			return true;
		return false;
	}

}
