<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>Insert title here</title>
		<link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/style.css"/>"  />
		<script type="text/javascript" src="<c:url value="/resources/scripts/jquery-3.4.1.min.js" />" ></script>
		
		<style type="text/css">
	
			#main_container {
				width: 1000px;
				margin: 30px auto;
				border: 5px #1984b6;
				border-style: double;
				border-radius: 5px;
				text-align: center;
				padding: 5px 5px;
			}
		
			#users_table {
				width: 950px;
				margin: 5px auto;
				border: 3px solid #006699;
				background: #efefef;
			}
			
			#users_table tr th, #users_table tr td {
				border: 0px solid #ccc;
				background: #667e99;
				text-align: left;
				margin: 5px 5px;
				padding: 10px 10px;
			}
			
			#users_table tr td {
				border: 0px solid #006699;
				background: #efefef;
			}
			
			.user_id {
				margin: 5px 10px;
				padding: 5px 5px;
				width: 20px;				
			}
						
			.user_name {
				margin: 5px 10px;
				padding: 5px 5px;
				width: 100px;
				word-wrap: break-word;
			}
			
			.user_email {
				margin: 5px 10px;
				padding: 5px 5px;
				width: 200px;
				word-wrap: break-word;
			}
						
			.user_creation_date {
				margin: 5px 10px;
				padding: 5px 5px;
				width: 90px;
			}
						
			.user_update_date {
				margin: 5px 10px;
				padding: 5px 5px;
				width: 90px;
			}
						
			.user_role {
				margin: 5px 10px;
				padding: 5px 5px;
				width: 20px;
			}
			
			.user_active {
				margin: 5px 10px;
				padding: 5px 5px;
				width: 20px;
			}
			
			
			.topic_editor_button {
				margin: 5px 10px;
				padding: 5px 5px;
				width: 100px;
			}
			
			.button_wrapper{
				margin: 5px 10px;
				padding: 5px;
			}
			
						
			#user_header span, #user_wrapper span {
				float: left;
				border: 0px solid #ccc;
			}
			
			
			.left {
				float: left;
			}
			
			.right {
				float: right;
			}
		</style>
		
		<script type="text/javascript">

			jQuery(function(){

				//alert("ready");
				
				var contextPath = "${pageContext.request.contextPath}";

				//Clic sur le bouton "Activer/Désactiver"
				jQuery(".edit_user_button").click(function(e){

					//Empêcher la soumission du formualire
					e.preventDefault();

					//récupérer l'identifiant de l'utilisateur sélectionné
					var userId = this.id.substr(2);
					//var topicId = jQuery(".post_topic_id").val();

					console.log("click toggle user id: " + userId);	

					//var params = "?postId="+postId+"&userId="+userId;
					
					//envoyer la requêt AJAX avec la méthode PUT
					var request = jQuery.ajax({
						  url: contextPath+"/toggleUser/"+ userId,
						  type: "put",
						//  data: {"postId": postId, "postTitle":postTitle, "postBody": postBody, "topicId": topicId, "userId": userId},
						  dataType: "text"
						});

					//Succès: réponse du serveur
					request.done(function(data) {
						console.log("Done: " + data);
						var jsonData = jQuery.parseJSON(data);
						console.log("jsonData.status: " + jsonData.status);
                        console.log("Success");

                        //Succès de la mise à jour
						if(jsonData["status"] && jsonData.status == "ok"){
							//refresh
							console.log("refresh page: " + jsonData.profile);
							console.log("Profile deleted with success");
							document.location.href = contextPath+"/users"; //refresh
                         } else{
                        	 console.log("Profile error");
                            jQuery("#error").append(jQuery("<div>").html("Error  with method " + this.type));
                          }
                     

					});

					//Échec: réponse su serveur
					request.fail(function(jqXHR, textStatus) {
						  console.log( "Request failed: " + textStatus );

						  jQuery("#error").append(jQuery("<div>").html("Une erreur est survenue lors de la tentative de mise à jour."));

						  
						});
					
				});
			});


		</script>
	</head>
	<body>

		<c:set var="session" value="${pageContext.request.session}" />
		<c:set var="errorMessage" value="${request.getAttribute(\"errorMessage\")}" />	
		
		<c:if test="${user == null || user.username == null}">
			<c:set var="user" value="${session.getAttribute(\"user\")}" />			
		</c:if>			
		<%@include file="menu.jsp" %>

		<div id="main_container">
			<h2>Liste des utilisateurs</h2>
						
			<table id="users_table">
				<tr>
					<th>
						<div class="user_header" id="user_header">
							<span class="user_id">Id</span>
							<span class="user_name">Nom</span>
							<span class="user_email">Courriel</span>
							<span class="user_creation_date">Date de création</span>
							<span class="user_update_date">Dernière mise à jour</span>
							<span class="user_role">Rôle</span>
							<span class="user_active">Actif</span>
							<span class="user_editor_button"></span>
						</div>
					</th>
				</tr>
				<c:forEach items="${userList}" var="userD">
				<tr>
					<td>
						<div class="user_wrapper" id="user_wrapper">
							<div class="topic">
								<span class="user_id"><c:out value="${userD.id}" /></span>
								<span class="user_name">${userD.username}</span>
								<span class="user_email">${userD.email}</span>
								<span class="user_creation_date"><c:out value="${userD.creationDate}" /></span>
								<span class="user_update_date"><c:out value="${userD.updateDate}" /></span>
								<span class="user_role"><c:out value="${userD.role}" /> </span>
								<span class="user_active">${userD.isActive}</span>
								<c:if test="${userD.username != \"admin\"}">
								<span class="button_wrapper right" id="default_button_wrapper_${userD.id}">
									<button class="edit_user_button" id="d_${userD.id}">Activer/Désactiver</button>
								</span>
	
								</c:if>
							</div>							
						</div>
					</td>					
				</tr>
				</c:forEach>
			</table>
		</div>
	</body>
</html>