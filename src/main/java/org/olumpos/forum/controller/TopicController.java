package org.olumpos.forum.controller;


import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.olumpos.forum.dao.PostDAO;
import org.olumpos.forum.dao.TopicDAO;
import org.olumpos.forum.dao.UserDAO;
import org.olumpos.forum.entity.Post;
import org.olumpos.forum.entity.Topic;
import org.olumpos.forum.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;



/************************************************************************************************************************************************
 * <br>
 * @author daristote<br>
 * <br>
 * Classe qui définit des méthodes qui prennent en charge les requêtes concernant l'affichage, l'ajout et l'édition des thèmes<br> 
 * <br>
 * Annotée avec @Controller elle indique au gestionnaire central que les requêtes peuvent être gérées par des méthodes de cette classe<br>
 * <br>
 * Le chemin (Path) de base est "/topics"<br>
 * <br>
 * Les autres chemins définis dans cette classe sont relatifs à celui-ci<br>
 * <br>
 * Le constructeur est annoté avec @Autowired indiquant qu'il doit être appelé lors du déploiement de l'application par le serveur<br>
 * Cet appel permet d'initialiser les variables globales 'TopicDAO topicDAO' et 'PostDAO postDAO'  utilisées pour <br>
 * accéder à la base de données <br>
 * <br>
 * Remarque: on aurait pu simplement placer l'annotation @Autowired devant la déclaration des variables (ex: @Autowired TopicDAO topicDAO)<br> 
 * ce qui aurait assuré l'injection, i.e. l'initialisation de la variable au déploiement<br>
 * <br>
 * Toutefois pour tester avec JUnit, on doit initialiser manuellement les membres, ce qui est fait en appelant explicitement le contructeur<br> 
 * de TopicController et qui permet, lors de son instanciation d'initialiser les variables globales permettant l'accès à la base  de données.<br>
 * <br>
 *
 *************************************************************************************************************************************************/

@Controller
//Path de base
@RequestMapping("/topics")
public class TopicController {

	Logger logger =  Logger.getLogger(TopicController.class.getName());
	
//	@Autowired
	TopicDAO topicDAO;
	
//	@Autowired
	PostDAO postDAO;

	//Constructeur appelé lors du déploiement
	@Autowired
	public TopicController(TopicDAO topicDAO, PostDAO postDAO) {
		this.topicDAO = topicDAO;
		this.postDAO =  postDAO;
	}
	
	
	//*********************************************************************************************************************
	//*********************************************************************************************************************

	/**********************************************************************************************************************
	 * <br>
	 * Méthode qui permet de gérer un requête '/topics' avec la méthode GET<br>
	 * <br>
	 * Cette méthode récupère la liste de tous les topics ouverts de la bd<br>
	 * <br>
	 * Appel à la fonction getAllOpenTopics() de la classe topicDAO<br>
	 * <br>
	 * retourne la liste des topics trouvés<br>
	 * <br>
	 * @param model: (Model) représente le modèle de l'application<br> 
	 * <br>
	 * @return: List<Topic> topicList: la liste contenant tous les topic ouverts de la bd<br>
	 * <br>
	 * 
	 **********************************************************************************************************************/
	
//	@RequestMapping(method = RequestMethod.GET) //identique
	@GetMapping
	private List<Topic> topics(Model model) {
		
		logger.log(Level.INFO, "in getTopics annotated with RequestMapping");

		List<Topic> topicList =  topicDAO.getAllOpenTopics();		
		
//		logger.log(Level.INFO, "topicList size: " + topicList.size());
		
	//	model.addAttribute("topicList", topicList); //pas nécessaire
		
		return topicList;

	}
	
	
	
	//*********************************************************************************************************************
	//*********************************************************************************************************************


