<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>Profile</title>
		<link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/style.css"/>"  />
		<script type="text/javascript" src="<c:url value="/resources/scripts/jquery-3.4.1.min.js" />" ></script>	
		
		<style>
			.error{
				color: #cc0000;
				font-style: italic;
			}
		</style>
		
		
		<script type="text/javascript">

			jQuery(function(){
				var contextPath = "${pageContext.request.contextPath}";
				console.log("ready: context path: " + contextPath);
				
				jQuery("#profile_form").submit(function( event ) {

					event.preventDefault();

					var valid =  false;
					var message = "";
					var username = jQuery("#username").val();
					var email = jQuery("#email").val();
					var password = jQuery("#password").val();
					var userId =  "${user.id}";
					

					console.log("id: " + userId+ "; username: " + username + "; password: " + password + "; email: " + email);

					if(!username || username.length  < 4 || username.length > 15 ){
						message +=  "Le pseudonyme doit contenir entre 4 et 15 caract&egrave;res <br />";
					}
					if(!password || password.length  < 4 || password.length  > 15){
						message +=  "Le mot de passe doit contenir entre 4 et 15 caract&egrave;res <br />";
					}

			//		console.log("message: " + message);
					//Afficher le message d'erreur
					if(message.length > 0){
						jQuery("#message_error").remove();
						jQuery("#header").after(jQuery("<div>").attr("id", "message_error").addClass("error").html(message));

						jQuery("#profile_form")[0].reset();

						jQuery("#username").html(username);
						jQuery("#email").html(email);
						jquery("#password").html("");

						
					} else{
						console.log("ok");
						//jQuery("#message").remove();
//						console.log(jQuery("#register").attr("action"));

						valid =  true;
					//	$("#register").submit(function(){
					//		return;
					//	});
					}
					console.log("valid ---------> " + valid);
					if(valid){	
//					    return;
						var messageDiv = jQuery("<div>").attr("id", "message");

						var params =  "/"+userId+"/"+username+"/"+email+"/"+password;

						console.log("params: " + params);

						//envoie de la requête AJAX avec la méthode PUT
						var request = jQuery.ajax({
							  url: contextPath+"/profile"+params,
							  type: "put",
							//  data: {"username":username, "password": password, "email": email},
							  dataType: "text"
							});

						//Succès: réponse du serveur
						request.done(function(data) {
								console.log("Done: " + data);
								var json = jQuery.parseJSON(data);
							  	console.log("status: " + json.status);

							  	//afficher le message
								if(json.status == "ok"){
									messageDiv.html("Votre profil aété mis à jour avec succès").addClass("message").addClass("valid");
									jQuery("#message_error").remove();										
								} else{
									messageDiv.html("Un problème est survenu lors de la mise à jour de votre profil").addClass("message").addClass("error");
								}
							
								jQuery("#header").before(messageDiv.show());

							});
						//Échec: réponse du serveur
						request.fail(function(jqXHR, textStatus) {
							 console.log( "Request failed: " + textStatus );
							 messageDiv.html("Un problème est survenu lors de la mise à jour de votre profil").addClass("message").addClass("error");
							 jQuery("#header").before(messageDiv.show());
						});

					}
					});
			});

		</script>
	</head>
	<body>
    	<c:set var="session" value="${pageContext.request.session}"/>			
		<c:if test="${user == null || user.username == null}">
			<c:set var="user" value="${session.getAttribute(\"user\")}" />			
		</c:if>	
		
			
		<%@include file="menu.jsp" %>
	
    	<div class="main_container message_container">


			<h2>Profil</h2>

			<div id="header">Modifier votre profil. Veuillez compl&eacute;ter tous les champs</div>
			<form id="profile_form" method="post">
				<div>
					<input type="text" id="username" name="username" maxlength="12" value="${user.username}" />
					<label id="pseudo_label" for="username">Pseudonyme: </label>
				</div>
				<div>
					<input type="email" id="email" name="email" maxlength="20" value="${user.email}" />
					<label for="email">Courriel: </label>
				</div>
				<div>
					<input type="password" id="password" name="password" maxlength="12" />
					<label for="password">Mot de passe: </label>
				</div> 
				<div>
					<input id="submit" type="submit" value="Enregistrer" />
				</div>
				<input type="hidden" name="op" value="update"/>
			</form>
		</div>
	</body>
</html>