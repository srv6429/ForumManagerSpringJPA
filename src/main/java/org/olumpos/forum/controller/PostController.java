package org.olumpos.forum.controller;



import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.olumpos.forum.dao.PostDAO;
import org.olumpos.forum.entity.JSONResponse;
import org.olumpos.forum.entity.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


/************************************************************************************************************************************************
 * <br>
 * @author daristote<br>
 * <br>
 * Classe qui définit des méthodes qui prennent en charge les requêtes concernant l'affichage, l'ajout et l'édition des posts (commentaires)<br>
 * <br>
 * Annotée avec @Controller elle indique au gestionnaire central que les requêtes peuvent être gérées par des méthodes de cette classe<br>
 * <br>
 * Le chemin (Path) de base est "/posts"<br>
 * <br>
 * Les autres chemins définis dans cette classe sont relatifs à celui-ci<br>
 * <br>
 * Le constructeur est annoté avec @Autowired indiquant qu'il doit être appelé lors du déploiement de l'application par le serveur<br>
 * <br>
 * Cet appel permet d'initialiser les variables globales TopicDAO topicDAO et PostDAO postDAO utilisées pour
 * accéder à la base de données <br>
 * <br>
 * Remarque: on aurait pu simplement placer l'annotation @Autowired devant la déclaration des variables (ex: @Autowired PostDAO postDAO)
 * ce qui aurait assuré l'injection, i.e. l'initialisation de la variable au déploiement<br>
 * <br>
 * Toutefois pour tester avec JUnit, on doit initialiser manuellement les membres, ce qui est fait en appelant explicitement le contructeur 
 * de PostController et qui permet, lors de son instanciation d'initialiser les variables globales permettant l'accès à la base  de données.<br>
 *<br>
 *
 *************************************************************************************************************************************************/

@Controller
//@RestController
//Path de base
@RequestMapping("/posts")
public class PostController {

	Logger logger =  Logger.getLogger(PostController.class.getName());

	
//	@Autowired
	PostDAO postDAO;
	
	//Constructeur appelé lors du déploiement
	@Autowired
	public PostController(PostDAO postDAO) {
		this.postDAO =  postDAO;
	}
	
		
	//*********************************************************************************************************************
	//*********************************************************************************************************************
	

	/**********************************************************************************************************************
	 * <br>
	 * Méthode qui permet de gérer un requête '/posts' avec la méthode GET<br>
	 * <br>
	 * Cette méthode récupère la liste de tous les posts ouverts associés au topic dont l'identifiant est spécifié en paramètre<br>
	 * <br>
	 * Le paramètre est annoté de @PathVariable pour spécifier qu'il doit être initialisé avec la valeur passée dans l'url: '/posts/{id}' <br>
	 *  <br>
	 * Appel à la fonction getAllActivePosts() de la classe postDAO<br>
	 * <br>
	 * retourne la liste des posts trouvés<br>
	 * <br>
	 * @param id :  -(int): l'identifiant du topic contenant les posts <br>
	 * @param model: représente le modèle de l'application<br>
	 * @return: - List(Post) postList: la liste contenant tous les posts ouverts associés au topic (id)<br>
	 * <br>
	 **********************************************************************************************************************/

	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public String getPosts (@PathVariable int id, Model model) {
		
		logger.log(Level.INFO, "in getPosts annotated with RequestMapping: topicId: " + id);

		//obtention de la liste des posts
		List<Post> postList =  postDAO.getAllActivePosts(id);		
		
		//si la list n'est pas vide ou nulle
		if(postList != null && postList.size() > 0) {
			//on cherche le topic pour récupérer le titre
			
			//Topic topic =  topicDAO.getTopic(id);
			String topicTitle = postList.get(0).getTopicFK().getTitle();
		
		//	logger.log(Level.INFO, "topic title: " + topicTitle);
			
			//ajout des attrributs au model
			model.addAttribute("postList", postList);
			model.addAttribute("topicId", id);
			model.addAttribute("topicTitle", topicTitle);
		}

		return "posts";

	}
	