	/**********************************************************************************************************************
	 * <br>
	 * Méthode qui permet  de gérer un requête '/topic/{id}' avec la méthode GET<br>
	 * <br>
	 * Cette méthode récupére le topic spécifié par l'id transmi dans l'url et affecté au param;tre id<br>
	 * <br>
	 * L'annotation @PathVariable permet d'associer la valeu transmise dans l'url  à la variable paramétrée de la fonction<br>
	 * <br>
	 * Appel à la fonction getTopic(int) de la classe topicDAO<br>
	 * <br>
	 * retourne le topic trouvé ou null si l'id ne correspond pas à un topic ouvert<br>
	 * <br>
	 * @param id: (int): l'idetifiant du topic<br>
	 * @param model: (Model): représente le modèle de l'application <br>
	 * <br>
	 * @return: - String "{status:ok}" une chaîne représetnant un objet json, si le topic a été trouvé<br>
	 * 			- String "{status:error}" sinon<br>
	 * <br>
	 **********************************************************************************************************************/
	
	@RequestMapping(value = "/topic/{id}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody String getTopic(@PathVariable int id, Model model) {
		

		logger.log(Level.INFO, "in getTopic annotated with RequestMethod.GET: topic id:  " + id);
		//obtention du Topic
		Topic topic =  topicDAO.getTopic(id);
		
		//si non nul
		if(topic != null) {
			//ajout comme attribut du model
			model.addAttribute("topic", topic);
			
			return "{topic:success}";
		}
		
		//échec
		return "{status:ok}";
	}

	//*********************************************************************************************************************
	//*********************************************************************************************************************
	
	/**********************************************************************************************************************
	 * <br>
	 * Méthode qui permet de gérer un requête '/addTopic/{userId}/{title}/{postTitle}/{comment}' avec la méthode POST<br>
	 * <br>
	 * Variante de la suivante mais les paramètre sont insérés dans l'url et sont récupéres dans les variables/paramètres<br>
	 * de la fonction, i.e. annotés avec @PathVariable<br>
	 * <br>
	 * Cette méthode est appelée lorsque l'utilisateur soumet le formulaire d'ajout d'un topic<br>
	 * <br>
	 * Un topic ne peut être ajouté sans un premier post<br>
	 * On effectue donc une opération en deux temps: on insère d'abord le topi puis si c'est réussi on insère le premier post<br>
	 * On doit donc utiliser une transaction pour s'assurer que les deux ajouts sont effectués avec succès avant de les enregistrer dans la be<br>
	 * On respecte ainsi le principe de l'atomicité de la transaction<br>
	 * <br>
	 * L'appel à la fonction topicDAO.addTopic() retourne un entier supérieur à 0 si l'opération est réussie<br>
	 *  <br>
	 * On récupère ensuite les derniers topic et post enregistrer pour obtenir leur identifiant et enregistrer l'identifiant du topic<br>
	 * dans la table post<br>
	 * <br>	  
	 * @param topicTitle: (String): le titre du topic<br>
	 * @param postTitle: (String): le titre du post<br>
	 * @param comment: (String): le commentaire du post<br>
	 * @param userId: (int): l'identifiant de l'utilisateur<br>
	 * @param model: (Model): représente le modèle de l'application<br> 
	 * <br>
	 * @return: - String "{status:ok}" une chaîne représetnant un objet de type json, si les topic et post ont été enregistrés avec succès<br>
	 * 			- String "{status:error}" sinon<br>
	 * <br>
	 **********************************************************************************************************************/
	
