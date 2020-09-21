package krythos.translator;

import krythos.translator.database.Database;
import krythos.translator.gui.MainWindow;

public class Translator {

	public static void main(String[] args) {
		Database db = Database.get();
		db.loadPartsOfSpeech();
		db.loadAllLanguages();
		new MainWindow();
	}

}
