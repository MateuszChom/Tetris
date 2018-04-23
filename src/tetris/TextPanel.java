package tetris;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class TextPanel extends JPanel {

	private String _text;
	private Color _color;
	private int _size;

	public TextPanel(String text, Color color, int size) {
		_text = text;
		_color = color;
		_size = size;
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		this.setDoubleBuffered(true);
		int w = this.getWidth();
		int h = this.getHeight();
		g.setColor(_color);
		g.setFont(new Font("Courier", Font.BOLD, _size));
		FontMetrics fm = g.getFontMetrics();
		int fw = fm.stringWidth(_text);
		int fh = fm.getAscent();
		g.drawString(_text, w / 2 - fw / 2, h / 2 + fh / 2);
	}

	public void setText(String text) {
		_text = text;
		repaint();
	}

	public void setColor(Color color) {
		_color = color;
		repaint();
	}
} 