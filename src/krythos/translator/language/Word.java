package krythos.translator.language;

import java.util.ArrayList;
import java.util.List;

public class Word {

	private String m_word;
	private List<Definition> m_definitions;
	private List<Translation> m_translations;


	public Word() {
		m_word = "";
		m_definitions = new ArrayList<Definition>();
		m_translations = new ArrayList<Translation>();
	}


	public Word(String word, List<Definition> definitons, List<Translation> translations, PartOfSpeech partOfSpeech) {
		m_word = word;
		m_definitions = definitons;
		m_translations = translations;
	}


	/**
	 * Adds a definition to this word. Will test against existing
	 * definitions to avoid duplicates. If a duplicate is found,
	 * {@code boolean false} is returned. If the definition is added to
	 * the
	 * word, {@code true} is returned.
	 * 
	 * @param definition {@code Definition}
	 * @return {@code boolean true} if definition was added to the word.
	 */
	public boolean addDefinition(Definition definition) {
		for (Definition d : m_definitions)
			if (d.equals(definition))
				return false;
		m_definitions.add(definition);
		return true;
	}


	public boolean updateDefinition(int index, Definition definition) {
		if (index < 0 || index >= m_definitions.size())
			return false;

		m_definitions.get(index).setDefinition(definition.getDefinition());
		m_definitions.get(index).setPartOfSpeech(definition.getPartOfSpeech());

		return true;
	}


	public boolean updateTransation(int index, Translation translation) {
		if (index < 0 || index >= m_translations.size())
			return false;

		m_translations.get(index).setLanguage(translation.getLanguage());
		m_translations.get(index).setString(translation.getString());

		return true;
	}


	/**
	 * Will use {@code addDefinition()} to add each definition in the
	 * given {@code List} to this word.
	 * 
	 * @param definitions
	 */
	public void addDefinitions(List<Definition> definitions) {
		for (Definition def : definitions)
			addDefinition(def);
	}


	/**
	 * Adds a translation to this word. Will test against existing
	 * translations to avoid duplicates. If a duplicate is found,
	 * {@code boolean false} is returned. If the translation is added to
	 * the
	 * word, {@code true} is returned.
	 * 
	 * @param translation {@code String[]}
	 * @return {@code boolean true} if definition was added to the word.
	 */
	public boolean addTranslation(Translation translation) {
		for (Translation t : m_translations)
			if (t.equals(translation))
				return false;
		m_translations.add(translation);
		return true;
	}


	/**
	 * Will use {@code addTranslation()} to add each translation in the
	 * given {@code ArrayList} to this word.
	 * 
	 * @param translations
	 */
	public void addTranslations(List<Translation> translations) {
		for (Translation tran : translations)
			addTranslation(tran);
	}


	public Word clone() {
		Word ret = new Word();
		ret.setWord(m_word);
		ret.addDefinitions(new ArrayList<Definition>(m_definitions));
		ret.addTranslations(new ArrayList<Translation>(m_translations));
		return ret;
	}


	/**
	 * Returns a specific definition of this word. If the index provided
	 * is out-of-bounds (< zero or >= definitions.size()), this method
	 * will return null.
	 * 
	 * @param index of the definition to return.
	 * @return {@code Definition} definition at {@code index}. or
	 *         {@code null} if out-of-bounds.
	 */
	public Definition getDefinition(int index) {
		// If out-of-bounds, return null
		if (index >= m_definitions.size() || index < 0)
			return null;

		return m_definitions.get(index);
	}


	/**
	 * Returns all the definitions of this word.
	 * 
	 * @return {@code List<Definition>} of all definitions.
	 */
	public List<Definition> getDefinitions() {
		return m_definitions;
	}


	/**
	 * Returns the definitions of this word that are of the given grammar.
	 * 
	 * @param grammar
	 * @return {@code List<Definition>} of all definitions with given
	 *         grammar.
	 */
	public List<Definition> getDefinitions(String grammar) {
		List<Definition> ret = new ArrayList<Definition>();

		for (Definition d : m_definitions)
			if (d.getPartOfSpeech().equals(grammar))
				ret.add(d);

		return ret;
	}


	/**
	 * Returns a specific translation of this word. If the index provided
	 * is out-of-bounds (< zero or >= translations.size()), this method
	 * will return null.
	 * 
	 * @param index of the translation to return.
	 * @return {@code Translation} translation at {@code index}.
	 */
	public Translation getTranslation(int index) {
		// If out-of-bounds, return null
		if (index >= m_translations.size() || index < 0)
			return null;

		return m_translations.get(index);
	}


	/**
	 * Returns all the translations of this word.
	 * 
	 * @return {@code List<Translation>} of all definitions.
	 */
	public List<Translation> getTranslations() {
		return m_translations;
	}


	/**
	 * Returns the definitions of this word that are of the given
	 * language.
	 * 
	 * @param language
	 * @return {@code List<Translation>} of all translations of the given
	 *         language.
	 */
	public List<Translation> getTranslations(String language) {
		List<Translation> ret = new ArrayList<Translation>();

		for (Translation d : m_translations)
			if (d.getLanguage().equals(language))
				ret.add(d);

		return ret;
	}


	/**
	 * Returns the word this object refers to.
	 * 
	 * @return {@code String} word
	 */
	public String getWordAsString() {
		return m_word;
	}


	/**
	 * Sets the {@code String} word for this Word.
	 * 
	 * @param word {@code String}
	 */
	public void setWord(String word) {
		m_word = word;
	}


	@Override
	public String toString() {
		String ret = "";

		ret += m_word + "\n";

		for (Definition def : m_definitions)
			ret += "\t" + def.getPartOfSpeech() + ": " + def.getDefinition() + "\n";

		for (Translation tran : m_translations)
			ret += "\t" + tran.getLanguage() + ": " + tran.getString() + "\n";

		ret = ret.substring(0, ret.length() - 1);

		return ret;
	}


	public static class Definition {
		private PartOfSpeech m_partOfSpeech;
		private String m_definition;


		public Definition(PartOfSpeech partOfSpeech, String definition) {
			setPartOfSpeech(partOfSpeech);
			setDefinition(definition);
		}


		public String getDefinition() {
			return m_definition;
		}


		public PartOfSpeech getPartOfSpeech() {
			return m_partOfSpeech;
		}


		public void setDefinition(String definition) {
			m_definition = definition;
		}


		public void setPartOfSpeech(PartOfSpeech partOfSpeech) {
			m_partOfSpeech = partOfSpeech;
		}


		@Override
		public boolean equals(Object definition) {
			return definition instanceof Definition ? false
					: ((Definition) definition).getDefinition().equals(this.getDefinition())
							&& ((Definition) definition).getPartOfSpeech().equals(this.getPartOfSpeech());
		}
	}


	public static class Translation {
		private String m_string, m_language;


		public Translation(String word, String language) {
			setString(word);
			setLanguage(language);
		}


		public String getLanguage() {
			return m_language;
		}


		public String getString() {
			return m_string;
		}


		public void setLanguage(String language) {
			m_language = language;
		}


		public void setString(String word) {
			m_string = word;
		}


		@Override
		public boolean equals(Object translation) {
			return translation instanceof Translation ? false
					: ((Translation) translation).getLanguage().equals(this.getLanguage())
							&& ((Translation) translation).getString().equals(this.getString());
		}
	}
}