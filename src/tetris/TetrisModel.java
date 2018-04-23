package tetris;

class TetrisModel implements Runnable {

	private boolean _cheat = true;
	private IntMatrix _map;
	private IntMatrix _viewMap;
	private ActiveCube _cube;
	private CubeShape _nextShape;
	private GameView _view;
	private boolean _online;
	private volatile boolean _paused = false;
	private volatile boolean _stopped = true;
	private volatile int _score;
	private String _name = null;
	private int _level;
	private Level _current;
	private Level _next;
	private int _time;
	private int _levelUp;

	public TetrisModel(int height, int width, GameView view, boolean online) {
		_view = view;
		_online = online;
		_map = new IntMatrix(height, width);
		_viewMap = new IntMatrix(height, width);
		_cube = new ActiveCube(_map);
		_nextShape = new CubeShape();
	}

	public boolean cheat() {
		if (_cheat) {
			_cheat = false;
			return false;
		}
		return true;
	}

	public void next() {
		if (_cheat)
			return;
		_nextShape = new CubeShape(_nextShape.getShapeInt() + 1);
		_view.previewChanged();
	}
	
	public void setName(String name) {
		_name = name;
	}

	public void start() {
		_stopped = false;
		_map.clear();
		_cube.next(getNextCube());
		_score = 0;
		_level = 0;
		_current = Config.level;
		if (_online)
			_next = new Level(1);
		_time = _current.getTime();
		_levelUp = _current.getLevelUp();
		_map = _current.getLevel();
		_cube = new ActiveCube(_map);
		_cube.next(getNextCube());
		update();
		_view.levelChanged();
		_view.scoreChanged();
		Thread t = new Thread(this);
		t.start();

	}

	public synchronized void stop() {
		_stopped = true;
		resume();
		_map.clear();
		update();
	}

	public synchronized boolean isStopped() {
		return _stopped;
	}

	public synchronized void pause() {
		_paused = true;
	}

	public synchronized void resume() {
		_paused = false;
		notify();
	}
	
	public synchronized boolean isPaused() {
		return _paused;
	}
	
	public void left() {
		if (isStoppedOrPaused())
			return;
		if (_cube.left())
			update();
	}

	public void right() {
		if (isStoppedOrPaused())
			return;
		if (_cube.right())
			update();
	}
	
	public void rotate() {
		if (isStoppedOrPaused())
			return;
		if (_cube.rotate())
			update();
	}

	public void down() {
		if (isStoppedOrPaused())
			return;
		if (_cube.down())
			update();
	}

	public void downBottom() {
		if (isStoppedOrPaused())
			return;
		while (_cube.down())
			update();
	}

	public IntMatrix getViewMap() {
		return _viewMap;
	}

	public int getScore() {
		return _score;
	}

	public int getLevel() {
		return _level;
	}

	public IntMatrix getPreviewShape() {
		return _nextShape.getShape();
	}

	public void run() {
		while (!_stopped) {
			maybePause();
			try {
				Thread.sleep(_time);
			} catch (InterruptedException e) {
			}
			maybePause();
			synchronized (this) {
				if (_cube.down()) {
					update();
				} else {
					accept();
					_cube.next(getNextCube());
					update();
					if (_cube.getPos().getRow() < 0) {
						_stopped = true;
						_paused = false;
						_view.gameOver();
						if (_cheat & _online)
							Client.sendScore(_name, _score);
						break;
					}
				}
			}
		}
	}

	private void update() {
		for (int i = 0; i < _map.getHeight(); i++)
			for (int j = 0; j < _map.getWidth(); j++)
				_viewMap.set(i, j, _map.get(i, j));
		if (_stopped) {
			_view.mapChanged();
			return;
		}

		IntMatrix shape = _cube.getShape();
		Position pos = _cube.getPos();
		int start = 0;
		if (pos.getRow() < 0)
			start = Math.abs(pos.getRow());
		for (int i = start; i < shape.getHeight(); i++)
			for (int j = 0; j < shape.getWidth(); j++) {
				if (shape.get(i, j) > 0)
					_viewMap.set(i + pos.getRow(), j + pos.getColumn(),
							shape.get(i, j));
			}
		_view.mapChanged();
	}

	private synchronized void maybePause() {
		try {
			while (_paused)
				wait();
		} catch (InterruptedException e) {
		}
	}

	private void accept() {
		_map.add(_cube.getShape(), _cube.getPos());
		int count = 0;
		int[] todelete = new int[_map.getHeight()];
		for (int i = 0; i < _map.getHeight(); i++) {
			if (_map.isRowOccupied(i)) {
				count++;
				todelete[count - 1] = i;
				_map.deleteRow(i);
			}
		}
		if (count > 0) {
			_score += count * (_current.getScore());
			if (_online) {
				if (_levelUp != 0 & _score >= _levelUp) {
					Sound.playSound("Sounds/level up.wav");
					if (_next.getError() == true | _next.getLevel() == null)
						_online = false;
					else {
						_level++;
						_current = _next;
						_time = _current.getTime();
						_levelUp = _current.getLevelUp();
						_map.clear();
						_map = _current.getLevel();
						_cube = new ActiveCube(_map);
						_view.levelChanged();
						_view.scoreChanged();
						if (_current.getLevelUp() != 0)
							_next = new Level(_level + 1);
						return;
					}
				}
			}
			Sound.playSound("Sounds/blink.wav");
			_view.rowsToDelete(todelete, count);
			_view.scoreChanged();
		}
	}

	private synchronized boolean isStoppedOrPaused() {
		return (_stopped || _paused);
	}

	private CubeShape getNextCube() {
		CubeShape tmp = _nextShape;
		_nextShape = new CubeShape();
		_view.previewChanged();
		return tmp;
	}
} 