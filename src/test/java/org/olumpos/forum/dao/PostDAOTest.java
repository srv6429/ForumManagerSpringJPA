package org.olumpos.forum.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.olumpos.forum.dao.PostDAO;
import org.olumpos.forum.dao.PostDAOImpl;
import org.olumpos.forum.dao.TopicDAO;
import org.olumpos.forum.dao.TopicDAOImpl;
import org.olumpos.forum.dao.UserDAO;
import org.olumpos.forum.dao.UserDAOImpl;
import org.olumpos.forum.entity.Post;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * 
 * @author daristote
 * 
 * Tests pour les fonction d'accès à la base de données de PostDAO utilisant JPA: 
 *
 */

//@RunWith(SpringRunner.class)
public class PostDAOTest {
	
	private static Logger logger =  Logger.getLogger(PostDAOTest.class.getName());
	
	private TopicDAO topicDAO;
	private PostDAO postDAO;
	private UserDAO userDAO;
	
	@Before
	public void init() {
		topicDAO =  new TopicDAOImpl();
		postDAO = new PostDAOImpl();
		userDAO =  new UserDAOImpl();
		
		logger.log(Level.INFO, "int PostDAOTest.init() Before: topicDAO: " + topicDAO);
		logger.log(Level.INFO, "int PostDAOTest.init() Before: postDAO: " + postDAO);
		logger.log(Level.INFO, "int PostDAOTest.init() Before: userDAO: " + userDAO);
		
	}

	
	//*********************************************************************************************************************
	//*********************************************************************************************************************
	
	@Test
	public void testGet() {
		
		logger.log(Level.INFO, "in PostDAOTest.testGet(): postDAO: " + postDAO);
		logger.log(Level.INFO, "in PostDAOTest.testGet(): topicDAO: " + topicDAO);
		
		List<Post> allPosts =  postDAO.getAllPosts();
		assertNotNull(allPosts);
		
		int nbAllPosts =  allPosts.size();
		logger.log(Level.INFO, "nbAllPosts: " + nbAllPosts);
	
		
		List<Post> allActivePosts =  postDAO.getAllActivePosts();
		assertNotNull(allActivePosts);
		
		int nbAllActivePosts =  allActivePosts.size();
		logger.log(Level.INFO, "nbAllActivePosts: " + nbAllActivePosts);
		
		//Le nombre de post actifs ne peut être supérieur au nombre total de posts 
		assertTrue(nbAllActivePosts <= nbAllPosts);
		
		
		//get posts by topic
		int lastTopicId = topicDAO.getLastInsertedTopicId();
		
		//on suppose au moins un topic dans la bd
		assertNotEquals (0, lastTopicId);
		
		List<Post> allPostsByTopic =  postDAO.getAllPosts(lastTopicId);
		assertNotNull(allPostsByTopic);
		
		int nbAllPostsByTopic =  allPostsByTopic.size();
		logger.log(Level.INFO, "nbAllPostsByTopic: " + nbAllPostsByTopic);
		

		List<Post> allActivePostsByTopic =  postDAO.getAllActivePosts(lastTopicId);
		assertNotNull(allActivePosts);
		
		int nbAllActivePostsByTopic =  allActivePostsByTopic.size();
		logger.log(Level.INFO, "nbAllActivePostsByTopic: " + nbAllActivePostsByTopic);
		
		//Le nombre de post actifs ne peut être supérieur au nombre total de posts pour une topic
		assertTrue(nbAllActivePostsByTopic <= nbAllPostsByTopic);
		
		
		//Get one post
		int lastInsertedPostId =  postDAO.getLastInsertedPostId();
		
		logger.log(Level.INFO, "last inserted post id: " + lastInsertedPostId);
		
		Post lastPost = postDAO.getPost(lastInsertedPostId);
		
		assertNotNull(lastPost);
		
		assertEquals(new Integer(lastInsertedPostId), lastPost.getId());
		
		
	}
	
	
	//*********************************************************************************************************************
	//*********************************************************************************************************************
	
	@Test
	public void testInsert() {

		logger.log(Level.INFO, "in PostDAOTest.testInsert(): postDAO: " + postDAO);
		
		Post post =  new Post();
		post.setTitle("A new post to add");
		post.setBody("A new comment");
		post.setUserId(3);
		post.setTopicId(5);
		
		List<Post> posts =  postDAO.getAllPosts();
		
		assertNotNull(posts);
		
		int postsSize =  posts.size();
		
		
		logger.log(Level.INFO, "posts size: " + postsSize);

		
		int result = postDAO.addPost(post);

		assertEquals(1, result);		
		
	
		int lastPostId =  postDAO.getLastInsertedPostId();
		
		Post lastPostInserted =  postDAO.getPost(lastPostId);
		
		assertEquals(new Integer(lastPostId), lastPostInserted.getId());
		
		posts =  postDAO.getAllPosts();
		
		logger.log(Level.INFO, "posts new size: " + posts.size());
		
		assertEquals(postsSize+1, posts.size());
		
	}
	
	//*********************************************************************************************************************
	//*********************************************************************************************************************
	
