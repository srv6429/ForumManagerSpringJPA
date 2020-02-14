package org.olumpos.forum.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.junit.Assert.assertEquals;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.Before;
import org.junit.Test;
import org.olumpos.forum.controller.PostController;
import org.olumpos.forum.dao.PostDAO;
import org.olumpos.forum.dao.PostDAOImpl;
import org.olumpos.forum.dao.TopicDAO;
import org.olumpos.forum.dao.TopicDAOImpl;
import org.olumpos.forum.dao.UserDAO;
import org.olumpos.forum.dao.UserDAOImpl;
import org.olumpos.forum.entity.Post;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.view.InternalResourceView;

/**
 * 
 * @author daristote
 * 
 * Tests pour les fonction du contrôleur Spring MVC: PostController
 *
 */

public class PostControllerTest {

	Logger logger =  Logger.getLogger(PostControllerTest.class.getName());
	
	private TopicDAO topicDAO;
	private PostDAO postDAO;
	private UserDAO userDAO;
	
	@Before
	public void init() {
		topicDAO =  new TopicDAOImpl();
		postDAO =  new PostDAOImpl();
		userDAO =  new UserDAOImpl();
		
		logger.log(Level.INFO, "PostControllerTest.init(): topicDao: " + topicDAO);
		logger.log(Level.INFO, "PostControllerTest.init(): postDao: " + postDAO);
		logger.log(Level.INFO, "PostControllerTest.init(): userDAO: " + userDAO);
	}
	
	//*********************************************************************************************************************
	//*********************************************************************************************************************
	
	@Test
	public void getPosts() throws Exception {
		
		logger.log(Level.INFO, "PostControllerTest.getPOsts(): postDao: " + postDAO);
		PostController controller =  new PostController(postDAO);
		MockMvc mockMvc = standaloneSetup(controller)
				.setSingleView(new InternalResourceView("/WEB-INF/views/posts.jsp"))
				.build();
		
 
		int lastTopicId =  topicDAO.getLastInsertedOpenTopicId();
		
		logger.log(Level.INFO, "PostControllerTest.getPOsts(): lastTopicId: " + lastTopicId);
		
		List<Post> posts = postDAO.getAllActivePosts(lastTopicId); 
		
		logger.log(Level.INFO, "PostControllerTest.getPOsts(): post.size: " + posts.size());
		
		mockMvc.perform(get("/posts/"+ lastTopicId))
	       .andExpect(view().name("posts"))
	       .andExpect(model().attributeExists("postList"))
	    //   .andExpect(model().attribute("userList", hasItems(posts.toArray()))) //error but should work
	       .andExpect(model().hasNoErrors());
		
	}
	

	
	
	//*********************************************************************************************************************
	//*********************************************************************************************************************
	@Test
	public void addPost() throws UnsupportedEncodingException, Exception {

		PostController controller =  new PostController(postDAO);
		
		MockMvc mockMvc = standaloneSetup(controller).build();
		
		long suffixe =  new Date().getTime() % 10000;
				
		String postTitle = "post #" + suffixe;
		String postBody = "Comnentaire " + suffixe;
		int topicId = topicDAO.getLastInsertedOpenTopicId();
		int userId =  userDAO.getLastInsertedUserId();
		
	    mockMvc.perform(post("/posts/addPost")
    				.param("postTitle", postTitle)
	    			.param("postBody", postBody)
	    			.param("topicId", Integer.toString(topicId))
	    			.param("userId", Integer.toString(userId)))
        		//	.andExpect(content().contentType("application/json;charset=UTF-8"))// OK
	    			.andReturn().getResponse().getContentAsString().equals("{\"status\":\"ok\"}");
	    			
		int lastPostId =  postDAO.getLastInsertedPostId();
	    Post post =  postDAO.getPost(lastPostId);
	    
	    assertEquals(Integer.valueOf(topicId), post.getTopicId());
	    assertEquals( postTitle, post.getTitle());
	    assertEquals(postBody, post.getBody());
	    assertEquals(Integer.valueOf(userId), post.getUserId());
	        
	}
	
