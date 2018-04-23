package tetris;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class Level extends Thread {

	private IntMatrix _level;
	private int _time;
	private int _score;
	private int _levelUp;
	private int _index;
	private boolean _error;

	public Level() {
		_level = new IntMatrix(Config.getDefaulBoardDimension()[0],
				Config.getDefaulBoardDimension()[1], 0);
		_time = 500;
		_score = 1;
		_levelUp = 0;
		_index = 0;
		_error = false;
	}
	
	public Level(String[] data) {
		setLevel(data);
		_error = false;
	}
	
	public Level(int index) {
		_index = index;
		_level = null;
		_error = false;
		start();
	}

	public void run() {

		Socket socket = null;

		try {
			try {
				socket = new Socket(Config.ip, Config.port);
				socket.setSoTimeout(Config.timeout);
			} catch (IllegalArgumentException e) {
				_error = true;
			}
			OutputStream os = socket.getOutputStream();
			PrintWriter pw = new PrintWriter(os, true);
			InputStream is = socket.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			pw.println(Protocol.GETLEVEL);
			pw.println(_index + 1);
			pw.println(Config.boardDimension[0]);
			pw.println(Config.boardDimension[1]);
			String answer[] = new String[Config.boardDimension[0] + 3];

			for (int i = 0; i < Config.boardDimension[0] + 3; i++) {
				answer[i] = br.readLine();
				if (answer[i].equals(Protocol.ERROR))
					throw new IOException("server error");
			}
			setLevel(answer);
			socket.close();
		} catch (IOException e) {
			_error = true;
		}
	}

	public IntMatrix getLevel() {
		return _level;
	}

	public int getTime() {
		return _time;
	}

	public int getScore() {
		return _score;
	}

	public int getLevelUp() {
		return _levelUp;
	}

	public boolean getError() {
		return _error;
	}

	private void setLevel(String[] data) {
		_level = new IntMatrix(Config.boardDimension[0],
				Config.boardDimension[1], 0);
		for (int i = 0; i < Config.boardDimension[0]; i++) {
			for (int j = 0; j < Config.boardDimension[1]; j++) {
				_level.set(i, j, 9);

				switch (data[i + 3].charAt(j)) {
				case 'I': {
					_level.set(i, j, 1);
					break;
				}
				case 'T': {
					_level.set(i, j, 2);
					break;
				}
				case 'O': {
					_level.set(i, j, 3);
					break;
				}
				case 'L': {
					_level.set(i, j, 4);
					break;
				}
				case 'J': {
					_level.set(i, j, 5);
					break;
				}
				case 'S': {
					_level.set(i, j, 6);
					break;
				}
				case 'Z': {
					_level.set(i, j, 7);
					break;
				}
				default: {
					_level.set(i, j, 0);
					break;
				}
				}
			}
		}
		_time = Integer.parseInt(data[0]);
		_score = Integer.parseInt(data[2]);
		_levelUp = Integer.parseInt(data[1]);
	}
}
 