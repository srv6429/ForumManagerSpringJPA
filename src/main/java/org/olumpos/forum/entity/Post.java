package org.olumpos.forum.entity;

import java.io.Serializable;
import javax.persistence.*;

import org.olumpos.forum.util.ForumDateFormatter;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


/**
 * The persistent class for the post database table.
 * 
 * Créée par JPA tool de Eclipse
 * Modifié par: Donald Michon
 * 
 * @Description:
 * 
 * Classe de type Entity associée à la table 'post' du schema 'blog' 
 * 
 * Le champs représentent ceux de la table
 * 
 * Les méthodes ('getter' et 'setter') sont utilisées pour initialiser les valeurs et les récupérer lorsque nécessaire
 *
 * Lorsque le nom des champs n'est pas le même que celui du champ correspondant de la table, 
 * l'annotation @Column est placée devant la déclaration avec le nom approprié de la table
 * 
 * Les champs annotés avec @Transient signifient qu'ils ne sont pas associés à un champ de la table
 * 
 * Le type des champs qui correspondent à des dates (i.e. creationDate et updateDate) ont été modifiés
 * 
 * Lors de la création automatique de la classe, ce type était 'sql.Timestamp', ce qui correspont 
 * au type approprié défini dans la table de la base de données.
 *  
 * Toufetois, il s'est avéré plus facile de gérer ces champs en modifiant le type en String.
 * 
 * Les requêtes définies ici en tant que @NamedQuery sont utiliés dans le fichier d'accès à la bd, (e.g. postDAO) et 
 * sont référencées directement par le nom associé en utilisant l'instruction: "query.createNamedQuery(name, class)" 
 * où  'name' est le nom donné pour référencer la requête,  et 'class' le nom de la classe qui conserve la requête nommée, 
 * en l'occurrence Post.class
 * 
 * La classe doit implémenter l'interface 'Serializable' pour permettre le transfert des données (valeurs des champs) d'un objet sous forme de texte sérialisé vers la bd
 * 
 * Chaque post de la base de données possèdent deux clés étrangères: 
 * 		1. une première, 'user_id', reliant le topic à son créateur par la clé primaire (id) de la table 'user'
 * 		2.une seconde, 'topic_id' reliant le post à un topic par la clé primaire de la table topic
 * 
 * Il s'agit des noms des tables de la base de données. Ces noms ici on été redéfinis par userId et topicId
 * 
 * Ainsi chaque post ne peut avoir qu'un seul créateur er ne peut appartenir qu'à un seul topic
 * 
 * Les annotations @ManytoOne permettent de relier un post à son créateur et à son topic, étant donnée qu'un utilisateur peut publier plusieurs posts 
 * et un topic peut contenir plusieurs posts
 * 
 * Ainsi lorsqu'on recherhe les posts on obtient du même coup l'utilisateur et le topic par leur identifiant et 
 * des objets de type User et Topic sont créés automatiquement alors que les champs sont initialisés avec ces objets
 * 
 * On peut ainsi obtenir toutes les données concernant les utilisateurs et les topic
 * Heureusement, pour des questions de sécurité les mots de passe sont encodés.
 * 
 * On pourrait également ignorer le champs 'password' ou l'annoter avec @Transient et utiliser une Native Query (requête SQL) au lieu d'une pseudo requête HQL
 * pour idenitifer un utilisateur lors de la connection avec le mot de passe.
 * 
 * 
 * 
 */
@Entity(name = "Post")  //nom de l'entité, facultatif si le nom correspond au nom de la classe 
@Table(name="post") //nom de la table de la bd
@NamedQuery(name="Post.findAll", query="select p from Post p") //requête pour obtenir tous les posts
@NamedQuery(name="Post.findAllActive", query="select p from Post p where p.isActive= :isActive")//requête pour obtenir tous les posts actifs 
@NamedQuery(name="Post.findAllByTopic", query="select p from Post p where p.topicFK.id= :topicId") //requête pour obtenir tous les posts d'un topic
//@NamedQuery(name="Post.findAllActiveByTopic", query="select p from Post p where p.topicFK.id= :topicId and p.isActive= :isActive") //requête pour obtenir tous les posts actif d'un topic

//requête pour obtenir tous les posts actif d'un topic
@NamedQuery(name="Post.findAllActiveByTopic", query="select p from Post p join fetch p.userFK where p.topicFK.id= :topicId  and p.isActive= :isActive and p.topicFK.isOpen=:isOpen") 

public class Post implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id", unique=true, nullable=false)
	private Integer id;

	@Column(name="title")
	private String title;
	
	@Lob
	private String body;

	@Column(name="creation_date")
	private LocalDateTime creationDate;

	@Column(name="update_date")
	private LocalDateTime updateDate;
	
	@Column(name="is_active")
	private byte isActive;

	//bi-directional many-to-one association to Customer
	@ManyToOne
	@JoinColumn(name="user_id", referencedColumnName="id")
	private User userFK;
	
	@Transient
	private Integer userId;
	
	@Transient
	private String username;
	
	@Transient
	private Integer topicId;

	//bi-directional many-to-one association to Customer
	@ManyToOne
	@JoinColumn(name="topic_id", referencedColumnName="id")
	private Topic topicFK;

	@Transient
	private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
	
	@Transient
	private String message;
	
	public Post() {
	}

	public String getBody() {
		return this.body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public byte getIsActive() {
		return this.isActive;
	}

	public void setIsActive(byte isActive) {
		this.isActive = isActive;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getTopicId() {
		if(this.topicFK != null) {
			return this.topicFK.getId();
		}
		return this.topicId;
	}

	public void setTopicId(Integer topicId) {
		this.topicId = topicId;
	}

	public Integer getUserId() {
		if(this.userFK != null) {
			return this.userFK.getId();
		}
		return this.userId;
	}

	public void setUserId(Integer userId) {
		this.userId =  userId;
	}

	public String getUsername() {
		if(this.userFK != null) {
			return this.userFK.getUsername();
		}
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
/*
	public String getCreationDate() {
		return ForumDateFormatter.fromLongStringToLocalDateTimeShortString(this.creationDate);
	}

	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}
	
	public String getUpdateDate() {
		return ForumDateFormatter.fromLongStringToLocalDateTimeShortString(this.updateDate);
	}

	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}
	*/
	
	public void setCreationDate(LocalDateTime creationDate) {
		this.creationDate = creationDate;
	}

	public String getCreationDate() {
		return formatter.format(creationDate);
	}
	
	public LocalDateTime getNonFormattedCreationDate() {
		return creationDate;
	}
	
	public void setUpdateDate(LocalDateTime updateDate) {
		this.updateDate = updateDate;
	}

	public String getUpdateDate() {
		return formatter.format(updateDate);
	}

	public LocalDateTime getNonFormattedUpdateDate() {
		return updateDate;
	}
	
	
	public User getUserFK() {
		return userFK;
	}

	public void setUserFK(User userFK) {
		this.userFK = userFK;
	}

	public Topic getTopicFK() {
		return topicFK;
	}

	public void setTopicFK(Topic topicFK) {
		this.topicFK = topicFK;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
	
	public boolean messageExists() {
		return message != null && message.trim().length() > 0;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("\nPost id: " + id + "\ntitle: " + title);
		sb.append("\nComment: " + body);
		sb.append("\nCreator id: "+ userId + "\nCreator name: " + getUsername());
		sb.append("\nCreation date: " + creationDate  + "\nupdate date: " + updateDate);
		sb.append("\nisActive "+ isActive + "\nmessage: " + message);
		
		return sb.toString();
	}
	

}