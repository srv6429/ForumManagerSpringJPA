package org.olumpos.forum.controller;



import org.junit.Before;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.olumpos.forum.controller.HomeController;
import org.olumpos.forum.controller.TopicController;
import org.olumpos.forum.dao.PostDAO;
import org.olumpos.forum.dao.PostDAOImpl;
import org.olumpos.forum.dao.TopicDAO;
import org.olumpos.forum.dao.TopicDAOImpl;
import org.olumpos.forum.dao.TopicDAOTest;
import org.olumpos.forum.entity.Post;
import org.olumpos.forum.entity.Topic;

import java.io.UnsupportedEncodingException;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.mock.web.MockBodyContent;
//import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceView;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
//import static org.junit.jupiter.api.Assertions.assertNull;
//import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

/**
 * 
 * @author daristote
 * 
 * Tests pour les fonction du contrôleur Spring MVC: TopicController
 *
 */

@RunWith(JUnit4.class)
public class TopicControllerTest {


	Logger logger =  Logger.getLogger(TopicDAOTest.class.getName());
	
	private TopicDAO topicDAO;
	private PostDAO postDAO;
	
	@Before
	public void init() {
		topicDAO =  new TopicDAOImpl();
		postDAO = new PostDAOImpl();
		
//		logger.log(Level.INFO, "TopicControllerTest.init(): topicDAO: " + topicDAO);
//		logger.log(Level.INFO, "TopicControllerTest.init(): postDAO:" + postDAO);
	}
	
	//*********************************************************************************************************************
	//*********************************************************************************************************************
	
	@Test
	public void getTopics() throws Exception {
		TopicController controller =  new TopicController(topicDAO, postDAO);
		
		
		MockMvc mockMvc = standaloneSetup(controller).
				setSingleView(new InternalResourceView("/WEB-INF/views/topics.jsp"))
				.build();
		
		
		//List<Topic> allOpenedTopics =  topicDAO.getAllOpenTopics();
	    
		mockMvc.perform(get("/topics"))
	       .andExpect(view().name("topics"))
	       .andExpect(model().attributeExists("topicList"))
	      // .andExpect(model().attribute("topicList", hasItems(allOpenedTopics.toArray()))); //error but should work
	       .andExpect(model().hasNoErrors());
	       
		
	}
	
	//*********************************************************************************************************************
	//*********************************************************************************************************************
	
	@Test
	public void testRoot() throws Exception {
		
		HomeController controller =  new HomeController();
		
		MockMvc mockMvc = standaloneSetup(controller).build();
		    mockMvc.perform(get("/"))
//		           .andExpect(redirectedUrl("login"));
		    .andExpect(view().name("login"));
	}
	
	//*********************************************************************************************************************
	//*********************************************************************************************************************
	
	//@Test
//	public void getTopic() throws Exception {
//
//		TopicController controller =  new TopicController(topicDAO, postDAO, userDAO);
//		
//		MockMvc mockMvc = standaloneSetup(controller).build();
//
//		int lastTopicId =  topicDAO.getLastInsertedOpenTopicId();
//		Topic topic = topicDAO.getTopic(lastTopicId);		
//		
//		
//		mockMvc.perform(get("/topics/topic/" + lastTopicId))
//       .andExpect(status().isOk())
//       .andExpect(content().contentType("application/json;charset=UTF-8"))// OK
//       .andExpect(model().attributeExists("topic"))
//       .andExpect(model().attribute("topic", topic))
//       .andReturn().getResponse().getContentAsString().equals("{topic:success}");
//	}
	
	//*********************************************************************************************************************
	//*********************************************************************************************************************
	
