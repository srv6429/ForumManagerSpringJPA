package org.olumpos.forum.dao;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.olumpos.forum.entity.User;
import org.olumpos.forum.factory.EntityManagerFactoryBean;
import org.olumpos.forum.util.ForumDateFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

/********************************************************************************************************************************************
 * 
 * @author D. Michon
 * 
 * @Description:
 *  
 * Classe qui permet d'accéder à la base de données pour créer, mettre à jour et récupérer les données de la table 'user' concernant tous les utilisateurs
 * 
 *  
 *
 ********************************************************************************************************************************************/


@Repository
public class UserDAOImpl implements UserDAO{

	//@Autowired
	EntityManagerFactoryBean entityManagerFactoryBean;
	
	Logger logger =  Logger.getLogger(UserDAOImpl.class.getName());
	
	/**
	 * Constructeur
	 * Permet l'initialisation de l'objet entityManagerFactoryBean qui  pour tâche de gégérer
	 * un objet de type EntityManagerFactory nécessaire pour accdéder à la base de données
	 */
	@Autowired
	public UserDAOImpl() {
		//initialisation
		this.entityManagerFactoryBean =  EntityManagerFactoryBean.getEntityManagerFactoryBean();
	}
		
	/**
	 * Appelé après la construction
	 * Utilisé ici simplement pour vérifier si l'objet entityManagerFactoryBean a bien été initialisé
	 */
	@PostConstruct
	public void init() {
		logger.log(Level.INFO, "in UserDAO.init(PostConstruct): entityManagerFactoryBean=============> " + entityManagerFactoryBean);

	}
	//*********************************************************************************************************************
	//*********************************************************************************************************************
	
	/**********************************************************************************************************************
	 * Fonction getAllUsers()
	 * Permet d'obtenir tous les tuples de la table user
	 * 
	 * Obtention d'un objet de type EntityManager créé par EntityManagerFactory
	 * 
	 * Utilisation de TypedQuery qui permet de vérifier le bon type à la compilation
	 * 
	 * Utilisation de la @NamedQuery  "User.findAll" définie dans la classe User.class
	 * 
	 * @param: aucun
	 * 
	 * @return: une liste contenant des Objets de type User qui on été instanciés pour chaque tuple de la table 'user'
	 * 
	 **********************************************************************************************************************/
	
	public List<User> getAllUsers() {

		logger.log(Level.INFO, "in userDAO.getAllUsers()");
		
		//Obtention d'un objet de type EntityManager créé lors dU premier appel et retourné
		EntityManager em =  entityManagerFactoryBean.getEntityManagerfactory().createEntityManager();
		
		TypedQuery<User> query =  em.createNamedQuery("User.findAll", User.class);
		
		List<User> users = query.getResultList();
		
		//logger.log(Level.INFO, "in userDAO.getAllUser() users " + users);
		
		em.close();
		
		return users;
		

	}
	
	//*********************************************************************************************************************
	//*********************************************************************************************************************
	
	/**********************************************************************************************************************
	 * Fonction getAllActiveUsers()
	 * Permet d'obtenir tous les utilisateurs actifs de la table user 
	 *
	 * Obtention d'un objet de type EntityManager créé par EntityManagerFactory
	 * 
	 * Utilisation de TypedQuery pour s'assurer à la compilation que le bon type est obtenu
	 * 
	 * Utilisation de la @NamedQuery  "User.findAllActive" définie dans la classe User.class
	 * 
	 * @param: aucun
	 * 
	 * @return: une liste contenant des Objets de type User qui on été instanciés pour chaque tuple retourné de la table 'user'
	 * 
	 **********************************************************************************************************************/
	
	@Override
	public List<User> getAllActiveUsers() {

		logger.log(Level.INFO, "in userDAO.getAllActiveUsers()");
		
		//Obtention d'un objet de type EntityManager créé lors dU premier appel et retourné
		EntityManager em =  entityManagerFactoryBean.getEntityManagerfactory().createEntityManager();
		
		//création d'une TypedQuery
		TypedQuery<User> query =  em.createNamedQuery("User.findAllActive", User.class);
		//paramètre 1 pour les utilisateurs actifs
		query.setParameter("isActive", (byte)1); 
		
		//obtention de la liste de la base de données
		List<User> users = query.getResultList();
		
	//	logger.log(Level.INFO, "in userDAO.getAllUser() users " + users);
		
		//fermeture de EntityManager
		em.close();
		
		return users;
		

	}
	
