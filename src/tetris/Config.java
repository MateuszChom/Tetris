package tetris;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Scanner;
import javax.swing.JOptionPane;

public class Config {

	public static String[] languageList;
	public static int[] comboBoxDimension;
	public static int[] labelSize;
	public static int[] buttonDimension;
	public static int[] textFieldDimension;
	public static Color[] color;
	public static int timeout;
	public static String[] language;
	public static Color[] tetriminoColors;
	public static Color[] interfaceColors;
	public static int[] textSize;
	public static int[] boardDimension;
	public static int boardSize;
	public static String ip;
	public static int port;
	public static Level level;

	private static String[] _defaultLanguageList = { "Default" };
	private static int[] _defaultComboBoxDimension = { 170, 30 };
	private static int[] _defaultLabelSize = { 12, 11 };
	private static int[] _defaultButtonDimension = { 85, 25 };
	private static int[] _defaultTextFieldDimension = { 12, 25 };
	private static Color[] _defaultColor = { new Color(0x6666BB),
			new Color(0x004400), new Color(0x002288), new Color(0x000000),
			new Color(0x004499), new Color(0x008000), new Color(0xFF0000),
			new Color(0x808000) };
	private static String[] _defaultIp = { "localhost", "1000" };
	private static int _defaultTimeout = 3000;
	private static Color[] _defaultTetriminoColors = { new Color(0xff0000),
			new Color(0x999999), new Color(0x00ffff), new Color(0xffff00),
			new Color(0xff00ff), new Color(0x0000ff), new Color(0x00ff00),
			new Color(0x364135), new Color(0x000000), };
	private static Color[] _defaultInterfaceColors = { new Color(0xffffff),
			new Color(0xffffff), new Color(0x000000), new Color(0xff0000) };
	private static int[] _defaultTextSize = { 12, 32, 16 };
	private static int _defaultBoardSize = 30;
	private static int[] _defaultBoardDimension = { 20, 10 };
	private static Level defaultLevel;
	private static String[] _defaultLanguage = { "Select Language",
			"Network Settings", "Server IP", "Port", "Start", "Check", "Exit",
			"ERROR", "CHECKED", "Error",
			"File containig selected language does not exist or is damaged",
			"Waiting For Server Response",
			"Connection with server could not be established",
			"Would you like to play offline", "File", "About", "New Game",
			"High Scores", "Exit", "Creators", "Rules",
			"Server sent incorrect data", "Server sent error message",
			"Server not available", "To see the rules go to:",
			"http://en.wikipedia.org/wiki/Tetris", "Score", "Preview", "Level",
			"Name", "Type your name", "Your score is", "Game Over", "Pause",
			"Stop", "Resume" };

	static {
		languageList = setLanguageList();
		comboBoxDimension = setInt("#ComboBox Dimension", 2,
				_defaultComboBoxDimension);
		labelSize = setInt("#Label Size", 2, _defaultLabelSize);
		buttonDimension = setInt("#Button Dimension", 2,
				_defaultButtonDimension);
		textFieldDimension = setInt("#Text Field Dimension", 2,
				_defaultTextFieldDimension);
		color = setColor("#Color", 8, _defaultColor);
		timeout = setValue("#Socket Timeout", _defaultTimeout);
		defaultLevel = new Level();
	}

