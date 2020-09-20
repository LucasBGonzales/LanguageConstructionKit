package krythos.translator.language;

public class PartOfSpeech {
	private String m_ID;
	private String m_name;
	private String m_function;


	public PartOfSpeech(String id, String name, String function) {
		m_ID = id;
		m_name = name;
		m_function = function;
	}


	public String getID() {
		return m_ID;
	}


	public String getName() {
		return m_name;
	}


	public String getFunction() {
		return m_function;
	}


	// TODO Implement referencing to PartsOfSpeech XML EDIT:: may remove referencing. Dumb. Just use IDs.
	public static String getFromReference(String s) {
		return s.replaceAll("@parts_of_speech/", "");
	}


	@Override
	public boolean equals(Object obj) {
		PartOfSpeech p;
		p = obj instanceof PartOfSpeech ? (PartOfSpeech) obj : null;
		return p != null && getID().equals(p.getID()) ? true : false;
	}


	public String toString() {
		return getName() + ": \"" + getFunction() + "\"";
	}
}
