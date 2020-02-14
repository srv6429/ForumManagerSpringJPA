package org.olumpos.forum.controller;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.olumpos.forum.dao.UserDAO;
import org.olumpos.forum.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;


/**
 * 
 * @author daristote
 * 
 * Classe qui définit des méthodes qui prennent en charge les requêtes quio concernent l'inscription, la connexion et la gestion des utilisateurs 
 * 
 * Annotée avec @Controller elle indique au gestionnaire central (Servlet) que les requêtes peuvent être gérées par des méthodes de cette classe
 * 
 * Le chemin (Path) de base est "/" soit la racine du projet à partir du contexte de l'application
 * 
 * Les autres chemins définis dans cette classe sont relatifs à celui-ci
 * 
 * Une variable de session 'user' est définie et est active tant que la session de l'utilsateur demeure valide
 * 
 * Le constructeur est annoté avec @Autowired indiquant qu'il doit être appelé lors du déploiement del'application par le serveur
 * Cet appel permet d'initialiser la variable globale 'UserDAO userDAO' utilisée pour accéder à la base de données 
 * 
 * Remarque: on aurait pu simplement placé l'annotattion @Autowired devant la déclaration de la variable 'userDAO' ce qui aurait assuré 
 * l'injection, i.e. l'initialisation de la variable
 * 
 * Mais pour tester avec JUnit, on doit initialiser manuellement la variable, ce qui est fait en appelant le contructeur de UserController 
 * qui permet, lors de son instanciation d'initialiser la variable.
 * 
 *
 */

//
@Controller
@SessionAttributes("user") //variable de session
public class UserController {

	
	//Logger qui permet d'afficher des messages en console
	Logger logger =  Logger.getLogger(UserController.class.getName());
	
	
//	@Autowired
	UserDAO userDAO;
	
	//Constructeur appelé lors du déploiement de l'application
	@Autowired
	public UserController(UserDAO userDAO) {
		this.userDAO = userDAO;
	}
	
	//*********************************************************************************************************************
	//*********************************************************************************************************************
	
	/**********************************************************************************************************************
	 * Fonction login(Model)
	 * 
	 * Permet de gérer la requête '/login' avec la méthode GET
	 * 
	 * En général L'utilisateur est redirigé vers cette page au départ avant d'être connecté
	 * 
	 * La méthode se contente d'instancier la variable de session 'user' sans initialiser les champs
	 * 	  
	 * @param model: (Model): représente le modèle de l'application permettant d"ajouter des variables de session	
	 * 							utilisable durant toute la session
	 * 
	 * @return: String "login": une chaîne de caractères indiquant la vue, i.e. login.jsp, vers laquelle on doit être dirigé
	 * 
	 **********************************************************************************************************************/
	
	@RequestMapping(value="/login", method = RequestMethod.GET)
	public String login(Model model) {
		
		//instanciation de la variable User
		User user = new User();
		//ajout de la variable de session
		model.addAttribute("user", user);
		
		return "login";
	}
	
	//*********************************************************************************************************************
	//*********************************************************************************************************************
	
