Nom du projet: ForumManagerSpringJPA
Créateur: Donald Michon
Date: 13 fécrier 2020

Description:

Le projet a été créé comme un projet maven utilisant le framework Spring MVC version 4.3.24 et Java Persistence API (JPA) 2.2

Il s'agit d'un projet web basé sur java qui peut être déployé sur un serveur d'application java tel que Tomcat, ou wildfly

Il a été testé avec succès sur un serveur local Tomcat (v. 9.0.21) et Wildfly (v. 17.0.1) sur un systême Linux (x86_64) Debian 9.0


Vous pouvez consulter le fichier 'forum.pdf' dans le répertoire doc/ qui contient une description détaillée du projet. 

Base  de données:

Le projet fonctionne avec une base de données relationnelle et a été testé avec le serveur MySQL 5.7.27 sur un systême Linux (x86_64) Debian 9.9


Des fichiers '.sql' pour générer la base de données ainsi que quelques entrées dans le tables ont été fornis avec le projet.
On peut les dans le répertoire doc/sql du projet

Pour créer la base de données et insérer les entrées dans es atbles on doit avoir les privilèges nécessaires

Création de la base "blog" et les tables on exécute le fichier db_creation_script.sql:

On doit démarrer le client mysql du répertoire où se trouve les fichiers de création

Pour cérer la base de donnée et les tables:

mysql > source db_creation_script.sql;


Pour remplir les tables avec les données d"exmple

mysql > source db_insert_script.sql;



Compilation:

Prérequis: 

- Java JVM  version 8
- maven 
- connection Internet pour les paquets de dépendance 


Pour compiler le projet en ligne de commande à partir du répertoire de base contenant le fichier pom.xml

> mvn install

ou

> mvn package


Les tests dans le répertoire src/test seront d'abord tous effectués avec JUnit et certianes classes de test de Spring 
( ex.: org.springframework.test.web.servlet.request.MockMvcRequestBuilders et org.springframework.test.web.servlet.result.MockMvcResultMatchers)

Si les tests sont réussis avec succès le projet sera compilé et une archive de type '.war' sera créé dans le dossier /target du projet


Déploiement:

Pour déployer le projet, copier le fichier "<nom_du_projet>.war", généré dans target lors de la compilation, 
dans le répertoire de déploiement du serveur d'application web-java.

Le projet sera alors automatiquement déployé si le serveur est démarré, sinon il le sera  lors du démarrage

Pour tomcat:
copier l'archive war dans: <tomcat-server-directory>/webapps

Pour Wildfly:
copier l'archive war dans: <wildfly-server-directory>/standalone/deployments


Fonctionnement:

Pour exécuter le projet on utilise l'url du context de base
Par exemple sur un serveur local:
http://localhost:8080/blog

On est alors sur la page de de connexion

On peut utilisé le non et le mot de passe d'un des utilisateurs de la base de données
Par exemple
Utilisteur régulier:
usename: dummy
password: dummy123

Pour un adminstrateur:
usename: admin
password: admin123

------------------------------------------------------------------------------------------------------
------------------------------------------------------------------------------------------------------
------------------------------------------------------------------------------------------------------

Problèmes connus:

1)

Une erreur dont nous n'avons toujours pas trouvé la source et qui semble liée au framework Spring 
est le non chargement des fichiers provenant du répertoire /resources, i.e. les scripts et le fichier de style css.

Nous avons placé dans le répertoire "src/main//webapp/resources/scripts" le script JQuery (jquery-3.4.1.min.js)
et dans le répertoire "src/main/webapp/resources/css" le fichier de style "style.css"

Lorsqu'on atteint la page de "login" la première fois, il semble que ces fichiers ne soient pas chargés.

Par contre une fois que nous sommes connectés, ces fichiers sont chargés par l'application.

On peut par la suite se déconnecter et on sera rediriger vers la page login. Or curieusement, à ce moment ils demeurent chargés.
 
Pour le style le problème est mineur. 

Par contre, pour javascript, nous utilisons JQuery dans presque toutes les pages
pour notamment récupérer les valeurs des champs d'un formulaire et envoyer les requêtes REST par la fonction AJAX

Si JQUery n'est pas chargé, l'appliaction n'est pas fonctionnelle.

Heureusement, JQuery n'est pas utilisé sur la page login.jsp.

À remarquer que le script JQuery ne se charge pas même si on utlise l'url de googleapis.com:

<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>


Nous allons tenter de résoudre ce problème. Sinon il faudra abandonné JQuery et réécrire les scipts en javascipt de base


------------------------------------

2) Le second problème qui risque de survenir est heureusement moins grave.

Il arrive parfois qu'on tente de redéployer l'application et qu'on nous signale une erreur à l'effet que 
la @NamedQuery "Topic.FindALL" est dupliqué. 

Nous ne sommes pas certain, mais nous croyons qu'il s'agit peut_être d'un problème avec la cache de Hibernate qui semble conserver 
les définitions de NamedQuery en cache.

En général on règle le problème et retirant le war du répertoire de déploiement (undeploy), puis en effectuant un nettoyage.

Avec tomcat on peut effacer sans danger le répertoire "<tomcat-directory>/work" et effacer toute référence au projet dans le répertoire "<tomcat-directory>/webapps".


Avec Wildfly, on efface toute référence du projet dans le répertoire de déploiement: <wildfly-directory>/standalone/deployments


Il est aussi recommandé de fermer toutes les fenêtres du navigateur pour fermer la session.

Par la suite, on redémarre le serveur, redéploie l'application et on ouvre une nouvelle session de navigation. 

Le problème n'est alors plus présent.













