package krythos.translator.windows;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import krythos.translator.database.Database;
import krythos.translator.language.Language;
import krythos.translator.language.PartOfSpeech;
import krythos.translator.language.Word;
import krythos.translator.language.Word.Definition;
import krythos.translator.language.Word.Translation;
import krythos.util.logger.Log;
import krythos.util.swing.DropSelection;

public class WordToolWindow extends JInternalFrame implements KeyListener, ListSelectionListener, ActionListener {
	private static final long serialVersionUID = -142191381966469757L;

	private Language m_selectedLanguage;
	private Word m_selectedWord;
	private Word m_editingWord;

	// Word Tool
	private DropSelection m_dropSelection;
	private JTextField m_txtSearchTool;
	private JTextField m_txtWord;
	private JButton m_btnNewWord;
	private JButton m_btnUpdateWord;
	private JButton m_btnDeleteWord;

	// Refine Search section
	// lblRefineSearch
	private JComboBox<String> m_cbxLanguage;
	private JCheckBox m_chkWord;
	private JCheckBox m_chkRoot;
	private JCheckBox m_chkPrefix;
	private JCheckBox m_chkSuffix;

	// Editor Definitions
	// private JList m_lstDefinitions;
	private DefaultListModel<String> m_lstDefinitions;
	private JList<String> m_lstDefinitionsBox;
	private JButton m_btnNewDefinition;
	private JButton m_btnUpdateDefinition;
	private JButton m_btnDeleteDefinition;
	private JComboBox<PartOfSpeech> m_cbxPartOfSpeech;
	private JTextArea m_txtDefinitionDescription;

	/// Translations
	private DefaultListModel<String> m_lstTranslations;
	private JList<String> m_lstTranslationsBox;
	private JTextField m_txtTranslatedWord, m_txtLanguage;
	private JButton m_btnDeleteTranslation;
	private JButton m_btnUpdateTranslation;
	private JButton m_btnNewTranslation;
	private GridBagConstraints gbc_1;
	private GridBagConstraints gbc_2;