	/**********************************************************************************************************************
	 * Fonction doLogin(User, Model)
	 * 
	 * Permet de gérer un requête '/login' avec la méthode POST
	 * 
	 * Cette méthode est appelée lorsque l'utilisateur soumet le formulaire de connexion de la page login.jsp
	 * 
	 * Les champs  'username' et 'password' du formulaire ont été remplis et les valeurs ont été automatiquement placées 
	 * dans les variables correspondantes de l'objet User qui a été préalablement placé dans une variable de session
	 * 
	 * L'annotation @ModelAttribute("user") devant le paramètre 'user' indique que cette variable est associée
	 * à l'attribut de session du même nom  
	 * 
	 * On peut ainsi récupérer les champs 'username' et 'password' initialisés après la soumission du formulaire
	 *  et vérifier si les champs correspondent à un utilisateur enregistré dans la base de données.
	 *  
	 *  La fonction userDAO.getUser() est appelée et si les valeurs sont valides elle retourne une instance de User avec toutes
	 *  les informations liés à l'utilisateur
	 *  
	 * On remplace alors la variable de session "user" par le nouvel objet User retourné avec le profile complet de l'utilisateur.
	 * 
	 * 	  
	 * @param user: (User): repésente l'attribut de session 'user' avec les champ username et password initialisés
	 * @param model: (Model): le modèle de l'application 
	 * 
	 * @return: - String: "redirect:topics": une chaîne de caractères indiquant la redirection vers la vue  "topics.jsp", 
	 * 					si l'utilisateur est un utilisateur valide dans la base de données
	 * 			- String login: la vue en cas d'échec de la validation de l'utilisateur
	 * 
	 **********************************************************************************************************************/
	
	
	@RequestMapping(value="/login", method = RequestMethod.POST)
	public String doLogin(@ModelAttribute ("user") User user, Model model) {
		
		logger.log(Level.INFO, "in doLogin annotated with RequestMapping method POST : user ==>  " + user);
		
		//vérification si un utilisateur avec le 'username' et 'password' existe dans la base d données
		User dbUser =  userDAO.getActiveUser(user.getUsername(), user.getPassword());
		
		logger.log(Level.INFO, "in doLogin annotated with RequestMapping method POST : dbUser ==>  " + dbUser);
		
		//si l'objet retourné n'est pas null, on remplace l'atribut 'user' par l'objet de type User retourné
		if(dbUser != null) {
			model.addAttribute("user", dbUser);
			
			//on redirige vers la vue "topics.jsp"
			return "redirect:topics";
		}
		//remettre le username à null
		user.setUsername(null);
		user.setPassword(null);
		//enregistrer un message d'erreur
		user.setMessage("Nom d'utilisateur et/ou mot de passe non valide(s)! Veuillez essayer de nouveau!");
		
		return "login";
	}
	
	//*********************************************************************************************************************
	//*********************************************************************************************************************
	
	
	/**********************************************************************************************************************
	 * Fonction verify(String, Model)
	 * 
	 * fonction appelée par l'utilisateur à partir de la page d'inscription register.jsp lorsque l'utilisateur clique sur le bouton
	 * pour vérifier si le nom d'utilisateur entré dans le champ approprié du formulaire est disponible
	 * 
	 * Puisque le champ 'username' doit être unique, deux utilisateurs ne peuvant pas utiliser le même
	 * 
	 * Donc on vérifie  ou s'il est déjà enregistré dans la bd
	 * 
	 * La valeur entrée dans le champ du formulaire est placée dans la variable {login} de l'url

	 * L'annotation @PathVariable indique que le paramètre doit être associé avec cette valeur
	 * 
	 * Puisque la requête est envoyée par un script javascript de manière asynchrone (AJAX),  la fonction retourne 
	 * une chaîne qui représente on objet javascript(JSON)
	 *  
	 * Pour indiquer qu'il s'agit non pas d'un vue mais d'un objet json, onajoute l'attribut produces dans l'annotation @RequestMapping
	 * en indiquant le type de données retourné
	 * De plus, on place l'annotation @ResponseBody devant le type de retour (String) de la fonction
	 * 
	 * La valeur retournée dépend de la valeur de retour que la fonction  userDAO.getUserbyUsername(login) renvoie
	 * 	- si l'utilisateur est trouvé dans la table 'user' de la bd, alors le nom d'utilisateur n'est pas disponible et est donc réservé
	 * 	- si l'utilisateur n'est pas trouvé dans la table 'user', alors il est disponible et peut être utilisé 
	 * 	  
	 * @param login: (String): e 'username' passé dans l'url
	 * @param model: (Model): le modèle de l'application 
	 * 
	 * @return: - String "{login:taken}": une chaîne de caractères indiquant que le nom d'utilisateur est réservé, 
	 * 			- String "{login:free}: une chaîne de caractères indiquant que le nom d'utilisateur est disponible, 
	 * 
	 **********************************************************************************************************************/
	
