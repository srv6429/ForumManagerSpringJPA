-- MySQL Script generated by MySQL Workbench
-- mer 12 2020 12:27:46 EDT
-- Model: New Model    Version: 1.0
-- MySQL Workbench Forward Engineering
-- MySQL Distrib 5.7.29, for Linux (x86_64)


-- SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
-- SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
-- SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema forum_spring
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `forum_spring` ;

CREATE SCHEMA IF NOT EXISTS `forum_spring` DEFAULT CHARACTER SET utf8 ;

-- CREATE USER 'springuser'@'localhost' IDENTIFIED BY 'spring123';
GRANT ALL PRIVILEGES ON `forum_spring`.* TO 'springuser'@'localhost' WITH GRANT OPTION;

USE `forum_spring` ;

-- ----------------------------------------------------------------------------
-- Structure de la table `user`
--


DROP TABLE IF EXISTS `forum_spring`.`user` ;

CREATE TABLE IF NOT EXISTS `forum_spring`.`user` (
	`id` int(11) NOT NULL AUTO_INCREMENT,
	`username` VARCHAR(255) NOT NULL,
	`password` VARCHAR(255) NOT NULL,
	`email` VARCHAR(255) NOT NULL,
	`creation_date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	`update_date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	`role` ENUM('A', 'U') NOT NULL DEFAULT 'U',
	`is_active` TINYINT(1) NOT NULL DEFAULT 1,
	PRIMARY KEY (`id`),
	UNIQUE KEY `username_UNIQUE` (`username` ASC),
	UNIQUE KEY `email_UNIQUE` (`email` ASC)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- CREATE UNIQUE INDEX `username_UNIQUE` ON `forum_spring`.`user` (`username` ASC);

-- CREATE UNIQUE INDEX `email_UNIQUE` ON `forum_spring`.`user` (`email` ASC);

--
-- Insertion de données dans la table `user`
--

LOCK TABLES `forum_spring`.`user` WRITE;
/*!40000 ALTER TABLE `forum_spring`.`user` DISABLE KEYS */;
INSERT INTO `forum_spring`.`user` VALUES 

	(1,'admin', md5('admin123'),'admin@olumpos.org','2019-09-08 03:45:39','2019-09-08 03:45:39','A', 1),
    (2,'zeus', md5('zeus123'),'zeus@olumpos.org','2019-09-08 03:45:39','2019-09-08 03:45:39','A',1),
    (3,'hera', md5('hera123'),'hera@olumpos.org','2019-09-08 03:45:39','2019-09-08 03:45:39','U',1),
    (4,'ares',md5('ares123'),'ares@olumpos.org','2019-09-08 03:45:39','2019-09-08 03:45:39','U',1),
    (5,'apollon',md5('apollon123'),'apollon@olumpos.org','2019-09-08 03:45:39','2019-09-08 03:45:39','U',1),
	(6,'hades',md5('hades123'),'hades@olumpos.org','2019-09-08 03:45:39','2020-02-05 23:08:27','U',1),
    (7,'hestia',md5('hestia123'),'hestia@olumpos.org','2019-09-08 03:45:39','2019-09-08 03:45:39','U',1),
    (8,'athena',md5('athena123'),'athena@olumpos.org','2019-09-08 03:45:39','2019-09-08 03:45:39','U',1),
    (9,'poseidon',md5('poseidon123'),'poseidon@olumpos.org','2019-09-08 03:45:39','2019-09-08 03:45:39','U',1),
    (10,'aphrodite',md5('aphrodite123'),'aphrodite@olumpos.org','2019-09-08 03:45:39','2019-09-08 03:45:39','U',1),
    (11,'artemis',md5('artemis123'),'artemis@olumpos.org','2019-09-08 03:45:39','2019-09-08 03:45:39','U',1),
    (12,'hephaïstos',md5('hephaïstos123'),'hephaïstos@olumpos.org','2019-09-08 03:45:39','2019-09-08 03:45:39','U',1),
    (13,'pegase',md5('pegase123'),'pegase@olumpos.org','2019-09-08 03:45:39','2019-09-08 03:45:39','U',1),
    (14,'icare',md5('icare123'),'icare@olumpos.org','2019-09-08 03:45:39','2019-09-08 18:57:57','U',1),
    (15,'dummy',md5('dummy123'),'dummy@olumpos.org','2019-09-08 03:45:39','2019-09-08 03:45:39','U',1)
;

/*!40000 ALTER TABLE `forum_spring`.`user` ENABLE KEYS */;
UNLOCK TABLES;



-- ----------------------------------------------------------------------------

-- Structure de la table `topic`
--

DROP TABLE IF EXISTS `forum_spring`.`topic` ;

CREATE TABLE IF NOT EXISTS `forum_spring`.`topic` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(255) NOT NULL,
  `creation_date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `creator_id` INT(11) DEFAULT NULL,
  `is_open` TINYINT(1) NOT NULL DEFAULT 1,
	PRIMARY KEY (`id`),
	KEY `fk_topic_user1_idx` (`creator_id`),
	CONSTRAINT `fk_topic_user1`
		FOREIGN KEY (`creator_id`)
		REFERENCES `forum_spring`.`user` (`id`)
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- CREATE INDEX `fk_topic_user1_idx` ON `forum_spring`.`topic` (`creator_id` ASC);


--
-- Insertion de données dans la table `topic`
--


LOCK TABLES `forum_spring`.`topic` WRITE;
/*!40000 ALTER TABLE `forum_spring`.`topic` DISABLE KEYS */;
INSERT INTO `topic` 
	(`id`,`title`,`creation_date`,`update_date`,`creator_id`,`is_open`)
	VALUES 
	(1,'La météo','2019-07-08 03:45:40','2020-02-10 19:34:17',3,1),
    (2,'Les élections','2019-09-17 14:22:56','2019-09-17 14:22:56',11,1),
    (3,'Le libre échange USA/Canada/Mexique','2019-10-05 21:19:28','2019-10-05 21:19:28',2,1),
    (4,'Le retour des Nordiques','2019-10-07 12:46:23','2019-10-07 12:46:23',8,1),
    (5,'Les difficultés des media écrits','2019-10-24 16:51:12','2019-10-24 16:51:12',4,1),
    (6,'La déroute des Républicains','2019-11-23 15:08:27','2019-11-23 15:08:27',7,1),
	(7,'Les déboires du Canadien de Montréal','2019-11-25 12:22:16','2020-01-08 20:15:40',10,1),
    (8,'La balance du pouvoir','2020-02-03 23:35:52','2020-02-04 11:31:42',7,1),
    (9,'Les élections états-uniennes','2020-02-08 04:22:52','2020-02-08 04:22:52',5,1)
;


/*!40000 ALTER TABLE `forum_spring`.`topic` ENABLE KEYS */;
UNLOCK TABLES;



-- ----------------------------------------------------------------------------
-- Structure de la table `post`
--


DROP TABLE IF EXISTS `forum_spring`.`post` ;

CREATE TABLE IF NOT EXISTS `forum_spring`.`post` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(255) NOT NULL,
  `body` TEXT NOT NULL,
  `creation_date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `user_id` INT(11) NULL DEFAULT NULL,
  `topic_id` INT(11) NULL DEFAULT NULL,
  `is_active` TINYINT(1) NOT NULL DEFAULT 1,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_post_user`
    FOREIGN KEY (`user_id`)
    REFERENCES `forum_spring`.`user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_post_topic1`
    FOREIGN KEY (`topic_id`)
    REFERENCES `forum_spring`.`topic` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- CREATE INDEX `fk_post_user_idx` ON `forum_spring`.`post` (`user_id` ASC);

-- CREATE INDEX `fk_post_topic1_idx` ON `forum_spring`.`post` (`topic_id` ASC);

--
-- Insertion de données dans la table `post`
--

LOCK TABLES `forum_spring`.`post` WRITE;
/*!40000 ALTER TABLE `forum_spring`.`post` DISABLE KEYS */;
/*
INSERT INTO `post` (`id`,`comment`,`creation_date`,`update_date`,`user_id`,`topic_id`,`is_active`)
	VALUES 
	(1,'L\'Europe agonise sous la canicule','2019-07-08 03:45:40','2019-07-08 03:45:40',3,1,1),
    (2,'Paris brûle-t-il?. 40 degrés à Paris! Du jamais vu!','2019-07-09 12:33:49','2019-07-09 12:33:49',8,1,1),
    (3,'Encore des élections cet automne','2019-09-17 14:22:56','2019-09-17 14:22:56',11,2,1),
    (4,'Ceci est un premier commentaire','2019-10-05 21:19:28','2019-10-05 21:19:28',2,3,1),
	(5,'Le retour des Nordiques ne se fera pas dans les prochaines années','2019-10-07 12:46:23','2019-10-07 12:46:23',8,4,1),
    (6,'Les Nordiques ont besoin d\'un nouvel amphithéâtre. La guimauve n\'a pas coûté assez cher','2019-10-08 14:26:31','2019-10-08 14:26:31',11,4,1),
	(7,'Les médias électroniques et les réseaux sociaux semblent avoir tué les media écrits','2019-10-24 16:51:12','2019-10-24 16:51:12',4,5,1),
    (8,'Même les journaux électroniques peinent à survivre sans aide financière des gouvernements','2019-10-26 05:12:21','2019-10-26 05:12:21',8,5,1),
    (9,'Vaut mieux être patient. Le retour des Nordiques ne se fera pas avant celui des Expos ... i.e. jamais','2019-10-29 21:37:52','2019-10-29 21:37:52',9,4,1),
	(10,'Un retour possible... Ne soyons pas si pessimiste','2019-11-04 22:12:03','2019-11-04 22:12:03',2,4,1),
    (11,'Trump divise! Mais les Républicains semblent se ranger derrière leur président ou n\'osent tout simplement pas le défier','2019-11-23 15:08:27','2019-11-23 15:08:27',7,6,1),
	(12,'La coupe de retour à Montréal ? Ce n\'est pas pour demain.','2019-11-25 12:22:16','2019-11-25 12:22:16',10,7,1),
    (13,'La haine envers les démocrates semble l\'emporter par dessus tout. C\'est pour cette raison que les républicains appuient sans réserve leur président','2019-12-05 17:21:04','2019-12-05 17:21:04',12,6,1),
    (14,'Le problème ce sont les filets. Il faut élargir le filet adverse, et rétrécir celui du Canadien','2019-12-13 14:18:45','2019-12-13 14:18:45',9,7,1),
    (15,'Ce sont les blessures qui ont coulé le Canadien cette année. Il faut échanger tous les soigneurs.','2020-01-08 20:15:40','2020-01-08 20:15:40',10,7,1),
	(16,'L\'Australie a connu l\'un des pires \'hivers\' avec la chaleur et les incendies','2020-02-04 05:52:06','2020-02-04 05:52:06',5,1,1),
	(17,'Une démocratie en santé permet à plus d\'un partis de bénéficier de la balance du pouvoir.','2020-02-07 23:35:52','2020-02-07 23:35:52',3,8,1),
    (18,'Bien sûr qu\'un gouvernement minoritaire procure aux partis d\'opposition plus de pouvoir face au gouvernement, mais celui-ci risque d\'être paralysé et marqué par l\'indécision.','2020-02-08 11:31:42','2020-02-08 11:31:42',6,8,1),
    (19,'Les primaires pour les démocrates semblent bien mal s\'amorcer. Le cafouillage dans l\'Iowa ne leur fait pas bonne presse ','2020-02-08 14:22:52','2020-02-08 14:22:52',5,9,1),
    (20,' ... et maintenant les innondations  ','2020-02-10 19:34:17','2020-02-10 19:34:17',9,1,1)

;
*/
INSERT INTO `post` (`id`,`title`, `body`,`creation_date`,`update_date`,`user_id`,`topic_id`,`is_active`)
	VALUES 
    
	(1,'Quelle chaleur!','L\'Europe agonise sous la canicule','2019-07-08 03:45:40','2019-07-08 03:45:40',3,1,1),
    (2,'Paris brûle-t-il?.', '40 degrés à Paris! Du jamais vu!','2019-07-09 12:33:49','2019-07-09 12:33:49',8,1,1),
    (3,'Les élections','Encore des élections cet automne','2019-09-17 14:22:56','2019-09-17 14:22:56',11,2,1),
    (4, 'Titre de commentaire', 'Ceci est un premier commentaire','2019-10-05 21:19:28','2019-10-05 21:19:28',2,3,1),
	(5, 'Vous rêvez!', 'Le retour des Nordiques ne se fera pas dans les prochaines années','2019-10-07 12:46:23','2019-10-07 12:46:23',8,4,1),
    (6, 'Une solution','Les Nordiques ont besoin d\'un nouvel amphithéâtre. La guimauve n\'a pas coûté assez cher','2019-10-08 14:26:31','2019-10-08 14:26:31',11,4,1),
	(7, 'Le manque de revenus publicitaires tue les media écrits','Les médias électroniques et les réseaux sociaux semblent avoir tué les media écrits','2019-10-24 16:51:12','2019-10-24 16:51:12',4,5,1),
    (8, 'Ils ont besoin d\'aide','Même les journaux électroniques peinent à survivre sans aide financière des gouvernements','2019-10-26 05:12:21','2019-10-26 05:12:21',8,5,1),
    (9, 'Je n\'y crois pas','Vaut mieux être patient. Le retour des Nordiques ne se fera pas avant celui des Expos ... i.e. jamais','2019-10-29 21:37:52','2019-10-29 21:37:52',9,4,1),
	(10, 'Moi je suis optimiste','Un retour possible... Ne soyons pas si pessimiste','2019-11-04 22:12:03','2019-11-04 22:12:03',2,4,1),
    (11, 'C\'est désolant!','Trump divise! Mais les Républicains semblent se ranger derrière leur président ou n\'osent tout simplement pas le défier','2019-11-23 15:08:27','2019-11-23 15:08:27',7,6,1),
	(12, 'Le Canadien ne fera pas les séries','La coupe de retour à Montréal ? Ce n\'est pas pour demain.','2019-11-25 12:22:16','2019-11-25 12:22:16',10,7,1),
    (13, 'La polarisation de la poslitique aux USA','La haine envers les démocrates semble l\'emporter par dessus tout. C\'est pour cette raison que les républicains appuient sans réserve leur président','2019-12-05 17:21:04','2019-12-05 17:21:04',12,6,1),
    (14, 'Il n\'y a pas 36 solutions', 'Le problème ce sont les filets. Il faut élargir le filet adverse, et rétrécir celui du Canadien','2019-12-13 14:18:45','2019-12-13 14:18:45',9,7,1),
    (15, 'Le Canadien est éclopé','Ce sont les blessures qui ont coulé le Canadien cette année. Il faut échanger tous les soigneurs.','2020-01-08 20:15:40','2020-01-08 20:15:40',10,7,1),
	(16, 'It\'s burning down under!','L\'Australie a connu l\'un des pires \'hivers\' avec la chaleur et les incendies','2020-02-04 05:52:06','2020-02-04 05:52:06',5,1,1),
	(17, 'Vive les gouvernements minoritaires','Une démocratie en santé permet à plus d\'un partis de bénéficier de la balance du pouvoir.','2020-02-07 23:35:52','2020-02-07 23:35:52',3,8,1),
    (18, 'Pas sûr!','Bien sûr qu\'un gouvernement minoritaire procure aux partis d\'opposition plus de pouvoir face au gouvernement, mais celui-ci risque d\'être paralysé et marqué par l\'indécision.','2020-02-08 11:31:42','2020-02-08 11:31:42',6,8,1),
    (19, 'La course à la présidence chez les démocrates','Les primaires pour les démocrates semblent bien mal s\'amorcer. Le cafouillage dans l\'Iowa ne leur fait pas bonne presse ','2020-02-08 14:22:52','2020-02-08 14:22:52',5,9,1),
    (20, 'Les Australiens ne sont pas au bout de leurs peines',' ... et maintenant les innondations  ','2020-02-10 19:34:17','2020-02-10 19:34:17',9,1,1)

/*
    (1, 'Quelle chaleur!', 'L\'Europe agonise sous la chaleur ... ', '2019-07-08 03:45:40','2019-07-08 03:45:40', 3, 1, 1),
    (2, 'Les élections', 'Encore des élections cet automne', '2019-07-09 12:33:49','2019-07-09 12:33:49', 11, 21, 1),
    (3, 'La coupe de retour à Montréal', 'Il faut échanger tous les soigneurs.', '2019-10-07 12:46:23','2019-10-07 12:46:23', 10, 3, 1),
	(4, 'Un Premier commentaire', 'Ceci est un premier commentaire', '2019-10-05 21:19:28','2019-10-05 21:19:28', 2, 4, 1),
	(5,'Les Nordiques ont besoin d\'un nouvel amphithéâtre. La guimauve n\'a pas coûté assez cher','2019-10-08 14:26:31','2019-10-08 14:26:31',11,4,1),
	(6, 'Le problème ce sont les filets', 'Il faut élargir le filet adverse, et rétrécir celui du Canadien', 4, 3),
	(7, 'Les Nordiques ont besoin d\'un nouvel amphithéâtre', 'Il faut un nouvel arena. La guimauve n\'a pas coûté assez cher', 8, 5),
    (8, 'Un Premier post', 'Ceci est un premier commentaire', 4, 6),
    (9, 'Paris brûle-t-il?', '40 degrés à Paris! Du jamais vu!', 8, 1),
    (10, 'Vaut mieux être patient', 'Le retour des Nordiques ne se fera pas avant celui des Expos ... i.e. jamais', 9, 5),
	(11, 'Un retour possible', 'Ne soyons pas si pessimiste', 2, 5),
	(12, 'Trump divise', 'Les Républicains semblent se ranger derrière leur président ou n\'osent tout simplement pas le défier', 7, 7)
*/

;

/*!40000 ALTER TABLE `forum_spring`.`post` ENABLE KEYS */;
UNLOCK TABLES;


-- ---------------------------------------------------------------------------
-- ----------------------------------------------------------------------------
-- Structure de la table `persistent_logins`
--


DROP TABLE IF EXISTS `forum_spring`.`persistent_logins`;
CREATE TABLE `forum_spring`.`persistent_logins` (
  `username` varchar(64) NOT NULL,
  `series` varchar(64) NOT NULL,
  `token` varchar(64) NOT NULL,
  `last_used` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`series`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


--
-- Insertion de données dans la table `persistent_logins`
--

LOCK TABLES `forum_spring`.`persistent_logins` WRITE;
/*!40000 ALTER TABLE `forum_spring`.`persistent_logins` DISABLE KEYS */;
/*!40000 ALTER TABLE `forum_spring`.`persistent_logins` ENABLE KEYS */;
UNLOCK TABLES;

-- SET SQL_MODE=@OLD_SQL_MODE;
-- SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
-- SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
