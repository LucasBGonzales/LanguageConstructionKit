package krythos.translator.windows;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.beans.PropertyVetoException;

import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

import krythos.translator.database.Database;
import krythos.util.logger.Log;

public class MainWindow extends JFrame implements ActionListener {
	private static final long serialVersionUID = -6392154213919210325L;

	public static final int WINDOW_TEST = 0;
	public static final int WINDOW_TRANSLATOR = 1;
	public static final int WINDOW_WORD_TOOL = 2;
	public static final int WINDOWS_LENGTH = 3;


	JDesktopPane m_frame;
	JInternalFrame[] m_arr_windows;
	/*
	 * JInternalFrame m_winTest;
	 * JInternalFrame m_winTranslator;
	 * JInternalFrame m_winWordTool;
	 */
	JMenuBar m_menuBar;
	JMenu m_menu_window;
	JCheckBoxMenuItem m_menuItem_showTestWindow;
	JCheckBoxMenuItem m_menuItem_showTranslateWindow;
	JCheckBoxMenuItem m_menuItem_showWordToolWindow;


	public MainWindow() {
		// Logger
		Log.setLevel(Log.LEVEL_DEBUG);

		// Look and Feel
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}

		// JDesktopPane
		m_frame = new JDesktopPane();
		m_frame.setBackground(Color.WHITE);

		// Show Windows
		m_arr_windows = new JInternalFrame[WINDOWS_LENGTH];
		showWindow(WINDOW_TEST, false);
		showWindow(WINDOW_TRANSLATOR, false);
		showWindow(WINDOW_WORD_TOOL, false);

		// Create Menus
		createMenus();