	@RequestMapping(value="/verifyLogin/{login}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody String verify(@PathVariable String login, Model model) {
	
		logger.log(Level.INFO, "verify login: " + login);
		
		//vérification du nom d'utilisateur
		User user = userDAO.getUserByUsername(login);
		
		logger.log(Level.INFO, "verify login:  user: " + user);
		
		//si l'utilisateur retourné n'est pas null alors le nom d'utilisateur n'est pas disponible
		if(user != null) {
			return "{\"login\":\"taken\"}";
		}

		//le nom d'utilisateur est disponible
		return "{\"login\":\"free\"}";
		
	}
	
	
	//*********************************************************************************************************************
	//*********************************************************************************************************************
	
	
	/**********************************************************************************************************************
	 * Fonction logout(Model)
	 * 
	 * Fonction appelée lorsque l'utilisateur clique sur l'option 'Déconnexion' à partir du menu 
	 * Permet de fermer la session de l'utilisateur en remplaçant la valeur de session 'user'
	 *  par un nouvel objet instancié avec des champs vides	 
	 *  
	 * @param model: (Model): le modèle de l'application 
	 * @return: - String "redirect:login": une chaîne de caractères indiquant la redirection vers la vue  "login.jsp", 
	 * 
	 **********************************************************************************************************************/
	@RequestMapping(value="/logout", method = RequestMethod.GET)
	public String logout(Model model) {
		
		logger.log(Level.INFO, "in logout annotated with RequestMapping method GET");		
		model.addAttribute("user", new User());
		
		//on renvoie vers la page login
		return "redirect:login";
	}
	
	//*********************************************************************************************************************
	//*********************************************************************************************************************
	
	/**********************************************************************************************************************
	 * Fonction register(Model)
	 * 
	 * Permet de gérer la requête '/register' avec la méthode GET
	 * 
	 * En général l'urtilisateur appelle cette requête lorsqu'il n'est âs connecté et qu'il clique sur le bouton 'Inscription'
	 * du menu en tête de la page
	 * 
	 * La méthode se contente d'instancier la variable de session 'user' sans initialiser les champs
	 * 	  
	 * @param model: (Model): représente le modèle de l'application permettant d"ajouter des variables de session	
	 * 							utilisable durant toute la session
	 * 
	 * @return: String "register": une chaîne de caractères indiquant la vue, i.e. register.jsp, vers laquelle on doit être dirigée
	 * 
	 **********************************************************************************************************************/
	
	@RequestMapping(value="/register", method = RequestMethod.GET)
	public String register(Model model) {
				
		User user = new User();
		model.addAttribute("user", user);
		
		logger.log(Level.INFO, "in register annotated with RequestMapping method GET");
		
		return "register";
	}
	
	//*********************************************************************************************************************
	//*********************************************************************************************************************

