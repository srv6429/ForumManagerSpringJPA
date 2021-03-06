package org.olumpos.forum.dao;



import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.olumpos.forum.entity.Post;
import org.olumpos.forum.entity.Topic;
import org.olumpos.forum.factory.EntityManagerFactoryBean;
import org.olumpos.forum.util.ForumDateFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/********************************************************************************************************************************************
 * <br>
 * @author daristote<br>
 * <br>
 * Description:<br>
 * <br>
 * Classe qui permet d'accéder à la base de données pour créer, mettre à jour et récupérer les données des 'topics' (thèmes de discussion)<br>
 * <br>
 *
 ********************************************************************************************************************************************/

@Repository
public class TopicDAOImpl implements TopicDAO{

	//Logger pour afficher les infos en console
	Logger logger =  Logger.getLogger(TopicDAOImpl.class.getName());
	
	//Fabrique qui permet de créer et d'accécer à un EntityManagerFactory
	EntityManagerFactoryBean entityManagerFactoryBean;
	
	//Appelé lors du déploiement de l'applcation par le serveur
	//Constructeur qui permet d'initialisé ou de récupérer le singleton EntityManagerFactoryBean
	@Autowired
	public TopicDAOImpl() {
		this.entityManagerFactoryBean =  EntityManagerFactoryBean.getEntityManagerFactoryBean();
	}
	
	
	@PostConstruct
	public void init() {
		logger.log(Level.INFO, "int TopicDAO.init(PostConstruct): entityManagerFactoryBean=============> " + entityManagerFactoryBean);

	}
	
	//*********************************************************************************************************************
	//*********************************************************************************************************************
	
	/**********************************************************************************************************************
	 * Fonction qui permet d'obtenir tous les tuples de la table topic<br>
	 * <br>
	 * Obtention d'un objet de type EntityManager créé par EntityManagerFactory<br>
	 * <br>
	 * Utilisation de TypedQuery qui permet de vérifier le bon type à la compilation<br>
	 * <br>
	 * Utilisation de la @NamedQuery  "Topic.findAll" définie dans la classe Topic<br>
	 * <br>
	 * param: aucun<br>
	 * <br>
	 * @return: une liste contenant des Objets de type Topic qui ont été instanciés pour chaque tuple de la table 'topic'<br>
	 * <br>
	 **********************************************************************************************************************/
	
	@Override
	public List<Topic> getAllTopics() {
		
		logger.log(Level.INFO, "getAllTopics()");
		
		//Obtention d'un objet de type EntityManager créé lors dU premier appel et retourné
		EntityManager em =  entityManagerFactoryBean.getEntityManagerfactory().createEntityManager();
		
		List<Topic> topics = null;
		
		try {

			//Requête @Named de la classe Topic
			TypedQuery<Topic> query =  em.createNamedQuery("Topic.findAll", Topic.class);
			//Obtentiton de la liste
			topics = query.getResultList();
			
		//	logger.log(Level.INFO, "Obtention de la liste des topics: size: " + topics.size());
		}
		
		catch(Exception e) {
			logger.log(Level.INFO, "Une erreur est survenue lors de la tentative d'obtenir la liste des topics");
		//	e.printStackTrace();
		}

		finally {
			//fermeture de l'EntityManager
			if(em != null) {
				em.close();
			}
		}

		return topics;
	}
	
	//*********************************************************************************************************************
	//*********************************************************************************************************************
    
	/**********************************************************************************************************************
	 * Fonction qui permet d'obtenir tous les topics actifs de la table 'topic' <br>
	 * <br>
	 * Obtention d'un objet de type EntityManager créé par EntityManagerFactory<br>
	 * <br>
	 * Utilisation de TypedQuery qui permet de vérifier le bon type à la compilation<br>
	 * <br>
	 * Utilisation de la @NamedQuery  "Topic.findAll" définie dans la classe Topic<br>
	 * <br>
	 * param: aucun<br>
	 * <br>
	 * @return: une liste contenant des objets de type Topic dont le statut ('is_open = 1') est ouvert<br>
	 * <br>
	 * 
	 **********************************************************************************************************************/
	