	//*********************************************************************************************************************
	//*********************************************************************************************************************
	
	/**********************************************************************************************************************
	 * Fonction getyUserById(int)
	 * Permet d'obtenir un utilisateur de la table user dont l'id correspond au paramètre 
	 * 
	 * Obtention d'un objet de type EntityManager créé par EntityManagerFactory
	 * 
	 * Utilisation de la fonction EntityManager find() avec le paramètre de classe permettant 
	 * 
	 * de vérifier le type à la compilation
	 * 
	 * 
	 * @param: id (int) représentant l'identifiant de l'utilisateur
	 * 
	 * @return: - un objet de type User instancié si L'utilisateur a été trouvé dans la table user
	 * 			- null si l'id n'est pas trouvé
	 * 
	 **********************************************************************************************************************/
	
	@Override
	public User getUserById(int id) {
		
		logger.log(Level.INFO, "in userDAO.getUserById(): user id: " + id);
		
		//Obtention d'un objet de type EntityManager créé lors dU premier appel et retourné
		EntityManager em =  entityManagerFactoryBean.getEntityManagerfactory().createEntityManager();
		
		User user = em.find(User.class, id);
		
	//	logger.log(Level.INFO, "in userDAO.getUserById(): user: " + user);
		
		em.close();
		
		return user;

	}
	
	//*********************************************************************************************************************
	//*********************************************************************************************************************
	
	/**********************************************************************************************************************
	 * Fonction getUserbyUsername(String)
	 * Permet d'obtenir un utilisateur de la table user 
	 * 
	 * Obtention d'un objet de type EntityManager créé par EntityManagerFactory
	 * 
	 * Utilisation de TypedQuery pour s'assurer à la compilation que le bon type est obtenu
	 * 
	 * Utilisation de la @NamedQuery  "User.findByUsername" définie dans la classe User.class
	 * 
	 * @param: username (String) représetnant le nom d'utilisateur
	 * 
	 * @return: - un objet de type User instancié si l'utilisateur a été trouvé dans la table user
	 * 			- null si le 'username' n'est pas trouvé
	 * 
	 **********************************************************************************************************************/

	@Override
	public User getUserByUsername(String username) {
	
		logger.log(Level.INFO, " Getting user by username: " + username);
		
		//Obtention d'un objet de type EntityManager créé lors dU premier appel et retourné
		EntityManager em =  entityManagerFactoryBean.getEntityManagerfactory().createEntityManager();
		
		User user = null;
		
		try {
			
		//	String sql = "select u from User u where username=:username or email=:username";
			//Query query = em.createQuery(sql);
			
			//NamedQueyr définie dans la classe User
			TypedQuery<User> query =  em.createNamedQuery("User.findByUsername", User.class);
			//initialisation du paramètre
			query.setParameter("username", username);

			//Obtention du seul résultat possible car username est unique
			user = (User)query.getSingleResult();
			
		//	logger.log(Level.INFO, "User retrieved from database: =====================> " + user);
			
		}
		catch(Exception e) {
			//e.printStackTrace();
			logger.log(Level.INFO, "Erruer lors de la tentative d'obtention de l'utilisateur avec le nom: =====================> " + username);
			
		}

		finally {
			//fermeture de l'entityManager
			if(em != null) {
				em.close();
			}
		}
		
		return user;
	}
	
	//*********************************************************************************************************************
	//*********************************************************************************************************************
	
	
	/**********************************************************************************************************************
	 * Fonction getUser(String, String)
	 * 
	 * Permet d'obtenir un utilisateur de la table user
	 * 
	 * Obtention d'un objet de type EntityManager créé par EntityManagerFactory
	 * 
	 * Utilisation de TypedQuery pour s'assurer à la compilation que le bon type est obtenu
	 * 
	 * Utilisation de la @NamedQuery  "User.findByUsernameAndPassword" définie dans la classe User.class
	 * 
	 * @param: 	- username (String) représetnant le nom d'utilisateur
	 * 			- password (String) représentant le mot de passe de l'utilisateur
	 * @return: - un objet de type User instancié si l'utilisateur a été trouvé dans la table user
	 * 			- null si le 'username' n'est pas trouvé
	 * 
	 **********************************************************************************************************************/
	