	/**********************************************************************************************************************
	 * Fonction doRegister(User, Model)
	 * 
	 * Permet de gérer un requête '/register' avec la méthode POST
	 * 
	 * Cette méthode est appelée lorsque l'utilisateur soumet le formulaire d'inscription de la page register.jsp
	 * 
	 * Les champs  'username', 'email' et 'password' du formulaire ont été remplis et les valeurs ont été automatiquement placées 
	 * dans les variables correspondantes de l'objet User qui a été préalablement placé dans une variable de session
	 * 
	 * L'annotation @ModelAttribute("user") devant le paramètre 'user' indique que cette variable est associée
	 * à l'attribut de session du même nom  
	 * 
	 * On peut ainsi récupérer les champs 'username', 'email' et 'password' initialisés après la soumission du formulaire
	 *  et enregistrer le nouvel utilisateur
	 *  
	 *  La fonction userDAO.createUser() est appelée et si les valeurs sont valides elle retourne une instance de User avec toutes
	 *  les informations liés à l'utilisateur
	 *  
	 * On remplace alors la variable de session "user" par le nouvel objet User retourné avec le profile complet de l'utilisateur.
	 * 
	 * 	  
	 * @param user: User: repésente l'attribut de session 'user' avec les champs 'username', email' et 'password' initialisés
	 * @param model: (Model): le modèle de l'application 
	 * 
	 * @return: - String: "redirect:topics": une chaîne de caractères indiquant la redirection vers la vue  "topics.jsp", 
	 * 					si l'utilisateur a été enregistré avec succès dans la base de données
	 * 			- String "register": la vue en cas d'échec de la validation de l'utilisateur, i.e. on reste sur la page "register.jsp"
	 * 
	 **********************************************************************************************************************/
	
	
	@RequestMapping(value="/register", method = RequestMethod.POST)
	public String doRegister(@ModelAttribute ("user") User user, Model model) {
		
		logger.log(Level.INFO, "in doRegister annotated with RequestMapping method POST");
		
		int result = 0;
		//on vérifie si 'user' n'est aps null
		if(user != null) {
			//cr.ation de l'utilisateur 
			result =  userDAO.createUser(user);
		}
		//si l'opération est réussie
		if(result > 0) {
			//on récupère lenouvel utilisateur dans un objet instancié avec les champs initialisés
			User dbUser =  userDAO.getUser(user.getUsername(), user.getPassword());
			//si l'utilisateur retourné n'est pas nul
			if(dbUser != null) {
				
				//on remplace l'attribut de session par le nouvel objet
				model.addAttribute("user", dbUser);
				//on redirige vers la page des topics
				return "redirect:topics";
			}
		}
		user.setUsername(null);
		user.setEmail(null);
		user.setPassword(null);
		//échec: on demeure sur la page register
		return "register";
	}
	
	//*********************************************************************************************************************
	//*********************************************************************************************************************
	
	/**********************************************************************************************************************
	 * Fonction profile(Model)
	 * 
	 * Permet de gérer la requête '/profile' avec la méthode GET
	 * 
	 * L'utilisateur connecté atteint cette page lorsqu'il clique sur le bouton 'Profil' du menu
	 * 
	 * La méthode ne fait rien d'utre que de reoutner la vue
	 * Les champs 'username' et 'email' du formulaire sont rempli par les valeurs appropriées de l'utilisateur
	 * 	  
	 * @param model: (Model): représente le modèle de l'application 
	 * 
	 * @return: String "profile": une chaîne de caractères indiquant la vue, i.e. profile.jsp, vers laquelle on doit être dirigée
	 * 
	 **********************************************************************************************************************/
	@RequestMapping(value="/profile", method = RequestMethod.GET)
	public String profile(Model model) {
	
		//on est dirigé vers la page profile.jsp
		return "profile";
	}
	
	//*********************************************************************************************************************
	//*********************************************************************************************************************
	

	/**********************************************************************************************************************
	 * Fonction update(User, Model)
	 * 
	 * Permet de gérer un requête '/profile' avec la méthode PUT
	 * 
	 * Cette méthode est appelée lorsque l'utilisateur soumet le formulaire de mise à jour du profile de la page 'profile.jsp"
	 * 
	 * Les champs  'username', 'email' et 'password' du formulaire ont été remplis et les valeurs sont trnasmises dans l'url
	 *  
	 *  La fonction userDAO.updateUser() est appelée et si les valeurs sont mises à jour avec succès elle retourne '1'
	 *  
	 *  On récupère le User avec la méthode userDAO.getyUserById(int) et on remplace alors la variable de session "user" 
	 *  par le nouvel objet User retourné 
	 * 
	 * Les valeurs des paramètre sont initialisées par les valeurs passées dans l'url
	 * 
	 * @param id: (int): l'identifiant de l'utilisateur
	 * @param username: (String): le nouveau nom de l'utilisateur
	 * @param email: (String): le nouveu courriel de l'utilisateur
	 * @param password: (String): le nouveu mot de passe de l'utilisateur
	 * @param model: (Model): représente le modèle de l'application 
	 * 
	 * @return: - String: "{profile:update}": un objet json indiquant le succès de la mise à jour
	 * 			- String: "{profile:failure}": un objet json indiquant l'échec de la mise à jour
	 * 
	 **********************************************************************************************************************/
	@RequestMapping(value="/profile/{id}/{username}/{email}/{password}", method = RequestMethod.PUT, produces = "application/json;charset=UTF-8")
	public @ResponseBody String update(@PathVariable int id, @PathVariable String username, @PathVariable String email, @PathVariable String password, Model model) {
		
		logger.log(Level.INFO, "in doUpdate annotated with RequestMapping method PUT");
//		logger.log(Level.INFO, "in doUpdate annotated with RequestMapping method PUT : email ==>  " + username);
//		logger.log(Level.INFO, "in doUpdate annotated with RequestMapping method PUT : email ==>  " + email);
//		logger.log(Level.INFO, "in doUpdate annotated with RequestMapping method PUT : email ==>  " + password);
	
		//récupération de l'utilisateur avec son identifiant
		User user =  userDAO.getUserById(id);
		//remplcement des champs par es nouvelles valeurs
		user.setUsername(username);
		user.setEmail(email);
		user.setPassword(password);
		
		//mise à jour dans la bd
		int result =  userDAO.updateUser(user);
		
		//si succès
		if(result > 0 ) {
			//récupère l'utilisateur avec les colonnes modifiées
			User dbUser = 	userDAO.getUserById(id);
			
			//on remplace la valeur de session par le nouveau 'user'
			model.addAttribute("user", dbUser);
		
			//retourne en cas de succès
			return "{\"status\":\"ok\"}";
		}

		//échec de la mise à jour
		return "{\"status\":\"error\"}";
	}
	