		// Setup Frame
		this.add(m_frame, BorderLayout.CENTER);
		this.setSize(1280, 720);
		this.setVisible(true);
		this.setIconImage(new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB));
		this.setTitle("Language Construction Kit");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
	}


	/**
	 * Creates the MenuBar and MenuItems.
	 */
	private void createMenus() {
		// Menu Bar
		m_menuBar = new JMenuBar();

		//// Menu Items
		// Window Group
		m_menu_window = new JMenu("Windows");
		m_menu_window.setMnemonic(KeyEvent.VK_W);
		m_menu_window.getAccessibleContext()
				.setAccessibleDescription("The only menu in this program that has menu items");
		m_menuBar.add(m_menu_window);

		// Show Test Window
		m_menuItem_showTestWindow = new JCheckBoxMenuItem("Test");
		m_menuItem_showTestWindow.setMnemonic(KeyEvent.VK_E);
		m_menuItem_showTestWindow.getAccessibleContext().setAccessibleDescription("This doesn't really do anything");
		m_menuItem_showTestWindow.addActionListener(this);
		m_menuItem_showTestWindow.setSelected(m_arr_windows[WINDOW_TEST].isVisible());
		m_menu_window.add(m_menuItem_showTestWindow);

		// Show Translator Window
		m_menuItem_showTranslateWindow = new JCheckBoxMenuItem("Translator");
		m_menuItem_showTranslateWindow.setMnemonic(KeyEvent.VK_T);
		m_menuItem_showTranslateWindow.addActionListener(this);
		m_menuItem_showTranslateWindow.setSelected(m_arr_windows[WINDOW_TRANSLATOR].isVisible());
		m_menu_window.add(m_menuItem_showTranslateWindow);

		// Show Translator Window
		m_menuItem_showWordToolWindow = new JCheckBoxMenuItem("Word Tool");
		m_menuItem_showWordToolWindow.setMnemonic(KeyEvent.VK_W);
		m_menuItem_showWordToolWindow.addActionListener(this);
		m_menuItem_showWordToolWindow.setSelected(m_arr_windows[WINDOW_WORD_TOOL].isVisible());
		m_menu_window.add(m_menuItem_showWordToolWindow);


		this.setJMenuBar(m_menuBar);
	}


	public void openWord(String word, String language) {
		showWindow(WINDOW_WORD_TOOL, true);
		((WordToolWindow) m_arr_windows[WINDOW_WORD_TOOL]).openWord(word, language);
	}


	/**
	 * Sets the visibility of the specified window.
	 * Also initializes the Test Window if it isn't
	 * initialized.
	 * 
	 * @param window   Use constants provided by MainWindow
	 * @param visibile {@code true} to show the window, {@code false} to
	 *                 hide it.
	 */
	private void showWindow(int window, boolean visibile) {
		String tag = "MainWindow.showWindow";


		// Make sure the window is created.
		switch (window) {
		case WINDOW_TEST:
			Log.info(tag, "Show Test Window: " + visibile);

			// If it hasn't been initialized, create it.
			if (m_arr_windows[WINDOW_TEST] == null) {
				m_arr_windows[WINDOW_TEST] = new JInternalFrame("Test Frame", true, true, true, true);
				m_arr_windows[WINDOW_TEST].setBounds(50, 90, 200, 250);
				m_arr_windows[WINDOW_TEST].setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
				m_frame.add(m_arr_windows[WINDOW_TEST]);


				JLabel label = new JLabel(m_arr_windows[WINDOW_TEST].getTitle(), JLabel.CENTER);
				m_arr_windows[WINDOW_TEST].add(label, BorderLayout.CENTER);

				JButton button = new JButton("Demo Button");
				m_arr_windows[WINDOW_TEST].add(button, BorderLayout.WEST);

				// This will uncheck the menu item when the window is closed.
				m_arr_windows[WINDOW_TEST].addInternalFrameListener(new InternalFrameAdapter() {
					public void internalFrameClosing(InternalFrameEvent e) {
						m_menuItem_showTestWindow.setSelected(false);
					}
				});
			}
			break;

		case WINDOW_TRANSLATOR:
			Log.info(tag, "Show Translator Window: " + visibile);

			// If it hasn't been initialized, create it.
			if (m_arr_windows[WINDOW_TRANSLATOR] == null) {
				// m_arr_windows[WINDOW_TRANSLATOR] = new TranslatorWindow(true, true,
				// true, true);
				m_arr_windows[WINDOW_TRANSLATOR] = new TranslatorTool(this);
				m_frame.add(m_arr_windows[WINDOW_TRANSLATOR]);

				// This will uncheck the menu item when the window is closed.
				m_arr_windows[WINDOW_TRANSLATOR].addInternalFrameListener(new InternalFrameAdapter() {
					public void internalFrameClosing(InternalFrameEvent e) {
						m_menuItem_showTranslateWindow.setSelected(false);
					}
				});
			}
			break;

		case WINDOW_WORD_TOOL:
			Log.info(tag, "Show Translator Window: " + visibile);

			// If it hasn't been initialized, create it.
			if (m_arr_windows[WINDOW_WORD_TOOL] == null) {
				m_arr_windows[WINDOW_WORD_TOOL] = new WordToolWindow(true, true, true, true);
				m_frame.add(m_arr_windows[WINDOW_WORD_TOOL]);

				// This will uncheck the menu item when the window is closed.
				m_arr_windows[WINDOW_WORD_TOOL].addInternalFrameListener(new InternalFrameAdapter() {
					public void internalFrameClosing(InternalFrameEvent e) {
						m_menuItem_showWordToolWindow.setSelected(false);
					}
				});
			}
			break;
		}

		// Show the window.
		m_arr_windows[window].setVisible(visibile);
		m_arr_windows[window].moveToFront();
		try {
			m_arr_windows[window].setSelected(true);
		} catch (PropertyVetoException e) {
			e.printStackTrace();
		}
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		String tag = "MainWindow.actionPerformed";
		Object source = e.getSource();

		// Show Test Window
		if (source.equals(m_menuItem_showTestWindow)) {
			if (((JCheckBoxMenuItem) e.getSource()).isSelected()) {
				Log.info(tag, "Showing Test Window");
				showWindow(WINDOW_TEST, true);
			} else {
				Log.info(tag, "Hiding Test Window");
				showWindow(WINDOW_TEST, false);
			}
		}

		// Show Translator Window
		if (source.equals(m_menuItem_showTranslateWindow)) {
			if (((JCheckBoxMenuItem) source).isSelected()) {
				Log.info(tag, "Showing Translator Window");
				showWindow(WINDOW_TRANSLATOR, true);
			} else {
				Log.info(tag, "Hiding Translator Window");
				showWindow(WINDOW_TRANSLATOR, false);
			}
		}

		// Show Word Tool Window
		if (source.equals(m_menuItem_showWordToolWindow)) {
			if (((JCheckBoxMenuItem) source).isSelected()) {
				Log.info(tag, "Showing Word Tool Window");
				showWindow(WINDOW_WORD_TOOL, true);
			} else {
				Log.info(tag, "Hiding Word Tool Window");
				showWindow(WINDOW_WORD_TOOL, false);
			}
		}
	}

}
