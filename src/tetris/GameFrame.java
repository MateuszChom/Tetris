package tetris;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;

@SuppressWarnings("serial")
class GameFrame extends JFrame {

	private boolean _online;
	private JButton _newGameButton;
	private JButton _pauseButton;
	private GameView _gameView;
	private ImageIcon _stopIcon;
	private JMenuItem _newGame;
	private ImageIcon _newGameIcon;
	private ImageIcon _pauseIcon;
	private ImageIcon _highScoresIcon;
	private ImageIcon _resumeIcon;

	private ImageIcon setIcon(String path) {
		try {
			ImageIcon icon = new ImageIcon(path);
			return icon;
		} catch (java.lang.NullPointerException e) {
			return null;
		}
	}

	GameFrame(boolean online) {

		super("Tetris");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout(new BorderLayout());
		_online = online;

		ImageIcon icon = setIcon("Icons/icon.png");
		ImageIcon exitIcon = setIcon("Icons/exit.png");
		ImageIcon rulesIcon = setIcon("Icons/rules.png");
		ImageIcon creatorsIcon = setIcon("Icons/creators.png");
		_stopIcon = setIcon("Icons/stop.png");
		_newGameIcon = setIcon("Icons/new game.png");
		_pauseIcon = setIcon("Icons/pause.png");
		_resumeIcon = setIcon("Icons/resume.png");
		_highScoresIcon = setIcon("Icons/high scores.png");
		if (icon != null)
			setIconImage(icon.getImage());

		JMenuBar menuBar = new JMenuBar();
		JMenu file = new JMenu(Config.language[14]);
		JMenu about = new JMenu(Config.language[15]);
		_newGame = new JMenuItem(Config.language[16], _newGameIcon);
		JMenuItem highScores = new JMenuItem(Config.language[17],
				_highScoresIcon);
		JMenuItem exit = new JMenuItem(Config.language[18], exitIcon);
		JMenuItem creators = new JMenuItem(Config.language[19], creatorsIcon);
		JMenuItem rules = new JMenuItem(Config.language[20], rulesIcon);
		exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W,
				ActionEvent.CTRL_MASK));

		file.add(_newGame);
		if (_online)
			file.add(highScores);
		file.addSeparator();
		file.add(exit);
		about.add(creators);
		about.add(rules);
		menuBar.add(file);
		menuBar.add(about);
		setJMenuBar(menuBar);

		_newGameButton = new JButton(_newGameIcon);
		JButton highScoresButton = new JButton(_highScoresIcon);
		JButton exitButton = new JButton(exitIcon);
		JButton creatorsButton = new JButton(creatorsIcon);
		JButton rulesButton = new JButton(rulesIcon);
		_pauseButton = new JButton(_pauseIcon);

		final JToolBar toolbar = new JToolBar();
		if (_online)
			toolbar.add(highScoresButton);
		toolbar.add(exitButton);
		toolbar.addSeparator();
		toolbar.add(creatorsButton);
		toolbar.add(rulesButton);
		toolbar.addSeparator();
		toolbar.add(_newGameButton);
		toolbar.add(_pauseButton);

		_newGameButton.setFocusable(false);
		highScoresButton.setFocusable(false);
		exitButton.setFocusable(false);
		creatorsButton.setFocusable(false);
		rulesButton.setFocusable(false);
		_pauseButton.setFocusable(false);

		_newGameButton.setToolTipText(Config.language[16]);
		highScoresButton.setToolTipText(Config.language[17]);
		exitButton.setToolTipText(Config.language[18]);
		creatorsButton.setToolTipText(Config.language[19]);
		rulesButton.setToolTipText(Config.language[20]);
		_pauseButton.setToolTipText(Config.language[33]);

		_gameView = new GameView(this);

		add(toolbar, BorderLayout.NORTH);
		add(_gameView);
		pack();

		ActionListener exitEvent = new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				System.exit(0);
			}
		};

		exit.addActionListener(exitEvent);
		exitButton.addActionListener(exitEvent);

		ActionListener highScoresEvent = new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				String[] scores = Client.getHighScores(Config.ip,
						Integer.toString(Config.port));
				if (scores == null) {
					JOptionPane.showMessageDialog(null, Config.language[23]
							+ "!", Config.language[9],
							JOptionPane.ERROR_MESSAGE);
				} else {
					String[] message = new String[10];
					for (int i = 0; i < 10; i++)
						message[i] = (i + 1) + ".   " + scores[2 * i] + "   "
								+ scores[2 * i + 1];
					JOptionPane.showMessageDialog(null, message,
							Config.language[17], JOptionPane.PLAIN_MESSAGE,
							_highScoresIcon);
				}
			}
		};

		highScores.addActionListener(highScoresEvent);
		highScoresButton.addActionListener(highScoresEvent);

		ActionListener creatorsEvent = new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				JOptionPane.showMessageDialog(null,
						"Mateusz Chomiczewski\nArtsiom Baranouski",
						Config.language[19], JOptionPane.INFORMATION_MESSAGE);
			}
		};

		creators.addActionListener(creatorsEvent);
		creatorsButton.addActionListener(creatorsEvent);

		ActionListener rulesEvent = new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				JOptionPane.showMessageDialog(null, Config.language[24] + "\n"
						+ Config.language[25], Config.language[20],
						JOptionPane.INFORMATION_MESSAGE);
			}
		};

		rules.addActionListener(rulesEvent);
		rulesButton.addActionListener(rulesEvent);

		ActionListener newGameEvent = new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				if (_gameView.start()) {
					Sound.playSound("Sounds/new game.wav");
					_newGameButton.setIcon(_stopIcon);
					_newGameButton.setToolTipText(Config.language[34]);
					_newGame.setVisible(false);
				} else {
					Sound.playSound("Sounds/stop.wav");
					_newGameButton.setIcon(_newGameIcon);
					_newGameButton.setToolTipText(Config.language[16]);
					_newGame.setVisible(true);
				}
				_pauseButton.setIcon(_pauseIcon);
				_pauseButton.setToolTipText(Config.language[33]);
			}
		};

		_newGame.addActionListener(newGameEvent);
		_newGameButton.addActionListener(newGameEvent);

		ActionListener pauseEvent = new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				int temp = _gameView.pause();
				if (temp == 0)
					return;
				Sound.playSound("Sounds/pause.wav");
				if (temp == 1) {
					_pauseButton.setIcon(_pauseIcon);
					_pauseButton.setToolTipText(Config.language[33]);
				} else {
					_pauseButton.setIcon(_resumeIcon);
					_pauseButton.setToolTipText(Config.language[35]);
				}
			}
		};

		_pauseButton.addActionListener(pauseEvent);
	}

	public void gameOver() {
		_newGameButton.setIcon(_newGameIcon);
		_newGame.setVisible(true);
		_pauseButton.setIcon(_pauseIcon);
		_pauseButton.setToolTipText(Config.language[33]);
		_newGameButton.setToolTipText(Config.language[16]);
	}

	public void pause() {
		_pauseButton.doClick();
	}

	public boolean getOnline() {
		return _online;
	}
} 