	@Override
	public User getUser(String username, String password) {
	
		logger.log(Level.INFO, " Getting user by username: " + username + " ; and password: " + password);
		
		//Obtention d'un objet de type EntityManager créé lors dU premier appel et retourné
		EntityManager em =  entityManagerFactoryBean.getEntityManagerfactory().createEntityManager();
		
		User user = null;
		
		try {
		//	
			//String sql = "select u from User u where (username=:username or email=:username) and password=:password";
			//String sql = "select * from user where username=? and password=?";
			//Query query = em.createNativeQuery(sql);
			//query.setParameter(1, username);
			//query.setParameter(2, username);
			//query.setParameter(3,  digestString(password, "MD5"));
			
			
			TypedQuery<User> query =  em.createNamedQuery("User.findByUsernameAndPassword", User.class);
			
			//initialisation des paramètre
			query.setParameter("username", username);
			//encryptage du mot de passe enregistré dans la bd
			query.setParameter("password",  digestString(password, "MD5"));
			
		//	logger.log(Level.INFO, "password: "+password+"; password encrypted: " + encrypter(password)); 
		//	query.setParameter("password", encrypter(password));
		
			//obtention d'un résultat unique
			user = query.getSingleResult();
			
			//logger.log(Level.INFO, "user from database: ==========================> " + user);
			
		}
		catch(Exception e) {
			//e.printStackTrace();
			logger.log(Level.INFO, "Wrong user or password ");
		}

		finally {
			//fermeture de l'entity mangager
			if(em != null) {
				em.close();
			}
		}
		
		return user;
	}	
	
	
	
	/**********************************************************************************************************************
	 * Fonction geActivetUser(String, String)
	 * 
	 * Permet d'obtenir un utilisateur actif de la table user
	 * 
	 * Obtention d'un objet de type EntityManager créé par EntityManagerFactory
	 * 
	 * Utilisation de TypedQuery pour s'assurer à la compilation que le bon type est obtenu
	 * 
	 * Utilisation de la @NamedQuery  "User.findActiveByUsernameAndPassword" définie dans la classe User.class
	 * 
	 * @param: 	- username (String) représetnant le nom d'utilisateur
	 * 			- password (String) représentant le mot de passe de l'utilisateur
	 * @return: - un objet de type User instancié si l'utilisateur a été trouvé dans la table user
	 * 			- null si le 'username' n'est pas trouvé
	 * 
	 **********************************************************************************************************************/
	
	@Override
	public User getActiveUser(String username, String password) {
	
		logger.log(Level.INFO, " Getting active user by username: " + username + " ; and password: " + password);
		
		//Obtention d'un objet de type EntityManager créé lors dU premier appel et retourné
		EntityManager em =  entityManagerFactoryBean.getEntityManagerfactory().createEntityManager();
		
		User user = null;
		
		try {

			//cr.ation de la Query
			TypedQuery<User> query =  em.createNamedQuery("User.findActiveByUsernameAndPassword", User.class);
			
			//initialisation des paramètres
			query.setParameter("username", username);
			//encryptage du mot de passe enregistré dans la bd
			query.setParameter("password",  digestString(password, "MD5"));
			//query.setParameter("password", encrypter(password));
			//recherche une utilisateur actif seulement
			query.setParameter("isActive",  (byte) 1);
			
			//obtention d'un résultat unique
			user = (User)query.getSingleResult();
			
			//logger.log(Level.INFO, "user from database: ==========================> " + user);
			
		}
		catch(Exception e) {
			//e.printStackTrace();
			logger.log(Level.INFO, "Wrong user or password ");
		}

		finally {
			//fermeture de l'entity mangager
			if(em != null) {
				em.close();
			}
		}
		
		return user;
	}	
	
	//*********************************************************************************************************************
	//*********************************************************************************************************************
	
