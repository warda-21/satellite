package model;

import java.io.FileWriter;
import java.text.DateFormat;
import java.util.Date;

public class Rapport {
	private String RAPPORT = "Le rapport du @date@ \n" + "Nombre de données recoltées : @nbInfortion@ \n"
			+ "@Information@ " + "=========================================================\n";
	private static final String NOM_FICHIER = "raport_satellite.txt";

	public void genererRapport(String information, int nbInf) {

		Date aujourdhui = new Date();
		DateFormat fullDateFormat = DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL);
		String date = fullDateFormat.format(aujourdhui);
		RAPPORT = RAPPORT.replaceAll("@date@", date);
		RAPPORT = RAPPORT.replaceAll("@nbInfortion@", "" + nbInf);
		RAPPORT = RAPPORT.replaceAll("@Information@", information);
		try {
			FileWriter myWriter = new FileWriter(NOM_FICHIER);
			myWriter.write(RAPPORT);
			myWriter.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
