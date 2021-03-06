package org.olumpos.forum.dao;


import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.olumpos.forum.entity.Post;
import org.olumpos.forum.factory.EntityManagerFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Repository;


/********************************************************************************************************************************************
 * 
 * @author daristote<br>
 * <br>
 * Description:<br>
 * <br>
 * Classe qui permet d'accéder à la base de données pour créer, mettre à jour et récupérer les données des 'posts' (commentaire)<br>
 * <br>
 *
 ********************************************************************************************************************************************/


@Repository
public class PostDAOImpl implements PostDAO{
	
	//Logger pour afficher les infos en console
	Logger logger =  Logger.getLogger(PostDAOImpl.class.getName());
	
//	@Autowired
	EntityManagerFactoryBean entityManagerFactoryBean;

	
	@Autowired
	public PostDAOImpl() {
		this.entityManagerFactoryBean =  EntityManagerFactoryBean.getEntityManagerFactoryBean();
	}
	
	@PostConstruct
	public void init() {
		logger.log(Level.INFO, "int PostDAO.init(PostConstruct): entityManagerFactoryBean=============> " + entityManagerFactoryBean);

	}
	
	//*********************************************************************************************************************
	//*********************************************************************************************************************
	
	/**********************************************************************************************************************
	 * Méthode qui permet d'obtenir tous les tuples de la table post<br>
	 * <br>
	 * Obtention d'un objet de type EntityManager créé lors dU premier appel et retourné<br>
	 * <br>
	 * Utilisation de TypedQuery qui permet de vérifier le bon type à la compilation<br>
	 * <br>
	 * Utilisation de la @NamedQuery  "Post.findAll" définie dans la classe Post<br>
	 * <br>
	 * paramètre: aucun<br>
	 * <br>
	 * @return: une liste contenant des Objets de type Post qui ont été instanciés pour chaque tuple de la table 'post'<br>
	 * <br>
	 * 
	 **********************************************************************************************************************/
	@Override
	public List<Post> getAllPosts(){
		
		logger.log(Level.INFO, "in postDAO.getAllPosts()");
		
		//Obtention d'un objet de type EntityManager créé lors dU premier appel et retourné
		EntityManager em =  entityManagerFactoryBean.getEntityManagerfactory().createEntityManager();
		
		//Requête @Named de la classe Post
		TypedQuery<Post> query =  em.createNamedQuery("Post.findAll", Post.class);
		
		//obtention de la list de posts
		List<Post> posts = query.getResultList();
		
		//logger.log(Level.INFO, "in postDAO.getPosts annotated: posts: " + posts);
		
		//fermeture de l'entity manager
		em.close();
		
		return posts;
	}
	
	
	//*********************************************************************************************************************
	//*********************************************************************************************************************
	
	/**********************************************************************************************************************
	 * Méthode qui permet d'obtenir tous les posts actifs de la table post<br>
	 * <br>
	 * Obtention d'un objet de type EntityManager créé lors dU premier appel et retourné<br>
	 * <br>
	 * Utilisation de TypedQuery qui permet de vérifier le bon type à la compilation<br>
	 * <br>
	 * Utilisation de la @NamedQuery  "Post.findAllActive" définie dans la classe Post<br>
	 * <br>
	 * param: aucun<br>
	 * <br>
	 * @return: une liste contenant des Objets de type Post qui ont été instanciés pour chaque tuple de la table 'post'<br>
	 * <br>
	 * 
	 **********************************************************************************************************************/
	
	@Override
	public List<Post> getAllActivePosts(){
		
		logger.log(Level.INFO, "in postDAO.getAllActivePosts");
		
		//Obtention d'un objet de type EntityManager
		EntityManager em =  entityManagerFactoryBean.getEntityManagerfactory().createEntityManager();
		
		//Requête @Named de la classe Post
		TypedQuery<Post> query =  em.createNamedQuery("Post.findAllActive", Post.class);
		
		//Initialisation du paramètre
		query.setParameter("isActive", (byte)1);
		
		List<Post> posts = query.getResultList();
		
		//logger.log(Level.INFO, "in postDAO.getPosts annotated: posts: " + posts);
		
		em.close();
		
		return posts;
	}
	
