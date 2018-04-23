package tetris;

class IntMatrix {

	private int _height;
	private int _width;
	private int _data[][];

	public IntMatrix(int height, int width, int initialValue) {
		_height = height;
		_width = width;
		_data = new int[_height][_width];
		for (int i = 0; i < _height; i++)
			for (int j = 0; j < _width; j++)
				_data[i][j] = initialValue;
	}

	public IntMatrix(IntMatrix source) {
		_height = source.getHeight();
		_width = source.getWidth();
		_data = new int[_height][_width];
		for (int i = 0; i < _height; i++)
			for (int j = 0; j < _width; j++)
				_data[i][j] = source.get(i, j);
	}

	public IntMatrix(int height, int width) {
		this(height, width, 0);
	}

	public int getHeight() {
		return _height;
	}

	public int getWidth() {
		return _width;
	}

	public void set(int row, int column, int value) {
		_data[row][column] = value;
	}

	public int get(int row, int column) {
		return _data[row][column];
	}

	public boolean contains(IntMatrix source, Position pos) {
		return partlyContains(source, pos, 0);
	}

	public boolean partlyContains(IntMatrix source, Position pos, int begin) {
		if (pos.getRow() < 0 || pos.getColumn() < 0)
			return false;
		if (pos.getColumn() + source.getWidth() > this._width)
			return false;
		if (pos.getRow() + source.getHeight() - begin > this._height)
			return false;
		for (int i = begin; i < source.getHeight(); i++) {
			for (int j = 0; j < source.getWidth(); j++) {
				if (source.get(i, j) > 0
						&& this.get(i + pos.getRow() - begin,
								j + pos.getColumn()) > 0)
					return false;
			}
		}
		return true;
	}

	public void add(IntMatrix source, Position pos) {
		for (int i = 0; i < source.getHeight(); i++) {
			for (int j = 0; j < source.getWidth(); j++) {
				if (source.get(i, j) > 0)
					this.set(pos.getRow() + i, pos.getColumn() + j,
							source.get(i, j));
			}
		}
	}
	
	public boolean isRowOccupied(int row) {
		for (int i = 0; i < _width; i++) {
			if (_data[row][i] == 0)
				return false;
		}
		return true;
	}

	public void deleteRow(int row) {
		for (int i = row; i > 0; i--)
			for (int j = 0; j < _width; j++) {
				_data[i][j] = _data[i - 1][j];
			}
		clearRow(0);
	}

	public void clear() {
		for (int i = 0; i < _height; i++) {
			clearRow(i);
		}
	}

	public static IntMatrix transform(IntMatrix source) {
		IntMatrix target = new IntMatrix(source.getWidth(), source.getHeight());
		for (int i = 0; i < target.getHeight(); i++)
			for (int j = 0; j < target.getWidth(); j++)
				target.set(i, j, source.get(source.getHeight() - j - 1, i));
		return target;
	}

	private void clearRow(int row) {
		for (int j = 0; j < _width; j++) {
			_data[row][j] = 0;
		}
	}
} 