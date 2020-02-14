<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
  <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
	<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<title>List of posts</title>
		<link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/style.css"/>"  />
		<link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/posts.css"/>"  />

		
		<script type="text/javascript">

		//récupérer le context
			var contextPath = "${pageContext.request.contextPath}";
			
			//récupérer l'identifiant du topic
			var topicId = "${topicId}";

			//récupérer l'identifiant de l'utilisateur courant
			var userId = "${user.id}";

			console.log("contextPath: " + contextPath);
			console.log("topicId: " + topicId);
			console.log("userId: " + userId);

		</script>
		
	</head>
	<body>
		<c:set var="request" value="${pageContext.request}" />
		
		<c:if test="${user == null || user.username == null}">
			<c:set var="user" value="${request.session.getAttribute(\"user\")}" />			
		</c:if>			
		
		<%@include file="menu.jsp" %>

		<div id="main_container">
			
			<h2>Liste des commentaires</h2>
			<div id="topic_title"><c:out value="${topicTitle}" /></div>
			<div id="result"></div>
			<div id="posts_container">
				<div>
					<!-- Formulaire d'ajout d'un post -->
					<div id="new_post_form" class="post_form">

						<form>
							<label for="new_post_title">Title</label><br />
							<input type="text" id="new_post_title" size="30"/><br />
							<label for="new_post_text">Body</label><br />
							<textarea rows="5" id="new_post_text"></textarea>
						</form>
						<div class="button_wrapper" id="new_button_wrapper" style="text-align: left;">
							<button class="new_send_button" id="new_send">Envoyer</button>
							<button class="new_cancel_button" id="new_cancel">Annuler</button>
						</div>
					</div>
				</div>
				<table id="posts_table">
					<tr>
						<th>
							<div class="post_header" id="post_header">
								<span class="post_num">Id</span>
								<span class="post_title">Titre</span>
								<span class="post_user">Nom (Id) </span>
								<span class="post_creation_date">Date de création</span>
								<span class="post_update_date">Dernière mise à jour</span>
								<span class="post_button">
									<button class="new_button" id="add_post">Ajouter</button>
								</span>
							</div>
						</th>
					</tr>
					<c:forEach var="post" items="${postList}">
					<tr>
						<td>
							<div class="post_wrapper" id="post_wrapper">
								<div class="post">
									<span class="post_num">${post.id}</span>
									<span class="post_title">${post.title}</span>
									<span class="post_user">${post.userFK.username} (${post.userFK.id})</span>
									<span class="post_creation_date">Créé le: ${post.creationDate}</span>
									<span class="post_update_date">Mis à jour le: ${post.updateDate}</span>
									<!-- Boutons d'édition du post -->
									<c:if test="${post.userFK.id  == user.id || user.role == \"A\" }">
										<div class="button_wrapper post_button visible" id="default_button_wrapper_${post.id}">
											<button class="update_post_button" id="u_${post.id}">Modifier</button>
											<button class="delete_post_button" id="d_${post.id}">Retirer</button>
										</div>		
									</c:if>
								</div><br />
								<div id="post_body_wrapper_${post.id}" class="post_body">${post.body}</div>
								<!-- formulaire d'édition d'un post -->
								<div id="update_post_editor_${post.id}" class="post_form">
									<form>
										<input type="text" id="update_post_title_${post.id}" size="30" value="${post.title}"/><br />
										<textarea rows="5" onchange="" onkeyup="" class="update_post_text" id="update_post_text_${post.id}">${post.body}</textarea>
									</form>
									<div class="update_button_wrapper" id="update_button_wrapper_${post.id}">
										<button class="send_update_button" id="s_${post.id}">Envoyer</button>
										<button class="cancel_update_button" id="c_${post.id}">Annuler</button>
									</div>	
								</div>
							</div>
						</td>		
					</c:forEach>
				</table>
				<div id="output"></div>
			</div>
		</div>
		
		<script type="text/javascript" src="<c:url value="/resources/scripts/jquery-3.4.1.min.js" />" ></script>
		<script type="text/javascript" src="<c:url value="/resources/scripts/main.js" />" ></script>
		<script type="text/javascript" src="<c:url value="/resources/scripts/posts.js" />" ></script>

	</body>
</html>