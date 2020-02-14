package org.olumpos.forum.interceptor;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.olumpos.forum.entity.User;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * 
 * @author daristote
 * 
 * Classe qui permet d'interceper les requêtes http de vérifier si l,utilisateur est connecté 
 * et de le rediriger de manière appropirée s'il ne l'est pas
 * 
 * Étend (extends) la classe 'org.springframework.web.servlet.handler.HandlerInterceptorAdapter' 
 * et redéfinit (@Override )la méthode prehandle
 * 
 * Pour que la méthode soit appelée à chaque requête, on doit spécifier le nom de la classe en tant que "bean" 
 * dans le fichier "WEB-INF/spring/appServler/servlet-context.xml"
 * à l'intérieur de la balise : <interceptor> en définissant l'attribut path="/**" 
 * pour spécifier que toute requête appelée à l'intérieur du contexte de l'application doit nécessairement passée par l'intercepteur
 * 
 * On veut rediriger un utilisateur vers la page de login s'il n'est aps encore connecté
 * 
 * Par contre on ignore la redirection si la requête est soit login, register ou verifyLogin
 * effectuée avec la méthode GET car ces requêtes sont acceptées pour un utilisateur non connecté
 * - la requête /login avec GET envoie l'utilisateur vers la page de connection (login), ce qui est désiré
 * - la requête /register avec GET, envoie l'utilisateur vers la page d'inscription, qui permet à un utilisteurce qui est désiré
 * - la requête /verifyLogin permet de vérifier, à partir de la page d'inscription, si un nom d'utilisateur est disponible ou non
 * 
 */

@Component
public class LoginInterceptor extends HandlerInterceptorAdapter {
	
	//Logger pour afficher dans la console
	Logger logger =  Logger.getLogger(LoginInterceptor.class.getName());
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
		
		
		logger.log(Level.INFO,"in LoginInterceptor.prehandle =========== > contextPath: " + request.getContextPath());
		logger.log(Level.INFO,"in LoginInterceptor.prehandle =========== > uri: " + request.getRequestURI());
		logger.log(Level.INFO,"in LoginInterceptor.prehandle =========== > uri: " + request.getRequestURI().equals(request.getContextPath() + "/login"));
				
		//obtenir la session
		HttpSession session = request.getSession();
		
		//Obtenir l'utilsateur courant enregistré dans un attribut de session
		User user =  (User)session.getAttribute("user");
		
		logger.log(Level.INFO,"in LoginInterceptor.prehandle =========== > user: " + (user == null ? "user":user.getUsername()));

		String contextPath =  request.getContextPath();
		String registerPath = contextPath + "/register";
		String loginPath = contextPath + "/login";
		String verifyLoginPath =  contextPath + "/verifyLogin";
		
		logger.log(Level.INFO,  "URI =========== > requestURI : " + request.getRequestURI());
		
		logger.log(Level.INFO,  "URI =========== > login? : " + request.getRequestURI().equals(loginPath));
		logger.log(Level.INFO,  "URI =========== > register? : " + request.getRequestURI().equals(registerPath));
		logger.log(Level.INFO,  "URI =========== > verifyLogin? : " + request.getRequestURI().startsWith(verifyLoginPath));		

		
		//on vérifie si l'utilisateur n'est pas connecté et que l'url n'est pas '/login',  '/register' ou '/verifyLogin'
		//si l'url est autre chose et que l'utilisateur n'est pas connecté, il sera redirigé vers la page  'login'
		if(   ! ( 	request.getRequestURI().equals(loginPath) || 
					request.getRequestURI().equals(registerPath) || 
					request.getRequestURI().startsWith(verifyLoginPath) 
				)  
				&& (user == null || user.getUsername() == null)) {
		

			logger.log(Level.INFO,"in LoginInterceptor.prehandle =========== > redirecting to: " + contextPath + "/login");
			//si l'utilisteur n'est pas connecté, la variable 'user' est soit à 'null', le  'username' est 'null'
			//on le redirige vers la page de login
			response.sendRedirect(contextPath + "/login");
			return false;
		} 
			
		logger.log(Level.INFO,"in LoginInterceptor.prehandle request =========== >  " + request.getRequestURI() );
		
		return true;
	}

}
