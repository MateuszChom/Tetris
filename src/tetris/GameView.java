package tetris;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.BoxLayout;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

@SuppressWarnings("serial")
class GameView extends JPanel {

	private TetrisModel _model = null;
	private GridPanel _panel;
	private GridPanel _previewPanel;
	private ScorePanel _scorePanel;
	private GameFrame _gameFrame;
	private JPanel _box3;
	private TextPanel _level;

	GameView(GameFrame gameFrame) {

		this.setLayout(new BorderLayout(10, 0));

		_gameFrame = gameFrame;
		_panel = new GridPanel(Config.boardDimension[0],
				Config.boardDimension[1], true, Config.interfaceColors[0],
				Config.boardSize);
		_previewPanel = new GridPanel(4, 4, true, Config.interfaceColors[1],
				Config.boardSize);
		_scorePanel = new ScorePanel(Config.interfaceColors[2],
				Config.textSize[1]);
		TextPanel scoreText = new TextPanel(Config.language[26] + ":",
				Config.interfaceColors[2], Config.textSize[0]);
		TextPanel previewText = new TextPanel(Config.language[27] + ":",
				Config.interfaceColors[2], Config.textSize[0]);
		_level = new TextPanel(Config.language[28] + " 1",
				Config.interfaceColors[2], Config.textSize[2]);

		int size = Config.boardSize * 4;
		_scorePanel.setPreferredSize(new Dimension(size, Config.textSize[1]));
		scoreText.setPreferredSize(new Dimension(size,
				Config.textSize[0] * 3 / 2));
		previewText.setPreferredSize(new Dimension(size,
				Config.textSize[0] * 3 / 2));
		_previewPanel.setPreferredSize(new Dimension(size, size));

		JPanel box = new JPanel();
		JPanel box2 = new JPanel();
		_box3 = new JPanel();
		JPanel all = new JPanel();
		box.setLayout(new BoxLayout(box, BoxLayout.Y_AXIS));
		box2.setLayout(new BoxLayout(box2, BoxLayout.Y_AXIS));
		_box3.setLayout(new GridLayout(2, 1));

		box.add(scoreText);
		box.add(_scorePanel);
		box2.add(previewText);
		box2.add(_previewPanel);
		_box3.add(_level);
		all.setLayout(new BorderLayout());
		all.add(box, BorderLayout.NORTH);
		all.add(box2, BorderLayout.SOUTH);
		all.add(_box3, BorderLayout.CENTER);
		add(all, BorderLayout.EAST);
		add(_panel, BorderLayout.CENTER);

		setupKeyboard();
	}
	
	public boolean start() {
		if (_model == null) {
			_model = new TetrisModel(Config.boardDimension[0],
					Config.boardDimension[1], this, _gameFrame.getOnline());
		}
		if (_model.isStopped()) {
			TextField text = new TextField();
			String name = JOptionPane.showInputDialog(text, Config.language[29]
					+ ":", Config.language[30], JOptionPane.PLAIN_MESSAGE);
			_model.setName(name);
			_model.start();
			return true;
		} else {
			_model.stop();
			return false;
		}
	}

	public int pause() {
		if (_model == null)
			return 0;
		if (_model.isStopped())
			return 0;
		if (_model.isPaused()) {
			_model.resume();
			return 1;
		} else {
			_model.pause();
			return 2;
		}
	}

	public void levelChanged() {
		_level.setText(Config.language[28] + " " + (_model.getLevel() + 1));
		int[] all = new int[Config.boardDimension[0]];
		for (int i = 0; i < Config.boardDimension[0]; i++)
			all[i] = i;
		_panel.blink(all, Config.boardDimension[0]);
	}
	
	public void scoreChanged() {
		_scorePanel.setScore(_model.getScore());
	}
	
	public void mapChanged() {
		_panel.setModel(_model.getViewMap());
	}
	
	public void previewChanged() {
		_previewPanel.setModel(_model.getPreviewShape());
	}

	public void gameOver() {
		Sound.playSound("Sounds/game over.wav");
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JOptionPane.showMessageDialog(GameView.this,
						Config.language[31] + " " + _model.getScore(),
						Config.language[32], JOptionPane.INFORMATION_MESSAGE);
				_gameFrame.gameOver();
			}
		});
	}

	public void rowsToDelete(final int row[], final int count) {
		_panel.blink(row, count);
	}
	
	private void setupKeyboard() {
		InputMap input = this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		input.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "left");
		input.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "right");
		input.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "up");
		input.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "down");
		input.put(KeyStroke.getKeyStroke(KeyEvent.VK_P, 0), "pause");
		input.put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0), "space");
		input.put(KeyStroke.getKeyStroke(KeyEvent.VK_U, ActionEvent.CTRL_MASK),
				"cheat");
		input.put(KeyStroke.getKeyStroke(KeyEvent.VK_N, 0), "next");
		ActionMap action = this.getActionMap();
		action.put("cheat", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				if (_model == null)
					return;
				if (_model.cheat())
					return;
				TextPanel cheater = new TextPanel("CHEAT MODE",
						Config.interfaceColors[3], Config.textSize[2]);
				_box3.add(cheater);
				revalidate();

			}
		});
		action.put("next", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				if (_model == null)
					return;
				_model.next();
			}
		});
		action.put("left", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				if (_model == null)
					return;
				_model.left();
			}
		});

		action.put("right", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				if (_model == null)
					return;
				_model.right();
			}
		});
		action.put("up", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				if (_model == null)
					return;
				_model.rotate();
			}
		});
		action.put("down", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				if (_model == null)
					return;
				_model.down();
			}
		});
		action.put("space", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				if (_model == null)
					return;
				_model.downBottom();
			}
		});
		action.put("pause", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				_gameFrame.pause();
			}
		});

	}
} 