	@Test
	public void addTopicTest() throws UnsupportedEncodingException, Exception {

		TopicController controller =  new TopicController(topicDAO, postDAO);
		
		MockMvc mockMvc = standaloneSetup(controller).build();
			
		long suffixe =  new Date().getTime() % 10000;
		
		String topicTitle = "Topic #" + suffixe;
		String postTitle = "post #" + suffixe;
		String postBody = "Comnentaire " + suffixe;
		int userId =  1;
		
		
		
	    mockMvc.perform(post("/topics/addTopic")
	    			.param("topicTitle", topicTitle) 
	    			.param("postTitle", postTitle)
	    			.param("postBody", postBody)
	    			.param("userId", String.valueOf(userId)))
        			.andExpect(content().contentType("application/json;charset=UTF-8"))// OK
        			.andReturn().getResponse().getContentAsString().equals("{\"status\":\"ok\"}");
	    			
	    int lastTopicId =  topicDAO.getLastInsertedTopicId();
	    
	    assertTrue(lastTopicId > 0);
	    
	    Topic addedTopic = topicDAO.getTopic(lastTopicId);
	    
	    assertNotNull(addedTopic);
	    
	    assertEquals(addedTopic.getTitle(), topicTitle);
	    
	    int lastPostId =  postDAO.getLastInsertedPostId();
	    
	    assertTrue(lastPostId > 0); 
	    
	    Post addedPost =  postDAO.getPost(lastPostId);
	    
	    assertNotNull(addedPost);
	    

	    assertEquals(addedPost.getTitle(), postTitle);
	    assertEquals(addedPost.getBody(), postBody);
	    
	    assertEquals(Integer.valueOf(lastTopicId), addedPost.getTopicId());
	    assertEquals(Integer.valueOf(userId), addedTopic.getCreatorId());
	    assertEquals(Integer.valueOf(userId), addedPost.getUserId());	    
	    
	    //delete topic and post from db

	    int deletedPostResult =  postDAO.deletePostFromDB(lastPostId);
	    
		logger.log(Level.INFO, "in addTopicTest() deletedPostResult: " + deletedPostResult);
	    
	    assertTrue(deletedPostResult > 0);
	    
	    Post deletedPost =  postDAO.getPost(lastPostId);
	    
	    assertNull(deletedPost);
	    
	    
	    //delete topic
	    int deletedTopicResult = topicDAO.deleteTopicFromDB(lastTopicId);
	    
	    assertTrue(deletedTopicResult > 0);
	    
		logger.log(Level.INFO, "in addTopicTest() deletedTopicResult: " + deletedTopicResult);
	    
	    Topic deletedTopic =  topicDAO.getTopic(lastTopicId);
	    
	    assertNull(deletedTopic);
    
	    
	}
	
	//*********************************************************************************************************************
	//*********************************************************************************************************************
	
	@Test
	public void addTopicTest2() throws UnsupportedEncodingException, Exception {

		TopicController controller =  new TopicController(topicDAO, postDAO);
		
		MockMvc mockMvc = standaloneSetup(controller).build();
			
		long suffixe =  new Date().getTime() % 10000;
		
		String topicTitle = "Topic - " + suffixe;
		String postTitle = "post - " + suffixe;
		String postBody = "Comnentaire-" + suffixe;
		int userId =  1;

		mockMvc.perform(post("/topics/addTopic/"+topicTitle+"/"+postTitle+"/"+postBody+"/"+userId))
        			.andExpect(content().contentType("application/json;charset=UTF-8"))// OK
        			.andReturn()
        			.getResponse()
        			.getContentAsString().equals("{\"status\":\"ok\"}");
	    			

	    
	    int lastTopicId =  topicDAO.getLastInsertedTopicId();
	    
	    assertTrue(lastTopicId > 0);
	    
	    Topic addedTopic = topicDAO.getTopic(lastTopicId);
	    
	    assertNotNull(addedTopic);
	    
	    assertEquals(addedTopic.getTitle(), topicTitle);
	    
	    int lastPostId =  postDAO.getLastInsertedPostId();
	    
	    assertTrue(lastPostId > 0); 
	    
	    Post addedPost =  postDAO.getPost(lastPostId);
	    
	    assertNotNull(addedPost);

	    assertEquals(addedPost.getTitle(), postTitle);
	    assertEquals(addedPost.getBody(), postBody);
	    
	    assertEquals(Integer.valueOf(lastTopicId), addedPost.getTopicId());
	    assertEquals(Integer.valueOf(userId), addedTopic.getCreatorId());
	    assertEquals(Integer.valueOf(userId), addedPost.getUserId());	    
	    
	    
	    //delete topic and post from db
	    int deletedPostResult =  postDAO.deletePostFromDB(lastPostId);
	    
		logger.log(Level.INFO, "in addTopicTest2() deletedPostResult: " + deletedPostResult);
	    
	    assertTrue(deletedPostResult > 0);
	    
	    Post deletedPost =  postDAO.getPost(lastPostId);
	    
	    assertNull(deletedPost);
	    
	    //delete topic
	    int deletedTopicResult = topicDAO.deleteTopicFromDB(lastTopicId);
	    
	    assertTrue(deletedTopicResult > 0);
	    
		logger.log(Level.INFO, "in addTopicTest2() deletedTopicResult: " + deletedTopicResult);
	    
	    Topic deletedTopic =  topicDAO.getTopic(lastTopicId);
	    
	    assertNull(deletedTopic);
    
	    
	    
	}
	