	/**********************************************************************************************************************
	 * Fonction getLastInsertedUserId()
	 * Permet d'obtenir l'identifiant du dernier utilisateur inséré dans de la table user 
	 * 
	 * Obtention d'un objet de type EntityManager créé par EntityManagerFactory
	 * 
	 * Utilisation d'une requête naturelle du langae SQL et la fonction max() pour obtenir l'id qui possède la plus grande valeur,
	 * étant donc le dernier utilisateur inséré dans la base de données
	 * 
	 * @param: aucun
	 * 
	 * @return: - un entier représentant l'identifiant de l'utilisateur
	 * 
	 **********************************************************************************************************************/
	
	@Override
	public int getLastInsertedUserId() {
		
		//Obtention d'un objet de type EntityManager créé lors dU premier appel et retourné
		EntityManager em =  entityManagerFactoryBean.getEntityManagerfactory().createEntityManager();
		
		logger.log(Level.INFO, "getting last user inserted ");
		
			int lastId = 0;
			try {
				
				//requête SQL qui recherche l'id le plus grand (max)
				String sql =  "select max(id) from user";
				
				//création de la Query
				Query query = em.createNativeQuery(sql);
	
				//obtention du seul résultat possible
				lastId = (int)query.getSingleResult();
				
				
				logger.log(Level.INFO, "Dernier id obtenu: " + lastId);
				
			
			}
			catch(Exception e) {
				logger.log(Level.INFO, "Erreur lors de la tentative d'obtenir le dernier id ");
				//e.printStackTrace();
			}

			finally {
				//fermeture de l'entity mangager
				if(em != null) {
					em.close();
				}
			}

		return lastId;
	
		
	}
	//*********************************************************************************************************************
	//*********************************************************************************************************************
	
	/**********************************************************************************************************************
	 * Fonction createUser(User)
	 * Permet d'insérer un nouvel utilisateur dans la bd 
	 * 
	 * Obtention d'un objet de type EntityManager créé par EntityManagerFactoryné
	 * 
	 * @param: User user: un objet de type User initialisé avec les champs username, email et password  
	 * 
	 * @return: - un entier représentant le nombre de lignes affectées par la requête, 
	 * 			i.e.: 	- 1 si le changement a été effectué avec succès
	 * 					- 0 sinon	
	 * 
	 **********************************************************************************************************************/
	
	
	@Override
	@Transactional
	public int createUser(User user) {
		
		if(user == null) {
			return 0;
		}
		
		logger.log(Level.INFO, "creating a new user: with username: " + user.getUsername() + "; email: " + user.getEmail());
		
		//Obtention d'un objet de type EntityManager 
		EntityManager em =  entityManagerFactoryBean.getEntityManagerfactory().createEntityManager();
		
		

		int result = 0;
		//transaction
		EntityTransaction transaction = null;
		
		try {
			//obtention d'une transaction
			transaction =  em.getTransaction();
			//début de transaction
			transaction.begin();
			
			//utilisation d'une requête SQL pour l'insertion 
			Query query = em.createNativeQuery("insert into user (username, email, password) values (?, ?, ?)");
			
			//initialisation des paramètres
			query.setParameter(1, user.getUsername());
			query.setParameter(2, user.getEmail());
			query.setParameter(3,  digestString(user.getPassword(), "MD5"));
		//	query.setParameter(3, encrypter(user.getPassword()));
			
			//exécution
			result = query.executeUpdate();
			
			//logger.log(Level.INFO, "in createUser =================> result: " + result);
			
			//si l'exécution a été effectués avec succès, on persiste dans la bd
			transaction.commit();
			
		}
		catch(Exception e) {
			//pas vraiement nécessaire car le commit n'est pas effectué
			//transaction.rollback();
			user.setMessage("Une erreur s'est produite lors de la tentative de création de l'utilisateur");
			logger.log(Level.INFO, "Une erreur s'est produite lors de la tentative de création de l'utilisateur");
			//e.printStackTrace();
		}

		finally {
			//fermeture de l'entity mangager
			if(em != null) {
				em.close();
			}
		}
		
		return result;
	}
	
	//*********************************************************************************************************************
	//*********************************************************************************************************************
	/**********************************************************************************************************************
	 * Fonction createUser(String, String, String)
	 * Permet d'insérer un nouvel utilisateur dans la bd 
	 * Variante de la précédente mais avec les paramètres sans utilisateur
	 * 
	 * Obtention d'un objet de type EntityManager créé par EntityManagerFactory
	 * 
	 * @param: 	- username(String): le nom du nouvel utilisateur 
	 * 			- email(String): le courriel
	 * 			- password(String): le mot de passe  
	 * 
	 * @return: - un entier représentant le nombre de lignes affectées par la requête, 
	 * 			i.e.: 	- 1 si le changement a été effectué avec succès
	 * 					- 0 sinon	
	 * 
	 **********************************************************************************************************************/
	
