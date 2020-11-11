package krythos.translator.windows;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FocusTraversalPolicy;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import javax.swing.event.HyperlinkEvent;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;

import krythos.translator.database.Database;
import krythos.translator.language.Word;
import krythos.translator.language.Word.Translation;
import krythos.util.abstract_interfaces.AbsMouseListener;
import krythos.util.swing.Dialogs;

@SuppressWarnings("serial")
public class TranslatorTool extends JInternalFrame {


	private MainWindow m_mainWindow;

	private JEditorPane paneL1;
	private JEditorPane paneL2;
	private JButton btnLanguage1;
	private JButton btnLanguage2;
	private JButton btnEnterText;
	private JButton btnEvaluateText;

	private String m_lang1, m_lang2, m_text;

	private static final String TAG_L1 = "L1T_";
	private static final String TAG_L1_NO_WORD = "L1N_";
	private static final String TAG_L2_ONE_TRANSLATION = "L2T_";
	private static final String TAG_L2_NO_TRANSLATION = "L2N_";
	private static final String TAG_L2_MULTI_TRANSLATION = "L2M_";


	/**
	 * Create the frame.
	 */
	public TranslatorTool(MainWindow mainWindow) {
		m_mainWindow = mainWindow;

		// Set up Translator Tool
		setTitle("Translator Tool");
		setIconifiable(true);
		setMaximizable(true);
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setClosable(true);
		setResizable(true);
		setBounds(100, 100, 662, 334);
		setMinimumSize(new Dimension(400, 200));

		// Set up UI

		SpringLayout springLayout = new SpringLayout();
		getContentPane().setLayout(springLayout);

		btnLanguage1 = new JButton();
		btnLanguage1.addActionListener(e -> {
			selectLanguage(1);
		});
		springLayout.putConstraint(SpringLayout.WEST, btnLanguage1, 10, SpringLayout.EAST, getContentPane());
		springLayout.putConstraint(SpringLayout.NORTH, btnLanguage1, 10, SpringLayout.NORTH, getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, btnLanguage1, 10, SpringLayout.WEST, getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, btnLanguage1, 110, SpringLayout.WEST, getContentPane());
		getContentPane().add(btnLanguage1);

		btnLanguage2 = new JButton();
		btnLanguage2.addActionListener(e -> {
			selectLanguage(2);
		});
		springLayout.putConstraint(SpringLayout.NORTH, btnLanguage2, 6, SpringLayout.SOUTH, btnLanguage1);
		springLayout.putConstraint(SpringLayout.WEST, btnLanguage2, 0, SpringLayout.WEST, btnLanguage1);
		springLayout.putConstraint(SpringLayout.EAST, btnLanguage2, 0, SpringLayout.EAST, btnLanguage1);
		getContentPane().add(btnLanguage2);


		btnEnterText = new JButton("Enter Text");
		btnEnterText.addActionListener(e -> {
			getTextFromUser();
		});
		springLayout.putConstraint(SpringLayout.NORTH, btnEnterText, 6, SpringLayout.SOUTH, btnLanguage2);
		springLayout.putConstraint(SpringLayout.WEST, btnEnterText, 0, SpringLayout.WEST, btnLanguage2);
		springLayout.putConstraint(SpringLayout.EAST, btnEnterText, 0, SpringLayout.EAST, btnLanguage2);
		getContentPane().add(btnEnterText);

		btnEvaluateText = new JButton("Evaluate Text");
		btnEvaluateText.addActionListener(e -> {
			evaluateText();
		});
		springLayout.putConstraint(SpringLayout.NORTH, btnEvaluateText, 6, SpringLayout.SOUTH, btnEnterText);
		springLayout.putConstraint(SpringLayout.WEST, btnEvaluateText, 0, SpringLayout.WEST, btnEnterText);
		springLayout.putConstraint(SpringLayout.EAST, btnEvaluateText, 0, SpringLayout.EAST, btnEnterText);
		getContentPane().add(btnEvaluateText);

		JPanel paneContainer = new JPanel();
		paneContainer.setLayout(new GridLayout(1, 2));
		springLayout.putConstraint(SpringLayout.NORTH, paneContainer, 10, SpringLayout.NORTH, getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, paneContainer, -10, SpringLayout.SOUTH, getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, paneContainer, 10, SpringLayout.EAST, btnLanguage1);
		springLayout.putConstraint(SpringLayout.EAST, paneContainer, -10, SpringLayout.EAST, getContentPane());
		getContentPane().add(paneContainer);

		HTMLEditorKit kit = new HTMLEditorKit();
		StyleSheet css = kit.getStyleSheet();
		css.addRule("a{text-decoration: none; color: black;}");
		Document doc1 = kit.createDefaultDocument();
		Document doc2 = kit.createDefaultDocument();

		paneL1 = new JEditorPane();
		paneL1.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
		paneL1.setEditable(false);
		paneL1.setContentType("text/html");
		paneL1.addHyperlinkListener(e -> {
			if (HyperlinkEvent.EventType.ACTIVATED.equals(e.getEventType()))
				wordSelected(e.getDescription());
		});
		paneL1.addMouseListener(new AbsMouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON3)
					getTextFromUser();
			}
		});
		paneL1.setDocument(doc1);
		paneContainer.add(paneL1);

		paneL2 = new JEditorPane();
		paneL2.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
		paneL2.setEditable(false);
		paneL2.setContentType("text/html");
		paneL2.addHyperlinkListener(e -> {
			if (HyperlinkEvent.EventType.ACTIVATED.equals(e.getEventType())) {
				wordSelected(e.getDescription());
			}
		});
		paneL2.setDocument(doc2);
		paneContainer.add(paneL2);

		this.setFocusTraversalPolicy(new FocusTraversalPolicy() {
			List<Component> arr_list = new ArrayList<Component>(Arrays.asList(
					(new Component[] { btnLanguage1, btnLanguage2, btnEnterText, btnEvaluateText, paneL1, paneL2 })));


			@Override
			public Component getComponentAfter(Container arg0, Component arg1) {
				int i = arr_list.indexOf(arg1) + 1;
				return i == 0 ? null : (i == arr_list.size() ? arr_list.get(0) : arr_list.get(i));
			}


			@Override
			public Component getComponentBefore(Container arg0, Component arg1) {
				int i = arr_list.indexOf(arg1) - 1;
				return i < -1 ? null : (i < 0 ? arr_list.get(arr_list.size() - 1) : arr_list.get(i));
			}


			@Override
			public Component getDefaultComponent(Container arg0) {
				return getFirstComponent(arg0);
			}


			@Override
			public Component getFirstComponent(Container arg0) {
				return arr_list.size() > 0 ? arr_list.get(0) : null;
			}


			@Override
			public Component getLastComponent(Container arg0) {
				return arr_list.size() > 0 ? arr_list.get(arr_list.size() - 1) : null;
			}

		});


		// Initialize Strings
		setLanguages("", "");
		m_text = "";
	}


	private void getTextFromUser() {
		String entry = Dialogs.showInputAreaDialog(null, "Enter the text to translate.", "");
		updateText(entry);
	}


	private void wordSelected(String desc) {
		String tag = desc.substring(0, 4);
		String word = desc.substring(4);

		switch (tag) {
		case TAG_L1:
			m_mainWindow.openWord(word, m_lang1);
			break;
		case TAG_L1_NO_WORD:
			// TODO Handle no word.
			m_mainWindow.openWord(word, m_lang1);
			break;
		case TAG_L2_NO_TRANSLATION:
			m_mainWindow.openWord(word, m_lang1);
			break;
		case TAG_L2_ONE_TRANSLATION:
			m_mainWindow.openWord(word, m_lang2);
			break;
		case TAG_L2_MULTI_TRANSLATION:
			// TODO Update text with selected word.
			
			// Get translations, get the words.
			Object[] arr = Database.get().getLanguage(m_lang1).getWord(word).getTranslations().toArray();
			for (int i = 0; i < arr.length; i++)
				arr[i] = ((Translation) arr[i]).getString();

			// Let user pick.
			String w = (String) JOptionPane.showInputDialog(this, "Select Translation:", "Multiple Translations",
					JOptionPane.QUESTION_MESSAGE, null, arr, arr[0]);
			m_mainWindow.openWord(w, m_lang2);
			break;
		}
	}


	private void selectLanguage(int language) {
		String lang1 = m_lang1;
		String lang2 = m_lang2;

		String[] arr_lang = Database.get().getLangaugeNames();
		@SuppressWarnings({ "unchecked", "rawtypes" })
		JComboBox cbx = new JComboBox(arr_lang);

		int result = JOptionPane.showOptionDialog(null, cbx, "Select Language", JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.QUESTION_MESSAGE, null, null, null);

		if (result != JOptionPane.CANCEL_OPTION) {
			if (language == 1)
				lang1 = (String) cbx.getSelectedItem();
			else
				lang2 = (String) cbx.getSelectedItem();
			setLanguages(lang1, lang2);
		}
	}


	private void setLanguages(String lang1, String lang2) {

		// If nothing passed, selected first two languages.
		if (lang1.equals(lang2) && lang1.equals("")) {
			String[] languages = Database.get().getLangaugeNames();
			if (languages.length > 1) {
				lang1 = languages[0];
				lang2 = languages[1];
			} else {
				lang1 = "!NAN!";
				lang2 = "!NAN!";
			}
		}

		// Set languages and button properties.
		m_lang1 = lang1;
		m_lang2 = lang2;
		btnLanguage1.setText(m_lang1);
		btnLanguage1.setToolTipText(m_lang1);
		btnLanguage2.setText(m_lang2);
		btnLanguage2.setToolTipText(m_lang2);
	}


	private void evaluateText() {
		String textL1, textL2;

		// Remove line breaks. Apparently HTMLEditorKit is a bitch.
		m_text = m_text.replaceAll("\n", " ").replaceAll("  ", " ");


		String[] words = m_text.split(" ");
		String[] wordsL1 = new String[words.length];
		String[] wordsL2 = new String[words.length];

		// Get Translations for wordsL2
		String STYLE_L1_LINK = "<a href='" + TAG_L1 + "{replace}'>{replace}</a>";
		String STYLE_L1_NO_WORD = "<a style=\"color:red\" href='" + TAG_L1_NO_WORD + "{replace}'>{replace}</a>";
		String STYLE_NO_TRANSLATION = "<a style=\"color:red\" href=\"" + TAG_L2_NO_TRANSLATION
				+ "{replace}\">{replace}</a>";
		String STYLE_ONE_TRANSLATION = "<a href='" + TAG_L2_ONE_TRANSLATION + "{replace}'>{replace}</a>";
		String STYLE_MULTI_TRANSLATION = "<a style=\"color:blue\" href='" + TAG_L2_MULTI_TRANSLATION
				+ "{replace_2}'>{replace}</a>";

		for (int i = 0; i < words.length; i++) {
			List<Translation> arr_t;
			boolean wordL1_exists = Database.get().getLanguage(m_lang1).getWord(words[i]) != null;
			try {
				arr_t = Database.get().getLanguage(m_lang1).getWord(words[i]).getTranslations(m_lang2);
			} catch (Exception e) {
				arr_t = null;
			}

			// Add links
			if (arr_t == null || arr_t.size() <= 0)
				wordsL2[i] = STYLE_NO_TRANSLATION.replace("{replace}", words[i]);
			else if (arr_t.size() == 1)
				wordsL2[i] = STYLE_ONE_TRANSLATION.replace("{replace}", arr_t.get(0).getString());
			else
				wordsL2[i] = STYLE_MULTI_TRANSLATION.replace("{replace}", arr_t.get(0).getString()).replace("{replace_2}",
						words[i]);

			if (wordL1_exists)
				wordsL1[i] = STYLE_L1_LINK.replace("{replace}", words[i]);
			else
				wordsL1[i] = STYLE_L1_NO_WORD.replace("{replace}", words[i]);
		}

		// Reconstruct Strings
		textL1 = "";
		textL2 = "";
		for (int i = 0; i < wordsL1.length; i++) {
			textL1 += wordsL1[i];
			if (i < wordsL1.length - 1)
				textL1 += " ";
			textL2 += wordsL2[i];
			if (i < wordsL2.length - 1)
				textL2 += " ";
		}
		paneL1.setText(textL1);
		paneL2.setText(textL2);
	}


	private void updateText(String text) {
		m_text = text;
		evaluateText();
	}
}
