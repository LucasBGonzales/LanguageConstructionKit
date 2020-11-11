package krythos.translator.windows;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class TranslatorWindow extends JInternalFrame {
	private static final long serialVersionUID = -7317064130866253123L;

	
	private JTextArea txtEntry, txtTranslation;
	private JButton btnChooseLanguages, btnTranslate;


	public TranslatorWindow(boolean resizable, boolean closable, boolean maximizable, boolean iconifiable) {
		super("Translator", resizable, closable, maximizable, iconifiable);

		// Set GridBagLayout as content pane.
		JPanel contentPane = new JPanel();
		contentPane.setLayout(new GridLayout());

		JPanel pnlRight = new JPanel();
		pnlRight.setLayout(new GridBagLayout());

		JPanel pnlLeft = new JPanel();
		pnlLeft.setLayout(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();


		// Where user inputs text to translate
		txtEntry = new JTextArea("Entry");
		txtEntry.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1.0;
		c.weighty = 1.0;
		c.insets = new Insets(3, 3, 3, 3);
		pnlLeft.add(new JScrollPane(txtEntry), c);


		// Output for translated text
		c = new GridBagConstraints();
		txtTranslation = new JTextArea("Translation");
		txtTranslation.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		// txtTranslation.setEditable(false);
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1.0;
		c.weighty = 1.0;
		c.gridx = 0;
		c.gridy = 0;
		c.gridheight = 1;
		c.gridwidth = 2;
		c.ipadx = 2;
		c.ipady = 2;
		pnlRight.add(new JScrollPane(txtTranslation), c);

		// Button that prompts the user to select languages to translate from
		// and to.
		btnChooseLanguages = new JButton("Choose Languages");
		c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0.5;
		c.weighty = 0;
		c.gridx = 0;
		c.gridy = 1;
		c.gridheight = 1;
		c.gridwidth = 1;
		c.ipadx = 2;
		c.ipady = 2;
		pnlRight.add(btnChooseLanguages, c);

		// Starts the process to translate the entered text.
		btnTranslate = new JButton("Translate");
		c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 0.5;
		c.weighty = 0;
		c.gridx = 1;
		c.gridy = 1;
		c.gridheight = 1;
		c.gridwidth = 1;
		c.ipadx = 2;
		c.ipady = 2;
		pnlRight.add(btnTranslate, c);


		// Insert Panels
		contentPane.add(pnlLeft);
		contentPane.add(pnlRight);


		this.setContentPane(contentPane);
		this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		this.setFrameIcon(null);
		this.setBounds(50, 90, 720, 480);
	}
}