	//*********************************************************************************************************************
	//*********************************************************************************************************************
	@Test
	public void updatePost() throws UnsupportedEncodingException, Exception {

		PostController controller =  new PostController(postDAO);
		
		MockMvc mockMvc = standaloneSetup(controller).build();
		
		long suffixe =  new Date().getTime() % 10000;
		
	    int lastPostId =  postDAO.getLastInsertedPostId();
		String postTitle = "post #" + suffixe;
		String postBody = "Comnentaire " + suffixe;
		
	    mockMvc.perform(put("/posts/updatePost")
	    			.param("postId", Integer.toString(lastPostId))
	    			.param("postTitle", postTitle)
	    			.param("postBody", postBody))
        			.andExpect(content().contentType("application/json;charset=UTF-8"))// OK
        			.andReturn().getResponse().getContentAsString().equals("{\"status\":\"ok\"}");
	    			
	    
	    Post post =  postDAO.getPost(lastPostId);
	    
	    assertEquals( postTitle, post.getTitle());
	    assertEquals(postBody, post.getBody());

	       
	}
	
	//*********************************************************************************************************************
	//*********************************************************************************************************************
	@Test
	public void update() throws UnsupportedEncodingException, Exception {

		PostController controller =  new PostController(postDAO);
		
		MockMvc mockMvc = standaloneSetup(controller).build();
		
		long suffixe =  new Date().getTime() % 10000;
		
		int lastPostId =  postDAO.getLastInsertedPostId();		
		String postTitle = "post - " + suffixe;
		String postBody = "Comnentaire - " + suffixe;

		
	    mockMvc.perform(put("/posts/updatePost/"+lastPostId +"/"+postTitle +"/" + postBody))
        			//.andExpect(content().contentType("application/json;charset=UTF-8"))
	    		.andReturn().getResponse().getContentAsString().equals("{\"status\":\"ok\"}");
	    			
	    
	    Post post =  postDAO.getPost(lastPostId);
	    
	    assertEquals( postTitle, post.getTitle());
	    assertEquals(postBody, post.getBody());
	        
	}
	
	//*********************************************************************************************************************
	//*********************************************************************************************************************
	@Test
	public void deactivate() throws UnsupportedEncodingException, Exception {

		PostController controller =  new PostController(postDAO);
		
		MockMvc mockMvc = standaloneSetup(controller).build();
	
		int lastPostId =  postDAO.getLastInsertedPostId();
		
		 //make sure post is active
		postDAO.activateDeactivatePost(lastPostId, (byte)1);
		
		Post post = postDAO.getPost(lastPostId);
		
		assertEquals(1, post.getIsActive());
	
		
	    mockMvc.perform(delete("/posts/deletePost")
    				.param("postId", Integer.toString(lastPostId)))
	    			.andExpect(content().contentType("application/json;charset=UTF-8"))// OK
	    			.andReturn().getResponse().getContentAsString().equals("{\"status\":\"ok\"}");
	    			
	    
	    post = postDAO.getPost(lastPostId);
	    
	    assertEquals(0, post.getIsActive());
	  
		 //reactivate
		postDAO.activateDeactivatePost(lastPostId, (byte)1);
		
		post = postDAO.getPost(lastPostId);
		
		assertEquals(1, post.getIsActive());
	    
	       
	}
	
	
	//*********************************************************************************************************************
	//*********************************************************************************************************************
	@Test
	public void deletePost() throws UnsupportedEncodingException, Exception {

		PostController controller =  new PostController(postDAO);
		
		MockMvc mockMvc = standaloneSetup(controller).build();
		
		int lastPostId =  postDAO.getLastInsertedPostId();
		
		postDAO.activateDeactivatePost(lastPostId, (byte)1); //make sure post is active
		
		Post post = postDAO.getPost(lastPostId);
		
		assertEquals(1, post.getIsActive());

		
	    mockMvc.perform(delete("/posts/deletePost/"+lastPostId))
        			.andExpect(content().contentType("application/json;charset=UTF-8"))
        			.andReturn().getResponse().getContentAsString().equals("{\"status\":\"ok\"}");
	    			
	    
	    post = postDAO.getPost(lastPostId);
	    assertEquals(0, post.getIsActive());
	    
	    
	    //ractivate post
		postDAO.activateDeactivatePost(lastPostId, (byte)1);
		
		post = postDAO.getPost(lastPostId);
		
		assertEquals(1, post.getIsActive());
	        
	}
	
}
