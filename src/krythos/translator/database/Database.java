package krythos.translator.database;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import krythos.translator.FileHandler;
import krythos.translator.language.Language;
import krythos.translator.language.PartOfSpeech;
import krythos.util.logger.Log;

public class Database {
	private static Database m_instance;
	private List<Language> m_languages;
	private List<PartOfSpeech> m_partsOfSpeech;


	public Database() {
		m_languages = new ArrayList<Language>(0);
		m_partsOfSpeech = new ArrayList<PartOfSpeech>(0);
	}


	/**
	 * Returns the unique instance of Database.
	 * 
	 * @return {@code Database}
	 */
	public static Database get() {
		if (m_instance == null)
			m_instance = new Database();
		return m_instance;
	}


	/**
	 * Returns the PartOfSpeech that has the ID matching the given String.
	 * 
	 * @param str
	 * @return
	 */
	public PartOfSpeech getPartOfSpeechFromID(String str) {
		for (PartOfSpeech pos : Database.get().getPartsOfSpeech())
			if (pos.getID().equals(str))
				return pos;
		return null;
	}


	/**
	 * Attempts to add the given language to the database. Will not add the language
	 * if the language already exists in the database. If the addition is successful
	 * {@code true} is returned. If the language already exists and is thus
	 * unsuccessful, {@code false} is returned.
	 * 
	 * @param language
	 * @return
	 */
	public boolean addLanguage(Language language) {
		// Do nothing if the language exists in database
		for (Language l : m_languages)
			if (l.getName().equals(language.getName()))
				return false;

		m_languages.add(language);
		return true;
	}


	/**
	 * Adds the passed PartOfSpeech to the database. Ignores duplicates that satisfy
	 * PartOfSpeech.equals()
	 * 
	 * @param pos
	 */
	public void addPartOfSpeech(PartOfSpeech pos) {
		for (PartOfSpeech p : getPartsOfSpeech())
			if (p.equals(pos))
				return;

		m_partsOfSpeech.add(pos);
	}


	/**
	 * Return a {@code String} array of the names for all loaded languages.
	 * 
	 * @return {@code String} array of language names.
	 */
	public String[] getLangaugeNames() {
		String[] ret = new String[m_languages.size()];

		for (int i = 0; i < ret.length; i++)
			ret[i] = m_languages.get(i).getName();

		return ret;
	}


	/**
	 * Will search the languages array for the specified language. If found, that is
	 * returned. If not found, null is returned. Case-sensitive.
	 * 
	 * @param language The {@code String} to compare language names to.
	 * @return {@code LanguageDB} if found, else {@code null}.
	 */
	public Language getLanguage(String language) {
		for (Language l : m_languages)
			if (l.getName().equals(language))
				return l;
		return null;
	}


	public List<PartOfSpeech> getPartsOfSpeech() {
		return m_partsOfSpeech;
	}


	/**
	 * Loads all languages in the Languages directory and adds them to the database.
	 */
	public void loadAllLanguages() {
		String[] arr_dir = FileHandler.get().getLanguageDirectories();

		for (String dir : arr_dir) {
			Language l = FileHandler.XMLParser.parseLanguage(new File(dir));
			addLanguage(l);
		}
	}


	/**
	 * Loads the parts of speech xml and adds the Parts of Speech to the database
	 * according to addPartOfSpeech().
	 */
	public void loadPartsOfSpeech() {
		List<PartOfSpeech> lst = null;
		try {
			lst = FileHandler.XMLParser.parsePartsOfSpeech(new File(FileHandler.get().getPartsOfSpeechDirectory()));
		} catch (NullPointerException e) {
			Log.error(e.getMessage());
		}

		for (PartOfSpeech pos : lst)
			addPartOfSpeech(pos);
	}

}