	@Override
	@Transactional
	public int createUser(String username, String email, String password) {

		logger.log(Level.INFO, "creating a new user: with username: " + username + "; email: " + email);
		
		//Obtention d'un objet de type EntityManager 
		EntityManager em =  entityManagerFactoryBean.getEntityManagerfactory().createEntityManager();


		int result = 0;
		
		EntityTransaction transaction = null;
		
		try {
			
			//obtention d'une transaction
			transaction =  em.getTransaction();
			//début de transaction
			transaction.begin();

			//requête native SQL
			final String sql = "insert into user (username, email, password) values (?, ?, ?)";
			
			//Création de la Query
			Query query = em.createNativeQuery(sql);
			
			//initialisation des paramètres
			query.setParameter(1, username);
			query.setParameter(2, email);
			query.setParameter(3,  digestString(password, "MD5"));
		//	query.setParameter(3, encrypter(password));
			
			//exécution de la requête
			result = query.executeUpdate();
		
			//persister dans la bd
			transaction.commit();

			//logger.log(Level.INFO, "in createUser =================> result: " + result);

		}
		catch(Exception e) {
			//inverser et fermer la transaction e
			transaction.rollback();
			logger.log(Level.INFO, "in createUser ; error while trying to create user  => result: " + result);
			//e.printStackTrace();
		}

		finally {
			//fermeture de l'entity mangager
			if(em != null) {
				em.close();
			}
		}
		
		return result;
	}
	

	//*********************************************************************************************************************
	//*********************************************************************************************************************
	
	/**********************************************************************************************************************
	 * Fonction updateUser(User)
	 * Permet de modifier les infos (username, email ou password seulement) 
	 * 
	 * Obtention d'un objet de type EntityManager créé par EntityManagerFactory
	 * 
	 * @param: User user: 	un objet de type User initialisé avec les champs username, email et password 
	 * 						tels que modifiés par l'utilisateur
	 * 
	 * @return: - un entier représentant le nombre de lignes affectées par la requête, 
	 * 			i.e.: 	- 1 si le changement a été effectué avec succès
	 * 					- 0 sinon	
	 * 
	 **********************************************************************************************************************/
	
	@Transactional
	@Override
	public int updateUser(User user) {
		
		logger.log(Level.INFO, "updating user with username: " + user.getUsername() + "; email: " + user.getEmail());
		
		//Obtention d'un objet de type EntityManager 
		EntityManager em =  entityManagerFactoryBean.getEntityManagerfactory().createEntityManager();
		
		int result = 0;
		
		EntityTransaction transaction = null;
		
		try {
			//obtention d'une transaction
			transaction =  em.getTransaction();
			//début d'une transaction
			transaction.begin();
			
			//requête SQL
			final String sql = "update user set username=:username, email=:email, password=:password, update_date=:updateDate where id=:id";
			
			//Création de la Query
			Query query = em.createNativeQuery(sql);
			
			//intialisation des paramètres
			query.setParameter("username", user.getUsername());
			query.setParameter("email", user.getEmail());
			query.setParameter("password", digestString(user.getPassword(), "MD5"));
//			query.setParameter("password", encrypter(user.getPassword()));
			query.setParameter("updateDate", ForumDateFormatter.fromLocalDateTimeToString(LocalDateTime.now()));
			query.setParameter("id", user.getId());
			
			//exécution
			result = query.executeUpdate();

			//persister dans la bd
			transaction.commit();
			
			//logger.log(Level.INFO, "in user updated  with result: " + result);
			
		}
		catch(Exception e) {
			//inverser et fermer la transaction e
			transaction.rollback();
			logger.log(Level.INFO, "in updateUser ; error while trying to update user  => result: " + result);
			//e.printStackTrace();
		}

		finally {
			//fermeture de l'entity mangager
			if(em != null) {
				em.close();
			}
		}
		return result;
	}