	public WordToolWindow(boolean resizable, boolean closable, boolean maximizable, boolean iconifiable) {
		super("Word Tool", resizable, closable, maximizable, iconifiable);

		// Set GridBagLayout as content pane.
		JPanel contentPane = new JPanel();
		contentPane.setLayout(new GridBagLayout());
		this.setContentPane(contentPane);
		GridBagConstraints gbc = new GridBagConstraints();

		// JPanels
		JPanel pnlWordTool = new JPanel();
		pnlWordTool.setLayout(new GridBagLayout());

		JPanel pnlRefineSearchLeft = new JPanel();
		pnlRefineSearchLeft.setLayout(new GridBagLayout());

		JPanel pnlRefineSearchRight = new JPanel();
		pnlRefineSearchRight.setLayout(new GridBagLayout());

		JPanel pnlRefineSearch = new JPanel();
		pnlRefineSearch.setLayout(new GridBagLayout());

		JPanel pnlEditorArea = new JPanel();
		pnlEditorArea.setLayout(new GridBagLayout());
		pnlEditorArea.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

		JPanel pnlEditorDefinitionsLeft = new JPanel();
		pnlEditorDefinitionsLeft.setLayout(new GridBagLayout());

		JPanel pnlEditorDefinitionsRight = new JPanel();
		pnlEditorDefinitionsRight.setLayout(new GridBagLayout());

		JPanel pnlEditorTranslations = new JPanel();
		pnlEditorTranslations.setLayout(new GridBagLayout());


		// Search Tool to find words to view/edit.
		m_txtSearchTool = new JTextField("Search Tool");
		m_txtSearchTool.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		m_txtSearchTool.addKeyListener(this);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 0.5;
		gbc.weighty = 1.0;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridheight = 1;
		gbc.gridwidth = 3;
		gbc.ipadx = 2;
		gbc.ipady = 2;
		gbc.insets = new Insets(3, 3, 3, 3);
		pnlWordTool.add(m_txtSearchTool, gbc);
		gbc = new GridBagConstraints();

		// Create DropSelection
		m_dropSelection = new DropSelection(m_txtSearchTool, this);
		Database db = Database.get();
		List<String> lst_words = new ArrayList<String>();
		for (String s : db.getLangaugeNames()) {
			for (int i = 0; db.getLanguage(s).getWord(i) != null; i++)
				lst_words.add((db.getLanguage(s).getWord(i).getWordAsString()) + " : " + s);
		}
		String[] words = new String[lst_words.size()];
		for (int i = 0; i < words.length; i++)
			words[i] = lst_words.get(i);
		m_dropSelection.setDictionary(words);
		m_dropSelection.addDropListener(e -> {
			String[] arr_s = m_txtSearchTool.getText().split(" : ");
			openWord(arr_s[0], arr_s[1]);
		});


		// Word
		m_txtWord = new JTextField();
		m_txtWord.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 0.5;
		gbc.weighty = 1.0;
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridheight = 1;
		gbc.gridwidth = 3;
		gbc.ipadx = 2;
		gbc.ipady = 2;
		gbc.insets = new Insets(3, 3, 3, 3);
		pnlWordTool.add(m_txtWord, gbc);
		gbc = new GridBagConstraints();


		// Word
		m_btnNewWord = new JButton("New Word");
		m_btnNewWord.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 0.5;
		gbc.weighty = 1.0;
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.gridheight = 1;
		gbc.gridwidth = 1;
		gbc.ipadx = 2;
		gbc.ipady = 2;
		gbc.insets = new Insets(3, 3, 3, 3);
		pnlWordTool.add(m_btnNewWord, gbc);
		gbc = new GridBagConstraints();


		// Word
		m_btnUpdateWord = new JButton("Update Word");
		m_btnUpdateWord.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		m_btnUpdateWord.addActionListener(this);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 0.5;
		gbc.weighty = 1.0;
		gbc.gridx = 1;
		gbc.gridy = 2;
		gbc.gridheight = 1;
		gbc.gridwidth = 1;
		gbc.ipadx = 2;
		gbc.ipady = 2;
		gbc.insets = new Insets(3, 3, 3, 3);
		pnlWordTool.add(m_btnUpdateWord, gbc);
		gbc = new GridBagConstraints();


		// Word
		m_btnDeleteWord = new JButton("Delete Word");
		m_btnDeleteWord.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 0.5;
		gbc.weighty = 1.0;
		gbc.gridx = 2;
		gbc.gridy = 2;
		gbc.gridheight = 1;
		gbc.gridwidth = 1;
		gbc.ipadx = 2;
		gbc.ipady = 2;
		gbc.insets = new Insets(3, 3, 3, 3);
		pnlWordTool.add(m_btnDeleteWord, gbc);
		gbc = new GridBagConstraints();

		/*
		 * Refine Search Section.
		 */

		// Refine Search Label
		JLabel lblRefineSearch = new JLabel("Refine Search:");
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 0.5;
		gbc.weighty = 0.5;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridheight = 1;
		gbc.gridwidth = 1;
		gbc.ipadx = 2;
		gbc.ipady = 2;
		gbc.insets = new Insets(3, 3, 3, 3);
		pnlRefineSearchLeft.add(lblRefineSearch, gbc);
		gbc = new GridBagConstraints();

		// Language Selection
		m_cbxLanguage = new JComboBox<String>();
		Database.get();
		for (String item : Database.get().getLangaugeNames())
			m_cbxLanguage.addItem(item);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 0.5;
		gbc.weighty = 0.5;
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridheight = 1;
		gbc.gridwidth = 1;
		gbc.ipadx = 2;
		gbc.ipady = 2;
		gbc.insets = new Insets(3, 3, 3, 3);
		pnlRefineSearchLeft.add(m_cbxLanguage, gbc);
		gbc = new GridBagConstraints();

		// Check Box Word
		m_chkWord = new JCheckBox("Word");
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 0.5;
		gbc.weighty = 0.5;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridheight = 1;
		gbc.gridwidth = 1;
		gbc.ipadx = 2;
		gbc.ipady = 2;
		pnlRefineSearchRight.add(m_chkWord, gbc);
		gbc = new GridBagConstraints();

		// Check Box Root
		m_chkRoot = new JCheckBox("Root");
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 0.5;
		gbc.weighty = 0.5;
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.gridheight = 1;
		gbc.gridwidth = 1;
		gbc.ipadx = 2;
		gbc.ipady = 2;
		// gbc.insets = new Insets(3, 3, 3, 3);
		pnlRefineSearchRight.add(m_chkRoot, gbc);
		gbc = new GridBagConstraints();

		// Check Box Prefix
		m_chkPrefix = new JCheckBox("Prefix");
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 0.5;
		gbc.weighty = 0.5;
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridheight = 1;
		gbc.gridwidth = 1;
		gbc.ipadx = 2;
		gbc.ipady = 2;
		// gbc.insets = new Insets(3, 3, 3, 3);
		pnlRefineSearchRight.add(m_chkPrefix, gbc);
		gbc = new GridBagConstraints();

		// Check Box Prefix
		m_chkSuffix = new JCheckBox("Suffix");
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 0.5;
		gbc.weighty = 0.5;
		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.gridheight = 1;
		gbc.gridwidth = 1;
		gbc.ipadx = 2;
		gbc.ipady = 2;
		// gbc.insets = new Insets(3, 3, 3, 3);
		pnlRefineSearchRight.add(m_chkSuffix, gbc);
		gbc = new GridBagConstraints();

		/*
		 * Refine Search Panels
		 */

		// Search Refine Pane
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.NORTH;
		gbc.weightx = 0.5;
		gbc.weighty = 0.0;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridheight = 1;
		gbc.gridwidth = 1;
		gbc.ipadx = 2;
		gbc.ipady = 2;
		gbc.insets = new Insets(3, 3, 3, 3);
		pnlRefineSearch.add(pnlRefineSearchLeft, gbc);
		gbc = new GridBagConstraints();


		// Search Refine Pane
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.NORTH;
		gbc.weightx = 0.5;
		gbc.weighty = 0.0;
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.gridheight = 1;
		gbc.gridwidth = 1;
		gbc.ipadx = 2;
		gbc.ipady = 2;
		gbc.insets = new Insets(3, 3, 3, 3);
		pnlRefineSearch.add(pnlRefineSearchRight, gbc);
		gbc = new GridBagConstraints();


		/*
		 * Editor Definition Components
		 */
		// Definitions List
		m_lstDefinitions = new DefaultListModel<String>();
		m_lstDefinitionsBox = new JList<String>(m_lstDefinitions);
		m_lstDefinitionsBox.setVisibleRowCount(0);
		m_lstDefinitionsBox.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));
		m_lstDefinitionsBox.getSelectionModel().addListSelectionListener(this);
		JScrollPane sp = new JScrollPane(m_lstDefinitionsBox);
		gbc.fill = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.NORTH;
		gbc.weightx = 0.5;
		gbc.weighty = 1;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridheight = 1;
		gbc.gridwidth = 3;
		gbc.ipadx = 2;
		gbc.ipady = 2;
		gbc.insets = new Insets(3, 3, 3, 3);
		pnlEditorDefinitionsLeft.add(sp, gbc);
		gbc = new GridBagConstraints();
		// pnlEditorDefinitionsLeft.add(m_lstDefinitions, gbc);gbc = new
		// GridBagConstraints();

		// New Button
		m_btnNewDefinition = new JButton("<html><center>New<br>Definition");
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 0.5;
		gbc.weighty = 0;
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridheight = 1;
		gbc.gridwidth = 1;
		gbc.ipadx = 2;
		gbc.ipady = 2;
		gbc.insets = new Insets(3, 3, 3, 3);
		pnlEditorDefinitionsLeft.add(m_btnNewDefinition, gbc);
		gbc = new GridBagConstraints();

		// Update Button
		m_btnUpdateDefinition = new JButton("<html><center>Update<br>Definition");
		m_btnUpdateDefinition.addActionListener(this);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 0.5;
		gbc.weighty = 0;
		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.gridheight = 1;
		gbc.gridwidth = 1;
		gbc.ipadx = 2;
		gbc.ipady = 2;
		gbc.insets = new Insets(3, 3, 3, 3);
		pnlEditorDefinitionsLeft.add(m_btnUpdateDefinition, gbc);
		gbc = new GridBagConstraints();

		// Delete Button
		m_btnDeleteDefinition = new JButton("<html><center>Delete<br>Definition");
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 0.5;
		gbc.weighty = 0;
		gbc.gridx = 2;
		gbc.gridy = 1;
		gbc.gridheight = 1;
		gbc.gridwidth = 1;
		gbc.ipadx = 2;
		gbc.ipady = 2;
		gbc.insets = new Insets(3, 3, 3, 3);
		pnlEditorDefinitionsLeft.add(m_btnDeleteDefinition, gbc);
		gbc = new GridBagConstraints();

		// Part of Speech
		List<PartOfSpeech> pos = Database.get().getPartsOfSpeech();
		m_cbxPartOfSpeech = new JComboBox<PartOfSpeech>();
		m_cbxPartOfSpeech.addItem(new PartOfSpeech("nullPOS", "No Selection", ""));
		for (PartOfSpeech p : pos)
			m_cbxPartOfSpeech.addItem(p);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.NORTH;
		gbc.weightx = 0.5;
		gbc.weighty = 0;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridheight = 1;
		gbc.ipadx = 2;
		gbc.ipady = 2;
		gbc.insets = new Insets(3, 3, 3, 3);
		pnlEditorDefinitionsRight.add(m_cbxPartOfSpeech, gbc);
		gbc = new GridBagConstraints();

		// Description
		m_txtDefinitionDescription = new JTextArea();
		m_txtDefinitionDescription.setLineWrap(true);
		m_txtDefinitionDescription.setWrapStyleWord(true);
		m_txtDefinitionDescription.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(Color.GRAY, 1), BorderFactory.createEmptyBorder(2, 5, 2, 5)));
		// BorderFactory.createLineBorder(Color.GRAY, 1));
		gbc.fill = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.NORTH;
		gbc.weightx = 0.5;
		gbc.weighty = 0.5;
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridheight = 1;
		gbc.ipadx = 2;
		gbc.ipady = 2;
		gbc.insets = new Insets(3, 3, 3, 3);
		pnlEditorDefinitionsRight.add(m_txtDefinitionDescription, gbc);
		gbc = new GridBagConstraints();


		/*
		 * Translations Panel
		 */

		// Translations List

		// Translations List
		m_lstTranslations = new DefaultListModel<String>();
		m_lstTranslationsBox = new JList<String>(m_lstTranslations);
		m_lstTranslationsBox.setVisibleRowCount(0);
		m_lstTranslationsBox.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));
		m_lstTranslationsBox.getSelectionModel().addListSelectionListener(this);
		gbc.fill = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.NORTH;
		gbc.weightx = 0.5;
		gbc.weighty = 1;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridheight = 1;
		gbc.gridwidth = 6;
		gbc.ipadx = 2;
		gbc.ipady = 2;
		gbc.insets = new Insets(3, 3, 3, 3);
		pnlEditorTranslations.add(new JScrollPane(m_lstTranslationsBox), gbc);
		gbc = new GridBagConstraints();
		// pnlEditorTranslations.add(m_lstTranslations, gbc);gbc = new
		// GridBagConstraints();


		// Word
		m_txtTranslatedWord = new JTextField();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 0.5;
		gbc.weighty = 0;
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridheight = 1;
		gbc.gridwidth = 3;
		gbc.ipadx = 2;
		gbc.ipady = 2;
		gbc.insets = new Insets(3, 3, 3, 3);
		pnlEditorTranslations.add(m_txtTranslatedWord, gbc);
		gbc = new GridBagConstraints();


		// Language
		m_txtLanguage = new JTextField();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 0.5;
		gbc.weighty = 0;
		gbc.gridx = 3;
		gbc.gridy = 1;
		gbc.gridheight = 1;
		gbc.gridwidth = 3;
		gbc.ipadx = 2;
		gbc.ipady = 2;
		gbc.insets = new Insets(3, 3, 3, 3);
		pnlEditorTranslations.add(m_txtLanguage, gbc);
		gbc = new GridBagConstraints();


		// Update Button
		m_btnUpdateTranslation = new JButton("<html><center>Update<br>Translation");
		m_btnUpdateTranslation.addActionListener(this);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 0.5;
		gbc.weighty = 0;
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.gridheight = 1;
		gbc.gridwidth = 1;
		gbc.ipadx = 2;
		gbc.ipady = 2;
		gbc.insets = new Insets(3, 3, 3, 3);
		pnlEditorTranslations.add(m_btnUpdateTranslation, gbc);
		gbc = new GridBagConstraints();


		// New Button
		m_btnNewTranslation = new JButton("<html><center>New<br>Translation");
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 0.5;
		gbc.weighty = 0;
		gbc.gridx = 1;
		gbc.gridy = 2;
		gbc.gridheight = 1;
		gbc.gridwidth = 1;
		gbc.ipadx = 2;
		gbc.ipady = 2;
		gbc.insets = new Insets(3, 3, 3, 3);
		pnlEditorTranslations.add(m_btnNewTranslation, gbc);
		gbc = new GridBagConstraints();


		// Delete Button
		m_btnDeleteTranslation = new JButton("<html><center>Delete<br>Translation");
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 0.5;
		gbc.weighty = 0;
		gbc.gridx = 3;
		gbc.gridy = 2;
		gbc.gridheight = 1;
		gbc.gridwidth = 1;
		gbc.ipadx = 2;
		gbc.ipady = 2;
		gbc.insets = new Insets(3, 3, 3, 3);
		pnlEditorTranslations.add(m_btnDeleteTranslation, gbc);
		gbc = new GridBagConstraints();


		// Add to Editor Area

		// Editor Definitions Left
		gbc.fill = GridBagConstraints.VERTICAL;
		gbc.weightx = 1.0;
		gbc.weighty = 1;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridheight = 1;
		gbc.gridwidth = 1;
		gbc.ipadx = 2;
		gbc.ipady = 2;
		gbc.insets = new Insets(3, 3, 3, 3);
		pnlEditorArea.add(pnlEditorDefinitionsLeft, gbc);
		gbc_2 = new GridBagConstraints();


		// Editor Definitions Right
		gbc_2.fill = GridBagConstraints.VERTICAL;
		gbc_2.weightx = 2.0;
		gbc_2.weighty = 1.0;
		gbc_2.gridx = 1;
		gbc_2.gridy = 0;
		gbc_2.gridheight = 1;
		gbc_2.gridwidth = 1;
		gbc_2.ipadx = 2;
		gbc_2.ipady = 2;
		gbc_2.insets = new Insets(3, 3, 3, 3);
		pnlEditorArea.add(pnlEditorDefinitionsRight, gbc_2);
		gbc = new GridBagConstraints();

		// Editor Translations
		gbc.fill = GridBagConstraints.VERTICAL;
		gbc.weightx = 1.0;
		gbc.weighty = 1;
		gbc.gridx = 2;
		gbc.gridy = 0;
		gbc.gridheight = 1;
		gbc.gridwidth = 1;
		gbc.ipadx = 2;
		gbc.ipady = 2;
		gbc.insets = new Insets(3, 3, 3, 3);
		pnlEditorArea.add(pnlEditorTranslations, gbc);
		gbc_1 = new GridBagConstraints();
		gbc_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_1.anchor = GridBagConstraints.NORTH;
		gbc_1.weightx = 0.5;
		gbc_1.weighty = 0.0;
		gbc_1.gridx = 0;
		gbc_1.gridy = 0;
		gbc_1.gridheight = 1;
		gbc_1.ipadx = 2;
		gbc_1.ipady = 2;
		gbc_1.insets = new Insets(3, 3, 3, 3);
		contentPane.add(pnlWordTool, gbc_1);
		gbc = new GridBagConstraints();


		// Refine Search
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 0.5;
		gbc.weighty = 0.0;
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.gridheight = 1;
		gbc.gridwidth = 1;
		gbc.ipadx = 2;
		gbc.ipady = 2;
		gbc.insets = new Insets(3, 3, 3, 3);
		contentPane.add(pnlRefineSearch, gbc);
		gbc = new GridBagConstraints();

		// Adding Editor Area
		gbc.fill = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.NORTH;
		gbc.weightx = 0.5;
		gbc.weighty = 0.5;
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridheight = 1;
		gbc.gridwidth = 2;
		gbc.ipadx = 2;
		gbc.ipady = 2;
		gbc.insets = new Insets(3, 3, 3, 3);
		contentPane.add(pnlEditorArea, gbc);


		this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		// this.setFrameIcon(null);
		this.setBounds(50, 90, 852, 480);
	}


	/**
	 * Load's the selected word's data into the UI.
	 */
	private void loadWord() {
		Word w = m_editingWord;

		if (w == null)
			return;

		// Clear UI
		m_lstDefinitions.clear();
		m_lstTranslations.clear();

		// Word
		m_txtWord.setText(w.getWordAsString());

		// Definitions
		List<Definition> lstDef = w.getDefinitions();
		for (Definition def : lstDef) {
			String append = "...";
			int length = 42;
			if (def.getDefinition().length() < length) {
				length = def.getDefinition().length();
				append = "";
			}
			m_lstDefinitions.addElement("<html><b>" + def.getPartOfSpeech().getName() + "</b> "
				+ def.getDefinition().substring(0, length) + append + "</html>");
		}

		// Translations
		List<Translation> lstTrans = w.getTranslations();
		for (Translation tran : lstTrans)
			m_lstTranslations.addElement("<html><b>" + tran.getLanguage() + "</b> " + tran.getString() + "</html>");
	}


	public void openWord(String word, String language) {
		m_cbxLanguage.setSelectedItem(language);
		m_selectedLanguage = Database.get().getLanguage(language);
		if (m_selectedLanguage != null) {
			Word w = m_selectedLanguage.getWord(word);
			if (w != null) {
				m_selectedWord = w.clone();
				m_editingWord = w.clone();
				loadWord();
			}
		}
	}


	@Override
	public void keyPressed(KeyEvent e) {
	}


	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getSource().equals(m_txtSearchTool)) {
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				String word = m_txtSearchTool.getText().trim();
				String lang = (String) m_cbxLanguage.getSelectedItem();
				openWord(word, lang);
			}

		}
	}


	@Override
	public void keyTyped(KeyEvent e) {
	}


	@Override
	public void valueChanged(ListSelectionEvent e) {
		String tag = "WordToolWindow.valueChanged()";
		if (e.getSource().equals(m_lstDefinitionsBox.getSelectionModel())) {
			int index = m_lstDefinitionsBox.getSelectedIndex();
			if (index >= 0) {
				Log.debug(tag, "Definition Selected.");
				Definition def = m_editingWord.getDefinition(index);
				Log.debug(tag, def.getPartOfSpeech() + " " + def.getDefinition());
				m_txtDefinitionDescription.setText(def.getDefinition());
				m_cbxPartOfSpeech.setSelectedItem(m_editingWord.getDefinition(index).getPartOfSpeech());
			}

		} else if (e.getSource().equals(m_lstTranslationsBox.getSelectionModel())) {
			int index = m_lstTranslationsBox.getSelectedIndex();
			if (index >= 0) {
				Log.debug(tag, "Translation Selected.");
				Translation tran = m_editingWord.getTranslation(index);
				Log.debug(tag, tran.getLanguage() + " " + tran.getString());
				m_txtLanguage.setText(tran.getLanguage());
				m_txtTranslatedWord.setText(tran.getString());
			}
		}
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(m_btnUpdateWord)) {
			if (m_selectedLanguage != null && m_selectedWord != null)
				m_selectedLanguage.updateWord(m_selectedWord);

		} else if (e.getSource().equals(m_btnUpdateDefinition)) {
			if (m_editingWord != null) {
				Definition def;
				String strDef = m_txtDefinitionDescription.getText();
				PartOfSpeech posP = (PartOfSpeech) m_cbxPartOfSpeech.getSelectedItem();
				def = new Definition(posP, strDef);
				m_editingWord.updateDefinition(m_lstDefinitionsBox.getSelectedIndex(), def);
				loadWord();
			}

		} else if (e.getSource().equals(m_btnUpdateTranslation)) {
			if (m_editingWord != null) {
				Translation tran;
				String[] strTran = new String[2];
				strTran[0] = m_txtTranslatedWord.getText();
				strTran[1] = m_txtLanguage.getText();
				tran = new Translation(strTran[0], strTran[1]);
				m_editingWord.updateTransation(m_lstTranslationsBox.getSelectedIndex(), tran);
				loadWord();
			}
		}
	}
}
