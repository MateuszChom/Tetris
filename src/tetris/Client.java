package tetris;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {

	public static Color[] setColor(String ip, String port, int count,
			String protocol) throws NumberFormatException, IOException {
		String[] temp = connection(protocol, ip, port);
		Color[] color = new Color[count];
		for (int i = 0; i < count; i++)
			color[i] = new Color(Integer.parseInt(temp[i], 16));
		return color;
	}

	public static int[] setInt(String ip, String port, int count,
			String protocol) throws NumberFormatException, IOException {
		String[] temp = connection(protocol, ip, port);
		int[] value = new int[count];
		for (int i = 0; i < count; i++)
			value[i] = Integer.parseInt(temp[i]);
		return value;
	}

	public static String[] getHighScores(String ip, String port) {
		String[] answer;
		try {
			answer = connection(Protocol.GETHIGHSCORES, ip, port);
		} catch (NumberFormatException e) {
			return null;
		} catch (IOException e) {
			return null;
		}
		return answer;
	}
	
	public static Level getLevel(String ip, String port) {
		String[] answer;
		try {
			answer = connection(Protocol.GETLEVEL, ip, port);
		} catch (NumberFormatException e) {
			return null;
		} catch (IOException e) {
			return null;
		}
		return new Level(answer);
	}

	public static void sendScore(String name, int score) {

		Socket socket;
		try {
			socket = new Socket(Config.ip, Config.port);
			socket.setSoTimeout(Config.timeout);
			OutputStream os = socket.getOutputStream();
			PrintWriter pw = new PrintWriter(os, true);
			pw.println(Protocol.SENDINGSCORE);
			pw.println(name);
			pw.println(Integer.toString(score));
		} catch (UnknownHostException e) {
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static String[] connection(String command, String ip, String port)
			throws NumberFormatException, IOException {

		Socket socket = new Socket(ip, Integer.parseInt(port));
		socket.setSoTimeout(Config.timeout);
		OutputStream os = socket.getOutputStream();
		PrintWriter pw = new PrintWriter(os, true);
		InputStream is = socket.getInputStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		pw.println(command);

		switch (command) {

		case Protocol.GETTETRIMINOCOLORS: {
			return getLines(9, br, socket);
		}

		case Protocol.GETINTERFACECOLORS: {
			return getLines(4, br, socket);
		}

		case Protocol.GETTEXTSIZE: {
			return getLines(3, br, socket);
		}

		case Protocol.GETBOARDDIMENSION: {
			return getLines(2, br, socket);
		}

		case Protocol.GETBOARDSIZE: {
			return getLines(1, br, socket);
		}

		case Protocol.GETHIGHSCORES: {
			return getLines(20, br, socket);
		}

		case Protocol.GETLEVEL: {
			pw.println("1");
			pw.println(Config.boardDimension[0]);
			pw.println(Config.boardDimension[1]);
			return getLines(3 + Config.boardDimension[0], br, socket);
		}

		default: {
			socket.close();
			throw new IOException("unknown command");
		}
		}

	}

	private static String[] getLines(int count, BufferedReader br, Socket socket)
			throws IOException {
		String[] answer = new String[count];
		for (int i = 0; i < count; i++) {
			answer[i] = br.readLine();
			if (answer[i].equals(Protocol.ERROR))
				throw new IOException("server error");
		}
		socket.close();
		return answer;
	}
}  