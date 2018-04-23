package tetris;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

@SuppressWarnings("serial")
public class Tetris extends JFrame {

	private String[] _language;
	private int _languageIndex;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Tetris() {
		
		super("Tetris");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setUndecorated(true);
		setResizable(false);
		_language = Config.getLanguage(0);
		_languageIndex = 0;
		final Tetris tetris = this;

		try {
			ImageIcon icon = new ImageIcon("Icons/icon.png");
			setIconImage(icon.getImage());
		} catch (java.lang.NullPointerException e) {
		}

		final ImageIcon[] icons = new ImageIcon[Config.languageList.length];

		for (int i = 0; i < Config.languageList.length; i++) {
			try {
				icons[i] = new ImageIcon("Icons/" + Config.languageList[i]
						+ ".png");
			} catch (NullPointerException e) {
				icons[i] = null;
			}
		}
		
		class ComboBoxRenderer extends JLabel implements ListCellRenderer {
			public ComboBoxRenderer() {
				setOpaque(true);
				setVerticalAlignment(CENTER);
			}

			public Component getListCellRendererComponent(JList list,
					Object value, int index, boolean isSelected,
					boolean cellHasFocus) {

				if (isSelected) {
					setBackground(list.getSelectionBackground());
					setForeground(list.getSelectionForeground());
				} else {
					setBackground(list.getBackground());
					setForeground(list.getForeground());
				}
				setPreferredSize(new Dimension(Config.comboBoxDimension[0],
						Config.comboBoxDimension[1]));
				setText(Config.languageList[(int) value]);
				setFont(list.getFont());
				setForeground(Config.color[4]);
				setIcon(icons[(int) value]);
				return this;
			}
		}

		Object[] object = new Object[Config.languageList.length];
		for (int i = 0; i < Config.languageList.length; i++)
			object[i] = i;
		final JComboBox combo = new JComboBox(object);
		combo.setRenderer(new ComboBoxRenderer());

		JPanel all = new JPanel();
		JPanel north = new JPanel();
		JPanel south = new JPanel();
		JPanel center = new JPanel();
		JPanel buttons = new JPanel();
		JPanel centerNorth = new JPanel();
		JPanel centerCenter = new JPanel();

		JPanel panel1 = new JPanel();
		JPanel panel2 = new JPanel();
		JPanel panel3 = new JPanel();
		JPanel panel4 = new JPanel();

		all.setLayout(new BorderLayout());
		south.setLayout(new BorderLayout());
		center.setLayout(new BorderLayout());
		centerCenter.setLayout(new GridLayout(2, 2));
		panel1.setLayout(new BorderLayout());
		panel2.setLayout(new BorderLayout());
		panel3.setLayout(new BorderLayout());
		panel4.setLayout(new BorderLayout());

		final JLabel label1 = new JLabel(_language[0] + ":   ");
		final JLabel label2 = new JLabel(_language[1]);
		final JLabel label3 = new JLabel(_language[2] + ":   ");
		final JLabel label4 = new JLabel(_language[3] + ":   ");
		final JLabel status = new JLabel();

		label1.setFont(label1.getFont().deriveFont((float) Config.labelSize[0]));
		label2.setFont(label2.getFont().deriveFont((float) Config.labelSize[1]));
		label3.setFont(label3.getFont().deriveFont((float) Config.labelSize[0]));
		label4.setFont(label4.getFont().deriveFont((float) Config.labelSize[0]));
		status.setFont(status.getFont().deriveFont((float) Config.labelSize[1]));

		label1.setForeground(Config.color[1]);
		label2.setForeground(Config.color[2]);
		label3.setForeground(Config.color[3]);
		label4.setForeground(Config.color[3]);

		String[] tempIp = Config.setIp();
		final JTextField ip = new JTextField(tempIp[0]);
		final JTextField port = new JTextField(tempIp[1]);

		final JButton start = new JButton(_language[4]);
		final JButton check = new JButton(_language[5]);
		final JButton exit = new JButton(_language[6]);

		start.setPreferredSize(new Dimension(Config.buttonDimension[0],
				Config.buttonDimension[1]));
		check.setPreferredSize(new Dimension(Config.buttonDimension[0],
				Config.buttonDimension[1]));
		exit.setPreferredSize(new Dimension(Config.buttonDimension[0],
				Config.buttonDimension[1]));

		ip.setPreferredSize(new Dimension(0, Config.textFieldDimension[1]));
		port.setPreferredSize(new Dimension(0, Config.textFieldDimension[1]));
		ip.setColumns(Config.textFieldDimension[0]);
		port.setColumns(Config.textFieldDimension[0]);

		start.setFocusable(false);
		check.setFocusable(false);
		exit.setFocusable(false);

		all.setBorder(BorderFactory.createLineBorder(Config.color[0], 1));
		north.setBorder(new EmptyBorder(10, 10, 10, 10));
		center.setBorder(new EmptyBorder(10, 10, 10, 10));
		centerNorth.setBorder(new EmptyBorder(0, 0, 5, 0));

		panel1.add(label3, BorderLayout.EAST);
		panel2.add(ip, BorderLayout.WEST);
		panel3.add(label4, BorderLayout.EAST);
		panel4.add(port, BorderLayout.WEST);

		centerNorth.add(label2);
		centerNorth.add(status);

		centerCenter.add(panel1);
		centerCenter.add(panel2);
		centerCenter.add(panel3);
		centerCenter.add(panel4);

		north.add(label1);
		north.add(combo);

		buttons.add(exit);
		buttons.add(check);
		buttons.add(start);

		center.add(centerNorth, BorderLayout.NORTH);
		center.add(centerCenter, BorderLayout.CENTER);

		south.add(buttons, BorderLayout.EAST);

		all.add(north, BorderLayout.NORTH);
		all.add(center, BorderLayout.CENTER);
		all.add(south, BorderLayout.SOUTH);

		add(all);

		start.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				int choice = Config.setConfig(ip.getText(), port.getText(),
						_language);
				GameFrame gameFrame = null;

				if (choice == 1)
					gameFrame = new GameFrame(true);
				if (choice == 2)
					gameFrame = new GameFrame(false);
				if (gameFrame != null) {
					tetris.dispose();
					Dimension dm = gameFrame.getToolkit().getScreenSize();
					gameFrame.setLocation(
							(int) (dm.getWidth() / 2 - gameFrame.getWidth() / 2),
							(int) (dm.getHeight() / 2 - gameFrame.getHeight() / 2));
					gameFrame.setVisible(true);
				}
			}
		});

		exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});

		combo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {

				String[] temp = Config.getLanguage(combo.getSelectedIndex());

				if (temp == null) {
					JOptionPane.showMessageDialog(null, _language[10] + "!",
							_language[9] + "!", JOptionPane.ERROR_MESSAGE);
					combo.setSelectedIndex(_languageIndex);
					return;
				}

				_language = temp;
				_languageIndex = combo.getSelectedIndex();
				label1.setText(_language[0] + ":   ");
				label2.setText(_language[1]);
				label3.setText(_language[2] + ":   ");
				label4.setText(_language[3] + ":   ");
				start.setText(_language[4]);
				check.setText(_language[5]);
				exit.setText(_language[6]);
				status.setText("");
			}
		});

		check.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				class Check extends Thread {

					private String _command;
					private String _ip;
					private int _port;
					private JLabel _status;

					public void check(String command, String ip, String port,
							JLabel status) {

						status.setForeground(Config.color[7]);
						status.setText("-  " + _language[11] + "!");
						status.revalidate();
						_command = command;
						_ip = ip;
						try {
							_port = Integer.parseInt(port);
						} catch (NumberFormatException e) {
							status.setForeground(Config.color[6]);
							status.setText("-  " + _language[7] + "!");
							status.revalidate();
							return;
						}
						_status = status;
						start();
					}

					public void run() {

						Socket socket = null;

						try {
							try {
								socket = new Socket(_ip, _port);
								socket.setSoTimeout(Config.timeout);
							} catch (IllegalArgumentException e) {
								_status.setForeground(Config.color[6]);
								_status.setText("-  " + _language[7] + "!");
								_status.revalidate();
								return;
							}
							String answer;
							OutputStream os = socket.getOutputStream();
							PrintWriter pw = new PrintWriter(os, true);
							InputStream is = socket.getInputStream();
							BufferedReader br = new BufferedReader(
									new InputStreamReader(is));
							pw.println(_command);

							answer = br.readLine();
							socket.close();
							if (answer.equals(Protocol.CHECKED)) {
								_status.setForeground(Config.color[5]);
								_status.setText("-  " + _language[8] + "!");
								_status.revalidate();
								;
								return;
							} else {
								_status.setForeground(Config.color[6]);
								_status.setText("-  " + _language[7] + "!");
								_status.revalidate();
								return;
							}
						} catch (IOException e) {
							_status.setForeground(Config.color[6]);
							_status.setText("-  " + _language[7] + "!");
							_status.revalidate();
							return;
						}
					}
				}

				if (!status.getText().equals("-  " + _language[11] + "!")) {
					Check check = new Check();
					check.check(Protocol.CHECK, ip.getText(), port.getText(),
							status);
				}
			}
		});
	}

	public static void main(String[] args) {

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new Config();
				Tetris tetris = new Tetris();
				tetris.setVisible(true);
				tetris.pack();
				Dimension dm = tetris.getToolkit().getScreenSize();
				tetris.setLocation(
						(int) (dm.getWidth() / 2 - tetris.getWidth() / 2),
						(int) (dm.getHeight() / 2 - tetris.getHeight() / 2));
			}
		});
	}
} 