	//*********************************************************************************************************************
	//*********************************************************************************************************************
	
	//*********************************************************************************************************************
	
	/**********************************************************************************************************************
	 * Fonction updateUser(int, String, String, String)
	 * 
	 * Permet de modifier les infos (username, email ou passwrd seulement) 
	 * 
	 * Variation de la fonction précédente: paramètres individuels et non encapsulés dans un objet de type User
	 * 
	 * Obtention d'un objet de type EntityManager créé par EntityManagerFactory
	 * 
	 * @param: 	- id(int): l'identifiant de l'utilisateur
	 * 			- username(String): le nom de l'utilisateur 
	 * 			- email(String): le courriel
	 * 			- password(String): le mot de passe
	 *   
	 * @return: - un entier représentant le nombre de lignes affectées par la requête, 
	 * 			i.e.: 	- 1 si le changement a été effectué avec succès
	 * 					- 0 sinon	
	 * 
	 **********************************************************************************************************************/
	@Transactional
	@Override
	public int updateUser(int id, String username, String email, String password) {
		
		logger.log(Level.INFO, " updating user with username: " + username + "; email: " + email);
		
		//Obtention d'un objet de type EntityManager 
		EntityManager em =  entityManagerFactoryBean.getEntityManagerfactory().createEntityManager();
		
		logger.log(Level.INFO, "Register new user using JPA");
		
		int result = 0;
		
		EntityTransaction transaction = null;
		
		try {
			//obtention d'une transaction
			transaction =  em.getTransaction();
			//début d'une transaction
			transaction.begin();
			
			//requête SQL			
			final String sql = "update user set username= :username, email= :email, password= :password, update_date= :updateDate where id= :id";
			
			//Création de la requête
			Query query = em.createNativeQuery(sql);
			
			//intialisation des paramètres
			query.setParameter("username", username);
			query.setParameter("email", email);
			query.setParameter("password", digestString(password, "MD5"));
		//	query.setParameter("password", encrypter(password));
			query.setParameter("updateDate", ForumDateFormatter.fromLocalDateTimeToString(LocalDateTime.now()));
			query.setParameter("id", id);
			
			//exécution de la requête
			result = query.executeUpdate();
				
			//persister dans la bd
			transaction.commit();
			
			//logger.log(Level.INFO, "in updateUser =================> result: " + result);
			
		}
		catch(Exception e) {
			//inverser et fermer la transaction e
			transaction.rollback();
			logger.log(Level.INFO, "in updateUser ; error while trying to update user  => result: " + result);
		//	e.printStackTrace();
		}

		finally {
			//fermeture de l'entity mangager
			if(em != null) {
				em.close();
			}
		}
		return result;
	}
	
	
	//*********************************************************************************************************************
	//*********************************************************************************************************************
	
	
	/**********************************************************************************************************************
	 * Fonction activateDeactivateUser(int, byte)
	 * 
	 * Permet d'activer ou désactiver un utilisateur en changeant la variable is_active(0 ou 1) de la table user 
	 * 
	 * Obtention d'un objet de type EntityManager créé par EntityManagerFactory
	 * 
	 * @param: 	- userId (int): l'identifiant de l'utilisateur
	 * 			- status(byte): tsatut avec valeur 0 pour désactiver, ou 1 pour activer 
	 * 
	 * @return: - un entier représentant le nombre de lignes affectées par la requête, 
	 * 			i.e.: 	- 1 si le changement a été effectué avec succès
	 * 					- 0 sinon	
	 * 
	 **********************************************************************************************************************/
	@Transactional
	@Override
	public int activateDeactivateUser(int userId, byte status) {

		
		logger.log(Level.INFO, "Activate/Deactivate user with id: "  + userId + "; status: " + status);
		
		//Obtention d'un objet de type EntityManager 
		EntityManager em =  entityManagerFactoryBean.getEntityManagerfactory().createEntityManager();

		
		int result = 0;
		
		EntityTransaction transaction = null;
		
		try {
			
			//obtention d'une transaction
			transaction =  em.getTransaction();
			//début d'une transaction
			transaction.begin();
			
			//requête SQL	
			final String sql = "update user set is_active= :isActive, update_date= :updateDate where id= :id";
			
			//Création de la requête
			Query query = em.createNativeQuery(sql);
			
			//intialisation des paramètres
			query.setParameter("isActive", status);
			query.setParameter("updateDate", ForumDateFormatter.fromLocalDateTimeToString(LocalDateTime.now()));
			query.setParameter("id", userId);
							
			//exécution de la requête
			result = query.executeUpdate();
		
			//persister dans la bd
			transaction.commit();
				
			//logger.log(Level.INFO, "in activateDeactivateUser =================> result: " + result);
			
		
		}
		catch(Exception e) {
			//inverser et fermer la transaction e
			transaction.rollback();
			logger.log(Level.INFO, "in activateDeactivateUser ; error while trying to update user  => result: " + result);
			//e.printStackTrace();
		}

		finally {
			//fermeture de l'entity mangager
			if(em != null) {
				em.close();
			}
		}
		return result;

	}
		
	
	//*********************************************************************************************************************
	//*********************************************************************************************************************
	
	
	/**********************************************************************************************************************
	 * Fonction deleteUserFromDB(int)
	 * Permet d'effacer un utilisateur de la bd  
	 * 
	 * Remarque: n'est utilisée que pour les tests JUnit
	 * 
	 * Obtention d'un objet de type EntityManager créé par EntityManagerFactory
	 * 
	 * @param: 	- userId(int): l'identifiant de l'utilisateur

	 * 
	 * @return: - un entier représentant le nombre de lignes affectées par la requête, 
	 * 			i.e.: 	- 1 si le changement a été effectué avec succès
	 * 					- 0 sinon	
	 * 
	 **********************************************************************************************************************/
	@Transactional
	@Override
	public int deleteUserFromDB(int userId) {

		logger.log(Level.INFO, "Deleting user with id: "  + userId);
		
		//Obtention d'un objet de type EntityManager 
		EntityManager em =  entityManagerFactoryBean.getEntityManagerfactory().createEntityManager();
		

		
		int result = 0;
		
		EntityTransaction transaction = null;
		
		try {
			
			//obtention d'une transaction
			transaction =  em.getTransaction();
			//début d'une transaction
			transaction.begin();
			
			//requête native SQL
			final String sql = "delete from user where id= :id";
			
			//Création de la Query
			Query query = em.createNativeQuery(sql);
			
			//intialisation du paramètre
			query.setParameter("id", userId);
			
			//exécution de la requête
			result = query.executeUpdate();
		
			//persister dans la bd
			transaction.commit();
				
			
			//logger.log(Level.INFO, "in update =================> result: " + result);
			
		}
		catch(Exception e) {
			//inverser et fermer la transaction e
			transaction.rollback();
			//e.printStackTrace();
		}

		finally {
			//fermeture de l'entity mangager
			if(em != null) {
				em.close();
			}
		}
		return result;

	}
	//*********************************************************************************************************************
	//*********************************************************************************************************************
	
	
	/**********************************************************************************************************************
	 * Fonction digestString(String, String)
	 * 
	 * Permet d'encrypter une chaîne de caractères en fonction de l'algorithme spécifié
	 * 
	 * Utilisé pour encrypter les mots de passe des utilisateurs  
	 * 
	 * @param: 	- str (String): la chaîne à encrypter
	 * 			- algorithme (String): l'algorithme utilisé pour l'encryptage

	 * 
	 * @return: - un objet de type String qui représente la chaîne encryptée 
	 * 
	 **********************************************************************************************************************/
	private String digestString(String str, String algorithme) throws NoSuchAlgorithmException {
		
		
		//obtention d'une instance
		MessageDigest messageDigest = MessageDigest.getInstance(algorithme);

		//découpage de la chaîne en tableau de byte pour chaque caractère
		byte[] bytes = str.getBytes();
		
		//encryptage des caractère
		byte[] hash = messageDigest.digest(bytes);
		
		String hashString = "";
		
		for (int i = 0; i < hash.length; i++) {
			int v =  hash[i] & 0xff; // for bytes
			//rajouter un 0 devant les nombre inférieurs à 16
	
			if(v < 16) {
				hashString += "0";
			}

			hashString +=  Integer.toString(v, 16).toLowerCase();
		}
		
		//logger.log(Level.INFO, "hashString: " + hashString);
		
		
		return hashString;

	}
		
	

}//end class
