package org.olumpos.forum.dao;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.olumpos.forum.dao.PostDAO;
import org.olumpos.forum.dao.PostDAOImpl;
import org.olumpos.forum.dao.TopicDAO;
import org.olumpos.forum.dao.TopicDAOImpl;
import org.olumpos.forum.entity.Post;
import org.olumpos.forum.entity.Topic;
/**
 * 
 * @author daristote
 * 
 * Tests pour les fonction d'accès à la base de données de TopicDAO utilisant JPA: 
 *
 */

//@RunWith(JUnit4.class)
public class TopicDAOTest {

	Logger logger =  Logger.getLogger(TopicDAOTest.class.getName());
	

	private TopicDAO topicDAO;
	private PostDAO postDAO;
	
	@Before
	public void init() {
		topicDAO =  new TopicDAOImpl();
		postDAO = new PostDAOImpl();
		
		logger.log(Level.INFO, "TopicDAOTest.init(): topicDAO: " + topicDAO);
		logger.log(Level.INFO, "TopicDAOTest.init(): postDAO:" + postDAO);
	}
	
	//*********************************************************************************************************************
	//*********************************************************************************************************************
	
	@Test
	public void testGet() {
		
		logger.log(Level.INFO, "TopicDAOTest.testGet()");
		
		List<Topic> allTopics =  topicDAO.getAllTopics();
		
		assertNotNull(allTopics);
		
		int nbAllTopics =  allTopics.size();
		
		List<Topic> openTopics =  topicDAO.getAllOpenTopics();

		assertNotNull(openTopics);
		
		int nbOpenTopics =  openTopics.size();
		
		assertTrue(nbOpenTopics <= nbAllTopics);
		
		
		int lastInsertedTopicId =  topicDAO.getLastInsertedTopicId();
		
	//	logger.log(Level.INFO, "last inserted topic id: " + lastInsertedTopicId);
		
		Topic lastTopic = topicDAO.getTopic(lastInsertedTopicId);
		
		assertNotNull(lastTopic);
		
		assertEquals(new Integer(lastInsertedTopicId), lastTopic.getId());
		
	}
	
	//*********************************************************************************************************************
	//*********************************************************************************************************************
	
	@Test
	public void testInsert() {

		logger.log(Level.INFO, "TopicDAOTest.testInsert()");
		
		Topic topic =  new Topic();
		topic.setTitle("A new topic to add");
		topic.setCreatorId(3);
		
		Post post =  new Post();
		post.setTitle("A new post to add");
		post.setBody("A new comment");
		post.setUserId(3);
		
		
		List<Topic> topics =  topicDAO.getAllTopics();
				
		assertNotNull(topics);
		
		int topicsSize =  topics.size();
		
		
	//	logger.log(Level.INFO, "topics size: " + topicsSize);
		
		
		List<Post> posts =  postDAO.getAllPosts();
		
		assertNotNull(posts);
		
		int postsSize =  posts.size();
		
		
	//	logger.log(Level.INFO, "posts size: " + postsSize);

		
		
		int result = topicDAO.addTopic(topic, post);

		assertEquals(1, result);

		int lastTopicId =  topicDAO.getLastInsertedTopicId();
		
		Topic lastTopicInserted =  topicDAO.getTopic(lastTopicId);
	
		
		assertEquals(lastTopicInserted.getId(), new Integer(lastTopicId));
		
		
	
		int lastPostId =  postDAO.getLastInsertedPostId();
		
		Post lastPostInserted =  postDAO.getPost(lastPostId);
		
		lastPostInserted.setTopicId(lastTopicId);
		
		result =  postDAO.updatePost(lastPostInserted);
		
		assertEquals(1, result);
		
		assertEquals(new Integer(lastPostId), lastPostInserted.getId());
		assertEquals(new Integer(lastTopicId), lastPostInserted.getTopicId());
		
		//Assert.assertEquals(lastInserted.getCreationDate(), lastInserted.getUpdateDate());
		
		topics =  topicDAO.getAllTopics();
		
	//	logger.log(Level.INFO, "topics new size: " + topics.size());
		
		assertEquals(topicsSize+1, topics.size());
		
		posts =  postDAO.getAllPosts();
		
	//	logger.log(Level.INFO, "posts new size: " + posts.size());
		
		assertEquals(postsSize+1, posts.size());
		
	}
	
	//*********************************************************************************************************************
	//*********************************************************************************************************************
	
	@Test
	public void testUpdate() {

		logger.log(Level.INFO, "TopicDAOTest.testUpdate()");
		
		Topic topic =  new Topic();
		topic.setTitle("New topic to update");
		topic.setCreatorId(1);

		int result = topicDAO.addTopic(topic);
		
		assertEquals(1, result);
		int lastTopicId =  topicDAO.getLastInsertedTopicId();

		assertNotEquals(0, lastTopicId);
		
		//get the just added topic 
		topic =  topicDAO.getTopic(lastTopicId);
		
		assertNotNull(topic);
		
		String titleBeforeUpdate =  topic.getTitle();
	//	String updateDate =  topic.getUpdateDate();
		
		String updatedTitle = titleBeforeUpdate + " updated";
		
		topic.setTitle(updatedTitle);
		
		//Take a pause for 3 seconds to be sure that the update date will be different from the previous one
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//update topic
		result = topicDAO.updateTopic(topic);
		
		assertEquals(1, result);
		
		Topic updatedTopic =  topicDAO.getTopic(lastTopicId);
		
		assertNotNull(updatedTopic);
		
		assertEquals(updatedTopic.getTitle(), updatedTitle);

		//verify that
		//assertNotEquals(updatedTopic.getUpdateDate(), updateDate);

		
	}
	
