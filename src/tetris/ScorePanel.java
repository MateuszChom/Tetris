package tetris;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.text.DecimalFormat;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class ScorePanel extends JPanel {

	private int _score;
	private static final int _numberCount = 6;
	private DecimalFormat _format;
	private Color _color;
	private int _size;

	public ScorePanel(Color color, int size) {
		_color = color;
		_size = size;
		_format = new DecimalFormat("######");
		_format.setMaximumIntegerDigits(_numberCount);
		_format.setMinimumIntegerDigits(_numberCount);
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		this.setDoubleBuffered(true);
		String string = _format.format(_score);
		int w = this.getWidth();
		int h = this.getHeight();
		g.setColor(_color);
		g.setFont(new Font("Courier", Font.BOLD, _size));
		FontMetrics fm = g.getFontMetrics();
		int fw = fm.stringWidth(string);
		int fh = fm.getAscent();
		g.drawString(string, w / 2 - fw / 2, h / 2 + fh / 2);
	}

	public void setScore(int score) {
		_score = score;
		repaint();
	}
} 