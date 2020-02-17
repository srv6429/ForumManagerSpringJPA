package org.olumpos.forum.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.persistence.*;

import org.olumpos.forum.util.ForumDateFormatter;

import java.util.List;


/**
 * The persistent class for the post database table.<br>
 * <br>
 * Créée par JPA tool de Eclipse<br>
 * Modifié par: Donald Michon<br>
 * <br>
 * Description:<br>
 * <br>
 * Classe de type Entity associée à la table 'user' du schema 'blog'<br> 
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
 * 
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
 * L'annotation @OneToMany devant List&lt;Post&gt; posts illustre que le topic est référencé par tous les posts associés avec le champ 'topicFK' de la classe Post<br>
 * C'est ainsi qu'on peut relier tous les posts au topic associé <br>
 * <br>
 */
@Entity(name = "User") //nom de l'entité, facultatif si le nom correspond au nom de la classe 
@Table(name="user") //nom de la table de la bd
@NamedQuery(name="User.findAll", query="select u from User u")//Requête pour obtenir tous les utilisateurs
@NamedQuery(name="User.findAllActive", query="select u from User u where u.isActive= :isActive")//Requête pour obtenir tous les utilisateurs actifs
@NamedQuery(name="User.findById", query="select u from User u where u.id= :userId") //Requête pour obtenir un ustilisateur par son id
@NamedQuery(name="User.findByUsername", query="select u from User u where u.username= :username or u.email= :username")//Requête pour obtenir un utilisateur par son username ou son email
//Requête pour obtenir un utilisateur par son username ou son email et son mot de passe
@NamedQuery(name="User.findByUsernameAndPassword", query="select u from User u where (u.username= :username or u.email= :username) and u.password= :password")
//Requête pour obtenir un utilisateur par son username ou son email et son mot de passe
@NamedQuery(name="User.findActiveByUsernameAndPassword", query="select u from User u where (u.username= :username or u.email= :username) and u.password= :password and u.isActive=:isActive") 
public class User implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id", unique=true, nullable=false)
	private Integer id;

	@Column(name="username", unique=true, nullable=false)
	private String username;

	@Column(name="password")
	private String password;

	@Column(name="email", unique=true, nullable=false)
	private String email;
	
	@Column(name="role")
	private String role;

	@Column(name="creation_date")
	private LocalDateTime creationDate;
	
	@Column(name="update_date")
	private LocalDateTime updateDate;


	@Column(name="is_active")
	private byte isActive;
	
	//bi-directional many-to-one association to Post
	@OneToMany(mappedBy="userFK", fetch=FetchType.LAZY)
	private List<Post> posts;
	
	//bi-directional many-to-one association to Topic
	@OneToMany(mappedBy="creatorFK", fetch=FetchType.LAZY)
	private List<Topic> topics;
	
	@Transient
	private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
	
	@Transient
	private String message;
	
	public User() {}
	
	public User(String username, String email, String password, String role, LocalDateTime creationDate, LocalDateTime updateDate, byte isActive) {
		this(null, username, email, password, role, creationDate, updateDate, isActive);
	}

	public User(Integer id, String username, String email, String password, String role, LocalDateTime creationDate, LocalDateTime updateDate, byte isActive) {
		this.id = id;
		this.username = username;
		this.email = email;
		this.password = password;
		this.role = role;
		this.creationDate = creationDate;
		this.updateDate = updateDate;
		this.isActive = isActive;
		
	}


	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return this.role;
	}

	public void setRole(String role) {
		this.role = role;
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
	
	public byte getIsActive() {
		return this.isActive;
	}

	public void setIsActive(byte isActive) {
		this.isActive = isActive;
	}

	public List<Post> getPosts() {
		return posts;
	}

	public void setPosts(List<Post> posts) {
		this.posts = posts;
	}
	
	public Post addPost(Post post) {
		getPosts().add(post);
		post.setUserFK(this);
		return post;
	}

	public Post removePost(Post post) {
		getPosts().remove(post);
		post.setUserFK(null);
		return post;
	}

	public List<Topic> getTopics() {
		return topics;
	}

	public void setTopics(List<Topic> topics) {
		this.topics = topics;
	}

	public Topic addTopic(Topic topic) {
		getTopics().add(topic);
		topic.setCreatorFK(this);
		return topic;
	}

	public Topic removeTopic(Topic topic) {
		getTopics().remove(topic);
		topic.setCreatorFK(null);
		return topic;
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
		
		sb.append("\nUser id: " + id + "\nusername: " + username + "\nemail: " + email);
		sb.append("\npassword: " + password + "\nrole: " + role +"\nCreation date: " + creationDate  + "\nupdate date: " + updateDate);
		sb.append("\nis active: "+isActive+"\nmessage: " + message);
		
		return sb.toString();
	}

}