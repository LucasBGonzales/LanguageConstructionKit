package krythos.translator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import krythos.translator.database.Database;
import krythos.translator.language.Language;
import krythos.translator.language.PartOfSpeech;
import krythos.translator.language.RootWord;
import krythos.translator.language.RootWord.RootType;
import krythos.translator.language.Word;
import krythos.translator.language.Word.Definition;
import krythos.translator.language.Word.Translation;
import krythos.util.file_search.FileSearch;
import krythos.util.logger.Log;

public class FileHandler {
	private static final String TAG = "FileHandler";
	private static FileHandler m_instance;
	private String dir_languages;
	private String dir_xml;


	public FileHandler() {
		// OLD Would fail if current working directory is different from
		// program location.
		// dir_languages = System.getProperty("user.dir") + "\\Languages";
		try {
			// Get Directory
			dir_languages = new File(Translator.class.getProtectionDomain().getCodeSource().getLocation().toURI())
				.getAbsolutePath();

			// Check for bin, necessary when running debug from Eclipse. Remove
			// it.
			dir_languages.lastIndexOf("\\bin");
			if (dir_languages.lastIndexOf("\\bin") == dir_languages.length() - 4)
				dir_languages = dir_languages.substring(0, dir_languages.length() - 4);

			// Add Languages to directory.
			dir_languages += "\\Languages";


			// Get Directory
			dir_xml = new File(Translator.class.getProtectionDomain().getCodeSource().getLocation().toURI())
				.getAbsolutePath();

			// Check for bin, necessary when running debug from Eclipse. Remove
			// it.
			dir_xml.lastIndexOf("\\bin");
			if (dir_xml.lastIndexOf("\\bin") == dir_xml.length() - 4)
				dir_xml = dir_xml.substring(0, dir_xml.length() - 4);

			// Add Languages to directory.
			dir_xml += "\\xml";
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}


	/**
	 * Get unique instance of FileHandler.
	 * 
	 * @return
	 */
	public static FileHandler get() {
		if (m_instance == null)
			m_instance = new FileHandler();
		return m_instance;
	}


	/**
	 * Retrieves all the directories for the XMLs located in the Languages
	 * directory.
	 * 
	 * @return {@code String[]} Language directories.
	 */
	public String[] getLanguageDirectories() {
		File dir = new File(dir_languages);
		// Get xml files from Languages directory.
		List<File> xmls = FileSearch.getNestedFiles(dir, -1, new String[] { "xml" }, false);

		// Get absolute paths
		String[] arr_ret = new String[xmls.size()];
		for (int i = 0; i < xmls.size(); i++)
			arr_ret[i] = xmls.get(i).getAbsolutePath();

		return arr_ret;
	}


	/**
	 * Retrieves the directory for the xml file that represents the given language.
	 * Does so by comparing {@code language_name} with the string in the name
	 * attribute of the Language element within the language XMLs. Case sensitive.
	 * 
	 * <p>
	 * Returns {@code null} if the language could not be found.
	 * 
	 * @param language_name
	 * @return
	 */
	public String getLanguageDirectory(String language_name) {

		// Get xml files from Languages directory.
		String[] languages = getLanguageDirectories();

		// Search for language
		for (String dir : languages) {
			String name = XMLParser.getNameAttribute(new File(dir));
			if (name != null && name.equals(language_name)) {
				return dir;
			}
		}
		return null;
	}


	/**
	 * Returns the directory of the parts_of_speech XML file.
	 * 
	 * @return {@code String} XML file directory.
	 */
	public String getPartsOfSpeechDirectory() {
		String dir = dir_xml + "\\parts_of_speech.xml";
		if ((new File(dir)).exists())
			return dir;
		else
			return null;
	}


	/**
	 * XMLParser handles everything that has to do with the XML files. Loading,
	 * saving, etc.
	 * 
	 * @author Lucas Gonzales "Krythos"
	 *
	 */
	public static class XMLParser {
		private static final String TAG = "FileHandler.XMLParser";

		private static final String EL_ROOT = "langauge";
		private static final String EL_WORD = "word";
		private static final String EL_DEFINITION = "definition";
		private static final String EL_PART_OF_SPEECH = "part_of_speech";
		private static final String EL_DESCRIPTION = "description";
		private static final String El_TRANSLATION = "translation";
		private static final String EL_ROOT_WORD = "root";
		private static final String ATTR_ROOT__NAME = "name";
		private static final String ATTR_WORD__NAME = "name";
		private static final String ATTR_PART_OF_SPEECH__PART = "part";
		private static final String ATTR_ROOT_WORD__NAME = "name";
		private static final String ATTR_ROOT_WORD__TYPE = "type";
		private static final String ATTR_ROOT_WORD__DEFINITION = "definition";
		private static final String ATTR_TRANSLATION__LANGUAGE = "language";
		private static final String ATTR_TRANSLATION__WORD = "word";


		/**
		 * Retrieves the name of the language from the XML located at the given
		 * {@code File f}. Returns the name as a string.
		 * 
		 * @param f The file to search. Must be an XML.
		 * @return {@code String} The name of the language.
		 */
		public static String getNameAttribute(File f) {
			try {
				// Select File
				File inputFile = f;

				// Set up parser
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(inputFile);
				doc.getDocumentElement().normalize();

				// Get Name
				return doc.getDocumentElement().getAttribute("name");
			} catch (Exception e) {
				e.printStackTrace();
			}

			return null;
		}


		/**
		 * Parses the language XML file from the given {@code File}.
		 * 
		 * @param f {@code File} to be parsed.
		 * @return {@code Language} that contains the information from the given file.
		 */
		public static Language parseLanguage(File f) {
			try {
				// Select File
				File inputFile = f;

				// Set up parser
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(inputFile);
				doc.getDocumentElement().normalize();

				// Get Name
				Language lang = new Language(doc.getDocumentElement().getAttribute("name"));

				// Get all word elements
				NodeList nWords = doc.getElementsByTagName("word");

				// Parsing word elements
				for (int temp = 0; temp < nWords.getLength(); temp++) {
					// Get item from list
					Node nNode = nWords.item(temp);

					Word w = new Word();

					// Get Data
					if (nNode.getNodeType() == Node.ELEMENT_NODE) {
						Element eElement = (Element) nNode;

						// Get Word
						w.setWord(eElement.getAttribute("name"));

						// Getting the definitions
						NodeList nDefinitions = eElement.getElementsByTagName("definition");
						for (int i = 0; i < nDefinitions.getLength(); i++) {
							Element e = (Element) nDefinitions.item(i);

							// Getting Content
							String[] def = new String[2];

							def[0] = ((Element) e.getElementsByTagName("part_of_speech").item(0)).getAttribute("part");
							def[0] = PartOfSpeech.getFromReference(def[0]);

							def[1] = e.getElementsByTagName("description").item(0).getTextContent();

							// Add to word;
							w.addDefinition(new Definition(Database.get().getPartOfSpeechFromID(def[0]), def[1]));
						}

						// Getting the Translations
						NodeList nTranslations = eElement.getElementsByTagName("translation");
						for (int i = 0; i < nTranslations.getLength(); i++) {
							Element e = (Element) nTranslations.item(i);

							// Getting Content
							String[] translation = new String[2];
							translation[0] = e.getAttribute("word");
							translation[1] = e.getAttribute("language");

							// Add to word;
							w.addTranslation(new Translation(translation[0], translation[1]));
						}

						// Add word to language.s
						lang.addWord(w);
					}
				}

				// Getting the roots
				NodeList nRoots = doc.getElementsByTagName("root");
				for (int i = 0; i < nRoots.getLength(); i++) {
					Element e = (Element) nRoots.item(i);

					// Getting Content
					RootWord root = new RootWord();
					root.setWord(e.getAttribute("name"));
					root.setType(RootWord.RootType.getFromReference(e.getAttribute("type")));
					root.setMeaning(e.getAttribute("definition"));

					// Add to language
					lang.addRoot(root);
				}

				return lang;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}


		/**
		 * Parses the given {@code File} as the Parts of Speech xml file, and returns it
		 * as a {@code List<PartOfSpeech>}.
		 * 
		 * @param f {@code File} to be parsed.
		 * @return {@code List<PartOfSpeech>} that contains the information from the
		 *         given file.
		 */
		public static List<PartOfSpeech> parsePartsOfSpeech(File f) {
			try {
				List<PartOfSpeech> lstReturn = new ArrayList<PartOfSpeech>();

				// Select File
				File inputFile = f;

				// Set up parser
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(inputFile);
				doc.getDocumentElement().normalize();

				// Get all part_of_speech elements
				NodeList nList = doc.getElementsByTagName("part_of_speech");

				// Parsing part_of_speech elements
				for (int temp = 0; temp < nList.getLength(); temp++) {
					// Item Node
					Node nItem = nList.item(temp);

					// Get data in this element.
					if (nItem.getNodeType() == Node.ELEMENT_NODE) {
						// Data to collect
						String name, title, function;

						Element eElement = (Element) nItem;

						name = eElement.getAttribute("name");
						title = eElement.getElementsByTagName("title").item(0).getTextContent();
						function = eElement.getElementsByTagName("function").item(0).getTextContent();

						lstReturn.add(new PartOfSpeech(name, title, function));
					}
				}
				return lstReturn;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}


		/**
		 * Saves the given language to the given directory as an XML
		 * 
		 * @param language
		 * @param directory
		 * @return {@code true} if successfully saved, {@code false} if save failed.
		 */
		public static boolean saveLanguage(Language language, String directory) {
			Document dom;

			// instance of a DocumentBuilderFactory
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			try {
				// use factory to get an instance of document builder
				DocumentBuilder db = dbf.newDocumentBuilder();
				// create instance of DOM
				dom = db.newDocument();

				// create the root element
				Element eRoot = dom.createElement(EL_ROOT);
				eRoot.setAttribute(ATTR_ROOT__NAME, language.getName());

				// Parse Words
				for (int i_l = 0; language.getWord(i_l) != null; i_l++) {
					Word word = language.getWord(i_l);

					Element eWord = dom.createElement(EL_WORD);
					eWord.setAttribute(ATTR_WORD__NAME, word.getWordAsString());

					// Parse Definitions of Words
					for (int i_d = 0; word.getDefinition(i_d) != null; i_d++) {
						Definition def = word.getDefinition(i_d);

						Element eDefinition = dom.createElement(EL_DEFINITION);

						Element ePOS = dom.createElement(EL_PART_OF_SPEECH);
						ePOS.setAttribute(ATTR_PART_OF_SPEECH__PART, def.getPartOfSpeech().getID());

						Element eDescription = dom.createElement(EL_DESCRIPTION);
						eDescription.appendChild(dom.createTextNode(def.getDefinition()));

						eDefinition.appendChild(ePOS);
						eDefinition.appendChild(eDescription);
						eWord.appendChild(eDefinition);
					}

					// Parse Translations of Words
					for (int i_t = 0; word.getTranslation(i_t) != null; i_t++) {
						Translation translation = word.getTranslation(i_t);

						Element eTransation = dom.createElement(El_TRANSLATION);
						eTransation.setAttribute(ATTR_TRANSLATION__LANGUAGE, translation.getLanguage());
						eTransation.setAttribute(ATTR_TRANSLATION__WORD, translation.getString());

						eWord.appendChild(eTransation);
					}

					// Add word to root.
					eRoot.appendChild(eWord);
				}

				// Parse Roots
				for (int i_l = 0; language.getRoot(i_l) != null; i_l++) {
					RootWord root = language.getRoot(i_l);

					Element eRootWord = dom.createElement(EL_ROOT_WORD);
					eRootWord.setAttribute(ATTR_ROOT_WORD__NAME, root.getWord());
					eRootWord.setAttribute(ATTR_ROOT_WORD__TYPE, RootType.getReference(root.getType()));
					eRootWord.setAttribute(ATTR_ROOT_WORD__DEFINITION, root.getMeaning());

					// Add root word to root element.
					eRoot.appendChild(eRootWord);
				}

				dom.appendChild(eRoot);

				// Save File.
				try {
					Transformer tr = TransformerFactory.newInstance().newTransformer();
					tr.setOutputProperty(OutputKeys.INDENT, "yes");
					tr.setOutputProperty(OutputKeys.METHOD, "xml");
					// tr.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
					tr.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

					// send DOM to file
					tr.transform(new DOMSource(dom), new StreamResult(new FileOutputStream(directory)));

				} catch (TransformerException te) {
					Log.error(TAG + ".saveLanguage()", te.getMessage());
				} catch (IOException ioe) {
					Log.error(TAG + ".saveLanguage()", ioe.getMessage());
				}

				return true;
			} catch (ParserConfigurationException pce) {
				Log.error(TAG + ".saveLanguage()", "UsersXML: Error trying to instantiate DocumentBuilder " + pce);

				return false;
			}
		}
	}
}
