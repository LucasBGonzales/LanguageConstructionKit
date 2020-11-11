package krythos.translator.language;

import java.util.ArrayList;
import java.util.List;

public class Language {
	// Name of the loaded language
	private String m_language_name;

	// Word array for the language database
	private List<Word> m_arr_lang_words;

	// RootWord array for root words.
	private List<RootWord> m_arr_root_words;


	/**
	 * Initializes Database without a loaded language. Will not be
	 * accessible until a language is loaded with {@code loadLanguage()}.
	 */
	public Language(String name) {
		m_language_name = name;
		m_arr_lang_words = new ArrayList<Word>();
		m_arr_root_words = new ArrayList<RootWord>();
	}


	/**
	 * Adds the given {@code RootWord} object to the database.
	 * 
	 * TODO Test for duplicates.
	 * 
	 * @param root
	 */
	public void addRoot(RootWord root) {
		m_arr_root_words.add(root);
	}


	/**
	 * Adds the given {@code Word} object to the database.
	 * 
	 * @param word
	 */
	public void addWord(Word word) {
		Word w = getWord(word.getWordAsString());
		if (w == null)
			m_arr_lang_words.add(word);
		else {
			w.addDefinitions(word.getDefinitions());
			w.addTranslations(word.getTranslations());
		}
	}


	/**
	 * Returns the loaded language's name.
	 * If no language is loaded, returns null.
	 * 
	 * @return {@code String} Language Name
	 */
	public String getName() {
		return m_language_name;
	}


	/**
	 * Returns the word at the given index, or {@code null} if the index
	 * is out-of-range.
	 * 
	 * @param index Index of the word.
	 * @return {@code Word} at the given index, or {@code null} if index
	 *         is out-of-range.
	 */
	public RootWord getRoot(int index) {
		return (index >= 0 && index < m_arr_root_words.size()) ? m_arr_root_words.get(index) : null;
	}


	/**
	 * Returns the word at the given index, or {@code null} if the index
	 * is out-of-range.
	 * 
	 * @param index Index of the word.
	 * @return {@code Word} at the given index, or {@code null} if index
	 *         is out-of-range.
	 */
	public Word getWord(int index) {
		return (index >= 0 && index < m_arr_lang_words.size()) ? m_arr_lang_words.get(index) : null;
	}

	/*
	 * TODO
	 * public RootWord getRootWord(String word)
	 * {
	 * 
	 * }
	 */


	/**
	 * Will update the word in this language that matches the passed
	 * {@code Word}.
	 * 
	 * @param word {@code Word} to update in this language.
	 * @return {@code true} if successfully updated the word.
	 *         {@code false} if the update was unsuccessful.
	 *         <p>
	 *         Update may have been unsuccessful because the passed word
	 *         was null, or because the word didn't exist in the language
	 *         and thus couldn't be updated.
	 */
	public boolean updateWord(Word word) {
		// Check word isn't null
		if (word == null)
			return false;

		// Check word exists in language.
		Word w = getWord(word.getWordAsString());
		if (w == null)
			return false;

		// Remove the old word, add new word.
		removeWord(word.getWordAsString());
		addWord(word);
		return true;
	}


	/**
	 * Remove the given {@code Word} from this language.
	 * 
	 * @param word {@code String}
	 * @return {@code true} if the word was successfully removed,
	 *         {@code false} if unsuccessful.
	 *         <p>
	 *         Removal is unsuccessful if the word didn't exist in the
	 *         language to begin with.
	 */
	public boolean removeWord(String word) {
		for (int i = 0; i < m_arr_lang_words.size(); i++)
			if (m_arr_lang_words.get(i).getWordAsString().toLowerCase().equals(word.toLowerCase())) {
				m_arr_lang_words.remove(i);
				return true;
			}
		return false;
	}


	/**
	 * Gets a {@code Word} based on the {@code String} provided.
	 * Returns {@code null} if there is no associative {@code Word}.
	 * <p>
	 * Search is case-insensitive.
	 * <p>
	 * The Word returned is a clone. To change a word in the language, use
	 * {@code updateWord(Word)}.
	 * 
	 * @param word
	 * @return {@code Word} or {@code null}
	 */
	public Word getWord(String word) {
		for (Word w : m_arr_lang_words)
			if (w.getWordAsString().toLowerCase().equals(word.toLowerCase()))
				return w.clone();

		return null;
	}


	@Override
	public String toString() {
		// Build return String.
		String ret = "";

		// Language Name
		ret += getName() + "\n";

		// Build Dictionary
		for (Word w : m_arr_lang_words)
			ret += w.toString() + "\n";

		ret += "\n";

		// Build Roots
		for (RootWord rt : m_arr_root_words)
			ret += rt.toString() + "\n";


		return ret;
	}
}