	//*********************************************************************************************************************
	//*********************************************************************************************************************
	
	
	/**********************************************************************************************************************
	 * Méthode qui permet d'obtenir tous les posts de la table post qui sont associés au topic dont l'identifiant est spécifié en paramètre<br>
	 * <br>
	 * Obtention d'un objet de type EntityManager créé lors dU premier appel et retourné<br>
	 * <br>
	 * Utilisation de TypedQuery qui permet de vérifier le bon type à la compilation<br>
	 * <br>
	 * Utilisation de la @NamedQuery  "Post.findAllActive" définie dans la classe Post<br>
	 * <br>
	 * @param topicId: (int) l'identifiant du topic<br>
	 * <br>
	 * @return: une liste contenant des Objets de type Post provenant de la table 'post' et dont le topic_id = topicId<br>
	 * <br>
	 * 
	 **********************************************************************************************************************/
	
	@Override
	public List<Post> getAllPosts(int topicId){
		
		logger.log(Level.INFO, "in postDAO.getPosts annotated: topicId: " + topicId);
		
		//Obtention d'un objet de type EntityManager
		EntityManager em =  entityManagerFactoryBean.getEntityManagerfactory().createEntityManager();
		
		//Requête @Named de la classe Post
		TypedQuery<Post> query =  em.createNamedQuery("Post.findAllByTopic", Post.class);
		
		//Initialisation du paramètre
		query.setParameter("topicId", topicId);
		
		List<Post> posts = query.getResultList();
		
		//logger.log(Level.INFO, "in postDAO.getPosts annotated: posts: " + posts);
		
		em.close();
		
		return posts;
	}
	
	
	//*********************************************************************************************************************
	//*********************************************************************************************************************
	
	
	
	/**********************************************************************************************************************
	 * Méthode qui permet d'obtenir tous les posts sctifs de la table post qui sont associés au topic dont l'identifiant est spécifié en paramètre<br>
	 * <br>
	 * Obtention d'un objet de type EntityManager créé lors dU premier appel et retourné<br>
	 * <br>
	 * Utilisation de TypedQuery qui permet de vérifier le bon type à la compilation<br>
	 * <br>
	 * Utilisation de la @NamedQuery  "Post.findAllActive" définie dans la classe Post<br>
	 * <br>
	 * @param topicId: (int): l'identifiant du topic<br>
	 * <br>
	 * @return: une liste contenant des Objets de type Post provenant de la table 'post' et dont le topic_id = topicId et qui sont actifs<br>
	 * <br>
	 * 
	 **********************************************************************************************************************/
	@Override
	public List<Post> getAllActivePosts(int topicId){
		
		logger.log(Level.INFO, "in postDAO.getAllActivePosts ==> topicId: " + topicId);
		
		//Obtention d'un objet de type EntityManager
		EntityManager em =  entityManagerFactoryBean.getEntityManagerfactory().createEntityManager();
		
		//Création de la requête @Named de la classe Post
		TypedQuery<Post> query =  em.createNamedQuery("Post.findAllActiveByTopic", Post.class);

		//Initialisation des paramètres
		query.setParameter("topicId", topicId);
		query.setParameter("isActive", (byte)1); //post doit être actif
		query.setParameter("isOpen", (byte)1); //le topic doit être ouvert

		//Obtention de la liste de posts
		List<Post> posts = query.getResultList();
		
	//	logger.log(Level.INFO, "in postDAO.getAllActivePosts annotated: posts: " + posts);
		
		//fermeture de l'EntityManager
		em.close();
		
		return posts;
	}
		
	
	//*********************************************************************************************************************
	//*********************************************************************************************************************
	
	/**********************************************************************************************************************
	 * Méthode qui permet d'obtenir un post de la table 'post' dont l'identifiant est le paramètre 'postId'<br> 
	 * <br>
	 * Obtention d'un objet de type EntityManager<br>
	 * <br>
	 * Utilisation de la fonction EntityManager.'find()'  avec le paramètre id et le nom de la classe pour vérifier le type<br>
	 * <br>
	 * @param id: (int): l'identifiant du post<br>
	 * <br>
	 * @return: - un objet de type Topic, null si l'id n'est pas trouvé dans la bd<br>
	 * <br>
	 **********************************************************************************************************************/
	