	@Override
	public List<Topic> getAllOpenTopics() {
		
		logger.log(Level.INFO, "Getting all open topics");
		
		//Obtention d'un objet de type EntityManager créé lors dU premier appel et retourné
		EntityManager em =  entityManagerFactoryBean.getEntityManagerfactory().createEntityManager();	
		
		List<Topic> topics = null;
		
		try {

			//Requête @Named de la classe Topic
			TypedQuery<Topic> query =  em.createNamedQuery("Topic.findAllOpen", Topic.class);
			
			//initialisation du paramètre
			query.setParameter("isOpen", (byte)1);

			//Obtention de la liste
			topics = query.getResultList();
			
			//logger.log(Level.INFO, "Obtention de la liste des topics: size: " + topics.size());
		}
		catch(Exception e) {
			logger.log(Level.INFO, "Une erreur est survenue lors de la tentative d'obtenir la liste des topics ouverts");
		//	e.printStackTrace();
		}

		finally {
			//fermeture de l'EntityManager
			if(em != null) {
				em.close();
			}
		}
		return topics;
	}
	
	
	//*********************************************************************************************************************
	//*********************************************************************************************************************
	
	/**********************************************************************************************************************
	 * Fonction qui permet d'obtenir un topic de la table 'topic' dont l'identifiant est le paramètre 'id' <br>
	 * <br>
	 * Obtention d'un objet de type EntityManager créé par EntityManagerFactory<br>
	 * <br>
	 * Utilisation de la fonction EntityManager.'find()'  avec le paramètre id et le nom de la classe pour vérifier le type<br>
	 * <br>
	 * @param id :(int): l'identifiant du topic<br>
	 * <br>
	 * @return: - un objet de type Topic<br>
	 * 			- null si l'id n'est pas trouvé dans la bd<br>
	 * <br>
	 * 
	 **********************************************************************************************************************/
	
	
	@Override
	public Topic getTopic(int id) {

		logger.log(Level.INFO, "getting topic with id: " + id);
		
		//Obtention d'un objet de type EntityManager créé lors dU premier appel et retourné
		EntityManager em =  entityManagerFactoryBean.getEntityManagerfactory().createEntityManager();
				
		Topic topic =  null;
		
		try {

			//Obtention du topic 
			topic =  em.find(Topic.class, id);
			
			
		}
		catch(Exception e) {
			logger.log(Level.INFO, "Une erreur est survenue lors de la tentative d'obtenir le topic avec id: " + id);
			//e.printStackTrace();
		}

		finally {
			//fermeture de l'EntityManager
			if(em != null) {
				em.close();
			}
		}
		
		return topic;
		
	}
	
	//*********************************************************************************************************************
	//*********************************************************************************************************************
	
	
	/**********************************************************************************************************************
	 * Fonction qui permet d'obtenir l'identifiant du dernier topic inséré dans de la table 'topic' <br>
	 * <br>
	 * Obtention d'un objet de type EntityManager créé par EntityManagerFactory<br>
	 * <br>
	 * Utilisation d'une requête SQL pour obtenir l'identifiant le plus grand (max)<br>
	 * <br>
	 * param: aucun<br>
	 * <br>
	 * @return: - un entier représentant l'identifiant (id) du topic<br>
	 * <br>
	 * 
	 **********************************************************************************************************************/
	
