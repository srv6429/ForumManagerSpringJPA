<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>List of Topics</title>
		
		<link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/style.css"/>"  />
		<link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/topics.css"/>"  />
		
		<script type="text/javascript">

			//récupérer l'identifiant de l'utilisateur
			var userId =  "${user.id}";
			//récupérer le context
			var contextPath = "${pageContext.request.contextPath}";

			//récupérer l'identifiant de l'utilisateur courant
			var userId = "${user.id}";

			console.log("contextPath: " + contextPath);
			console.log("userId: " + userId);

		</script>
		
	</head>
	<body id="top">

		<c:set var="request" value="${pageContext.request}" />
		<c:set var="errorMessage" value="${request.getAttribute(\"errorMessage\")}" />	
		
		<c:if test="${user == null || user.username == null}">
			<c:set var="user" value="${request.session.getAttribute(\"user\")}" />			
		</c:if>			
		<%@include file="menu.jsp" %>
		
		<div id="main_container">
		
			<h2>Liste des thèmes de discussion</h2>
			<div id="result"></div>
			<div id="errorMessage" class="error">
				<c:if test="${errorMessage != null }">
					<div class="error">
						<c:out value="${errorMessage}" />
					</div>
				</c:if>
			</div>
			
			<div id="topics_container">
				<div>
					<!-- Formulaire d'jout d'un nouveua topic/post -->
					<div id="new_topic_form" class="topic_form">
						<form>
							<label for="new_topic_title">Thème</label><br />
							<input type="text" id="new_topic_title" size="30"/><br />
							<label for="new_post_title">Titre du commentaire</label><br />
							<input type="text" id="new_post_title" size="30"/><br />
							<label for="new_post_text">Commentaire</label><br />
							<textarea rows="5" id="new_post_text"></textarea>
						</form>
						<div class="button_wrapper" id="new_button_wrapper" style="text-align: left;">
							<button class="new_send_button" id="new_send">Enregistrer</button>
							<button class="new_cancel_button" id="new_cancel">Annuler</button>
						</div>
					</div>
				</div>
				<!-- List des topics -->
				<table id="topics_table">
					<tr>
						<th>
							<div class="topic_header" id="topic_header">
								<span class="topic_num">Id</span>
								<span class="topic_title">Titre</span>
								<span class="topic_creation_date">Date de création</span>
								<span class="topic_update_date">Dernière mise à jour</span>
								<span class="topic_creator"> Créateur (id)</span>
								<span class="topic_editor_button"><button class="new_button" id="add_topic">Ajouter un thème</button></span>
							</div>
						</th>
					</tr>
					<c:forEach items="${topicList}" var="topic">
					<tr>
						<td>
							<div class="topic_wrapper" id="topic_wrapper">
								<div class="topic">
									<span class="topic_num"><c:out value="${topic.id}" /></span>
									<span class="topic_title"><a href="posts/${topic.id}"> ${topic.title}</a></span>
									<span class="topic_creation_date"><c:out value="${topic.creationDate}" /></span>
									<span class="topic_update_date"><c:out value="${topic.updateDate}" /></span>
									<span class="topic_creator"><c:out value="${topic.creatorFK.username} (${topic.creatorFK.id})" /> </span>
									<c:if test="${topic.creatorId  == user.id || user.role == \"A\" }">
										<div class="topic_editor_button button_wrapper visible" id="default_button_wrapper_${topic.id}">
											<button class="update_button" name="update_${topic.id}" id="update_${topic.id}">Modifier</button>
											<button class="delete_button" name="delete_${topic.id}" id="delete_${topic.id}">Retirer</button>
										</div>		
									</c:if>
								</div>							
								<div class="topic_editor">
									<!-- Formulaire d'édition d'un topic -->
									<div id="update_topic_form_${topic.id}" class="topic_form">
										<form>
											<label for="update_topic_title_${topic.id}">Modifier le titre: </label>
											<input type="text" id="update_topic_title_${topic.id}" size="30" value="${topic.title}"/><br />
										</form>
										<div class="update_button_wrapper" id="update_button_wrapper_${topic.id}">
											<button class="send_update" name="send_${topic.id}" id="send_${topic.id}">Enregistrer</button>
											<button class="cancel_update" name="cancel_${topic.id}" id="cancel_${topic.id}">Annuler</button>
										</div>	
									</div>
								</div>
							</div>
						</td>					
					</tr>
					</c:forEach>
				</table>

			</div>
		</div>
		<script type="text/javascript" src="<c:url value="/resources/scripts/jquery-3.4.1.min.js" />" ></script>
		<script type="text/javascript" src="<c:url value="/resources/scripts/main.js" />" ></script>
		<script type="text/javascript" src="<c:url value="/resources/scripts/topics.js" />" ></script>	
	</body>
</html>