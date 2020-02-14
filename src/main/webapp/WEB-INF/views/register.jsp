<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
	<head>
		<meta charset="UTF-8">
		<title>Registration</title>
	
		<link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/style.css"/>"  />
		<script type="text/javascript" src="<c:url value="/resources/scripts/jquery-3.4.1.min.js" />"></script>	
		
		
		<script type="text/javascript">
			//récupérer le context path
			var contextPath = "${pageContext.request.contextPath}";
			console.log("contextPath: "+ contextPath);
			
			jQuery(function(){


				//alert("ready: context path: ");

				//Clic sur le bouton "Vérifier login"
				jQuery("#login_verif").click(function(){

						console.log("Verification login");
						//récupérer le login entré dans le champ de texte
						var login =  jQuery("#username").val();


						//non nul
						if(login && login.length > 0){

							console.log("login: " + login);					

							//Envoi de la requête AJAX avec la méthode GET
							var request = jQuery.ajax({
								  url: contextPath+"/verifyLogin/"+login,
								  type: "get",
								 // data: {"login":login},
								  dataType: "text"
								});

							//Succès: réponse du serveur
							request.done(function(data) {
									console.log("Done: " + data);
									var json = jQuery.parseJSON(data);
									console.log("json: " + json);
									console.log("login: " + json.login);


								  	var messageDiv = jQuery("<div>").attr("id", "login_avail");

								  	//si le login est disopnible: afficcher message
									if(json.login == "free"){
										messageDiv.html(" Ce pseudonyme est disponible").addClass("message").addClass("valid");										
									} else{
										//sinon: aficher un message: non disponible
										messageDiv.html(" Ce pseudonyme n'est pas disponible. Veuillez en choisir un autre").addClass("message").addClass("error");
									}

									//afficher le message
									jQuery("#login_avail").remove();
									jQuery("#login_verif").after(messageDiv.show());

								});

								//Échec: réponse du serveur
								request.fail(function(jqXHR, textStatus) {
								  alert( "Request failed: " + textStatus );
								});
							
							 
						} else{
							//champ username vide
							var messageDiv = jQuery("<span>").addClass("error").attr("id", "error_login").html(" Veuillez entrer un pseudonyme dans le champ r&eacute;serv&eacute;");
							jQuery("#login").after(messageDiv.show());
						}


				});
				

					//Clic sur bouton de soumission
					jQuery("#submit").click(function( event ) {

						
						//event.preventDefault();
						console.log("click soumission");
						
						var valid =  false;
						var message = "";
						var username = jQuery("#username").val();
						var email = jQuery("#email").val();		
						var password = jQuery("#password").val();
						var password2 = jQuery("#password2").val();

						if(!username  || username.length < 4 || username.length > 15){
							message +=  "Le nom d'utilisateur doit contenir entre 4 et 15 caract&egrave;res<br />";
						}
						if(!email && email.length  == 0){
							message +=  "Le courriel est obligatoire<br />";
						}
						if(!password || password.length  < 4 || password.length  > 15){
							message +=  "Le mot de passe doit contenir entre 8 et 15 caract&egrave;res <br />";
						}
						
						if(password  !== password2){
							message +=  "Les deux mots de passe ne concordent pas<br />";
						}
						

						console.log("message: " + message);
						if(message.length > 0){
							jQuery("#message").remove();
							jQuery("#header").after(jQuery("<div>").attr("id", "message").addClass("error").html(message));

							jQuery("#register_form")[0].reset();

							jQuery("#username").val(username);
							jQuery("#email").val(email);
							jQuery("#password").val("");
							jQuery("#password2").val("");
							 event.preventDefault();
						} else{
							console.log("ok");
							jQuery("#message").remove();
							$("#register_form").submit(function(){
								console.log("submit");
								return;
							});
						}
					
					});
				});

			</script>
	</head>
	<body>
	
   		<c:set var="session" value="${pageContext.request.session}"/>
		<c:set var="errMessage" value="${session.getAttribute(\"errorMessage\")}" />
		<c:if test="${user == null}">
			<c:set var="user" value="${session.getAttribute(\"user\")}" />			
		</c:if>	

		<%@include file="menu.jsp" %>	
	
    	<div class="main_container message_container">

			   	
    		  <c:if test="${user != null && user.messageExists()}">
				<div class="error"><c:out value="${user.message}" /></div>
			</c:if>

			<h2>Inscription</h2>
			<div id="header">Veuillez compl&eacute;ter tous les champs</div>
			<form id="register_form" method="post">
				<div>
					<input type="text" id="username" name="username" maxlength="15" value="" />
					<label id="pseudo_label" for="username">Pseudonyme: </label>
					<input id="login_verif" type="button" value="V&eacute;rifier la disponibilit&eacute;"/>
				
				</div>
				<div>
					<input type="email" id="email" name="email" maxlength="60" value="" />
					<label for="email">Courriel: </label>
				</div>
				<div>
					<input type="password" id="password" name="password" maxlength="15" value="" />
					<label for="password">Mot de passe: </label>
				</div> 
				<div>
					<input type="password" id="password2" name="password2" maxlength="15" value=""/>
					<label for="password2">Confirmer le mot de passe: </label>
				</div>
				<div>
					<input id="submit" type="submit" value="Enregistrer" />
				</div>
				
				<div>* Le pseudonyme et le mot de passe doivent contenir entre 4 et 15 caract&egrave;res</div>
				<input type="hidden" name="op" value="register"/>
			</form>
		</div>
		
	</body>
</html>