	@Override
	public int getLastInsertedTopicId() {
		
		logger.log(Level.INFO, "Getting last topic inserted ");
		
		//Obtention d'un objet de type EntityManager créé lors dU premier appel et retourné
		EntityManager em =  entityManagerFactoryBean.getEntityManagerfactory().createEntityManager();
		
		int lastId = 0;
		try {
			
			//Requête native SQL 
			final String sql = "select max(id) from topic";
			
			//Création de la Query
			Query query = em.createNativeQuery(sql);
			
			//Obtention du résultat unique
			lastId = (int)query.getSingleResult();
		
		}
		catch(Exception e) {
			logger.log(Level.INFO, "Une erreur est survenue lors de la tentative d'obtenir le dernier id ");
			lastId = 0;
			//e.printStackTrace();
		}

		finally {
			//fermeture de l'EntityManager
			if(em != null) {
				em.close();
			}
		}

		return lastId;
	
		
	}
	//*********************************************************************************************************************
	//*********************************************************************************************************************
	
	
	/**********************************************************************************************************************
	 * Fonction qui permet d'obtenir l'identifiant du dernier topic ouvert inséré dans de la table 'topic' <br>
	 * <br>
	 * Semblableà la précédent mais vérifie si le champ 'is_open = 1'<br>
	 * <br>
	 * Obtention d'un objet de type EntityManager créé par EntityManagerFactory<br>
	 * <br>
	 * Utilisation d'une requête SQL pour obtnir l'identifiant le plus grand (max)<br>
	 * <br>
	 * param: aucun<br>
	 * <br>
	 * @return: - un entier représentant l'identifiant (id) du topic<br>
	 * <br>
	 * 
	 **********************************************************************************************************************/
	@Override
	public int getLastInsertedOpenTopicId() {

		logger.log(Level.INFO, "getting last open topic inserted: ");
		
		//Obtention d'un objet de type EntityManager créé lors dU premier appel et retourné
		EntityManager em =  entityManagerFactoryBean.getEntityManagerfactory().createEntityManager();

		int lastId = 0;
		try {
			
			//Requête native SQL
			Query query = em.createNativeQuery("select max(id) from topic where is_open= :isOpen");
			
			//Initialisation du paramètre
			query.setParameter("isOpen", (byte)1);
			
			lastId = (int)query.getSingleResult();
		
		}
		catch(Exception e) {
			logger.log(Level.INFO, "Une erreur est survenue lors de la tentative d'obtenir le dernier id ");
			lastId = 0;
			//e.printStackTrace();
		}

		finally {
			//fermeture de l'EntityManager
			if(em != null) {
				em.close();
			}
		}

		return lastId;
	
		
	}
	
	
	//*********************************************************************************************************************
	//*********************************************************************************************************************
	
	
	/**********************************************************************************************************************
	 * Fonction qui permet d'obtenir d'insérer un nouveau topic dans la bd <br>
	 * <br>
	 * Obtention d'un objet de type EntityManager créé par EntityManagerFactory<br>
	 * <br>
	 * @param topic: Topic: un objet de type Topic initialisé avec les champs 'title' par l'utilisateur créant le topic  <br>
	 * <br>
	 * @return: - un entier représentant le nombre de lignes affectées par la requête, <br>
	 * 			i.e.: 	- 1 si le topic  a été créé avec succès<br>
	 * 					- 0 sinon	<br>
	 * <br>
	 **********************************************************************************************************************/
	
	@Override
	public int addTopic(Topic topic) {
		
		logger.log(Level.INFO, "Adding topic with title: " + topic.getTitle() + "; creator: " + topic.getCreatorId());
		
		//Obtention d'un objet de type EntityManager créé lors dU premier appel et retourné
		EntityManager em =  entityManagerFactoryBean.getEntityManagerfactory().createEntityManager();
		
		int result = 0;

		EntityTransaction transaction = null;

		try {	
		
			//obtention d'une transaction
			transaction =  em.getTransaction();
			//début de transaction
			transaction.begin();
			
			//Requête native SQL
			final String sql =  "insert into topic (title, creator_id) values (?, ?)";
			
			//Création de la Query
			Query query = em.createNativeQuery(sql);
			
			//Initialisation des paramètres
			query.setParameter(1, topic.getTitle());
			query.setParameter(2, topic.getCreatorId());
			
			//exécution de la requête
			result = query.executeUpdate();
			
			//persiste dans la bd
			transaction.commit();
				
			
		} catch (Exception e) {
			//rollback et fermeture de la transaction
			transaction.rollback();
			logger.log(Level.INFO, "Une erreur est survenue lors de la tentative d'ajout d'un topic");
			topic.setMessage("Une erreur est survenue lors de la tentative d'insertion dans la base de données");
			//e.printStackTrace();
		}

		finally {
			//fermeture de l'EntityManager
			if(em != null) {
				em.close();
			}
		}
	
		return result ;
	}
	
