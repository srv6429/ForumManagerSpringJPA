<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>List of Topics</title>
		
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
			
			#topics_table {
				width: 960px;
				margin: 5px auto;
				border: 3px solid #006699;
				background: #efefef;
			}
			
			#topics_table tr th, #topics_table tr td {
				border: 0px solid #ccc;
				background: #667e99;
				text-align: left;
				margin: 5px 5px;
				padding: 10px 10px;
			}
			
			#topics_table tr td {
				border: 0px solid #006699;
				background: #efefef;
			}

			
			.topic_num {
				margin: 5px 0px;
				padding: 5px 5px;
				width: 30px;				
			}
						
			.topic_title {
				margin: 5px 0px;
				padding: 5px 5px;
				width: 200px;
			}
						
			.topic_creation_date {
				margin: 5px 0px;
				padding: 5px 5px;
				width: 160px;
			}
						
			.topic_update_date {
				margin: 5px 0px;
				padding: 5px 5px;
				width: 160px;
			}
						
			.topic_creator{
				margin: 5px 0px;
				padding: 5px 5px;
				width: 140px;
			}
			
			.topic_editor_button {
				margin: 5px 0px;
				padding: 5px 5px;
				width: 120px;
			}
			
			#topic_header span, #topic_wrapper span {
				float: left;
				border: 0px solid #ccc;
			}
			
			.topic_form {
				display: none;
				margin: 10px 25px;
			}
			
			.topic_form input {
				width: 200px;
				margin: 10px 0px;
			}
			
			.topic_form textarea {
				width: 600px;
				margin: 10px 0px;
			}
			
						
			.visible {
				display: inline;
			}
			
			.hidden {
				display: none;
			}
			
			.button_wrapper {
				margin: 5px 10px;
				text-align: right;
				width: 100px;
			}
			
			.update_button_wrapper {
				margin: 5px 10px;
				text-align: right;
				width: 200px;
			}
			
			#new_button_wrapper{
				width: 200px;
			}
			
			button {
				margin: 5px 2px;
			}
			
			

		</style>
		
		<script type="text/javascript">
			jQuery(function(){
				//alert("ready");

				//récupérer le contectPath
				var contextPath = "${pageContext.request.contextPath}";
				//récupérer l'identifiant de l'utilisateur
				var userId =  "${user.id}";
				
				//console.log(userId);
				
				//Clic sur le bouton "Modifier" un topic
				jQuery(".update_button").click(function(){

					//récupérer l'identifiant du topic sélectionné
					var topicId = this.id.substr(7);

					console.log("update_button click: topicId: " + topicId);

					//rendre invisibles les boutons d'édition
					jQuery("#default_button_wrapper_"+topicId).removeClass("visible")
					jQuery("#default_button_wrapper_"+topicId).addClass("hidden");
		

					//afficher le formulaire d'édition du topic
					jQuery("#update_topic_form_"+topicId).removeClass("hidden");
					jQuery("#update_topic_form_"+topicId).addClass("visible");
	//				jQuery("#update_button_wrapper_"+topicId).removeClass("hidden");
	//				jQuery("#update_button_wrapper_"+topicId).addClass("visible");
		
					//désactiver le bounton "Ajouter" un topic
					jQuery("#add_topic").attr("disabled","disabled");
		
				});


				//Clic sur le bouton "Annuler" la modification
				jQuery(".cancel_update").click(function(){

					//récupérer l'identifiant du topic
					var topicId = this.id.substr(7); //getting the post id

					console.log("click cancel post id: " + topicId);

					//cacher le formulaire d'édition
					jQuery("#update_topic_form_"+topicId).removeClass("visible");
					jQuery("#update_topic_form_"+topicId).addClass("hidden");
		//			jQuery("#update_button_wrapper_"+topicId).removeClass("visible");
		//			jQuery("#update_button_wrapper_"+topicId).addClass("hidden");

					//afficher les boutons d'édition
					jQuery("#default_button_wrapper_"+topicId).removeClass("hidden");
					jQuery("#default_button_wrapper_"+topicId).addClass("visible");

					//réactiver le bouton "Ajouter"
					jQuery("#add_topic").removeAttr("disabled");
				});

				//Clic sur le bouton "Envoyer" le topic modifié
				jQuery(".send_update").click(function(e){

					//empĉher la soumission du formulaire
					e.preventDefault();

					//récupérer l'identifiant du topic
					var topicId = this.id.substr(5); //getting the post id

					//récupérer le titre modifié
					var topicTitle =  jQuery("#update_topic_title_"+topicId).val();

					console.log("topicId id: " + topicId + "; topicTitle: " + topicTitle);


					//encoder le titre pour le transmettre dans l'url
					var topicTitleEncoded =  topicTitle.replace(/\n/, "<br />");
					topicTitleEncoded =  encodeURIComponent(topicTitleEncoded);

					console.log("title encoded: " + topicTitleEncoded);
					//var url = contextPath +"/topics/updateTopic/"+topicId + "/"+topicTitleEncoded;
					//console.log("url: " + url);

					//transmettre la requête avec la méthode PUT par AJAX
					var request = jQuery.ajax({
						  url: contextPath +"/topics/updateTopic/"+topicId + "/"+topicTitleEncoded,
					//	  url: "updateTopic?topicId="+topicId +"&topicTitle="+topicTitle, //Ok dans params
						  type: "put",
//						  data: {"topicId": topicId, "topicTitle":topicTitle},
						  dataType: "text"
						});

					//Succès: réponse du serveur
					request.done(function(data) {
						console.log("Done: " + data);
						var json = jQuery.parseJSON(data);
						console.log("json status: " + json.status);
					
						var result = data
                        console.log("Success");
                        jQuery("#result").append(jQuery("<div>").html("Success! with method " + this.type));
                     // document.location.href = "blog?op=posts&topic="+topicId; //refresh
                     
                     // succès de la mise à jour
                     	if(json.status && json.status == "ok"){
							//refresh
							console.log("refresh page");
	                     	document.location.href = "topics";
                         }

					});

					//Échec:réponse du serveur
					request.fail(function(jqXHR, textStatus) {
					  console.log( "Request failed: " + textStatus );
					  jQuery("message").html("Une erreur est survenue lors de la tentative de mise à jour.");
					  
					});

									
				});

				//Clis sur le bouton "Retirer" le topic
				jQuery(".delete_button").click(function(){


					var response = confirm("Désirez-vous vraiment retirer cette discussion?");

					if(response == false){
						return;	
					}
					
					//récupérer l'identifiant du topic
					var topicId = this.id.substr(7);

					console.log("click delete topic id: " + topicId );

					//envoi de la requête AJAX par la méthode DELETE
					var request = jQuery.ajax({
						  //url: contextPath+"/topics/deleteTopic?topicId="+ topicId,
						  url: contextPath+"/topics/deleteTopic/"+ topicId,
						  type: "delete",
						 // data: {"topicId": topicId},
						  dataType: "text"
						});

					//Succès: réponse du serveur
					request.done(function(data) {
						console.log("Done: " + data);
						var json = jQuery.parseJSON(data);
					//	console.log("json: " + json);
						//succès de la mise à jour
						if(json.status && json.status == "ok"){
							jQuery("#result").append(jQuery("<div>").html("Success! with method " + this.type));
	                        console.log("Success");
	                        console.log("Success");
	                        //rafraichir la page
	                        document.location.href = "topics";
								
						} else{
							jQuery("#result").append(jQuery("<div>").html("Failure! with method " + this.type));
						}


					});

					//Échec: réponse du serveur
					request.fail(function(jqXHR, textStatus) {
					  console.log( "Request failed: " + textStatus );
					  jQuery("#result").append(jQuery("<div>").html("Failure! with method " + this.type));
					});
					
				});


				//Clic sur "Ajouter" un topic
				jQuery("#add_topic").click(function(){

					
					console.log("click add topic ");
				//	jQuery("#new_post_form").removeClass("hidden");
				//	jQuery("#new_post_form").addClass("visible");
				
					//afficher le formulaire
					jQuery("#new_topic_form").show();

					//désactiver le bouton "Ajouter"
					jQuery("#add_topic").attr("disabled", "disabled");
					//document.location.href="#topic_wrapper";
				
				});

				//Clic sur "Annuler" ajout de topic
				jQuery("#new_cancel").click(function(){

					console.log("click cancel new topic");
					
				//	jQuery("#new_post_form").removeClass("visible");
				//	jQuery("#new_post_form").addClass("hidden");
				
					//Cacher le formulaire
					jQuery("#new_topic_form").hide();

					//réactiver le nouton "Ajouter"
					jQuery("#add_topic").removeAttr("disabled");
					
					document.location.href="#top";
				});


				//Clic sur "Envoyer" nouveau topic/post				
				jQuery("#new_send").click(function(){

					//var userId = jQuery("#new_topic_user_id").val();

					//récupérer les valeurs de schamps
					var topicTitle = jQuery("#new_topic_title").val();
					var postTitle = jQuery("#new_post_title").val();
					var postBody = jQuery("#new_post_text").val();

					console.log("click new send " +"; userId: " + userId + "; topicTitle: " + topicTitle + 
							"; postTitle: " + postTitle + "; postBody: " + postBody);
					
					//vérifier que les champs ne sont pas vides
					if((!userId || (!topicTitle || topicTitle.length == 0) || (!postTitle || postTitle.length == 0) || (!postBody || postBody.length == 0))){

						if(error.length == 0){
							jQuery("#new_topic_form").prepend(jQuery("<div>").attr("id", "error_send_new").addClass("error").css({"font-size": "11pt", "margin":"0px 10px"}).html("Le titre et/ou le texte ne peuvent être vides"));
						}
					} else{


						//encoder le titre du topic
						var topicTitleEncoded =  topicTitle.replace(/\n/, "<br />");
						topicTitleEncoded =  encodeURIComponent(topicTitleEncoded);
						console.log("topic title encoded: " + topicTitleEncoded);

						//encoder le titre du post
						var postTitleEncoded =  postTitle.replace(/\n/, "<br />");
						postTitleEncoded =  encodeURIComponent(postTitleEncoded);
						console.log("post title encoded: " + postTitleEncoded);

						//encoder le commentaire du post
						var postBodyEncoded =  postBody.replace(/\n/, "<br />");
						postBodyEncoded =  encodeURIComponent(postBodyEncoded);
						console.log("post body encoded: " + postBodyEncoded);
						
						jQuery("#error_send_new").remove();
						console.log("before sending request");

						//Envoi de la requête AJAX par la méthode POST
						var request = jQuery.ajax({
							  url: contextPath+"/topics/addTopic/"+topicTitleEncoded+"/"+postTitleEncoded+"/"+postBodyEncoded+"/"+userId,
						//	  url: contextPath+"/topics/addTopic",
							  type: "post",
							//  data: {"userId": userId, "topicTitle": topicTitle, "postTitle": postTitle, "postBody": postBody }, 
							  dataType: "text"
							});

						//Succès: réponse du serveur
						request.done(function(data) {
							console.log("Done with success: " + data);
							var json = jQuery.parseJSON(data);
							console.log("json.status: " + json.status);

							if(json.status && json.status == "ok"){
	                        	console.log("Success");
	                        	jQuery("#result").append(jQuery("<div>").html("Success! with method " + this.type));

	                        	//réinitialiser le formulaire d'ajout
	                        	jQuery("#new_topic_form form input[type=text]").val("");
	                        	jQuery("#new_topic_form form textarea").val("");

		    					document.location.href = "topics";
		                        	

							} else{
								jQuery("#new_topic_form").prepend(jQuery("<div>").attr("id", "error_send_new").addClass("error").html("Une erreur est survenue lors de la tentative d'enregisrer le post."));
							}
//	    					jQuery("#new_topic_form").removeClass("visible");
//	    					jQuery("#new_topic_form").addClass("hidden");


	                        	//cacher le formulaire
		    				jQuery("#new_post_form").hide();
		    					
		    				jQuery("#add_post").removeAttr("disabled");


	    					
						});

						//Échec: réponse du serveur
						request.fail(function(jqXHR, textStatus) {
						  console.log( "Request failed: " + textStatus );
						  jQuery("#new_topic_form").prepend(jQuery("<div>").attr("id", "error_send_new").addClass("error").html("Une erreur est survenue lors de la tentative d'enregisrer le post."));
						  
						});
						
					}
				});
				

				
			});
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
	</body>
</html>