	//*********************************************************************************************************************
	//*********************************************************************************************************************
	
	@Test
	public void testDelete() {
		
		logger.log(Level.INFO, "TopicDAOTest.tesDelete()");
				
		Topic topic =  new Topic();
		topic.setTitle("New topic to delete");
		topic.setCreatorId(1);

		int result = topicDAO.addTopic(topic);
		
		assertEquals(1, result);
		
		List<Topic> openedTopics =  topicDAO.getAllOpenTopics();
		assertNotNull(openedTopics);
		int openedTopicsSize =  openedTopics.size();
		
		List<Topic> allTopics =  topicDAO.getAllTopics();
		assertNotNull(allTopics);
		int allTopicsSize =  allTopics.size();
		
		
		int lastTopicId =  topicDAO.getLastInsertedTopicId();
		
		Topic lastInserted =  topicDAO.getTopic(lastTopicId);
		
		assertEquals(lastInserted.getId(), new Integer(lastTopicId));
		
		result = topicDAO.openCloseTopic(lastTopicId, (byte)0);

		assertEquals(1, result);
		
		openedTopics =  topicDAO.getAllOpenTopics();
		allTopics =  topicDAO.getAllTopics();
		
		assertNotEquals(openedTopicsSize, openedTopics.size());
		assertEquals(allTopicsSize, allTopics.size());

	}
	
	
	//*********************************************************************************************************************
	//*********************************************************************************************************************
	
	@Test
	public void testErrors() {

		
		logger.log(Level.INFO, "TopicDAOTest.testErrors()");
		
		//1. get topic with a non valid id
		Topic topic =  topicDAO.getTopic(0);
		
		assertNull(topic);
		
		//2.insert with a non valid user id
		topic =  new Topic();
		topic.setTitle("New topic");
		topic.setCreatorId(0);

		int result = topicDAO.addTopic(topic);
		
		assertEquals(result, 0);
		
		//logger.log(Level.SEVERE, "error: " + topic.getMessage());
		
		//3.update topic with non valid id
		
		topic = new Topic();
		topic.setId(0);
		topic.setTitle("New topic");
		result =  topicDAO.updateTopic(topic);
		
		assertEquals(result, 0);
		
		//logger.log(Level.SEVERE, "error: " + topic.getMessage());
		
		//4.update topic with null title
		
		topic = new Topic();
		topic.setId(3);
		topic.setTitle(null);
		result =  topicDAO.updateTopic(topic);
		
		assertEquals(result, 0);
		
		//logger.log(Level.SEVERE, "error: " + topic.getMessage());
		

	}
	
	//*********************************************************************************************************************
	//*********************************************************************************************************************
	@Test
	public void testInsertRollback() {

		
		//Test insertion d'un topic avec une erreur ddans le post : rollback
		//Ni le topic ni le post ne deravient être insérés
		
		logger.log(Level.INFO, "TopicDAOTest.testInsertRoolback()");
		
		Topic topic =  new Topic();
		topic.setTitle("A new topic to add");
		topic.setCreatorId(3);
		
		Post post =  new Post();
		post.setTitle("A new post to add");
		post.setBody(null);//erreur body ne peut être nul
		post.setUserId(3);
		
		
		List<Topic> topics =  topicDAO.getAllTopics();
				
		assertNotNull(topics);
		
		int topicsSize =  topics.size();
		
		
	//	logger.log(Level.INFO, "topics size: " + topicsSize);
		
		
		List<Post> posts =  postDAO.getAllPosts();
		
		assertNotNull(posts);
		
		int postsSize =  posts.size();
		
		
	//	logger.log(Level.INFO, "posts size: " + postsSize);

		int lastTopicId =  topicDAO.getLastInsertedTopicId();
		int lastPostId =  postDAO.getLastInsertedPostId();
		
		
		int result = topicDAO.addTopic(topic, post);//should not update topic and post

		assertEquals(0, result);
		
		//les derniers topic et post demeurent les mêmes 
		assertEquals(lastTopicId, topicDAO.getLastInsertedTopicId());
		assertEquals(lastPostId, postDAO.getLastInsertedPostId());
		
		
		topics =  topicDAO.getAllTopics();		
		//la taille des topics est toujours la même
		assertEquals(topicsSize, topics.size());
		logger.log(Level.INFO, "topics new size: " + topics.size());
		
		posts =  postDAO.getAllPosts();
		//la taille des posts est toujours la même
		assertEquals(postsSize, posts.size());
		
	//	logger.log(Level.INFO, "posts new size: " + posts.size());
		
		//rollback effectué avec succès
	//	logger.log(Level.INFO, "rollback effectué avec succès!");
	}
	
	
	
}
