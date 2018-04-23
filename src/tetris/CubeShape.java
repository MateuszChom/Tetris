package tetris;

class CubeShape {

	private int _direction;
	private int _shape;
	private static final int _shapeCount = 7;
	private static java.util.Random _random = new java.util.Random();
	private static IntMatrix _shapes[][] = new IntMatrix[_shapeCount][4];

	static {
		IntMatrix I = new IntMatrix(1, 4, 1);
		_shapes[0] = buildSeriesShape(I);

		IntMatrix T = new IntMatrix(3, 2, 2);
		T.set(0, 1, 0);
		T.set(2, 1, 0);
		_shapes[1] = buildSeriesShape(T);

		IntMatrix O = new IntMatrix(2, 2, 3);
		_shapes[2] = buildSeriesShape(O);

		IntMatrix L = new IntMatrix(3, 2, 4);
		L.set(0, 1, 0);
		L.set(1, 1, 0);
		_shapes[3] = buildSeriesShape(L);

		IntMatrix J = new IntMatrix(3, 2, 5);
		J.set(0, 0, 0);
		J.set(1, 0, 0);
		_shapes[4] = buildSeriesShape(J);

		IntMatrix S = new IntMatrix(3, 2, 6);
		S.set(0, 1, 0);
		S.set(2, 0, 0);
		_shapes[5] = buildSeriesShape(S);

		IntMatrix Z = new IntMatrix(3, 2, 7);
		Z.set(0, 0, 0);
		Z.set(2, 1, 0);
		_shapes[6] = buildSeriesShape(Z);
	}
	
	public CubeShape() {
		_shape = _random.nextInt(_shapeCount);
		_direction = _random.nextInt(4);
	}

	public CubeShape(int shape) {
		_shape = shape % _shapeCount;
		_direction = _random.nextInt(4);
	}

	public boolean rotate(IntMatrix map, Position pos) {
		int next = (_direction + 1) % 4;
		IntMatrix currShape = getShape();
		int tryCount = currShape.getHeight() - currShape.getWidth() + 1;
		if (tryCount <= 0)
			tryCount = 1;
		Position temp = new Position(pos);
		for (int i = 0; i < tryCount; i++) {
			if (map.contains(_shapes[_shape][next], temp)) {
				_direction = next;
				pos.setColumn(temp.getColumn());
				return true;
			}
			temp.setColumn(temp.getColumn() - 1);
		}
		return false;
	}

	public IntMatrix getShape() {
		return _shapes[_shape][_direction];
	}

	public int getShapeInt() {
		return _shape;
	}

	private static IntMatrix[] buildSeriesShape(IntMatrix initial) {
		IntMatrix[] shapes = new IntMatrix[4];
		shapes[0] = new IntMatrix(initial);
		shapes[1] = IntMatrix.transform(shapes[0]);
		shapes[2] = IntMatrix.transform(shapes[1]);
		shapes[3] = IntMatrix.transform(shapes[2]);
		return shapes;
	}
} 