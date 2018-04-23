package tetris;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

@SuppressWarnings("serial")
public class GridPanel extends JPanel {

	private Grid[][] _grids;
	private static int _size;

	public GridPanel(int rows, int columns, boolean hasBorder,
			Color borderColor, int size) {
		setLayout(new GridLayout(rows, columns));
		setBorder(new LineBorder(Color.GRAY));
		_grids = new Grid[rows][columns];
		_size = size;
		for (int i = 0; i < rows; i++)
			for (int j = 0; j < columns; j++) {
				_grids[i][j] = new Grid(hasBorder, borderColor);
				add(_grids[i][j]);
			}
	}

	static class Grid extends JComponent {

		private int _on = 0;

		public Grid(boolean hasBorder, Color borderColor) {
			if (hasBorder)
				setBorder(new LineBorder(borderColor));
		}

		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			this.setDoubleBuffered(true);
			int w = this.getWidth();
			int h = this.getHeight();
			if (_on > 0) {
				g.setColor(Config.tetriminoColors[_on - 1]);
				g.fillRect(0, 0, w, h);
			} else
				g.clearRect(0, 0, w, h);
		}

		public Dimension getPreferredSize() {
			return new Dimension(_size, _size);
		}

		public void set(int value) {
			_on = value;
		}

		public int get() {
			return _on;
		}

	}

	public int getRows() {
		return _grids.length;
	}

	public int getCols() {
		return _grids[0].length;
	}

	public void setModel(IntMatrix model) {
		reset();
		int colBegin = 0;
		if (model.getWidth() < getCols()) {
			colBegin = (getCols() - model.getWidth()) / 2;
		}
		int rowBegin = 0;
		if (model.getHeight() < getRows()) {
			rowBegin = (getRows() - model.getHeight()) / 2;
		}
		for (int i = 0; i < model.getHeight(); i++)
			for (int j = 0; j < model.getWidth(); j++) {
				_grids[i + rowBegin][j + colBegin].set(model.get(i, j));
			}
		repaint();
	}

	public void reset() {
		for (int i = 0; i < getRows(); i++)
			for (int j = 0; j < getCols(); j++)
				_grids[i][j].set(0);
	}

	public void blink(int row[], int count) {
		int sleep = 150;
		int[][] temp = new int[count][getCols()];
		for (int i = 0; i < count; i++)
			for (int j = 0; j < getCols(); j++) {
				temp[i][j] = _grids[row[i]][j].get();
			}
		try {
			setRowsColor(row, count, 8);
			repaint();
			Thread.sleep(sleep);
			back(temp, row, count);
			repaint();
			Thread.sleep(sleep);
			setRowsColor(row, count, 8);
			repaint();
			Thread.sleep(sleep);
			back(temp, row, count);
			repaint();
		} catch (InterruptedException e) {
		}
	}

	private void back(int[][] temp, int row[], int count) {
		for (int i = 0; i < count; i++)
			for (int j = 0; j < getCols(); j++) {
				_grids[row[i]][j].set(temp[i][j]);
			}
	}

	private void setRowsColor(int row[], int count, int color) {
		for (int i = 0; i < count; i++)
			for (int j = 0; j < getCols(); j++) {
				_grids[row[i]][j].set(color + 1);
			}
	} 
}