	public static int setConfig(String tempIp, String tempPort, String[] lang) {
		try {
			tetriminoColors = Client.setColor(tempIp, tempPort, 9,
					Protocol.GETTETRIMINOCOLORS);
			interfaceColors = Client.setColor(tempIp, tempPort, 4,
					Protocol.GETINTERFACECOLORS);
			textSize = Client.setInt(tempIp, tempPort, 3, Protocol.GETTEXTSIZE);
			boardDimension = Client.setInt(tempIp, tempPort, 2,
					Protocol.GETBOARDDIMENSION);
			boardSize = (Client.setInt(tempIp, tempPort, 1,
					Protocol.GETBOARDSIZE))[0];
			level = Client.getLevel(tempIp, tempPort);
			language = lang;
			ip = tempIp;
			port = Integer.parseInt(tempPort);

		} catch (NumberFormatException | StringIndexOutOfBoundsException e) {
			if (JOptionPane.showConfirmDialog(null, lang[21] + "!" + "\n"
					+ lang[13] + "?", lang[9] + "!", JOptionPane.YES_NO_OPTION,
					JOptionPane.WARNING_MESSAGE) == 1)
				return 0;
			else {
				tetriminoColors = _defaultTetriminoColors;
				interfaceColors = _defaultInterfaceColors;
				textSize = _defaultTextSize;
				boardDimension = _defaultBoardDimension;
				boardSize = _defaultBoardSize;
				level = defaultLevel;
				language = lang;
				return 2;
			}
		} catch (IOException e) {
			if (e.getMessage().equals("server error")) {
				if (JOptionPane.showConfirmDialog(null, lang[22] + "!" + "\n"
						+ lang[13] + "?", lang[9] + "!",
						JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == 1)
					return 0;
				else {
					tetriminoColors = _defaultTetriminoColors;
					interfaceColors = _defaultInterfaceColors;
					textSize = _defaultTextSize;
					boardDimension = _defaultBoardDimension;
					boardSize = _defaultBoardSize;
					level = defaultLevel;
					language = lang;
					return 2;
				}
			}
			if (JOptionPane.showConfirmDialog(null, lang[12] + "!" + "\n"
					+ lang[13] + "?", lang[9] + "!", JOptionPane.YES_NO_OPTION,
					JOptionPane.WARNING_MESSAGE) == 1)
				return 0;
			else {
				tetriminoColors = _defaultTetriminoColors;
				interfaceColors = _defaultInterfaceColors;
				textSize = _defaultTextSize;
				boardDimension = _defaultBoardDimension;
				boardSize = _defaultBoardSize;
				level = defaultLevel;
				language = lang;
				return 2;
			}
		}
		return 1;
	}

	/**
	 * Zamiana Stringu wczytanego z pliku na int
	 * 
	 * @param parameter
	 * @param defaultValue
	 */

	private static int setValue(String parameter, int defaultValue) {
		String temp = fromFile(parameter);
		int tempValue;
		if (temp == null)
			return defaultValue;
		try {
			tempValue = Integer.parseInt(temp);
		} catch (NumberFormatException e) {
			return defaultValue;
		}
		return tempValue;
	}

	/**
	 * Zamiana Stringu wczytanego z pliku na tablice int�w
	 * 
	 * @param parameter
	 * @param count
	 * @param defaultInt
	 */

	private static int[] setInt(String parameter, int count, int[] defaultInt) {
		String temp = fromFile(parameter);
		if (temp == null)
			return defaultInt;
		try {
			int[] space = new int[count - 1];
			space[0] = temp.indexOf(' ');
			for (int i = 0; i < count - 2; i++)
				space[i + 1] = temp.indexOf(' ', space[i] + 1);
			int length = temp.length();
			String[] tempString = new String[count];
			tempString[0] = temp.substring(0, space[0]);
			for (int i = 1; i < count - 1; i++)
				tempString[i] = temp.substring(space[i - 1] + 1, space[i]);
			tempString[count - 1] = temp
					.substring(space[count - 2] + 1, length);
			int[] tempInt = new int[count];
			for (int i = 0; i < count; i++)
				tempInt[i] = Integer.parseInt(tempString[i]);
			return tempInt;
		} catch (Exception e) {
			return defaultInt;
		}
	}

	/**
	 * Zamiana Stringu wczytanego z pliku na tablice kolor�w
	 * 
	 * @param parameter
	 * @param count
	 * @param defaultColor
	 */

	private static Color[] setColor(String parameter, int count,
			Color[] defaultColor) {
		String temp = fromFile(parameter);
		if (temp == null)
			return defaultColor;
		try {
			int[] space = new int[count - 1];
			space[0] = temp.indexOf(' ');
			for (int i = 0; i < count - 2; i++)
				space[i + 1] = temp.indexOf(' ', space[i] + 1);
			int length = temp.length();
			String[] tempColor = new String[count];
			tempColor[0] = temp.substring(2, space[0]);
			for (int i = 1; i < count - 1; i++)
				tempColor[i] = temp.substring(space[i - 1] + 3, space[i]);
			tempColor[count - 1] = temp.substring(space[count - 2] + 3, length);
			Color[] color = new Color[count];
			for (int i = 0; i < count; i++)
				color[i] = new Color(Integer.parseInt(tempColor[i], 16));
			return color;
		} catch (Exception e) {
			return defaultColor;
		}
	}

	public static String[] getLanguage(int index) {
		if (index == 0)
			return _defaultLanguage;
		File file = new File("Language Pack/" + languageList[index] + ".txt");
		Scanner in = null;
		int count = 36;
		try {
			in = new Scanner(file);
		} catch (FileNotFoundException e) {
			return null;
		}
		String[] temp = new String[count];
		try {
			for (int i = 0; i < count; i++) {
				temp[i] = in.nextLine();
			}
		} catch (NoSuchElementException e) {
			try {
				in.close();
			} catch (IllegalStateException ise) {
			}
			return null;
		} catch (IllegalStateException e) {
			try {
				in.close();
			} catch (IllegalStateException ise) {
			}
			return null;
		}
		try {
			in.close();
		} catch (IllegalStateException ise) {
		}
		return temp;
	}

	public static String[] setIp() {
		String temp = fromFile("#Network");
		if (temp == null)
			return _defaultIp;
		try {
			int space = temp.indexOf(' ');
			int length = temp.length();
			String[] tempIp = new String[2];
			tempIp[0] = temp.substring(0, space);
			tempIp[1] = temp.substring(space + 1, length);
			Integer.parseInt(tempIp[1]);
			return tempIp;
		} catch (Exception e) {
			return _defaultIp;
		}
	}

	private static String[] setLanguageList() {
		File file = new File("Language Pack/list.txt");
		Scanner in = null;
		try {
			in = new Scanner(file);
		} catch (FileNotFoundException e) {
			return _defaultLanguageList;
		}
		String[] temp = new String[99];
		int i = 1;
		temp[0] = "Default";
		try {
			in.nextLine();
			while (true) {
				temp[i] = in.nextLine();
				i++;
			}
		} catch (NoSuchElementException e) {
			String[] lang = new String[i];
			for (int j = 0; j < i; j++)
				lang[j] = temp[j];
			try {
				in.close();
			} catch (IllegalStateException ise) {
			}
			return lang;
		} catch (IllegalStateException e) {
			try {
				in.close();
			} catch (IllegalStateException ise) {
			}
			return _defaultLanguageList;
		}
	}

	private static String fromFile(String description) {
		File file = new File("config.txt");
		Scanner in = null;
		try {
			in = new Scanner(file);
		} catch (FileNotFoundException e) {
			return null;
		}
		String parameter = null;
		try {
			while (true) {
				parameter = in.nextLine();
				if (parameter.equals(description)) {
					parameter = in.nextLine();
					break;
				}
			}
		} catch (NoSuchElementException e) {
			try {
				in.close();
			} catch (IllegalStateException ise) {
			}
			return null;
		} catch (IllegalStateException e) {
			try {
				in.close();
			} catch (IllegalStateException ise) {
			}
			return null;
		}
		try {
			in.close();
		} catch (IllegalStateException ise) {
		}
		return parameter;
	}

	public static int[] getDefaulBoardDimension() {
		return _defaultBoardDimension;
	}
}
 