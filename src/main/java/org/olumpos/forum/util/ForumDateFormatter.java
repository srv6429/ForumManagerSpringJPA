package org.olumpos.forum.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

/**
 * 
 * @author daristote
 * 
 * Classe qui contient des fonctions qui permettent de convetir des Objet de type LocalDateTime en String
 * ou des date sous forme de String en LocalDateTime
 * 
 * Les Objets de type LocatDateTime donnent la date et l'heure
 *
 */

public class ForumDateFormatter {

	
	/**
	 * fonction qui prend en paramètre un objet de type LocalDateTime et qui la transforme en
	 * une String 
	 * @param dateTime: un objet de type LocalDateTime
	 * @return : une String de format: yyyy-MM-dd HH:mm:ss
	 */
	public static String fromLocalDateTimeToString(LocalDateTime dateTime) {
		DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)
				.withLocale(Locale.getDefault());
		return formatter.format(dateTime);
	}
	
	//*********************************************************************************************************************
	//*********************************************************************************************************************
	
	/**
	 * fonction qui "parse" une String représentant une date et l'heure et qui la transforme en 
	 * un objet de type LocalDateTime
	 *  
	 * @param dateStr: Une String représentant la date de format: yyyy-MM-dd HH:mm:ss
	 * @return: un objet de type LocalDateTime
	 */
	public static LocalDateTime fromStringToLocalDateTime(String dateStr) {
		
		//définit le formatter en fonction du format de date attendu: yyyy-MM-dd HH:mm:ss
		DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM).withLocale(Locale.getDefault());
		//retourne un objet de type LocalDateTime 
		return formatter.parse(dateStr, LocalDateTime::from);
	}

	//*********************************************************************************************************************
	//*********************************************************************************************************************
	
	/**
	 * fonction qui "parse" une String représentant une date et l'heure avec les secondes et qui la transforme en 
	 * un objet de type LocalDateTime pour la retransformer en String représentant la date et l'heure mais 
	 * tronquée des secondes 
	 * 
	 * remarque: permet d'éviter les possibles erreurs en manipulant les chaînes et les sous-chaînes représentant des dates
	 *  
	 * @param dateString: une String de format: "yyyy-MM-dd HH:mm:ss"
	 * @return : une String de format: "yyyy-MM-dd HH:mm"
	 */
	public static String fromLongStringToLocalDateTimeShortString(String dateString) {
		
		
		//définit le formatter en fonction du format de date attendu
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
		
		//parse la chaîne et créé un objet de type LocalDateTime
		LocalDateTime dateTime = formatter.parse(dateString, LocalDateTime::from);
		
		///redéfinit le formatter cette fois sans les secondes 
		formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		
	
		//retourne la date et l'heure encapsulé dans un objet de type LocalDateTime sous forme de chaîne formattée
		return formatter.format(dateTime);
		//return "";
	}

	//*********************************************************************************************************************
	//*********************************************************************************************************************
	
	//Test local
	public static void main(String[] args) {
		System.out.println(ForumDateFormatter.fromLocalDateTimeToString(LocalDateTime.now()));
		
		System.out.println(ForumDateFormatter.fromStringToLocalDateTime("2019-07-22 12:45:34"));
		
		System.out.println(ForumDateFormatter.fromLongStringToLocalDateTimeShortString("2019-07-22 12:45:09.0"));
		
		//System.out.println(BlogDateFormatter.fromLongStringToLocalDateTimeShortString(BlogDateFormatter.fromLocalDateTimeToString(LocalDateTime.now())));
		
	}
	
}
