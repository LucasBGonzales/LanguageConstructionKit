package krythos.translator.language;

public class RootWord {
	private RootType m_root;
	private String m_word;
	private String m_meaning;


	/**
	 * Initializes this RootWord with {@code RootType.prefix}, and empty
	 * {@code String}s
	 * ("") for word and meaning.
	 */
	public RootWord() {
		this(RootType.prefix, "", "");
	}


	/**
	 * Initialize RootWord with a RootType, word, and meaning.
	 * 
	 * @param r
	 */
	public RootWord(RootType r, String word, String meaning) {
		setType(r);
	}


	/**
	 * Returns this {@code RootWord}'s meaning.
	 * 
	 * @return
	 */
	public String getMeaning() {
		return m_meaning;
	}


	/**
	 * 
	 * @return {@code RootType} of this {@code RootWord}.
	 */
	public RootType getType() {
		return m_root;
	}


	/**
	 * Returns this {@code RootWord}'s word.
	 * 
	 * @return
	 */
	public String getWord() {
		return m_word;
	}


	/**
	 * Set this {@code RootWord}'s meaning.
	 * 
	 * @param meaning
	 */
	public void setMeaning(String meaning) {
		m_meaning = meaning;
	}


	/**
	 * Sets the {@code RootType} for this {@code RootWord}.
	 * 
	 * @param r {@code RootType} to assign to this {@code RootWord}.
	 */
	public void setType(RootType r) {
		m_root = r;
	}


	/**
	 * Set this {@code RootWord}'s word.
	 * 
	 * @param word
	 */
	public void setWord(String word) {
		m_word = word;
	}


	@Override
	public String toString() {
		String ret = "";


		// Type
		ret += getType().toString() + ":\n";

		// Definitions
		ret += "\tWord: ";
		ret += getWord() + "\n";

		// Translations
		ret += "\tMeaning: ";
		ret += getMeaning();

		return ret;
	}


	/**
	 * RootType is used by RootWord to ensure consistent RootTypes, that
	 * is prefix, suffix or root.
	 * 
	 * @author Lucas Gonzales
	 *
	 */
	public static enum RootType {
		prefix, suffix, root, unknown;
		
		private static final String REFERENCE = "@root_types/";

		public static RootType getFromReference(String ref) {
			switch (ref.replaceAll(REFERENCE, "")) {
			case "prefix":
				return RootType.prefix;
			case "root":
				return RootType.root;
			case "suffix":
				return RootType.suffix;
			}
			return RootType.unknown;
		}
		
		
		public static String getReference(RootType rt) {
			return REFERENCE + rt.toString().toLowerCase();
		}
		
		@Override
		public String toString() {
			switch(this) {
			case prefix:
				return "Prefix";
			case root:
				return "Root";
			case suffix:
				return "Suffix";
			default:
				return "";
			}
		}
	}
}
