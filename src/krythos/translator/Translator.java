package krythos.translator;

import krythos.translator.database.Database;
import krythos.translator.gui.MainWindow;

public class Translator {

	public static void main(String[] args) {
		/*Language_old al_database = new Language_old();

		print("Loading Language...");
		al_database.loadLanguage("English");

		print("Successful Load: " + al_database.isLanguageLoaded());
		print("Database: ");
		print(al_database.toString());*/
		
		//Database db = Database.get();
		//db.loadAllLanguages();

		Database db = Database.get();
		db.loadPartsOfSpeech();
		db.loadAllLanguages();
		new MainWindow();
	}

}