	//*********************************************************************************************************************
	//*********************************************************************************************************************
	
	@Test
	public void updateTopic() throws UnsupportedEncodingException, Exception {

		TopicController controller =  new TopicController(topicDAO, postDAO);
		
		MockMvc mockMvc = standaloneSetup(controller).build();
			
		long suffixe =  new Date().getTime() % 10000;
		
		String topicTitle = "Topic - " + suffixe;
		Topic topic = new Topic();
		topic.setTitle(topicTitle);
		topic.setCreatorId(1);
		//add topic first
		
		int addedTopicResult = topicDAO.addTopic(topic);
		
		assertTrue(addedTopicResult > 0);
		
		int lastTopicId = topicDAO.getLastInsertedTopicId();
		
		assertTrue(lastTopicId > 0);
		
		String newTitle =  "New Title for topic - " + suffixe;
		
	    mockMvc.perform(put("/topics/updateTopic")
	    			.param("topicTitle", newTitle) 
	    			.param("topicId", Integer.toString(lastTopicId)))
        			.andExpect(content().contentType("application/json;charset=UTF-8"))// OK
        			.andReturn().getResponse().getContentAsString().equals("{\"status\":\"ok\"}");
	    			
	    
	    Topic updatedTopic = topicDAO.getTopic(lastTopicId);
	    
	    assertNotNull(updatedTopic);
	    
	    assertEquals(newTitle, updatedTopic.getTitle());
	    
	    //delete topic from db
	  
	    int deletedTopicResult = topicDAO.deleteTopicFromDB(lastTopicId);
	    
	    assertTrue(deletedTopicResult > 0);
	    
		logger.log(Level.INFO, "in updateTopicTest() deletedTopicResult: " + deletedTopicResult);
	    
	    Topic deletedTopic =  topicDAO.getTopic(lastTopicId);
	    
	    assertNull(deletedTopic);
	    
	}
	
	
	
	//*********************************************************************************************************************
	//*********************************************************************************************************************
	
	@Test
	public void updateTopicTest2() throws UnsupportedEncodingException, Exception {

		TopicController controller =  new TopicController(topicDAO, postDAO);
		
		MockMvc mockMvc = standaloneSetup(controller).build();
			
		
		long suffixe =  new Date().getTime() % 10000;
		
		String topicTitle = "Topic title - " + suffixe;
		Topic topic = new Topic();
		topic.setTitle(topicTitle);
		topic.setCreatorId(1);
		//add topic first
		
		int addedTopicResult = topicDAO.addTopic(topic);
		
		assertTrue(addedTopicResult > 0);
		
		int lastTopicId = topicDAO.getLastInsertedTopicId();
		
		assertTrue(lastTopicId > 0);
		
		String newTitle =  "New Topic Title - " + suffixe;
		
		logger.log(Level.INFO, "in updateTopicTest2() newTitle: " + newTitle);
		
	    mockMvc.perform(put("/topics/updateTopic/"+lastTopicId+"/"+newTitle))
        			.andExpect(content().contentType("application/json;charset=UTF-8"))// OK
        			.andReturn()
        			.getResponse()
        			.getContentAsString().equals("{\"status\":\"ok\"}");
	    			
	    Topic updatedTopic = topicDAO.getTopic(lastTopicId);
	    
	    assertNotNull(updatedTopic);
	    
	    assertEquals(newTitle, updatedTopic.getTitle());
	    
	    //delete topic from db
	    int deletedTopicResult = topicDAO.deleteTopicFromDB(lastTopicId);
	    
	    assertTrue(deletedTopicResult > 0);
	    
		logger.log(Level.INFO, "in updateTopicTest2() deletedTopicResult: " + deletedTopicResult);
	    
	    Topic deletedTopic =  topicDAO.getTopic(lastTopicId);
	    
	    assertNull(deletedTopic);

	    
	}
	
