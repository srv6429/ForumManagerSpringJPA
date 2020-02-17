package org.olumpos.forum.entity;

/**
 * <br>
 * @author daristote<br>
 * <br>
 * Classe qui permet d'encapsuler une un objet de type String qui représente le statut d'une requête REST<br>
 * Les méthodes des contrôleurs retourn un Objet de type ResponseEntity (JSONResponse) <br>
 * Si la reue retourne le résultat attendu, le status prend la valeur "Ok"; sinon on lui attribue la valeur "Erreur"<br>
 * Les requêtes sont envoyées à partir des pages html au moyen de jQuery et AJAX. La réponse est donc reçue sou form d'objet JSON<br> 
 * <br>
 *
 */

public class JSONResponse {

	private String status;

	//getter and setter
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
}