	//*********************************************************************************************************************
	//*********************************************************************************************************************
	
	
	/**********************************************************************************************************************
	 * <br>
	 * Méthode qui permet de gérer un requête '/addPost/{userId}/{title}/{postTitle}/{comment}' avec la méthode POST<br>
	 * <br>
	 * Variante de la suivante mais les paramètres sont insérés dans l'url et récupéres dans les paramètres formels
	 * de la fonction, i.e. annotés avec @PathVariable<br>
	 * <br>
	 * Cette méthode est appelée lorsque l'utilisateur soumet le formulaire d'ajout d'un post<br>
	 * <br>
	 * Le titre du post et le commentaire sont enregistrés dans les paramètres transmis dans l'url
	 * par la fonction javascipt AJAX <br>
	 * <br>
	 * L'appel à la fonction postDAO.addPost(Post) retourne un entier (1) si l'opération est réussie, (0) sinon<br>
	 * 	  <br>
	 * @param title: le titre du post<br>
	 * @param comment: le commentaire du post<br>
	 * @param topicId: l'identifiant du topic<br>
	 * @param userId: l'identifiant de l'utilisateur<br>
	 * @param model: le modèle de l'application <br>
	 * <br>
	 * @return: - String "{status:ok}": une chaîne représentant un objet de type json, si le post a été enregistré avec succès<br>
	 * 			- String "{status:error}" sinon<br>
	 * 
	 **********************************************************************************************************************/
	