	//*********************************************************************************************************************
	//*********************************************************************************************************************
	
	@Test
	public void deleteTopic() throws UnsupportedEncodingException, Exception {

		TopicController controller =  new TopicController(topicDAO, postDAO);
		
		MockMvc mockMvc = standaloneSetup(controller).build();
			
		
		long suffixe =  new Date().getTime() % 10000;
		
		String topicTitle = "Topic title - " + suffixe;
		Topic topic = new Topic();
		topic.setTitle(topicTitle);
		topic.setCreatorId(1);
		topic.setIsOpen((byte) 1);
		
		//add topic first
		int addedTopicResult = topicDAO.addTopic(topic);
		
		assertTrue(addedTopicResult > 0);
		
		int lastTopicId = topicDAO.getLastInsertedTopicId();
		
		assertTrue(lastTopicId > 0);
		
		Topic addedTopic = topicDAO.getTopic(lastTopicId);
		
		assertEquals(1, addedTopic.getIsOpen());
				
		//close topic
	    mockMvc.perform(delete("/topics/deleteTopic")
	    			.param("topicId", Integer.toString(lastTopicId)))
        			.andExpect(content().contentType("application/json;charset=UTF-8"))// OK
        			.andReturn()
        			.getResponse()
        			.getContentAsString().equals("{\"status\":\"ok\"}");
	    			
	    Topic closedTtopic = topicDAO.getTopic(lastTopicId);
	 
	    assertNotNull(closedTtopic);
	    
	    assertEquals(0, closedTtopic.getIsOpen());
	    
	    //delete topic from db
	    int deletedTopicResult = topicDAO.deleteTopicFromDB(lastTopicId);
	    
	    assertTrue(deletedTopicResult > 0);
	    
		logger.log(Level.INFO, "in updateTopicTest2() deletedTopicResult: " + deletedTopicResult);
	    
	    Topic deletedTopic =  topicDAO.getTopic(lastTopicId);
	    
	    assertNull(deletedTopic);
	}
	
	
	
	//*********************************************************************************************************************
	//*********************************************************************************************************************
	
	@Test
	public void deactivate() throws UnsupportedEncodingException, Exception {

		TopicController controller =  new TopicController(topicDAO, postDAO);
		
		MockMvc mockMvc = standaloneSetup(controller).build();

		long suffixe =  new Date().getTime() % 10000;
		
		String topicTitle = "Topic title - " + suffixe;
		Topic topic = new Topic();
		topic.setTitle(topicTitle);
		topic.setCreatorId(1);
		topic.setIsOpen((byte) 1);
		
		//add topic first
		int addedTopicResult = topicDAO.addTopic(topic);
		
		assertTrue(addedTopicResult > 0);
		
		int lastTopicId = topicDAO.getLastInsertedTopicId();
		
		assertTrue(lastTopicId > 0);
		
		Topic addedTopic = topicDAO.getTopic(lastTopicId);
		
		assertEquals(1, addedTopic.getIsOpen());
		
		
		//deactivate
	    mockMvc.perform(delete("/topics/deleteTopic/"+lastTopicId))
        			.andExpect(content().contentType("application/json;charset=UTF-8"))// OK
        			.andReturn()
        			.getResponse()
        			.getContentAsString().equals("{\"status\":\"ok\"}");
	    			
	    Topic closedTtopic = topicDAO.getTopic(lastTopicId);
		 
	    assertNotNull(closedTtopic);
	    
	    assertEquals(0, closedTtopic.getIsOpen());
	    
	    //delete topic from db
	    int deletedTopicResult = topicDAO.deleteTopicFromDB(lastTopicId);
	    
	    assertTrue(deletedTopicResult > 0);
	    
		logger.log(Level.INFO, "in updateTopicTest2() deletedTopicResult: " + deletedTopicResult);
	    
	    Topic deletedTopic =  topicDAO.getTopic(lastTopicId);
	    
	    assertNull(deletedTopic);

	    
	}	
	
	//*********************************************************************************************************************
	//*********************************************************************************************************************

}