	@Override
	public Post getPost(int id){
		
		logger.log(Level.INFO, "in postDAO.getPost -> post id: " + id);
		
		//Obtention d'un objet de type EntityManager
		EntityManager em =  entityManagerFactoryBean.getEntityManagerfactory().createEntityManager();		
		
		//Recherche le post avec l'identifiant postId
		Post post = em.find(Post.class, id);
		
		//logger.log(Level.INFO, "in postDAO.getPosts annotated: posts: " + post);
		
		//fermeture de l'EntityManager
		em.close();
		
		return post;
	}
	

	//*********************************************************************************************************************
	//*********************************************************************************************************************
	
	
	/**********************************************************************************************************************
	 * Méthode qui permet d'obtenir l'identifiant du dernier post actif inséré dans de la table 'post' <br>
	 * <br>
	 * Obtention d'un objet de type EntityManager<br>
	 * <br>
	 * Utilisation d'une requête SQL pour obtenir l'identifiant le plus grand (max)<br>
	 * <br>
	 * param: aucun<br>
	 * <br>
	 * @return: - un entier représentant l'identifiant (id) du post<br>
	 * <br>
	 * 
	 **********************************************************************************************************************/
	
	@Override
	public int getLastInsertedActivePostId() {
		
		logger.log(Level.INFO, "getting last post inserted id ");
		
		//Obtention d'un objet de type EntityManager
		EntityManager em =  entityManagerFactoryBean.getEntityManagerfactory().createEntityManager();
		
		
		int lastId = 0;
		try {
			
			//Requête native SQL
			final String sql = "select max(id) from post  where is_active=:isActive";
			
			//création de la QUery
			Query query = em.createNativeQuery(sql);
			
			//initialisation du paramètre
			query.setParameter("isActive", (byte)1);

			//exécution de la requête
			lastId = (int)query.getSingleResult();
		
		}
		catch(Exception e) {
			logger.log(Level.SEVERE, "Une erruer s'est produite lors de la tentative d'obtenir l'identifiant du dernier post ");
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
	 * Méthode qui permet d'obtenir l'identifiant du dernier post inséré dans de la table 'post' <br>
	 * <br>
	 * Obtention d'un objet de type EntityManager<br>
	 * <br>
	 * Utilisation d'une requête SQL pour obtenir l'identifiant le plus grand (max)<br>
	 * <br>
	 * param: aucun<br>
	 * <br>
	 * @return: - un entier représentant l'identifiant (id) du post<br>
	 * <br>
	 * 
	 **********************************************************************************************************************/
	
	@Override
	public int getLastInsertedPostId() {

		logger.log(Level.INFO, "getLastInsertedPostId");
		
		//Obtention d'un objet de type EntityManager
		EntityManager em =  entityManagerFactoryBean.getEntityManagerfactory().createEntityManager();
		
		int lastId = 0;
		try {
			
			//Requête native SQL
			final String sql = "select max(id) from post";
			
			//création de la Query
			Query query = em.createNativeQuery(sql);
			
			//exécution de la requête
			lastId = (int)query.getSingleResult();
		
		}
		catch(Exception e) {
			logger.log(Level.SEVERE, "Une erruer s'est produite lors de la tentative d'obtenir l'identifiant du dernier post ");
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
	 * Méthode qui permet d'obtenir d'insérer un nouveau post dans la bd <br>
	 * <br>
	 * Obtention d'un objet de type EntityManager créé lors dU premier appel et retourné<br>
	 * <br>
	 * @param post: Post: un objet de type Post initialisé avec les champs 'title', 'body' et topicId par l'utilisateur créant le post  <br>
	 * <br>
	 * @return: - un entier représentant le nombre de lignes affectées par la requête, i.e.: 1 si le topic  a été créé avec succès, 0 sinon	
	 * <br>
	 * 
	 **********************************************************************************************************************/
	@Override
	public int addPost(Post post) {

		logger.log(Level.INFO, "in addPost in topic " + post.getTopicId() + "for user: " + post.getUserId());
//		logger.log(Level.INFO, "in addPost: " + post.getUserId());
		
		//Obtention d'un objet de type EntityManager
		EntityManager em =  entityManagerFactoryBean.getEntityManagerfactory().createEntityManager();
		
		int result = 0;
		EntityTransaction transaction = null;
		
		try {
			
			//obtention d'une transaction
			transaction =  em.getTransaction();
			//début de transaction
			transaction.begin();
			
			//Requête native SQL
			final String sql =  "insert into post (title, body, user_id, topic_id) values (?, ?, ?, ?)";
						
			//création de la Query
			Query query = em.createNativeQuery(sql);
			
			//Initialisation des paramètres
			query.setParameter(1, post.getTitle());
			query.setParameter(2, post.getBody());
			query.setParameter(3, post.getUserId());
			query.setParameter(4, post.getTopicId());
			
			//exécution de la requête
			result = query.executeUpdate();
			
		//	logger.log(Level.INFO, "new post added ============ > result : " + result);
			
			//persiste
			transaction.commit();
			
		} catch (Exception e) {
			transaction.rollback(); //pas vraiment nécessaire
			post.setMessage("Une erreur est survenue lors de la tentative d'insertion du post");
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
	 * Méthode qui permet de modifier les infos (title, body) d'un post <br>
	 * <br>
	 * Obtention d'un objet de type EntityManager<br>
	 * <br>
	 * @param post (Post): un objet de type Post qui contient les informations modifiées<br>
	 * <br>
	 * @return: - un entier représentant le nombre de lignes affectées par la requête <br>
	 * i.e.: 1 si le changement a été effectué avec succès, 0 sinon	
	 * <br>
	 * 
	 **********************************************************************************************************************/
	@Override
	public int updatePost(Post post) {

		logger.log(Level.INFO,"Updating post id: " + post.getId());
		
		//Obtention d'un objet de type EntityManager
		EntityManager em =  entityManagerFactoryBean.getEntityManagerfactory().createEntityManager();		
		
		int result = 0;
		EntityTransaction transaction = null;
		
		try {
			
			//obtention d'une transaction
			transaction =  em.getTransaction();
			//début de transaction
			transaction.begin();
			
			//Requête native SQL
			final String sql =  "update post set title= :title, body= :body, topic_id= :topicId, update_date= :updateDate, is_active= :isActive where id= :id";
	
			//création de la Query
			Query query = em.createNativeQuery(sql);
			
			//Initialisation des paramètres
			query.setParameter("title", post.getTitle());
			query.setParameter("body", post.getBody());
			query.setParameter("updateDate", null);
			query.setParameter("isActive", post.getIsActive());
			query.setParameter("id", post.getId());
			query.setParameter("topicId", post.getTopicId());			
			
			//exécution de la requête
			result =  query.executeUpdate();
			
			//persiste
			transaction.commit();
	
		} catch (Exception e) {
			transaction.rollback();
			post.setMessage("Une erreur est survenue lors de la tentative de mise à jour du post");
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
	 * Méthode qui permet qui permet d'effacer (désactiver) un post <br>
	 * <br>
	 * Obtention d'un objet de type EntityManager<br>
	 * <br>
	 * @param id: (int) l'identifiant du post <br>
	 * <br>
	 * @return: - un entier représentant le nombre de lignes affectées par la requête,<br> 
	 * i.e.: 1 si le changement a été effectué avec succès, 0 sinon<br>	
	 * <br>
	 * 
	 **********************************************************************************************************************/
	
	@Override
	public int deletePost(int id) {

		logger.log(Level.INFO,"deleting post id: " + id);
		
		//Obtention d'un objet de type EntityManager
		EntityManager em =  entityManagerFactoryBean.getEntityManagerfactory().createEntityManager();
		
 		int result = 0;
		EntityTransaction transaction = null;
		
		try {
		
			//obtention d'une transaction
			transaction =  em.getTransaction();
			//début de transaction
			transaction.begin();
				
			//Requête native SQL
			final String sql =  "update post set is_active=:isActive, update_date=:updateDate where id=:id";
	
			//création de la Query
			Query query = em.createNativeQuery(sql);
			
			//Initialisation des paramètres
			query.setParameter("isActive", (byte)0);
			query.setParameter("updateDate", null);
			query.setParameter("id", id);
					
			//exécution de la requête
			result =  query.executeUpdate();
			
			//persiste
			transaction.commit();
			
		} catch (Exception e) {
			transaction.rollback();
			logger.log(Level.SEVERE, "Une erreur est survenue lors de la tentative de mise à jour du post");
			//e.printStackTrace();
		}
		
		finally {
			//fermeture de l'EntityManager
			if(em !=null){
				em.close();	
			}
		}
			
		return result;
	}
	
	//*********************************************************************************************************************
	//*********************************************************************************************************************
	
	
	/**********************************************************************************************************************
	 * Méthode qui permet d'activer/désactiver un post <br>
	 * <br>
	 * Obtention d'un objet de type EntityManager<br>
	 * <br>
	 * @param id: (int): l'identifiant du post<br>
	 * @param status: (byte): 0 ou 1 pour désactiver ou activer<br>
	 * 
	 * @return: - un entier représentant le nombre de lignes affectées par la requête, <br>
	 * 			i.e.: 	- 1 si le changement a été effectué avec succès<br>
	 * 					- 0 sinon	<br>
	 * <br>
	 **********************************************************************************************************************/
	
	@Override
	public int activateDeactivatePost (int id, byte status) {

		logger.log(Level.INFO,"activateDeactivatePost post id: " + id + "; status: " + status);
		
		//Obtention d'un objet de type EntityManager
		EntityManager em =  entityManagerFactoryBean.getEntityManagerfactory().createEntityManager();
				
		int result = 0;
		EntityTransaction transaction = null;
		
		try {
		
			//obtention d'une transaction
			transaction =  em.getTransaction();
			//début de transaction
			transaction.begin();
				
			//Requête native SQL
			final String sql =  "update post set is_active=:isActive, update_date=:updateDate where id=:id";
	
			//création de la Query
			Query query = em.createNativeQuery(sql);
			
			//Initialisation des paramètres
			query.setParameter("isActive", status);
			query.setParameter("updateDate", null);
			query.setParameter("id", id);
					
			//exécution de la requête
			result =  query.executeUpdate();
			
			//persiste
			transaction.commit();
			
		} catch (Exception e) {
			transaction.rollback();
			//post.setMessage("Une erreur est survenue lors de la tentative de mise à jour du post");
			e.printStackTrace();
		}
		
		finally {
			//fermeture de l'EntityManager
			if(em !=null){
				em.close();	
			}
		}
			
		return result;
	}
	
	
	//*************************************************************************************************************************************************
	//*************************************************************************************************************************************************
	
	/**********************************************************************************************************************
	 * Méthode qui permet qui permet d'effacer  un post <br>
	 * <br>
	 * Utilisée seulement popur les tests<br>
	 * <br>
	 * @param id: (int) l'identifiant du post <br>
	 * <br>
	 * @return: - un entier représentant le nombre de lignes affectées par la requête,<br> 
	 * i.e.: 1 si le changement a été effectué avec succès, 0 sinon<br>	
	 * <br>
	 * 
	 **********************************************************************************************************************/
	
	@Override
	public int deletePostFromDB(int id) {

		logger.log(Level.INFO,"deleting post id: " + id);
		
		//Obtention d'un objet de type EntityManager
		EntityManager em =  entityManagerFactoryBean.getEntityManagerfactory().createEntityManager();
		
 		int result = 0;
		EntityTransaction transaction = null;
		
		try {
		
			//obtention d'une transaction
			transaction =  em.getTransaction();
			//début de transaction
			transaction.begin();
				
			//Requête native SQL
			final String sql =  "delete from post where id=:id";
	
			//création de la Query
			Query query = em.createNativeQuery(sql);
			
			//Initialisation des paramètres
			query.setParameter("id", id);
					
			//exécution de la requête
			result =  query.executeUpdate();
			
			//persiste
			transaction.commit();
			
		} catch (Exception e) {
			transaction.rollback();
			logger.log(Level.SEVERE, "Une erreur est survenue lors de la tentative de mise à jour du post");
			//e.printStackTrace();
		}
		
		finally {
			//fermeture de l'EntityManager
			if(em !=null){
				em.close();	
			}
		}
			
		return result;
	}
	
	
}
