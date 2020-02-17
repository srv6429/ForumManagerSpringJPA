package org.olumpos.forum.entity;

import java.io.Serializable;
import javax.persistence.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * The persistent class for the post database table.<br>
 * <br>
 * Créée par JPA tool de Eclipse<br>
 * Modifié par: Donald Michon<br>
 * <br>
 * Description:<br>
 * <br>
 * Classe de type Entity associée à la table 'topic' du schema 'blog'<br> 
 * <br>
 * Le champs représentent ceux de la table<br>
 * <br>
 * Les méthodes ('getter' et 'setter') sont utilisées pour initialiser les valeurs et les récupérer lorsque nécessaire<br>
 *<br>
 * Lorsque le nom des champs n'est pas le même que celui du champ correspondant de la table,<br> 
 * l'annotation @Column est placée devant la déclaration avec le nom approprié de la table<br>
 * <br>
 * Les champs annotés avec @Transient signifient qu'ils ne sont pas associés à un champ de la table<br>
 * <br>
 * Le type des champs qui correspondent à des dates (i.e. creationDate et updateDate) ont été modifiés<br>
 * <br>
 * Lors de la création automatique de la classe, ce type était 'sql.Timestamp', ce qui correspont<br> 
 * au type approprié défini dans la table de la base de données.<br>
 *  <br>
 * Toufetois, il s'est avéré plus facile de gérer ces champs en modifiant le type en String.<br>
 * <br>
 * Les requêtes définies ici en tant que @NamedQuery sont utiliés dans le fichier d'accès à la bd, (e.g. topicDAO) et<br> 
 * sont référencées directement par le nom associé en utilisant l'instruction: "query.createNamedQuery(name, class)" <br>
 * où  'name' est le nom donné pour référencer la requête,  et 'class' le nom de la classe qui conserve la requête nommée,<br> 
 * en l'occurrence Post.class<br>
 * <br>
 * La classe doit implémenter l'interface 'Serializable' pour permettre le transfert des données (valeurs des champs) d'un objet sous forme de texte sérialisé vers la bd<br>
 * <br>
 * Chaque topic de la base de données possèdent une clé étrangère:<br> 
 * 		1. une première, 'user_id', reliant le topic à son créateur par la clé primaire (id) de la table 'user'<br>
 * <br>
 * Il s'agit du nom défini dans la table de la base de données qui a été redéfini ici par userId<br>
 * <br>
 * Ainsi chaque topic ne peut avoir qu'un seul créateur<br>
 * <br>
 * L'annotation @ManytoOne permet de relier un topic à son créateur étant donnée qu'un utilisateur peut créer plusieurs topics<br> 
 * <br>
 * Ainsi lorsqu'on recherhe les topic on obtient du même coup l'utilisateur qui L'a créé par identifiant et<br> 
 * un objet de type User est créé automatiquement alors que les champs appropirés sont initialisés avec cet objet<br>
 * <br>
 * On peut ainsi obtenir toutes les données concernant les utilisateurs concernés<br>
 * Heureusement, pour des questions de sécurité les mots de passe sont encodés.<br>
 * <br>
 * On pourrait également ignorer le champs 'password' ou l'annoter avec @Transient et utiliser une Native Query (requête SQL) au lieu d'une pseudo requête HQL<br>
 * pour idenitifer un utilisateur lors de la connection avec le mot de passe.<br>
 * <br>
 * L'annotation @OneToMany devant List&lt;Post&gt; posts illustre que le topic est référencé par tous les posts associés avec le champ 'topicFK' de la classe Ppst<br>
 * C'est ainsi qu'o peut relier tous les posts au topic associé <br>
 * <br>
 */
@Entity(name = "Topic") //nom de l'entité, facultatif si le nom correspond au nom de la classe
@Table(name="topic") //nom de la table associée dans la bd
@NamedQuery(name="Topic.findAll", query="select t from Topic t") //Requête pour obtenir tous les topics
@NamedQuery(name="Topic.findAllOpen", query="select t from Topic t where t.isOpen= :isOpen")//Requête pour obtenir tous les topics ouverts 
public class Topic implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id", unique=true, nullable=false)
	private Integer id;

	@Column(name="title")
	private String title;

	@Column(name="creation_date")
	private LocalDateTime creationDate;
	
	@Column(name="update_date")
	private LocalDateTime updateDate;

	@Column(name="is_open")
	private byte isOpen;

	//bi-directional many-to-one association to Topic
	@OneToMany(mappedBy="topicFK", fetch=FetchType.LAZY)
	private List<Post> posts;
	
	//bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name="creator_id", referencedColumnName="id")
	private User creatorFK;
	
	@Transient
	private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
	
//	@Column(name="creator_id")
	@Transient
	private Integer creatorId;
	
	@Transient
	private String creatorName;
	
	@Transient
	private String message;
	
	public Topic() {}
	
	public Topic(String title, LocalDateTime creationDate, LocalDateTime updateDate, byte isOpened, User creatorFK) {
		this(null, title, creationDate, updateDate, isOpened, creatorFK);
	}
	
	public Topic(Integer id, String title, LocalDateTime creationDate, LocalDateTime updateDate, byte isOpen, User creatorFK) {

		this.id = id;
		this.title = title;
		this.creationDate =  creationDate;
		this.updateDate =  updateDate;
		this.isOpen = isOpen;
		this.creatorFK = creatorFK;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	/*
	public String getCreationDate() {
		return ForumDateFormatter.fromLongStringToLocalDateTimeShortString(this.creationDate);
//		return this.creationDate;
	}

	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}
	
	public String getUpdateDate() {
		return ForumDateFormatter.fromLongStringToLocalDateTimeShortString(this.updateDate);
		//return this.updateDate;
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
	
	
	public byte getIsOpen() {
		return isOpen;
	}

	public void setIsOpen(byte isOpen) {
		this.isOpen = isOpen;
	}
	
	public User getCreatorFK() {
		return creatorFK;
	}

	public void setCreatorFK(User creatorFK) {
		this.creatorFK = creatorFK;
	}

	public String getCreatorName() {
		if(creatorFK!= null) {
			return creatorFK.getUsername();
		}
		return this.creatorName;
	}

	public void setCreatorName(String creatorName) {
		this.creatorName = creatorFK.getUsername();
	}

	public Integer getCreatorId() {
		if(creatorFK != null) {
			return creatorFK.getId();
		}
		return creatorId;
	}

	public void setCreatorId(Integer creatorId) {
		this.creatorId = creatorId;
	}

	public List<Post> getPosts() {
		return posts;
	}

	public void setPosts(List<Post> posts) {
		this.posts = posts;
	}
	
	public Post addPost(Post post) {
		getPosts().add(post);
		post.setTopicFK(this);
		return post;
	}

	public Post removePost(Post post) {
		getPosts().remove(post);
		post.setTopicFK(null);
		return post;
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
		
		sb.append("\nTopic id: " + id + "\ntitle: " + title);
		sb.append("\nCreator id: "+ creatorId + "\nCreator name: " + creatorName + "\nCreation date: " + creationDate  + "\nupdate date: " + updateDate);
		sb.append("\nIsOpen: "+ isOpen + "\nmessage: " + message);
		
		return sb.toString();
	}








}