	//*********************************************************************************************************************
	//*********************************************************************************************************************	

	/**********************************************************************************************************************
	 * Fonction getUsers(User, Model)
	 * 
	 * Permet de gérer un requête '/users' avec la méthode GET
	 * 
	 * Cette méthode permet de récupérer la liste de tous les utilisateurs de bd pour un administrateur
	 * 
	 * On vérifie sir l'utilisateur a bien le role "A"
	 * 
	 * S'il sagit d'un utilisateur régulier il est redirigé vers la page des topics
	 * 
	 * Les valeurs des paramètre sont initialisées par les valeurs passées dans l'url
	 * 
	 * @param user: (User): repésente l'attribut de session 'user' 
	 * @param model: (Model): représente le modèle de l'application 
	 * 
	 * @return: - String: "users": la vue users.jsp affichant la liste des utilisateurs
	 * 			- String: "redirect:topics": la page topics.jsp si l'utilisateur n'est pas un administrateur ou s'lil n'est pas connecté
	 * 
	 **********************************************************************************************************************/
	
	@RequestMapping(value="/users", method = RequestMethod.GET)
	public String getUsers(@ModelAttribute("user") User user, Model model){
		
		logger.log(Level.INFO, "in getUsers() annotated with RequestMapping method.GET");
		
		if(user != null && user.getRole() != null && "A".equals(user.getRole())) {
		
			//obtenir la liste des utilisateurs
			List<User> users = userDAO.getAllUsers();
			model.addAttribute("userList", users);
			return "users";			
		}
		
		return "redirect:topics";

	}
	
	//*********************************************************************************************************************
	//*********************************************************************************************************************
	
	/**********************************************************************************************************************
	 * Fonction deleteUser(User, Model)
	 * 
	 * Permet de gérer un requête '/deleteUser' avec la méthode DELETE
	 * 
	 * Cette méthode permet uniquement à un administrateur de désactiver un utilisateur de la bd
	 * On n'efface par l'utilisateur de la bd; on ne fait que mettre le champ 'is_active' à 0
	 * 
	 * Cette commande est envoyée à partir d'une requête javascipt AJAX
	 * La méthode retourne donc un objet de type JSON
	 *  
	 * S'il sagit d'un utilisateur régulier la méthode retourne un objet json idiquant un échec
	 * 
	 * Mais en principe un utilisateur réglier ne devrait pas pouvoir effectuer cette requête ...
	 * bien qu'un petit malin pourrait facilement le faire, donc on fait une vérification de routine
	 * pour vérifier qu'il s'agit bien d'un administrateur connecté avant de communiquer avec la bd 
	 * 
	 * La valeur du paramètre id est par la valeur transmise dans l'url
	 * 
	 * @param user: (User): repésente l'attribut de session 'user' 
	 * @param id: (int): l'identifiant de l'utilisateur
	 * @param model: (Model): représente le modèle de l'application 
	 * 
	 * @return: - String: "users": la vue users.jsp affichant la liste des utilisateurs
	 * 			- String: "{profile:deleted}": une chaîne représentant un objet json indiquant le succès la page topics.jsp si l'utilisateur n'est pas un administrateur ou s'lil n'est pas connecté
	 * 
	 **********************************************************************************************************************/
	