	//*********************************************************************************************************************
	//*********************************************************************************************************************
	
	
	/**********************************************************************************************************************
	 * Fonction qui permet d'insérer un nouveau topic et un post à la fois dans la bd <br>
	 * <br>
	 * Lors de la création d'un topic, l'utilisateur doit obligatoirement créer un post et ajouter un commentaire<br>
	 * pour ne pas avoir de topic vide sans post<br>
	 * <br>
	 * On effectue donc deux opérations: on insère d'abor le Topic et si l'opération réussit, on insère le post<br>
	 * <br>
	 * Pour respecter l'atomicité de la transaction selon le principe ACID, si le topic a été enregistré <br>
	 * mais qu'une erreur est survenue lors de l'engistrement du post, l'opération entière est annulée par un rollback<br>
	 * <br>
	 * On peut ici profiter des avantages d'une transaction: toutes les opérations ne seront persistées dans la bd<br>
	 * ou aucune si une étape de l'opération ne peut être complétée<br>
	 * <br>
	 * Obtention d'un objet de type EntityManager créé par EntityManagerFactory<br>
	 * <br>
	 * @param topic: Topic: un objet de type Topic initialisé avec les champs 'title' par l'utilisateur créant le topic  <br>
	 * @param post: Post: un objet de type Post initialisé avec les champs 'title', 'body' et userId<br>
	 * <br>
	 * @return: - un entier représentant le nombre de lignes affectées par la requête, <br>
	 * 			i.e.: 	- 1 si le topic et post ont été créés avec succès<br>
	 * 					- 0 sinon	<br>
	 * <br>
	 **********************************************************************************************************************/

	

	@Override
	public int addTopic(Topic topic, Post post) {
		
		logger.log(Level.INFO, "Adding topic with title: " + topic.getTitle() + "; creator: " + topic.getCreatorId());
		
		//Obtention d'un objet de type EntityManager créé lors dU premier appel et retourné
		EntityManager em =  entityManagerFactoryBean.getEntityManagerfactory().createEntityManager();
				
		int result = 0;
		EntityTransaction transaction = null;
		try {	
			
			//obtention d'une transaction
			transaction =  em.getTransaction();
			//début de transaction
			transaction.begin();
			
			//Requête native SQL
			final String sql =  "insert into topic (title, creator_id) values (?, ?)";
		
			//Création de la Query
			Query query = em.createNativeQuery(sql);
			
			//Initialisation des paramètres
			query.setParameter(1, topic.getTitle());
			query.setParameter(2, topic.getCreatorId());
			
			//exécution de la seconde requête
			result = query.executeUpdate();
			
			//logger.log(Level.INFO, "new topic added ============ >  " + result);//			
			
			//si la première requêt a été exécutée avec succès
			if(result > 0) {			

				result = 0;
				//Requêt SQL
				final String sql2 =  "insert into post (title, body, user_id) values (?, ?, ?)";
				
				//Création de la Query
				Query query2 = em.createNativeQuery(sql2);
				
				//Initialisation des paramètres
				query2.setParameter(1, post.getTitle());
				query2.setParameter(2, post.getBody());
				query2.setParameter(3, post.getUserId());
			//	query2.setParameter(4, (byte)1);
				
				//exécution de la seconde requête
				result = query2.executeUpdate();
				
				if(result > 0) {
					transaction.commit();
				} else {
					transaction.rollback();
					post.setMessage("Une erreur est survenue lors de la tentative de création du nouveau commnetaire dans la base de données.\n");
					topic.setMessage("Une erreur est survenue lors de la tentative de création du nouveau thème dans la base de données.\n");
				}
			}
		} catch (Exception e) {
			//rollback: annule l'ensemble des opérations
			transaction.rollback();
			logger.log(Level.SEVERE, "Une erreur est survenue lors de la tentative d'ajout d'un topic/post");
			topic.setMessage("Une erreur est survenue lors de la tentative d'ajout d'un topic/post");
			post.setMessage("Une erreur est survenue lors de la tentative de création du nouveau commnetaire dans la base de données.\n");
			//e.printStackTrace();
		}

		finally {
			//fermeture de l'EntityManager
			if(em != null) {
				em.close();
			}
		}
	
		return result;
	}
	
	//*********************************************************************************************************************
	//*********************************************************************************************************************
	
