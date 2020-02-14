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
import org.olumpos.forum.dao.UserDAO;
import org.olumpos.forum.dao.UserDAOImpl;
import org.olumpos.forum.entity.Post;
import org.olumpos.forum.entity.Topic;
import org.olumpos.forum.util.ForumDateFormatter;

import java.io.UnsupportedEncodingException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
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
	public void addTopic() throws UnsupportedEncodingException, Exception {

		TopicController controller =  new TopicController(topicDAO, postDAO);
		
		MockMvc mockMvc = standaloneSetup(controller).build();
			
		long suffixe =  new Date().getTime() % 10000;
		
		String topicTitle = "Topic #" + suffixe;
		String postTitle = "post #" + suffixe;
		String postBody = "Comnentaire " + suffixe;
		String userId =  "4";
		
	    mockMvc.perform(post("/topics/addTopic")
	    			.param("topicTitle", topicTitle) 
	    			.param("postTitle", postTitle)
	    			.param("postBody", postBody)
	    			.param("userId", userId))
        			.andExpect(content().contentType("application/json;charset=UTF-8"))// OK
        			.andReturn().getResponse().getContentAsString().equals("{\"status\":\"ok\"}");
	    			
	    int lastTopicId =  topicDAO.getLastInsertedTopicId();
	    Topic topic = topicDAO.getTopic(lastTopicId);
	    
	    int lastPostId =  postDAO.getLastInsertedPostId();
	    Post post =  postDAO.getPost(lastPostId);
	    
	    assertEquals(topic.getTitle(), topicTitle);
	    assertEquals(post.getTitle(), postTitle);
	    assertEquals(post.getBody(), postBody);
	    assertEquals(Integer.valueOf(lastTopicId), post.getTopicId());
	    assertEquals(topic.getCreatorId(), post.getUserId());
	    
	    
	}
	
	//*********************************************************************************************************************
	//*********************************************************************************************************************
	
	@Test
	public void addTopic2() throws UnsupportedEncodingException, Exception {

		TopicController controller =  new TopicController(topicDAO, postDAO);
		
		MockMvc mockMvc = standaloneSetup(controller).build();
			
		long suffixe =  new Date().getTime() % 10000;
		
		String topicTitle = "Topic-" + suffixe;
		String postTitle = "post-" + suffixe;
		String postBody = "Comnentaire-" + suffixe;
		int userId =  4;
		
//	    mockMvc.perform(post("/topics/addTopic/"+userId+"/"+topicTitle+"/"+postTitle+"/"+postBody))
	    mockMvc.perform(post("/topics/addTopic/"+topicTitle+"/"+postTitle+"/"+postBody+"/"+userId))
//	    			.param("topicTitle", topicTitle) 
	//    			.param("postTitle", postTitle)
	 //   			.param("postBody", postBody)
	 //   			.param("userId", userId))
        			.andExpect(content().contentType("application/json;charset=UTF-8"))// OK
        			.andReturn().getResponse().getContentAsString().equals("{\"status\":\"ok\"}");
	    			
	    int lastTopicId =  topicDAO.getLastInsertedTopicId();
	    Topic topic = topicDAO.getTopic(lastTopicId);
	    
	    int lastPostId =  postDAO.getLastInsertedPostId();
	    Post post =  postDAO.getPost(lastPostId);
	    
	    assertEquals(topic.getTitle(), topicTitle);
	    assertEquals(post.getTitle(), postTitle);
	    assertEquals(post.getBody(), postBody);
	    assertEquals(Integer.valueOf(lastTopicId), post.getTopicId());
	    assertEquals(topic.getCreatorId(), post.getUserId());
	    
	    
	}
	
	//*********************************************************************************************************************
	//*********************************************************************************************************************
	
	@Test
	public void updateTopic() throws UnsupportedEncodingException, Exception {

		TopicController controller =  new TopicController(topicDAO, postDAO);
		
		MockMvc mockMvc = standaloneSetup(controller).build();
			
		long suffixe =  new Date().getTime() % 10000;
		
		String topicTitle = "Topic #" + suffixe;

		int lastTopicId = topicDAO.getLastInsertedTopicId();
		
	    mockMvc.perform(put("/topics/updateTopic")
	    			.param("topicTitle", topicTitle) 
	    			.param("topicId", Integer.toString(lastTopicId)))
        			.andExpect(content().contentType("application/json;charset=UTF-8"))// OK
        			.andReturn().getResponse().getContentAsString().equals("{\"status\":\"ok\"}");
	    			
	    Topic topic = topicDAO.getTopic(lastTopicId);
	    
	    assertEquals(topic.getTitle(), topicTitle);
	    
	}
	
	
	
	//*********************************************************************************************************************
	//*********************************************************************************************************************
	
	@Test
	public void updateTopic2() throws UnsupportedEncodingException, Exception {

		TopicController controller =  new TopicController(topicDAO, postDAO);
		
		MockMvc mockMvc = standaloneSetup(controller).build();
			
		long suffixe =  new Date().getTime() % 10000;
		
		String topicTitle = "Topic-" + suffixe;		
		int lastTopicId = topicDAO.getLastInsertedTopicId();
		
	    mockMvc.perform(put("/topics/updateTopic/"+lastTopicId+"/"+topicTitle))
        			.andExpect(content().contentType("application/json;charset=UTF-8"))// OK
        			.andReturn().getResponse().getContentAsString().equals("{\"status\":\"ok\"}");
	    			
	    Topic topic = topicDAO.getTopic(lastTopicId);
	    	    
	    assertEquals(topic.getTitle(), topicTitle);

	    
	}
	
	//*********************************************************************************************************************
	//*********************************************************************************************************************
	
	@Test
	public void deleteTopic() throws UnsupportedEncodingException, Exception {

		TopicController controller =  new TopicController(topicDAO, postDAO);
		
		MockMvc mockMvc = standaloneSetup(controller).build();
			
		int lastTopicId = topicDAO.getLastInsertedTopicId();
		
		topicDAO.openCloseTopic(lastTopicId, (byte)1); //make sure topic is open
		
		Topic topic = topicDAO.getTopic(lastTopicId);
		
		assertEquals((byte)1, topic.getIsOpen());
		
	    mockMvc.perform(delete("/topics/deleteTopic")
	    			.param("topicId", Integer.toString(lastTopicId)))
        			.andExpect(content().contentType("application/json;charset=UTF-8"))// OK
        			.andReturn().getResponse().getContentAsString().equals("{\"status\":\"ok\"}");
	    			
	    topic = topicDAO.getTopic(lastTopicId);
	    
	    assertEquals(0, topic.getIsOpen());
	    
	    
	}
	
	
	
	//*********************************************************************************************************************
	//*********************************************************************************************************************
	
	@Test
	public void deactivate() throws UnsupportedEncodingException, Exception {

		TopicController controller =  new TopicController(topicDAO, postDAO);
		
		MockMvc mockMvc = standaloneSetup(controller).build();

		int lastTopicId = topicDAO.getLastInsertedTopicId();
		
		topicDAO.openCloseTopic(lastTopicId, (byte)1); //make sure topic is open
		
		Topic topic = topicDAO.getTopic(lastTopicId);
		
		assertEquals((byte)1, topic.getIsOpen());
		
	    mockMvc.perform(delete("/topics/deleteTopic/"+lastTopicId))
        			.andExpect(content().contentType("application/json;charset=UTF-8"))// OK
        			.andReturn().getResponse().getContentAsString().equals("{\"status\":\"ok\"}");
	    			
	    topic = topicDAO.getTopic(lastTopicId);
	    
	    assertEquals(0, topic.getIsOpen());

	    
	}	
	
	//*********************************************************************************************************************
	//*********************************************************************************************************************

}