	@RequestMapping(value="/deleteUser/{id}", method = RequestMethod.DELETE, produces = "application/json;charset=UTF-8")
	public @ResponseBody String deleteUser(@ModelAttribute("user") User user, @PathVariable int id, Model model){
		
		logger.log(Level.INFO, "in deleteUser annotated with RequestMapping method.DELETE");
		
		if(user != null && user.getRole() != null && "A".equals(user.getRole())) {
		
			//désactive l'utilisateur dans la table user
			int result = userDAO.activateDeactivateUser(id, (byte)0);

			//int result = 1;
			if(result > 0) {
				return "{\"status\":\"ok\"}";
			}
		}
		
		return "{\"status\":\"error\"}";
	}
	
	
	//*********************************************************************************************************************
	//*********************************************************************************************************************
	
	
	/**********************************************************************************************************************
	 * Fonction toggleUser(User, Model)
	 * 
	 * Permet de gérer un requête '/toggleUser' avec la méthode PUT
	 * 
	 * Cette méthode permet uniquement à un administrateur d'activer ou désactiver un utilisateur de la bd
	 * On n'efface par l'utilisateur de la bd; on ne fait que mettre le champ 'is_active' à 0 ou à 1
	 * 	
	 * La page d'utilisateur permet à un administrateur de cliquer sur un bouton qui alternativement active ou désactive un utilisateur
	 * 
	 * On ne fait donc que changer la valeur du champ 'is_active' de la db
	 * 	- si la valeur est 1, elle est changée pour 0
	 * 	- si la valeur est 0, elle est changée pour 1
	 * 
	 * La valeurs du paramètre id est par la valeur transmise dans l'url
	 * 
	 * @param user: User: repésente l'attribut de session 'user' 
	 * @param id: int: l'identifiant de l'utilisateur
	 * @param model: (Model): représente le modèle de l'application 
	 * 
	 * @return: - String: "users": la vue users.jsp affichant la liste des utilisateurs
	 *
	 * 			- String: "{profile:deleted}": une chaîne représentant un objet json indiquant le succès la page topics.jsp si l'utilisateur n'est pas un administrateur ou s'lil n'est pas connecté
	 * 
	 **********************************************************************************************************************/
	
	@RequestMapping(value="/toggleUser/{id}", method = RequestMethod.PUT, produces = "application/json;charset=UTF-8")
	public @ResponseBody String toggleUser(@ModelAttribute("user") User user, @PathVariable int id, Model model){
		
		logger.log(Level.INFO, "_+_+_+_+_+_+_+_+_+_+_+_+ in deleteUser annotated with RequestMapping method.DELETE");
		
		if(user != null && user.getRole() != null && "A".equals(user.getRole())) {
		
			//récupère l'utilisateur
			User userToUpdate =  userDAO.getUserById(id);
			
			//récupère le statut (is_active) de l'utilisateur
			byte status =  userToUpdate.getIsActive();
			
			//modifie la valeur du statut en effectuant un XOR: [ status ^1 ] => (0 devient 1,  1 devient 0)
			int result = userDAO.activateDeactivateUser(userToUpdate.getId(), (byte)(status^1));

			//int result = 1;
			if(result > 0) {
				//succès
				return "{\"status\":\"ok\"}";
			}
		}
		//échec
		return "{\"status\":\"error\"}";
	}
	
	//*********************************************************************************************************************
	//*********************************************************************************************************************
	
}