	/**********************************************************************************************************************
	 * Fonction qui permet de modifier les infos (title) d'un topic <br>
	 * <br>
	 * Obtention d'un objet de type EntityManager créé par EntityManagerFactory<br>
	 * <br>
	 * @param topicId: int:  un entier représentant l'identifiant du topic à mettre à jour<br>
	 * @param title: String : le titre à modifier<br>
	 * <br>
	 * @return: - un entier représentant le nombre de lignes affectées par la requête, <br>
	 * 			i.e.: 	- 1 si le changement a été effectué avec succès<br>
	 * 					- 0 sinon	<br>
	 * <br>
	 * 
	 **********************************************************************************************************************/
	
	@Override
	public int updateTopic(int topicId, String title) {

		logger.log(Level.INFO,"Updating topic id: " + topicId + "; title: " + title);
		
		//Obtention d'un objet de type EntityManager créé lors dU premier appel et retourné
		EntityManager em =  entityManagerFactoryBean.getEntityManagerfactory().createEntityManager();
		
		int result = 0;
		
		EntityTransaction transaction =  null;
		
		try {			
			
			//obtention d'une transaction
			transaction =  em.getTransaction();
			//début de transaction
			transaction.begin();
			
			//Requête native SQL
			final String sql =  "update topic set title=:title, update_date=:updateDate where id=:id";
			
			//Création de la Query
			Query query = em.createNativeQuery(sql);
			
			//Initialisation des paramètres
			query.setParameter("title", title);
			query.setParameter("updateDate", ForumDateFormatter.fromLocalDateTimeToString(LocalDateTime.now()));
			query.setParameter("id", topicId);

			result = query.executeUpdate();
		//	logger.log(Level.INFO, "topic updated ============ > result : " + result);
			
			//persiste
			transaction.commit();
			
			
		} catch (Exception e) {
			//rollback et fermeture de la transaction
			transaction.rollback();
			logger.log(Level.SEVERE, "Une erreur est survenue lors de la tentative de mise à jour open/close.");
			//e.printStackTrace();
			
		}

		finally {
			//fermeture de l'EntityManager
			if(em != null) {
				em.close();
			}
		}
		
		return result;
	}
	
	//*********************************************************************************************************************
	//*********************************************************************************************************************
	
	
	
	/**********************************************************************************************************************
	 * Fonction qui permet de modifier les infos (title) d'un topic <br>
	 * <br>
	 * Obtention d'un objet de type EntityManager créé par EntityManagerFactory<br>
	 * <br>
	 * @param topic: Topic: le topic avec le titre modifé et l'identifiant du topic à mettre à jour<br>
	 * <br>
	 * @return: - un entier représentant le nombre de lignes affectées par la requête, <br>
	 * 			i.e.: 	- 1 si le changement a été effectué avec succès<br>
	 * 					- 0 sinon	<br>
	 * <br>
	 **********************************************************************************************************************/
	
	@Override
	public int updateTopic(Topic topic) {
		
		logger.log(Level.INFO,"Updating topic id: " + topic.getId());
		
		//Obtention d'un objet de type EntityManager créé lors dU premier appel et retourné
		EntityManager em =  entityManagerFactoryBean.getEntityManagerfactory().createEntityManager();
		
		int result = 0;
		
		EntityTransaction transaction =  null;
		
		try {			
			
			//obtention d'une transaction
			transaction =  em.getTransaction();
			//début de transaction
			transaction.begin();
			
			//Requête native SQL
			final String sql =  "update topic set title=:title, update_date=:updateDate, is_open=:isOpen where id=:id";
			
			//Création de la Query
			Query query = em.createNativeQuery(sql);
			
			//Initialisation des paramètres
			query.setParameter("title", topic.getTitle());
			query.setParameter("isOpen", topic.getIsOpen());
			query.setParameter("updateDate", ForumDateFormatter.fromLocalDateTimeToString(LocalDateTime.now()));
			query.setParameter("id", topic.getId());

			result = query.executeUpdate();			
			
			transaction.commit();
			
		} catch (Exception e) {
			transaction.rollback();
			topic.setMessage("Une erreur est survenue lors de la tentative de mise à jour dans la base de données.\n");
			logger.log(Level.SEVERE, "Une erreur est survenue lors de la tentative de mise à jour.");
			//e.printStackTrace();
		}

		finally {
			//fermeture de l'EntityManager
			if(em != null) {
				em.close();
			}
		}
		
		return result;
	}
	
	
	//*********************************************************************************************************************
	//*********************************************************************************************************************
	
	
	
