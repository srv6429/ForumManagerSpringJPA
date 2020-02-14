<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
  <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
	<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<title>List of posts</title>
		<link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/style.css"/>"  />
		<script type="text/javascript" src="<c:url value="/resources/scripts/jquery-3.4.1.min.js" />" ></script>
		<script type="text/javascript" src="<c:url value="/resources/scripts/main.js" />" ></script>
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
			
			#topic_title{
				color: #006699;
				margin: 15px auto;
				padding: 5px 5px;	
				font-size: 20px;
				text-align: left;
				width: 90%;
								
			}
			#posts_container {
				margin: 20px auto;
				padding: 15px;
			}
			
			#posts_table {
				margin: 15px auto;
				background: none;
				border: 1px solid #999;
				border-radius: 3px;
			}
			
			#posts_table  tr {
				background: #efefef;
				margin: 10px 10px;
				padding: 5px;
				border: 1px solid #006699;
			}
			
			#posts_table tr th, #posts_table tr td {
				text-align: left;
				margin: 5px 5px;
				padding: 10px 10px;
				border: 1px solid #006699;
			}
			
			#posts_table tr th {
				background: #667e99;
			}
			
			#posts_table tr td {
				border: 0px solid #006699;
				border-radius: 5px;
			}
			
			#posts_table .post {
				background: none;
				border-top: 1px dotted #006699;
				border-radius: 5px;
				margin: 10px 10px;
			}
			
			.post_body {
				margin: 10px 10px;
				padding: 5px 5px;
				text-align: left;
				color: #999999;
				font-style: italic;	
				border: 0px solid #ccc;	
				width: 90%;
				float: left;	
			}
			.post_num {
				margin: 5px 10px;
				padding: 5px 5px;
				width: 30px;	
			}
			.post_title {
				margin: 5px 10px;
				padding: 5px 5px;
				width: 200px;
			}
			.post_user {
				margin: 5px 10px;
				padding: 5px 5px;
				width: 100px;
			}
			.post_creation_date {
				margin: 5px 10px;
				padding: 5px 5px;
				width: 130px;
			}
			.post_update_date {
				margin: 5px 10px;
				padding: 5px 5px;
				width: 130px;
			}
			
			.post_button {
				margin: 10px 10px;
				padding: 5px 5px;
				width: 150px;
				text-align: end;
			}
			
			#post_header span, #post_wrapper span {
				float: left;
				border: 0px solid #ccc;
			}
			
			
			.button_wrapper {
				margin: 5px 10px;
				text-align: right;
			}
			.post_button button {
				margin: 5px 5px;
			}
			
			
			.post {
				text-align: left;
				font-family: Verdana;
				font-size: 14px;
				font-style: italic;
				color: #006699;
				margin: 10px 10px;
				padding: 0px;
			}
		


			.post_form {
				display: none;
			}
			
			.post_form input {
				width: 200px;
				margin: 10px 0px;
			}
			
			.post_form textarea {
				width: 600px;
				margin: 10px 0px;
			}
			
			.visible {
				display: inline;
			}
			
			.hidden {
				display: none;
			}
			
			#new_post_form {
				margin: 10px 0px;
				border: 0px solid #006699;
				border-radius: 5px;
				padding: 10px;
				display: none;
			}
			#new_post_form .error{
				font-size: 11pt;
				margin: 0px 10px;
			}
			
		</style>
						
		<script type="text/javascript">

			jQuery(function(){
			
//				alert("ready");
				//récupérer le context
				var contextPath = "${pageContext.request.contextPath}";

				console.log("contextPath: " + contextPath);

				
				//récupérer l'idenitifiant du topic
				var topicId = "${topicId}";
				//r.cupérer l'idenitifiant de l'utilisateur courant
				var userId = "${user.id}";

				//console.log("topicId: " + topicId + "; userId: " + userId);

				//clic sur le bouton "Modifier"
				jQuery(".update_post_button").click(function(e){
					//on ne soumet par le formulaire
					e.preventDefault();

					//on récupère l'idenitifiant du post sélectionné
					var postId = this.id.substr(2);

					console.log("click update post id: " + postId);

					//on remplace le contenu du post par le formulaire d'éition
					
					//rednre invisible l'affichage du commentaire
					jQuery("#post_body_wrapper_"+postId).removeClass("visible");
					jQuery("#post_body_wrapper_"+postId).addClass("hidden");

					//rendre invisible le conteneur de boutons 
					jQuery("#default_button_wrapper_"+postId).removeClass("visible")
					jQuery("#default_button_wrapper_"+postId).addClass("hidden");

					//on affiche le formulaire d'édtition
					jQuery("#update_post_editor_"+postId).removeClass("hidden");
					jQuery("#update_post_editor_"+postId).addClass("visible");
				//	jQuery("#update_button_wrapper_"+postId).removeClass("hidden");
				//	jQuery("#update_button_wrapper_"+postId).addClass("visible");
//
					//désactiver le bouton "Ajouter" en haut à droite 
					jQuery("#add_post").attr("disabled","disabled");

					

				});


				//Clic sur le boton "Annuler"
				jQuery(".cancel_update_button").click(function(e){

					//empêcher la soumission du formulaire
					e.preventDefault();

					//récupérer l'identifiant du post sélectionné
					var postId = this.id.substr(2); //getting the post id

					console.log("click cancel post id: " + postId);
					
					//rendre les boutons visibles de mnuveau
					jQuery("#post_body_wrapper_"+postId).removeClass("hidden");
					jQuery("#post_body_wrapper_"+postId).addClass("visible");
					jQuery("#default_button_wrapper_"+postId).removeClass("hidden");
					jQuery("#default_button_wrapper_"+postId).addClass("visible");

					//cacher le formulaire d'édition du post
					jQuery("#update_post_editor_"+postId).removeClass("visible");
					jQuery("#update_post_editor_"+postId).addClass("hidden");

					//réactiver le bout "Ajouter"
					jQuery("#add_post").removeAttr("disabled");
				});

				//Clic sur "Enregistrer" les modifications apportées au post
				jQuery(".send_update_button").click(function(e){

					//empêcher la soumission du formulaire
					e.preventDefault();
					
					//récupérer l'identifiant du post
					var postId = this.id.substr(2); //getting the post id
					
					console.log("postId: " + postId);

					//récupérer le titre et le commentaires modifié(s) 
					var postTitle =  jQuery("#update_post_title_"+postId).val();
					var postBody =  jQuery("#update_post_text_"+postId).val();

					//remplacer les retours de ligne par '<br />'
					var postTitleEncoded =  postTitle.replace(/\n/g, "<br />");

					//encoder le titre i.e. caracètres spéciaux pour les passer dans une url
					postTitleEncoded = encodeURIComponent(postTitleEncoded);
					console.log("postTitleEncoded: " + postTitleEncoded);

					//encoder le commentaire i.e. caracètres spéciaux pour les passer dans une url
					var postBodyEncoded =  postBody.replace(/\n/g, "<br />");
					postBodyEncoded = encodeURIComponent(postBodyEncoded);
					console.log(postBodyEncoded);
					
					console.log("encoded title: " + postTitleEncoded);
					console.log("encoded body: " + postBodyEncoded);

					//var params = "?postId="+postId+"&postTitle="+postTitleEncoded+"&postBody="+postBodyEncoded;

					var url = contextPath+"/posts/updatePost/"+postId+"/"+postTitleEncoded+"/"+postBodyEncoded;
						
					var method = "put";
					var errorMessage = "Une erreur est survenue lors de la tentative de mise à jour.";

					//envoi de la requête par AJAX
					sendAJAXRequest(url, method, errorMessage);
					
/**/
/*
					//transmission par une requête AJAX
					var request = jQuery.ajax({
						  url: contextPath+"/posts/updatePost/"+postId+"/"+postTitleEncoded+"/"+postBodyEncoded,
						  //url: contextPath+"/posts/updatePost" + params, //alternative
						  type: "put",
						//  data: {"postId": postId, "postTitle":postTitle, "postBody": postBody}, //non transmis par la méthode put
						  dataType: "text"
						});


					//Succès de la réponse du serveur
					request.done(function(data) {
						console.log("Done: " + data);
						var jsonData = jQuery.parseJSON(data);
						console.log("jsonData: " + jsonData.status);
                        console.log("Success");
//                       console.log("Success");
						
						//vérifier la réponse (JSON) 
                     	if(jsonData["status"] && jsonData.status == "ok"){
							//succès
							//console.log("refresh page: " + jsonData.post);
							//on rafaîchit la page
							document.location.href = contextPath+"/posts/"+topicId; //refresh
                         } else{
						//échec  de la mise à jour
                            jQuery("#error").append(jQuery("<div>").html("Error  with method " + this.type));
                         }
                     

					});

					//erreur dans la réponse du serveur: problème
					request.fail(function(jqXHR, textStatus) {
					 // console.log( "Request failed: " + textStatus );
					  jQuery("#error").append(jQuery("<div>").html("Une erreur est survenue lors de la tentative de mise à jour."));

					  
					});
*/
/**/
									
				});

				//Clis sur "Retirer" un post
				jQuery(".delete_post_button").click(function(e){

					//empêcher la soumission du formulaire
					e.preventDefault();


					var response = confirm("Désirez-vous vraiment retirer ce commentaire?");
					if(response == false){
						return;	
					}
					

					//récupérer l'identifiant du post sélectionné
					var postId = this.id.substr(2);
					//var topicId = jQuery(".post_topic_id").val();

					console.log("click delete post id: " + postId + "; topic: " + topicId);	

					//var params = "?postId="+postId;

					var url = contextPath+"/posts/deletePost/"+postId;
						
					var method = "delete";
					var errorMessage = "Une erreur est survenue lors de la tentative de mise à jour.";

					//envoi de la requête par AJAX
					sendAJAXRequest(url, method, errorMessage);
					

/**/	
/*
					var request = jQuery.ajax({
						  url: contextPath+"/posts/deletePost/"+postId,
						//  url: contextPath+"/posts/deletePost" + params, //alternative
						  type: "delete",
						//  data: {"postId": postId}, //non transmis par la méthode delete
						  dataType: "text"
						});

					//Succès de la réponse
					request.done(function(data) {
						//console.log("Done: " + data);
						var jsonData = jQuery.parseJSON(data);
						//console.log("jsonData: " + jsonData.status);
						console.log("success: " + jsonData.status);
                       
                        //vérifier le succès de la mise à jour
						if(jsonData["status"] && jsonData.status == "ok"){
							//refresh
							document.location.href = contextPath+"/posts/"+topicId; //refresh
                         } else{
							///mise à jour non effectuée
                            jQuery("#error").append(jQuery("<div>").html("Error  with method " + this.type));
                          }
                     

					});

					//échec: réponse du serveur
					request.fail(function(jqXHR, textStatus) {
						  console.log( "Request failed: " + textStatus );
						  jQuery("#result").append(jQuery("<div>").html("Une erreur est survenue lors de la tentative de mise à jour."));

						});

*/
					/**/					
				});

 
 //*****************************************************************************
				//Clic sur bout "Ajouter" un nouveau post
				jQuery("#add_post").click(function(){

					//var topicId = jQuery(".post_form_topic").val();
					
					console.log("click add post: topicId: " + topicId);
				//	jQuery("#new_post_form").removeClass("hidden");
				//	jQuery("#new_post_form").addClass("visible");
				
					//afficher le formulaire d'Ajout
					jQuery("#new_post_form").show();
					
					//désactiver le bout on d'ajout
					jQuery("#add_post").attr("disabled", "disabled");
				
				});

				//Clic sur le bouton "Annuler" ajout d'un nouveau post
				jQuery("#new_cancel").click(function(){

					//var topicId = jQuery(".post_form_topic").val();

					//console.log("click cancel topic id: " + topicId);
					
				//	jQuery("#new_post_form").removeClass("visible");
				//	jQuery("#new_post_form").addClass("hidden");
				
					//Cacher le formulaire d'ajout
					jQuery("#new_post_form").hide();
					//réactiver le bouton "Ajouter"
					jQuery("#add_post").removeAttr("disabled");
				});

				//Clic sur le bouton "Enregistrer" le nouveua Post
				jQuery("#new_send").click(function(e){

					//empĉher la soumission du formulaire
					e.preventDefault();
				//	var topicId = jQuery(".post_form_topic").val();
				//	var userId = jQuery(".post_form_user_id").val();

					console.log("click new send topic id: " + topicId + "; userId: " + userId);


					//récupérer le titre et le commentaire
					var  postTitle = jQuery("#new_post_title").val();
					var  postBody = jQuery("#new_post_text").val();

					//vérifier que les chaînes ne sont pas vides
					if((!postTitle || postTitle.length == 0  || !postBody || postBody.length == 0)){
						var error = jQuery("#error_send_new");
						
						if(error.length == 0){
							jQuery("#new_post_form").prepend(jQuery("<div>").attr("id", "error_send_new").addClass("error").css({"font-size": "11pt", "margin":"0px 10px"}).html("Le titre et/ou le texte ne peuvent être vides"));
						}
					} else{
						jQuery("#error_send_new").remove();

						//var params = "postTitle="+postTitle+"&postBody="+postBody+"&topicId="+topicId+"&userId="+userId;
						


						console.log("title: " + postTitle + "; body: " + postBody)
	
				//		var postTitle =  jQuery("#update_post_title_"+postId).val();
				//		var postBody =  jQuery("#update_post_text_"+postId).val();
	
						//encoder le titre et le commentaire pour la transmission dans une url
						var postTitleEncoded =  postTitle.replace(/\n/g, "<br />");
						postTitleEncoded = encodeURIComponent(postTitleEncoded);
						console.log("postTitleEncoded: " + postTitleEncoded);
						
						var postBodyEncoded =  postBody.replace(/\n/g, "<br />");
						postBodyEncoded = encodeURIComponent(postBodyEncoded);
						console.log(postBodyEncoded);
						
						console.log("encoded title: " + postTitleEncoded);
						console.log("encoded body: " + postBodyEncoded);


						var url = contextPath+"/posts/addPost/"+postTitleEncoded+"/"+postBodyEncoded+"/"+topicId+"/"+userId;
						
						var method = "post";
						var errorMessage = "Une erreur est survenue lors de la tentative d'ajout du nouveau post.";
						
						sendAJAXRequest(url, method, errorMessage);

/**/
						//***************************
						//enovoyer la requête avec la méthode "post"
/*		
						var request = jQuery.ajax({
							url: contextPath+"/posts/addPost/"+postTitleEncoded+"/"+postBodyEncoded+"/"+topicId+"/"+userId,
							//url: contextPath+"/posts/addPost",
							type: "post",
							//data: {"postTitle":postTitleEncoded, "postBody": postBodyEncoded, "topicId": topicId, "userId": userId},
							dataType: "text"
						});

						//Succès de la réponse du serveur
						request.done(function(data) {
							console.log("Done: " + data);
							var jsonData = jQuery.parseJSON(data);
							console.log("jsonData: " + jsonData.status);
	                        console.log("Success");

	                        //succès de l'ajout
							if(jsonData["status"] && jsonData.status == "ok"){
								//refresh
								console.log("refresh page: " + jsonData.post);
								document.location.href = contextPath+"/posts/"+topicId; //refresh
	                        } else{
		                        //échec de l'ajout dans la bd
								jQuery("#result").append(jQuery("<div>").html("Error  with method " + this.type));
							}
	                     

						});

						//Échec: réponse du serveur
						request.fail(function(jqXHR, textStatus) {
							console.log( "Request failed: " + textStatus );
							jQuery("#result").append(jQuery("<div>").html("Une erreur est survenue lors de la tentative d'ajout du nouveau post."));

						});
*/
					}
				});


			});	
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

	</body>
</html>