package org.olumpos.forum.factory;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.springframework.stereotype.Component;

/**
 * <br>
 * @author daristote<br>
 *<br>
 * Classe qui permet d'initialiser l'objet de type EntityManagerFactory utilisé pour générer des<br> 
 * Objets type EntityManager pour accéder à la base de données<br>
 * <br>
 * La classe EntityManagerFactoryBean étant annotée avec l'annontation @Component, l'objet est initialisé<br> 
 * lors du déploiement de l'application par le serveur web  <br>
 * <br>
 * Par contre, pour les tests avec JUnit,  l'injection des composantes Spring (@Component, @Repository)<br>
 * ne sont pas injectés. On doit donc initialiser à partir des constructeurs des classes UserDAOImpl, TopicDAOImpl et PostDAOImpl<br>
 * <br>
 * Ces classes étant toutefois marquées de l'annotation @Repository (@Component) les constructeurs sont appelés automatiquement<br>
 * lors du déploiement de l'application et l'objet EntityManegerFactoryBean est alors initialisé s'il ne l'est pas déjà<br>
 * <br>
 * Pour éviter d'intiialiser plus d'un objet de type EntityManagerFactoryBean. on utilise le principe du Singleton (Design Pattern)<br>
 * pour initialiser l'objet qu'une seule fois.<br>
 * <br>
 * Utilisation du principe du Design Pattern Abstract Factory pour initialiser de manière synchrone l'objet de type EntityManagerFactory<br>
 * <br>
 * Normalement on aurait simplement initialiser EntityManagerFactory dans la fonction init() lors dU déploiement<br>
 * de l'application puisqu'lle est annotée de @PostConstruct et donc appelée après la construction de l'Objet EntityManagerFactoryBean<br>
 * <br>
 * il n'aurait alors pas été nécessaire d'utiliser la synchronisation 'synchronized' pour s'assurer que la création<br>
 * de l'objet de fait de manière unique.<br>
 * <br>
 * Une fonction simple qui retourne l'objet créer l'ors du d.ploiement aurait été suffisante<br>
 * <br>
 *
 */

@Component
public class EntityManagerFactoryBean {
	
	Logger logger =  Logger.getLogger(EntityManagerFactory.class.getName());
	
	private EntityManagerFactory entityManagerFactory ; 

	//Pous tester avec JUnit
	private static EntityManagerFactoryBean entityManagerFactoryBean;
	
	private EntityManagerFactoryBean() {
		//logger.log(Level.INFO, "in EntityFactoryBean.constructor(): entityManagerfactoryBean ===============> " + entityManagerFactoryBean); 
		
	}
	/**
	 * param: none
	 * return: void
	 * 
	 * Fonction appelé après la construction de l'objet de type EntityManagerFactoryBean injecté lors du déploiement de l"application 
	 * par le serveur web
	 * 
	 * 
	 */
	
	@PostConstruct
	public void init() {
		//injection de l'entityentityManagerFactory suivant la construction de entityManagerFactoryBean
		//entityManagerFactory = Persistence.createEntityManagerFactory("BlogManagementSpringMVCJPA");
		logger.log(Level.INFO, "in EntityFactoryBean.init(@PostConstructo): entityManagerfactoryBean ===============> " + entityManagerFactoryBean);
		logger.log(Level.INFO, "in EntityFactoryBean.init(@PostConstructo): entityManagerfactory ===============> " + entityManagerFactory); 	
	}
	
	//*********************************************************************************************************************
	//*********************************************************************************************************************
	/**
	 * Fonction qui initialise une fois le champ entityManagerFactoryBean et qui le retourne au besoin
	 * 
	 * La double vérification est nécessaire pour éviter qu'un appel soit pris en attente entre la première vérifcation 
	 * et le verrouillage (synchronized) pendant qu'un appel précédent soit sur le point d'initialiser l'objet
	 * L'objet peut ne pas avoir encore été instancié après la première vérification mais pourrait l'avoir été pendant après
	 * que le verrou soit levé
	 * 
	 * param: aucun
	 * @return; un objet de type EntityManagerFactoryBean
	 */
	//for testing: injection not working when running with JUnit
	public static EntityManagerFactoryBean getEntityManagerFactoryBean() {
		
		
		if(entityManagerFactoryBean == null) {
			synchronized (EntityManagerFactoryBean.class) {
				if(entityManagerFactoryBean == null) {
					entityManagerFactoryBean =  new EntityManagerFactoryBean();
				}
			}
		}
		return entityManagerFactoryBean;
	}
	
	//*********************************************************************************************************************
	//*********************************************************************************************************************
	
	/**
	 * 'EntityManagerFactoryBean.getEntityManagerfactory()' appelée au besoin lors d'une demande dA'ccès à la base de données
	 * 
	 * Principe de synchronisation avec double vérification
	 * 
	 * param: aucun
	 * @return : un objet de type EntityManagerFactory
	 * 
	 */
	
	//for testing: injection not working when running with JUnit
	public EntityManagerFactory getEntityManagerfactory() {
		
		if(entityManagerFactory == null) {
			synchronized (EntityManagerFactory.class) {
				if(entityManagerFactory == null) {
					entityManagerFactory =  Persistence.createEntityManagerFactory("ForumManagerPersistenceUnit");
					logger.log(Level.INFO, "in EntityFactoryBean.getEntityManagerfactory(): entityManagerfactory ===============> " + entityManagerFactory);
				}
			}
		}

		return entityManagerFactory;
	}
	
	
	
}