	/**********************************************************************************************************************
	 * Fonction qui permet d'activer ou désactiver un topic en changeant la variable is_open(0 ou 1) de la table topic <br>
	 * <br>
	 * Obtention d'un objet de type EntityManager créé par EntityManagerFactory<br>
	 * <br>
	 * @param topicId (int): l'identifiant du topic<br>
	 * @param status: (byte): tsatut avec valeur 0 pour désactiver, ou 1 pour activer <br>
	 * <br>
	 * @return: - un entier représentant le nombre de lignes affectées par la requête, <br>
	 * 			i.e.: 	- 1 si le changement a été effectué avec succès<br>
	 * 					- 0 sinon	<br>
	 * <br>
	 **********************************************************************************************************************/
	
	@Override
	public int openCloseTopic(int topicId, byte status) {

		
		logger.log(Level.INFO,"Updating topic status: " + topicId + "; status: " + status);
		
		//Obtention d'un objet de type EntityManager créé lors dU premier appel et retourné
		EntityManager em =  entityManagerFactoryBean.getEntityManagerfactory().createEntityManager();
		
		int result = 0;
		
		EntityTransaction transaction =  null;
		
		try {			
			
			//obtention d'une transaction
			transaction =  em.getTransaction();
			//début de transaction
			transaction.begin();			
			
			//Requête native SQL
			final String sql =  "update topic set is_open=:isOpen, update_date=:updateDate where id=:id";
			
			//Création de la Query
			Query query = em.createNativeQuery(sql);
			
			//Initialisation des paramètres
			query.setParameter("isOpen", status);
			query.setParameter("updateDate", ForumDateFormatter.fromLocalDateTimeToString(LocalDateTime.now()));
			query.setParameter("id", topicId);

			result = query.executeUpdate();
			
			//persist
			transaction.commit();
			logger.log(Level.INFO, "topic updated ============ > result : " + result);
			
			
		} catch (Exception e) {
			//rollback et fermeture de la transaction
			transaction.rollback();
			logger.log(Level.SEVERE, "Une erreur est survenue lors de la tentative de mise à jour open/close.");
		}

		finally {
			//fermeture de l'EntityManager
			if(em != null) {
				em.close();
			}
		}
		
		return result;
	}	
	
	//*********************************************************************************************************************
	//*********************************************************************************************************************
	/**
	 * Méthode qui effece un topic de la bd<br>
	 * <br>
	 * Utilisée uniquement pour les tests<br>
	 * <br>
	 * @param topicId : l'identifiant du topic à effacer<br>
	 * @return une entier correspondant au nombre de lignes affectées par la requête<br>
	 * <br>
	 */
	@Override
	public int deleteTopicFromDB(int topicId) {
		
		logger.log(Level.INFO,"========================> deleteTopicFromDB topic id: " + topicId );
		
		//Obtention d'un objet de type EntityManager créé lors dU premier appel et retourné
		EntityManager em =  entityManagerFactoryBean.getEntityManagerfactory().createEntityManager();
		
		int result = 0;
		
		EntityTransaction transaction =  null;
		
		try {			
			
			//obtention d'une transaction
			transaction =  em.getTransaction();
			//début de transaction
			transaction.begin();			
			
			//Requête native SQL
			final String sql =  "delete from topic where id=:id";
			
			//Création de la Query
			Query query = em.createNativeQuery(sql);
			
			//Initialisation des paramètres
			query.setParameter("id", topicId);

			result = query.executeUpdate();
			
			//persist
			transaction.commit();
			logger.log(Level.INFO, "topic updated ============ > result : " + result);
			
			
		} catch (Exception e) {
			//rollback et fermeture de la transaction
			transaction.rollback();
			logger.log(Level.SEVERE, "Une erreur est survenue lors de la tentative de mise à jour open/close.");
			//e.printStackTrace();
		}

		finally {
			//fermeture de l'EntityManager
			if(em != null) {
				em.close();
			}
		}
		
		return result;

	}
	
}