	@Test
	public void testUpdate() {

		logger.log(Level.INFO, "in PostDAOTest.testUpdate(): postDAO: " + postDAO);
		
		Post post =  new Post();
		post.setTitle("A new post 2983");
		post.setBody("A new comment 2983");
		post.setUserId(3);
		post.setTopicId(5);
		
		int result = postDAO.addPost(post);
		
		assertEquals(1, result);
		int lastpostId =  postDAO.getLastInsertedPostId();

		assertNotEquals(0, lastpostId);
		
		//get the just added topic 
		post =  postDAO.getPost(lastpostId);
		
		assertNotNull(post);
		
		String titleBeforeUpdate =  post.getTitle();
		String bodyBeforeUpdate =  post.getBody();
		//String updateDate =  post.getUpdateDate();
		
		String updatedTitle = titleBeforeUpdate + " updated";
		String updatedComment =  bodyBeforeUpdate + " updated";
		
		post.setTitle(updatedTitle);
		post.setBody(updatedComment);
		
		//Delay for 3 seconds to be sure that the update date will be different from the previous one
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//update topic
		result = postDAO.updatePost(post);
		
		assertEquals(1, result);
		
		Post updatedPost =  postDAO.getPost(lastpostId);
		
		assertNotNull(updatedPost);
		
		assertEquals(updatedPost.getTitle(), updatedTitle);
		assertEquals(updatedPost.getBody(), updatedComment);

		//verify that
	//	assertNotEquals(updatedPost.getUpdateDate(), updateDate);

		
	}
	
	
	//*********************************************************************************************************************
	//*********************************************************************************************************************
	
	@Test
	public void testDelete() {
		
		logger.log(Level.INFO, "in PostDAOTest.tesDelete(): postDAO: " + postDAO);
		
		int lastTopicId = topicDAO.getLastInsertedTopicId();
		int userId = userDAO.getLastInsertedUserId();
		
		List<Post> activePosts =  postDAO.getAllActivePosts(lastTopicId);
		int nbActivePostsBeforeInsert =  activePosts.size();
		
		List<Post> allPosts =  postDAO.getAllPosts(lastTopicId);
		int nbAllPostsBeforeInsert =  allPosts.size();
		
		Post post =  new Post();
		post.setTitle("A new post to add");
		post.setBody("A new comment");
		post.setUserId(userId);
		post.setTopicId(lastTopicId);
		
		//add new Post
		int result = postDAO.addPost(post);
		
		assertEquals(1, result);
		
		//get last inserted
		int lastPostId =  postDAO.getLastInsertedPostId();

		assertNotEquals(0, lastPostId);
		
		//get active posts
		activePosts =  postDAO.getAllActivePosts(lastTopicId);
		
		assertNotNull(activePosts);
		
		//should have one more active post
		assertEquals(nbActivePostsBeforeInsert+1, activePosts.size());
	
		
		//deactivate last inserted post
		result = postDAO.deletePost(lastPostId);

		assertEquals(1, result);
		
		activePosts =  postDAO.getAllActivePosts(lastTopicId);
		
		//should be back to the same after deactivation of the last post inserted
		assertEquals(nbActivePostsBeforeInsert, activePosts.size());
		
		//verify the total number of posts is one more despite deactivation
		allPosts = postDAO.getAllPosts(lastTopicId);
		
		assertEquals(nbAllPostsBeforeInsert+1, allPosts.size());
		
		//ractivate post
		result = postDAO.activateDeactivatePost(lastPostId, (byte)1);
		
		assertEquals(1, result);
		
		activePosts =  postDAO.getAllActivePosts(lastTopicId);
		
		//verify the number of active posts is one more withe the new inserted post
		assertEquals(nbActivePostsBeforeInsert+1, activePosts.size());

	}
	
	//*********************************************************************************************************************
	//*********************************************************************************************************************
	
	@Test
	public void testErrors() {

		
		logger.log(Level.INFO, "in PostDAOTest.testErrors(): postDAO: " + postDAO);
		
		
		
		//1. get post with a non valid id
		Post post =  postDAO.getPost(0);
		
		assertNull(post);

		//2. get posts with a non valid topic id
		List<Post> posts =  postDAO.getAllPosts(0);
		
		assertEquals(0, posts.size());

		
		//3.insert with a non valid user id
		post =  new Post();
		post.setTitle("New post");
		post.setBody("New comment");
		post.setUserId(0); //Erreur! id nn valide

		int result = postDAO.addPost(post);
				
		assertEquals(result, 0); //aucun résultat retourné
		assertTrue(post.messageExists()); //un message d'erreur a été enregistré
		logger.log(Level.SEVERE, "error: " + post.getMessage());
		

		
//		//4.update post with non valid id
		int userId =  userDAO.getLastInsertedUserId();
		post.setUserId(userId);
		post.setId(0);//Erreur! id non valide
		
		result = postDAO.updatePost(post);

		assertEquals(result, 0); //aucun résultat retourné
		assertTrue(post.messageExists()); //un message d'erreur a été enregistré
		logger.log(Level.SEVERE, "error: " + post.getMessage());
		
//		//5.update post with null title
		
		int lastPostId =  postDAO.getLastInsertedPostId();
		post.setId(lastPostId);
		post.setTitle(null);
		result = postDAO.updatePost(post);

		assertEquals(result, 0); //aucun résultat retourné
		assertTrue(post.messageExists()); //un message d'erreur a été enregistré
		logger.log(Level.SEVERE, "error: " + post.getMessage());
		
		
//		//5.update post with null body
		post.setTitle("Un titre");
		post.setBody(null);
		result = postDAO.updatePost(post);

		assertEquals(result, 0); //aucun résultat retourné
		assertTrue(post.messageExists()); //un message d'erreur a été enregistré
		logger.log(Level.SEVERE, "error: " + post.getMessage());
		
		
	}
}