	@RequestMapping(value = "/addPost/{title}/{comment}/{topicId}/{userId}", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody String add(@PathVariable String title, @PathVariable String comment, @PathVariable int topicId, @PathVariable int userId, Model model) {

		logger.log(Level.INFO, "in addPost annotated with RequestMethod.POST");
		
//		logger.log(Level.INFO, "in addPost annotated with RequestMethod.POST: params title: " + title);
//		logger.log(Level.INFO, "in addPost annotated with RequestMethod.POST: param postBody:  " + comment);
//		logger.log(Level.INFO, "in addPost annotated with RequestMethod.POST: param userId:  " + userId);
//		logger.log(Level.INFO, "in addPost annotated with RequestMethod.POST: param topicId:  " + topicId);
		
		//cération du post
		Post post = new Post();
		
		//initialisation des champs
		post.setTitle(title);
		post.setBody(comment);
		post.setUserId(userId);
		post.setTopicId(topicId);
		
		//ajout dand la bd
		int result = postDAO.addPost(post);
			
		//succès
		if(result > 0 ) {
			return "{\"status\":\"ok\"}";
		}
		
		//échec
		return "{\"status\":\"error\"}";


	}
	//*********************************************************************************************************************
	//*********************************************************************************************************************
	
	
	/**********************************************************************************************************************
	 * <br>
	 * Méthode qui permet de gérer un requête '/addPost/{userId}/{title}/{postTitle}/{comment}' avec la méthode POST<br>
	 * <br>
	 * Variante de la suivante mais les paramètres sont insérés dans l'url et récupéres dans les paramètres formels
	 * de la fonction, i.e. annotés avec @PathVariable<br>
	 * <br>
	 * Cette méthode est appelée lorsque l'utilisateur soumet le formulaire d'ajout d'un post<br>
	 * <br>
	 * Le titre du post et le commentaire sont enregistrés dans les paramètres transmis dans l'url
	 * par la fonction javascipt AJAX <br>
	 * <br>
	 * L'appel à la fonction postDAO.addPost(Post) retourne un entier (1) si l'opération est réussie, (0) sinon<br>
	 * 	  <br>
	 * @param title: le titre du post<br>
	 * @param comment: le commentaire du post<br>
	 * @param topicId: l'identifiant du topic<br>
	 * @param userId: l'identifiant de l'utilisateur<br>
	 * @param model: le modèle de l'application <br>
	 * <br>
	 * @return: - String "{status:ok}": une chaîne représentant un objet de type json, si le post a été enregistré avec succès<br>
	 * 			- String "{status:error}" sinon<br>
	 * 
	 **********************************************************************************************************************/
//	
//	@RequestMapping(value = "/addPost/{title}/{comment}/{topicId}/{userId}", method = RequestMethod.POST)//, produces = "application/json;charset=UTF-8")
//	public ResponseEntity<JSONResponse> add2(@PathVariable String title, @PathVariable String comment, @PathVariable int topicId, @PathVariable int userId, Model model) {
//
//		logger.log(Level.INFO, "in addPost annotated with RequestMethod.POST");
//		
////		logger.log(Level.INFO, "in addPost annotated with RequestMethod.POST: params title: " + title);
////		logger.log(Level.INFO, "in addPost annotated with RequestMethod.POST: param postBody:  " + comment);
////		logger.log(Level.INFO, "in addPost annotated with RequestMethod.POST: param userId:  " + userId);
////		logger.log(Level.INFO, "in addPost annotated with RequestMethod.POST: param topicId:  " + topicId);
//		
//		JSONResponse response =  new JSONResponse();
//		
//		//création du post
//		Post post = new Post();
//		
//		//initialisation des champs
//		post.setTitle(title);
//		post.setBody(comment);
//		post.setUserId(userId);
//		post.setTopicId(topicId);
//		
//		//ajout dand la bd
//		int result = postDAO.addPost(post);
//			
//		logger.log(Level.INFO, "in postController.add2 result: " + result);
//		//succès
//		if(result > 0 ) {
//		//	return "{\"status\":\"ok\"}";
//			response.setStatus("ok");
//		} else {
//			//échec
////			return "{\"status\":\"error\"}";
//			response.setStatus("error");
//			
//		}
//		
//		return ResponseEntity.ok(response);
//		
//
//
//	}
	//*********************************************************************************************************************
	//*********************************************************************************************************************
	
	/**********************************************************************************************************************
	 * <br>
	 * Méthode qui permetde gérer un requête '/addPost/' avec la méthode POST<br>
	 * <br>
	 * Variante de la précédent mais les paramètres sont trasmis dans un objet json par la fonction javascript AJAX 
	 * et sont enregistrés dans une collection Map(String, String) annotée avec @PathVariable en tant que paramètre formel de la fonction<br>
	 * <br>
	 * On peut récupérer les paramètres transmis avec les clés-valeurs de la collection<br>
	 * <br>
	 * Cette méthode n'est pas utilisée mais a été testée avec succès et peut être une alternative à la précédente<br>
	 * 	 <br>
	 * L'appel à la fonction postDAO.addPost(Post) retourne un entier (1) si l'opération est réussie, (0) sinon<br>
	 * 	  <br>
	 * @param params: (Map(String, String)) : 	une collection de type Map contenant les paramètre (titre du post, commentaire, 
	 * 											l'identifiant du topic, l'identifiant de l'utilisateur)<br>
	 * @param model: : le modèle de l'application <br>
	 * <br>
	 * @return: - String "{post:success}" une chaîne représetnant un objet de type json, si le post a été enregistré avec succès<br>
	 * 			- String "{post:failure}" sinon<br>
	 * 
	 **********************************************************************************************************************/
	
	@RequestMapping(value = "/addPost", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody String add(@RequestParam Map<String, String> params, Model model) {
		
		logger.log(Level.INFO, "in PostController.addPost annotated with RequestMethod.POST: param size:  " + params.size());

		String postTitle =  params.get("postTitle");
		String postBody =  params.get("postBody");
		String userId =  params.get("userId");
		String topicId =  params.get("topicId");

//		logger.log(Level.INFO, "in addPost annotated with RequestMethod.PUT: param postTitle:  " + postTitle);
//		logger.log(Level.INFO, "in addPost annotated with RequestMethod.PUT: param postBody:  " + postBody);
//		logger.log(Level.INFO, "in addPost annotated with RequestMethod.PUT: param userId:  " + userId);
//		logger.log(Level.INFO, "in addPost annotated with RequestMethod.PUT: param topicId:  " + topicId);
		
		//cération du post
		Post post = new Post();
		//initialisation des champs
		post.setTitle(postTitle);
		post.setBody(postBody);
		post.setUserId(Integer.parseInt(userId));
		post.setTopicId(Integer.parseInt(topicId));
		
		//ajout dand la bd
		int result = postDAO.addPost(post);
			
		//succès
		if(result > 0 ) {
			return "{\"status\":\"ok\"}";
		}
		
		//échec
		return "{\"status\":\"error\"}";


	}
	
	//*********************************************************************************************************************
	//*********************************************************************************************************************

	/**********************************************************************************************************************
	 * <br>
	 * Méthode qui permet de gérer une requête '/updatePost/{id}/{title}/{comment} avec la méthode PUT<br>
	 * <br>
	 * Variante de la suivante mais les paramètre sont insérés dans l'url et sont récupéres dans les paramètres formels
	 * de la fonction annotés avec @PathVariable<br>
	 * <br>
	 * Cette méthode est appelée lorsque l'utilisateur soumet le formulaire d'édition d'un post<br>
	 * <br>
	 * L'identifiant, le titre et le commentaire sont ainsi enregistrés dans les paramètres transmis
	 * par la fonction javascipt AJAX <br>
	 * 	 <br>
	 * L'appel à la fonction postDAO.updatePost() retourne un entier (1) si l'opération est réussie, (0) sinon<br>
	 * 	  <br>
	 * @param id: (int): l'identifiant du post<br>
	 * @param title: (String): le titre du post<br>
	 * @param comment: (String): le commentaire du post<br>
	 * @param model: (Model): le modèle de l'application <br>
	 * <br>
	 * @return: String "{status:ok}": une chaîne représetnant un objet de type json, si le post est modifié avec succès<br>
	 * 			String "{status:error}" sinon<br>
	 * 
	 **********************************************************************************************************************/
	
	@RequestMapping(value = "/updatePost/{id}/{title}/{comment}", method = RequestMethod.PUT, produces = "application/json;charset=UTF-8")
	public @ResponseBody String update(@PathVariable int id, @PathVariable String title, @PathVariable String comment, Model model) {
		
		logger.log(Level.INFO, "in PostController.updatePost annotated with RequestMethod.PUT");
		
//		logger.log(Level.INFO, "in PostController.updatePost annotated with RequestMethod.PUT: param id:  " + id);
//		logger.log(Level.INFO, "in PostController.updatePost annotated with RequestMethod.PUT: param title:  " + title);
//		logger.log(Level.INFO, "in PostController.updatePost annotated with RequestMethod.PUT: param comment:  " + comment);
		
		//récupérer le post avec don id
		Post post =  postDAO.getPost(id);
		
		if(post != null) {
			//mettre à jour les champs
			post.setTitle(title);
			post.setBody(comment);
			
			//mettre à jour dans la bd
			int result = postDAO.updatePost(post);
			
			//succès
			if(result > 0 ) {
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
	 * Méthode qui permet de gérer un requête '/updatePost' avec la méthode PUT<br>
	 * <br>
	 * Variante de la précédente mais les paramètres sont trasmis dans un objet json par la fonction javascript AJAX 
	 * et sont enregistrés dans une collection Map(String, String) annotée avec PathVariable en tant que paramètre formel de la fonction<br>
	 * <br>
	 * On peut récupérer les paramètres transmis avec les clés-valeurs de la collection<br>
	 * <br>
	 * Cette méthode n'est pas utilisée mais a été testée avec succès et peut être une alternative à la précédente<br>
	 * <br>
	 * Le titre du post et le commentaire sont enregistrés dans les paramètres transmis dans l'url
	 * par la fonction javascipt AJAX: url: "/posts/updatePost?postId="+postId+"&amp;postTitle="+postTitle+"&amp;postBody="+postBody;<br>
	 * <br>
	 * 
	 * L'appel à la fonction postDAO.addPost() retourne un entier (1) si l'opération est réussie, (0) sinon<br>
	 * 	  <br>
	 * @param params: (Map(String, String)) : une collection de type Map contenant les paramètre (titre du post, commentaire, 
	 * 											l'identifiant du topic, l'identifiant de l'utilisateur)<br>
	 * @param model: (Model): le modèle de l'application <br>
	 * <br>
	 * @return: String: "{status:ok}": une chaîne représetnant un objet de type json, si le post est modifié avec succès<br>
	 * 			String: "{status:error}" sinon<br>
	 * 
	 **********************************************************************************************************************/

	@RequestMapping(value = "/updatePost", method = RequestMethod.PUT, produces = "application/json;charset=UTF-8")
	public @ResponseBody String update(@RequestParam Map<String, String> params, Model model) {
		
		logger.log(Level.INFO, "in PostController.updatePost annotated with RequestMethod.PUT: param size:  " + params.size());
		
		String postId =  params.get("postId");
		String postTitle =  params.get("postTitle");
		String postBody =  params.get("postBody");

//		logger.log(Level.INFO, "in updatePost(2) annotated with RequestMethod.PUT: param postId:  " + postId);
//		logger.log(Level.INFO, "in updatePost(2) annotated with RequestMethod.PUT: param postTitle:  " + postTitle);
//		logger.log(Level.INFO, "in updatePost(2) annotated with RequestMethod.PUT: param postBody:  " + postBody);


		//on récupère le post avec sin id
		Post post =  postDAO.getPost(Integer.parseInt(postId));
				
		if(post != null) {
			//mettre à jour les champs
			post.setTitle(postTitle);
			post.setBody(postBody);
			
			//mettre à jour dans ; bd
			int result = postDAO.updatePost(post);
			
			//succès
			if(result > 0 ) {
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
	 * Méthode qui permet de gérer une requête '/deletePost/{id}' avec la méthode DELETE<br>
	 * <br>
	 * Variante de la suivante mais le paramètre est transmis dans l'url et peut être récupéré dans le paramètre de la fonction
	 * annoté avec @PathVariable<br>
	 * <br>
	 * Cette méthode est appelée lorsque l'utilisateur qui a créé le post ou un administrateur cliquent sur le bouton 'Retirer'
	 * <br>
	 * L'identifiant est enregistré dans le paramètre transmis par la fonction javascipt AJAX: "/deletePost/"+postId<br> 
	 *  <br>
	 * L'appel à la fonction postDAO.deletePost(Post) est appelée avec en paramètre le post récupéré de la bd par son identifiant<br>
	 * <br>
	 * On désactive le post en modifiant la valeur de la variable isActive qui est mise à 0 (on ne l'efface pas)<br>
	 *  <br>
	 * @param id (int): un entier qui représente l'identifiant du topic<br>
	 * @param model (Model): le modèle de l'application <br>
	 * <br>
	 * @return: String "{status:ok}" une chaîne représentant un objet de type json, si le post est modifié avec succès<br>
	 * 			String "{status:error}" sinon<br>
	 * <br>
	 * 
	 **********************************************************************************************************************/
	//Params vide lorques transféré par json object 
	//Ok lorsque les paramètres sont ajoutés à l'url
	@RequestMapping(value = "/deletePost/{id}", method = RequestMethod.DELETE, produces = "application/json;charset=UTF-8")
	public @ResponseBody String delete(@PathVariable int id, Model model) {
		
		logger.log(Level.INFO, "in PostController.updatePost annotated with RequestMethod.DELETE: param id:  " + id);
	
		//on récupère le post avec l'identifiant
		Post post =  postDAO.getPost(id);
		
		//s'il existe dans la bd
		if(post != null) {
			
			//on désactive le post
			post.setIsActive((byte) 0);
			
			//on met à jour dans la bd
			int result = postDAO.updatePost(post);
			
			//succès
			if(result > 0 ) {
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
	 * Méthode qui permet de gérer un requête '/deletePost/{id}' avec la méthode DELETE<br>
	 * <br>
	 * Variante de la précédente mais le paramètre est transmis dans l'url et peut être récupéré dans le paramètre de la fonction
	 * annoté avec @PathVariable<br>
	 * <br>
	 * Cette méthode est appelée lorsque l'utilisateur qui a créé le post ou un administrateur cliquent sur le bouton 'Retirer'<br>
	 * <br>
	 * Cette méthode n'est pas utilisée mais a été testée avec succès et peut être une alternative à la précédente<br>
	 * L'identifiant est enregistré dans le paramètre transmis par la fonction javascipt AJAX: "/deletePost?postId="+postId<br> 
	 *  <br>
	 * L'appel à la fonction postDAO.deletePost(Post) est appelée avec en paramètre le post récupéré de la bd par son idientifiant<br>
	 * <br>
	 * On désactive le post en modifiant la valeur de la variable isActive qui est mise à 0 (on ne l'efface pas)<br>
	 *  <br>
	 * 	  
	 * @param postId: (String): une chaîne qui représente l'identifiant du topic<br>
	 * @param model: (Model):  le modèle de l'application <br>
	 * <br>
	 * @return: String "{status:ok}" une chaîne représentant un objet de type json, si le post est modifié avec succès<br>
	 * 			String "{status:error}" sinon<br>
	 * <br>
	 **********************************************************************************************************************/
	@RequestMapping(value = "/deletePost", method = RequestMethod.DELETE, produces = "application/json;charset=UTF-8")
	public @ResponseBody String delete(@RequestParam String postId, Model model) {
		
	
		logger.log(Level.INFO, "in deletePost(2) annotated with RequestMethod.DELETE --------->  param postId:  " + postId);
		
		//on récupère le post avec l'identifiant
		Post post =  postDAO.getPost(Integer.parseInt(postId));
		
		//s'il existe dans la bd
		if(post != null) {
			
			//on désactive le post
			post.setIsActive((byte) 0);
			
			//on met à jour dans la bd
			int result = postDAO.updatePost(post);
			
			//succès
			if(result > 0 ) {
				return "{\"status\":\"ok\"}";
			}
		}
				
		//échec
		return "{\"status\":\"error\"}";

	}
	

	
}
