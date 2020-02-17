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
	public void getAllTopicsTest() {
		
		logger.log(Level.INFO, "TopicDAOTest.testGet()");
		
		List<Topic> allTopics =  topicDAO.getAllTopics();
		
		assertNotNull(allTopics);
		
		int nbAllTopics =  allTopics.size();
		
		List<Topic> openTopics =  topicDAO.getAllOpenTopics();

		assertNotNull(openTopics);
		
		int nbOpenTopics =  openTopics.size();
		
		assertTrue(nbOpenTopics <= nbAllTopics);
		

		
	}
	
	//*************************************************************************************************************************************************
	//*************************************************************************************************************************************************
	
	@Test
	public void getOneTopicTest() {
		
		//test get one topic
		
		int lastInsertedTopicId =  topicDAO.getLastInsertedTopicId();
		
		logger.log(Level.INFO, "last inserted topic id: " + lastInsertedTopicId);
		
		Topic lastTopic = topicDAO.getTopic(lastInsertedTopicId);
		
		assertNotNull(lastTopic);
		
		assertEquals(Integer.valueOf(lastInsertedTopicId), lastTopic.getId());

	}
	
	//*********************************************************************************************************************
	//*********************************************************************************************************************
	
	@Test
	public void insertTopicTest() {

		logger.log(Level.INFO, "TopicDAOTest.insertTopicTest()");
		
		//add new Topic and post
		Topic topic =  new Topic();
		topic.setTitle("A new topic to add");
		topic.setCreatorId(1);
		
		Post post =  new Post();
		post.setTitle("A new post to add");
		post.setBody("A new comment");
		

		int lastTopicIdBefore =  topicDAO.getLastInsertedTopicId();
		int lastPostIdBefore =  postDAO.getLastInsertedPostId();
		
		assertTrue(lastTopicIdBefore > 0);
		assertTrue(lastPostIdBefore > 0);
		
		
		int insertedTopicResult = topicDAO.addTopic(topic, post);

		assertTrue(insertedTopicResult > 0);

		int lastTopicId =  topicDAO.getLastInsertedTopicId();
		
		assertTrue(lastTopicId > lastTopicIdBefore);
		
		Topic lastTopicInserted =  topicDAO.getTopic(lastTopicId);
	
		assertNotNull(lastTopicInserted);
		
		
		assertEquals(Integer.valueOf(lastTopicId), lastTopicInserted.getId());
		assertEquals(topic.getTitle(), lastTopicInserted.getTitle());
		
	
		int lastPostId =  postDAO.getLastInsertedPostId();
		assertTrue(lastPostId > lastPostIdBefore);
		
		Post lastPostInserted =  postDAO.getPost(lastPostId);
		
		assertNotNull(lastPostInserted);
		
		assertEquals(post.getTitle(), lastPostInserted.getTitle());
		assertEquals(post.getBody(), lastPostInserted.getBody());

		
		//delete post and topic
		int deletedPostResult =  postDAO.deletePostFromDB(lastPostId);
		
		assertTrue(deletedPostResult > 0);
		
		Post deletedPost = postDAO.getPost(lastPostId); 
		assertNull(deletedPost);
		
		//delete topic
		int deletedTopicRessult = topicDAO.deleteTopicFromDB(lastTopicId);
		assertTrue(deletedTopicRessult > 0);
		
		Topic deletedTopic =  topicDAO.getTopic(lastTopicId);
		assertNull(deletedTopic);
		

	}
	
	//*********************************************************************************************************************
	//*********************************************************************************************************************
	
	@Test
	public void updateTopicTest() {

		logger.log(Level.INFO, "TopicDAOTest.testUpdate()");
		
		//add a new topic first
		Topic topic =  new Topic();
		topic.setTitle("New topic to add");
		topic.setCreatorId(1);
		
		int topicAddedResult = topicDAO.addTopic(topic);
		
		assertEquals(1, topicAddedResult);
		int lastTopicId =  topicDAO.getLastInsertedTopicId();

		assertNotEquals(0, lastTopicId);
		
		//get the just added topic 
		Topic addedTopic =  topicDAO.getTopic(lastTopicId);
		
		assertNotNull(addedTopic);
		
		String titleBeforeUpdate =  topic.getTitle();
	//	String updateDate =  topic.getUpdateDate();
		
		String updatedTitle = titleBeforeUpdate + " updated";
		
		addedTopic.setTitle(updatedTitle);
		
		//update topic
		int updatedTopicResult = topicDAO.updateTopic(addedTopic);
		
		assertEquals(1, updatedTopicResult);
		
		Topic updatedTopic =  topicDAO.getTopic(lastTopicId);
		
		assertNotNull(updatedTopic);
		
		assertEquals(updatedTopic.getTitle(), updatedTitle);
		
		//delete topic from db
		int deletedTopicRessult = topicDAO.deleteTopicFromDB(lastTopicId);
		assertTrue(deletedTopicRessult > 0);
		
		Topic deletedTopic =  topicDAO.getTopic(lastTopicId);
		assertNull(deletedTopic);

		
	}
	
	//*********************************************************************************************************************
	//*********************************************************************************************************************
	
	@Test
	public void toggleTopicTest() {
		
		logger.log(Level.INFO, "TopicDAOTest.tesDelete()");
				
		//add a new topic first
		Topic topic =  new Topic();
		topic.setTitle("New topic to add");
		topic.setCreatorId(1);
		topic.setIsOpen((byte) 1);
		
		int topicAddedResult = topicDAO.addTopic(topic);
		
		assertEquals(1, topicAddedResult);
		int lastTopicId =  topicDAO.getLastInsertedTopicId();

		assertNotEquals(0, lastTopicId);
		
		//get the just added topic 
		Topic addedTopic =  topicDAO.getTopic(lastTopicId);
		
		assertNotNull(addedTopic);
		assertTrue(addedTopic.getIsOpen() == 1);
	
		
		int closeTopicResult = topicDAO.openCloseTopic(lastTopicId, (byte)0);

		assertEquals(1, closeTopicResult);
		
		Topic closedTopic =  topicDAO.getTopic(lastTopicId);
		
		assertNotNull(closedTopic);
		assertTrue(closedTopic.getIsOpen() == 0);
		
		
		//reopen topic
		int openTopicResult = topicDAO.openCloseTopic(lastTopicId, (byte)1);

		assertEquals(1, closeTopicResult);
		
		Topic openTopic =  topicDAO.getTopic(lastTopicId);
		
		assertNotNull(openTopic);
		assertTrue(openTopic.getIsOpen() == 1);;

		
		//delete topic from db
		int deletedTopicRessult = topicDAO.deleteTopicFromDB(lastTopicId);
		assertTrue(deletedTopicRessult > 0);
		
		Topic deletedTopic =  topicDAO.getTopic(lastTopicId);
		assertNull(deletedTopic);
	}
	
	
	//*********************************************************************************************************************
	//*********************************************************************************************************************
	
	@Test
	public void getErrorsTest() {

		
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
		
	//	logger.log(Level.INFO, "posts size: " + postsSize);

		int lastTopicId =  topicDAO.getLastInsertedTopicId();
		int lastPostId =  postDAO.getLastInsertedPostId();
		
		
		int result = topicDAO.addTopic(topic, post);//should not update topic and post

		assertEquals(0, result);
		
		//les derniers topic et post demeurent les mêmes 
		assertEquals(lastTopicId, topicDAO.getLastInsertedTopicId());
		assertEquals(lastPostId, postDAO.getLastInsertedPostId());
		
	}
	
	
	
}
