package ru.spbstu.icc.kspt.kuznetsov.tictactoe.gamemodel;


import java.io.Serializable;

public class GameModel implements Serializable {
	public static class GameModelException extends Exception{
		private static final long serialVersionUID = -2994209845750904063L;

		public GameModelException(String message) {
			super(message);
		}
	}
	
	public static enum VALUES {EMPTY, X, O;

        public VALUES invert() {
            if (this == X) return O;
            if (this == O) return X;
            return this;
        }

        public char toChar() {
            switch (this) {
                case O:
                    return 'O';
                case X:
                    return 'X';
                default:
                    return ' ';
            }
        }
    };
	
	private VALUES currentPlayer = VALUES.X;

	private final VALUES gameField[];
	private final int sz;
    private final int lnSize;
	
	public GameModel(int sz) {
		this(sz, 4);
	}

    public GameModel(int sz, int lnSize) {
        if (lnSize > sz) lnSize = sz;

        gameField = new VALUES[sz * sz];
        this.sz = sz;
        this.lnSize = lnSize;

        reset();
    }

	private final void reset() {
		currentPlayer = VALUES.X;
		for (int i = 0; i < gameField.length; i++){
			gameField[i] = VALUES.EMPTY;
		}
	}

	public VALUES getCurrentPlayer() {
		return currentPlayer;
	}
	
	public int getSz() {
		return sz;
	}

	/**
	 * 
	 * @return символ игрока для следующего хода
	 * @throws GameModelException 
	 */
	public VALUES makeMove(int pos) throws GameModelException {
		validate(pos);
		
		if (!canMove(pos))
			throw new GameModelException(String.format("Cell %d is busy. Try another one", pos));
		
		gameField[pos] = getCurrentPlayer();
		
		changePlayer();
		return getCurrentPlayer();
	}
	
	/**
	 * 
	 * @return символ игрока для следующего хода
	 * @throws GameModelException 
	 */
	public VALUES makeMove(int x, int y) throws GameModelException {
		validate(x,y);
		return makeMove(x + y * sz);
	}
	
	private void validate(int x, int y) throws GameModelException {
		if (x < 0 || x >= sz) throw new GameModelException(String.format("x=%d is out of bound [0, %d]", x, sz-1));
		if (y < 0 || y >= sz) throw new GameModelException(String.format("y=%d is out of bound [0, %d]", y, sz-1));
	}

	private void validate(int pos) throws GameModelException {
		if (pos < 0 || pos >= gameField.length) throw new GameModelException(String.format("pos=%d is out of bound [0, %d]", pos, gameField.length - 1));
	}
	
	private void changePlayer() {
		if (currentPlayer.equals(VALUES.O)){
			currentPlayer = VALUES.X;
		} else {
			currentPlayer = VALUES.O;
		}
	}
	
	/**
	 * 
	 * @return символ игрока, который победил, 
	 * EMPTY в случае "тупика",
	 * null в случае, если пока никто не победил
	 */
	public VALUES getWinner(){
		VALUES r;
		
		// проверим вертикальные линии
        for (int y = 0; y <= sz - lnSize; y++) {
            for (int x = 0; x < sz; x++) {
                r = chkLine(x + y * sz, sz);
                if (r != VALUES.EMPTY) return r;
            }
        }

		// проверим горизонтальные линии
        for (int x = 0; x <= sz - lnSize; x++) {
            for (int y = 0; y < sz; y++) {
                r = chkLine(x + y * sz, 1);
                if (r != VALUES.EMPTY) return r;
            }
        }

		// проверим диагонали: "\"
        for (int x = 0; x <= sz - lnSize; x++) {
            for (int y = 0; y <= sz - lnSize; y++) {
                r = chkLine(x + y * sz, sz + 1);
                if (r != VALUES.EMPTY) return r;
            }
        }

        // проверим диагонали: "/"
        for (int x = 0; x <= sz - lnSize; x++) {
            for (int y = 0; y <= sz - lnSize; y++) {
                r = chkLine(x + y * sz + lnSize - 1, sz - 1);
                if (r != VALUES.EMPTY) return r;
            }
        }
		
		// проверим тупик
		for (VALUES i : gameField){
			if (i == VALUES.EMPTY){
				return null; 
			}
		}
		
		// знаки ставить больше некуда
		return VALUES.EMPTY;
	}

	private VALUES chkLine(int start, int step) {
		VALUES r = gameField[start];
		for (int i = start + step, j = 1; i < gameField.length && j < lnSize; i += step, j++){
			if (r != gameField[i]) return VALUES.EMPTY;
		}
		return r;
	}

	public VALUES getSignAt(int x, int y) throws GameModelException {
		validate(x, y);
		return gameField[x + y * sz];
	}
	
	public GameModel dup() {
        GameModel other = new GameModel(this.sz);

        for (int i = 0; i < gameField.length; i++)
            other.gameField[i] = this.gameField[i];

        other.currentPlayer = this.currentPlayer;
        return other;
	}

	public boolean canMove(int pos) throws GameModelException {
		validate(pos);
		return gameField[pos] == VALUES.EMPTY;
	}

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder((getSz() + 3) * getSz() );

        try {
            for (int y = 0; y < getSz(); y++) {
                for (int x = 0; x < getSz(); x++) {
                    sb.append(getSignAt(x, y).toChar());
                }
                sb.append('\n');
            }
        } catch (GameModelException e){
            
        }

        return sb.toString();
    }
}