	@RequestMapping(value = "/addTopic/{topicTitle}/{postTitle}/{comment}/{userId}", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody String add(@PathVariable String topicTitle, @PathVariable String postTitle, @PathVariable  String comment, @PathVariable int userId, Model model) {
	
		logger.log(Level.INFO, "in addTopic annotated with RequestMethod.POST");
//		logger.log(Level.INFO, "in addTopic annotated with RequestMethod.POST ======================>  userId:  " + userId);
//		logger.log(Level.INFO, "in addTopic annotated with RequestMethod.POST ======================>  topic title:  " + topicTitle);
//		logger.log(Level.INFO, "in addTopic annotated with RequestMethod.POST ======================>  post title:  " + postTitle);
//		logger.log(Level.INFO, "in addTopic annotated with RequestMethod.POST ======================>  postComment:  " + comment);
						
		//instanciation du nouveau topic et des champs title et creatorId
		Topic topic =  new Topic();
		topic.setTitle(topicTitle);
		topic.setCreatorId(userId);
			
		//instanciation du nouveau post et des champs title, comment et userId
		Post post = new Post();
		post.setTitle(postTitle);
		post.setBody(comment);
		post.setUserId(userId);
	//	post.setIsActive((byte)1);
		
		int result = topicDAO.addTopic(topic, post);

		if(result > 0) {
			//on récupère l'identifiant des derniers topic et post insérés
			int lastTopicId =  topicDAO.getLastInsertedTopicId();
			int lastPostId =  postDAO.getLastInsertedPostId();
			
			//s'ils sont valides
			if(lastPostId > 0 && lastTopicId > 0) {
				//mettre à jour le topic id pour la dernièrer entrée de la table post
				post =  postDAO.getPost(lastPostId);
				post.setTopicId(lastTopicId);
				
				//mise à jour dans la bd
				result = postDAO.updatePost(post);
				
				//logger.log(Level.INFO, "in addTopic annotated with RequestMethod.POST: ======================>: new Topic/Post added result: " + result);
			}
			
		}
		//succès
		if(result > 0) {
			return "{\"status\":\"ok\"}";
		}

		//échec
		if(topic.messageExists()) {
			model.addAttribute("errorMessage", topic.getMessage());
		} else if (post.messageExists()) {
			model.addAttribute("errorMessage", post.getMessage());
		}
		return "{\"status\":\"error\"}";

	}
	//*********************************************************************************************************************
	//*********************************************************************************************************************

	/**********************************************************************************************************************
	 * <br>
	 * Méthode qui permet de gérer un requête '/addTopic' avec la méthode POST<br>
	 * <br>
	 * Variante de la précédent mais les paramètre sont transmis et enregistrés dans une collection de type Map(String, String)<br>
	 * avec les clés-valeurs <br>
	 * <br>
	 * Cette méthode n'est pas utilisée mais a été testée avec succès et peut être une alternative à la précédente<br>
	 * <br>
	 * Un topic ne peut être ajouté sans un premier post<br>
	 * On effectue donc une opération en deux temps: on insère d'abord le topic puis si c'est réussi on insère le npremier post<br>
	 * On doit donc utiliser une transaction pour s'assurer que les deux ajouts sont effectués avec succès avant de les enregistrer dans la be<br>
	 * On respecte ainsi le principe de l'atomicité de la transaction<br>
	 * <br>
	 * Le titre du nouveau topic, ainsi que le titre du post et le commentaire sont enregistrés dans les paramètres transmis<br>
	 * par la fonction javascipt AJAX <br>
	 * On peut alors les récupérer au moyen d'un objet de type Map(String, String) annoté de @RequestParam pour signifie que les paramètres<br>
	 * de la requête sont inclus dans la collection<br>
	 * <br>
	 * On peut par la suite récupérer ces valeurs avec les clés c'est-à-dire les noms utilisés pour chacun des paramètres<br>
	 * lors de la transmission de la requête<br>
	 * <br>
	 *  L'appel à la fonction topicDAO.addTopic() retourne un entier supérieur à 0 si l'opération est réussie<br>
	 *  <br>
	 *  On récupère ensuite les derniers topic et post enregistrer pour obtenir leur identifiant et enregistrer l'identifiant du topic<br>
	 *  dans la table post	 <br>
	 * <br>	  
	 * @param params: (Map(String, String)): une collection de type Map (interface) contenant les clés-valeurs de paramètres<br>
	 * @param model: (Model): représente le modèle de l'application <br>
	 * <br>
	 * @return: - String "{status:ok}" une chaîne représetnant un objet de type json, si les topic et post ont été enregistrés avec succès
	 * 			- String "{status:error}" sinon
	 * <br>
	 **********************************************************************************************************************/
	
	@RequestMapping(value = "/addTopic", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody String addTopic(@RequestParam Map<String, String> params, Model model) {

		logger.log(Level.INFO, "in addTopic annotated with RequestMethod.POST: param size:  " + params.size());
				
		String topicTitle =  params.get("topicTitle");
		String postTitle =  params.get("postTitle");
		String postBody =  params.get("postBody");
		int userId =  Integer.parseInt(params.get("userId"));
		
//		logger.log(Level.INFO, "in addTopic(2) annotated with RequestMethod.POST: ======================>:  " + userId);
//		logger.log(Level.INFO, "in addTopic(2) annotated with RequestMethod.POST: ======================>:  " + topicTitle);
//		logger.log(Level.INFO, "in addTopic(2) annotated with RequestMethod.POST: ======================>:  " + postTitle);
//		logger.log(Level.INFO, "in addTopic(2) annotated with RequestMethod.POST: ======================>:  " + postBody);
		
		
		Topic topic =  new Topic();
		topic.setTitle(topicTitle);
		topic.setCreatorId(userId);
		
		Post post = new Post();
		post.setTitle(postTitle);
		post.setBody(postBody);
		post.setUserId(userId);
			
		//ajout du topic et post dans la bd
		int result = topicDAO.addTopic(topic, post);
	
		if(result > 0) {
			
			//mise à jour du topic_id pour la denière entrée de la table post
			int lastTopicId =  topicDAO.getLastInsertedTopicId();
			int lastPostId =  postDAO.getLastInsertedPostId();
			
			if(lastPostId > 0 && lastTopicId > 0) {
				
				post =  postDAO.getPost(lastPostId);
				post.setTopicId(lastTopicId);
				
				result = postDAO.updatePost(post);
				logger.log(Level.INFO, "in addTopic annotated with RequestMethod.POST: ======================>: new Topic/Post added result: " + result);
			}
			
		}
		//succès
		if(result > 0) {
			return "{\"status\":\"ok\"}";
		}

		//échec
		if(topic.messageExists()) {
			model.addAttribute("errorMessage", topic.getMessage());
		} else if (post.messageExists()) {
			model.addAttribute("errorMessage", post.getMessage());
		}
		return "{\"status\":\"error\"}";

	}
	
	
	//*********************************************************************************************************************
	//*********************************************************************************************************************
	

	/**********************************************************************************************************************
	 * <br>
	 * Méthode qui permet  de gérer un requête '/updateTopic/{id}/{title}<br>
	 * <br>
	 * Variante de la suivante mais les paramètre sont insérés dans l'url et sont récupéres dans les variables/paramètres<br>
	 * de la fonction, i.e. annotés avec @PathVariable<br>
	 * <br>
	 * Cette méthode est appelée lorsque l'utilisateur soumet le formulaire d'édition d'un topic<br>
	 * <br>
	 * Le titre peut être modifié mais seul le créateur du topic ou un administrateur peuvent le faire<br>
	 * <br>
	 * Le titre du nouveau topic, ainsi que son idientifiant sont enregistrés dans les paramètres transmis<br>
	 * par la fonction javascipt AJAX <br>
	 * <br>
	 * On peut par la suite récupérer ces valeurs avec les paramètres de la fonction annotés @PathVariable<br>
	 * <br>
	 * L'appel à la fonction topicDAO.updateTopic() retourne un entier supérieur à 0 si l'opération est réussie<br>
	 * 	  <br>
	 * @param id (int): l'identifiant du topic<br>
	 * @param title: (String): le titre du topic<br>
	 * @param model: (Model): le modèle de l'application <br>
	 * <br>
	 * @return: - String "{status:ok}" une chaîne représetnant un objet de type json, si les topic et post ont été enregistrés avec succès<br>
	 * 			- String "{status:error}" sinon<br>
	 * <br>
	 **********************************************************************************************************************/

	@RequestMapping(value = "/updateTopic/{id}/{title}", method = RequestMethod.PUT, produces = "application/json;charset=UTF-8")
	public @ResponseBody String update(@PathVariable int id, @PathVariable String title, Model model) {
		
		logger.log(Level.INFO, "in update annotated with RequestMethod.PUT ");
		
		//récupérer le topic de la bd
		Topic topic =  topicDAO.getTopic(id);
		
		
		if(topic != null) {
			//mettre ç jour
			topic.setTitle(title);
			int result = topicDAO.updateTopic(topic);
		
			//succès
			if(result > 0) {
				return "{\"status\":\"ok\"}";
			}
		}
		//échec
		return "{\"status\":\"error\"}";
	}
	
	//*********************************************************************************************************************
	//*********************************************************************************************************************
	

	/**********************************************************************************************************************
	 * <br>
	 * Méthode qui permet  de gérer un requête '/updateTopic' avec la méthode PUT<br>
	 * <br>
	 * Variante de la précédente mais les paramètres sont transmis et enregistrés dans une collection de type Map(String, String)<br>
	 * avec les clés-valeurs <br>
	 * <br>
	 * Cette méthode n'ests pas utilisée masi elle permet d'illusterer qu'il est possible de passer les paramètre dans l'url mais<br>
	 * de facon classique, c'est-à-dire avec un point d'interrogtion devant la liste des paramètres qui sont définis individuellement par une<br>
	 * paire de clé-valeur, associées par un symbole d'égalité ('=') et séparées par un '&amp;' <br>
	 * <br>
	 * La requête pourrait être: "/topics/updateTopic?topicId=4&amp;topicTitle="Un%20nouveau%20titre"<br>
	 * 	 <br>
	 * Le titre du nouveau topic, ainsi que son idientifiant sont enregistrés dans les paramètres transmis<br>
	 * par la fonction javascipt AJAX <br>
	 * <br>
	 * On peut alors les récupérer au moyen d'un objet de type Map(String, String) annoté de @RequestParam pour signifie que les paramètres<br>
	 * de la requête sont inclus dans la collection<br>
	 * <br>
	 * On peut par la suite récupérer ces valeurs avec les clés c'est-à-dire les noms utilisés pour chacun des paramètres<br>
	 * lors de la transmission de la requête<br>
	 * <br>
	 *  L'appel à la fonction topicDAO.updateTopic() retourne un entier supérieur à 0 si l'opération est réussie<br>
	 * 	  <br>
	 * @param params: (Map(String, String)): une collection de type Map (interface) contenant les clés-valeurs de paramètres<br>
	 * @param model: (Model): le modèle de l'application <br>
	 * <br>
	 * @return: - String "{status:ok}" une chaîne représetnant un objet de type json, si les topic et post ont été enregistrés avec succès<br>
	 * 			- String "{status:error}" sinon<br>
	 * <br>
	 **********************************************************************************************************************/
	
	@RequestMapping(value = "/updateTopic", method = RequestMethod.PUT, produces = "application/json;charset=UTF-8")
	public @ResponseBody String updateTopic(@RequestParam Map<String, String> params, Model model) {
		
		logger.log(Level.INFO, "in updateTopic annotated with RequestMethod.PUT: param size:  " + params.size());
		
		//on récupère les param;tres
		String topicId =  params.get("topicId");
		String topicTitle =  params.get("topicTitle");

		//on met à jour le titre
		int result = topicDAO.updateTopic(Integer.parseInt(topicId), topicTitle);
		
		if(result > 0) {
			return "{\"status\":\"ok\"}";
		}

		
		return "{\"status\":\"error\"}";
	}
	
	//*********************************************************************************************************************
	//*********************************************************************************************************************
	
	/**********************************************************************************************************************
	 * <br>
	 * Méthode qui permet  de gérer un requête '/deleteTopic/{id}' avec la méthode DELETE<br>
	 * <br>
	 * Variante de la suivante mais le paramètre est transmis dans l'url et peut être récupéré dans le paramètre de la fonction<br>
	 * annoté avec @PathVariable<br>
	 * <br>
	 * Cette méthode est appelée lorsque l'utilisateur qui a créé le topic pu un administrateur clique sur le bouton 'Retirer'<br>
	 * <br>
	 * L'identifiant est enregistré dans le paramètre transmis par la fonction javascipt AJAX <br>
	 *  <br>
	 * L'appel à la fonction topicDAO.openCloseTopic() eest appelée avec en paramètre un byte de valeur 0 pour désactiver le topic (on ne l'efface pas)<br>
	 *  <br>
	 * @param id: (int): un entier qui représente l'identifiant du topic<br>
	 * @param model: (Model): représente le modèle de l'application <br>
	 * <br>
	 * @return: - String "{status:ok}" une chaîne représetnant un objet de type json, si les topic et post ont été enregistrés avec succès<br>
	 * 			- String "{status:error}" sinon<br>
	 * <br>
	 **********************************************************************************************************************/
	
	@RequestMapping(value = "/deleteTopic/{id}", method = RequestMethod.DELETE, produces = "application/json;charset=UTF-8")
	public @ResponseBody String delete(@PathVariable int id, Model model) {
		
		logger.log(Level.INFO, "in delete annotated with RequestMethod.DELETE: id:  " + id);
		
		//désactiver le topic
		int result = topicDAO.openCloseTopic(id, (byte)0);
		
		//succès
		if(result > 0) {
			return "{\"status\":\"ok\"}";
		}

		//échec
		return "{\"status\":\"error\"}";
	}
	
	//*********************************************************************************************************************
	//*********************************************************************************************************************
	
	/**********************************************************************************************************************
	 * <br>
	 * Méthode qui permet de gérer un requête '/deleteTopic' avec la méthode DELETE<br>
	 * <br>
	 * Variante de la précédente mais le paramètres est transmis et enregistré dans un objet de type String, et non une collection <br>
	 * de type Map(String, String avec les clés-valeurs, puisqu'on a seulement un paramètre <br>
	 * <br>
	 * Cette méthode est appelée lorsque l'utilisateur qui a créé le topic pu un administrateur clique sur le bouton 'Effacer'<br>
	 * <br>
	 * L'identifiant est enregistré dans le paramètre transmis par la fonction javascipt AJAX et est annoté de @RequestParam comme paramètre de la fonction<br>
	 *  <br>
	 * L'appel à la fonction topicDAO.openCloseTopic() eest appelée avec en paramètre un byte de valeur 0 pour désactiver le topic (on ne l'efface pas)
	 * 	 <br> 
	 * @param topicId: (String): une chaîne qui représente l'identifiant du topic qu'on convertit en entier<br>
	 * @param model: (Model): représente le modèle de l'application <br>
	 * <br>
	 * @return: - String "{status:ok}" une chaîne représetnant un objet de type json, si les topic et post ont été enregistrés avec succès<br>
	 * 			- String "{status:error}" sinon<br>
	 * <br>
	 **********************************************************************************************************************/
	//Params vide lorques transféré par json object
	@RequestMapping(value = "/deleteTopic", method = RequestMethod.DELETE, produces = "application/json;charset=UTF-8")
	public @ResponseBody String delete(@RequestParam String topicId, Model model) {
		
		logger.log(Level.INFO, "in delete annotated with RequestMethod.DELETE: topicId :  " + topicId);

		//mettre à jour le champ is_active dans la table post
		int result = topicDAO.openCloseTopic(Integer.parseInt(topicId), (byte)0);
		
		//succès
		if(result > 0) {
			return "{\"status\":\"ok\"}";
		}

		//échec
		return "{\"status\":\"error\"}";
	}
	
	//*********************************************************************************************************************
	//*********************************************************************************************************************
	
}//end of class


