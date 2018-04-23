package tetris;

class Position {

	private int _row;
	private int _column;

	public Position(int row, int column) {
		_row = row;
		_column = column;
	}

	public Position(Position position) {
		_row = position.getRow();
		_column = position.getColumn();
	}

	public Position() {
		_row = 0;
		_column = 0;
	}

	public int getRow() {
		return _row;
	}

	public void setRow(int row) {
		_row = row;
	}

	public int getColumn() {
		return _column;
	}

	public void setColumn(int column) {
		_column = column;
	}

	public void setPosition(Position position) {
		_row = position.getRow();
		_column = position.getColumn();
	}
} 